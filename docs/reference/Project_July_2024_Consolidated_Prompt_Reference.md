# PROJECT JULY 2024
## Consolidated Project Reference & Antigravity Prompt Pack

**উদ্দেশ্য:** `Project July 2024`, উপলভ্য planning materials, decision snippets, `Batch 105` / `v12.97` সম্পর্কে পাওয়া summary, এবং বিদ্যমান Master Game Development Prompt—সবকিছুকে এক জায়গায় এনে Antigravity-তে পরবর্তী prompting সহজ করা।

**তথ্য-সততার নিয়ম:** এই ফাইলটি উপলভ্য source থেকে তৈরি করা consolidated reference। সম্পূর্ণ original `Batch 105` বা `Project Brief v12.97`-এর verbatim document পাওয়া যায়নি। তাই যেখানে সম্পূর্ণ original wording বা approval evidence নেই, সেখানে তা স্পষ্টভাবে চিহ্নিত করা হয়েছে। প্রস্তাবিত radio-option-কে user-approved সিদ্ধান্ত বলে ধরা হয়নি। কোনো অনুপস্থিত decision ID বা wording বানিয়ে পূরণ করা হয়নি।

**Project phase:** Planning / prompt preparation only. User-এর আলাদা স্পষ্ট অনুমতি ছাড়া game code, engine project, engine selection/benchmark, final Master Prompt approval, Git mutation, external upload বা paid service শুরু করা যাবে না।

---

# 1. Source inventory and confidence

## 1.1 যে source content পাওয়া গেছে

1. **`Project_July_2024_Master_Game_Development_Prompt.md`** — 43.9 KB-এর কাছাকাছি Markdown prompt; এর পূর্ণ content এই ফাইলের Appendix A-তে রাখা হয়েছে।
2. **`গেম উন্নয়ন পরিকল্পনা.txt`** — campaign map, সাতটি level/chapters-এর ধারণা, signature scenes, campus map accuracy workflow এবং location-planning নির্দেশনা। এর full text Appendix B-তে রাখা হয়েছে।
3. **Project conversation excerpts / decision snippets** — Project Foundation Decision 01, Scene Recreation Decision 02, Decision 03, Decision 04 এবং world structure, controls, character, technical feasibility, story, graphics ও mission planning-সংক্রান্ত excerpts.
4. **`Batch 105` / `v12.97`-এর বিষয়ে project context summary** — baseline label ও কিছু late-stage decision themes সম্পর্কে summary পাওয়া গেছে; কিন্তু full original register বা exact v12.97 brief পাওয়া যায়নি।

## 1.2 Source status labels

- **Source-backed:** উপলভ্য ফাইলে/কথোপকথনের snippet-এ স্পষ্টভাবে আছে।
- **Working direction:** project context-এ বর্তমান direction হিসেবে সংরক্ষিত; complete original decision text এখানে নেই।
- **Proposed / approval unclear:** option বা recommendation দেখা গেছে, কিন্তু user-এর final selection-এর প্রমাণ নেই।
- **Needs verification:** history, place, date, technical feasibility বা original approval যাচাই করতে হবে।
- **Not recovered:** exact original text/ID এই recovery pass-এ পাওয়া যায়নি।

---

# 2. Project identity and operating boundaries

## 2.1 Core project

- **Name:** Project July 2024
- **Type:** Realistic, cinematic, third-person 3D historical action-adventure.
- **Historical setting:** Bangladesh, July–August 2024 student-led uprising, with the campaign’s major turning point around 5 August 2024.
- **Player perspective:** An ordinary fictional university student—not a superhero, trained soldier, or substitute for a real historical person.
- **Player experience:** The player actively explores, interacts, navigates crowds, helps others, completes missions, makes limited contextual choices, and experiences cinematic scenes. It must not become a cutscene-only interactive documentary.
- **Core emotional question:** When an ordinary person sees history unfolding, will they remain silent, help others, or take the risk of documenting what is happening?
- **Historical result:** Real historical outcomes must not be changed for convenience. Fictional gameplay can vary small consequences, but it must not rewrite the verified outcome of real events.

## 2.2 Strict operating mode

**Current mode: PLANNING ONLY.**

Until the user explicitly approves implementation:
- Do not write gameplay code or generate a full engine project.
- Do not select the game engine or run benchmarks.
- Do not finalize the Master Prompt.
- Do not perform Git add/commit/push/fetch/reset/branch-switch/remote operations.
- Do not upload files to external services or use paid services without permission.
- Do not claim a feature, build, performance result, or historical claim has been tested if it has not.
- Do not overwrite existing files without inspection and an explanation.
- Do not silently drop a requirement; explain the trade-off and offer the closest practical alternative.
- Ask only one concise question when the answer materially changes the outcome. Otherwise state a labelled assumption and record it for later approval.

When implementation is separately authorized, inspect the actual workspace and system first, establish a small prototype, implement one testable feature at a time, and report files changed, how to run, what was tested, known issues, and next steps.

## 2.3 User communication style

- Explain technical decisions in simple, direct language.
- Define necessary technical terms briefly.
- The user is a beginner programmer and wants prompts that make Antigravity work reliably.
- Handle minor planning choices independently; ask only when genuinely important.
- Never fabricate a source, decision ID, test result, location dimension, API, license, or capability.

---

# 3. Priority order and definition of quality

## 3.1 Quality priorities

The source Master Prompt gives this priority order:
1. Recognizable, reference-accurate real-world signature locations.
2. Convincing Bangladeshi environmental identity.
3. Smooth, responsive player movement and controls.
4. Strong lighting, composition, atmosphere, and sound.
5. Clear objectives and complete mission flow.
6. Stable frame rate and minimal critical bugs.
7. Expansion to more locations and missions.

**Important:** Do not lower visual quality by default just to chase FPS. Profile first, identify the bottleneck, and discuss a specific trade-off before reducing quality.

## 3.2 Visual identity

- High-quality, polished, realistic, cinematic presentation; target a credible independent/AA-style vertical slice rather than promising AAA results from a beginner-scale project.
- The world must feel unmistakably Bangladeshi—not a generic Western or generic South Asian stock environment with Bangla signs pasted on.
- Realistic trees, foliage, architecture, materials, street furniture, vehicles, people, animation, camera response, collision, lighting, and sound all contribute to realism. Good-looking buildings alone are not enough.
- Natural summer/monsoon palette for July–August. Avoid winter-like scenes, washed-out white/grey grading, monochrome appearance, overexposure, crushed shadows, neon-green foliage, and excessive saturation.
- Preserve natural colours while using cinematic composition and grading.
- Weather must fit the scene and evidence. Do not make every scene rainy.

## 3.3 Performance target

Working target from the available technical-planning materials:
- **Hardware:** MacBook Air M4, 16 GB unified memory.
- **Resolution:** 1920 × 1080.
- **Frame rate:** stable 60 FPS target.
- **Primary language preference in earlier planning:** Java.
- **Engine:** not selected; jMonkeyEngine and libGDX were candidates to compare, and the existing Master Prompt separately says the actual system and constraints must be inspected before recommending an engine.

1080p/60 FPS is a **target, not a verified result or guarantee**. MacBook Air is fanless; long-run thermal behaviour and unified-memory pressure need testing if implementation is later approved. No benchmark result is currently established in this reference.

---

# 4. Historical integrity and evidence policy

Every important historical scene or claim must be labelled in research notes as one of:

- **VERIFIED** — reliable source support.
- **CORROBORATED BUT INCOMPLETE** — broad event supported, but exact time/place/details remain uncertain.
- **DISPUTED / UNCONFIRMED** — evidence conflicts or does not settle the claim.
- **FICTIONAL GAMEPLAY** — invented protagonist action, combined scene, mission objective, or dialogue.
- **RECONSTRUCTION** — visual/spatial recreation from photos, videos, maps; not a surveyed or officially measured model.

For each major scene record:
- Claim/scene, date and time if known, location.
- Source title, URL, publisher, publication date.
- What the source supports and what it does not establish.
- Confidence label and intended portrayal in the game.
- Open questions and the next verification step.

Rules:
- Do not invent exact dialogue or actions for identifiable real people.
- Use real quotations only if exact and sourced; otherwise use fictional dialogue or clearly labelled paraphrase.
- Do not treat uncertain claims as established fact.
- Do not use graphic violence for shock value or turn real victims into enemies/score rewards.
- Do not generalize an entire political, social, or institutional group as inherently evil; represent specific actions with evidence.
- Show consequences for ordinary people, families, students, bystanders, transport workers, journalists, and medical workers.
- Use a sourced credits/reference section and optional historical context cards.
- Do not imply a reconstructed location/event is exact when evidence is incomplete.

### Helicopter-related caution

Available source material explicitly says that claims of gunfire from helicopters during 19–21 July must not be presented as conclusively proven. The UN/OHCHR investigation could neither confirm nor exclude witness claims of personnel firing rifles/shotguns from helicopters; reports also discuss helicopters dropping tear gas with launchers that may resemble firearms at a distance. Do not conflate verified tear-gas deployment, disputed gunfire claims, and Sheikh Hasina’s later departure by helicopter.

### 5 August / Ganabhaban caution

Ganabhaban may be used as a researched final-level location if the design requires it. However, the exact departure location and chronology of Sheikh Hasina leaving Bangladesh must be researched independently. Do not imply that the departure itself occurred at Ganabhaban if the evidence does not support that. Cinematic transitions must not falsely combine separate places or events.

### Mugdho scene caution

The project context describes a possible mission involving Mir Mahfuzur Rahman Mugdho distributing water to protesters and being shot on 18 July 2024, with Uttara/Azampur mentioned as a possible location. This must be treated as **a scene proposal requiring source verification** for exact time, location, circumstances, and portrayal. Keep the treatment respectful and non-graphic; do not convert a real death into a reward mechanic.

### Unclear location names

The Master Prompt records the names “ঘরোয়া ভবন”, “PSC”, and “IAL Building” as user-mentioned labels needing confirmation/identification. Do not guess their meaning or model them as exact locations until resolved.

---

# 5. World, locations, and campaign direction

## 5.1 Main location set

Source materials identify these signature locations:
- Dhaka University campus.
- Curzon Hall.
- Dhaka University Central Library.
- Raju Sculpture.
- TSC (Teachers–Student Centre).
- Central Shaheed Minar.
- Jahangirnagar University campus.
- Ganabhaban as a proposed final-level location.
- Additional location labels listed above remain unclear and require confirmation.

The campaign may use believable composite Bangladeshi streets/neighbourhoods for generic areas, but named signature landmarks must be researched first.

## 5.2 Campaign structure from the available location-plan file

This is the current **campaign proposal**, not a claim that every level has final approval:

1. **Level 01 — Dhaka University: The Beginning**
   - Exploration, character introduction, light interaction.
   - Potential landmarks: Curzon Hall, Central Library, Raju Sculpture, TSC, Central Shaheed Minar, verified connecting paths.
   - Campus layout must use a real map; do not guess building positions, route connections, or distances.

2. **Level 02 — Jahangirnagar University: The Escalation**
   - Exploration, crowd movement, historically grounded mission.
   - Campus, residential-hall surroundings, and real paths.
   - Events including reported 16 July clashes and students sheltering near the Vice-Chancellor’s residence require source verification before scene scripting.

3. **Level 03 — Streets of Dhaka: The Clash**
   - More action-oriented, but grounded in a verified location/date.
   - Crowd navigation, defensive movement, escape, rescue, contextual melee, and objectives.
   - Every encounter’s location/date must be checked.

4. **Level 04 — Under Fire**
   - High tension, survival, escape/rescue, possible helicopter-related set piece.
   - Portray claims according to evidence; do not present disputed helicopter gunfire as confirmed fact.

5. **Level 05 — The Breaking Point**
   - Curfew, communication disruption, helping injured people, survival and changing circumstances.
   - Exact dates, rules, security-force presence, military role, and any specific orders require verification.

6. **Level 06 — One Point Demand**
   - Large crowd, traversal, narrative climax, one-point demand phase.
   - Verify timeline and wording of the demand; combine playable objectives with cinematic scenes and historical context.

7. **Final Level — Ganabhaban: The Turning Point**
   - Final mission/cinematic climax around the 5 August political turning point.
   - Historical chronology, location distinctions, and departure details must be verified.
   - Ending tone: reflection, release, uncertainty, remembrance—not triumphal violence.

The broader Master Prompt proposes seven chapters, but explicitly says the chapter order is a working proposal and may need splitting, combining, or reordering after building a sourced timeline.

## 5.3 Signature scenes proposed in the location-plan file

- Campus exploration across verified landmarks.
- Large-scale confrontation and crowd simulation.
- Helicopter sequence represented according to evidence.
- Martyrdom/remembrance scenes only after verifying the exact event/location and with respectful treatment.
- Military turning point based on documented information.
- Final Ganabhaban sequence with correct chronology and a victory-themed/reflective resolution.

These are proposed visual/story pillars; do not assume every scene is approved or historically verified.

## 5.4 Location research workflow

Before modelling each signature location:
1. Find official/reliable campus maps.
2. Collect multiple-viewpoint photographs and videos, including 2024-era references if relevant.
3. Record façade, floors, roofline, columns, arches, windows, doors, colour, materials, signage, gates, boundary walls, trees, paths, nearby structures, and sightlines.
4. Label what is verified, obscured, or uncertain.
5. Make a reference sheet with links/screenshots and notes.
6. Build a top-down blockout preserving relative positions and orientations.
7. Compare the blockout against maps and references.
8. Add detailed meshes, materials, vegetation, lighting, decals, props, and crowd paths only after blockout review.
9. Compare in-engine screenshots from matching viewpoints.
10. Record remaining differences.

Do not fabricate exact dimensions. If authoritative measurements are unavailable, reconstruct proportions from permitted maps/photos and label them approximate. Do not copy proprietary map tiles, commercial models, or copyrighted assets into the game without a valid licence.

---

# 6. Environment Art Bible — Bangladesh-specific detail

## 6.1 Vegetation

Research species at each actual location. Potential Bangladesh-appropriate examples to verify against references include rain tree, banyan, peepal, mango, jackfruit, coconut, date palm, areca palm, neem, mahogany, bamboo, shrubs, grass, hedges, garden beds, and local ornamental plants.

For each tree/plant asset, track:
- Species or best-supported identification.
- Trunk/bark, branch pattern, canopy silhouette.
- Leaf shape/size/density and colour variation.
- Height and canopy-width ranges.
- Root flare and ground contact.
- Seasonal appearance and wind response.
- LODs and collision settings.
- Plausible placement and ecological context.

Avoid repeated copy-paste trees. Use silhouette, scale, rotation, canopy-density, and colour variants while ensuring important buildings, paths, signs, and entrances remain visible. Placement around signature landmarks must follow references.

## 6.2 Climate, season, and lighting

- July–August Bangladesh summer/monsoon, not winter.
- Warm, humid daylight, believable sun/shadows, varied green foliage, plausible humidity.
- Puddles, damp surfaces, dust, and rain only when scene/weather supports them.
- Varied monsoon skies by mission; not every scene should be rainy.
- Natural exposure and colours; avoid white/grey washed-out grading.
- Night scenes need realistic street/shop lights, headlights, and navigable dark areas.

## 6.3 Buildings and street identity

For signature buildings, reproduce visible architecture from references. For non-signature buildings, plausible local features include:
- Painted plaster, exposed brick, concrete, terrazzo, ceramic tile, metal shutters/grills, balconies, rooftop water tanks, awnings, and sun-shading.
- Locally plausible heights and shopfronts.
- Gates, guard booths, drains, steps, ramps, railings, utility boxes, rooftop details.
- Correct scale for doors, stairs, windows, balconies, vehicles, and people.
- Subtle weathering, chipped paint, water marks, dust, faded posters, repairs, and uneven pavement.
- Do not add damage or political graffiti to a real landmark without evidence or a clearly fictional label.

For streets and public spaces, use locally plausible rickshaws/cycle rickshaws, CNG auto-rickshaws, buses, motorcycles, bicycles, cars, vendors, pharmacies, tea stalls, signs, banners, benches, drains, manholes, speed breakers, traffic signals, overhead cables, poles, transformers, street lights, Bengali typography, locally plausible number plates and road signs, and appropriate pedestrians. Avoid both generic-city visuals and caricatured “everything is dirty/chaotic” dressing.

---

# 7. Story and character direction

## 7.1 Narrative premise

A fictional university student is busy with studies, family, friendships, and the future when the July–August 2024 historical crisis tests their safety, responsibility, friendship, and ability to preserve the truth. This is a creative proposal, not a real person’s biography.

## 7.2 Protagonist and supporting characters

Available project context summary identifies:
- **Arif:** fictional second-year Political Science student at Dhaka University.
- **Friends:** Rafi, Tanvir, Nusrat.
- Opening concept: “An Ordinary Morning” — a believable Bangladeshi campus morning before the crisis intensifies.

This character set is present in the current project context summary, but a complete verbatim original character decision register was not recovered. Keep names/details as working character notes and do not attribute their actions to real people.

## 7.3 Narrative principles

- Use natural contemporary Bangladeshi student dialogue; Bengali is the primary spoken language unless context requires otherwise.
- Keep dialogue short, human, and situation-appropriate; avoid exaggerated speeches.
- Real quotes must be exact, sourced, and attributed.
- Let silence, ambience, facial expression, and environment carry emotion.
- Separate historical events from the fictional protagonist’s point of view.
- The historical outcome must remain intact; gameplay choices may affect small fictional consequences but not rewrite real history.
- Story should cover context, escalation, curfew/survival, the movement widening, one-point demand, and the 5 August turning point; exact chapter structure remains a proposal pending research.
- The final sequence should include respectful remembrance and sourced credits/context.

---

# 8. Gameplay systems and interaction design

## 8.1 Core gameplay identity

The player must genuinely play a full 3D action-adventure: explore, interact, navigate crowds, protect/escort people, rescue, escape danger, make choices, complete objectives, and progress through missions. It must not be only an interactive documentary or cinematic video.

## 8.2 Core gameplay loop

1. Enter a readable environment.
2. Receive an objective through dialogue, a phone message, or environmental clue.
3. Explore and identify a route.
4. Make a decision or complete an action objective.
5. Respond to crowd/environment changes.
6. Reach a safe point or complete the mission.
7. See a short consequence scene and receive the next objective.

## 8.3 Action boundaries

Prefer:
- Dodge, block, break away, evade, and seek cover.
- Protect/escort an NPC.
- Create distance and find a safe route.
- Distract or redirect attention without harming people.
- Environmental navigation and short contextual defensive interactions.
- Non-lethal conflict resolution where plausible.

Do not build a shooter where the player farms kills. Avoid graphic spectacle and simplistic target-practice enemies based on real-world groups. Any combat-like system should have a clear narrative purpose and be restrained.

## 8.4 Mission design checklist

Every mission should define:
- Title; date/time and location.
- Historical evidence label.
- Player goal and why it matters to the protagonist.
- Start/end scenes, main/optional routes.
- Required interactions and NPC roles.
- Hazards, failure states, checkpoints.
- Dialogue/subtitles, sound cues, lighting/weather.
- Performance budget.
- Historical context card and QA checklist.

## 8.5 Systems for a complete playable build

The existing Master Prompt specifies:
1. Main menu: Start, Continue if save exists, Settings, Credits, Quit.
2. Playable third-person character.
3. Camera and input.
4. Collision and tested navigation.
5. At least one complete level with start, objectives, checkpoints, and ending.
6. At least three meaningful interactions.
7. Dialogue UI with subtitles.
8. Mission objective UI.
9. Pause menu and restart.
10. At least one failure/recovery path that cannot soft-lock the player.
11. Ambient audio and sound effects.
12. Save/checkpoint behaviour or a documented simpler alternative for the first milestone.
13. Credits and historical/source note.
14. Appropriate performance settings.
15. Packaged build or clear launch instructions, depending on environment.

## 8.6 Minimum vertical slice

Do not start by building the whole city/campaign. First milestone, only after implementation authorization:
- One small but beautiful environment.
- One researched signature location or well-defined part of it.
- One complete mission.
- One dialogue/cinematic scene.
- One gameplay challenge.
- One checkpoint.
- One ending transition.
- A reliable restart loop.

---

# 9. Controls, camera, and mission flow — decision snippets

**Important:** The available source contains option lists and prompts asking the user to choose. Unless a final selection is independently visible, the options below are not automatically treated as approved.

## Decision 01 — Project foundation (source snippet)

Options seen:
- Main experience: story-driven adventure / action-focused / survival / hybrid.
- Combat importance: low / medium / high.
- Highest priority: story and historical experience / world exploration / responsive action / realistic graphics and cinematic presentation / balanced.

Project direction elsewhere points toward a hybrid story-driven action-adventure with high historical and visual priorities, but the exact final selection for Decision 01 was not recovered as a verbatim confirmation.

## Decision 02 — Scene recreation (source snippet)

Options seen:
- Playable missions.
- Cinematic scenes.
- Both cinematic scenes and playable missions.

Reference collection options:
- Research reliable footage/images first.
- User provides specific footage/images.
- Both research and user-provided references.

The surrounding source recommends **both cinematic scenes and playable missions**, and both researched and user-provided references. This is a recommendation/source direction; treat as final only if a separate user confirmation is found.

## Decision 03 — Combat and mission failure (source snippet)

Combat options:
- Grounded realistic: believable movement/impact, dodge, block, counter.
- Cinematic action: realistic environment with faster/dramatic combat animation.
- Hybrid: grounded movement and believable impact with cinematic finishers/encounters.

Failure options:
- Checkpoint retry.
- Full mission restart.
- Mixed rules: checkpoints in ordinary missions, stricter conditions in special missions.

The source lists these options, but does not prove which one was finally selected. The Master Prompt later constrains combat toward contextual defence, escape, rescue, and non-lethal play.

## Decision 04 — Controls and camera (source snippet)

Control options:
- Standard PC controls.
- Custom controls.
- Configurable key bindings.

Camera options:
- Over-the-shoulder third-person.
- Centered third-person.
- Dynamic third-person camera.

A nearby recommendation favours configurable controls + dynamic third-person camera, but the exact final approval was not recovered.

## Other world-structure options seen in source

- World: open world / linear campaign / hybrid campaign with separate explorable locations.
- Exploration scope: small detailed areas / medium areas with landmarks and side activities / large areas with extensive exploration.
- Mission flow: fixed story-driven progression / choice-driven alternate mission and character outcomes / mixed—historical outcome fixed, smaller gameplay consequences can vary.

The available project context favours separate but connected-feeling location-based levels and a fixed real historical outcome, with limited variation in smaller fictional gameplay consequences. Exact original selection IDs and final approval wording are not fully recovered.

## Player starting point options seen in source

- Near Curzon Hall.
- Near Central Library.
- At a campus entrance, walking in.
- A verified historical-event location selected after research.

A source recommendation says not to put combat in the first mission; first test movement, camera, interaction, navigation, and dialogue. The exact final spawn-point choice was not recovered.

---

# 10. Technical direction and constraints

## 10.1 Language / engine

Earlier planning strongly preferred Java for the primary game/application logic. jMonkeyEngine and libGDX were discussed as candidates, with benchmarking/feasibility comparison. A previous technical snippet mentions a preliminary jMonkeyEngine 3.9 stable recommendation, but the broader project rules and current authorization boundary say the engine is **not selected** and must not be selected or benchmarked until explicitly authorized.

Do not call the project an Unreal/Unity/Godot project or silently switch language/engine. If the user later changes the constraints, record that change explicitly.

## 10.2 Performance / profiling

- On Apple Silicon, CPU and GPU share unified memory. Monitor total app memory pressure, not just JVM heap.
- Profile CPU-side rendering work as well as GPU load.
- Test long-run performance, not just the first minute.
- Possible optimization variables: shadow distance, foliage density, texture resolution, LOD, visible-object count, instancing, occlusion culling where supported, material efficiency, simpler collision meshes, distant NPC detail, light count, baked/dynamic lighting, streaming/modular levels.
- Use measurements to find bottlenecks; do not guess or degrade all graphics at once.

Performance tiers from the technical-planning snippet:
- **Tier A — Target achieved:** desired visual quality at 1080p/60 target, acceptable frame-time stability in a long run, no critical graphics/gameplay defect.
- **Tier B — Optimization required:** target not sustained but bottleneck identifiable; profile and tune one variable at a time.
- **Tier C — Engine feasibility concern:** renderer/compatibility errors or severe stutter persist in a basic scene after reasonable optimization; evaluate configuration/engine feasibility before building the full game.

No actual Tier A/B/C result is currently verified.

## 10.3 Project documents planned in the original Master Prompt

When implementation is approved and the files become useful, maintain meaningful—not empty—documents:
- `MASTER_PROMPT.md`
- `GAME_DESIGN_DOCUMENT.md`
- `HISTORICAL_TIMELINE.md`
- `RESEARCH_SOURCES.md`
- `LOCATION_REFERENCE_GUIDE.md`
- `ENVIRONMENT_ART_BIBLE.md`
- `ASSET_REGISTER.md`
- `TECHNICAL_ARCHITECTURE.md`
- `MILESTONE_BACKLOG.md`
- `DECISIONS.md`
- `KNOWN_ISSUES.md`
- `BUILD_STATUS.md`
- `QA_CHECKLIST.md`

These are a proposed documentation structure, not a statement that those files already exist in the current workspace.

---

# 11. Audio, dialogue, UI, accessibility, and QA

## 11.1 Audio identity

Plan:
- Campus ambience, birds, local outdoor sound.
- Footsteps on concrete, grass, wet surfaces, stairs.
- Rickshaw bells, traffic, buses, CNGs, horns, street vendors.
- Crowd murmur, chants, announcements, distant commotion only where supported by the scene/reference.
- Indoor reverb and outdoor ambience.
- Rain/wind only where appropriate.
- Radio/phone/news audio only if legally usable and contextually accurate.
- Original or properly licensed music.
- Quiet, respectful memorial soundscape; avoid manipulative over-scoring.
- Do not imply a real recording is from a particular date/location unless verified.

## 11.2 UI

Required:
- Main menu, pause menu, settings.
- Mission title/objective, interaction prompt.
- Dialogue/subtitle UI.
- Checkpoint/retry feedback.
- Optional map/compass if useful.
- Historical context card.
- Credits and sources.

Design:
- Modern, restrained, readable; not generic military-shooter HUD.
- Correct Bengali font rendering if Bangla UI is used.
- Strong contrast, limited clutter, consistent icons.
- No fabricated news graphics or fake quotations.
- No political/national symbolism used merely as decoration.

## 11.3 Accessibility

The source Master Prompt asks for:
- Mouse sensitivity adjustment.
- Key rebinding if feasible.
- Subtitles enabled by default.
- Text-size options if feasible.
- Audio sliders.
- Camera-shake reduction.
- Clear objective markers.
- Checkpoints before difficult sequences.
- No forced long replay after failure.
- Reasonable default difficulty for first-time players.

## 11.4 QA acceptance checklist

A feature is not done merely because code exists. When implementation is authorized, test:
- Game launches.
- Movement, turning, jump if supported, and interaction work.
- Camera works near walls and in narrow spaces.
- Objectives are reachable.
- NPCs do not get stuck.
- Player cannot fall through the world or become trapped.
- Checkpoints and restart work.
- Dialogue, subtitles, and audio work.
- Performance is measured on target hardware.
- Textures/materials/lighting are coherent.
- Environment reads as Bangladesh.
- Signature locations match reference packs.
- Historical claims have sources and evidence labels.
- No missing/unlicensed/placeholder assets appear unintentionally final.
- Bugs include reproduction steps and severity.

---

# 12. Reference assets and copyright

Maintain an asset register with:
- Asset name and type.
- Source/creator.
- Licence and permitted use.
- Intended location.
- Realism/quality notes.
- LOD/collision status.
- Import settings.
- Placeholder/original/final status.

Categories include buildings, architecture modules, trees/vegetation, roads/pavements/drains/walls/gates, local vehicles, signage/posters/banners/shopfronts, player/NPC characters, clothing/bags, animations, audio/music, UI/fonts/textures, and cinematic props.

Do not use ripped game assets, unlicensed music, or copyrighted models without permission. Prefer original assets, appropriately licensed assets, or clearly labelled placeholders. A generic asset is acceptable in distant/background areas when it fits Bangladesh; a signature landmark should not be replaced by a generic lookalike.

---

# 13. Planning sprint and implementation roadmap

## Planning sprint from the original Master Prompt

1. **Project definition:** confirm constraints; resolve ambiguous location labels; define minimum viable game and long-term vision; create a simple glossary.
2. **Historical research:** source-backed July–August 2024 chronology; evidence labels; identify responsibly portrayable missions; record uncertainties.
3. **Signature location research:** maps and reference packs for Dhaka University landmarks, Jahangirnagar University, and final-level location; identify custom assets.
4. **Environment art bible:** vegetation, materials, roads, props, lighting, weather, colour grading, traffic, crowd identity, asset register.
5. **Gameplay/story:** protagonist, supporting fictional characters, mission objectives, dialogue, cutscenes, transitions, rescue/escape/survival.
6. **Technical plan:** compare engine options only when authorized, define systems, folders, performance target, input, UI, saves, build pipeline, and small testable tasks.
7. **Prompt audit:** check contradictions/gaps, milestone backlog, risks, acceptance criteria; implementation kickoff prompt only after user approval.

## Implementation milestones (future, not authorized yet)

- **Milestone 0 — Setup:** engine/version, clean project, blank scene runs, source control, folder structure, launch/build instructions.
- **Milestone 1 — Playable greybox:** third-person movement/camera, collision/interactions, small campus blockout, objective/checkpoint, pause/restart; no expensive final art yet.
- **Milestone 2 — Visual vertical slice:** researched signature location section, Bangladesh vegetation/materials/props/lighting, small crowd and ambience, dialogue/cinematic, optimization and reference comparison.
- **Milestone 3 — Complete first mission:** menus, objectives, checkpoints, ending/restart, three interactions, QA, packaged build.
- **Milestone 4 — Campaign expansion:** next modular location, reusable systems, independent mission testing, context/source notes.
- **Milestone 5 — Polish:** animation, audio mix, UI/subtitles, lighting/colour, performance, accessibility, QA/release checklist.

Protect the ability to run the game at every milestone. If a feature threatens the core playable loop, simplify it instead of leaving the project broken.

---

# 14. Late-stage decision themes in the available project context

The project context summary mentions later decisions and settings covering accessibility, audio, save/load, diagnostics, installer/packaging, asset licensing, QA, screenshots/gallery/photo mode, and related presentation systems. However, the full original decision register for `Batch 105` and `v12.97` was **not recovered verbatim** during this consolidation. The following are the specific screenshot/photo/gallery behaviours present in the available summary; treat them as **summary-derived requirements needing confirmation against the original register**, not as a verified full decision-by-decision transcript:

- F12 screenshot shortcut.
- F10 photo mode.
- PNG output.
- Dedicated screenshot folder.
- Unique filenames; never overwrite existing images.
- No automatic deletion.
- Let user choose save location; provide a way to open the folder in Finder.
- In-game Gallery with search, newest-first sorting, date grouping, preview/zoom, keyboard navigation, and notifications.
- Low-disk-space safeguards and graceful error handling.
- Local-only processing/storage.
- Avoid unnecessary metadata beyond basic file details.
- Watermark off.
- Session-only thumbnail cache with bounded memory.
- Graceful failure and accessibility.
- Hide HUD in screenshots.
- Photo camera orbit around Arif.

Other late-stage decisions are only broadly summarized (audio, saves, settings, QA, licensing, diagnostics, packaging); do not invent their exact option values, decision IDs, or approval status. Recover their original source before treating them as final.

---

# 15. `Batch 105` and `v12.97` — recovery status

- The current project context identifies **Project Brief v12.97, Batch 105** as the planning baseline.
- The exact full text of `Project Brief v12.97` and the original `Batch 105` decision register were not found among the currently accessible source excerpts during this consolidation.
- Searchable source material does include earlier Project Brief drafts (e.g. v0.1, v0.4 and related narrative/technical plans), the 44 KB Master Prompt, the location-plan file, and many decision-option snippets.
- Therefore this file **does not claim to reproduce every one of the 105 batches or every decision through v12.97**. It consolidates all the details that are presently recoverable and preserves a clear gap list rather than fabricating the missing register.
- Do not rename an earlier draft v12.97 or mark any unconfirmed proposal “approved” merely to fill the gap.
- If the original Batch 105/v12.97 file or complete decision transcript becomes available, merge it into this reference while preserving its original IDs, wording, status, and order.

## Recovery gap list

1. Exact complete `Project Brief v12.97` source text.
2. Complete `Batch 1–105` history, with exact batch titles/changes.
3. Complete decision register, original IDs, exact user choices, status, and timestamps.
4. Original wording/approval for Decisions 01–04 and any later option questions.
5. Complete exact contents of decisions 05–107.
6. Full text of decisions 108–257, beyond the limited screenshot/photo-mode summary.
7. Final approved story bible and full historical chronology with evidence citations.
8. Final location-name resolution for “ঘরোয়া ভবন”, “PSC”, and “IAL Building”.
9. Exact confirmation of any technical decision changes after earlier Java/jMonkeyEngine/libGDX planning.

---

# 16. Antigravity prompting protocol

Use the following reusable instruction at the beginning of a planning task:

> Read this entire consolidated reference first, including the source appendices. Work in PLANNING ONLY mode. Do not write game code, create an engine project, select or benchmark an engine, finalize the Master Prompt, or perform Git mutations. Treat this file as a consolidated working reference—not proof that every old decision is final. Preserve all known requirements and original decision IDs. Never invent missing IDs or mark proposals approved without evidence. Clearly separate source-backed facts, working directions, proposals, uncertainties, and fictional gameplay. For a task, first state which relevant requirements you found in this reference, then list only material conflicts/gaps, then propose the smallest useful next step. Ask no more than one concise question if a missing answer materially changes the result; otherwise state a labelled assumption. Do not claim research, testing, file changes, or performance results that have not actually occurred.

## Recommended task prompt template

> **Task:** [one narrowly scoped planning task]
>
> **Source of truth:** `Project_July_2024_Consolidated_Prompt_Reference.md`
>
> **Mode:** Planning only.
>
> **Preserve:** historical integrity, Bangladesh-specific visual identity, exact signature-location workflow, existing requirements, and original decision IDs.
>
> **Do not:** write code, create engine project, choose/benchmark engine, finalize the Master Prompt, or mutate Git.
>
> **Output:** (1) source requirements found, (2) decisions/assumptions used, (3) unresolved issues, (4) proposed result, (5) validation checklist, (6) files that would be changed later—do not change them now.

---

# 17. Final principles

- Accuracy before decoration.
- Playability before scale.
- Evidence before historical claims.
- Reference-driven location work before detailed modelling.
- Natural Bangladesh summer/monsoon colours, not winter-like grading.
- Visual quality is a core requirement; profile before sacrificing it for performance.
- One stable, complete vertical slice before campaign expansion.
- Do not turn real victims into enemies or score rewards.
- Preserve every known requirement and original decision ID.
- Never fabricate missing Batch/Brief content.
- Implementation and Git changes require separate explicit authorization.

---

# APPENDIX A — Existing Master Game Development Prompt (verbatim source)

The full previously generated Master Game Development Prompt is included below to preserve the detailed source instructions. It may contain older wording or proposals; where it conflicts with the current explicit project authorization boundary at the top of this file, the current authorization boundary takes precedence.

# PROJECT JULY 2024

## Master Game Development Prompt --- Planning, World Design, Historical Accuracy, and Playable Implementation

**Document purpose:** This is the single source of truth for planning
and building the game. Give this document to Google Antigravity as the
project brief. The agent must read it fully, follow it as a persistent
specification, and maintain it as the project evolves.

**Project type:** Third-person 3D cinematic narrative action-adventure\
**Working title:** Project July 2024\
**Primary platform:** Windows PC first; controller support and
console-readiness are later goals\
**Primary setting:** Bangladesh, especially Dhaka University,
Jahangirnagar University, and selected real locations associated with
the July--August 2024 uprising\
**Current phase:** Planning and prompt preparation only. Do not start
implementation until the user explicitly says the planning phase is
complete and gives permission to begin coding.

------------------------------------------------------------------------

# 1. ROLE AND OPERATING RULES FOR THE AI AGENT

Act as a senior game director, technical game designer, environment
artist, level designer, historical research assistant, gameplay
programmer, technical artist, audio designer, QA tester, and project
manager.

The user is a beginner programmer and has limited time. Explain
decisions in simple, direct language. Do not assume the user knows
game-engine terminology. When a technical term is necessary, define it
briefly.

Follow these rules:

1.  **Read this entire file before proposing changes.**
2.  **Do not claim that an unverified place, event, building, map,
    quotation, or historical detail is exact.**
3.  Separate verified history, reasonable environmental reconstruction,
    and fictional gameplay clearly.
4.  Never fabricate real building dimensions, exact routes, police
    positions, casualty details, dialogue, or event chronology.
5.  For signature locations, research first, then model. Do not replace
    a named real landmark with a generic lookalike.
6.  The visual target is high-quality and polished, but the project must
    remain achievable by a small team or beginner working with AI
    assistance.
7.  Prioritize a complete, stable, playable vertical slice over a huge
    unfinished map.
8.  Never silently remove a requested feature. If it is too expensive or
    risky, explain the trade-off and offer the closest practical
    alternative.
9.  Use source control, backups, clear file names, and small testable
    changes once implementation is authorized.
10. Before changing existing project files, inspect the repository and
    explain what will be changed. Do not overwrite unrelated work.
11. Do not invent plugin names, APIs, asset licenses, engine features,
    or commands. Verify them against current documentation when needed.
12. Never put secrets, passwords, access tokens, or private credentials
    into the project or this prompt.
13. Do not download or use copyrighted game assets, ripped models, or
    unlicensed music. Prefer original assets, properly licensed assets,
    or clearly documented placeholders.
14. If a task is ambiguous, ask one concise question only when the
    answer materially affects the result. Otherwise, proceed with a
    clearly labelled assumption and record it in `DECISIONS.md`.
15. Keep a `KNOWN_ISSUES.md`, `RESEARCH_SOURCES.md`,
    `ASSET_REGISTER.md`, `DECISIONS.md`, and `BUILD_STATUS.md` once
    project implementation begins.

## Two operating modes

### MODE A --- PLANNING ONLY (active by default)

-   Do not create gameplay code or start a full engine project.
-   Produce the design plan, research plan, location reference list, art
    bible, asset list, story outline, mission flow, technical
    architecture proposal, risk register, and staged implementation
    roadmap.
-   Identify missing information and uncertain landmark names.
-   Do not present concept art as a factual reference.
-   Do not claim the whole game is ready or playable.

### MODE B --- IMPLEMENTATION (only after explicit user approval)

-   First inspect the user's machine, installed engine, current
    repository, and existing files.
-   Recommend one engine based on the actual setup and project
    constraints. Do not assume a specific engine has been installed.
-   Create a tiny working prototype before expanding the world.
-   Build and test one feature at a time.
-   After every meaningful change, report: files changed, how to run it,
    what to test, known issues, and the next step.
-   Run the project and test actual gameplay whenever the environment
    permits. Never claim a feature works if it has not been run or
    verified.
-   Keep the game playable after every milestone.

------------------------------------------------------------------------

# 2. GAME VISION

Create a visually beautiful, immersive, fully playable third-person 3D
narrative action-adventure inspired by the student-led July--August 2024
uprising in Bangladesh.

The game must feel unmistakably Bangladeshi---not like a generic
European, American, or South Asian stock environment with Bangladeshi
signs pasted on top.

The player controls a fictional ordinary university student. The
protagonist is not a superhero or a trained soldier. The player's
strengths are movement, awareness, courage, teamwork, helping people,
making decisions under pressure, navigating crowds, escaping danger, and
completing objectives.

The game should combine: - Free exploration of carefully selected
locations - Cinematic story scenes - Mission-based progression -
Environmental storytelling - Crowd movement and navigation - Rescue,
first-aid support, escort, communication, stealth, escape, and survival
objectives - Limited, contextual action sequences - Dialogue choices
where appropriate - Collectible historical notes and source-based
context - Emotional but respectful remembrance of people who died or
were injured - A historically grounded final chapter connected to 5
August 2024

**Design principle:** The game can be exciting, tense, and enjoyable
without turning real suffering into a score-chasing spectacle. Avoid
rewarding killing, glorifying violence, or making real victims into
enemies. Combat-like moments should be contextual, restrained, and
designed around survival, defence, escape, or protecting others.

## Visual quality target

Aim for a polished independent/AA-style vertical slice with a clear path
to higher quality. Do not promise AAA production values from a one-week
plan or a beginner-scale project.

Quality priorities, in order: 1. Recognizable real-world signature
locations 2. Convincing Bangladeshi environmental identity 3. Smooth and
responsive player controls 4. Strong lighting, composition, atmosphere,
and sound 5. Clear objectives and complete mission flow 6. Stable frame
rate and minimal bugs 7. Expansion to more locations and missions

------------------------------------------------------------------------

# 3. HISTORICAL INTEGRITY AND RESEARCH STANDARD

The historical events are sensitive and must be treated carefully. Build
a source-backed chronology before scripting missions.

## Evidence categories

Every historical claim used in the game must be assigned one of these
labels in the research notes:

-   **VERIFIED:** Supported by reliable sources such as the UN/OHCHR
    fact-finding report, credible reporting, official records, or
    multiple corroborating sources.
-   **CORROBORATED BUT INCOMPLETE:** Several sources support the broad
    event, but exact details, timing, or location remain uncertain.
-   **DISPUTED / UNCONFIRMED:** Accounts conflict or available evidence
    does not establish the claim conclusively.
-   **FICTIONAL GAMEPLAY:** An invented character, combined scene,
    mission objective, or dialogue used to make the game playable. It
    must not be presented as a real quotation or documented event.
-   **RECONSTRUCTION:** A visual or spatial reconstruction based on
    available photos, videos, maps, and references. It is not a surveyed
    or officially measured model.

Use a `RESEARCH_SOURCES.md` entry for each important event: - Claim or
scene - Date and approximate time, if reliably known - Location - Source
title and URL - Publisher and publication date - What the source
actually supports - What it does not establish - Confidence label - How
the game will portray it

Use more than one reliable source for major scenes when possible. Prefer
primary evidence and reputable reporting. Use social-media footage only
when its origin, date, location, and context can be reasonably verified.

## Historical portrayal rules

-   Do not invent exact dialogue for real people. Use verified
    quotations only when properly sourced; otherwise use paraphrase or
    fictional characters.
-   Do not attribute invented actions or statements to identifiable real
    people.
-   Do not imply that a reconstructed scene is exact when the evidence
    is incomplete.
-   Do not show a specific person being killed unless the portrayal is
    well-supported, necessary, respectful, and responsibly
    contextualized.
-   Do not use graphic violence merely for shock value.
-   Do not present unverified helicopter gunfire claims as established
    fact. Research the OHCHR report and other reliable sources;
    distinguish verified tear-gas deployment from allegations or
    uncertain reports of bullets fired from helicopters.
-   Represent police, RAB, BGB, military, political student groups,
    protesters, bystanders, medics, journalists, and other groups
    according to sourced evidence and the specific event. Avoid treating
    an entire population or identity group as inherently evil.
-   Do not imply that all members of any group acted identically.
-   Show the consequences of violence for ordinary people, including
    students, families, shopkeepers, transport workers, medical workers,
    and bystanders.
-   The ending must respect the real historical outcome. The game may
    use cinematic transitions, but must not misstate where or when a
    real event occurred.
-   The game may depict the political turning point on 5 August 2024,
    including Sheikh Hasina leaving Bangladesh, but the chronology and
    departure location must be researched and represented accurately. Do
    not imply that the departure itself happened at Ganabhaban if
    evidence says otherwise.
-   Do not invent a scene in which the military refused an order or
    handled weapons in a particular way unless reliable sources support
    that exact event. If evidence is incomplete, frame the scene as a
    documented public development or use a fictionalized, clearly
    labelled perspective.
-   The game is not a substitute for a historical report. Add a
    credits/reference section and optional historical context cards.

## Starting research sources (verify URLs and use the newest accessible versions)

1.  OHCHR, *Human Rights Violations and Abuses related to the Protests
    of July and August 2024 in Bangladesh*:
    https://www.ohchr.org/sites/default/files/documents/countries/bangladesh/ohchr-fftb-hr-violations-bd.pdf
2.  OHCHR / United Nations in Bangladesh report announcement:
    https://bangladesh.un.org/en/289108-bangladesh-un-report-finds-brutal-systematic-repression-protests-calls-justice-serious
3.  EUAA, *Bangladesh: Country Focus* timeline of July--August 2024:
    https://www.euaa.europa.eu/coi/bangladesh/2025/country-focus/coi-report-bangladesh-country-focus/annex-1-timeline-protests-july-august-2024
4.  The Daily Star timeline:
    https://www.thedailystar.net/news/bangladesh/news/timeline-student-protests-3668996
5.  July Archive timeline: https://www.julyarchive.org/timeline
6.  Jahangirnagar University official website and campus map:
    https://development.juniv.edu/
7.  University of Dhaka official website and campus map:
    https://old.du.ac.bd/footer_widget/footer_widget_item/map
8.  University of Dhaka official TSC contact/location page:
    https://www.du.ac.bd/index.php/officeContact/TSC

These are starting points, not permission to assume every detail is
verified. Check links, dates, source context, and location-specific
evidence before using a claim in the game.

------------------------------------------------------------------------

# 4. LOCATION ACCURACY POLICY

## Signature locations must be recognizable and research-led

Priority locations identified by the user: - Dhaka University campus -
Curzon Hall - Dhaka University Central Library - Raju Sculpture - TSC
(Teachers--Student Centre) - Central Shaheed Minar - Jahangirnagar
University campus - Ganabhaban as a final-level location - The user also
mentioned "ঘরোয়া ভবন", "PSC", and "IAL Building". These names must be
confirmed with the user or reliably identified before modelling. Do not
guess what these labels mean.

Other locations may be built as believable composite Bangladeshi streets
and neighbourhoods if the user has not specified an exact landmark.

## Required workflow for each real location

Before modelling a signature location:

1.  Find the official map or a reliable campus map.
2.  Collect current and, where relevant, 2024-era photographs and videos
    from multiple viewpoints.
3.  Record building frontage, number and rhythm of visible floors,
    roofline, columns, arches, windows, doors, colour, surface
    materials, signage, boundary walls, gates, trees, paths, nearby
    structures, and sightlines.
4.  Identify which features are visible and verified and which are
    obscured or uncertain.
5.  Build a location reference sheet with annotated screenshots or
    links.
6.  Create a top-down blockout that preserves the relative position and
    orientation of the main landmarks.
7.  Check the blockout against the map and reference photos.
8.  Only then add detailed meshes, materials, vegetation, lighting,
    decals, props, and crowd paths.
9.  Compare in-engine screenshots against the references from matching
    viewpoints.
10. Record known differences in the location sheet.

Do not invent exact dimensions. If no authoritative measurements exist,
use proportional reconstruction from maps and photographs, label it as
approximate, and preserve recognisable silhouette and relative layout.

## Real-world map and copyright caution

Use maps and reference imagery as research aids according to their
terms. Do not automatically copy proprietary map tiles, commercial 3D
assets, or copyrighted models into the game. Create original geometry
based on permitted references, and record asset provenance and licence.

------------------------------------------------------------------------

# 5. ENVIRONMENT ART BIBLE --- AUTHENTIC BANGLADESH

The environment must feel natural, lived-in, geographically plausible,
and visually coherent. Avoid the "generic game city" look.

## 5.1 Vegetation and trees

Research the actual plants and trees visible in each specific location.
Do not place random tropical trees everywhere.

Potential Bangladesh-appropriate vegetation to verify against location
references: - Rain trees / Albizia saman where present - Banyan and
peepal trees where appropriate - Mango, jackfruit, coconut, date palm,
areca palm, and other locally plausible species where the location
supports them - Neem and mahogany where present - Bamboo and dense
shrubs only where ecologically and visually plausible - Grass, low
hedges, ground cover, potted plants, and campus garden beds - Seasonal
flowering trees and local ornamental plants, based on reference evidence

For each tree asset, define: - Species or best-supported visual
identification - Trunk shape, bark, branching pattern, canopy
silhouette - Leaf size, shape, density, and colour variation - Height
range and canopy width - Root flare and ground contact - Seasonal
appearance - Wind response and animation amplitude - LOD versions and
collision settings - Typical placement and ecological context

Do not use one repeated tree model across the whole map. Use several
silhouettes, scale variations, rotations, canopy densities, and colour
variants. Avoid obviously repeated "copy-paste" vegetation. Keep paths,
entrances, signs, and important architecture visible. Tree placement
around signature buildings must follow references rather than artistic
guesswork.

## 5.2 Climate, seasons, and light

Default setting should reflect the Bangladesh summer/monsoon season
relevant to July--August 2024---not winter.

Visual goals: - Warm, humid South Asian daylight - Strong but believable
sun and shadows - Green foliage with natural variation, not neon green -
Slight atmospheric humidity where appropriate - Dust, moisture, puddles,
or damp surfaces only where scene and weather justify them - Cloud cover
and monsoon skies varied by mission - Natural exposure; avoid
overexposed whites, crushed shadows, excessive saturation, and a
washed-out monochrome look - Cinematic colour grading that preserves
natural Bangladeshi colours - Night scenes with realistic street lights,
shop lights, vehicle headlights, and dark areas that remain navigable

Do not make every scene rainy. Weather must be intentional and
compatible with the event date and reference evidence. Do not use winter
foliage or cold-weather visual cues for a July--August scene without a
justified narrative reason.

## 5.3 Buildings and architectural language

For signature buildings, reproduce actual visible architecture from
references. For non-signature buildings, use a plausible mix of
Bangladesh urban and campus architecture: - Painted plaster, exposed
brick, concrete, terrazzo, ceramic tile, metal shutters, grills,
balconies, rooftop water tanks, awnings, and practical sun-shading -
Locally plausible building heights and street frontage - Realistic
boundary walls, campus gates, guard booths, drains, steps, ramps,
railings, utility boxes, and rooftop details - Correct scale for doors,
stairs, windows, balconies, vehicles, and people - Imperfections: mild
staining, chipped paint, weathering, dust, water marks, faded posters,
repaired plaster, and uneven pavement - Do not add damage or political
graffiti to a real landmark unless supported by scene references or
deliberately labelled as fictional environmental dressing

Do not use the same building facade repeatedly. Build a small modular
kit with distinct window patterns, balcony types, shutters, rooflines,
shopfronts, and wall materials.

## 5.4 Bangladeshi streets and public spaces

For generic street areas, use grounded local details: - Rickshaws and
cycle rickshaws with locally plausible frames, canopies, decorations,
and proportions - CNG auto-rickshaws, buses, motorcycles, bicycles,
private cars, delivery vehicles, and pedestrians where appropriate -
Roadside tea stalls, small shops, pharmacies, food stalls, signboards,
banners, awnings, benches, and practical street furniture - Concrete
footpaths, kerbs, drains, manholes, uneven road repairs, zebra crossings
where appropriate, road markings, speed breakers, and traffic signals -
Tangled or bundled overhead cables where locally plausible, poles,
transformers, street lamps, and telecom equipment - Locally plausible
Bengali typography, spelling, shop names, notices, number plates, and
traffic signs - Appropriate waste bins, occasional litter, water
puddles, and street wear---without making every place dirty or chaotic -
Street vendors, shopkeepers, students, families, transport workers,
journalists, medical volunteers, and other background NPCs - Correct
street scale, density, and movement patterns

Do not use stereotypical clutter as a substitute for authenticity.
Streets should look composed and visually readable, with variation
between campus, residential, commercial, and major-road areas.

## 5.5 Materials and surface quality

Create a material library for: - Painted plaster - Old and new brick -
Weathered concrete - Asphalt - Footpath slabs - Soil and grass - Metal
shutters and railings - Glass and window frames - Roof tiles or sheet
roofing where appropriate - Wood, fabric awnings, rickshaw paintwork,
banners, and paper posters

Use physically plausible roughness, normal detail, dirt masks, and
colour variation. Avoid overly glossy roads, plastic-looking trees,
perfectly clean walls everywhere, excessive grunge, or repetitive
texture tiling.

## 5.6 Composition and visual readability

Every mission space must be beautiful but readable: - Strong landmarks
and silhouettes for navigation - Clear foreground, middle-ground, and
background - Controlled density; do not let foliage block objectives -
Colour contrast between objective markers and the environment -
Cinematic establishing shots before major scenes - Environmental
storytelling through posters, damaged objects, abandoned belongings,
closed shops, crowd movement, and sound - Optional photo mode only if
time and scope allow

------------------------------------------------------------------------

# 6. CHARACTER, ANIMATION, CROWD, AND INTERACTION

## Player character

Create a believable fictional university student with locally plausible
clothing, footwear, backpack, hair, and accessories. Provide cosmetic
options only if feasible. Do not use a real person's likeness without
permission.

Required movement: - Walk, jog, sprint - Turn and accelerate naturally -
Camera-relative movement - Jump or step over small obstacles only if the
environment supports it - Crouch or take cover if needed by a mission -
Contextual interact - Assist or escort an NPC - Recover from a
fall/stagger in a restrained way - Smooth transitions and responsive
input

The camera should be third-person, stable, and comfortable. Prevent
camera clipping through walls, abrupt snaps, excessive motion blur, and
disorienting shake. Include sensitivity settings and an option to reduce
camera shake.

## NPCs and crowds

Build a scalable crowd system: - Idle, walk, talk, look, react, move
aside, gather, flee, assist, and seek safety behaviours as appropriate -
Clear navigation paths and collision avoidance - Avoid NPCs walking
through walls, vehicles, or each other - Use simple crowd simulation at
a distance and richer behaviour near the player - Avoid huge numbers of
high-detail NPCs if performance suffers - Crowd movement should look
human and varied, not synchronized like robots

## Interaction design

Interaction examples: - Speak with a student or shopkeeper - Read a
notice or historical context card - Pick up a dropped item for a story
beat - Help an injured fictional character reach a safe place - Find an
exit route - Coordinate with a small group - Reach a checkpoint before
conditions change - Choose between a risky shortcut and a safer longer
route

Avoid turning real deaths or injuries into collectibles, achievements,
or entertainment rewards.

------------------------------------------------------------------------

# 7. STORY AND CAMPAIGN STRUCTURE

The story should have a fictional protagonist and supporting cast, while
major public events remain evidence-based. Fictional characters can help
the player experience events without claiming to represent specific real
victims.

Proposed chapter structure; revise only with reasons and evidence:

## Chapter 1 --- Dhaka University: Before the Turning Point

Purpose: introduce the protagonist, movement, exploration, campus
layout, and student life. - Explore selected campus landmarks - Meet
fictional classmates - Learn navigation and interaction - Hear
contextual discussion based on sourced events - End with the tension
rising and a clear next objective

## Chapter 2 --- Jahangirnagar University: A Campus Under Pressure

Purpose: introduce a second real campus with a different spatial and
environmental identity. - Use official/reliable campus maps and
photographic references - Verify dates, incidents, and locations before
depicting a specific clash - Include exploration, communication, rescue,
or escape gameplay - Do not assume every event took place at the same
campus location

## Chapter 3 --- Dhaka Streets: Crowd, Conflict, and Escape

Purpose: transition from campus spaces to Bangladeshi city streets. -
Use a composite street map where exact real street recreation is not
necessary - Include crowd navigation, route selection, safe escort,
communication, and escape objectives - Action sequences must be grounded
in the relevant date and evidence - Keep the player's actions plausible
for an ordinary student

## Chapter 4 --- Curfew and Survival

Purpose: create tension through restricted movement, uncertain
information, limited visibility, and helping people. - Research the
exact dates and rules relevant to the scene - Use sound, distant
activity, checkpoints, blocked routes, and environmental changes - Do
not turn uncertain helicopter reports into confirmed gunfire - Provide a
route or objective that is playable and fair

## Chapter 5 --- The Movement Widens

Purpose: show the shift from specific campus protests to a wider public
movement. - Use sourced chronology and contextual cards - Show varied
participants and ordinary city life affected by events - Keep fictional
dialogue clearly fictional

## Chapter 6 --- One-Point Demand

Purpose: portray the political turning point and the changing nature of
the movement. - Verify the timeline and wording of the demand using
reliable sources - Use a mix of playable objectives, cutscenes, and
historical context - Avoid simplifying a complex event into an invented
single conversation

## Chapter 7 --- 5 August: The Turning Point

Purpose: conclude the campaign around the historical outcome of 5 August
2024. - Use Ganabhaban as a researched final-level location if the
design requires it - Verify the chronology of events, the military's
publicly reported role, and Sheikh Hasina's departure independently -
Use cinematic editing carefully so separate locations/events are not
falsely shown as one event - The emotional tone should be release,
reflection, uncertainty, and remembrance---not triumphal violence - Show
that the end of one political moment does not erase the loss, injuries,
fear, or future challenges faced by ordinary people - End with a
respectful memorial/context sequence, sourced credits, and a clear
historical note

The chapter order is a working proposal. A source-backed timeline may
require splitting, combining, or reordering missions.

------------------------------------------------------------------------

# 8. GAMEPLAY LOOP AND MISSION DESIGN

Each mission must specify: - Mission title - Date/time and location -
Historical evidence level - Player goal - Why the goal matters to the
fictional protagonist - Starting scene and ending scene - Main route and
optional route - Required interactions - NPC roles - Hazards and failure
states - Checkpoints - Dialogue and subtitles - Sound cues -
Lighting/weather - Performance budget - Historical context card - QA
test checklist

## Core gameplay loop

1.  Enter a readable environment.
2.  Receive a short objective through dialogue, phone message, or
    environmental clue.
3.  Explore and identify the route.
4.  Make a decision or complete an action objective.
5.  Respond to changing crowd/environment conditions.
6.  Reach a safe point or complete the mission goal.
7.  See a short consequence scene and receive the next objective.

## Action and combat boundaries

The game may include tense defensive or escape situations, but do not
design it as a shooter where the player farms kills. Prefer: - Dodge,
block, break away, evade, and seek cover - Protect or escort an NPC -
Create distance and find a safe route - Distract or redirect attention
without harming people - Use environmental navigation - Short,
contextual defensive interactions - Non-lethal conflict resolution where
plausible

If any combat-like system is included, explain why it serves the story,
keep it limited, and avoid graphic spectacle. Real-world groups must not
become simplistic target practice.

## Difficulty and accessibility

Include: - Adjustable mouse sensitivity - Key rebinding if feasible -
Subtitles enabled by default - Text size options if feasible - Audio
sliders - Camera-shake reduction - Clear objective markers - Checkpoints
before difficult sequences - No forced long replay after failure - A
reasonable default difficulty for first-time players

------------------------------------------------------------------------

# 9. CINEMATICS, DIALOGUE, AND SOUND

## Cutscenes

Each major cutscene needs a written shot plan: - Shot number and
duration - Camera position and movement - Character blocking -
Background activity - Lighting and weather - Sound and music - Dialogue
or narration - Transition to gameplay - Historical source or fiction
label

Keep cutscenes concise. Use gameplay to communicate whenever possible.
Do not use a long cinematic to hide missing playable content.

## Dialogue

-   Write natural, contemporary Bangladeshi student dialogue.
-   Use Bengali as the primary spoken language unless a scene genuinely
    requires another language.
-   Provide English subtitles or a language setting if feasible.
-   Avoid exaggerated speeches and melodrama.
-   Fictional dialogue must sound human, brief, and
    situation-appropriate.
-   Real quotations must be exact, sourced, and clearly attributed.
-   Do not invent quotes for identifiable real people.
-   Let silence, ambient sound, facial expression, and environmental
    detail carry emotion.

## Audio identity

Create an audio plan for: - Campus ambience - Birds and local outdoor
sound - Footsteps on concrete, grass, wet surfaces, and stairs -
Rickshaw bells, traffic, buses, CNGs, horns, and street vendors - Crowd
murmur, chants, announcements, and distant commotion where supported by
references - Indoor reverb and outdoor ambience - Rain and wind only
where appropriate - Radio, phone, or news audio only when legally usable
and contextually accurate - Original or properly licensed music - Quiet
memorial scenes without manipulative or excessively dramatic music

Never use a real recording as if it were from a particular date or
location unless it has been verified.

------------------------------------------------------------------------

# 10. TECHNICAL DESIGN AND ENGINE CHOICE

The agent must inspect the user's actual system and experience before
choosing the engine. If starting from scratch, compare current stable
versions of Unreal Engine, Unity, and Godot against: - Beginner
usability - Third-person 3D support - Environment and lighting quality -
Asset availability and licences - Performance on the user's computer -
AI coding-agent compatibility - Packaging a Windows PC build - Time
needed to reach a playable prototype

Give one recommendation with a short explanation. Do not start multiple
engines or create a complicated multi-engine workflow.

## Technical requirements

The project should aim for: - Stable third-person controller - Modular
mission logic - Reusable interaction system - Data-driven objectives and
dialogue where practical - Reliable checkpoint and restart system -
Pause menu and settings - Main menu and credits - Audio and subtitle
settings - Keyboard/mouse support first; controller support where
feasible - Scene/level streaming or modular loading if required -
Collision and navigation that are tested, not assumed - Asset naming
conventions and organized folders - Basic crash/error logging -
Performance profiling early

## Performance target

Choose a realistic target after inspecting the machine and engine. Aim
for smooth gameplay on the target hardware, not maximum graphical
settings at all costs.

Use: - Level of Detail (LOD) - Instancing for repeated props and
vegetation - Occlusion culling where supported - Efficient materials and
texture sizes - Collision meshes simpler than render meshes - Controlled
shadow distance and light count - Lower-detail distant NPCs - Baked
lighting or dynamic lighting based on the selected engine and scene
needs - Streaming and modular level design if the world grows -
Profiling rather than guessing

Do not build one enormous seamless city first. Use a hub or level-based
campaign with connected-feeling spaces, loading transitions, and
carefully directed routes. Expand only after the first level is stable
and enjoyable.

------------------------------------------------------------------------

# 11. REQUIRED GAME SYSTEMS FOR A COMPLETE PLAYABLE BUILD

Minimum complete version must include:

1.  Main menu: Start, Continue if save exists, Settings, Credits, Quit.
2.  Playable third-person character.
3.  Camera and input.
4.  Collision and functional navigation.
5.  At least one complete level with start, objectives, checkpoints, and
    a proper ending.
6.  At least three meaningful interactions.
7.  Dialogue UI with subtitles.
8.  Mission objective UI.
9.  Pause menu and restart.
10. At least one failure/recovery path that does not soft-lock the
    player.
11. Sound effects and ambient audio.
12. Save/checkpoint behaviour, or a documented simple alternative if
    full save is beyond the first milestone.
13. Credits and historical/source note.
14. Performance settings appropriate to the engine.
15. A packaged build or clear launch instructions, depending on the
    available environment.

The first playable milestone should be a vertical slice, not the entire
campaign: - One small but beautiful environment - One accurate signature
location or a well-defined section of it - One complete mission - One
cinematic or dialogue scene - One gameplay challenge - One checkpoint -
One ending transition - A reliable restart loop

After this works, expand to the second location and further chapters.

------------------------------------------------------------------------

# 12. ASSET PRODUCTION AND REFERENCE MANAGEMENT

Create `ASSET_REGISTER.md` with: - Asset name - Type - Source/creator -
Licence and permitted use - Target location - Realism/quality notes -
LOD/collision status - Import settings - Whether it is placeholder,
original, or final

Asset categories: - Signature buildings and architectural modules -
Trees, shrubs, grass, and ground cover - Roads, pavements, drains,
walls, gates, and street furniture - Rickshaws, CNGs, buses,
motorcycles, bicycles, and cars - Signs, posters, banners, shopfronts,
and local props - Player and NPC characters - Clothing, bags, and
accessories - Animation sets - Audio and music - UI icons, fonts, and
textures - Cinematic props

Do not use a generic asset if a key landmark is supposed to be exact.
Generic assets are acceptable for distant or non-signature background
areas when they fit Bangladesh.

For every signature location, prepare a "reference pack": - Official or
reliable map - Front, side, and oblique photographs - Street-level route
screenshots - Current reference date - 2024-era references if the scene
requires historical condition - Landmark adjacency and route notes - An
uncertainty list - A list of assets that need custom modelling

------------------------------------------------------------------------

# 13. UI AND PRESENTATION

The user interface should be modern, restrained, and readable. It should
support the story rather than look like a generic military shooter HUD.

Required UI: - Main menu - Pause menu - Settings - Mission title and
objective - Interaction prompt - Dialogue box and subtitles -
Checkpoint/retry feedback - Optional map or compass if useful -
Historical context card - Credits and sources

Design language: - Clean typography - Strong contrast - Limited visual
clutter - Bengali typography that renders correctly if Bengali UI is
used - Consistent icons - Short, understandable text - No fake news
graphics or fabricated real-world quotations - No unnecessary
national/political symbolism used merely as decoration

------------------------------------------------------------------------

# 14. QA, TESTING, AND ACCEPTANCE CRITERIA

A feature is not "done" just because code exists. It is done when it
runs and has been tested.

For each milestone, test: - Can the game launch? - Can the player move,
turn, jump if supported, and interact? - Does the camera behave near
walls and in narrow spaces? - Are all mission objectives reachable? -
Can NPCs get stuck? - Can the player fall through the world or get
trapped? - Do checkpoints work? - Does restart return to a valid
state? - Do dialogue, subtitles, and audio work? - Is the frame rate
acceptable on the target machine? - Are textures, materials, and
lighting coherent? - Does the environment look recognizably
Bangladeshi? - Are signature locations consistent with the reference
pack? - Are historical claims sourced and accurately labelled? - Are
there missing, unlicensed, or placeholder assets that look
unintentionally final?

Use a short QA checklist for each build. Record bugs with reproduction
steps and severity.

------------------------------------------------------------------------

# 15. ONE-WEEK PLANNING SPRINT

The user's current week is for planning and prompt preparation, not
implementation.

## Day 1 --- Project definition

-   Confirm engine/system constraints and target platform.
-   Confirm ambiguous location names: "ঘরোয়া ভবন", "PSC", "IAL
    Building".
-   Define the minimum viable game and the long-term vision.
-   Create a glossary in simple language.

## Day 2 --- Historical research

-   Build a sourced timeline for July--August 2024.
-   Mark verified, uncertain, and fictional elements.
-   Identify which events can responsibly become playable missions.
-   Note chronology and location uncertainties.

## Day 3 --- Signature location research

-   Collect maps and reference packs for Dhaka University, Curzon Hall,
    Central Library, Raju Sculpture, TSC, Central Shaheed Minar,
    Jahangirnagar University, and the final-level location.
-   Create a location sheet for each.
-   Identify which assets need custom modelling.

## Day 4 --- Environment art bible

-   Define vegetation, building materials, road props, lighting,
    weather, colour grading, local traffic, and crowd identity.
-   Prepare a reference-driven asset register.
-   Decide what must be exact and what may be a composite environment.

## Day 5 --- Gameplay and story

-   Write the protagonist, supporting fictional characters, mission
    objectives, dialogue principles, cutscene plans, and mission
    transitions.
-   Keep historical dialogue sourced.
-   Specify action, rescue, escape, and survival mechanics.

## Day 6 --- Technical plan

-   Compare engine options based on the user's actual machine and
    constraints.
-   Define project folders, systems, performance target, input, UI,
    saves, and build pipeline.
-   Break work into small tasks that an AI coding agent can execute and
    test.

## Day 7 --- Prompt audit

-   Review this file for contradictions and missing requirements.
-   Produce a milestone backlog, risks, and acceptance checklist.
-   Create a final implementation kickoff prompt for the next week.
-   Do not start coding until the user explicitly approves.

------------------------------------------------------------------------

# 16. IMPLEMENTATION ROADMAP FOR THE FOLLOWING WEEK AND BEYOND

The full game is not expected to be completed in one week. The following
is a staged plan to prevent scope collapse.

## Milestone 0 --- Environment and project setup

-   Confirm engine and version.
-   Create a clean project.
-   Verify a blank scene runs.
-   Set up source control and project structure.
-   Record launch/build instructions.

## Milestone 1 --- Playable greybox

-   Third-person movement and camera
-   Collision and basic interaction
-   Small greybox campus section
-   One objective and one checkpoint
-   Restart/pause loop
-   No expensive final art yet

## Milestone 2 --- Visual vertical slice

-   Replace the greybox with a researched signature location section
-   Add believable Bangladesh vegetation, surfaces, props, and lighting
-   Add a small crowd and ambient sound
-   Add one dialogue/cinematic sequence
-   Optimize and compare against reference images

## Milestone 3 --- Complete first mission

-   Main menu, objectives, checkpoints, ending, and restart
-   At least three interactions
-   QA pass and packaged build
-   Fix major bugs before expansion

## Milestone 4 --- Expand the campaign

-   Add the next location as a separate modular level
-   Reuse systems, not inaccurate landmark assets
-   Build and test each mission independently
-   Add historical context and source notes

## Milestone 5 --- Polish

-   Animation refinement
-   Audio mix
-   UI and subtitles
-   Lighting and colour grading
-   Performance optimization
-   Accessibility settings
-   QA and release checklist

At every milestone, protect the ability to run the game. If a feature
threatens the core playable loop, simplify it rather than leaving the
project broken.

------------------------------------------------------------------------

# 17. REQUIRED PROJECT DOCUMENTS

When implementation is approved, maintain:

-   `MASTER_PROMPT.md` --- this file, the main specification
-   `GAME_DESIGN_DOCUMENT.md` --- gameplay, story, mechanics, mission
    flow
-   `HISTORICAL_TIMELINE.md` --- dated source-backed chronology
-   `RESEARCH_SOURCES.md` --- citations and evidence notes
-   `LOCATION_REFERENCE_GUIDE.md` --- maps, photos, landmark layout,
    uncertainties
-   `ENVIRONMENT_ART_BIBLE.md` --- vegetation, architecture, materials,
    weather, lighting
-   `ASSET_REGISTER.md` --- asset provenance, licence, quality, and
    status
-   `TECHNICAL_ARCHITECTURE.md` --- engine, systems, folders, build
    process
-   `MILESTONE_BACKLOG.md` --- small, ordered implementation tasks
-   `DECISIONS.md` --- assumptions and user-approved decisions
-   `KNOWN_ISSUES.md` --- known bugs and limitations
-   `BUILD_STATUS.md` --- what currently runs and how to launch it
-   `QA_CHECKLIST.md` --- repeatable test steps

Do not create all these files with empty filler. Each file must contain
useful, current information.

------------------------------------------------------------------------

# 18. FIRST RESPONSE REQUIRED FROM ANTIGRAVITY

When this prompt is first provided, remain in **MODE A --- PLANNING
ONLY** and respond with:

1.  A concise understanding of the game's vision.
2.  The biggest risks and what can realistically be achieved first.
3.  A list of unclear location names or essential decisions.
4.  A proposed research plan for the signature locations.
5.  A proposed one-week planning checklist.
6.  A proposed vertical-slice scope.
7.  Any important facts that require verification.
8.  A list of files/documents that will be prepared during planning.

Do not create game code, generate an engine project, or claim to have
built a playable game at this stage.

When the user later explicitly says "Planning approved; begin
implementation," switch to **MODE B --- IMPLEMENTATION**. Start by
inspecting the actual computer/project setup, confirm the engine, and
implement only the first small milestone. Never attempt to generate the
entire campaign in one giant code change.

------------------------------------------------------------------------

# 19. FINAL QUALITY PRINCIPLE

The goal is not simply a large map. The goal is a world that feels like
Bangladesh, a small number of signature locations that are recognisable
and reference-accurate, a story that respects real history, and gameplay
that is genuinely responsive and complete.

**Accuracy before decoration. Playability before scale. Evidence before
historical claims. Polish before unnecessary complexity.**

The final result should make a player think: "This environment feels
like Bangladesh, these important places are recognisable, the world is
alive, and the game actually plays well."


---

# APPENDIX B — Original uploaded location-plan file (verbatim source)

The following is the original uploaded `গেম উন্নয়ন পরিকল্পনা.txt`, preserved as text for traceability. Its image/widget markup is source formatting, not additional confirmed decisions.

```text
এবার তোমার গেমের structure অনেক পরিষ্কার। তুমি চাচ্ছ **একটি location-based, cinematic 3D action-adventure game**, যেখানে প্রতিটি লেভেলের নিজস্ব বাস্তব লোকেশন, আলাদা gameplay এবং স্মরণীয় দৃশ্য থাকবে। প্রথমে ঢাকা বিশ্ববিদ্যালয়, এরপর জাহাঙ্গীরনগর বিশ্ববিদ্যালয়, তারপর রাজপথে বড় combat missions, এবং শেষ লেভেলে গণভবনকে কেন্দ্র করে ৫ আগস্টের ঐতিহাসিক পরিণতি।

আমার মতে, এই ধারণাটার সবচেয়ে শক্তিশালী দিক হলো—**প্রতিটি লেভেল শুধু নতুন mission নয়, বাংলাদেশের ইতিহাসের একটি আলাদা অধ্যায়ের মতো অনুভূত হবে।**

আমরা তোমার নির্ধারিত লোকেশনগুলো ধরে রাখব। তবে বাস্তব ঘটনার তারিখ, স্থান, সংশ্লিষ্ট বাহিনীর ভূমিকা এবং হেলিকপ্টার বা সামরিক সরঞ্জামসংক্রান্ত দৃশ্যগুলো নির্ভরযোগ্য প্রমাণ দিয়ে যাচাই করে নেব। এতে গেমটি action-heavy হলেও ইতিহাসের সঙ্গে বিশ্বাসযোগ্য সম্পর্ক বজায় রাখবে।

## ১. Revised campaign map — তোমার নির্ধারিত লোকেশন অনুযায়ী

<box gap={3}>
  <box border radius="xl" padding={3} gap={2}>
    <AsyncImage query="University of Dhaka Curzon Hall historic red brick colonial architecture campus green trees Bangladesh realistic photography" aspectRatio="16:9" width="100%" radius="lg"/>
    <title size="lg">LEVEL 01 — Dhaka University: The Beginning</title>
    <text color="secondary" size="sm">Gameplay: Exploration · Character introduction · Light interaction</text>
    **মূল লোকেশন:** কার্জন হল, ঢাকা বিশ্ববিদ্যালয় কেন্দ্রীয় গ্রন্থাগার, রাজু ভাস্কর্য, টিএসসি, শহীদ মিনার এবং যাচাই করা সংযোগপথ।

    - ক্যাম্পাসের মধ্যে অবাধে ঘোরার সুযোগ।
    - ভবন ও গুরুত্বপূর্ণ স্থাপনা শনাক্ত করা।
    - বন্ধুদের সঙ্গে কথোপকথন, ফোন ও mission objectives।
    - আন্দোলনের প্রাথমিক পরিস্থিতির সঙ্গে পরিচয়।

    **বিশেষ শর্ত:** ক্যাম্পাসটি একটি বাস্তব map-এর ভিত্তিতে তৈরি হবে। ভবনের অবস্থান, রাস্তার সংযোগ এবং দূরত্ব যাচাই না করে অনুমাননির্ভর layout তৈরি করব না।
  </box>
  <box border radius="xl" padding={3} gap={2}>
    <AsyncImage query="Jahangirnagar University Bangladesh administrative building campus lake lush green trees open landscape realistic photography" aspectRatio="16:9" width="100%" radius="lg"/>
    <title size="lg">LEVEL 02 — Jahangirnagar University: The Escalation</title>
    <text color="secondary" size="sm">Gameplay: Exploration · Crowd movement · Historical mission</text>
    জাহাঙ্গীরনগর বিশ্ববিদ্যালয়ের বাস্তব ক্যাম্পাস ও আন্দোলনের নথিভুক্ত ঘটনাগুলোকে কেন্দ্র করে আলাদা অধ্যায়।

    - ক্যাম্পাস, আবাসিক হলের আশপাশ এবং বাস্তব সংযোগপথ।
    - ১৬ জুলাইয়ের রাতের সংঘর্ষসহ সংশ্লিষ্ট ঘটনাগুলো যাচাই করে mission design।
    - নিরাপদ স্থানে পৌঁছানো, বন্ধুদের খোঁজা এবং পরিস্থিতি অনুযায়ী objective complete করা।

    ১৬ জুলাই জাহাঙ্গীরনগর বিশ্ববিদ্যালয়ে সংঘর্ষ ও শিক্ষার্থীদের উপাচার্যের বাসভবনে আশ্রয় নেওয়ার ঘটনা সম্পর্কে প্রতিবেদন রয়েছে। নির্দিষ্ট দৃশ্য বানানোর আগে আরও বিস্তারিত উৎস যাচাই করতে হবে। <Cite refs={["turn387615search6","turn387615search1"]}/>
  </box>
  <box border radius="xl" padding={3} gap={2}>
    <AsyncImage query="realistic Dhaka Bangladesh urban road intersection summer daylight overhead cables rickshaws buses shops cinematic third person action adventure environment" aspectRatio="16:9" width="100%" radius="lg"/>
    <title size="lg">LEVEL 03 — Streets of Dhaka: The Clash</title>
    <text color="secondary" size="sm">Gameplay: Combat · Dodge · Rescue · Escape</text>
    এবার gameplay হবে বেশি action-oriented। রাজপথের নির্দিষ্ট, যাচাই করা লোকেশন ধরে combat encounters, crowd navigation এবং mission-based objectives থাকবে।

    - সংঘর্ষের মধ্যে আটকে পড়া থেকে বেরিয়ে আসা।
    - বন্ধু বা অন্য চরিত্রকে নিরাপদ স্থানে পৌঁছে দেওয়া।
    - Close-range combat, blocking, dodging এবং movement।
    - পরিবেশের বাধা ও পরিবর্তিত পরিস্থিতির মধ্যে objective সম্পন্ন করা।

    বাস্তব ছাত্রলীগ, পুলিশ ও অন্যান্য বাহিনীর সঙ্গে সংঘর্ষের ঘটনাগুলো ঐতিহাসিক ভিত্তি হিসেবে থাকবে। তবে প্রত্যেকটি encounter-এর স্থান ও তারিখ যাচাই করে তারপর তৈরি হবে। <Cite refs={["turn387615search1","turn387615search11"]}/>
  </box>
  <box border radius="xl" padding={3} gap={2}>
    <AsyncImage query="cinematic realistic helicopter viewed from street level above Dhaka Bangladesh urban buildings summer daylight documentary style" aspectRatio="16:9" width="100%" radius="lg"/>
    <title size="lg">LEVEL 04 — Under Fire</title>
    <text color="secondary" size="sm">Gameplay: High tension · Survival · Cinematic set piece</text>
    RAB-এর হেলিকপ্টার, আকাশপথে অভিযান এবং সংশ্লিষ্ট সংঘাতের নথিভুক্ত তথ্যের ভিত্তিতে একটি স্মরণীয় mission।

    জাতিসংঘের তদন্তে ১৯–২১ জুলাই হেলিকপ্টার থেকে গুলি চালানোর প্রত্যক্ষদর্শী বিবরণ পাওয়া গেলেও, হেলিকপ্টার থেকে রাইফেল বা শটগান দিয়ে গুলি চালানোর বিষয়টি নিশ্চিত বা সম্পূর্ণভাবে নাকচ করা যায়নি। RAB-এর পক্ষ থেকে tear gas ও sound grenade ব্যবহারের কথা জানানো হয়েছিল। তাই এই দুটি ঘটনাকে এক করে নিশ্চিত ইতিহাস হিসেবে দেখানো যাবে না। <Cite refs={["turn387615search46"]}/>

    Gameplay-তে আমরা বিপজ্জনক আকাশপথের sequence, escape এবং rescue রাখতে পারি; নির্দিষ্ট firing scene-টি প্রমাণের সীমা অনুযায়ী উপস্থাপন করব।
  </box>
  <box border radius="xl" padding={3} gap={2}>
    <AsyncImage query="realistic cinematic Bangladesh city street under curfew summer afternoon quiet shops closed concrete buildings natural colors" aspectRatio="16:9" width="100%" radius="lg"/>
    <title size="lg">LEVEL 05 — The Breaking Point</title>
    <text color="secondary" size="sm">Gameplay: Survival · Stealth · Decision-making</text>
    কারফিউ, যোগাযোগ বিচ্ছিন্নতা, আহত মানুষকে সাহায্য এবং আন্দোলনের পরবর্তী পর্যায়ের দিকে এগোনো।

    এই অধ্যায়ে নিরাপত্তা বাহিনীর উপস্থিতি, সেনাবাহিনীর ভূমিকা এবং বিভিন্ন ঘটনার সুনির্দিষ্ট chronology যাচাই করে sequence তৈরি করব। সেনাবাহিনীর কোনো নির্দিষ্ট আদেশ প্রত্যাখ্যানের দৃশ্য থাকলে সেটির নির্ভরযোগ্য ভিত্তিও দরকার।
  </box>
  <box border radius="xl" padding={3} gap={2}>
    <AsyncImage query="cinematic realistic Bangladesh student led mass public gathering Dhaka wide avenue August daylight hopeful tense documentary inspired game environment" aspectRatio="16:9" width="100%" radius="lg"/>
    <title size="lg">LEVEL 06 — One Point Demand</title>
    <text color="secondary" size="sm">Gameplay: Large-scale crowd · Traversal · Narrative climax</text>
    এক দফা দাবির পর্যায়কে campaign-এর turning point হিসেবে উপস্থাপন করা হবে। খেলোয়াড় বড় জনসমাগম, বিভিন্ন objective এবং protagonist-এর ব্যক্তিগত সিদ্ধান্তের মধ্য দিয়ে এগোবে।
  </box>
  <box border radius="xl" padding={3} gap={2}>
    <AsyncImage query="Bangabhaban Ganabhaban former Bangladesh prime minister official residence exterior gates grounds Dhaka realistic architectural reference" aspectRatio="16:9" width="100%" radius="lg"/>
    <title size="lg">FINAL LEVEL — Ganabhaban: The Turning Point</title>
    <text color="secondary" size="sm">Gameplay: Final mission · Cinematic climax · Victory-themed ending</text>
    তোমার নির্ধারিত final location হবে গণভবন। ৫ আগস্ট ২০২৪-এর ঐতিহাসিক রাজনৈতিক পরিবর্তনকে কেন্দ্র করে শেষ mission তৈরি হবে।

    - শেষ পর্যায়ের narrative objectives।
    - বড় জনসমাগম ও উত্তেজনাপূর্ণ cinematic sequence।
    - শেখ হাসিনার দেশত্যাগের ঘটনাকে ঐতিহাসিক chronology অনুযায়ী উপস্থাপন।
    - আবেগপূর্ণ original soundtrack ও campaign resolution।

    **গুরুত্বপূর্ণ:** গণভবনকে final playable location হিসেবে ব্যবহার করা আর শেখ হাসিনার দেশত্যাগের সঠিক দৃশ্যগত স্থান ও সময় এক বিষয় নয়। দুটিকে cinematic editing দিয়ে যুক্ত করা যেতে পারে, কিন্তু এমনভাবে নয় যাতে বাস্তব ঘটনা কোথায় ঘটেছিল তা নিয়ে ভুল ধারণা তৈরি হয়। <Cite refs={["turn387615search1","turn387615search2"]}/>
  </box>
</box>

## ২. তোমার signature scenes

আমার মতে, campaign-এর মধ্যে কয়েকটি দৃশ্য অবশ্যই আলাদা করে design করা উচিত:

- **Campus exploration:** কার্জন হল থেকে কেন্দ্রীয় গ্রন্থাগার ও অন্যান্য বাস্তব landmark পর্যন্ত seamless exploration।
- **Massive confrontation:** রাজপথের বড় সংঘর্ষ, crowd simulation, responsive combat ও cinematic camera।
- **Helicopter sequence:** হেলিকপ্টারের উপস্থিতি, শব্দ, পরিবেশগত আতঙ্ক এবং প্রমাণ অনুযায়ী উপস্থাপিত ঘটনা।
- **Martyrdom and remembrance:** শহীদদের স্মরণ, জানাজা বা সংশ্লিষ্ট ঐতিহাসিক দৃশ্য—নির্দিষ্ট ঘটনা ও স্থান যাচাই করে।
- **Military turning point:** সেনাবাহিনীর ভূমিকা এবং রাজনৈতিক পরিস্থিতির পরিবর্তন নথিভুক্ত তথ্য অনুযায়ী।
- **Final Ganabhaban sequence:** বড় cinematic build-up, ইতিহাসসম্মত chronology এবং victory-themed ending।

এগুলোকে আমরা গেমের visual identity হিসেবে ধরব। তবে বাস্তব নিহত ব্যক্তিদের মৃত্যু বা পরিবারের শোককে সাধারণ combat reward কিংবা entertainment spectacle বানাব না। এতে গেমের emotional impact আরও শক্তিশালী হবে।

## ৩. এখন সবচেয়ে জরুরি কাজ: বাস্তব ক্যাম্পাসের map

Level 1 ও Level 2-এর ক্ষেত্রে তোমার নির্দেশটি খুবই গুরুত্বপূর্ণ: **আমরা নিজেরা কল্পনা করে ঢাকা বিশ্ববিদ্যালয় বা জাহাঙ্গীরনগর বিশ্ববিদ্যালয়ের layout বানাব না।**

পরবর্তী ধাপে প্রতিটি ক্যাম্পাসের জন্য আলাদা reference package তৈরি করতে হবে:

1. Satellite map এবং সঠিক রাস্তার সংযোগ।
2. ভবনের বাইরের ছবি ও architecture reference।
3. গুরুত্বপূর্ণ landmark-এর অবস্থান।
4. গেমে খেলোয়াড় কোথা দিয়ে প্রবেশ করবে, কোন পথে যাবে এবং কোথায় mission শুরু বা শেষ হবে।
5. বাস্তব ঐতিহাসিক ঘটনার সঙ্গে সংশ্লিষ্ট নির্দিষ্ট স্থান ও তারিখ।

এই তথ্যের ওপর ভিত্তি করেই আমরা playable map-এর scope ঠিক করব। বাস্তব ক্যাম্পাসের প্রতিটি বর্গমিটার প্রথম build-এ বানানো জরুরি নয়; বরং প্রথমে গুরুত্বপূর্ণ পথ ও landmark-গুলো সঠিকভাবে তৈরি করা বেশি কার্যকর।

**আমার পরবর্তী প্রস্তাব:** আমরা আগে Level 1 — Dhaka University-এর একটি detailed location plan তৈরি করি। কার্জন হল, কেন্দ্রীয় গ্রন্থাগার, রাজু ভাস্কর্য, টিএসসি ও শহীদ মিনারের মধ্যে সঠিক spatial relationship, entry point, exploration route এবং প্রথম mission-এর অবস্থান নির্ধারণ করব। এই map অনুমোদিত হওয়ার পরেই আমরা Level 1-এর 3D environment design-এ যাব।
```
