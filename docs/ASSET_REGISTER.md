# Asset Register — Project July 2024 (jMonkeyEngine build)

Every external asset used by the current code is listed here. "Publicly viewable" is not a licence;
an asset is added only with a licence that permits redistribution and modification. Legacy
LibGDX-era registers under `docs/research/` are not authoritative for this build.

Last updated: 2026-10-26

## 3D models

| Field | Value |
|---|---|
| Asset | Universal Animation Library (Standard / free tier), rigged mannequin "Mannequin" with 46 animation clips |
| Files in repo | `assets/Models/Characters/UAL/AnimationLibrary_Godot_Standard.gltf` (2,459,987 bytes), `AnimationLibrary_Godot_Standard.bin` (1,587,224 bytes), `LICENSE` (CC0 1.0 legal code), `README_mirror.md` |
| Creator | Quaternius (https://quaternius.com) |
| Official page | https://quaternius.itch.io/universal-animation-library — page states "Asset license: Creative Commons Zero v1.0 Universal" (checked 2026-10-25) |
| Obtained from | https://github.com/J-Ponzo/gltf-universal-animation-library (glTF-only mirror of the Standard zip as distributed on itch.io on 2025-06-10; repository licence CC0-1.0; commit on `main` as of 2026-10-25) |
| Licence | CC0 1.0 Universal (public domain dedication) |
| Attribution | Not required. Credit given voluntarily: "Character rig and animations: Universal Animation Library by Quaternius (CC0)". |
| Modification permitted | Yes |
| Commercial use permitted | Yes |
| Restrictions | None under CC0. The mirror is not the author's own distribution; the licence is the author's CC0 statement on itch.io. |
| Modifications made | None to the files. At runtime the glTF PBR materials are replaced with Phong materials and the model is scaled to 1.72 m (`PlayerMesh.setupModel`). |
| Used for | Arif and the three companions (`character/PlayerMesh.java`, `character/CompanionNPC.java`) |
| Clips used | `Idle_Loop`, `Idle_Talking_Loop`, `Walk_Loop`, `Jog_Fwd_Loop`, `Sprint_Loop`, `Crouch_Idle_Loop`, `Crouch_Fwd_Loop`, `Jump_Loop` |
| Status | Integrated 2026-10-25. **Interim character**: human proportions and animation, but a faceless unclothed mannequin. A dressed student model on the same rig is still required to meet the visual gate. |
| Placeholder | Yes, labelled as such in code and docs |

| Field | Value |
|---|---|
| Asset | Ultimate Modular Men Pack and Ultimate Modular Women Pack: clothed low-poly characters, each with its own rig and 24 animation clips (Idle, Idle_Neutral, Walk, Run, Run_Back/Left/Right, Wave, Interact, Roll, Punch, Kick, HitRecieve, Death, gun and sword poses) |
| Files in repo | `assets/Models/Characters/QuaterniusModular/Men/Casual_2.gltf` (3,058,092 bytes), `Casual_Hoodie.gltf` (3,104,001), `Suit.gltf` (3,315,342), `License.txt`, `How_To_Use.txt`; `assets/Models/Characters/QuaterniusModular/Women/Casual.gltf` (3,131,788), `License.txt`. Buffers are embedded (base64); no external textures, colours are material factors and vertex colours. |
| Creator | Quaternius (https://quaternius.com) |
| Official pages | https://quaternius.com/packs/ultimatemodularcharacters.html (Men, February 2022) and https://quaternius.com/packs/ultimatemodularwomen.html (Women); both pages state "License CC0" (checked 2026-10-25) |
| Obtained from | The Google Drive folders linked from those pages: Men `1USAAquX2JJWuA2m6zol0KUkFe3UkZ8zX` → Individual Characters → glTF; Women `1720N9IGyQHXYvtvZJzazhxtTTlz-y2Vf` → Individual Characters → glTF. The packs' own `License.txt` (committed beside the models) states CC0 1.0 Universal. |
| Licence | CC0 1.0 Universal (public domain dedication) |
| Attribution | Not required. Credit given voluntarily: "Characters: Ultimate Modular Men / Women packs by Quaternius (CC0)". |
| Modification permitted | Yes |
| Commercial use permitted | Yes |
| Restrictions | None under CC0 |
| Modifications made | None to the files. At runtime the glTF PBR materials are replaced with Phong materials keeping the authored base colours; the shirt and trouser materials named in `CharacterSpec` are tinted per character; the model is scaled to 1.72 m. |
| Used for | Arif (`Men/Casual_2`), Rafi (`Men/Casual_Hoodie`), Tanvir (`Men/Suit`), Nusrat (`Women/Casual`) via `character/CharacterSpec.java` and `character/PlayerMesh.java` |
| Clips used | `Idle`, `Walk`, `Run` (also for sprint at a higher rate), `Idle_Neutral` (crouch idle), `Walk` (crouch move). No jump clip exists; the take-off clip keeps playing while airborne. |
| Status | Integrated 2026-10-25. Clothed, with faces and hair; stylized low-poly rather than realistic. Current character of record. |
| Placeholder | No, but the art style is stylized; a realistic character remains a later ambition. |

Not integrated but downloaded and checked on 2026-10-25: Quaternius *Universal Base Characters* (Standard tier, CC0). The free tier contains only "Superhero" male and female bodies with skin textures and dark briefs, no clothing, and an Unreal-named rig; it was rejected for this project and is not in the repository.

| Field | Value |
|---|---|
| Asset | "Island Tree 02" and "Jacaranda Tree", photogrammetry tree scans |
| Files in repo | `assets/Models/Trees/island_tree_02/{krishnachura,radhachura,shade}_lod{0,1,2}.gltf` + `.bin`, `textures/island_tree_02{,_branches,_leaves}_{diff,nor_gl}_1k.jpg`; `assets/Models/Trees/jacaranda_tree/jarul_lod{0,1,2}.gltf` + `.bin`, `textures/jacaranda_tree_{trunk,branches,leaves}_{diff,nor_gl}_1k.jpg`; `assets/Models/Trees/LICENSE.txt`. About 39 MB in total. |
| Creator | Island Tree 02: Rob Tuytel (scanning, processing), Rico Cilliers (cleanup, processing). Jacaranda Tree: Rico Cilliers (all), Rob Tuytel (guidance). Published by Poly Haven. |
| Official pages | https://polyhaven.com/a/island_tree_02 and https://polyhaven.com/a/jacaranda_tree (author and licence fields read from the Poly Haven API `/info/<id>` on 2026-10-25) |
| Obtained from | Poly Haven download API, glTF at 1K texture resolution (`dl.polyhaven.org/file/ph-assets/Models/gltf/1k/<id>/<id>_1k.gltf` with its `.bin` and `textures/`). |
| Licence | CC0 1.0 Universal (https://polyhaven.com/license) |
| Attribution | Not required. Credit given voluntarily: "Trees: Island Tree 02 and Jacaranda Tree by Poly Haven (CC0)". |
| Modification permitted | Yes |
| Commercial use permitted | Yes |
| Restrictions | None under CC0 |
| Modifications made | Heavy. The scans are 1.07 M and 3.86 M triangles. `tools/tree_reduce.py` (numpy, in repo) replaces every scanned leaf mesh with one textured quad fitted by principal-component plane and an affine UV fit, subsamples leaves per level of detail while enlarging the remaining ones, decimates twigs and trunk by vertex clustering, bakes the branches' `KHR_texture_transform` into the UVs, tints a fraction of leaf quads with a vertex colour as a blossom stand-in, and writes three glTF levels (island: 94 k / 31 k / 6 k triangles; jacaranda: 150 k / 40 k / 8 k). The `_arm` (roughness) textures are dropped; at runtime PBR materials are replaced by Phong materials with alpha discard for leaves (`world/TreeLibrary.java`). |
| Used for | All campus trees in `CampusWorld.buildVegetation` and the Hakim Chattar tree (`buildBanyanTree`). |
| Status | Integrated 2026-10-25. **Species stand-ins**: the island tree (an unidentified 3.4 m broadleaf scan) is scaled to 9-12 m for krishnachura, radhachura and plain shade trees and to 15 m for the banyan; the jacaranda stands in for jarul (both have purple blossom). None is a model of the named species; see `docs/REFERENCE_LEDGER.md` section 6. |
| Placeholder | Partly: real licensed tree models, but not the right species. |

## Textures

| Field | Value |
|---|---|
| Asset | Six ambientCG PBR texture sets, 1K JPG tier: `Bricks059`, `Concrete030`, `Asphalt010`, `Grass004`, `PavingStones070`, `PaintedPlaster017` |
| Files in repo | `assets/Textures/<id>/<id>_Color.jpg` and `<id>_NormalGL.jpg` for each set (12 files, 1024x1024 JPG), plus `assets/Textures/LICENSE.txt` |
| Creator | ambientCG (Lennart Demes), https://ambientcg.com |
| Official pages | https://ambientcg.com/a/Bricks059, https://ambientcg.com/a/Concrete030, https://ambientcg.com/a/Asphalt010, https://ambientcg.com/a/Grass004, https://ambientcg.com/a/PavingStones070, https://ambientcg.com/a/PaintedPlaster017 |
| Obtained from | The `1K-JPG.zip` download of each page on 2026-10-25; only the Color and NormalGL maps were copied into the repo (roughness, displacement and AO maps discarded to keep the repo small) |
| Licence | CC0 1.0 Universal, stated at https://ambientcg.com/content/license |
| Attribution | Not required. Voluntary credit in `assets/Textures/LICENSE.txt`. |
| Modification permitted | Yes |
| Commercial use permitted | Yes |
| Used for | Level 01 ground, roads, footpaths, brick and concrete surfaces (`world/CampusWorld.java`), tiled with repeat wrapping and OpenGL-convention normal maps |
| Status | Integrated 2026-10-25 |

## Fonts

| Asset | Files | Creator | Licence | Notes |
|---|---|---|---|---|
| Noto Sans Bengali (variable) | `assets/fonts/NotoSansBengali-Variable.ttf`, `LICENSE-NotoSansBengali.txt` | Google / Noto Project | SIL Open Font License 1.1 | Used for all HUD and in-world text. |
| Liberation Sans | `assets/fonts/LiberationSans-Regular.ttf`, `-Bold.ttf`, `LICENSE-LiberationSans.txt` | Red Hat / Ascender | SIL Open Font License 1.1 | Fallback for Latin and symbol glyphs. |

## Audio

| Asset | Files | Origin | Licence | Status |
|---|---|---|---|---|
| Soundbank (18 files) | `assets/audio/ui_click.wav`, `ui_confirm`, `ui_back`, `ui_notify`, `ui_warning`, `objective`, `bell`, `footstep_{grass,paving,asphalt}_{1,2,3}`, `ambience_campus` (39.5 s loop), `music_title` (58.6 s loop); 8.9 MB, 44.1 kHz 16-bit mono | Synthesised from first principles by `tools/synth_audio.py` (numpy) on 2026-10-26 for this project: sine/FM tones with envelopes, filtered noise, Karplus-Strong plucked strings, a flute-like voice playing an original melody written for the project, a small Schroeder reverb. Every parameter is in the script; re-running it regenerates identical files. The ten unverified LibGDX-era WAVs were deleted. | Original work of the project (no third-party material) | Integrated. Limits: the ambience has no real recordings (wind, traffic, koel-like calls and crowd murmur are all synthetic); the music is a placeholder-quality procedural cue; no Bangla voice. |

## Not yet sourced

- Dressed student character (shirt, trousers, backpack) compatible with the UAL rig.
- Campus architecture models (the landmarks are built from primitives in code; surface textures are sourced, see Textures).
- Species-correct tree models (krishnachura, jarul, radhachura, banyan); the current trees are CC0 stand-ins (see 3D models).
- Original music, ambience, voice.
