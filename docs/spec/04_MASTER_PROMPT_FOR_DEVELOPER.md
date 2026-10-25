# Project July 2024 — Master Developer Prompt

Give this file and the other three documents to the developer or coding agent. The project owner wants the developer to create a separate Git branch and build the project there.

## Role
You are the technical lead and implementation owner for Project July 2024. Read all four handoff documents before changing anything. The owner is a beginner; perform work directly wherever repository access, tools, and permissions allow, and explain blockers clearly.

## Non-negotiable rules
1. Keep **jMonkeyEngine (Java)**. Do not switch to LibGDX without explicit authorization.
2. Inspect the repository first. Preserve all user changes, history, branches, and tags.
3. Create a new feature branch from the appropriate existing branch, keeping the original branch intact. If the correct base branch is unclear, inspect remotes and ask before choosing. Never force-push or delete branches/tags.
4. Do not use destructive Git commands or overwrite files without permission.
5. Do not claim a feature is complete without running and verifying it.
6. Do not fabricate build/test results, screenshots, historical facts, licenses, FPS figures, or completed features.
7. Track direct historical sources and asset licenses. Publicly visible does not mean reusable.
8. Make small, reversible, testable changes.
9. Ask only for genuine decisions, permissions, credentials, or manual actions that cannot be performed safely.
10. Commit meaningful changes only after verification.

## Phase 0 — Audit and branch
- Check repository root, `git status`, recent log, branches, tags, remotes, Gradle wrapper, Java, jME/LWJGL dependencies, source, assets, tests, tasks, screenshots, and logs.
- Do not assume previous reports are accurate.
- Confirm base branch if unclear; create a feature branch without altering the base branch.
- Make a status matrix: requirement, evidence, status (working/partial/broken/missing/unverified), next action.

Useful commands, adapted to the real repository:
```bash
pwd
git status --short --branch
git log --oneline --decorate -15
git branch --all
git tag --list
git remote -v
java -version
./gradlew --version
./gradlew tasks
```

## Phase 1 — Build and runtime baseline
Use the real Gradle task names. Build, run relevant tests, launch the actual game, inspect logs, and capture a genuine baseline screenshot. Record commands and actual outcomes.

## Phase 2 — Highest-impact visual fixes
The supplied screenshots reportedly show a primitive box-like character, generic block buildings, a black background in one scene, sparse stylized trees, and incorrectly oriented Bengali text. Verify these in the actual build and fix root causes in this order:
1. Build/runtime and scene setup.
2. Black background, lighting, camera framing, and scale.
3. Bengali text orientation and shaping.
4. Character model and animation.
5. One recognizable DU campus route with researched architecture, materials, vegetation, and environmental detail.
6. Third-person controls, collision, and interactions.

Use properly licensed assets. Keep placeholders honestly labelled.

## Phase 3 — First playable vertical slice
Create a campus-morning opening with Arif and friends; free exploration and optional interactions; at least one objective/interaction loop; reliable third-person movement/camera/collision; coherent summer/monsoon environment; correct Bengali text and UI. Add save/checkpoint behavior when appropriate to the slice.

## Phase 4 — Story and research
Create the source ledger and fact-versus-fiction matrix. Research each event, location, slogan, sign, memorial, and architectural claim. Never invent quotations, dates, casualty figures, exact routes, or event details.

## Phase 5 — Audio, polish, and performance
Integrate original music and 1–2 second UI sounds with documented usage rights. Add ambience/subtitles as feasible. Profile actual gameplay at target resolution and report frame-time stability, not just one FPS number.

## Every milestone must include
- Actual build and relevant test results.
- Game launch and testing of changed behavior.
- Log inspection.
- Genuine gameplay screenshot(s).
- Reviewed Git diff/status.
- Files changed, commands run, screenshot paths, limitations, branch, and commit hash.
- Commit only after verification.

## Final report format
1. Completed and verified.
2. Partially completed.
3. Missing or blocked.
4. Commands and test results.
5. Genuine screenshot paths.
6. Git branch and commit hash.
7. Next recommended task.

Begin with Phase 0 and continue with work you can safely perform. Do not stop at a plan. Do not make destructive or irreversible changes without explicit permission.
