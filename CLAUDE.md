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

## What the game is

A fork of Shattered Pixel Dungeon v4.0.2 called Onyx's Pixel Dungeon. Tagline "Every floor a new world."

- **World shuffle.** Most floors are built in a random world instead of their usual one. There are 24 worlds, Shattered's five plus 19 ported from four other GPLv3 mods. See `core/.../levels/WorldShuffle.java`.
- **Depth sets danger.** The world sets the look (tileset, water, decoration painter, ambient effects, music). Monsters, loot and traps always follow the actual depth. Ported worlds always use their floor's usual trap list.
- **Fixed floors.** Floor 1, boss floors (5, 10, 15, 20, 25), floor 26, and quest floors 4, 9, 14, 19 never shuffle, so the Ghost, Wandmaker, Blacksmith and Imp quests always appear.
- **Seeded.** Rolls come from the dungeon seed. Appending to `WorldShuffle.WORLDS` is safe, reordering changes which world a seed rolls.
- **Arrival message.** Entering a ported world logs "You enter X, a world from Y" (`WorldShuffle.announce`, called from `GameScene`).
- **Branding.** Title screen draws "ONYX'S" over the banner art (`TitleScene.addTitleName`, banner edited in `interfaces/banners.png`), with the tagline below. App name "Onyx's Pixel Dungeon", package id `com.onyxrothman.onyxpixeldungeon`, saves in `%APPDATA%\.onyxrothman`, separate from Shattered. Java packages are still `com.shatteredpixel...`, which is fine.
- **Desktop only.** Android and iOS modules are off in `settings.gradle`. Update and news services point at debug stubs so the game never offers Shattered's updates.

### Ported worlds

All in `core/.../levels/worlds/`, art in `assets/environment/worlds/`, music in `assets/music/worlds/`. Painters ported into `levels/painters/`.

| Mod | Worlds | Notes |
| --- | --- | --- |
| Combusted PD (Inferno214221) | Ice Caves, Necropolis, Spider Nest | Direct subclasses of Caves/Prison/City levels, as in the mod |
| Devoted PD (Arackulele) | Gardens, Coldhouse, Forge, Citadel, Ashen Void | Own painters. Four tracks from Cevin2006's soundtrack (credited). Forge's track was missing from the repo |
| Magic Ling PD (LingASDJ) | Ancient Mystery City, Dragon Cave, Pale Palace, Lost Tomb, Hollow | Hollow's door mimics left out. Music not used, no clear credits |
| Transformation PD (dongyusangyu) | Astral Library, Frost Archive, Gothic Castle, Sky Alchemy Greenhouse, Tower Core Gearworks, Chinese Hall | TowerPainter. Music not used, no clear credits |

Most world classes extend `WorldLevel`, which handles painter setup, traps, music and ambient visuals. Each world is its own class so saved games reload correctly. World name strings are in `messages/levels/levels.properties` under `levels.worlds.*`.

Not ported yet. Monsters from any mod (every world uses Shattered's monsters for its depth). Pixel Spacebase (its art is for the pre-2017 tile layout at 4x scale and would need redrawing). Avoid porting mods built around other companies' franchises (Touhou, Arknights, Blue Archive, Girls' Frontline, JoJo).

### Fixes made to Shattered code

- `HallwayRoom.merge` null-guard, since Caves and Halls painters merge rooms that share no door.
- `CityPainter` and the ported Citadel and Jungle painters clamp `Random.Int(21 - depth)` to at least 2. Below floor 20 it decorated every wall.

## Building and testing

Normal path on a PC with internet: `./gradlew desktop:debug` to run, `./gradlew desktop:release` for a jar. This Gradle setup was edited (Android plugin and the `org.beryx.runtime` plugin removed, `application` plugin used) but never run in the original build environment, so fix it if it fails.

Gradle-free path used for releases (`scripts/`):

- `scripts/compile.sh <ShatteredPD-v4.0.2-Java.jar> <outdir>` compiles with javac, taking libraries from the official Shattered jar.
- `scripts/package-windows.sh <Java.jar> <Windows.zip> <outdir>` makes the full Windows zip, the small installer zip, the cross-platform jar, `OnyxPixelDungeon-game.jar` and `update.json`.
- `scripts/test/WorldShuffleTest.java` builds every floor of N seeded dungeons headlessly, checks all 24 worlds appear and have strings, and saves and reloads each ported world. Run it after any world change.

## Releases and updates

- `.github/workflows/release.yml` builds, runs the test on 1000 seeds, packages and publishes a GitHub release. Bump `appVersionName` in `build.gradle`, then on GitHub go to Actions, Build and release, Run workflow. It tags the release from the version.
- Windows auto-update. `scripts/windows-installer/Play.ps1` (run by Play.bat and the installer's desktop shortcut) checks the latest release's `update.json`, downloads `OnyxPixelDungeon-game.jar` if the version differs, verifies its SHA-256 and swaps it in. If the release needs a different Shattered base, it tells the player to rerun the installer instead. Keep the asset names stable.
- The game jar is loaded ahead of the official Shattered jars on the launcher classpath, so it only needs the game's classes and changed assets.

## Next planned work (Onyx's sandbox plan)

He wants to playtest everything, so build these next.

1. **Sandbox mode.** A separate start option, kept apart from normal runs (badges and rankings off). Everything unlocked. A god mode panel to skip to any floor, force a specific world or mod, toggle mods on and off in the shuffle, edit stats, and give any item.
2. **Mod intro.** Before each floor, the source mod's name and logo fade in. Most of these mods have no proper logo, so use their title text or app icon.
3. **Compendium.** A quick in-game catalog, by mod, of what each one contributes (worlds, tilesets, and later monsters and items). Doesn't need to be perfect.
4. **Later.** Port monsters, starting with whichever world he likes most.

Also, his promo banner art says "50+ MODS". The game currently has content from four mods, so suggest an accurate label like "24 WORLDS" if it comes up.
