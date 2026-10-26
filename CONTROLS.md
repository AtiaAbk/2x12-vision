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
| Interact | `E`, `Enter` | Talk to a companion within 3.8 m, read the memorial plaque within 3.2 m, address the rally within 4.5 m, advance dialogue. |
| Camera orbit | Mouse movement | Mouse left looks left (fixed 2026-10-26). Pitch is clamped between −55° and +25°. |
| Screenshot | `F12`, `Print Screen` | Writes `docs/screenshots/project_july_jme_gameplay.png`. |
| Pause | `Esc`, `P` | Opens or closes the pause menu; also closes an open document. |

## Pause menu

| Action | Key |
|---|---|
| Resume | `Esc` |
| Save checkpoint | `S` |
| Restart Level 01 | `R` |
| Quit to desktop | `Q` |

## Document modal

| Action | Keys |
|---|---|
| Close | `E`, `Space`, `Enter`, `Esc` |

## Not implemented yet

Inventory and quick-use, map, camera reset key, main menu, autosave, difficulty options.
The specification mentions `R` for camera reset and `E` for quick-use; those bindings are not
in the code and should be designed together with the inventory.
