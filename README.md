# Onyx's Pixel Dungeon

**Every floor a new world.**

Onyx's Pixel Dungeon is a fork of [Shattered Pixel Dungeon](https://github.com/00-Evan/shattered-pixel-dungeon) (v4.0.2) where most floors are built in a randomly chosen world instead of the one their depth normally uses. There are 54 worlds, Shattered's own five plus 49 ported from 14 other Pixel Dungeon mods. Floor 2 might be the Gardens, floor 7 the Necropolis, floor 12 the Lost Tomb. Some mods also bring their monsters, bosses and weapons, and there is a sandbox mode for trying everything.

## How it works

Shattered has five worlds (Sewers, Prison, Caves, Dwarven City and Demon Halls), normally five floors each. In this fork, each shuffled floor rolls a world that its mod made for that stage of the dungeon. A sewer-stage floor gets a sewer-stage world like Devoted's Gardens, a prison-stage floor gets one like Combusted's Necropolis, and so on. Transformation's tower styles weren't tied to a depth in their mod, so they can appear at any stage, sharing one slot so they don't crowd out the others. Arriving in a ported world logs its name and the mod it comes from, and the mod's icon and short name show at the top left of the game screen. Clicking the icon opens a short summary of what the mod changed.

- **The rolled world sets the look of the floor.** That means its tileset, layout style, water and grass, terrain decoration, ambient effects and music.
- **The actual depth sets the danger.** Monsters, loot and traps follow how deep you are. Ported worlds use the traps the floor's usual world would have.
- **Some floors stay in their home world.** Floor 1, every boss floor (5, 10, 15, 20, 25), the final floor, and floors 4, 9, 14 and 19 never shuffle. The last four are where the Ghost, Wandmaker, Blacksmith and Imp quests are guaranteed to appear, so every quest still happens. A boss floor can hold a ported mod's boss when one of that stage's floors rolled that mod's world.
- **Rolls come from the dungeon seed**, so the same seed always gives the same worlds.

## Worlds from other mods

| World | From | Stage | Notes |
| --- | --- | --- | --- |
| Ice Caves, Necropolis, Spider Nest | [Combusted Pixel Dungeon](https://github.com/Inferno214221/Combusted-Pixel-Dungeon) | Caves, Prison, City | Reskins of Shattered's Caves, Prison and City, as in the original mod. |
| The Gardens, the Coldhouse, the Forge, the Dwarven Citadel, the Ashen Void | [Devoted Pixel Dungeon](https://github.com/Arackulele/devoted-pixel-dungeon) | Sewers, Prison, Caves, City, Halls | Own decoration painters. Four have their own music from the Devoted soundtrack by Cevin2006. |
| Ancient Mystery City, Dragon Cave, Pale Palace, Lost Tomb, the Hollow | [Magic Ling Pixel Dungeon](https://github.com/LingASDJ/Magic_Ling_Pixel_Dungeon) | City, Sewers, City, Caves, Halls | Own decoration painters where the mod had them. The Hollow's door mimics are left out. |
| Astral Library, Frost Archive, Gothic Castle, Sky Alchemy Greenhouse, Tower Core Gearworks, Chinese Hall | [Transformation Pixel Dungeon](https://github.com/dongyusangyu/transformation-pixel-dungeon-master) | Any | The mod's tower floors, with its tower decoration painter. |

**Second wave, 30 more worlds.** These are smaller ports made from tile sheets in other mods. Each uses the Shattered painter, music and effects for its stage, with the mod's own tiles and water.

| Mod | Worlds |
| --- | --- |
| Shattered PD Matt Edition | Guild Hall, Snowfield |
| Pixel Dungeon Reforged | Kobold Caves |
| Shittened Pixel Dungeon | Christmas Caves |
| Summoning Pixel Dungeon Reincarnated | Abyss |
| Re-ARranged Pixel Dungeon | Labs, Ancient Temple |
| Experienced Pixel Dungeon | Arena |
| Cursed Pixel Dungeon (Legacy) | Beach, Surface, Jungle Temple, Dolya Haven |
| Ryuse's Pixel Dungeon | Polluted Sewers |
| Abyssalnite Pixel Dungeon | Abyssal Sewers, Prison, Caves, City, Halls |
| Rat King Pixel Dungeon 2 | Rat King's Sewers |
| Transformation Pixel Dungeon | UES, Lush Surface |
| Magic Ling Pixel Dungeon | Golden City, Desert Wilds, Cold Lands, Dead Lands, Ghost Manor, Frozen Vault, Overgrown Garden, Tengu's Hideout |
| Devoted Pixel Dungeon | True Void |

The names and stages of these 30 were chosen from each tile sheet's look and file name, so some may be corrected after more playtesting.

Each ported world brings its tileset, water, decoration and, where credited, music. Worlds without credited music of their own use the closest Shattered soundtrack.

## Monsters, bosses and weapons

Most worlds still use Shattered's monsters for their depth. Two mods are ported further.

- **Devoted Pixel Dungeon** is ported in full for monsters. All 54 of its monsters and its five boss levels are in, along with its two weapons and eleven other items. Floors built in a Devoted world spawn its monsters by depth.
- **Shattered PD Matt Edition** has 13 monsters, the Dark King (sandbox only), its nine blacksmith weapons and its gun. Its Guild Hall and Snowfield worlds spawn its monsters.
- **Combusted Pixel Dungeon** adds no monsters. Its four throwable weapons are in the catalog.

Items and weapons from these mods appear in the sandbox item list. Porting monsters from the other mods is ongoing.

The ported worlds live in [`levels/worlds`](core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/levels/worlds) and their art in `assets/environment/worlds`. Adding another means writing a small level class, listing it in `WorldShuffle.WORLDS`, and giving it the stage its mod made it for in `WorldShuffle.REGIONS`.

## Sandbox mode

The title screen has a Sandbox button that starts a run kept apart from normal play. Badges and rankings are off, and every hero and option is unlocked. In a sandbox run you can

- skip to any floor and force a specific world for new floors
- turn whole mods on or off in the shuffle
- level up, add strength, heal, reveal the floor, and turn god mode and full sight on or off
- give yourself any item from the catalog at a chosen upgrade level
- spawn any ported monster next to you
- browse and play every song, grouped by mod
- press Ctrl+R to rebuild the current floor with a new layout, and Ctrl+N to jump to a random floor in a random world

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

For development on a Windows PC, `scripts\make-exe.ps1` builds the game, wraps it with its own Java into `Documents\OnyxPixelDungeon-app` and adds a desktop shortcut. The downloads below are for everyone else.

Download `OnyxPixelDungeon-<version>-Windows.zip` from [Releases](../../releases), unzip it anywhere, and double-click **Play.bat**. Java is bundled, so nothing else needs to be installed. If SmartScreen warns about an unrecognized app, click **More info** and then **Run anyway**.

There is also a small installer, `OnyxPixelDungeon-<version>-Windows-Installer.zip` (about 16 MB). Unzip it and double-click **Install.bat**. It downloads the official Shattered Pixel Dungeon Windows release from GitHub for its launcher and Java runtime, checks the download's checksum, sets the game up in a folder next to the installer, and adds a desktop shortcut.

**Staying up to date.** Both Windows downloads include **Play.bat**, and the installer's desktop shortcut runs it too. Each launch checks GitHub for a newer release and, if there is one, downloads just the game file (about 17 MB) and starts the new version. If you're offline it starts the version you have. You only need a fresh download when a release moves to a newer Shattered base, and Play.bat will tell you when that happens.

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

- **More monsters and weapons.** Magic Ling and Transformation have hundreds of monsters and weapons still to port, and the ten newer mods have more. Many depend on mechanics unique to their mod.
- **Single mod mode.** A start screen option that locks every floor to one mod's worlds, monsters and items.
- **Pixel Spacebase.** Its art is drawn for Pixel Dungeon's pre-2017 tile layout at four times the resolution, so it would need redrawing to fit, not just porting.
- **More worlds.** Forks built on Shattered 1.0 or later share this game's tile format and are the easiest to add. Check each one's credits for art with separate terms first.

## Credits and license

Onyx's Pixel Dungeon is by Onyx Rothman.

Content ported from Combusted Pixel Dungeon (Inferno214221), Devoted Pixel Dungeon (Arackulele and contributors, soundtrack by Cevin2006), Magic Ling Pixel Dungeon (LingASDJ and contributors) and Transformation Pixel Dungeon (dongyusangyu), all under the GPLv3. The second wave and the monsters and weapons come from Shattered PD Matt Edition, Pixel Dungeon Reforged, Shittened Pixel Dungeon, Summoning Pixel Dungeon Reincarnated, Re-ARranged Pixel Dungeon, Experienced Pixel Dungeon, Cursed Pixel Dungeon (Legacy), Ryuse's Pixel Dungeon, Abyssalnite Pixel Dungeon and Rat King Pixel Dungeon 2. Their licenses and art credits have not been checked yet and must be before any public release.

Built on [Shattered Pixel Dungeon](https://shatteredpixel.com/shatteredpd/) by Evan Debenham, which is based on [Pixel Dungeon](https://github.com/00-Evan/pixel-dungeon-gradle) by Oleg Dolya (Watabou). If you enjoy this, consider [supporting Shattered's developer on Patreon](https://www.patreon.com/ShatteredPixel). Please send bug reports about this fork here, not to Shattered.

Licensed under the GNU General Public License v3. See [LICENSE.txt](LICENSE.txt).
