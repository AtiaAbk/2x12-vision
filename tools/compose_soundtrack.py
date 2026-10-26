#!/usr/bin/env python3
"""
Project July 2024 — Cinematic Original Soundtrack Composer & Synthesizer
Generates 44.1 kHz, 16-bit stereo PCM WAV files for the adaptive music system:
  1. music_main_theme.wav       (Main Menu / Title Theme)
  2. music_exploration.wav      (DU Campus Summer Exploration)
  3. music_dialogue.wav         (Story & Intimate Character Moments)
  4. music_gathering.wav        (Student Gathering & Rising Unity)
  5. music_tension.wav          (High-Tension & Resistance Pulse)
  6. music_chapter_complete.wav (Chapter Resolution Fanfare)
"""
import os
import struct
import numpy as np

SR = 44100

def note_to_freq(note_str):
    names = ['C', 'C#', 'D', 'Eb', 'E', 'F', 'F#', 'G', 'Ab', 'A', 'Bb', 'B']
    n = note_str.strip()
    if n == 'R': return 0.0
    octave = int(n[-1])
    pitch = n[:-1]
    idx = names.index(pitch)
    midi = 12 + octave * 12 + idx
    return 440.0 * (2.0 ** ((midi - 69) / 12.0))

def env_adsr(length, a=0.04, d=0.1, s=0.7, r=0.2):
    a_n, d_n, r_n = int(a * SR), int(d * SR), int(r * SR)
    s_n = max(0, length - a_n - d_n - r_n)
    e = np.concatenate([
        np.linspace(0, 1, a_n, endpoint=False),
        np.linspace(1, s, d_n, endpoint=False),
        np.full(s_n, s),
        np.linspace(s, 0, r_n)
    ])
    return e[:length] if len(e) >= length else np.pad(e, (0, length - len(e)))

def synth_piano(freq, dur, vel=0.8):
    if freq <= 0: return np.zeros(int(dur * SR))
    n = int(dur * SR)
    t = np.arange(n) / SR
    out = np.zeros(n)
    # Fundamental and decay-per-harmonic
    harmonics = [(1, 1.0, 1.8), (2, 0.6, 2.5), (3, 0.35, 3.5), (4, 0.2, 4.8), (5, 0.12, 6.0), (6, 0.06, 8.0)]
    inharm = 1.0004
    for h, g, dec in harmonics:
        f = freq * h * (inharm ** (h - 1))
        decay = np.exp(-t * dec * (0.8 + 0.3 * (freq / 200.0)))
        out += g * np.sin(2 * np.pi * f * t) * decay
    # Attack hammer transient
    attack_n = min(n, int(0.008 * SR))
    out[:attack_n] += np.random.normal(0, 0.12 * vel, attack_n) * np.linspace(1, 0, attack_n)
    return out * vel

def synth_strings(freq, dur, vel=0.7, vib_hz=5.2, vib_depth=0.012, is_cello=False):
    if freq <= 0: return np.zeros(int(dur * SR))
    n = int(dur * SR)
    t = np.arange(n) / SR
    # Vibrato modulation
    pitch_mod = 1.0 + vib_depth * np.sin(2 * np.pi * vib_hz * t) * np.minimum(1.0, t / 0.3)
    phase = 2 * np.pi * freq * np.cumsum(pitch_mod) / SR
    out = np.zeros(n)
    # Sawtooth/warm string harmonics
    num_harmonics = 10 if is_cello else 14
    for h in range(1, num_harmonics + 1):
        g = (1.0 / (h ** 1.15)) * (0.8 if h % 2 == 0 else 1.0)
        # Formant shaping
        if is_cello and 200 <= freq * h <= 600: g *= 1.4
        elif not is_cello and 1500 <= freq * h <= 3500: g *= 1.3
        out += g * np.sin(h * phase)
    env = env_adsr(n, a=0.15 if is_cello else 0.2, d=0.2, s=0.85, r=0.35)
    return out * env * vel

def synth_bansuri(freq, dur, vel=0.75):
    if freq <= 0: return np.zeros(int(dur * SR))
    n = int(dur * SR)
    t = np.arange(n) / SR
    vib_mod = 1.0 + 0.018 * np.sin(2 * np.pi * 5.0 * t) * np.minimum(1.0, t / 0.4)
    phase = 2 * np.pi * freq * np.cumsum(vib_mod) / SR
    out = np.sin(phase) + 0.45 * np.sin(2 * phase) + 0.18 * np.sin(3 * phase) + 0.08 * np.sin(4 * phase)
    # Breath air noise
    air = np.random.normal(0, 0.08, n) * (1.0 + 0.3 * np.sin(phase))
    env = env_adsr(n, a=0.12, d=0.15, s=0.75, r=0.25)
    return (out * 0.85 + air * 0.15) * env * vel

def synth_percussion(kind, vel=0.8):
    if kind == 'timpani':
        n = int(1.8 * SR)
        t = np.arange(n) / SR
        f0 = 73.42 # D2
        decay = np.exp(-t * 2.8)
        sig = np.sin(2 * np.pi * f0 * t) + 0.4 * np.sin(2 * np.pi * f0 * 1.5 * t) + 0.2 * np.sin(2 * np.pi * f0 * 2.0 * t)
        hit = np.random.normal(0, 0.3, int(0.015 * SR)) * np.linspace(1, 0, int(0.015 * SR))
        sig[:len(hit)] += hit
        return sig * decay * vel
    elif kind == 'dhol_low':
        n = int(0.6 * SR)
        t = np.arange(n) / SR
        f = 95.0 * np.exp(-t * 12.0) + 55.0
        sig = np.sin(2 * np.pi * f * t) * np.exp(-t * 7.5)
        return sig * vel
    elif kind == 'dhol_high':
        n = int(0.35 * SR)
        t = np.arange(n) / SR
        f = 280.0 * np.exp(-t * 18.0) + 140.0
        sig = np.sin(2 * np.pi * f * t) * np.exp(-t * 14.0)
        hit = np.random.normal(0, 0.2, int(0.008 * SR)) * np.linspace(1, 0, int(0.008 * SR))
        sig[:len(hit)] += hit
        return sig * vel
    return np.zeros(int(0.5 * SR))

def apply_stereo_reverb(sig_l, sig_r, mix=0.22):
    n = len(sig_l)
    out_l = sig_l.copy()
    out_r = sig_r.copy()
    # Decorrelated stereo comb delays
    delays_l = [int(0.0297 * SR), int(0.0371 * SR), int(0.0411 * SR), int(0.0437 * SR)]
    delays_r = [int(0.0313 * SR), int(0.0359 * SR), int(0.0425 * SR), int(0.0451 * SR)]
    comb_l = np.zeros(n + max(delays_l) * 4)
    comb_r = np.zeros(n + max(delays_r) * 4)
    comb_l[:n] += sig_l
    comb_r[:n] += sig_r
    for d in delays_l:
        for i in range(0, n, d):
            seg = comb_l[i:i + d]
            comb_l[i + d:i + d + len(seg)] += seg * 0.48
    for d in delays_r:
        for i in range(0, n, d):
            seg = comb_r[i:i + d]
            comb_r[i + d:i + d + len(seg)] += seg * 0.48
    out_l = (1.0 - mix) * sig_l + mix * comb_l[:n]
    out_r = (1.0 - mix) * sig_r + mix * comb_r[:n]
    return out_l, out_r

def write_stereo_wav(path, left, right):
    m = max(np.max(np.abs(left)), np.max(np.abs(right)), 1e-6)
    norm_l = np.clip(left / m * 0.88, -1.0, 1.0)
    norm_r = np.clip(right / m * 0.88, -1.0, 1.0)
    # Interleave 16-bit PCM
    interleaved = np.empty((len(norm_l) * 2,), dtype='<i2')
    interleaved[0::2] = (norm_l * 32767).astype('<i2')
    interleaved[1::2] = (norm_r * 32767).astype('<i2')
    raw = interleaved.tobytes()
    os.makedirs(os.path.dirname(os.path.abspath(path)), exist_ok=True)
    with open(path, 'wb') as f:
        f.write(b'RIFF' + struct.pack('<I', 36 + len(raw)) + b'WAVE')
        f.write(b'fmt ' + struct.pack('<IHHIIHH', 16, 1, 2, SR, SR * 4, 4, 16))
        f.write(b'data' + struct.pack('<I', len(raw)) + raw)
    size_mb = os.path.getsize(path) / (1024 * 1024)
    dur = len(left) / SR
    print(f"  -> Generated {os.path.basename(path)}: {dur:.1f}s, stereo 44.1kHz, {size_mb:.2f} MB")

def crossfade_loop(sig_l, sig_r, cross_sec=2.5):
    c = int(cross_sec * SR)
    head_l, tail_l = sig_l[:c], sig_l[-c:]
    head_r, tail_r = sig_r[:c], sig_r[-c:]
    w = np.linspace(0, 1, c)
    res_l = sig_l[:-c].copy()
    res_r = sig_r[:-c].copy()
    res_l[:c] = head_l * w + tail_l * (1.0 - w)
    res_r[:c] = head_r * w + tail_r * (1.0 - w)
    return res_l, res_r

# ==================== COMPOSITIONS ====================

def compose_main_theme():
    # 76 BPM, D Dorian / D Minor heroic-poignant cinematic theme
    bpm = 76.0
    spb = 60.0 / bpm
    bars = 16
    total_sec = bars * 4 * spb + 4.0
    n = int(total_sec * SR)
    left, right = np.zeros(n), np.zeros(n)
    
    # 1. Piano Chords & Arpeggios (Dm, Bb, F, C, Gm, Dm, A, Dm)
    prog = [
        ('D3', ['D4', 'F4', 'A4']), ('Bb2', ['D4', 'F4', 'Bb4']),
        ('F2', ['C4', 'F4', 'A4']), ('C3', ['C4', 'E4', 'G4']),
        ('G2', ['Bb3', 'D4', 'G4']), ('D3', ['A3', 'D4', 'F4']),
        ('A2', ['A3', 'C#4', 'E4']), ('D3', ['A3', 'D4', 'F4']),
        ('D3', ['F4', 'A4', 'D5']), ('Bb2', ['F4', 'Bb4', 'D5']),
        ('F2', ['A4', 'C5', 'F5']), ('C3', ['G4', 'C5', 'E5']),
        ('Bb2', ['D4', 'F4', 'Bb4']), ('C3', ['E4', 'G4', 'C5']),
        ('D3', ['F4', 'A4', 'D5']), ('D3', ['D4', 'A4', 'D5'])
    ]
    for bar_idx, (bass, chord) in enumerate(prog):
        t_bar = bar_idx * 4 * spb
        idx_bar = int(t_bar * SR)
        # Bass note
        b_sig = synth_piano(note_to_freq(bass), 3.8 * spb, vel=0.7)
        left[idx_bar:idx_bar+len(b_sig)] += b_sig * 0.6
        right[idx_bar:idx_bar+len(b_sig)] += b_sig * 0.6
        # Broken chord notes
        for note_idx, note in enumerate(chord):
            t_note = t_bar + (note_idx + 1) * 0.9 * spb
            idx_n = int(t_note * SR)
            p_sig = synth_piano(note_to_freq(note), 2.2 * spb, vel=0.65)
            pan = 0.35 if note_idx % 2 == 0 else 0.65
            left[idx_n:idx_n+len(p_sig)] += p_sig * (1.0 - pan)
            right[idx_n:idx_n+len(p_sig)] += p_sig * pan

    # 2. String Orchestra (Cello drone + Viola/Violin swells)
    for bar_idx in range(bars):
        t_bar = bar_idx * 4 * spb
        idx_bar = int(t_bar * SR)
        root = prog[bar_idx][0]
        c_sig = synth_strings(note_to_freq(root), 4.2 * spb, vel=0.55, is_cello=True)
        left[idx_bar:idx_bar+len(c_sig)] += c_sig * 0.55
        right[idx_bar:idx_bar+len(c_sig)] += c_sig * 0.55
        # Violin harmony in second half
        if bar_idx >= 8:
            top_note = prog[bar_idx][1][-1]
            v_sig = synth_strings(note_to_freq(top_note), 4.0 * spb, vel=0.45, is_cello=False)
            left[idx_bar:idx_bar+len(v_sig)] += v_sig * 0.7
            right[idx_bar:idx_bar+len(v_sig)] += v_sig * 0.3

    # 3. Bansuri Flute Melodic Theme (Enters at bar 4)
    melody = [
        # Bar 4-7
        ('A4', 1.5), ('Bb4', 0.5), ('C5', 1.0), ('D5', 2.0), ('C5', 1.0), ('Bb4', 1.0), ('A4', 2.0),
        ('G4', 1.0), ('A4', 1.0), ('Bb4', 1.5), ('A4', 0.5), ('G4', 1.0), ('F4', 3.0),
        # Bar 8-11: Ascending Courage
        ('D5', 1.5), ('E5', 0.5), ('F5', 1.5), ('G5', 0.5), ('F5', 1.0), ('E5', 1.0), ('D5', 2.5),
        ('A4', 1.0), ('D5', 1.5), ('C5', 0.5), ('Bb4', 1.0), ('A4', 1.0), ('G4', 3.0),
        # Bar 12-15: Resolute Climax
        ('F4', 1.0), ('G4', 1.0), ('A4', 2.0), ('Bb4', 1.0), ('C5', 1.0), ('D5', 3.0),
        ('E5', 1.0), ('F5', 1.5), ('E5', 0.5), ('D5', 4.0)
    ]
    cur_t = 4 * 4 * spb
    for note, beats in melody:
        dur_s = beats * spb
        m_sig = synth_bansuri(note_to_freq(note), dur_s, vel=0.8)
        idx_m = int(cur_t * SR)
        # Centered with gentle right bias
        left[idx_m:idx_m+len(m_sig)] += m_sig * 0.42
        right[idx_m:idx_m+len(m_sig)] += m_sig * 0.58
        cur_t += dur_s

    # 4. Timpani on landmark downbeats
    for bar_idx in [0, 4, 8, 10, 12, 14]:
        idx_t = int(bar_idx * 4 * spb * SR)
        timp = synth_percussion('timpani', vel=0.7)
        left[idx_t:idx_t+len(timp)] += timp * 0.5
        right[idx_t:idx_t+len(timp)] += timp * 0.5

    left, right = apply_stereo_reverb(left, right, mix=0.25)
    return crossfade_loop(left, right, cross_sec=3.0)

def compose_exploration():
    # 84 BPM, Gentle campus acoustic warmth (G Dorian / D minor)
    bpm = 84.0
    spb = 60.0 / bpm
    bars = 16
    total_sec = bars * 4 * spb + 3.0
    n = int(total_sec * SR)
    left, right = np.zeros(n), np.zeros(n)
    
    # Plucked acoustic guitar / ektara fingerstyle pattern
    patterns = [
        ('G3', ['Bb3', 'D4', 'G4', 'D4']), ('C3', ['G3', 'C4', 'E4', 'C4']),
        ('D3', ['F3', 'A3', 'D4', 'A3']), ('Bb2', ['F3', 'Bb3', 'D4', 'Bb3'])
    ]
    for bar in range(bars):
        root, notes = patterns[bar % len(patterns)]
        t_bar = bar * 4 * spb
        # Bass thumb pluck
        idx_b = int(t_bar * SR)
        b = synth_piano(note_to_freq(root), 2.0 * spb, vel=0.5)
        left[idx_b:idx_b+len(b)] += b * 0.5
        right[idx_b:idx_b+len(b)] += b * 0.5
        # Fingerpicking 8th notes
        for i in range(8):
            t_p = t_bar + i * 0.5 * spb
            idx_p = int(t_p * SR)
            note = notes[i % len(notes)]
            p = synth_piano(note_to_freq(note), 1.2 * spb, vel=0.45)
            pan = 0.3 if i % 2 == 0 else 0.7
            left[idx_p:idx_p+len(p)] += p * (1.0 - pan)
            right[idx_p:idx_p+len(p)] += p * pan

    # Soft ambient warm string pad in background
    for bar in range(0, bars, 2):
        t_pad = bar * 4 * spb
        idx_pad = int(t_pad * SR)
        pad = synth_strings(note_to_freq('D3'), 8.2 * spb, vel=0.3, is_cello=True)
        left[idx_pad:idx_pad+len(pad)] += pad * 0.4
        right[idx_pad:idx_pad+len(pad)] += pad * 0.4

    # Peaceful woodwind motif every 4 bars
    flute_phrases = [
        (4, [('D4', 2.0), ('F4', 1.0), ('G4', 1.0), ('A4', 3.0)]),
        (8, [('Bb4', 1.5), ('A4', 0.5), ('G4', 2.0), ('F4', 3.0)]),
        (12, [('D4', 1.5), ('G4', 1.5), ('F4', 1.0), ('D4', 4.0)])
    ]
    for start_bar, phrase in flute_phrases:
        cur_t = start_bar * 4 * spb
        for note, beats in phrase:
            f_sig = synth_bansuri(note_to_freq(note), beats * spb, vel=0.55)
            idx_f = int(cur_t * SR)
            left[idx_f:idx_f+len(f_sig)] += f_sig * 0.6
            right[idx_f:idx_f+len(f_sig)] += f_sig * 0.4
            cur_t += beats * spb

    left, right = apply_stereo_reverb(left, right, mix=0.20)
    return crossfade_loop(left, right, cross_sec=2.5)

def compose_dialogue():
    # 66 BPM, Intimate contemplative piano & solo cello (leaves dialogue frequencies open)
    bpm = 66.0
    spb = 60.0 / bpm
    bars = 12
    total_sec = bars * 4 * spb + 3.0
    n = int(total_sec * SR)
    left, right = np.zeros(n), np.zeros(n)
    
    prog = [
        ('D3', 'F4'), ('A2', 'E4'), ('Bb2', 'D4'), ('F2', 'C4'),
        ('G2', 'Bb3'), ('D3', 'A3'), ('Eb3', 'G3'), ('A2', 'C#4'),
        ('Bb2', 'D4'), ('C3', 'E4'), ('D3', 'F4'), ('D3', 'D4')
    ]
    for bar, (bass, treble) in enumerate(prog):
        t_bar = bar * 4 * spb
        idx_bar = int(t_bar * SR)
        # Deep expressive piano bass
        p_b = synth_piano(note_to_freq(bass), 3.8 * spb, vel=0.5)
        p_t = synth_piano(note_to_freq(treble), 3.5 * spb, vel=0.45)
        left[idx_bar:idx_bar+len(p_b)] += p_b * 0.55
        right[idx_bar:idx_bar+len(p_b)] += p_b * 0.45
        left[idx_bar:idx_bar+len(p_t)] += p_t * 0.35
        right[idx_bar:idx_bar+len(p_t)] += p_t * 0.65
        # Solo expressive cello melody on alternate bars
        if bar % 2 == 1:
            c = synth_strings(note_to_freq(bass), 3.9 * spb, vel=0.38, is_cello=True)
            left[idx_bar:idx_bar+len(c)] += c * 0.4
            right[idx_bar:idx_bar+len(c)] += c * 0.4

    left, right = apply_stereo_reverb(left, right, mix=0.30)
    return crossfade_loop(left, right, cross_sec=2.5)

def compose_gathering():
    # 96 BPM, Rhythmic cello ostinato, dhol beats, rising solidarity
    bpm = 96.0
    spb = 60.0 / bpm
    bars = 16
    total_sec = bars * 4 * spb + 3.0
    n = int(total_sec * SR)
    left, right = np.zeros(n), np.zeros(n)
    
    # Driving Cello Ostinato (D-D-D-F-D-D-G-F)
    ostinato = ['D3', 'D3', 'D3', 'F3', 'D3', 'D3', 'G3', 'F3']
    for bar in range(bars):
        t_bar = bar * 4 * spb
        for i, note in enumerate(ostinato):
            t_step = t_bar + i * 0.5 * spb
            idx_s = int(t_step * SR)
            ost = synth_strings(note_to_freq(note), 0.45 * spb, vel=0.6, is_cello=True)
            left[idx_s:idx_s+len(ost)] += ost * 0.5
            right[idx_s:idx_s+len(ost)] += ost * 0.5
        
        # Subtle acoustic dhol cadence on beats 1 and 3
        idx_d1 = int(t_bar * SR)
        idx_d3 = int((t_bar + 2 * spb) * SR)
        d_low = synth_percussion('dhol_low', vel=0.7)
        d_hi = synth_percussion('dhol_high', vel=0.55)
        left[idx_d1:idx_d1+len(d_low)] += d_low * 0.6
        right[idx_d1:idx_d1+len(d_low)] += d_low * 0.6
        left[idx_d3:idx_d3+len(d_hi)] += d_hi * 0.4
        right[idx_d3:idx_d3+len(d_hi)] += d_hi * 0.7

    # Ascending solidarity violin lines in bars 8-16
    violin_melody = [
        ('A4', 2.0), ('Bb4', 2.0), ('C5', 2.0), ('D5', 2.0),
        ('E5', 2.0), ('F5', 2.0), ('G5', 2.0), ('A5', 2.0)
    ]
    cur_t = 8 * 4 * spb
    for note, beats in violin_melody:
        v = synth_strings(note_to_freq(note), beats * spb, vel=0.6, is_cello=False)
        idx_v = int(cur_t * SR)
        left[idx_v:idx_v+len(v)] += v * 0.7
        right[idx_v:idx_v+len(v)] += v * 0.3
        cur_t += beats * spb

    left, right = apply_stereo_reverb(left, right, mix=0.22)
    return crossfade_loop(left, right, cross_sec=2.0)

def compose_tension():
    # 112 BPM, Driving resistance pulse, syncopation, dramatic resolve
    bpm = 112.0
    spb = 60.0 / bpm
    bars = 16
    total_sec = bars * 4 * spb + 2.5
    n = int(total_sec * SR)
    left, right = np.zeros(n), np.zeros(n)
    
    # Staccato string ostinato (Dm - Eb - Dm - C#)
    chords = ['D2', 'D2', 'Eb2', 'D2', 'D2', 'C#2', 'D2', 'Eb2']
    for bar in range(bars):
        t_bar = bar * 4 * spb
        for i, root in enumerate(chords):
            t_step = t_bar + i * 0.5 * spb
            idx_s = int(t_step * SR)
            stacc = synth_strings(note_to_freq(root), 0.35 * spb, vel=0.75, is_cello=True)
            left[idx_s:idx_s+len(stacc)] += stacc * 0.5
            right[idx_s:idx_s+len(stacc)] += stacc * 0.5
        # Driving percussion hits
        for beat in range(4):
            idx_p = int((t_bar + beat * spb) * SR)
            p = synth_percussion('timpani' if beat == 0 else 'dhol_low', vel=0.75)
            left[idx_p:idx_p+len(p)] += p * 0.5
            right[idx_p:idx_p+len(p)] += p * 0.5

    left, right = apply_stereo_reverb(left, right, mix=0.20)
    return crossfade_loop(left, right, cross_sec=2.0)

def compose_chapter_complete():
    # 80 BPM, One-shot triumphant orchestral resolution (12 seconds)
    bpm = 80.0
    spb = 60.0 / bpm
    total_sec = 13.5
    n = int(total_sec * SR)
    left, right = np.zeros(n), np.zeros(n)
    
    # Cadence progression: Dm -> Bb -> C -> D Major!
    chords = [
        (0.0, 'D3', ['F4', 'A4', 'D5'], 2.0 * spb),
        (2.0 * spb, 'Bb2', ['F4', 'Bb4', 'D5'], 2.0 * spb),
        (4.0 * spb, 'C3', ['G4', 'C5', 'E5'], 2.0 * spb),
        (6.0 * spb, 'D3', ['F#4', 'A4', 'D5'], 7.0 * spb)  # Resonant triumphant Picardy third D Major!
    ]
    for t_c, bass, treble, dur in chords:
        idx_c = int(t_c * SR)
        b = synth_piano(note_to_freq(bass), dur, vel=0.85)
        left[idx_c:idx_c+len(b)] += b * 0.6
        right[idx_c:idx_c+len(b)] += b * 0.6
        c = synth_strings(note_to_freq(bass), dur, vel=0.75, is_cello=True)
        left[idx_c:idx_c+len(c)] += c * 0.5
        right[idx_c:idx_c+len(c)] += c * 0.5
        for note in treble:
            v = synth_strings(note_to_freq(note), dur, vel=0.7, is_cello=False)
            left[idx_c:idx_c+len(v)] += v * 0.5
            right[idx_c:idx_c+len(v)] += v * 0.5

    timp = synth_percussion('timpani', vel=0.85)
    left[:len(timp)] += timp * 0.5
    right[:len(timp)] += timp * 0.5
    idx_climax = int(6.0 * spb * SR)
    left[idx_climax:idx_climax+len(timp)] += timp * 0.7
    right[idx_climax:idx_climax+len(timp)] += timp * 0.7

    left, right = apply_stereo_reverb(left, right, mix=0.28)
    return left, right

def main():
    target_dir = os.path.join(os.path.dirname(__file__), '..', 'assets', 'Audio')
    print("Project July 2024 — Composing and Synthesizing Original Soundtrack...")
    
    print("[1/6] Composing Main Theme (music_main_theme.wav)...")
    l, r = compose_main_theme()
    write_stereo_wav(os.path.join(target_dir, 'music_main_theme.wav'), l, r)
    
    print("[2/6] Composing Exploration Theme (music_exploration.wav)...")
    l, r = compose_exploration()
    write_stereo_wav(os.path.join(target_dir, 'music_exploration.wav'), l, r)
    
    print("[3/6] Composing Dialogue Theme (music_dialogue.wav)...")
    l, r = compose_dialogue()
    write_stereo_wav(os.path.join(target_dir, 'music_dialogue.wav'), l, r)
    
    print("[4/6] Composing Gathering Theme (music_gathering.wav)...")
    l, r = compose_gathering()
    write_stereo_wav(os.path.join(target_dir, 'music_gathering.wav'), l, r)
    
    print("[5/6] Composing Tension Theme (music_tension.wav)...")
    l, r = compose_tension()
    write_stereo_wav(os.path.join(target_dir, 'music_tension.wav'), l, r)
    
    print("[6/6] Composing Chapter Complete Fanfare (music_chapter_complete.wav)...")
    l, r = compose_chapter_complete()
    write_stereo_wav(os.path.join(target_dir, 'music_chapter_complete.wav'), l, r)
    
    print("Soundtrack composition and synthesis successfully completed!")

if __name__ == '__main__':
    main()
