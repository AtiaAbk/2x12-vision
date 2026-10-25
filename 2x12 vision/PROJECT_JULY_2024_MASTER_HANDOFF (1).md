# PROJECT JULY 2024 --- MASTER PROJECT HANDOFF

**Purpose:** Move the project from Antigravity to another IDE or AI
coding agent without losing the vision, requirements, technical
decisions, setup expectations, or known problems.

**Important status note:** This is a requirements and handoff document,
not proof that every described feature exists. The receiving IDE must
inspect the actual repository, run the game, and verify claims before
treating them as complete.

## 1. Project identity

-   **Name:** Project July 2024
-   **Genre:** Cinematic narrative third-person action-adventure
-   **Setting:** Bangladesh's July--August 2024 student movement
-   **Protagonist:** Arif, a fictional male second-year Political
    Science student at the University of Dhaka
-   **Main goal:** Create a playable, emotionally grounded, historically
    responsible game with convincing Bangladeshi environments, strong
    storytelling, polished controls, and realistic 3D presentation.
-   **Priority:** Build a reliable playable vertical slice first. Do not
    claim "AAA" quality unless the actual running game supports that
    claim.

**Core rule:** A report, code comment, or FPS counter is not proof.
Inspect the implementation, run it, test it, and capture genuine in-game
evidence.

## 2. Workspace, engine, and migration

Previously reported workspace:

`/Users/atiaoishi/Downloads/2x12-vision`

Verify the actual path on the new machine. The user has explicitly
chosen **jMonkeyEngine (jME)** as the primary engine. Do not switch back
to LibGDX unless the user explicitly authorizes it.

### Migration requirements

1.  Open and inspect the existing repository before editing.
2.  Preserve Git history, branches, tags, uncommitted changes, and user
    work.
3.  Do not run destructive cleanup, reset, rebase, force-push, or delete
    branches/tags without explicit permission.
4.  Do not create a separate workspace unless requested.
5.  Make a backup branch or patch before major migration work if needed.
6.  Keep the project buildable through small, reversible milestones.
7.  Explain any step that genuinely requires user login, permission,
    purchase, or manual action.
8.  Configure reproducible build/run/test instructions so the project is
    not dependent on Antigravity-specific hidden state.

### Reported Git references --- verify before relying on them

-   `backup/legacy-2x12-vision` at `14dd97f`
-   `v0.1-legacy-backup` at `14dd97f`
-   `backup/libgdx-level01-implementation` at `c76130a`
-   Reported commit `57b87e4`:
    `feat(graphics): enhance campus architecture, authentic July graffiti decals, student character details, and lush monsoon vegetation`
-   A previous report claimed `origin/main` was synchronized and the
    working tree clean. Verify this; do not assume it.

### Initial audit commands

Run from the real repository root:

``` bash
pwd
git status --short --branch
git log --oneline --decorate -15
git branch --all
git tag --list
java -version
./gradlew --version
./gradlew tasks
```

Inspect the README, Gradle files, source, assets, tests, logs, and
screenshots. Do not restore the old LibGDX implementation as the active
game, but preserve its history if useful.

## 3. User and collaboration style

The user is a beginner in game development and uses a MacBook Air M4
with 16 GB RAM. Use clear, practical language and concrete steps. The
agent should perform setup, builds, tests, and implementation itself
wherever tools and permissions allow, rather than only giving generic
instructions. Do not ask the user to repeat requirements already
recorded here. Do not silently make major decisions that contradict the
brief. If something is blocked, state the exact blocker and smallest
next action.

## 4. Technical direction

-   **Engine:** jMonkeyEngine.
-   **Initial platform:** macOS on Apple Silicon.
-   **Performance target:** 1920×1080, aiming for stable 60 FPS where
    feasible.
-   **Build:** Use the repository's actual Gradle wrapper and compatible
    dependencies. Find the real run task with `./gradlew tasks`; do not
    invent task names.
-   **Toolchain claims from a previous report:** Java 17 Corretto
    aarch64, Gradle 9.6.1, LWJGL 3.3.3, jMonkeyEngine 3.7.0-stable.
    These are reported values, not verified facts. Check the actual
    project and machine before changing versions.
-   A previous report claimed PBR, PSSM shadows, SSAO, filmic tone
    mapping, and FXAA. Verify that these effects are present, working,
    and worth their performance cost.

Measure actual gameplay over a representative duration. Report
resolution, graphics settings, average frame time, frame-time stability,
memory use, and test duration. An instantaneous FPS number or a short
benchmark does not prove stable performance.

## 5. Story and characters

### Arif

-   Fictional male second-year Political Science student at the
    University of Dhaka.
-   Ordinary student, not a superhero or professional soldier.
-   Casual shirt, jeans/trousers, student ID, canvas backpack where
    appropriate.
-   Grounded proportions, believable animation and collision.
-   Fixed protagonist with limited customization.
-   Motivation: fairness, student rights, solidarity, and survival.
-   Realistic, balanced personality.

### Friends

-   Rafi, Tanvir, and Nusrat: two male friends and one female friend.
-   They are already close to Arif and trust each other.
-   Their dialogue, movement, and companion behavior should feel natural
    and grounded.

### Opening and experience

-   A natural campus morning with Arif and his three friends.
-   Let the player explore and discover optional campus interactions.
-   Story and historical experience are top priorities.
-   Gameplay should balance exploration, narrative, movement,
    interaction, and grounded action.
-   Combat importance is medium, not the core fantasy.
-   Use connected mission areas, main missions, optional side missions,
    and clear progression.
-   Make Level 01 a polished vertical slice rather than a huge empty
    map.

## 6. Historical integrity and research

The game concerns a real and sensitive period in Bangladesh's recent
history. Treat it respectfully.

For each important claim, date, location, slogan, quotation, event, or
historical reconstruction, record: - Direct clickable source URL. -
Source title and publisher/organization. - Publication date and event
date separately where relevant. - The exact claim supported. -
Verification status: cross-checked, single-source, disputed, or
unverified. - Limitations, ambiguity, conflicting accounts, and
last-checked date.

Prefer primary sources and credible reporting. Cross-check significant
claims. Separate historical fact from fictional content. Arif, Rafi,
Tanvir, Nusrat, their personal dialogue, and their individual missions
are fictional unless sources establish otherwise. Never invent a quote
and attribute it to a real person. Label dramatized or composite scenes.
Do not fabricate dates, casualty figures, perpetrators, or exact routes.
Document disagreements between sources.

### Previously reported Bengali text --- verify before use

-   "কোটা না মেধা? মেধা! মেধা!"
-   "৩৬ জুলাই --- ছাত্র-জনতার ঐতিহাসিক বিজয়"
-   "আমার ভাই মরলো কেন? জবাব চাই!"
-   "স্বৈরাচার নিপাত যাক, গণতন্ত্র মুক্তি পাক"

Do not treat these as historically verified merely because they appeared
in an earlier implementation report. Verify wording, context, date, and
suitability. All Bengali text must be upright, readable, correctly
shaped, and correctly placed in the 3D world.

## 7. Locations and environment

### Art direction

-   Realistic, detailed 3D; no primitive blockout presented as finished
    visuals.
-   Bangladesh summer/monsoon atmosphere appropriate to July--August.
-   Lush believable local vegetation, natural leaves, varied tree forms,
    wind response, and realistic ground cover.
-   Balanced warm daylight, natural shadows, convincing material
    response, and coherent color grading.
-   Avoid washed-out gray scenes, monochrome/winter appearance, pure
    black backgrounds caused by rendering errors, cartoonish
    proportions, and generic untextured cuboids.
-   Consistent scale between people, doors, stairs, roads, trees, and
    buildings.

### Planned campaign locations

-   University of Dhaka campus.
-   Jahangirnagar University.
-   Relevant Dhaka locations including Chankharpul, Rampura, and
    Jatrabari where research supports their inclusion.
-   Later route/ending concept around the Ganabhaban road area and
    Shaheed Minar.

Do not invent exact historical routes or claim an event happened at a
precise location without evidence.

### Level 01 --- University of Dhaka

Requested or mentioned: - Curzon Hall and surroundings. - Central
Library. - Hakim Chattar. - Teacher-Student Centre (TSC). -
Anti-Terrorism Raju Memorial Sculpture. - Nearby roads, paths, trees,
campus furniture, and student gathering areas.

A prior report claimed specific architectural details and attributions
for the Central Library, TSC, and Raju Memorial Sculpture. Verify these
details with reliable sources and visual references before representing
them as accurate. Do not label generic blocks as faithful replicas.

### Asset licensing

Use appropriately licensed 3D models, textures, fonts, sounds, and
images. Record asset name, creator, source URL, license, attribution,
modification and commercial-use terms, and integration status. Publicly
viewable images are not automatically reusable. Do not extract
proprietary models, textures, music, or sound effects from commercial
games. If an asset is unavailable, document the placeholder and the
precise asset needed; never mislabel a placeholder as realistic.

## 8. Gameplay systems

Implement incrementally and test each system.

### Movement and camera

-   Standard PC controls; WASD movement, mouse camera, sprint, jump,
    crouch where appropriate, interaction.
-   Smooth walking/sprinting transitions and reliable collision.
-   Prevent falling through the world, camera clipping, and getting
    stuck.
-   R may reset the camera if that is already the project's mapping;
    inspect current controls first.
-   ESC should open a functional pause/menu flow if implemented.

### Stamina and items

-   Sprinting consumes stamina with a soft limit.
-   Water supports recovery; food restores stamina.
-   No hunger meter.
-   Simple limited inventory with quick-use water, first aid, and food.
    E was previously suggested for quick-use; check existing mapping
    before changing it.

### Mission and interaction

-   Contextual interaction with people and objects.
-   Main and optional side missions.
-   Objective text, optional markers, map, and navigation assistance.
-   Checkpoints, autosave, manual save, adaptive difficulty.
-   Clear mission completion/failure and recovery states.
-   Companions must not block progress.

### Action and conflict

-   Grounded action-adventure, not power fantasy.
-   Medium combat importance.
-   Emphasize evasion, defense, rescue, escape, survival, and protecting
    others where appropriate.
-   Do not make violence the only gameplay loop; preserve historical
    context and human dignity.

### UI and subtitles

-   Clean, modern, consistent UI with good typography, spacing,
    hierarchy, icons, and feedback.
-   Hybrid HUD: objective text, optional markers, map/navigation
    assistance.
-   No debug overlays in polished screenshots unless intentionally
    demonstrating diagnostics.
-   Bangla voice direction and Bangla/English subtitles are desired.
-   Use a Bengali-capable font and verify Bengali conjunct shaping.

## 9. Music and sound

The user has a Google Flow subscription and wants original game audio.

### Music

Create original instrumental music for a cinematic historical narrative
set in Bangladesh in July--August. The user likes the broad
emotional/cinematic feel of games such as Where Winds Meet, but do
**not** copy or imitate any specific copyrighted track, melody,
arrangement, or proprietary audio library. Use a distinct original
musical identity with restrained orchestral tension, emotional strings,
atmospheric layers, and respectful regional instrumental colors. The
score should support campus life, rising tension, reflection,
escape/survival, and resolution.

### UI effects

Create original 1--2 second UI click/notification sounds with a clean
transient, pleasant decay, and no harsh clipping. Possible categories:
click, confirm, back/cancel, notification, objective update, warning.

Document each audio asset's filename, purpose, duration, format, sample
rate, channels, source/generation prompt, usage terms, and integration
status. Keep music, ambience, voice, UI, and gameplay effects organized
separately.

## 10. Visual quality gates

Do not mark a visual milestone complete unless it meets these checks:

-   Character is not a box body with a sphere head in a milestone called
    realistic.
-   Character has believable proportions, clothing/materials, animation,
    and shadows.
-   No pure black sky/background caused by setup errors.
-   No collection of untextured cuboids presented as a finished campus.
-   Recognizable, reference-guided architecture with grounded scale.
-   Natural summer vegetation and convincing daylight.
-   Roads, paths, buildings, and props connect logically.
-   Third-person framing is useful; camera does not clip through
    geometry.
-   Bengali text is not upside down, mirrored, broken, or unreadable.
-   No missing textures, rendering artifacts, or accidental debug text
    in presentation screenshots.
-   HUD is legible and does not unnecessarily obscure the player.

For every visual milestone, capture genuine screenshots from the running
game (ideally 1920×1080 where supported), and a second angle or short
clip if needed. Explain what visibly changed and what remains
incomplete. AI concept art can be a reference, but it is not proof of an
in-engine result.

## 11. Testing and definition of done

For each milestone: 1. Run the actual Gradle build. 2. Run relevant
tests and report real results. 3. Launch the game with the documented
command. 4. Test movement, camera, collision, and changed systems. 5.
Check logs for exceptions, missing assets, and rendering errors. 6.
Capture a genuine screenshot. 7. Review changed files and Git status. 8.
Commit only after verification.

### First playable vertical slice acceptance

-   The game launches from documented setup.
-   The player can control Arif in third person.
-   At least one interaction/objective works end-to-end.
-   Level 01 has recognizable, reference-guided campus landmarks.
-   Summer lighting, vegetation, and materials look coherent.
-   UI and Bengali text render correctly.
-   Build/tests work from the documented setup.
-   A genuine screenshot demonstrates the result.
-   Missing systems and placeholder assets are explicitly listed.

## 12. Suggested phases

**Phase 0 --- Audit and setup:** Inspect Git, build, engine, source,
assets, tests, tasks, and screenshots. Create a truthful
done/partial/missing/unverified report. Verify the toolchain.

**Phase 1 --- Stable foundation:** Fix launch/build issues, camera,
movement, collision, scene lifecycle, sky/background, lighting, scale,
and text orientation.

**Phase 2 --- Character and campus:** Integrate a properly licensed
humanoid and animations. Build one polished DU route with researched
landmarks, materials, textures, vegetation, and environmental details.

**Phase 3 --- Gameplay loop:** Add an opening sequence, interactions,
objective, mission completion, checkpoint/save behavior, and basic UI
feedback.

**Phase 4 --- Story and research:** Add the friends, grounded dialogue,
verified locations/events, and a source ledger. Separate fact from
fiction.

**Phase 5 --- Audio and polish:** Add original music, ambience, UI
sounds, subtitles, audio controls, animation improvements, and measured
optimization.

**Phase 6 --- Expansion:** Expand to further researched locations and
connected mission areas only after the first slice is stable.

## 13. Suggested repository documentation

Adapt the existing structure rather than blindly rearranging it. A
possible organization:

``` text
Project-July-2024/
├── README.md
├── PROJECT_HANDOFF.md
├── docs/
│   ├── design/
│   ├── research/
│   │   ├── historical-sources.md
│   │   └── asset-licenses.md
│   ├── testing/
│   └── screenshots/
├── assets/
│   ├── models/
│   ├── materials/
│   ├── textures/
│   ├── vegetation/
│   ├── audio/
│   │   ├── music/
│   │   ├── ambience/
│   │   ├── ui/
│   │   └── gameplay/
│   ├── fonts/
│   └── ui/
├── src/
│   ├── main/
│   └── test/
└── gradlew
```

Keep generated, licensed, and user-created assets distinguishable. Do
not commit secrets, passwords, tokens, or API keys.

## 14. Copy/paste startup prompt for the new IDE agent

> You are taking over **Project July 2024**, an existing jMonkeyEngine
> game project. Read `PROJECT_HANDOFF.md` completely before making
> changes.
>
> The user is a beginner and wants you to understand the project and
> configure it as far as your tools and permissions allow. Do not merely
> provide generic instructions if you can perform the steps yourself.
>
> **First, do no harm:** inspect the repository root, Git status, recent
> commits, branches, tags, Gradle wrapper, Java version, jME
> dependencies, assets, tests, run tasks, and existing screenshots.
> Preserve user changes and Git history. Do not use destructive Git
> commands or overwrite files without permission.
>
> Keep **jMonkeyEngine** as the chosen engine. Do not switch back to
> LibGDX. Work in the existing repository unless the user authorizes
> otherwise.
>
> Treat this handoff as the requirements source. Existing screenshots
> reportedly show a primitive blockout, a box-like character, generic
> block buildings, a black background in one scene, sparse stylized
> trees, and incorrectly oriented Bengali text. Verify the actual
> current state, find root causes, and fix the highest-impact issues in
> small, testable steps.
>
> Do not claim realistic/AAA visuals, accurate architecture, stable 60
> FPS, passing tests, licensed assets, or completed systems unless
> verified. Use genuine screenshots from the running game. Clearly mark
> features as implemented, partial, missing, or unverified.
>
> Follow the historical research rules: direct clickable sources, source
> title/publisher, dates, supported claim, verification status, and
> limitations. Separate historical fact from fictional characters and
> dramatization. Publicly viewable images are not automatically
> reusable. Do not extract proprietary assets/audio from commercial
> games.
>
> Start with Phase 0. Report the actual repository state and blockers,
> then complete setup/build/run checks you can perform. After the audit,
> implement the highest-impact visual fixes and verify them in the
> running game. Do not stop at a plan. Commit meaningful changes only
> after build/tests and visual checks succeed.
>
> If a step requires a user-only decision, login, permission, or manual
> action, explain exactly what is needed and why. Otherwise proceed
> autonomously within the repository and available permissions.
>
> At every milestone report: files changed, commands actually run, real
> build/test results, genuine screenshot paths, limitations, Git commit
> hash, and next most valuable task.

## 15. Previously reported implementation --- verify everything

A previous report mentioned: - `PlayerMesh.java` - `CampusWorld.java` -
`BengaliTextRenderer.java` - `ProjectJulyJmeTest.java` -
`docs/screenshots/project_july_jme_gameplay.png` - Commit `57b87e4` -
7/7 tests passing - A benchmark around 70.8 FPS and a screenshot
displaying around 97 FPS.

The user subsequently shared screenshots that visibly show a primitive
scene: a box-like body and sphere head, simple block structures, black
background in one view, sparse stylized trees, and Bengali text that
appears incorrectly oriented. Therefore, the visual-quality requirements
are **not met by the supplied screenshots**, regardless of previous
report wording. The new IDE must inspect actual files and rerun tests
before trusting earlier claims. Do not rely on any filename or commit
hash if it is absent from the real repository.

## 16. First actions in the new IDE

1.  Open the existing repository.
2.  Read this file completely.
3.  Inspect Git and build state without modifying files.
4.  Identify the real Gradle run and test tasks.
5.  Build and launch the game.
6.  Capture a new baseline screenshot.
7.  Make a status table: requirement, evidence, status
    (done/partial/missing/unverified), next action.
8.  Fix black background, lighting, camera, and Bengali text orientation
    first.
9.  Improve the player and one recognizable campus location using
    properly licensed assets.
10. Verify again and commit the milestone.

**Success means a working, verifiable project the new IDE
understands---not merely a report, a concept image, or an FPS number.**
