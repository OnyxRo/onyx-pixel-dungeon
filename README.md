# Onyx's Pixel Dungeon

**Every floor a new world.**

Onyx's Pixel Dungeon is a fork of [Shattered Pixel Dungeon](https://github.com/00-Evan/shattered-pixel-dungeon) (v4.0.2) where most floors are built in a randomly chosen world instead of the one their depth normally uses. There are 24 worlds, Shattered's own five plus 19 ported from four other Pixel Dungeon mods. Floor 2 might be the Demon Halls, floor 7 a Dragon Cave, floor 12 an Astral Library.

## How it works

Shattered has five worlds (Sewers, Prison, Caves, Dwarven City and Demon Halls), normally five floors each. In this fork, each shuffled floor rolls any of the 24 worlds except its usual one. Arriving in a ported world logs its name and the mod it comes from.

- **The rolled world sets the look of the floor.** That means its tileset, layout style, water and grass, terrain decoration, ambient effects and music.
- **The actual depth sets the danger.** Monsters, loot and traps follow how deep you are, so a Demon Halls floor at depth 2 plays like depth 2. A deeper world rolled on a shallow floor, or any ported world, uses the traps the floor's usual world would have.
- **Some floors stay in their home world.** Floor 1, every boss floor (5, 10, 15, 20, 25), the final floor, and floors 4, 9, 14 and 19 never shuffle. The last four are where the Ghost, Wandmaker, Blacksmith and Imp quests are guaranteed to appear, so every quest still happens.
- **Rolls come from the dungeon seed**, so the same seed always gives the same worlds.

## Worlds from other mods

| World | From | Notes |
| --- | --- | --- |
| Ice Caves, Necropolis, Spider Nest | [Combusted Pixel Dungeon](https://github.com/Inferno214221/Combusted-Pixel-Dungeon) | Reskins of Shattered's Caves, Prison and City, as in the original mod. |
| The Gardens, the Coldhouse, the Forge, the Dwarven Citadel, the Ashen Void | [Devoted Pixel Dungeon](https://github.com/Arackulele/devoted-pixel-dungeon) | Own decoration painters. Four have their own music from the Devoted soundtrack by Cevin2006. |
| Ancient Mystery City, Dragon Cave, Pale Palace, Lost Tomb, the Hollow | [Magic Ling Pixel Dungeon](https://github.com/LingASDJ/Magic_Ling_Pixel_Dungeon) | Own decoration painters where the mod had them. The Hollow's door mimics are left out. |
| Astral Library, Frost Archive, Gothic Castle, Sky Alchemy Greenhouse, Tower Core Gearworks, Chinese Hall | [Transformation Pixel Dungeon](https://github.com/dongyusangyu/transformation-pixel-dungeon-master) | The mod's tower floors, with its tower decoration painter. |

Each ported world brings its tileset, water, decoration and, where credited, music. Monsters are not ported yet, so every world uses Shattered's monsters for its depth. Worlds without credited music of their own use the closest Shattered soundtrack. All four mods are GPLv3, and their authors are credited in each ported file.

The ported worlds live in [`levels/worlds`](core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/levels/worlds) and their art in `assets/environment/worlds`. Adding another means writing a small level class and listing it in `WorldShuffle.WORLDS`.

## Code changes

All of this lives in [`WorldShuffle.java`](core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/levels/WorldShuffle.java). The changes to Shattered itself are small.

| File | Change |
| --- | --- |
| `levels/WorldShuffle.java` | New. Picks each floor's world and caps trap danger. |
| `Dungeon.java` | Uses `WorldShuffle` when creating shuffled floors. |
| `levels/{Sewer,Prison,Caves,City,Halls}Level.java` | Ask `WorldShuffle` for their trap list. |
| `levels/rooms/standard/HallwayRoom.java` | Fixes a crash when City rooms appear in Caves or Halls layouts. |
| `levels/painters/CityPainter.java` | Caps a depth formula that decorated every wall when the City appeared below floor 20. |
| `levels/worlds/*`, `levels/painters/{Garden,Coldhouse,Forge,Citadel,Void,Jungle,Hollow,Tower}Painter.java` | New. The ported worlds. |
| `scenes/GameScene.java` | Logs the world name on arrival. |
| `scenes/TitleScene.java`, `scenes/WelcomeScene.java`, `interfaces/banners.png` | New title and tagline. |

## Playing on Windows

Download `OnyxPixelDungeon-<version>-Windows.zip` from [Releases](../../releases), unzip it anywhere, and double-click **Onyx Pixel Dungeon.exe**. Java is bundled, so nothing else needs to be installed. If SmartScreen warns about an unrecognized app, click **More info** and then **Run anyway**.

There is also a small installer, `OnyxPixelDungeon-<version>-Windows-Installer.zip` (about 16 MB). Unzip it and double-click **Install.bat**. It downloads the official Shattered Pixel Dungeon Windows release from GitHub for its launcher and Java runtime, checks the download's checksum, sets the game up in a folder next to the installer, and adds a desktop shortcut.

Saves live in `%APPDATA%\.onyxrothman`, separate from any Shattered Pixel Dungeon install.

On Mac or Linux with Java 11 or newer installed, run the `.jar` from Releases with `java -jar OnyxPixelDungeon-<version>.jar`. On a Mac, add `-XstartOnFirstThread` before `-jar`.

## Building

With normal internet access, Gradle works as in Shattered (see [docs/getting-started-desktop.md](docs/getting-started-desktop.md)). This fork builds desktop only, so the Android and iOS modules are switched off in `settings.gradle`.

```
./gradlew desktop:debug      # run the game
./gradlew desktop:release    # build a jar in desktop/build/libs
```

There is also a Gradle-free path that borrows libGDX and the other libraries from an official Shattered release, which is how the Windows zip is made. Download `ShatteredPD-v4.0.2-Java.jar` and `ShatteredPD-v4.0.2-Windows.zip` from [Shattered's releases](https://github.com/00-Evan/shattered-pixel-dungeon/releases/tag/v4.0.2), then run

```
scripts/package-windows.sh ShatteredPD-v4.0.2-Java.jar ShatteredPD-v4.0.2-Windows.zip dist
```

This makes both Windows zips and a cross-platform jar.

To check the worlds, compile with `scripts/compile.sh` and run `scripts/test/WorldShuffleTest.java`. It builds every floor of thousands of seeded dungeons without opening a window, checks that every world appears and has its strings, and saves and reloads each ported world.

## Next steps

- **Monsters.** Porting each mod's enemies is the next layer, and the most work, since many depend on mechanics unique to their mod.
- **Pixel Spacebase.** Its art is drawn for Pixel Dungeon's pre-2017 tile layout at four times the resolution, so it would need redrawing to fit, not just porting.
- **More worlds.** Forks built on Shattered 1.0 or later share this game's tile format and are the easiest to add. Check each one's credits for art with separate terms first.

## Credits and license

Onyx's Pixel Dungeon is by Onyx Rothman.

Worlds ported from Combusted Pixel Dungeon (Inferno214221), Devoted Pixel Dungeon (Arackulele and contributors, soundtrack by Cevin2006), Magic Ling Pixel Dungeon (LingASDJ and contributors) and Transformation Pixel Dungeon (dongyusangyu), all under the GPLv3.

Built on [Shattered Pixel Dungeon](https://shatteredpixel.com/shatteredpd/) by Evan Debenham, which is based on [Pixel Dungeon](https://github.com/00-Evan/pixel-dungeon-gradle) by Oleg Dolya (Watabou). If you enjoy this, consider [supporting Shattered's developer on Patreon](https://www.patreon.com/ShatteredPixel). Please send bug reports about this fork here, not to Shattered.

Licensed under the GNU General Public License v3. See [LICENSE.txt](LICENSE.txt).
