"""Reduce a Poly Haven scanned tree (glTF + bin) to game LODs.

Leaves (one small mesh per leaf) become one textured quad each (optionally subsampled and enlarged
to keep coverage); twigs and trunk are decimated by vertex clustering; small twig pieces can be
dropped. Writes <out>/<name>_lod<N>.gltf + .bin referencing the original diffuse / normal textures.

usage: tree_reduce.py in.gltf out_dir name lodspec [lodspec ...] [--bloom frac r g b] [--seed n]
lodspec: leafKeep,leafScale,twigCell,twigMinTris,trunkCell   (twigCell 0 = drop twigs entirely)
"""
import json, os, shutil, struct, sys
import numpy as np

CT = {5120: np.int8, 5121: np.uint8, 5122: np.int16, 5123: np.uint16, 5125: np.uint32, 5126: np.float32}
NC = {'SCALAR': 1, 'VEC2': 2, 'VEC3': 3, 'VEC4': 4}


def read_accessor(g, bufs, acc_i):
    a = g['accessors'][acc_i]
    bv = g['bufferViews'][a['bufferView']]
    dt = CT[a['componentType']]; n = NC[a['type']]
    off = bv.get('byteOffset', 0) + a.get('byteOffset', 0)
    stride = bv.get('byteStride', 0)
    item = n * np.dtype(dt).itemsize
    if stride and stride != item:
        raw = np.frombuffer(bufs[bv['buffer']], dtype=np.uint8, count=stride * (a['count'] - 1) + item, offset=off)
        rows = np.lib.stride_tricks.as_strided(raw, shape=(a['count'], item), strides=(stride, 1))
        return rows.copy().view(dt).reshape(a['count'], n)
    return np.frombuffer(bufs[bv['buffer']], dtype=dt, count=a['count'] * n, offset=off).reshape(a['count'], n).copy()


def components(tris, nverts):
    labels = np.arange(nverts)
    while True:
        m = labels[tris].min(axis=1)
        newl = labels.copy()
        for k in range(3):
            np.minimum.at(newl, tris[:, k], m)
        newl = newl[newl]
        if np.array_equal(newl, labels):
            return labels
        labels = newl


def leaves_to_quads(pos, uv, idx, keep, scale, rng, centre):
    """One quad per connected leaf. Returns (pos, nor, uv, idx) float32/uint32."""
    lab = components(idx.astype(np.int64), len(pos))
    uniq, inv = np.unique(lab, return_inverse=True)
    L = len(uniq)
    n = np.bincount(inv, minlength=L).astype(np.float64)
    ok = n >= 3
    # centroid
    c = np.stack([np.bincount(inv, weights=pos[:, k], minlength=L) for k in range(3)], 1) / np.maximum(n, 1)[:, None]
    d = pos - c[inv]
    # covariance per leaf
    cov = np.zeros((L, 3, 3))
    for i in range(3):
        for j in range(i, 3):
            s = np.bincount(inv, weights=d[:, i] * d[:, j], minlength=L)
            cov[:, i, j] = s; cov[:, j, i] = s
    w, v = np.linalg.eigh(cov)  # ascending eigenvalues
    e1 = v[:, :, 2]; e2 = v[:, :, 1]
    p1 = np.einsum('ij,ij->i', d, e1[inv]); p2 = np.einsum('ij,ij->i', d, e2[inv])
    ext1 = np.zeros(L); ext2 = np.zeros(L)
    np.maximum.at(ext1, inv, np.abs(p1)); np.maximum.at(ext2, inv, np.abs(p2))
    # affine fit of uv from (p1, p2): [u] = A [p1 p2 1]
    S = np.zeros((L, 3, 3))
    S[:, 0, 0] = np.bincount(inv, weights=p1 * p1, minlength=L)
    S[:, 0, 1] = S[:, 1, 0] = np.bincount(inv, weights=p1 * p2, minlength=L)
    S[:, 0, 2] = S[:, 2, 0] = np.bincount(inv, weights=p1, minlength=L)
    S[:, 1, 1] = np.bincount(inv, weights=p2 * p2, minlength=L)
    S[:, 1, 2] = S[:, 2, 1] = np.bincount(inv, weights=p2, minlength=L)
    S[:, 2, 2] = n
    S += np.eye(3)[None] * 1e-9
    rhs = np.zeros((L, 3, 2))
    for k in range(2):
        rhs[:, 0, k] = np.bincount(inv, weights=uv[:, k] * p1, minlength=L)
        rhs[:, 1, k] = np.bincount(inv, weights=uv[:, k] * p2, minlength=L)
        rhs[:, 2, k] = np.bincount(inv, weights=uv[:, k], minlength=L)
    A = np.linalg.solve(S, rhs)  # (L,3,2): rows a,b,u0 for u and v
    # subsample
    sel = np.where(ok & (rng.random(L) < keep))[0]
    ext1 = ext1[sel] * scale; ext2 = ext2[sel] * scale
    c = c[sel]; e1 = e1[sel]; e2 = e2[sel]; A = A[sel]
    corners = np.array([[-1, -1], [1, -1], [1, 1], [-1, 1]], dtype=np.float64)
    P = c[:, None, :] + corners[None, :, 0, None] * (ext1[:, None] * e1)[:, None, :] + corners[None, :, 1, None] * (ext2[:, None] * e2)[:, None, :]
    pp1 = corners[None, :, 0] * ext1[:, None]; pp2 = corners[None, :, 1] * ext2[:, None]
    U = A[:, 0, 0][:, None] * pp1 + A[:, 1, 0][:, None] * pp2 + A[:, 2, 0][:, None]
    V = A[:, 0, 1][:, None] * pp1 + A[:, 1, 1][:, None] * pp2 + A[:, 2, 1][:, None]
    # soft normals: half up, half outward from the crown centre
    out = c - centre[None, :]; out[:, 1] *= 0.3
    out /= np.maximum(np.linalg.norm(out, axis=1), 1e-6)[:, None]
    nrm = out * 0.5 + np.array([0, 1, 0])[None, :] * 0.5
    nrm /= np.linalg.norm(nrm, axis=1)[:, None]
    N = np.repeat(nrm[:, None, :], 4, axis=1)
    q = len(sel)
    base = np.arange(q, dtype=np.uint32)[:, None] * 4
    tri = np.concatenate([base + np.array([[0, 1, 2]], dtype=np.uint32), base + np.array([[0, 2, 3]], dtype=np.uint32)], 1).reshape(-1, 3)
    return (P.reshape(-1, 3).astype(np.float32), N.reshape(-1, 3).astype(np.float32),
            np.stack([U, V], -1).reshape(-1, 2).astype(np.float32), tri, q)


def cluster_decimate(pos, nor, uv, idx, cell, min_comp_tris=0):
    if min_comp_tris > 0:
        lab = components(idx.astype(np.int64), len(pos))
        tl = lab[idx[:, 0]]
        uniq, cnt = np.unique(tl, return_counts=True)
        keep_lab = uniq[cnt >= min_comp_tris]
        idx = idx[np.isin(tl, keep_lab)]
    key = np.floor(pos / cell).astype(np.int64)
    _, inv = np.unique(key, axis=0, return_inverse=True)
    inv = inv.reshape(-1)
    K = inv.max() + 1
    cnt = np.bincount(inv, minlength=K).astype(np.float64)
    npos = np.stack([np.bincount(inv, weights=pos[:, k], minlength=K) for k in range(3)], 1) / cnt[:, None]
    nnor = np.stack([np.bincount(inv, weights=nor[:, k], minlength=K) for k in range(3)], 1)
    nnor /= np.maximum(np.linalg.norm(nnor, axis=1), 1e-6)[:, None]
    nuv = np.stack([np.bincount(inv, weights=uv[:, k], minlength=K) for k in range(2)], 1) / cnt[:, None]
    t = inv[idx]
    good = (t[:, 0] != t[:, 1]) & (t[:, 1] != t[:, 2]) & (t[:, 0] != t[:, 2])
    t = t[good]
    t = np.unique(t, axis=0)
    used = np.unique(t)
    remap = np.full(K, -1, dtype=np.int64); remap[used] = np.arange(len(used))
    return (npos[used].astype(np.float32), nnor[used].astype(np.float32), nuv[used].astype(np.float32), remap[t].astype(np.uint32))


def write_gltf(out_path, prims, materials, images):
    """prims: list of dict(name, pos, nor, uv, idx, color|None, material index)."""
    blob = bytearray()
    bufferViews, accessors, meshes_prims = [], [], []

    def add_view(arr, target):
        nonlocal blob
        while len(blob) % 4: blob += b'\0'
        off = len(blob); b = arr.tobytes(); blob += b
        bufferViews.append({'buffer': 0, 'byteOffset': off, 'byteLength': len(b), 'target': target})
        return len(bufferViews) - 1

    def add_acc(arr, target, ctype, typ, minmax=False):
        v = add_view(arr, target)
        a = {'bufferView': v, 'componentType': ctype, 'count': len(arr), 'type': typ}
        if minmax:
            a['min'] = arr.min(0).tolist(); a['max'] = arr.max(0).tolist()
        accessors.append(a)
        return len(accessors) - 1

    for p in prims:
        attrs = {'POSITION': add_acc(p['pos'], 34962, 5126, 'VEC3', True),
                 'NORMAL': add_acc(p['nor'], 34962, 5126, 'VEC3'),
                 'TEXCOORD_0': add_acc(p['uv'], 34962, 5126, 'VEC2')}
        if p.get('color') is not None:
            attrs['COLOR_0'] = add_acc(p['color'], 34962, 5126, 'VEC4')
        meshes_prims.append({'attributes': attrs, 'indices': add_acc(p['idx'].reshape(-1), 34963, 5125, 'SCALAR'), 'material': p['material']})
    bin_name = os.path.splitext(os.path.basename(out_path))[0] + '.bin'
    g = {'asset': {'version': '2.0', 'generator': 'tree_reduce.py (Project July 2024)'},
         'scene': 0, 'scenes': [{'nodes': [0]}], 'nodes': [{'mesh': 0, 'name': os.path.basename(out_path)}],
         'meshes': [{'primitives': meshes_prims}], 'materials': materials,
         'textures': [{'source': i, 'sampler': 0} for i in range(len(images))],
         'samplers': [{'magFilter': 9729, 'minFilter': 9987, 'wrapS': 10497, 'wrapT': 10497}],
         'images': [{'uri': u} for u in images],
         'buffers': [{'uri': bin_name, 'byteLength': len(blob)}], 'bufferViews': bufferViews, 'accessors': accessors}
    with open(out_path, 'w', encoding='utf-8') as f:
        json.dump(g, f)
    with open(os.path.join(os.path.dirname(out_path), bin_name), 'wb') as f:
        f.write(blob)


def main():
    args = sys.argv[1:]
    bloom = None; seed = 7
    if '--bloom' in args:
        i = args.index('--bloom'); bloom = (float(args[i + 1]), np.array([float(args[i + 2]), float(args[i + 3]), float(args[i + 4])])); del args[i:i + 5]
    if '--seed' in args:
        i = args.index('--seed'); seed = int(args[i + 1]); del args[i:i + 2]
    src, out_dir, name, lodspecs = args[0], args[1], args[2], args[3:]
    g = json.load(open(src, encoding='utf-8'))
    base = os.path.dirname(src)
    bufs = [open(os.path.join(base, b['uri']), 'rb').read() for b in g['buffers']]
    os.makedirs(os.path.join(out_dir, 'textures'), exist_ok=True)

    # classify primitives
    prims = {}
    for p in g['meshes'][0]['primitives']:
        mname = g['materials'][p['material']]['name'].lower()
        kind = 'leaves' if 'leaves' in mname else ('twigs' if 'branch' in mname else 'trunk')
        prims[kind] = dict(mat=g['materials'][p['material']],
                           pos=read_accessor(g, bufs, p['attributes']['POSITION']).astype(np.float64),
                           nor=read_accessor(g, bufs, p['attributes']['NORMAL']).astype(np.float64),
                           uv=read_accessor(g, bufs, p['attributes']['TEXCOORD_0']).astype(np.float64),
                           idx=read_accessor(g, bufs, p['indices']).reshape(-1, 3).astype(np.int64))
        # Bake KHR_texture_transform of the base colour texture into the UVs (jME ignores the extension).
        bt = g['materials'][p['material']].get('pbrMetallicRoughness', {}).get('baseColorTexture', {})
        tt = bt.get('extensions', {}).get('KHR_texture_transform')
        if tt:
            uv = prims[kind]['uv']
            prims[kind]['uv'] = np.array(tt.get('offset', [0, 0]))[None, :] + uv * np.array(tt.get('scale', [1, 1]))[None, :]
            print(f"  baked texture transform {tt} into {kind} UVs")
        print(f"{kind}: {len(prims[kind]['idx'])} tris from material {g['materials'][p['material']]['name']}")

    # textures: copy diffuse + normal per material, build material list
    def tex_uri(mat, key):
        pbr = mat.get('pbrMetallicRoughness', {})
        t = pbr.get('baseColorTexture') if key == 'diff' else mat.get('normalTexture')
        if not t: return None
        img = g['images'][g['textures'][t['index']]['source']]['uri']
        dst = os.path.join(out_dir, 'textures', os.path.basename(img))
        if not os.path.exists(dst):
            shutil.copyfile(os.path.join(base, img), dst)
        return 'textures/' + os.path.basename(img)

    images, materials, mat_index = [], [], {}
    for kind in ('trunk', 'twigs', 'leaves'):
        if kind not in prims: continue
        mat = prims[kind]['mat']
        m = {'name': f'{name}_{kind}', 'doubleSided': True, 'pbrMetallicRoughness': {'metallicFactor': 0.0, 'roughnessFactor': 0.9}}
        d = tex_uri(mat, 'diff'); nrm = tex_uri(mat, 'nor')
        if d:
            images.append(d); m['pbrMetallicRoughness']['baseColorTexture'] = {'index': len(images) - 1}
        if nrm:
            images.append(nrm); m['normalTexture'] = {'index': len(images) - 1}
        if kind == 'leaves':
            m['alphaMode'] = 'MASK'; m['alphaCutoff'] = 0.5
        materials.append(m); mat_index[kind] = len(materials) - 1

    allpos = np.concatenate([p['pos'] for p in prims.values()])
    centre = np.array([(allpos[:, 0].min() + allpos[:, 0].max()) / 2, allpos[:, 1].max() * 0.6, (allpos[:, 2].min() + allpos[:, 2].max()) / 2])
    rng = np.random.default_rng(seed)

    for li, spec in enumerate(lodspecs):
        leafKeep, leafScale, twigCell, twigMin, trunkCell = [float(x) for x in spec.split(',')]
        out_prims = []; total = 0
        if 'trunk' in prims:
            t = prims['trunk']
            pos, nor, uv, idx = (t['pos'].astype(np.float32), t['nor'].astype(np.float32), t['uv'].astype(np.float32), t['idx'].astype(np.uint32)) if trunkCell <= 0 else cluster_decimate(t['pos'], t['nor'], t['uv'], t['idx'], trunkCell)
            out_prims.append(dict(pos=pos, nor=nor, uv=uv, idx=idx, material=mat_index['trunk'])); total += len(idx)
            print(f"  lod{li} trunk: {len(idx)} tris")
        if 'twigs' in prims and twigCell > 0:
            t = prims['twigs']
            pos, nor, uv, idx = cluster_decimate(t['pos'], t['nor'], t['uv'], t['idx'], twigCell, int(twigMin))
            out_prims.append(dict(pos=pos, nor=nor, uv=uv, idx=idx, material=mat_index['twigs'])); total += len(idx)
            print(f"  lod{li} twigs: {len(idx)} tris")
        if 'leaves' in prims and leafKeep > 0:
            t = prims['leaves']
            lrng = np.random.default_rng(seed)  # same leaf subset ordering per LOD
            pos, nor, uv, idx, q = leaves_to_quads(t['pos'], t['uv'], t['idx'], leafKeep, leafScale, lrng, centre)
            color = None
            if bloom:
                frac, rgb = bloom
                brng = np.random.default_rng(seed + 1)
                tint = np.ones((q, 4), dtype=np.float32)
                pick = brng.random(q) < frac
                tint[pick, :3] = rgb.astype(np.float32)
                color = np.repeat(tint[:, None, :], 4, axis=1).reshape(-1, 4)
            out_prims.append(dict(pos=pos, nor=nor, uv=uv, idx=idx, color=color, material=mat_index['leaves'])); total += len(idx)
            print(f"  lod{li} leaves: {q} quads, {len(idx)} tris")
        out_path = os.path.join(out_dir, f'{name}_lod{li}.gltf')
        write_gltf(out_path, out_prims, materials, images)
        print(f"  lod{li} total {total} tris -> {out_path} ({os.path.getsize(out_path.replace('.gltf', '.bin'))/1e6:.1f} MB bin)")
    bb_min = allpos.min(0); bb_max = allpos.max(0)
    print(f"source bbox min {bb_min.round(2)} max {bb_max.round(2)} (height {bb_max[1]-bb_min[1]:.2f} m, spread {max(bb_max[0]-bb_min[0], bb_max[2]-bb_min[2]):.2f} m)")


if __name__ == '__main__':
    main()
