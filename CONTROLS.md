# Project July 2024 — Controls

These are the bindings actually registered in `ProjectJulyApp.initInputs()` as of 2026-10-25.
The previous version of this file described the LibGDX build and listed abilities (dodge,
combos, wall climb, map, gender switch) that do not exist in the current code.

## Gameplay

| Action | Keys | Notes |
|---|---|---|
| Move forward / back | `W` / `S`, `↑` / `↓` | Relative to the camera: forward walks away from the camera (fixed 2026-10-26). |
| Move left / right | `A` / `D`, `←` / `→` | |
| Sprint | `Left Shift`, `Right Shift` | Drains stamina; sprint stops below 5 % and recovers when not sprinting. |
| Jump | `Space`, `J` | |
| Crouch | `C` (hold) | |
| Interact | `E`, `Enter` | Talk to a companion within 3.8 m, read a document within 3.2 m (Raju plaque on the island lawn, notice boards at the Central Library and TSC), address the rally within 4.5 m, advance dialogue. |
| Camera orbit | Mouse movement | Mouse left looks left (fixed 2026-10-26). Pitch is clamped between −55° and +25°. |
| Reset camera | `R` | Snaps camera view directly behind Arif facing his current heading. |
| Toggle Campus Map | `M` | Opens or closes the Dhaka University campus navigation map overlay. |
| Screenshot | `F12`, `Print Screen` | Writes `docs/screenshots/project_july_jme_gameplay.png`. |
| Pause | `Esc`, `P` | Opens or closes the pause menu; also closes an open document or map. |

## Menus (title, main menu, settings, pause, chapter complete)

| Action | Keys |
|---|---|
| Move selection | `↑` / `↓`, `W` / `S` |
| Change a setting | `←` / `→`, `A` / `D` |
| Select | `Enter`, `E`, `Space` |
| Back / resume | `Esc` |

Pause menu entries: Resume, Save checkpoint, Settings, Restart level, Main menu, Quit.
Chapter complete entries: Return to main menu, Restart level.
Settings: mouse sensitivity, invert mouse Y, master volume, subtitles (both / বাংলা / English),
window size and fullscreen (the last two apply at the next start). Settings are stored in
`save/settings.properties` (per-user folder when installed).

## Document & Map modals

| Action | Keys |
|---|---|
| Close Document | `E`, `Space`, `Enter`, `Esc` |
| Close Campus Map | `M`, `Esc`, `E`, `Space` |

## Not implemented yet

Inventory and quick-use, corner HUD mini-radar, mouse-driven menus, autosave, difficulty options.
