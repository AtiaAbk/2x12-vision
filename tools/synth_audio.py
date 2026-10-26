"""Synthesises every sound under assets/audio from first principles (numpy only), so the whole
soundbank is original work with documented parameters and no external audio.

usage: python tools/synth_audio.py assets/audio

Files written (44.1 kHz, 16-bit PCM mono WAV):
  ui_click, ui_confirm, ui_back, ui_notify, ui_warning, objective, bell
  footstep_{grass,paving,asphalt}_{1,2,3}
  ambience_campus (40 s seamless loop: wind in leaves, distant traffic, koel-like bird calls,
                   low crowd murmur)
  music_title    (about 60 s seamless loop: drone of plucked strings in D with a flute-like melody,
                  minor scale D Eb F G A Bb C, original tune written for this project)
"""
import os
import struct
import sys
import numpy as np

SR = 44100
rng = np.random.default_rng(2024_07_01)


# ---- helpers -------------------------------------------------------------------------------------

def t_axis(seconds):
    return np.arange(int(seconds * SR)) / SR


def env_exp(n, decay):
    return np.exp(-np.arange(n) / SR * decay)


def env_adsr(n, a=0.005, d=0.05, s=0.6, r=0.1):
    a_n, d_n, r_n = int(a * SR), int(d * SR), int(r * SR)
    s_n = max(0, n - a_n - d_n - r_n)
    e = np.concatenate([
        np.linspace(0, 1, a_n, endpoint=False),
        np.linspace(1, s, d_n, endpoint=False),
        np.full(s_n, s),
        np.linspace(s, 0, r_n),
    ])
    return e[:n] if len(e) >= n else np.pad(e, (0, n - len(e)))


def fft_filter(x, lo=None, hi=None, order=4):
    """Zero-phase Butterworth-shaped band filter in the frequency domain."""
    X = np.fft.rfft(x)
    f = np.fft.rfftfreq(len(x), 1 / SR)
    H = np.ones_like(f)
    if hi is not None:
        H *= 1 / np.sqrt(1 + (f / hi) ** (2 * order))
    if lo is not None:
        with np.errstate(divide='ignore'):
            H *= 1 / np.sqrt(1 + (lo / np.maximum(f, 1e-9)) ** (2 * order))
    return np.fft.irfft(X * H, n=len(x))


def noise(n):
    return rng.standard_normal(n)


def pink(n):
    """Pink-ish noise by summing octave-band filtered white noise."""
    w = noise(n)
    out = np.zeros(n)
    for lo, hi, g in [(20, 80, 1.0), (80, 320, 0.7), (320, 1280, 0.5), (1280, 5120, 0.35), (5120, 16000, 0.25)]:
        out += fft_filter(w, lo, hi, 2) * g
    return out / np.max(np.abs(out) + 1e-9)


def normalize(x, peak=0.9):
    m = np.max(np.abs(x)) + 1e-9
    return x / m * peak


def fade(x, ms_in=5, ms_out=20):
    n_in, n_out = int(ms_in * SR / 1000), int(ms_out * SR / 1000)
    x = x.copy()
    if n_in > 0:
        x[:n_in] *= np.linspace(0, 1, n_in)
    if n_out > 0:
        x[-n_out:] *= np.linspace(1, 0, n_out)
    return x


def make_loop(x, cross_seconds=2.0):
    """Crossfades the tail into the head so the file loops without a click."""
    c = int(cross_seconds * SR)
    head, tail = x[:c], x[-c:]
    w = np.linspace(0, 1, c)
    x = x[:-c].copy()
    x[:c] = head * w + tail * (1 - w)
    return x


def reverb(x, mix=0.25, decay=0.5):
    """Small Schroeder-style reverb: four feedback combs plus two allpasses."""
    out = np.zeros_like(x)
    for d_ms, g in [(29.7, decay), (37.1, decay * 0.96), (41.1, decay * 0.92), (43.7, decay * 0.88)]:
        d = int(d_ms * SR / 1000)
        y = np.zeros(len(x) + d)
        for i in range(0, len(x), d):  # block-wise comb is exact because the delay is one block
            seg = x[i:i + d]
            y[i:i + len(seg)] += seg
            y[i + d:i + d + len(seg)] += y[i:i + len(seg)] * g
        out += y[:len(x)]
    out /= 4
    for d_ms in (5.0, 1.7):
        d = int(d_ms * SR / 1000)
        g = 0.5
        y = np.zeros(len(out) + d)
        for i in range(0, len(out), d):
            seg = out[i:i + d]
            prev = y[i:i + len(seg)]
            y[i:i + len(seg)] = -g * seg + prev
            y[i + d:i + d + len(seg)] += seg + g * y[i:i + len(seg)]
        out = y[:len(out)]
    return x * (1 - mix) + out * mix


def write_wav(path, x):
    x = np.clip(x, -1, 1)
    pcm = (x * 32767).astype('<i2').tobytes()
    with open(path, 'wb') as f:
        f.write(b'RIFF' + struct.pack('<I', 36 + len(pcm)) + b'WAVE')
        f.write(b'fmt ' + struct.pack('<IHHIIHH', 16, 1, 1, SR, SR * 2, 2, 16))
        f.write(b'data' + struct.pack('<I', len(pcm)) + pcm)
    print(f'{os.path.basename(path):28s} {len(x) / SR:6.2f} s  {os.path.getsize(path) / 1e6:5.2f} MB')


def tone(freq, seconds, harmonics=((1, 1.0),), vib_hz=0.0, vib_depth=0.0):
    t = t_axis(seconds)
    phase = 2 * np.pi * freq * t
    if vib_hz:
        phase += vib_depth * np.sin(2 * np.pi * vib_hz * t)
    out = np.zeros_like(t)
    for h, g in harmonics:
        out += g * np.sin(h * phase)
    return out


def sweep(f0, f1, seconds):
    t = t_axis(seconds)
    f = f0 * (f1 / f0) ** (t / seconds)
    phase = 2 * np.pi * np.cumsum(f) / SR
    return np.sin(phase)


# ---- UI sounds -----------------------------------------------------------------------------------

def ui_click():
    n = int(0.045 * SR)
    s = sweep(1400, 500, 0.045) * env_exp(n, 90)
    s += fft_filter(noise(n), 2000, 8000) * env_exp(n, 400) * 0.25
    return fade(normalize(s, 0.6), 1, 8)


def ui_confirm():
    a = tone(660, 0.09, ((1, 1), (2, 0.3), (3, 0.1))) * env_adsr(int(0.09 * SR), 0.004, 0.02, 0.7, 0.04)
    b = tone(880, 0.16, ((1, 1), (2, 0.3), (3, 0.1))) * env_adsr(int(0.16 * SR), 0.004, 0.03, 0.6, 0.09)
    s = np.concatenate([a, b])
    return fade(normalize(reverb(s, 0.15, 0.4), 0.6), 1, 20)


def ui_back():
    a = tone(880, 0.07, ((1, 1), (2, 0.25))) * env_adsr(int(0.07 * SR), 0.003, 0.02, 0.7, 0.03)
    b = tone(560, 0.14, ((1, 1), (2, 0.25))) * env_adsr(int(0.14 * SR), 0.003, 0.03, 0.5, 0.08)
    return fade(normalize(np.concatenate([a, b]), 0.55), 1, 20)


def ui_notify():
    parts = []
    for i, f in enumerate([523.25, 659.25, 783.99]):
        n = int(0.14 * SR)
        parts.append(tone(f, 0.14, ((1, 1), (2, 0.2), (4, 0.05))) * env_adsr(n, 0.003, 0.04, 0.5, 0.08))
    s = np.zeros(int(0.5 * SR))
    for i, p in enumerate(parts):
        o = int(i * 0.09 * SR)
        s[o:o + len(p)] += p
    return fade(normalize(reverb(s, 0.2, 0.45), 0.6), 1, 40)


def ui_warning():
    n = int(0.09 * SR)
    pulse = tone(440, 0.09, ((1, 1), (3, 0.33), (5, 0.2), (7, 0.14))) * env_adsr(n, 0.002, 0.01, 0.8, 0.03)
    s = np.zeros(int(0.32 * SR))
    s[:n] += pulse
    s[int(0.16 * SR):int(0.16 * SR) + n] += pulse
    return fade(normalize(fft_filter(s, None, 6000), 0.6), 1, 20)


def objective():
    n = int(0.9 * SR)
    s = np.zeros(n)
    for f, g in [(523.25, 1.0), (659.25, 0.8), (783.99, 0.7), (1046.5, 0.5)]:
        s += g * tone(f, 0.9, ((1, 1), (2, 0.15))) * env_exp(n, 3.2)
        s += g * 0.3 * tone(f * 1.003, 0.9) * env_exp(n, 2.6)  # slight detune shimmer
    return fade(normalize(reverb(s, 0.3, 0.55), 0.7), 2, 80)


def bell():
    n = int(3.2 * SR)
    s = np.zeros(n)
    for ratio, g, dec in [(1.0, 1.0, 1.1), (2.0, 0.6, 1.6), (2.76, 0.5, 2.2), (3.4, 0.35, 2.8), (4.07, 0.25, 3.5), (5.43, 0.15, 4.5)]:
        s += g * tone(220 * ratio, 3.2) * env_exp(n, dec)
    strike = fft_filter(noise(n), 1500, 6000) * env_exp(n, 60) * 0.4
    return fade(normalize(reverb(s + strike, 0.3, 0.6), 0.75), 1, 200)


# ---- footsteps -----------------------------------------------------------------------------------

def footstep(kind, variant):
    r = np.random.default_rng(100 + variant * 7 + hash(kind) % 50)
    if kind == 'grass':
        n = int(0.11 * SR)
        s = fft_filter(r.standard_normal(n), 300, 1800 + 300 * variant, 2) * env_adsr(n, 0.012, 0.03, 0.35, 0.06)
        s += fft_filter(r.standard_normal(n), 2000, 6000, 2) * env_exp(n, 70) * 0.25  # rustle
        s += tone(70 + 5 * variant, 0.11) * env_exp(n, 40) * 0.5
    elif kind == 'paving':
        n = int(0.075 * SR)
        s = fft_filter(r.standard_normal(n), 1200, 7000, 2) * env_exp(n, 120)  # hard click
        s += tone(95 + 6 * variant, 0.075) * env_exp(n, 45) * 0.9                 # heel thump
        s += fft_filter(r.standard_normal(n), 400, 1500, 2) * env_exp(n, 60) * 0.3
    else:  # asphalt
        n = int(0.085 * SR)
        s = fft_filter(r.standard_normal(n), 500, 3500, 2) * env_exp(n, 80)
        s += tone(82 + 5 * variant, 0.085) * env_exp(n, 42) * 0.8
        s += fft_filter(r.standard_normal(n), 1500, 5000, 2) * env_exp(n, 160) * 0.35
    return fade(normalize(s, 0.55), 1, 15)


# ---- ambience ------------------------------------------------------------------------------------

def koel_call(t0_frac, total_n, out, pitch_base=1000.0, repeats=4):
    """Koel-like rising two-tone whistle, repeated with rising pitch."""
    pos = int(t0_frac * total_n)
    for k in range(repeats):
        dur = 0.55
        n = int(dur * SR)
        f0 = pitch_base * (1.08 ** k)
        t = t_axis(dur)
        f = np.where(t < 0.22, f0, f0 * 1.45)
        f = f * (1 + 0.01 * np.sin(2 * np.pi * 7 * t))
        ph = 2 * np.pi * np.cumsum(f) / SR
        s = (np.sin(ph) + 0.25 * np.sin(2 * ph)) * env_adsr(n, 0.03, 0.05, 0.8, 0.12)
        s = fft_filter(s, 600, 6000)
        end = min(pos + n, total_n)
        if pos < total_n:
            out[pos:end] += 0.22 * s[:end - pos]
        pos += int(0.75 * SR)


def ambience_campus():
    seconds = 42.0
    n = int(seconds * SR)
    t = t_axis(seconds)
    # Wind through leaves: pink noise with two slow LFOs and a bright rustle layer that follows gusts
    gust = 0.55 + 0.45 * np.sin(2 * np.pi * 0.07 * t + 1.0) * np.sin(2 * np.pi * 0.023 * t)
    wind = fft_filter(pink(n), 80, 1200, 2) * gust * 0.35
    rustle = fft_filter(noise(n), 1500, 7000, 2) * np.clip(gust - 0.45, 0, 1) ** 2 * 0.22
    # Distant traffic from Shahbagh: brown noise swells
    brown = np.cumsum(noise(n))
    brown = fft_filter(brown - np.mean(brown), 40, 220, 2)
    brown /= np.max(np.abs(brown)) + 1e-9
    swell = 0.6 + 0.4 * np.sin(2 * np.pi * 0.05 * t + 2.3) * np.sin(2 * np.pi * 0.017 * t + 0.4)
    traffic = brown * swell * 0.28
    # Low crowd murmur: many noise "voices" with syllable-rate amplitude modulation, band limited
    murmur = np.zeros(n)
    for v in range(14):
        rate = 2.5 + rng.random() * 2.5
        am = 0.5 + 0.5 * np.sin(2 * np.pi * rate * t + rng.random() * 6.28)
        am *= 0.5 + 0.5 * np.sin(2 * np.pi * (0.03 + rng.random() * 0.05) * t + rng.random() * 6.28)
        lo = 250 + rng.random() * 300
        murmur += fft_filter(noise(n), lo, lo * 4, 2) * am
    murmur = murmur / (np.max(np.abs(murmur)) + 1e-9) * 0.16
    out = wind + rustle + traffic + murmur
    # Birds: koel calls at a few places, sparrows (short chirps) scattered
    for frac, pb in [(0.08, 980.0), (0.47, 1050.0), (0.78, 1010.0)]:
        koel_call(frac, n, out, pb, repeats=4)
    for _ in range(26):
        pos = int(rng.random() * (n - SR))
        d = 0.06 + rng.random() * 0.05
        m = int(d * SR)
        f0 = 2800 + rng.random() * 1400
        s = sweep(f0, f0 * (0.8 + rng.random() * 0.5), d) * env_adsr(m, 0.005, 0.01, 0.7, 0.03)
        out[pos:pos + m] += 0.07 * s
    out = make_loop(out, 2.5)
    return normalize(out, 0.8)


# ---- music ---------------------------------------------------------------------------------------

def pluck(freq, seconds, brightness=0.5):
    """Karplus-Strong plucked string."""
    n = int(seconds * SR)
    p = int(SR / freq)
    buf = rng.uniform(-1, 1, p)
    buf = fft_filter(buf, None, 3000 + 6000 * brightness, 2)
    out = np.zeros(n)
    for i in range(n):
        out[i] = buf[i % p]
        buf[i % p] = 0.5 * (buf[i % p] + buf[(i + 1) % p]) * 0.996
    return out * env_adsr(n, 0.002, 0.1, 0.9, min(0.5, seconds * 0.4))


def flute(freq, seconds, amp=1.0):
    n = int(seconds * SR)
    t = t_axis(seconds)
    vib = 0.012 * np.sin(2 * np.pi * 5.3 * t) * np.clip((t - 0.15) / 0.3, 0, 1)
    ph = 2 * np.pi * freq * np.cumsum(1 + vib) / SR
    s = np.sin(ph) + 0.35 * np.sin(2 * ph) + 0.08 * np.sin(3 * ph)
    breath = fft_filter(noise(n), freq * 0.8, freq * 6, 2) * 0.06
    e = env_adsr(n, 0.06, 0.1, 0.85, min(0.25, seconds * 0.35))
    return (s + breath) * e * amp


# Scale: D minor-ish (D Eb F G A Bb C), octave 4/5. Note names -> Hz
NOTE = {'D4': 293.66, 'Eb4': 311.13, 'F4': 349.23, 'G4': 392.00, 'A4': 440.00, 'Bb4': 466.16, 'C5': 523.25,
        'D5': 587.33, 'Eb5': 622.25, 'F5': 698.46, 'G5': 783.99, 'A5': 880.00, 'R': 0.0}

# Original melody for this project (beats at 72 bpm, 48 s = 58 beats; two phrases, answer, return)
MELODY = [
    ('A4', 1.5), ('Bb4', 0.5), ('C5', 1.0), ('D5', 2.0), ('C5', 1.0), ('Bb4', 1.0), ('A4', 2.0), ('R', 1.0),
    ('G4', 1.0), ('A4', 1.0), ('Bb4', 1.5), ('A4', 0.5), ('G4', 1.0), ('F4', 2.0), ('R', 2.0),
    ('D5', 1.0), ('Eb5', 1.0), ('D5', 1.0), ('C5', 1.0), ('Bb4', 1.5), ('A4', 0.5), ('G4', 2.0), ('R', 1.0),
    ('F4', 1.0), ('G4', 1.0), ('A4', 2.0), ('Bb4', 1.0), ('A4', 1.0), ('G4', 1.0), ('F4', 1.0), ('D4', 3.0), ('R', 3.0),
    ('A4', 1.0), ('C5', 1.0), ('D5', 2.0), ('F5', 1.0), ('Eb5', 1.0), ('D5', 2.0), ('C5', 1.0), ('A4', 2.0), ('R', 2.0),
    ('G4', 1.0), ('A4', 1.0), ('Bb4', 1.0), ('A4', 1.0), ('G4', 1.0), ('F4', 1.0), ('D4', 4.0), ('R', 2.0),
]


def music_title():
    bpm = 72.0
    beat = 60.0 / bpm
    melody_beats = sum(b for _, b in MELODY)
    seconds = 1.8 + melody_beats * beat + 4.0  # melody plus a tail that the loop crossfade absorbs
    n = int(seconds * SR)
    out = np.zeros(n)
    # Drone: plucked D2, A2, D3 repeating like a tanpura, slightly detuned
    cycle = [(73.42, 0.0), (110.0, 0.9), (146.83, 1.8), (146.83 * 1.002, 2.7)]
    t0 = 0.0
    while t0 < seconds:
        for f, off in cycle:
            pos = int((t0 + off) * SR)
            if pos >= n:
                break
            s = pluck(f, 3.6, 0.35) * 0.28
            end = min(pos + len(s), n)
            out[pos:end] += s[:end - pos]
        t0 += 3.6
    # Pad: soft sine chord D3 + A3, slow swell
    t = t_axis(seconds)
    pad = (np.sin(2 * np.pi * 146.83 * t) + 0.7 * np.sin(2 * np.pi * 220.0 * t) + 0.3 * np.sin(2 * np.pi * 293.66 * t))
    pad *= 0.05 * (0.6 + 0.4 * np.sin(2 * np.pi * 0.03 * t))
    out += pad
    # Melody
    pos = int(1.8 * SR)
    for name, beats in MELODY:
        dur = beats * beat
        if pos >= n:
            break
        if name != 'R':
            s = flute(NOTE[name], dur * 0.95, 0.32)
            end = min(pos + len(s), n)
            out[pos:end] += s[:end - pos]
        pos += int(dur * SR)
    out = reverb(out, 0.35, 0.65)
    out = make_loop(out, 3.0)
    return normalize(out, 0.75)


def main():
    out_dir = sys.argv[1] if len(sys.argv) > 1 else 'assets/audio'
    os.makedirs(out_dir, exist_ok=True)
    w = lambda name, x: write_wav(os.path.join(out_dir, name + '.wav'), x)
    w('ui_click', ui_click())
    w('ui_confirm', ui_confirm())
    w('ui_back', ui_back())
    w('ui_notify', ui_notify())
    w('ui_warning', ui_warning())
    w('objective', objective())
    w('bell', bell())
    for kind in ('grass', 'paving', 'asphalt'):
        for v in (1, 2, 3):
            w(f'footstep_{kind}_{v}', footstep(kind, v))
    w('ambience_campus', ambience_campus())
    w('music_title', music_title())


if __name__ == '__main__':
    main()
