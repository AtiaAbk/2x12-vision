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
| WASD + mouse look | `ProjectJulyApp.initInputs` maps W/A/S/D, arrows, mouse axes. **2026-10-26:** the owner reported that ↑ walked towards the screen. Confirmed from the camera-relative formula (both axes negated) and fixed in `PlayerControl.update`; mouse-left also turned the view right and was fixed in `onAnalog`. Verified with `TEST_HOLD=Forward` (player z 17.0 → 4.0 while the camera stayed south at z 21 → 8), `TEST_HOLD=Right` (x 0 → +23, east, facing north) and `TEST_HOLD=CamLeft` (view bearing 0° → 310°, sculpture on the right). | fixed, verified by automated runs; by-hand check by the owner pending | Owner to confirm feel. |
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
| Checkpoint save / load | `level/SaveManager.java` → `save/checkpoint.json` (per-user folder when packaged); "Save checkpoint" in the pause menu, "Continue" in the main menu restores it; unit-tested | implemented | — |
| Autosave | Not implemented. | missing | — |
| Adaptive difficulty | Not implemented. | missing | — |
| Pause menu (ESC) | **2026-10-26:** `ui/MenuSystem.java` replaces `PauseMenu`: a navigable list (Resume, Save checkpoint, Settings, Restart level, Main menu, Quit) driven by arrows/WASD and Enter, Esc resumes. Captured with `TEST_UI=pause` (`frontend_2026-10-26_pause_v1.png`). | implemented, capture inspected; by-hand check pending | Owner to try. |
| Main menu | **2026-10-26:** title screen (game name, one-line historical note, fiction disclaimer, press Enter) → main menu (New game, Continue when a checkpoint exists, Settings, Quit) → chapter card ("অধ্যায় ১ · Where It All Began", auto-dismisses after 5 s) → play. Behind the menus the camera circles the Raju sculpture slowly and the title music plays. Settings: mouse sensitivity, invert Y, master volume, subtitles (both / বাংলা / English), window size and fullscreen (applied at next start), saved to `save/settings.properties` (per-user folder when packaged). Captures `frontend_2026-10-26_{title,menu,settings,chapter}_v1.png`. Automated runs skip the front end unless `TEST_UI` asks for it. | implemented, captures inspected | Mouse-driven menus later (cursor is captured by the orbit camera). |
| HUD | `ui/ProjectJulyHUD.java`: objective text (BN+EN), compass heading, distance, stamina, context prompt; visible in baseline screenshot. **Restyled later on 2026-10-25** after the owner found the UI ugly: one panel language (`BengaliTextRenderer.renderPanel`: translucent dark cards, accent bar, per-line sizes, word wrap), objective card with counter, bearing + target pill, real stamina bar, dialogue and prompt cards, restyled pause menu and document modal. Screenshots `ui_2026-10-25_hud_v1.png`, `_dialogue_v1.png`, `_document_v1.png`, `_pause_v1.png`. | working | Key column in the pause menu is not aligned (proportional font). |
| Map / markers | Only heading and distance text. `assets/maps/campus_map.png` exists but is unused by jME code. | partial | — |
| Bengali text shaping and orientation | `ui/BengaliTextRenderer.java` rasterizes Noto Sans Bengali via Java2D. Baseline screenshot on this machine and the committed screenshot from `47c0a36` both show upright, readable conjuncts. The owner's "mirrored text" screenshot (`project_july_level01_scene.png`, 2880×1790) has the **entire frame** flipped, HUD and title included, which is a framebuffer-capture bug of an earlier build, not a text-shaping bug. | working | Inspect conjunct shaping at close range during manual play. |
| Subtitles BN/EN | `dialogue/DialogueLine` carries both; HUD draws both | implemented | — |

## 4. Visual acceptance gate (spec §03)

| Gate item | Evidence | Status | Next action |
|---|---|---|---|
| Character is not primitive geometry | **Updated later on 2026-10-25.** `character/PlayerMesh.java` now loads `assets/Models/Characters/UAL/AnimationLibrary_Godot_Standard.gltf` (Quaternius Universal Animation Library, CC0, see `docs/ASSET_REGISTER.md`): skinned mesh, 46 clips, scaled to 1.72 m, idle/walk/jog/sprint/crouch/jump driven from `PlayerControl`. Screenshots `character_2026-10-25_spawn_v1.png` (idle, Arif and Rafi) and `_v2.png` (jog mid-stride). Run log: `Character: model loaded=true, clip=Jog_Fwd_Loop, clip time=0.30 s`. The mesh is a faceless, unclothed mannequin. **Updated again the same day:** `character/CharacterSpec.java` now selects Quaternius *Ultimate Modular Men / Women* models (CC0): Arif `Men/Casual_2` (shirt tinted blue, jeans), Rafi `Men/Casual_Hoodie` (red), Tanvir `Men/Suit` (navy), Nusrat `Women/Casual` (gold top). Each has a face, hair, clothing and its own Idle/Walk/Run clips. Screenshots `character_2026-10-25_clothed_v1.png` (authored colours) and `_v2.png` (tinted). Log: `Character: model loaded=true, clip=Idle, clip time=0.79 s`. Style is stylized low-poly, not realistic; no crouch or jump clip in these packs. | passes "not primitive" and "clothing"; "realistic" remains open | Realistic character is a later milestone; talking idle during dialogue still unwired. |
| Campus recognizable | **Updated later on 2026-10-25.** `world/CampusWorld.java` rebuilt at true scale: origin at the Raju sculpture, footprints and positions of the TSC union block, auditorium, cafeteria, Central Library Buildings I and II, the roundabout island and Hakim Chattar taken from OpenStreetMap (raw data in `docs/reference/osm/`), building programmes from the Daily Star, Wikipedia and Banglapedia. Every value and its status is in `docs/REFERENCE_LEDGER.md`. Raju: 8 figures with linked raised arms on a plinth (figure size unverified). TSC: 3-storey brick union block with verandah, jali panels, double roof and covered walkways; auditorium 51.8 m long under a parabolic vault; two cafeteria wings. Library: two blocks on pilotis with open ground floors, ramp, vaulted bar, brise-soleil fins. Screenshots `campus_2026-10-25_spawn_v3.png`, `_tsc_v2.png`, `_tsc_auditorium_v1.png`, `_library_v2.png`. Still primitives and custom meshes, not reference photographs; facades, heights above the OSM data, colours and the sculpture's proportions are modelling choices. No Curzon Hall (outside Level 01). | approximate — footprints referenced, elevations approximate | Elevation references (photographs, drawings) per landmark; proper tree assets. |
| Lighting / background correct | Sky: unshaded 280 m sphere plus viewport colour; baseline screenshot shows light blue sky, not black. Sun + fill + ambient, PSSM shadows 2048×3, SSAO, tone map, FXAA. | working | — |
| Vegetation suits monsoon | **Updated later on 2026-10-25.** 31 trees are reduced Poly Haven CC0 photogrammetry scans (`assets/Models/Trees/`, `world/TreeLibrary.java`): textured bark and twigs, alpha-cut leaf quads, three levels of detail switched by camera distance (45 m / 130 m), cast and receive shadows. Species are stand-ins: one broadleaf scan serves krishnachura, radhachura, shade trees and the banyan at different scales and blossom tints; a jacaranda serves jarul. Dense monsoon undergrowth, hedges and grass clumps are not modelled. | partial — real tree models, wrong species, no undergrowth | Species-correct models, hedges, ground cover. |
| Materials coherent | **Updated later on 2026-10-25.** Six ambientCG CC0 texture sets (`assets/Textures/`, colour + OpenGL normal maps, 1K) applied through `Lighting.j3md` with world-space tiling: grass, asphalt, paving stones, brick, concrete, painted plaster. Glass, bronze, steel remain flat colours; tree bark and leaves are textured (see Vegetation). Static geometry batched per material (853 -> 22 draw batches). | partial — surfaces textured, no roughness/AO, no textured props | PBR materials and vegetation assets later. |
| Bengali text legible | See §3. | working | — |
| Camera / collision work | See §2. | unverified | — |
| No missing textures / artifacts | None visible in baseline screenshot. | working | — |

## 5. Audio

| Requirement | Evidence | Status | Next action |
|---|---|---|---|
| UI / gameplay sounds | **2026-10-26:** the whole soundbank is regenerated by `tools/synth_audio.py` (original, parameters in the script, see `docs/ASSET_REGISTER.md`): click, confirm, back, notify, warning, objective chord, bell; footsteps in three variants each for grass, paving and asphalt, chosen per step from `CampusWorld.groundTypeAt` with a small random pitch change. Old unverified files deleted. | implemented; heard by the developer only through the automated runs' log (no listening test recorded) | Owner to listen and judge levels. |
| Original music | **2026-10-26:** `music_title.wav`, a 58 s procedural loop (plucked D drone, flute-like melody in D minor written for the project) on the title and main menu; stops when a chapter starts. | placeholder quality, original | Composed music later. |
| Ambience | **2026-10-26:** `ambience_campus.wav`, a 39.5 s seamless synthetic loop (wind in leaves, distant traffic swell, koel-like calls, sparrow chirps, low crowd murmur) at full level in play and 35 % under the menus. | implemented, synthetic | Real field recordings with licences later. |
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
| Primitive box character | Replaced the same day, first by a CC0 rigged mannequin and then by CC0 clothed, animated characters with faces and hair (see §4). | closed as "primitive"; realism still open |
| Generic block buildings | Confirmed in code and screenshot. | open |
| Sparse stylized trees | Sphere trees replaced by scanned tree models later the same day (see §4, Vegetation). | closed as "stylized"; species accuracy open |

## 10. Evidence files

- `docs/screenshots/baseline_windows_2026-10-25_before.png` — automated 300-frame run on this branch before any visual change (Windows, 1280×720).
- `docs/screenshots/campus_2026-10-25_spawn_v3.png` — true-scale campus, spawn view facing north: island, sculpture, Rafi, library in the background.
- `docs/screenshots/campus_2026-10-25_tsc_v2.png` — TSC union block from the entrance plaza (`TEST_SPAWN=4,24,0`).
- `docs/screenshots/campus_2026-10-25_tsc_auditorium_v1.png` — auditorium vault and cafeteria wing from the south-west (`TEST_SPAWN=-45,125,135`).
- `docs/screenshots/campus_2026-10-25_library_v2.png` — Central Library Building II on pilotis, ramp, fins, Hakim Chattar (`TEST_SPAWN=-25,-35,225`).
- `docs/screenshots/project_july_jme_gameplay.png` — unchanged committed image from `47c0a36`, kept for comparison.
- `docs/screenshots/character_2026-10-25_spawn_v1.png` — first run with the rigged mannequin: Arif (blue) idle at spawn facing the camera, Rafi (red) idle by the plinth.
- `docs/screenshots/character_2026-10-25_spawn_v2.png` — second run; input reached the window during the automated run (camera yaw 280°, character moved), which shows the jog clip mid-stride.
- `docs/screenshots/character_2026-10-25_clothed_v1.png` — clothed Quaternius characters with authored colours; Arif faces the sculpture at spawn (verifies the heading change).
- `docs/screenshots/character_2026-10-25_clothed_v2.png` — same with per-character shirt and trouser tints.
- `docs/screenshots/trees_2026-10-25_spawn_v1.png` — spawn view with the model trees along the west and north-east road arms.
- `docs/screenshots/trees_2026-10-25_close_v1.png` — the krishnachura stand-ins along the west road from about 28 m, blossom tint visible (`TEST_SPAWN=-30,34,180`, pitch −2).
- `docs/screenshots/trees_2026-10-25_library_v1.png` — jarul stand-ins (jacaranda) in front of the library (`TEST_SPAWN=-25,-35,225`).
- `docs/screenshots/trees_2026-10-25_banyan_v1.png` — the large shade tree standing in for the Hakim Chattar banyan (`TEST_SPAWN=-75,-35,215`, pitch 4).
- `docs/screenshots/trees_2026-10-25_tsc_v1.png` — TSC union block framed by two trees (`TEST_SPAWN=0,35,17`).
- `docs/screenshots/controls_2026-10-26_forward_v1.png` — after holding Forward for 120 frames from the spawn: Arif has walked north onto the island, away from the camera.
- `docs/screenshots/controls_2026-10-26_mouse_left_v1.png` — after mouse-left for 120 frames: view turned to NW 310°, sculpture on the right.
- `docs/screenshots/slogans_2026-10-26_{west_road,tsc,library}_v1.png` — Level 01 quota-reform slogans on the west road, the TSC banner and the library banner.
- `docs/screenshots/frontend_2026-10-26_{title,menu,settings,chapter,pause}_v1.png` — title screen, main menu, settings, chapter card and pause menu (`TEST_UI=title|menu|settings|chapter|pause`).
- Build and run logs are quoted above; they were not committed.

## 11. What this matrix does not claim

- Nothing in this file has been run on the target MacBook Air M4.
- Rows marked `unverified` have not been exercised by hand on this machine; the automated run has no input.
- No claim of 60 FPS is made for any machine.

## 9. Addendum, same day: owner's first local run

The owner ran the game and reported an arrow on the screen and an ugly UI. Checked on the live window with a desktop capture (`docs/screenshots/ui_2026-10-25_desktop_cursor_hidden.png` is the after state):

| Finding | Cause | Fix | Evidence |
|---|---|---|---|
| OS mouse arrow visible over the game | `flyCam.setEnabled(false)` re-shows the cursor and nothing hid it again; a hide call during `simpleInitApp` is dropped because the LWJGL3 window is not yet renderable | `inputManager.setCursorVisible(false)` after disabling flyCam and again on the first rendered frame | Win32 `GetCursorInfo` on the focused window: showing=True before, showing=False after; A/B runs show no frame-time cost (26.9 vs 27.6 ms steady) |
| HUD looked ugly | Flat grey boxes, bold yellow text, decorative glyphs, text-only stamina | Panel restyle listed in §2 | Four UI captures |
