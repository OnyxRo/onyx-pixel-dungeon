# Onyx's Pixel Dungeon

**Every floor a new world.**

Onyx's Pixel Dungeon is a fork of [Shattered Pixel Dungeon](https://github.com/00-Evan/shattered-pixel-dungeon) (v4.0.2) where most floors are built in a randomly chosen world instead of the one their depth normally uses. There are 54 worlds, Shattered's own five plus 49 ported from 14 other Pixel Dungeon mods. Floor 2 might be the Gardens, floor 7 the Necropolis, floor 12 the Lost Tomb. Devoted's and Matt Edition's monsters are ported too, and a sandbox mode lets you visit anything. It is a desktop game, built for Windows.

## How it works

Shattered has five worlds (Sewers, Prison, Caves, Dwarven City and Demon Halls), normally five floors each. In this fork, each shuffled floor rolls a world that its mod made for that stage of the dungeon. A sewer-stage floor gets a sewer-stage world like Devoted's Gardens, a prison-stage floor gets one like Combusted's Necropolis, and so on. Transformation's tower styles weren't tied to a depth in their mod, so they can appear at any stage, sharing one slot so they don't crowd out the others. Arriving in a ported world logs its name and the mod it comes from.

- **The rolled world sets the look of the floor.** That means its tileset, layout style, water and grass, terrain decoration, ambient effects and music.
- **The actual depth sets the danger.** Monsters, loot and traps follow how deep you are. Ported worlds use the traps the floor's usual world would have.
- **Some floors stay in their home world.** Floor 1, every boss floor (5, 10, 15, 20, 25), the final floor, and floors 4, 9, 14 and 19 never shuffle. The last four are where the Ghost, Wandmaker, Blacksmith and Imp quests are guaranteed to appear, so every quest still happens.
- **Rolls come from the dungeon seed**, so the same seed always gives the same worlds.

## Worlds from other mods

| World | From | Stage | Notes |
| --- | --- | --- | --- |
| Ice Caves, Necropolis, Spider Nest | [Combusted Pixel Dungeon](https://github.com/Inferno214221/Combusted-Pixel-Dungeon) | Caves, Prison, City | Reskins of Shattered's Caves, Prison and City, as in the original mod. |
| The Gardens, the Coldhouse, the Forge, the Dwarven Citadel, the Ashen Void | [Devoted Pixel Dungeon](https://github.com/Arackulele/devoted-pixel-dungeon) | Sewers, Prison, Caves, City, Halls | Own decoration painters. Four have their own music from the Devoted soundtrack by Cevin2006. |
| Ancient Mystery City, Dragon Cave, Pale Palace, Lost Tomb, the Hollow | [Magic Ling Pixel Dungeon](https://github.com/LingASDJ/Magic_Ling_Pixel_Dungeon) | City, Sewers, City, Caves, Halls | Own decoration painters where the mod had them. The Hollow's door mimics are left out. |
| Astral Library, Frost Archive, Gothic Castle, Sky Alchemy Greenhouse, Tower Core Gearworks, Chinese Hall | [Transformation Pixel Dungeon](https://github.com/dongyusangyu/transformation-pixel-dungeon-master) | Any | The mod's tower floors, with its tower decoration painter. |

A second wave of 30 worlds was made from the tile sheets of ten more mods. Each is a small level class that uses the Shattered painter, ambience and music for its stage, and the mod's own water sheet where it had one.

| From | Worlds |
| --- | --- |
| Shattered PD Matt Edition | Guild Hall, Snowfield |
| Pixel Dungeon Reforged | Kobold Caves |
| Shittened PD | Christmas Caves |
| Summoning PD Reincarnated | Abyss |
| Re-ARranged PD | Labs, Ancient Temple |
| Experienced PD | Arena |
| Cursed PD (Legacy) | Beach, Surface, Jungle Temple, Dolya Haven |
| Ryuse's PD | Polluted Sewers |
| Abyssalnite PD | Abyssal Sewers, Prison, Caves, City, Halls |
| Rat King PD 2 | Rat King's Sewers |
| Transformation PD | UES, Lush Surface |
| Magic Ling PD | Golden City, Desert Wilds, Cold Lands, Dead Lands, Ghost Manor, Frozen Vault, Overgrown Garden, Tengu's Hideout |
| Devoted PD | True Void |

Each ported world brings its tileset, water, decoration and, where credited, music. Worlds without credited music of their own use the closest Shattered soundtrack. While you are in a ported world, the source mod's icon shows at the top left, and clicking it opens a short summary of the mod. The mods are GPLv3 or similar, and their authors are credited in each ported file. Licenses and art credits for the newer mods still need a proper check before any public release.

## Monsters and items

- **Devoted PD** is ported in full for monsters. All 54 monsters, its five boss levels, two weapons and eleven other items are in. Its boss can replace the usual one on a boss floor.
- **Shattered PD Matt Edition** has 13 monsters plus the Dark King, nine unique blacksmith weapons and a gun. Guild Hall and Snowfield floors spawn them.
- **Combusted PD** adds four throwable melee weapons.
- Weapons and items that are not in the loot tables are in the journal catalog, so the sandbox can give them to you.
- Every other world uses Shattered's monsters for its depth. More mods' monsters are on the way.

## Sandbox and game modes

- **Sandbox** is a separate start option on the title screen. Badges and rankings are off and everything is unlocked. A panel lets you skip to any floor, force a world, turn mods on and off in the shuffle, level up, heal, reveal the floor, spawn any ported monster and give any item. God mode and full sight are on by default. Ctrl+R rebuilds the floor and Ctrl+N jumps to a random floor.
- **Single mod mode** is a Game Mode button on the hero select screen. It locks every shuffled floor to one chosen mod's worlds, and to its monsters where that mod has ported ones.
- **Song list and compendium** show every song by mod and a catalog of what each mod adds.

The ported worlds live in [`levels/worlds`](core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/levels/worlds) and their art in `assets/environment/worlds`. Adding another means writing a small level class, listing it in `WorldShuffle.WORLDS`, and giving it the stage its mod made it for in `WorldShuffle.REGIONS`.

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

- **Monsters and weapons.** Porting the other mods' enemies and weapons is the next layer, and the most work, since many depend on mechanics unique to their mod.
- **Pixel Spacebase.** Its art is drawn for Pixel Dungeon's pre-2017 tile layout at four times the resolution, so it would need redrawing to fit, not just porting.
- **More worlds.** Forks built on Shattered 1.0 or later share this game's tile format and are the easiest to add. Check each one's credits for art with separate terms first.

## Credits and license

Onyx's Pixel Dungeon is by Onyx Rothman.

Content ported from Combusted Pixel Dungeon (Inferno214221), Devoted Pixel Dungeon (Arackulele and contributors, soundtrack by Cevin2006), Magic Ling Pixel Dungeon (LingASDJ and contributors), Transformation Pixel Dungeon (dongyusangyu), Shattered PD Matt Edition (mr-matthias1829), Pixel Dungeon Reforged (nathanlink169), Shittened Pixel Dungeon (Haukii), Abyssalnite Pixel Dungeon (MywaifuisTataraKogasa), Cursed Pixel Dungeon Legacy (Smujb), Summoning PD Reincarnated, Re-ARranged PD, Experienced PD, Ryuse's PD and Rat King PD 2.

Built on [Shattered Pixel Dungeon](https://shatteredpixel.com/shatteredpd/) by Evan Debenham, which is based on [Pixel Dungeon](https://github.com/00-Evan/pixel-dungeon-gradle) by Oleg Dolya (Watabou). If you enjoy this, consider [supporting Shattered's developer on Patreon](https://www.patreon.com/ShatteredPixel). Please send bug reports about this fork here, not to Shattered.

Licensed under the GNU General Public License v3. See [LICENSE.txt](LICENSE.txt).
