# Project July 2024 — Phase 0 Status Matrix

**Date:** 2026-10-25
**Branch:** `feature/vertical-slice-fixes` (created from `main` at `47c0a36`)
**Audit machine:** Windows 11 x64, OpenJDK 17.0.18, Gradle 9.6.1 (wrapper)
**Target machine (per spec):** MacBook Air M4, 16 GB, 1920×1080 @ 60 FPS

This matrix records what was **directly inspected** on this date. Status values:
`working` (verified by build, test or run here), `partial`, `broken`, `missing`, `placeholder`,
`unverified` (code exists but behaviour not confirmed by a run on this machine), `stale`
(documentation that describes a previous codebase).

## 1. Toolchain and build

| Requirement | Evidence | Status | Next action |
|---|---|---|---|
| Engine is jMonkeyEngine (Java) | `game/build.gradle`: jme3-core/desktop/lwjgl3/plugins/effects/terrain `3.7.0-stable`, LWJGL `3.3.3` | working | — |
| Gradle wrapper usable | `./gradlew --version` → Gradle 9.6.1, Launcher JVM 17.0.18 | working | — |
| Project compiles | `./gradlew :game:compileJava :game:test` → `BUILD SUCCESSFUL in 27s` | working | — |
| Unit tests run | `game/build/test-results/test/TEST-bd.july2024.ProjectJulyJmeTest.xml`: tests=7 skipped=0 failures=0 errors=0 | working | Tests cover mission tracker, save manager, dialogue data and text rasterizer only. No rendering or input tests. |
| Game launches on this machine | On `main`, natives were `natives-macos-arm64` only and `jvmArgs` included the macOS-only `-XstartOnFirstThread`. This branch selects natives and JVM args by OS. `TEST_EXIT_FRAMES=300 ./gradlew :game:run` → 300 frames rendered, screenshot written, exit 0. | working (fixed in this branch) | — |
| Game launches on target Mac | Not testable from this machine. The macOS path in `build.gradle` is unchanged in behaviour. | unverified | Owner runs `./gradlew :game:run` on the Mac. |
| Resolution 1920×1080 | `ProjectJulyApp.main` hard-codes 1280×720. | partial | Make resolution configurable; test 1080p on target. |
| Patched engine internals | `game/src/main/java/com/jme3/{renderer,system}/lwjgl/*`: 4 files, 1,693 lines shadowing jME 3.7 classes, no header explaining the change. | unverified | Diff against upstream 3.7.0-stable; document or remove. |
| Stale LibGDX settings | `gradle.properties` still defines `gdxVersion`, `lwjgl3Version=3.4.1`, `enableGraalNative`; `assets/libgdx.png`, `assets/visuals.properties`, `assets/shaders/*` are not referenced by jME code. | stale | Remove or document as legacy. |

## 2. Player, camera, controls

| Requirement | Evidence | Status | Next action |
|---|---|---|---|
| Third-person orbit camera | `camera/CinematicOrbitCamera.java`: spring arm, pitch clamp, ray–AABB clipping, ground clamp | unverified | Verify by hand; check clipping inside library colonnade. |
| WASD + mouse look | `ProjectJulyApp.initInputs` maps W/A/S/D, arrows, mouse axes | unverified | Verify by hand. |
| Sprint / jump / crouch | `character/PlayerControl.java`: walk 4.2 m/s, sprint 7.6 m/s, jump, crouch, gravity | unverified | Verify by hand. |
| Stamina soft limit | `PlayerControl`: drain 28/s, recover 20/s, sprint blocked below 5 | implemented | — |
| Water / food restore, inventory | No inventory class, no items in world. | missing | Design and implement after the visual gate. |
| Interaction key E | `handleInteraction()`: companions (3.8 m), plaque (3.2 m), rally (4.5 m) | unverified | Verify by hand. |
| Collision | AABB list from `CampusWorld.getColliders()` used by player and camera | unverified | Verify by hand. |
| Controls doc | `CONTROLS.md` describes the LibGDX build (dodge, combos, double jump) that does not exist in the jME code. | stale | Rewrite from `initInputs`. |

## 3. Missions, save, UI

| Requirement | Evidence | Status | Next action |
|---|---|---|---|
| Main objective loop | `level/MissionTracker.java`: 6 linear objectives OBJ_01_RAFI … OBJ_06_COMPLETE; unit-tested | implemented | — |
| Optional side missions | None. | missing | Later milestone. |
| Checkpoint save / load | `level/SaveManager.java` → `save/checkpoint.json`; S in pause menu saves; load on start; unit-tested | implemented | `save/` is not in `.gitignore`. |
| Autosave | Not implemented. | missing | — |
| Adaptive difficulty | Not implemented. | missing | — |
| Pause menu (ESC) | `ui/PauseMenu.java`; R restart, Q quit | unverified | Verify by hand. |
| Main menu | `GameState` enum exists but `state` is always `CAMPAIGN`; no menu is rendered. | missing | — |
| HUD | `ui/ProjectJulyHUD.java`: objective text (BN+EN), compass heading, distance, stamina, context prompt; visible in baseline screenshot | working | — |
| Map / markers | Only heading and distance text. `assets/maps/campus_map.png` exists but is unused by jME code. | partial | — |
| Bengali text shaping and orientation | `ui/BengaliTextRenderer.java` rasterizes Noto Sans Bengali via Java2D. Baseline screenshot on this machine and the committed screenshot from `47c0a36` both show upright, readable conjuncts. The owner's "mirrored text" screenshot (`project_july_level01_scene.png`, 2880×1790) has the **entire frame** flipped, HUD and title included, which is a framebuffer-capture bug of an earlier build, not a text-shaping bug. | working | Inspect conjunct shaping at close range during manual play. |
| Subtitles BN/EN | `dialogue/DialogueLine` carries both; HUD draws both | implemented | — |

## 4. Visual acceptance gate (spec §03)

| Gate item | Evidence | Status | Next action |
|---|---|---|---|
| Character is not primitive geometry | **Updated later on 2026-10-25.** `character/PlayerMesh.java` now loads `assets/Models/Characters/UAL/AnimationLibrary_Godot_Standard.gltf` (Quaternius Universal Animation Library, CC0, see `docs/ASSET_REGISTER.md`): skinned mesh, 46 clips, scaled to 1.72 m, idle/walk/jog/sprint/crouch/jump driven from `PlayerControl`. Screenshots `character_2026-10-25_spawn_v1.png` (idle, Arif and Rafi) and `_v2.png` (jog mid-stride). Run log: `Character: model loaded=true, clip=Jog_Fwd_Loop, clip time=0.30 s`. The mesh is a faceless, unclothed mannequin. | partial — no longer primitive; still fails "clothing/materials" | Source a dressed student model on the same rig; add talking idle during dialogue. |
| Campus recognizable | `world/CampusWorld.java`: all landmarks are `Box`/`Cylinder` primitives with flat colours. No Curzon Hall at all. No texture maps. | placeholder — FAILS gate | Reference-based modelling per landmark (multi-day). |
| Lighting / background correct | Sky: unshaded 280 m sphere plus viewport colour; baseline screenshot shows light blue sky, not black. Sun + fill + ambient, PSSM shadows 2048×3, SSAO, tone map, FXAA. | working | — |
| Vegetation suits monsoon | Sphere canopies on cylinder trunks, flat green. | placeholder — FAILS gate | Tree models or billboards with licences. |
| Materials coherent | Phong `Lighting.j3md` flat colours, no albedo, normal or roughness maps; 3 PNGs in repo, none used as surface textures. | placeholder — FAILS gate | — |
| Bengali text legible | See §3. | working | — |
| Camera / collision work | See §2. | unverified | — |
| No missing textures / artifacts | None visible in baseline screenshot. | working | — |

## 5. Audio

| Requirement | Evidence | Status | Next action |
|---|---|---|---|
| UI / gameplay sounds | 10 WAVs in `assets/audio/`; jME code uses 4 (`ui_click`, `objective`, `footstep`, `bell`). `docs/audio_design.md` (LibGDX era) claims procedural synthesis. | partial | Record provenance per file in an asset register. |
| Original music | None. | missing | — |
| Ambience | None. | missing | — |
| Bangla voice | None. | missing | — |

## 6. Historical integrity and licensing

| Requirement | Evidence | Status | Next action |
|---|---|---|---|
| Source ledger for in-game claims | `docs/historical_research.md` and `docs/historical_timeline.md` are LibGDX-era and do not cite the texts now in `dialogue/HistoricalDialogues.java`. | missing | Build ledger with claim IDs before any claim is called verified. |
| In-game document texts | `getRajuMemorialDocument`, `getLibraryDocument`, `getTscDocument` make specific claims (students "locked arms" at Raju; "emergency aid and dispatch centers" under the library pilotis; victory "proclaimed" at TSC on 5 Aug). No source is cited anywhere in the repo. | unverified | Research each; mark dramatized where not supported. |
| Four slogans | Used as road decals and banners in `CampusWorld.buildJulyMovementBannersAndMurals`. Spec says they are not certified. | unverified | Research wording, date, place. |
| Fictional characters only | Dialogue is between Arif, Rafi, Tanvir, Nusrat and an unnamed collective; no real person is quoted. | working | Keep it that way. |
| Asset licence register | `docs/research/ASSET_LICENSE_REGISTER.md` is LibGDX-era. Fonts have licence files (`LICENSE-NotoSansBengali.txt`, `LICENSE-LiberationSans.txt`). Audio provenance unrecorded. | partial | New register for the jME asset set. |

## 7. Performance

| Requirement | Evidence | Status | Next action |
|---|---|---|---|
| Measured frame-time stability at 1080p on target | No measurement exists for the jME build on the Mac. | missing | Owner runs a profiled session on the Mac. |
| Measurement on this Windows machine | 300-frame automated run at 1280×720: average 17.57 ms (~56.9 FPS), min 3.10 ms, max 287.58 ms (startup frames), heap 31 MB used. Static camera, no input. This is **not** representative gameplay and **not** the target machine. | informational only | — |
| Honest reporting in code | `ProjectJulyApp.simpleUpdate` prints `Target: 1920x1080 @ 60 FPS verified on MacBook Air M4` **unconditionally**, regardless of resolution or measured result. | broken (false claim) | Remove the line in this branch. |

## 8. Documentation

| Item | Evidence | Status | Next action |
|---|---|---|---|
| Docs describe the current code | 35 of 36 Markdown files under `docs/` plus `CHANGES.md`, `CONTROLS.md`, `LEVELS.md` describe the LibGDX "Spark: Birth of 36 July" codebase (6 levels, combat combos, boss AI) that is not in this repository. | stale | Mark as legacy or move to `docs/legacy/`. |
| Claimed verification screenshots | `docs/visual_qa_report.md` reports "PASS (100%)" for Levels 1–6 citing 16 images under `assets/screenshots/`; that directory does not exist and is git-ignored. | stale / unsupported | Do not cite. |
| README | One line. | missing | Write build, run and test instructions. |

## 9. Owner-reported defects, re-checked against the current code

| Defect (owner screenshots) | Finding | Status |
|---|---|---|
| Black sky / background | Current code sets viewport colour and a sky sphere; baseline run on this machine shows blue sky. | fixed before this branch; re-verified 2026-10-25 |
| Mirrored / upside-down Bengali text | The affected screenshot is flipped as a whole (title, HUD and scene). Text rasterization path is upright in the baseline run. | capture bug of an earlier build; not present now |
| Primitive box character | Replaced the same day by a CC0 rigged, animated mannequin (see §4). Clothing, face and hair still missing. | reduced — dressed model still needed |
| Generic block buildings | Confirmed in code and screenshot. | open |
| Sparse stylized trees | Confirmed in code and screenshot. | open |

## 10. Evidence files

- `docs/screenshots/baseline_windows_2026-10-25_before.png` — automated 300-frame run on this branch before any visual change (Windows, 1280×720).
- `docs/screenshots/project_july_jme_gameplay.png` — unchanged committed image from `47c0a36`, kept for comparison.
- `docs/screenshots/character_2026-10-25_spawn_v1.png` — first run with the rigged mannequin: Arif (blue) idle at spawn facing the camera, Rafi (red) idle by the plinth.
- `docs/screenshots/character_2026-10-25_spawn_v2.png` — second run; input reached the window during the automated run (camera yaw 280°, character moved), which shows the jog clip mid-stride.
- Build and run logs are quoted above; they were not committed.

## 11. What this matrix does not claim

- Nothing in this file has been run on the target MacBook Air M4.
- Rows marked `unverified` have not been exercised by hand on this machine; the automated run has no input.
- No claim of 60 FPS is made for any machine.
