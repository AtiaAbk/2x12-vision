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
