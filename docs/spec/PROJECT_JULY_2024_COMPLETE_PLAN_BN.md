# PROJECT JULY 2024
## আমাদের সম্পূর্ণ গেম পরিকল্পনা ও সিদ্ধান্তের সংকলন
**Document type:** Complete planning handover / project vision  
**Language:** বাংলা  
**Prepared for:** Project owner and receiving developer / AI coding agent  
**Status:** Working master document — requirements and decisions consolidated from our planning discussions and the supplied “গেম উন্নয়ন পরিকল্পনা.txt”. It is not a verbatim transcript and does not certify that every planned feature has already been implemented.

---

# ১. এই নথির উদ্দেশ্য

এই নথিটি Project July 2024 নিয়ে আমাদের আলোচনার মূল লক্ষ্য, পছন্দ, সিদ্ধান্ত, পরিবর্তন, gameplay, story, locations, visual quality, technical setup, sound, historical research, workflow এবং acceptance criteria এক জায়গায় রাখে। নতুন developer বা AI agent যেন শুধু কয়েকটি prompt নয়, পুরো project vision বুঝে কাজ করতে পারে—এটাই উদ্দেশ্য।

**নিয়ম:** এখানে “চাই/নির্ধারিত” লেখা মানে design requirement; “পরিকল্পিত” মানে এখনও implement হয়েছে বলে প্রমাণিত নয়। বর্তমান code, assets, Git state, build এবং screenshots বাস্তবে পরীক্ষা না করে কোনো feature complete বলা যাবে না।

# ২. Project identity এবং vision

- **নাম:** Project July 2024 (working title)
- **Genre:** Cinematic narrative-driven third-person action-adventure.
- **মূল ভিত্তি:** বাংলাদেশের ২০২৪ সালের জুলাই–আগস্টের ছাত্র আন্দোলন ও তার ঐতিহাসিক প্রেক্ষাপট।
- **গল্পের ধরন:** নথিভিত্তিক ইতিহাসের সঙ্গে একটি কাল্পনিক protagonist এবং কাল্পনিক ব্যক্তিগত গল্প; বাস্তব ঘটনা, fictional content ও dramatization আলাদা করে চিহ্নিত করতে হবে।
- **প্রধান লক্ষ্য:** খেলোয়াড় যেন শুধু ঘটনা দেখে না, বরং একজন সাধারণ শিক্ষার্থীর চোখ দিয়ে সেই সময়ের অনিশ্চয়তা, ভয়, সাহস, বন্ধুত্ব, সিদ্ধান্ত এবং মানুষের অভিজ্ঞতা অনুভব করে।
- **Platform:** প্রথমে PC/macOS; ভবিষ্যতে console support একটি দীর্ঘমেয়াদি ambition।
- **Quality ambition:** AAA-মানের storytelling, presentation ও emotional impact-এর দিকে লক্ষ্য; কিন্তু বাস্তবসম্মতভাবে একটি ছোট, polished, playable vertical slice দিয়ে শুরু করতে হবে।
- **Developer profile:** project owner beginner; coding/3D pipeline-এর সব কিছু আগে থেকে জানা আছে ধরে নেওয়া যাবে না।
- **Current selected engine:** **jMonkeyEngine (Java)**। এটি সর্বশেষ সিদ্ধান্ত। পুরোনো LibGDX implementation বা আগের stack-সংক্রান্ত নির্দেশনা এই সিদ্ধান্তকে override করবে না। jMonkeyEngine-এ ফিরে যাওয়া/থাকা—owner-এর স্পষ্ট সিদ্ধান্ত; অনুমতি ছাড়া engine switch করা যাবে না।

## ২.১ মূল নীতি
1. শক্তিশালী গল্প ও স্মরণীয় চরিত্র।
2. বাংলাদেশের বাস্তবসম্মত পরিবেশ ও বিশ্বাসযোগ্য স্থাপত্য।
3. Smooth third-person movement, camera ও interaction।
4. অর্থবহ mission ও gameplay loop।
5. Original music, sound effects, ambience এবং subtitles।
6. Historical accuracy, source citations ও asset licensing।
7. ছোট milestone, বাস্তব build/run/test এবং Git-এ নিরাপদ পরিবর্তন।

# ৩. Development roadmap — আমরা কোন ক্রমে এগোব

## Phase 1 — Story Bible
- Historical context ও timeline গবেষণা।
- Story premise, tone, protagonist, supporting characters, motivation, conflict, beginning, story arc এবং ending direction।
- Verified fact, disputed claim, fiction ও dramatization আলাদা করা।
- Character relationship এবং dialogue principles নির্ধারণ।

## Phase 2 — Mission and level planning
- Campaign chapters, level, mission, objective, optional side content এবং failure/retry logic।
- প্রতিটি mission-এ location, time, player goal, interaction, tension, gameplay sequence, outcome এবং historical references।
- Real event-এর exact location/timing evidence ছাড়া নির্ধারণ করা যাবে না।

## Phase 3 — World and environment design
- Buildings, roads, trees, vegetation, shops, campus furniture, transport, signage, lighting, weather এবং crowd।
- Photo/reference research এবং location-by-location documentation।
- 2D/3D references, blockout, scale test, collision/navigation, material and lighting plan।

## Phase 4 — Character, script and animation
- Arif ও friends-এর appearance, clothing, proportions, personality, dialogue, Bangla voice direction, subtitles, movement, animation এবং relationships।
- Realistic student appearance; placeholder body থাকলে সেটিকে clearly temporary বলতে হবে।

## Phase 5 — Gameplay and playable prototype
- Third-person controls/camera, exploration, interaction, objective system, companions, mission flow, movement animation, collision, sound এবং save/retry।
- প্রথমে ছোট কিন্তু end-to-end playable slice।
- তারপর visual polish, environment density, mission content এবং additional locations।

## Phase 6 — Production, testing and release preparation
- Remaining missions, assets, animation, voice, music, optimization, accessibility, bug fixing, QA, trailer এবং packaging।
- Console support পরে feasibility ও engine/toolchain অনুযায়ী evaluate করতে হবে।

**Production rule:** Story Bible ও প্রথম level/mission scope না বুঝে পুরো campaign বা অনেক asset একসঙ্গে বানানোর চেষ্টা নয়। আবার শুধু পরিকল্পনা লিখে থেমেও থাকা যাবে না—প্রতিটি অনুমোদিত milestone-এ বাস্তব কাজ, verification এবং evidence চাই।

# ৪. Story foundation

## ৪.১ Protagonist — Arif
- কাল্পনিক ছেলে; ঢাকা বিশ্ববিদ্যালয়ের Political Science বিভাগের second-year student।
- সাধারণ বিশ্ববিদ্যালয় শিক্ষার্থী; trained soldier বা superhero নয়।
- Motivation: fairness, student rights, solidarity এবং বন্ধুদের নিরাপদ রাখা/বিপদের মধ্যে টিকে থাকা।
- Clothing: casual shirt ও jeans/trousers; প্রয়োজনে student ID এবং canvas backpack।
- Character style: realistic, balanced, believable।
- Protagonist fixed থাকবে; customization সীমিত।

## ৪.২ Friends
- **Rafi**
- **Tanvir**
- **Nusrat**
- দুইজন ছেলে ও একজন মেয়ে।
- Arif-এর সঙ্গে আগে থেকেই বন্ধুত্ব এবং mutual trust আছে।
- তাদের ব্যক্তিগত ইতিহাস, private dialogue, family details ও ব্যক্তিগত mission fictional—যদি না কোনো বিষয় সত্যিই reliable source-এ প্রতিষ্ঠিত হয়।

## ৪.৩ Opening
- স্বাভাবিক campus morning দিয়ে শুরু।
- Arif ও তিন বন্ধু natural ভাবে একত্রিত হয়।
- শুরুতেই player control পাবে এবং campus explore করতে পারবে।
- Optional conversations, campus locations ও everyday student life দিয়ে character relationship প্রতিষ্ঠা হবে।
- গল্পের tension ধীরে ধীরে বাড়বে; শুরুতেই অকারণ action sequence দিয়ে character grounding বাদ দেওয়া যাবে না।

## ৪.৪ Campaign scope
গল্প শুধু ২০ জুলাইয়ের একটি দিনের মধ্যে সীমাবদ্ধ হবে না। নাম Project July 2024 হলেও জুলাইয়ের প্রেক্ষাপট থেকে আগস্ট এবং বৃহত্তর ঐতিহাসিক পরিণতি পর্যন্ত যাওয়ার পরিকল্পনা আছে। Timeline research করে সঠিকভাবে সাজাতে হবে।

উচ্চ-স্তরের সম্ভাব্য arc:
1. Campus introduction ও friendship।
2. আন্দোলনের প্রেক্ষাপট ও tension বৃদ্ধি।
3. বিপজ্জনক পরিস্থিতি, সিদ্ধান্ত, অন্যদের সাহায্য, escape/survival এবং solidarity।
4. Research-supported wider locations।
5. ঐতিহাসিক ঘটনাপ্রবাহকে সম্মান করে climax/resolution।

## ৪.৫ Ending direction
- Owner-এর ধারণা: final collective victory/resolution feeling, Ganabhaban route/area এবং Shaheed Minar-এর সঙ্গে যুক্ত ending image/sequence।
- এটি creative direction, নিজে থেকে কোনো historical claim-এর প্রমাণ নয়।
- Exact event, route, timeline ও staging credible sources দিয়ে verify করতে হবে।

# ৫. Historical accuracy এবং research policy

গেমে real history এবং fiction এক করে দেখানো যাবে না।

## ৫.১ প্রতিটি গুরুত্বপূর্ণ দাবির source record
- Claim ID ও exact claim।
- Direct clickable URL।
- Source title এবং publisher/organization।
- Publication date।
- Event date (আলাদা হলে)।
- Source কোন claim সরাসরি support করে।
- Cross-check sources।
- Verification status: `verified`, `supported-but-incomplete`, `disputed`, অথবা `unverified`।
- Limitations এবং last-checked date।

## ৫.২ নিষেধাজ্ঞা/সতর্কতা
- বাস্তব ব্যক্তির নামে fabricated quote দেওয়া যাবে না।
- Date, casualty figure, perpetrator, route, location বা event detail বানিয়ে লেখা যাবে না।
- Source-গুলোর মধ্যে মতভেদ থাকলে তা document করতে হবে।
- Composite/dramatized scene স্পষ্টভাবে চিহ্নিত করতে হবে।
- Real victims-কে fictional props হিসেবে ব্যবহার করলে যথাযথ research ও respectful treatment দরকার।
- Online image দেখা যায় বলে reuse permission আছে—এমন ধরে নেওয়া যাবে না।

## ৫.৩ আগের আলোচনায় উল্লেখিত slogans
নিচের lines আগে আলোচনায় উঠেছিল; এগুলোর exact wording, context, date, location এবং historical appropriateness আলাদা করে verify না করে factual environmental text হিসেবে final করা যাবে না:
- “কোটা না মেধা? মেধা! মেধা!”
- “৩৬ জুলাই — ছাত্র-জনতার ঐতিহাসিক বিজয়”
- “আমার ভাই মরলো কেন? জবাব চাই!”
- “স্বৈরাচার নিপাত যাক, গণতন্ত্র মুক্তি পাক”

# ৬. Locations এবং world scope

## ৬.১ Level 01 — University of Dhaka
বিশেষভাবে আলোচিত/অনুরোধ করা স্থান:
- Curzon Hall এবং আশপাশ।
- Central Library।
- Hakim Chattar।
- Teacher-Student Centre (TSC)।
- Anti-Terrorism Raju Memorial Sculpture।
- Campus paths, roads, roundabout/road crossings যেখানে প্রাসঙ্গিক, trees, campus furniture, tea stall, gathering areas এবং student-life details।

প্রতিটি landmark-এর shape, materials, scale, location, architectural details ও attribution reference দিয়ে verify করতে হবে। Generic cuboid-কে accurate replica বলা যাবে না।

## ৬.২ Wider campaign aspirations
- Jahangirnagar University।
- Research-supported Dhaka locations; আলোচনায় Chankharpul, Rampura ও Jatrabari এসেছে।
- Later campaign/ending concept: Ganabhaban route/area এবং Shaheed Minar।
- Bangladesh-এর wider context থাকবে; game শুধু Dhaka বা Rajshahi-তে সীমাবদ্ধ রাখার ইচ্ছা নেই।

কোন event কোথায় ঘটেছে—এমন নির্দিষ্ট দাবি করার আগে source verification বাধ্যতামূলক।

# ৭. Gameplay design

## ৭.১ Camera and controls
- Third-person, centered player framing।
- WASD movement, mouse camera।
- Walk/sprint, jump, crouch where appropriate, contextual interaction।
- Smooth acceleration/deceleration, believable turn, reliable collision, camera clipping prevention, fall prevention।
- আগের আলোচনায় R = camera reset এবং ESC = menu/pause প্রস্তাব করা হয়েছিল; existing mapping পরীক্ষা না করে তা ধরে নেওয়া যাবে না।

## ৭.২ Gameplay experience
- Hybrid: narrative, exploration, movement, interaction, mission objectives, companion moments এবং action।
- Story/history experience top priority।
- Combat importance medium; combat পুরো game-এর একমাত্র loop নয়।
- Connected mission areas; main missions + optional side missions।
- Exploration প্রথম slice-এ সীমিত ও focused হতে পারে, পরে expand করবে।
- HUD: objective text + optional markers + map/navigation help।
- Clean modern UI এবং readable feedback।

## ৭.৩ Combat and conflict
- Combat feel: **Hybrid** — grounded physical movement/interaction, কিন্তু গুরুত্বপূর্ণ encounter-এ cinematic camera, dramatic animation ও satisfying impact ব্যবহার করা যেতে পারে।
- Movement ও confrontation believable হতে হবে।
- আলোচনায় ছাত্রলীগের সঙ্গে সংঘর্ষের মতো historically important confrontation-এ tension, face-to-face confrontation, defensive movement, dodge, melee encounter, retreat এবং অন্য ছাত্রদের নিরাপদে সরিয়ে নেওয়ার gameplay possibility এসেছে।
- Real event-এর depiction source-verified হবে; disputed claim-কে established fact হিসেবে উপস্থাপন করা যাবে না।
- Historical context ও human dignity বজায় রাখতে হবে।

## ৭.৪ Mission failure
- নির্ধারিত approach: **Checkpoint retry**।
- Objective ব্যর্থ হলে player শেষ checkpoint থেকে retry করতে পারবে।
- Checkpoint placement ও mission-specific retry rules পরে design করতে হবে।

## ৭.৫ Stamina and inventory
- Sprint stamina ব্যবহার করবে; soft limit থাকবে।
- Water stamina recovery-তে সাহায্য করবে।
- Food stamina restore করবে।
- Hunger meter থাকবে না।
- Simple limited inventory: water, first aid, food।
- E key quick-use হিসেবে আগের আলোচনায় এসেছে; current mapping যাচাই করতে হবে।

## ৭.৬ Saves and difficulty
- Checkpoints।
- Autosave + manual save।
- Adaptive difficulty।
- Mission completion/failure/recovery state পরিষ্কার হতে হবে।
- Companion/player collision যেন mission progression block না করে।

# ৮. Visual quality bible

## ৮.১ Target
- Realistic, cinematic 3D action-adventure।
- Quality inspiration: Where Winds Meet, Black Myth, God of War, Cyberpunk/Wildlands ধরনের polished presentation—শুধু broad quality reference; proprietary model, music, UI বা specific assets copy করা যাবে না।
- Sharp, high-resolution detail, convincing materials, believable character movement, coherent composition।
- Final game screenshot-এ debug overlays থাকবে না, যদি না debugging প্রদর্শনের উদ্দেশ্য থাকে।

## ৮.২ Bangladesh summer/monsoon
- July–August বাংলাদেশে summer/monsoon atmosphere উপযুক্তভাবে দেখাতে হবে।
- Lush green natural vegetation; believable local tree silhouettes, leaf shapes, ground cover, tree canopy এবং restrained wind motion।
- আগের output-এ winter/white-grey/washed-out look নিয়ে owner অসন্তুষ্ট হয়েছেন। এমন monochrome/winter-looking grading এড়াতে হবে।
- Natural warm daylight, humidity-appropriate atmosphere, balanced greens এবং coherent color grading।
- Over-saturated/artificial colors নয়।

## ৮.৩ Character realism
- Box body + sphere head final-quality character হিসেবে গ্রহণযোগ্য নয়।
- Properly licensed humanoid model, believable proportions, skin/hair/clothing material, natural shadow এবং movement animation দরকার।
- Arif ordinary Bangladeshi university student হিসেবে readable হবে; casual shirt/trousers, ID/backpack appropriate হলে থাকবে।
- Walk, turn, sprint, stop, interaction ও emotional scene-এর জন্য animation plan।

## ৮.৪ Environment realism
- Campus/building যেন generic blockout না থাকে।
- Recognizable architecture, doors, stairs, windows, columns, façade materials, paths, campus props ও foliage।
- Correct world scale, collision, navigable routes।
- Real references ব্যবহার করতে হবে; inaccurate geometry-কে exact replica বলে দাবি করা যাবে না।
- Sky/background black হয়ে যাওয়া, text mirrored/upside-down হওয়া, missing texture এবং camera clipping—এসব rendering/runtime defects হিসেবে ধরতে হবে।

## ৮.৫ Visual acceptance gate
কোনো scene polished বলা যাবে না যদি:
- Character primitive placeholder থাকে।
- Campus recognizable না হয় বা untextured blocks-এর মতো দেখায়।
- Lighting/background ভুল থাকে।
- Vegetation Bangladesh summer/monsoon-এর মতো না লাগে।
- Bengali text উল্টো/mirrored/অপাঠ্য হয়।
- Camera/collision ভাঙা থাকে।
- Missing textures/rendering artifacts থাকে।
- Running game-এর genuine screenshot দিয়ে প্রমাণ না থাকে।

# ৯. Audio and music plan

## ৯.১ Music
- Original instrumental score।
- Cinematic emotion, restrained orchestral tension, emotional strings, atmospheric layers এবং respectful regional instrumental colors।
- Different emotional states: everyday campus, growing tension, danger, reflection, solidarity, survival এবং resolution।
- Where Winds Meet-এর broad emotional/cinematic quality থেকে inspiration নেওয়া যেতে পারে, কিন্তু exact track, melody, arrangement বা proprietary audio library copy করা যাবে না।

## ৯.২ UI sounds
- প্রতিটি button click/notification-এর জন্য short **1–2 second** original sound।
- Click, confirm, back/cancel, notification, objective update, warning—প্রয়োজনে পৃথক sound identity।
- Clear transient, pleasant decay, no clipping/harsh peaks।
- Google Flow ব্যবহার করার আগ্রহ আছে; user-এর subscription আছে, কিন্তু আগে ব্যবহার করেননি। Prompts/asset workflow এমনভাবে তৈরি করতে হবে যাতে original sound তৈরি ও licensing/usage terms যাচাই করা যায়।

## ৯.৩ Audio organization
- Music, ambience, voice, UI, gameplay effects আলাদা করে organize করতে হবে।
- Asset record: filename, purpose, duration, format, sample rate, channels, source/generation prompt, usage rights/license এবং integration status।
- Bangla voice direction এবং Bangla/English subtitles desired।
- Audio controls/mute categories সম্ভব হলে যোগ করতে হবে।

# ১০. Technical requirements এবং performance

- Engine: jMonkeyEngine (Java) — current authoritative choice।
- Development target machine: MacBook Air M4, 16 GB RAM, Apple Silicon।
- Desired target: 1920×1080 at stable 60 FPS where feasible।
- Performance target guarantee নয়; real running game-এ profile করতে হবে।
- Java/JDK ও Gradle version repository compatibility দেখে নির্ধারণ করতে হবে; শুধু newer বলে সব dependency upgrade করা যাবে না।
- Existing Gradle wrapper first; actual task name `./gradlew tasks` দেখে নিতে হবে।
- Build/test/run logs inspect করতে হবে।
- FPS report-এ resolution, graphics settings, test duration, average frame time, stability/percentiles available হলে, memory use এবং scene details উল্লেখ করতে হবে।
- Brief 60-frame test বা instantaneous FPS counter stable 60 FPS প্রমাণ করে না।
- Performance এবং visual quality-এর মধ্যে measured balance করতে হবে।
- Console support future ambition; বর্তমান compatibility যাচাই না করে promise নয়।

# ১১. Assets, references এবং licensing

প্রতিটি external asset-এর জন্য record:
- Asset name/type।
- Creator/author।
- Direct source URL।
- License/attribution।
- Modification permission।
- Commercial-use permission।
- Restrictions।
- Downloaded/modified/integrated status।
- Placeholder কি না।

Publicly viewable image বা video মানেই free to reuse নয়। Commercial game থেকে proprietary models, textures, music, sound effects বা complete audio library permission ছাড়া extract/reuse করা যাবে না। Asset না থাকলে missing requirement document করতে হবে; placeholder-কে final বলে দেখানো যাবে না।

# ১২. Current known implementation context — VERIFY BEFORE TRUSTING

আগের project report-এ দাবি করা হয়েছিল:
- jMonkeyEngine 3.7.0-stable, LWJGL 3.3.3, Java 17 Corretto aarch64 এবং Gradle 9.6.1।
- `PlayerMesh.java`, `CampusWorld.java`, `BengaliTextRenderer.java`, `ProjectJulyJmeTest.java`।
- DU campus blockout/landmarks, stylized Raju Memorial Sculpture, Central Library/TSC/Hakim Chattar, procedural character, Bengali slogans, vegetation।
- 7/7 tests pass এবং short benchmark ~70.8 FPS; একটি later screenshot overlay-তে 97 FPS দেখা গেছে।
- Commit `57b87e4` message: `feat(graphics): enhance campus architecture, authentic July graffiti decals, student character details, and lush monsoon vegetation`.
- Earlier backup references: `backup/legacy-2x12-vision` এবং `v0.1-legacy-backup` at `14dd97f`; `backup/libgdx-level01-implementation` at `c76130a`.

**এগুলো পুরোনো report-এর claims, স্বাধীনভাবে যাচাই করা তথ্য নয়।** Receiving developer অবশ্যই real repository, branches/tags, code, tests, build, actual runtime, screenshot এবং commit history পরীক্ষা করবে।

Owner-provided screenshots-এ যা দেখা গেছে:
- প্রথম screenshot: sphere head ও blue rectangular body-এর মতো primitive player, blocky grey buildings/columns, black background এবং flat ground; title text prominent; debug stats overlay।
- দ্বিতীয় screenshot: character silhouette অন্ধকার/black, sparse tree canopies, black sky/background এবং Bengali text উল্টো/মিররড দেখাচ্ছে।
- Owner স্পষ্টভাবে বলেছেন, এই output তার visual expectations পূরণ করে না।

অতএব প্রথম visual milestone-এর কাজ হলো live project-এ সমস্যাগুলো reproduce করে root cause fix করা; report-এ “high quality” লেখা আছে বলে সেগুলোকে finished ধরা যাবে না।

# ১৩. Git and handover workflow

Owner চান receiving developer নিজের branch-এ কাজ করুক, main/original branch অক্ষত থাকুক।

1. Repository root এবং Git state যাচাই।
2. Existing changes, branches, tags, remotes এবং recent history inspect।
3. Base branch অস্পষ্ট হলে owner-কে জিজ্ঞেস করা।
4. Appropriate base থেকে নতুন feature branch তৈরি।
5. Destructive command—`reset --hard`, `clean -fd`, force push, history rewrite, branch/tag deletion—owner-এর explicit permission ছাড়া নয়।
6. Small, reversible changes।
7. Meaningful milestone-এর পর build/test/run/screenshot evidence।
8. Git diff/status review করে commit।
9. Report-এ branch name ও commit hash উল্লেখ।

Useful initial commands (repository অনুযায়ী adjust করতে হবে):
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

# ১৪. Milestone acceptance / definition of done

প্রতিটি milestone complete বলতে হলে:
- Real build succeeds।
- Relevant tests actually run; result recorded।
- Game launches through documented run command।
- Changed gameplay/visual behavior tested।
- Console/logs inspected for exceptions, missing assets, warnings।
- Genuine screenshot(s) from running game captured and stored।
- Screenshot before/after comparison করা যায়।
- Performance claim হলে repeatable benchmark/profiling evidence আছে।
- Historical claim হলে source ledger আছে।
- Asset থাকলে license/usage rights documented।
- Git diff/status reviewed এবং commit hash available।
- Partial, missing, blocked ও unverified items পরিষ্কারভাবে বলা হয়েছে।

# ১৫. Receiving developer/AI agent-এর জন্য execution order

## Phase 0 — Audit
- Read this document completely.
- Inspect repository, Git state, Java, Gradle, dependencies, source, assets, tests, tasks, screenshots, logs.
- Verify reported implementation claims.
- Create a requirement status matrix: working / partial / broken / missing / unverified.
- Preserve existing work.

## Phase 1 — Baseline
- Build and run using actual task names.
- Run tests.
- Inspect logs.
- Capture genuine baseline screenshot.
- Record reproducible commands and results.

## Phase 2 — Visual/root-cause fixes
1. Build/runtime issues।
2. Black background, lighting, camera framing, scale।
3. Bengali text orientation and shaping।
4. Player model and animation।
5. Research-guided DU campus geometry/materials/vegetation।
6. Third-person movement, collision, interactions।

## Phase 3 — Playable vertical slice
- Campus morning opening.
- Arif and three friends.
- Free exploration and optional interactions.
- At least one complete objective/interaction loop.
- Reliable movement/camera/collision.
- Coherent summer/monsoon environment.
- Readable UI/Bengali text.
- Checkpoint/save behavior where in scope.

## Phase 4 — Story/research integration
- Historical source ledger.
- Fact-versus-fiction matrix per mission.
- Research-supported locations, slogans, architecture, timeline.
- Bangla/English script/subtitle preparation.

## Phase 5 — Audio/polish/performance
- Original music and 1–2 second UI sounds.
- Ambience, subtitle and font support.
- Profile real gameplay at target resolution.
- Optimize based on evidence.

## Final report format
1. Completed and verified.
2. Partially completed.
3. Missing or blocked.
4. Commands actually run and test results.
5. Genuine screenshot paths.
6. Git branch and commit hash.
7. Next recommended task.

# ১৬. Final instruction to the receiving developer

এই নথিটি পুরোপুরি পড়ে repository inspect করো। শুধু plan বা summary দিয়ে থেমে থেকো না—যে কাজ safe এবং available tools/permissions দিয়ে করা সম্ভব, তা বাস্তবে করো। কিন্তু destructive/irreversible action-এর আগে permission নাও। কোনো result বানিয়ে বলবে না। প্রথমে real state verify করো, তারপর highest-impact verified fixes থেকে কাজ শুরু করো।
