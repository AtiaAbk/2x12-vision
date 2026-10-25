# Documentation map

Last updated: 2026-10-25

## Current (describes the jMonkeyEngine code in `game/`)

| File | Purpose |
|---|---|
| `spec/01_GAME_REQUIREMENTS.md` | Approved game requirements (engine, cast, systems, locations, integrity rules). |
| `spec/02_STORY_AND_HISTORICAL_FRAMEWORK.md` | Narrative direction and research-ledger format. Not a verified chronology. |
| `spec/03_VISUAL_AUDIO_AND_TECHNICAL_QUALITY.md` | Visual acceptance gate, audio plan, performance and licensing rules. |
| `spec/04_MASTER_PROMPT_FOR_DEVELOPER.md` | Phase 0 to Phase 5 execution order for the developer or coding agent. |
| `spec/PROJECT_JULY_2024_COMPLETE_PLAN_BN.md` | Consolidated owner plan in Bengali, including the "verify before trusting" list. |
| `STATUS_MATRIX_2026-10-25.md` | Phase 0 audit: what builds, runs, passes, is placeholder, is missing, is unverified. |
| `screenshots/baseline_windows_2026-10-25_before.png` | Automated 300-frame run on Windows before any change on the feature branch. |
| `screenshots/after_text_fallback_2026-10-25.png` | Same run after the HUD font-fallback fix. |
| `screenshots/project_july_jme_gameplay.png` | Committed image from `47c0a36`, kept for comparison. |
| `../README.md`, `../CONTROLS.md` | Build, run, test instructions and the actual key bindings. |

## Legacy (describes the earlier LibGDX build "Spark: Birth of 36 July")

Every other Markdown file under `docs/`, plus `../CHANGES.md` and `../LEVELS.md`, was written for
the LibGDX codebase that preceded the jMonkeyEngine rebuild (commit `4475461`). They describe six
levels, martial-arts combat, boss AI, a tactical map and verification screenshots under
`assets/screenshots/` that do not exist in this repository.

Rules for legacy files:

- Do not cite their "PASS", FPS or verification claims as evidence for the current build.
- Research material in them (`research/`, `story/`, `historical_research.md`, `location_reference_matrix.md`)
  may be reused as *starting points* only after each claim is re-checked and entered in a new source
  ledger with a claim ID, URL and verification status.
- They are kept for history; move or delete only with the owner's authorization.
