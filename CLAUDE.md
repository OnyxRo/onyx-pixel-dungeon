# Onyx's Pixel Dungeon

Notes for Claude Code sessions on this project. Read this before making changes.

## About the owner

Onyx Rothman (GitHub OnyxRo) is not a professional programmer. He builds by describing what he wants, then playtests on Windows. Explain things plainly, make changes yourself, and tell him exactly what to click or run.

His writing rules apply to anything written for him, including commit messages, README text, in-game strings and docs.

- Never put a colon in the middle of a sentence.
- No em dashes or similar dashes.
- Never use the word "real" as an intensifier, or "genuinely".
- No small eyebrow or kicker labels above headings.
- Never use the interpunct character.

## Working rules

- Read PORTING.md at the start of every session and update it at the end of every task.
- At the start of every task (porting, features, anything that changes game code), run `python -I scripts/crashes.py`. If any crash is open, fix it first with the `fix-bugs` skill, then do the task. Tell Onyx which crashes were fixed in the final message.
- To check a port, use `scripts/check.sh --new` (headless, seconds) and then `scripts/arena.sh` once (one picture of everything new, 16 seconds). Do not use the full screenshot tour for monsters, it spawns each ported monster one at a time and takes ten minutes. The tour (`scripts/gallery.sh`) is for worlds and windows only, with the separate save folder.
- For any task that starts with "Port", invoke the `port-mod` skill (`.claude/skills/port-mod/SKILL.md`) first. It has the tools, the order of work and the mistakes to avoid. Rewrite it when you learn something that would have saved time.
- Write files that contain backslashes with the Write or Edit tools, not shell heredocs. Run Python as `python -I`.
- Always open the game when you finish a task, so Onyx can see the result. Do it once at the end, never once per monster or per check (checks stay headless, see above).
- Keep command output short. Send long output to a file and grep it.
- Keep context small, it is what Onyx's usage goes on. Every turn re-reads the whole conversation, so a run that grows to 500k tokens costs ten times as much per step as a fresh one. Find with `grep -n`, then read only the lines you need with `sed -n`. Do not cat whole files, logs, PORTING.md or TASKS.md.
- Never give one agent a whole mod or a long list. Split the work into batches (see Batches in the port-mod skill), one agent each, and have each agent commit, write a short handoff and stop when its batch is done or at about 150 tool calls.
- When a task is finished or the conversation has run long, tell Onyx it is a good time for /clear before starting the next thing, instead of carrying on in the same session.
- Commit after each finished mod or feature.
- Keep TASKS.md up to date as you go. Check a task off the moment it is finished, note partial progress under batched tasks, and add any new work Onyx asks for to the list.
- Remind Onyx to type /clear after every response.
- Never read, write or delete anything in %APPDATA%\.onyxrothman (Onyx's real saves) or anywhere else outside this repo. The only exceptions are the `Onyx Gallery` test save folder used by the screenshot tour, the mod sources folder `~/mod-sources` (read only, for porting), this session's scratchpad folder, and the normal Gradle and Java caches that builds use.
- Never force-push and never delete branches, local or remote.
- Never replace, rename or edit anything in `Documents\OnyxPixelDungeon-app` by hand, and never swap in a jar built from another mod's repo. Ports go into this repo's source, never into the installed app. On 2026-10-15 another session swapped Ryuse's 26 MB jar over the 779 MB game jar (it was restored from the backup, nothing was lost).
- When a task finishes and game code changed, commit it and make sure the desktop app is updated. The post-commit hook does this by itself. Check `build/auto-update.log` for "desktop app updated", and if it did not run, run `powershell -ExecutionPolicy Bypass -File scripts\auto-update-desktop.ps1 -Force`. Tell Onyx whether the update happened.

## What the game is

A fork of Shattered Pixel Dungeon v4.0.2 called Onyx's Pixel Dungeon. Tagline "Every floor a new world."

- **World shuffle.** Most floors are built in a world other than their usual one. There are 54 worlds, Shattered's five plus 49 ported from 14 other mods. See `core/.../levels/WorldShuffle.java`.
- **Stage matching.** Onyx wants each floor to use a world made for that stage, not a deep world scaled down. Each world has a region in `WorldShuffle.REGIONS`, taken from where its mod used it, and a shuffled floor only picks from its own region's worlds. Transformation's tower styles are `ANY_REGION` and share one slot. Shattered's own five now only appear on their fixed floors. When porting monsters later, use each mod's monsters for that same stage.
- **Depth sets danger.** The world sets the look (tileset, water, decoration painter, ambient effects, music). Monsters, loot and traps always follow the actual depth. Ported worlds always use their floor's usual trap list.
- **Fixed floors.** Floor 1, boss floors (5, 10, 15, 20, 25), floor 26, and quest floors 4, 9, 14, 19 never shuffle (though a boss floor can hold a ported mod's boss, see below), so the Ghost, Wandmaker, Blacksmith and Imp quests always appear.
- **Mods picker.** On the hero select screen the Mods button (`WndChooseMods`) picks which mods a run's shuffled floors come from, stored in `Dungeon.chosenMods` (empty means all).
- **Game modes.** The Game modes button on the hero select screen (`WndGameModes`) holds whole-run modes from mods that are game modes rather than content. Half PD and Even Shorter PD (shorter dungeons through `GameModes.nextDepth`), Radar's playstyles and required items seed search, Custom PD's starting kits and Too Cruel's modifiers. `GameModes` keeps them per run and in settings, `GameModes.register` adds more.
- **Seeded.** Rolls come from the dungeon seed. Appending to `WorldShuffle.WORLDS` is safe, reordering changes which world a seed rolls.
- **Arrival message.** Entering a ported world logs "You enter X, a world from Y" (`WorldShuffle.announce`, called from `GameScene`).
- **Mod logo.** While in a ported world, the source mod's app icon and short name show at the top left of the game screen (`GameScene.create`, `WorldShuffle.modLogo`). Clicking the icon opens `WndModInfo`, a short summary of what the mod changed taken from its GitHub page (text in `windows.properties` under `windows.wndmodinfo.*`, keyed by the mod name with only letters and digits kept). Icons are in `assets/interfaces/mods/`, taken from each mod's repo at 32x32. A new mod needs an icon there and an entry in `WorldShuffle.LOGOS`.
- **Branding.** Title screen draws "ONYX'S" over the banner art (`TitleScene.addTitleName`, banner edited in `interfaces/banners.png`), with the tagline below. App name "Onyx's Pixel Dungeon", package id `com.onyxrothman.onyxpixeldungeon`, saves in `%APPDATA%\.onyxrothman`, separate from Shattered. Java packages are still `com.shatteredpixel...`, which is fine.
- **Locked start for friends (2026-11-02).** A copy from GitHub starts as plain Shattered until the player beats Yog on floor 25, then every mod opens at once (`ModsUnlock`). Onyx's desktop copy is built with `-Donyx.dev=true` (`make-exe.ps1`), so it always has everything plus the sandbox. The Friend view button at the top left of his title screen shows the shared copy, with its own unlock kept apart from his (Start over relocks it). While locked, `WorldShuffle.shuffles` is false, only Shattered's six heroes, nine challenges, recipes, rooms, enchantments, badges and items appear (`ModsUnlock.isOriginal` checks `assets/shattered-classes.txt`, every class and badge of commit 0e810728), and about 60 mod hooks inside Shattered's own code are gated with `ModsUnlock.locked()`. Any new mod hook that fires in a plain Shattered floor needs the same gate. `LockedModeTest` (in `check.sh`) builds whole locked dungeons and fails on anything from the mods. The headless tests and the tour run as Onyx's copy.
- **Desktop only.** Android and iOS modules are off in `settings.gradle`. Update and news services point at debug stubs so the game never offers Shattered's updates.

### Ported worlds, mods done, and what is left out

The per mod history (which worlds and monsters came from where, what was left out and why, the old tile layout fixes, the Shattered fixes) is in `docs/game-notes.md`. Search it, do not read it whole.

## Building and testing

Normal path on a PC with internet: `./gradlew desktop:debug` to run, `./gradlew desktop:release` for a jar. This Gradle setup was edited (Android plugin and the `org.beryx.runtime` plugin removed, `application` plugin used) but never run in the original build environment, so fix it if it fails.

Onyx's playable copy. `scripts\make-exe.ps1` builds the release jar, wraps it with jpackage into `Documents\OnyxPixelDungeon-app\OnyxPixelDungeon\OnyxPixelDungeon.exe` (about 200 MB, with its own Java) and puts an "Onyx's Pixel Dungeon" shortcut on his desktop. It shares the normal save folder. The app has to be closed before the script can replace it. Because the app runs from its own copy, the repo can be rebuilt while he plays.

Desktop copy updates by itself. A git hook (`scripts/hooks/post-commit` and `post-merge`, switched on with `git config core.hooksPath scripts/hooks`, already set in this clone) runs `scripts/auto-update-desktop.ps1` in the background after any commit or merge that changed game code. It runs `make-exe.ps1`, so the desktop shortcut always points at the newest build. It never starts the game. If the game is open it skips and tries again at the next commit. Progress and errors are in `build/auto-update.log`. A failed build leaves the old app alone. Run `powershell -ExecutionPolicy Bypass -File scripts\auto-update-desktop.ps1 -Force` to update by hand. A fresh clone needs `git config core.hooksPath scripts/hooks` once. Builds made on the cloud routine cannot reach the desktop, merge and pull on this machine to get them.

Gradle-free path used for releases (`scripts/`):

- `scripts/compile.sh <ShatteredPD-v4.0.2-Java.jar> <outdir>` compiles with javac, taking libraries from the official Shattered jar.
- `scripts/package-windows.sh <Java.jar> <Windows.zip> <outdir>` makes the full Windows zip, the small installer zip, the cross-platform jar, `OnyxPixelDungeon-game.jar` and `update.json`.
- **The screenshot tour deletes and overwrites save slot 1 of whatever save folder it runs in.** On 2026-10-13 a tour started straight from the release jar used Onyx's real save folder, because the jar's manifest title overrode the title passed on the command line, and it wiped his slot 1. `DesktopLauncher` now forces the tour into the `Onyx Gallery` folder and `Gallery.active()` refuses to run without that. Do not weaken either check.
- `scripts/gallery.sh <folder>` runs the screenshot tour (`Gallery.java`, switched on by `-Donyx.gallery`). The game plays itself through every ported world and the sandbox windows and saves a picture of each, in its own save folder. Use it to look at worlds and UI without Onyx clicking through them. Read the pictures back to check. After the worlds it spawns every ported monster beside the hero, lets it act, pictures it and kills it. Add `-Donyx.gallery.only=mobs` to the java line to skip the worlds. All 49 ported worlds, the sandbox windows and Devoted's 29 monsters were checked this way on 2026-10-13 with no errors.
- **Porting and testing tools (2026-10-14).** `scripts/check.sh` compiles and runs `WorldShuffleTest` plus `MobArenaTest`, the headless testing ground (`scripts/test/`, with `TestBoot` for the window stubs and `mob-baseline.txt` for known harness limits). `scripts/arena.sh` shows chosen monsters together in one picture. `scripts/mod-report.py` plans a port, `scripts/port-mob.py` ports a batch of monsters in one command, `scripts/get-mod.sh` clones from `scripts/mods.txt`, `scripts/reforged-stats.py` helps with table based stats. `ModRoster` is the single place a ported monster is registered, one line makes it show in the bestiary and sandbox list and get tested. What a mod's world spawns comes from the mod's own table, copied by `scripts/mod-spawns.py` into `ModSpawns` (2026-11-01). Full details in the `port-mod` skill.
- `scripts/test/WorldShuffleTest.java` builds every floor of N seeded dungeons headlessly, checks all 24 worlds appear and have strings, and saves and reloads each ported world. Run it after any world change.

## Releases and updates

- `.github/workflows/release.yml` builds, runs the test on 1000 seeds, packages and publishes a GitHub release. Bump `appVersionName` in `build.gradle`, then on GitHub go to Actions, Build and release, Run workflow. It tags the release from the version.
- Windows auto-update. `scripts/windows-installer/Play.ps1` (run by Play.bat and the installer's desktop shortcut) checks the latest release's `update.json`, downloads `OnyxPixelDungeon-game.jar` if the version differs, verifies its SHA-256 and swaps it in. If the release needs a different Shattered base, it tells the player to rerun the installer instead. Keep the asset names stable.
- The game jar is loaded ahead of the official Shattered jars on the launcher classpath, so it only needs the game's classes and changed assets.

## Mods to add next and the sandbox plan

The candidate mod list, how to judge a mod, and the sandbox plan are in `docs/game-notes.md`.

## Crash list

Every crash on this computer is added to `crashes/crashes.jsonl` in the main project folder (`desktop/.../CrashLog.java`, git-ignored, screenshot tour crashes are left out). When Onyx says "fix bugs", use the `fix-bugs` skill, which reads the list with `python -I scripts/crashes.py`, fixes each open crash and marks it fixed. The crash box used to show "Invalid message with quotes" for every crash because of the apostrophe in the game title, fixed on 2026-10-23.

## Improving the process

- Edit code with `scripts/edit.py` (a plain text spec, see its header), not shell heredocs or sed. Backslashes in heredocs got mangled repeatedly.
- At the end of every task, follow the improvement loop in `.claude/skills/port-mod/SKILL.md`. Turn anything that cost time into a tool change, and add a short dated entry to `RETRO.md`. This applies to Daniel's cloud runs as well.

## Working faster (added 2026-10-15 after a 2 hour crash hunt that should have taken 40 minutes)

- **Ask for symptoms first.** When Onyx says something is "broken" or "crashes", ask which floor, which mod and what he sees before building anything, unless the answer is already in the log.
- **Use the bots, do not rebuild them.** `Gallery.java` has modes, set with `JAVA_TOOL_OPTIONS="-Donyx.gallery.only=<mode>"` and run through `scripts/gallery.sh <folder>`. `ui` (windows, 40 seconds), `branches` (Matt's branch floors), `allbosses` (every boss and special floor, kills them in phases, takes the exit, use `-Donyx.gallery.from=N -Donyx.gallery.count=30` to run slices of about 8 minutes), `fullrun` with `-Donyx.gallery.seed=N` (floor 1 to 26, Amulet, back to floor 1, 3 minutes a seed), `arena`, and `final` (opens Cthulhu for Onyx to play, keeps his audio on). Every tour mutes music and sound. Read the `[ALLB]` and `[FULL]` lines and `Exception` in the log, then read only the pictures that matter.
- **Run long things in the background.** Start with `run_in_background`, or a bash script that writes a `.done` file, and wait with one loop. Never sit in several 10 minute foreground waits. While a tour runs, only edit source, do not compile, the game reads the class files.
- **Fix as you go.** Fix each crash the moment its stack trace appears, then rerun only the slice that failed.
- **Known noise, do not chase.** "No cap character found in font" repeating every frame means the game window was hidden or minimized, restart that slice. "Ogg bitstream does not contain Vorbis" is the three Opus tracks in `music/devoted/`, not a crash. The auto desktop update hook rewrites the 700 MB jar after every commit, so tours started in the next 2 minutes fail with `NoClassDefFoundError`, wait for the jar size to stop changing.
- **Shell facts.** Python is `python -I`, there is no `python3`, `jq`, `strings` or `ffmpeg`. Bash heredocs and `sed a\` mangle `$` and backslashes, use the Edit tool or `python -I -` with a quoted heredoc. The Edit tool needs a Read of the file first in the same session. Sandbox special floors must be built at a normal depth, the tours use 12.
- **Finish with a sound.** A user level Stop hook in `~/.claude/settings.json` plays `tada.wav` whenever a response ends. Do not remove it. If it ever stops, the hook needs `/hooks` opened once to reload.
- **Report bug counts plainly.** Count real bugs fixed, separate confirmed player facing crashes from test only ones, and say what was not reproduced.
