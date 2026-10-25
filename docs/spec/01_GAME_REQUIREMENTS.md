# Project July 2024 — Game Requirements

## Purpose
Create a cinematic, narrative-driven third-person action-adventure inspired by Bangladesh's July–August 2024 student movement. The project should be historically responsible, emotionally grounded, playable, and visually convincing. Build a polished vertical slice first; do not claim AAA quality without evidence.

## Technical decisions
- Engine: **jMonkeyEngine (Java)**, explicitly chosen by the owner. Do not switch back to LibGDX without permission.
- First target: PC/macOS; later console support is an ambition, not a guarantee.
- Development machine: MacBook Air M4, 16 GB RAM.
- Desired target: 1920×1080 and stable 60 FPS where feasible; measure real gameplay rather than trusting a brief FPS counter.
- Work on a feature branch based on the correct existing branch. Preserve original branches, tags, history, and uncommitted work. Never reset, clean, force-push, delete branches, or overwrite work without permission.

## Main character and cast
- **Arif:** fictional male second-year Political Science student at the University of Dhaka; ordinary student, grounded personality, motivated by fairness, student rights, solidarity, and survival.
- Clothing: casual shirt, jeans/trousers, student ID, canvas backpack where appropriate.
- Friends: Rafi, Tanvir, Nusrat (two male friends and one female friend). They already know and trust Arif.
- Keep characters believable; no superhero/professional-soldier fantasy.

## Player experience and systems
- Third-person camera, WASD movement, mouse camera, sprint, jump, crouch where appropriate, interaction, reliable collision.
- Check current controls before changing them; R was previously suggested for camera reset, ESC for menu, and E for quick-use.
- Sprint uses stamina with a soft limit; water helps recovery; food restores stamina; no hunger meter.
- Simple inventory with water, first aid, and food.
- Contextual interactions, main missions and optional side missions, objectives, optional markers, map/navigation help.
- Checkpoints, autosave/manual save, adaptive difficulty, clear mission success/failure/recovery.
- Grounded action: evasion, defense, rescue, escape, survival, and protecting others. Combat importance is medium, not the entire game.
- Clean modern HUD; Bangla voice direction and Bangla/English subtitles desired; Bengali text must shape correctly.

## Locations
Level 01: University of Dhaka, including Curzon Hall surroundings, Central Library, Hakim Chattar, TSC, Anti-Terrorism Raju Memorial Sculpture, paths, roads, trees, and campus details.
Wider campaign aspirations: Jahangirnagar University; researched relevant Dhaka locations such as Chankharpul, Rampura, Jatrabari; later ending concept around the Ganabhaban road area and Shaheed Minar.
Do not claim a historical event occurred at a precise location without evidence.

## Historical integrity
Arif, Rafi, Tanvir, Nusrat, private conversations, and personal missions are fictional unless evidence establishes otherwise. Separate verified history from fiction and dramatization. Never invent quotes attributed to real people, dates, casualty figures, perpetrators, or event details. Record conflicting sources rather than silently choosing one.

For every important claim record direct URL, source title, publisher, publication date, event date (if different), supported claim, verification status, limitations, and last-checked date.

Previously mentioned slogans—“কোটা না মেধা? মেধা! মেধা!”, “৩৬ জুলাই — ছাত্র-জনতার ঐতিহাসিক বিজয়”, “আমার ভাই মরলো কেন? জবাব চাই!”, “স্বৈরাচার নিপাত যাক, গণতন্ত্র মুক্তি পাক”—must be researched before use; this document does not certify them.

## Assets
Publicly viewable does not mean reusable. Record creator, URL, license, attribution, modification and commercial-use permissions. Do not extract proprietary models, textures, music, UI sounds, or other assets from commercial games. Clearly label placeholders.

## Definition of a verified milestone
Build succeeds, relevant tests run, actual game launches, changed behavior is tested, logs are checked, genuine gameplay screenshots are captured, Git diff is reviewed, and changes are committed only after verification. Report implemented, partial, missing, and unverified items honestly.
