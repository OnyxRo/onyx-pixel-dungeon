# Onyx's Pixel Dungeon

**Every floor a new world.**

Onyx's Pixel Dungeon is a fork of [Shattered Pixel Dungeon](https://github.com/00-Evan/shattered-pixel-dungeon) (v4.0.2) where most floors are built in a randomly chosen world instead of the one their depth normally uses. Floor 2 might be the Demon Halls, floor 7 the Caves, floor 12 the old Sewers.

## How it works

Shattered has five worlds (Sewers, Prison, Caves, Dwarven City and Demon Halls), normally five floors each. In this fork, each shuffled floor rolls a world other than its usual one.

- **The rolled world sets the look of the floor.** That means its tileset, layout style, water and grass, terrain decoration, ambient effects and music.
- **The actual depth sets the danger.** Monsters, loot and traps follow how deep you are, so a Demon Halls floor at depth 2 plays like depth 2. A deeper world rolled on a shallow floor uses the shallow floor's traps.
- **Some floors stay in their home world.** Floor 1, every boss floor (5, 10, 15, 20, 25), the final floor, and floors 4, 9, 14 and 19 never shuffle. The last four are where the Ghost, Wandmaker, Blacksmith and Imp quests are guaranteed to appear, so every quest still happens.
- **Rolls come from the dungeon seed**, so the same seed always gives the same worlds.

All of this lives in [`WorldShuffle.java`](core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/levels/WorldShuffle.java). The changes to Shattered itself are small.

| File | Change |
| --- | --- |
| `levels/WorldShuffle.java` | New. Picks each floor's world and caps trap danger. |
| `Dungeon.java` | Uses `WorldShuffle` when creating shuffled floors. |
| `levels/{Sewer,Prison,Caves,City,Halls}Level.java` | Ask `WorldShuffle` for their trap list. |
| `levels/rooms/standard/HallwayRoom.java` | Fixes a crash when City rooms appear in Caves or Halls layouts. |
| `scenes/TitleScene.java`, `scenes/WelcomeScene.java`, `interfaces/banners.png` | New title and tagline. |

## Playing on Windows

Download `OnyxPixelDungeon-<version>-Windows.zip` from [Releases](../../releases), unzip it anywhere, and double-click **Onyx Pixel Dungeon.exe**. Java is bundled, so nothing else needs to be installed. If SmartScreen warns about an unrecognized app, click **More info** and then **Run anyway**.

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

To check that floor generation never crashes, compile with `scripts/compile.sh` and run `scripts/test/WorldShuffleTest.java`, which builds every floor of a few thousand seeded dungeons without opening a window.

## Next steps

`WorldShuffle` is built so other Pixel Dungeon forks can join the rotation. Porting a fork's world means bringing over its level class, painter, tileset and (optionally) monsters, then adding it to `WorldShuffle.create()`. Forks are GPLv3 like this project, but check each one's credits for art with separate terms before porting it.

## Credits and license

Onyx's Pixel Dungeon is by Onyx Rothman.

Built on [Shattered Pixel Dungeon](https://shatteredpixel.com/shatteredpd/) by Evan Debenham, which is based on [Pixel Dungeon](https://github.com/00-Evan/pixel-dungeon-gradle) by Oleg Dolya (Watabou). If you enjoy this, consider [supporting Shattered's developer on Patreon](https://www.patreon.com/ShatteredPixel). Please send bug reports about this fork here, not to Shattered.

Licensed under the GNU General Public License v3. See [LICENSE.txt](LICENSE.txt).
