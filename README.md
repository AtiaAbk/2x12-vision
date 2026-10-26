# Project July 2024

A historically grounded, third-person 3D action-adventure set during the July–August 2024
student movement in Bangladesh. Built with **jMonkeyEngine 3.7 (Java 17)**. Working title.

The protagonist Arif and his friends Rafi, Tanvir and Nusrat are fictional. Real events,
places and people are handled under the rules in `docs/spec/`.

## Status (2026-10-25)

This is an early vertical slice, not a finished game. The current state, with evidence, is in
[`docs/STATUS_MATRIX_2026-10-25.md`](docs/STATUS_MATRIX_2026-10-25.md). In short:

- Builds and runs on Windows and (per the original configuration) macOS Apple Silicon; 7 unit tests pass.
- One campus scene (Raju Memorial, Central Library, Hakim Chattar, TSC) built from procedural primitives.
- Third-person camera, movement, stamina, six-step objective loop, Bengali/English HUD, title screen and menus with settings, pause menu, checkpoint save, original synthesised sound.
- The character, buildings and trees are **placeholders** and do not yet pass the visual acceptance gate.
- No music, no inventory, no main menu, no measured performance on the target MacBook Air M4.

## Requirements

- JDK 17 (the Gradle wrapper is pinned to a Java 17 toolchain).
- A GPU with OpenGL 3.2 core profile.
- No system Gradle needed; the wrapper downloads Gradle 9.6.1.

## Build, test, run

Build and run the unit tests:

```bash
./gradlew :game:compileJava :game:test
```

Launch the game (1280×720 window):

```bash
./gradlew :game:run
```

Automated smoke run: render N frames, write a screenshot, print a frame-time summary, exit.
The screenshot goes to `docs/screenshots/project_july_jme_gameplay.png`.

```bash
TEST_EXIT_FRAMES=300 ./gradlew :game:run
```

On Windows PowerShell use `$env:TEST_EXIT_FRAMES=300; .\gradlew.bat :game:run`.

The frame-time summary from this mode is a static scene with no input. It is not a gameplay
benchmark and must not be quoted as one.

## Build a Windows package (app image or installer)

Requires a JDK 17 or newer on Windows (the Gradle JVM must be a JDK, since `jpackage` ships with it).

```bash
./gradlew :game:packageWindowsZip
```

This produces `game/build/installer/ProjectJuly2024/` (a self-contained folder with a trimmed Java
runtime, the jars and the `assets/` folder; about 86 MB) and
`game/build/installer/ProjectJuly2024-1.0.0-windows-x64.zip` for distribution. Start the game with
`ProjectJuly2024.exe`; `ProjectJuly2024-console.exe` is the same game with a console window that
shows the log. Checkpoint saves of the packaged game go to `%LOCALAPPDATA%\ProjectJuly2024\save`.

A real installer (`.exe` or `.msi` with Start-menu entry and shortcut) is one more step:

```bash
./gradlew :game:packageWindows -PinstallerType=exe
```

It needs the free, open-source WiX Toolset 3.x (https://wixtoolset.org) on the `PATH`; without it
`jpackage` stops with "Can not find WiX tools". No installer has been built or tested yet for that
reason. The package is Windows x64 only; the project's primary target, the MacBook Air M4, is not
covered by this task.

Controls are listed in [`CONTROLS.md`](CONTROLS.md).

## Repository layout

| Path | Contents |
|---|---|
| `game/src/main/java/bd/july2024/` | Game code: app, camera, character, dialogue, level, ui, world, audio. |
| `game/src/main/java/com/jme3/` | Copies of four jME LWJGL3 classes shadowing the library versions. Purpose undocumented; under review. |
| `game/src/test/java/` | JUnit 5 tests. |
| `assets/fonts/` | Noto Sans Bengali and Liberation Sans with their licences. |
| `assets/audio/` | Short WAV effects. Provenance to be recorded in an asset register. |
| `docs/spec/` | The approved requirements, story framework, quality bible and developer prompt. |
| `docs/STATUS_MATRIX_2026-10-25.md` | Phase 0 audit. |
| `docs/screenshots/` | Dated, genuine in-engine screenshots. |
| `docs/` (other files), `CHANGES.md`, `LEVELS.md` | Legacy documents from the earlier LibGDX build. See `docs/README.md`. |

## Working rules

See `AGENTS.md` and `docs/spec/04_MASTER_PROMPT_FOR_DEVELOPER.md`. In particular: no feature is
called complete without a build, a run and a genuine screenshot; no historical claim is called
verified without a source ledger entry; no asset is used without a recorded licence.
