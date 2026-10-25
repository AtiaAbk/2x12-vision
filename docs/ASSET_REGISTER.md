# Asset Register — Project July 2024 (jMonkeyEngine build)

Every external asset used by the current code is listed here. "Publicly viewable" is not a licence;
an asset is added only with a licence that permits redistribution and modification. Legacy
LibGDX-era registers under `docs/research/` are not authoritative for this build.

Last updated: 2026-10-25

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

## Fonts

| Asset | Files | Creator | Licence | Notes |
|---|---|---|---|---|
| Noto Sans Bengali (variable) | `assets/fonts/NotoSansBengali-Variable.ttf`, `LICENSE-NotoSansBengali.txt` | Google / Noto Project | SIL Open Font License 1.1 | Used for all HUD and in-world text. |
| Liberation Sans | `assets/fonts/LiberationSans-Regular.ttf`, `-Bold.ttf`, `LICENSE-LiberationSans.txt` | Red Hat / Ascender | SIL Open Font License 1.1 | Fallback for Latin and symbol glyphs. |

## Audio

| Asset | Files | Origin | Licence | Status |
|---|---|---|---|---|
| UI and gameplay effects | `assets/audio/*.wav` (10 files; 4 referenced by `AudioController`) | Legacy LibGDX-era docs (`docs/audio_design.md`) state they were synthesised procedurally in-project. Not independently verified. | Unrecorded | **Provenance must be confirmed or the files regenerated before release.** |

## Not yet sourced

- Dressed student character (shirt, trousers, backpack) compatible with the UAL rig.
- Campus architecture models and PBR textures.
- Vegetation models or billboards.
- Original music, ambience, voice.
