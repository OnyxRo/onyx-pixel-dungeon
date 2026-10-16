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
- To check a port, use `scripts/check.sh --new` (headless, seconds) and then `scripts/arena.sh` once (one picture of everything new, 16 seconds). Do not use the full screenshot tour for monsters, it spawns each ported monster one at a time and takes ten minutes. The tour (`scripts/gallery.sh`) is for worlds and windows only, with the separate save folder.
- For any task that starts with "Port", invoke the `port-mod` skill (`.claude/skills/port-mod/SKILL.md`) first. It has the tools, the order of work and the mistakes to avoid. Rewrite it when you learn something that would have saved time.
- Write files that contain backslashes with the Write or Edit tools, not shell heredocs. Run Python as `python -I`.
- Always open the game when you finish a task, so Onyx can see the result. Do it once at the end, never once per monster or per check (checks stay headless, see above).
- Keep command output short. Send long output to a file and grep it.
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
- **Seeded.** Rolls come from the dungeon seed. Appending to `WorldShuffle.WORLDS` is safe, reordering changes which world a seed rolls.
- **Arrival message.** Entering a ported world logs "You enter X, a world from Y" (`WorldShuffle.announce`, called from `GameScene`).
- **Mod logo.** While in a ported world, the source mod's app icon and short name show at the top left of the game screen (`GameScene.create`, `WorldShuffle.modLogo`). Clicking the icon opens `WndModInfo`, a short summary of what the mod changed taken from its GitHub page (text in `windows.properties` under `windows.wndmodinfo.*`, keyed by the mod name with only letters and digits kept). Icons are in `assets/interfaces/mods/`, taken from each mod's repo at 32x32. A new mod needs an icon there and an entry in `WorldShuffle.LOGOS`.
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

**Second wave, 30 more worlds (54 in total, from 14 mods).** Generated in bulk from tile sheets in more Shattered family mods. Each is a small `WorldLevel` subclass that uses its region's Shattered painter, ambience and music, and the mod's water sheet where it had one.

| Mod | Worlds |
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

Missing tiles. Sheets from older Shattered bases lacked up to 60 tiles that newer Shattered added, which would draw as nothing. Every world sheet now has those cells filled from Shattered's own sheet for the same stage, so a few decorations in older-base worlds are in Shattered's style. Do the same for any new sheet.

Things to know about the second wave. The world names and regions were chosen from each tile sheet's look and file name, not checked against each mod's `Dungeon.newLevel`, so correct them as Onyx playtests. All were looked at through the screenshot tour and render as proper floors. Left out as unfinished or duplicate sheets. Magic Ling's morpheus, zero, zero spring, bubble and halloween, Transformation's winter surface, Matt Edition's snowless ice. Cursed PD's current repo uses a taller 256x384 tile layout that does not fit. Licenses and art credits for these mods have not been checked. Onyx said on 2026-10-13 that this is a personal project and licensing is not a concern for now, so check them before any public release.

Most world classes extend `WorldLevel`, which handles painter setup, traps, music and ambient visuals. Each world is its own class so saved games reload correctly. World name strings are in `messages/levels/levels.properties` under `levels.worlds.*`.

Monsters ported so far. Devoted's 29 regular monsters (`actors/mobs/altregion/`, sprites in `assets/sprites/devoted/`, strings at the end of `actors.properties`). Floors built in a Devoted world spawn them by depth through `WorldShuffle.mobRotation` and `DevotedSpawner`, copied from Devoted's MobSpawner. A few Devoted-only things were swapped for Shattered equivalents (buff icons, missile effects, magma tiles became embers, the Royal Grub drops a normal lit bomb). Devoted is based on Shattered 3.3.0, so its code ports with few changes. Mod sources for porting can be cloned with `git -c core.longpaths=true`.

Devoted is now ported in full for monsters. All 54 of its monster classes are in, with its five boss levels (`GardenBossLevel`, `ColdhouseBossLevel`, `ForgeBossLevel`, `CitadelBossLevel`, `VoidBossLevel` in `levels/`), boss music, its two weapons and eleven other items (listed in the journal catalog, so the sandbox item list has them). Its boss floors are reached in sandbox by forcing a Devoted world and skipping to floor 5, 10, 15, 20 or 25 (`WorldShuffle.modBossLevel`). In every run, a boss floor uses Devoted's boss when any shuffled floor of that region rolled a Devoted world (`WorldShuffle.bossWorldFor`, Onyx said yes to this on 2026-10-13). Devoted's bosses do not drop Tengu's mask or the King's crown, so its floor 10 and 20 boss levels drop them on unseal (`WorldShuffle.dropProgressionItem`). Any future mod boss needs the same. Its quest monsters only come from the sandbox spawn list, since Devoted's quest rooms are not ported. Finished on 2026-10-15 (evening). The Void trap (`VoidTrapLevel`, a shadow realm where the hero drops to level 1 and wins levels back by popping orbs, it is a special floor in the sandbox because Devoted itself never links to it), the bounty hunter's item infusion (`Item.isInfused` and the `InfusionCD` buff, an infused item comes back to the pack after it is used up, `Hero.bountyhunterbuff` adds max HP), and ally summons (`ConjureAbility`, `WndConjurerSummoning`, the five conjured allies Wisp, Blue Imp, Wasp, Wretch and Homonculus, reached from the Summon allies button in the sandbox panel since the Zealot class that owns them is not here). The Zealot hero class came later the same night (see Two new hero classes below). A handful of Devoted-only icons and effects are swapped for Shattered ones. The Void trap and the summons were built and run headless only, not seen on screen yet. Everything else was checked with the screenshot tour.

Matt Edition's monsters are ported. Thirteen regular monsters and the Dark King, in the main `actors/mobs` package. Floors built in its Guild Hall spawn burglars and snatchers, and its Snowfield spawns its ice monsters (`MattSpawner`, through `WorldShuffle.mobRotation`). The rest only come from the sandbox spawn list. Left out. The friendly thief quest (needs Matt Edition's extra dungeon branch), its animation speed setting, its augment system for weapons and armor, and the Dark King cannot be spawned since Matt Edition never used him and he needs the Dwarf King's throne room. Its nine unique blacksmith weapons and its gun are in the journal catalog only (`items/weapon/melee/blacksmith/`, `Glock`), since Matt Edition hands them out through its own blacksmith window. `scripts/port-itemsprites.py` copies item pictures from a mod's sheet into rows 13 and 19 of ours. `Bestiary.isPorted` lists which monsters are ported, for the screenshot tour.

What the ten newer mods add beyond this game, counted by new class files on 2026-10-13 (monsters, weapons, all items, total). Matt Edition 16, 17, 32, 83. Summoning Reincarnated 33, 12, 113, 255. Reforged 11, 2, 17, 98. Re-ARranged 14, 97, 173, 325. Experienced 5, 9, 55, 78. Shittened 41, 6, 106, 225. Rat King 2 1, 2, 6, 19 (mostly hero changes). Ryuse's 8, 5, 28, 69. Abyssalnite 18, 4, 30, 80. Cursed Legacy 23, 17, 65, 163. Their sources clone fine with `git -c core.longpaths=true`.

Porting tools, kept in the session scratchpad and worth rebuilding if lost. A copier that compiles, copies each class the compiler cannot find from the mod when the name is unambiguous, adds missing `Assets` constants with their files under a per-mod subfolder, and repeats. A string copier that appends the English text for every new class from the mod's message files. Windows needs the long path prefix for files inside deep mod folders.

Two new hero classes. Devoted's Zealot (Conjurer and Devotee subclasses, Blood Vial, three armor abilities, 17 talents) and Reforged's Artificer (Constructor and Armorer subclasses, the Gun, three armor abilities, 26 talents, the Construct ally) are playable heroes, finished 2026-10-15 (night). Both are on the hero select screen, unlocked in the sandbox and otherwise by badge (the Zealot by creating a trinket, the Artificer by crafting anything in an alchemy pot). Talent icons for the Artificer sit in cells 219 to 245 of `talent_icons.png` and the Rat King's icons moved to 247 to 250, because Reforged and Devoted both used 192 to 217. Hero subclass icons are in `hero_icons.png` cells 12 to 15, 35 to 39, 67 and 111 to 112. Reforged's combat system is not here, so the Artificer's talents were written on Shattered's damage (Reflection hooks `Hero.damage`, Effective Shot and Tinkerer's Meal live in `Gun`, Quick Calibration in `Hero.defenseSkill`, Overclocked Systems in `RingOfEnergy`). The Zealot and Artificer were run through a full bot game and the headless checks (`DevotedLeftoversTest`), the tour options `-Donyx.gallery.class=ZEALOT` and `-Donyx.gallery.subclass=CONSTRUCTOR` play the bot as them. Neither class has been seen by Onyx on screen yet. Not ported, Reforged's Artificer extras that need its combat engine (the Heavy Blow and Lunge trackers) and the Zealot's Eldritch Energy and Tag Team talents, which the mod itself never made do anything.

Reforged's Half Ripper escort quest and its Gun are in (`HalfRipper`, `Gun`, see PORTING.md). The Half Ripper appears on floor 24 of any world, follows the hero up to floor 21 and pays out on floor 20. `HalfRipper.Quest.spawn` runs from `Dungeon.switchLevel` on every arrival, so it works in shuffled worlds and on boss floors.

Shittened PD is ported in full (2026-10-15 night). 45 monsters (the roster entries in `ModRoster` with depth ranges close to the mod's own spawner, weight 0 for the ones that only come from rooms, items or other monsters), about 100 foods, drinks, albums, armors, amulets and misc items, 3 potions and 1 scroll with their exotic versions, 3 plants, the music player artifact, the slot machine on floor 20, the Boombox in every Caves entrance room, the capybara nest room in the sewers, Mr Tokmanni on floor 11 when the shopkeeper is attacked, and the mod's systems. Systems added to the core. Weapon prefixes (`mechanics/Prefix`, `Weapon.prefix`, 60 percent of melee weapons find one, shown by a small icon in the item slot, every melee weapon's top damage goes through `MeleeWeapon.trueMax`), food nutrition facts (`Food.carbs` and the rest, shown in the item window), hunger changes (`Acceleration`, `Lavage`, `Satisfied` and `Overstuffed` buffs), the ketchup, caffeine, furor and goblin mode buffs, `Hero.defecate`, spray paint (a light tint of the hero sprite instead of Shittened's hair redraw), `Property.ANNOYING` and `HUMAN`, `Level.spawnMob(int, Mob)`, `Music.musicPlayerActive`, `Eye` fields made protected, three new `Dungeon.LimitedDrops`, `Generator.Category.FOOD_NEW` and `EXOTIC` (weight 0, used by loot bags and Evan), and `Statistics.shitsTaken`. The new potions, scroll and seeds are in the item decks with weight 0 so the labels and brewing work, the mod's monsters, loot bags and crates hand them out. Shittened armors name an ID for their hero picture, this game draws them from the tier. Sprites that need extra frames on a shared sheet (rat, bat, snake, swarm, goo, dm100, dm200, eye, piranha) use the mod's own sheet under `sprites/shittened/`. Left out. Shittened's unused Rat Room, its holiday album, the Seven badges (hooks are kept and do nothing), and GreenGoo, which no part of the mod spawns (it is in the sandbox spawn list).

Ryuse's PD is ported (2026-10-16, repo Ryuse/My-Pixel-Dungeon, key `ryuse`). It is a small mod. It adds the Summoner hero class (`HeroClass.SUMMONER`, subclasses Eidolon and Attractor, armor ability `PhaseShift`, unlocked by reading three scrolls of mirror image in one run, Dried Rose ghost with its own level and experience in `DriedRose`, better Ghost quest rewards, Ryuse's lover dialogue), a helmet slot (`Belongings.helmet`, 16 helmets and 2 inscriptions in `items/helmets` and `items/inscriptions`, effects hooked through the static helpers in `Helmet`, found as `Generator.Category.HELMET`), Training Whip and Summoner Whip, the Eye of Wisdom artifact and its level (a special floor), the Rat Nest room on floor 4, Polluted Rat and Polluted Crab. Left out on purpose. Its 5 ring, 5 artifact and 6 armor slots and the Equipment Page bag (they rewrite the core equipment code), its Damage combat class, the ghost wearing helmets and rings, HP, EXP and shield bar art, loading screens, three uncredited songs, the Sorceress splash and the demon Imp sprite. The mod gave the Summoner no talents, so hers are Shattered talents picked to suit her. Not played on screen by Onyx yet.

Not ported yet. Combusted is done. It adds no monsters, and its four throwable melee weapons (`items/weapon/thrown/`) are in the journal catalog only, since Combusted itself never put them in its loot tables. They drop at the enemy's feet instead of sticking. Monsters and weapons from Magic Ling (about 350 monster files, 74 weapons) and Transformation (59 monsters, 35 weapons). Shittened is done. Every non-Devoted world still uses Shattered's monsters for its depth. Pixel Spacebase (its art is for the pre-2017 tile layout at 4x scale and would need redrawing).

Abyssalnite, Cursed PD and Experienced are ported in full, finished 2026-10-15, with Reforged nearly so (details in PORTING.md). They bring bosses and special levels, so three things are new. Boss floors from Abyssalnite and Cursed are sandbox only until Onyx has played them (`WorldShuffle.bossPlaytested`). Levels that are not part of a normal run (Cursed's hub and challenge floors, Abyssalnite's final arena) are listed in `levels/SpecialFloors` and opened from the sandbox Floors panel under Special floors. Experienced's arena and black mimic fights are reached with its ticket and chest items. `scripts/test/BossArenaTest.java` plays the start and end of every mod boss fight headless and runs in every full check, read its WARN lines and play those fights in the sandbox. Cursed PD gives the hero a mana pool for its power items, shown as a thin blue bar under the health bar (built, not seen on screen yet).

### Fixes made to Shattered code

- `HallwayRoom.merge` null-guard, since Caves and Halls painters merge rooms that share no door.
- `CityPainter` and the ported Citadel and Jungle painters clamp `Random.Int(21 - depth)` to at least 2. Below floor 20 it decorated every wall.

## Building and testing

Normal path on a PC with internet: `./gradlew desktop:debug` to run, `./gradlew desktop:release` for a jar. This Gradle setup was edited (Android plugin and the `org.beryx.runtime` plugin removed, `application` plugin used) but never run in the original build environment, so fix it if it fails.

Onyx's playable copy. `scripts\make-exe.ps1` builds the release jar, wraps it with jpackage into `Documents\OnyxPixelDungeon-app\OnyxPixelDungeon\OnyxPixelDungeon.exe` (about 200 MB, with its own Java) and puts an "Onyx's Pixel Dungeon" shortcut on his desktop. It shares the normal save folder. The app has to be closed before the script can replace it. Because the app runs from its own copy, the repo can be rebuilt while he plays.

Desktop copy updates by itself. A git hook (`scripts/hooks/post-commit` and `post-merge`, switched on with `git config core.hooksPath scripts/hooks`, already set in this clone) runs `scripts/auto-update-desktop.ps1` in the background after any commit or merge that changed game code. It runs `make-exe.ps1`, so the desktop shortcut always points at the newest build. It never starts the game. If the game is open it skips and tries again at the next commit. Progress and errors are in `build/auto-update.log`. A failed build leaves the old app alone. Run `powershell -ExecutionPolicy Bypass -File scripts\auto-update-desktop.ps1 -Force` to update by hand. A fresh clone needs `git config core.hooksPath scripts/hooks` once. Builds made on the cloud routine cannot reach the desktop, merge and pull on this machine to get them.

Gradle-free path used for releases (`scripts/`):

- `scripts/compile.sh <ShatteredPD-v4.0.2-Java.jar> <outdir>` compiles with javac, taking libraries from the official Shattered jar.
- `scripts/package-windows.sh <Java.jar> <Windows.zip> <outdir>` makes the full Windows zip, the small installer zip, the cross-platform jar, `OnyxPixelDungeon-game.jar` and `update.json`.
- **The screenshot tour deletes and overwrites save slot 1 of whatever save folder it runs in.** On 2026-10-13 a tour started straight from the release jar used Onyx's real save folder, because the jar's manifest title overrode the title passed on the command line, and it wiped his slot 1. `DesktopLauncher` now forces the tour into the `Onyx Gallery` folder and `Gallery.active()` refuses to run without that. Do not weaken either check.
- `scripts/gallery.sh <folder>` runs the screenshot tour (`Gallery.java`, switched on by `-Donyx.gallery`). The game plays itself through every ported world and the sandbox windows and saves a picture of each, in its own save folder. Use it to look at worlds and UI without Onyx clicking through them. Read the pictures back to check. After the worlds it spawns every ported monster beside the hero, lets it act, pictures it and kills it. Add `-Donyx.gallery.only=mobs` to the java line to skip the worlds. All 49 ported worlds, the sandbox windows and Devoted's 29 monsters were checked this way on 2026-10-13 with no errors.
- **Porting and testing tools (2026-10-14).** `scripts/check.sh` compiles and runs `WorldShuffleTest` plus `MobArenaTest`, the headless testing ground (`scripts/test/`, with `TestBoot` for the window stubs and `mob-baseline.txt` for known harness limits). `scripts/arena.sh` shows chosen monsters together in one picture. `scripts/mod-report.py` plans a port, `scripts/port-mob.py` ports a batch of monsters in one command, `scripts/get-mod.sh` clones from `scripts/mods.txt`, `scripts/reforged-stats.py` helps with table based stats. `ModRoster` is the single place a ported monster is registered, one line makes it spawn, show in the bestiary and sandbox list and get tested. Full details in the `port-mod` skill.
- `scripts/test/WorldShuffleTest.java` builds every floor of N seeded dungeons headlessly, checks all 24 worlds appear and have strings, and saves and reloads each ported world. Run it after any world change.

## Releases and updates

- `.github/workflows/release.yml` builds, runs the test on 1000 seeds, packages and publishes a GitHub release. Bump `appVersionName` in `build.gradle`, then on GitHub go to Actions, Build and release, Run workflow. It tags the release from the version.
- Windows auto-update. `scripts/windows-installer/Play.ps1` (run by Play.bat and the installer's desktop shortcut) checks the latest release's `update.json`, downloads `OnyxPixelDungeon-game.jar` if the version differs, verifies its SHA-256 and swaps it in. If the release needs a different Shattered base, it tells the player to rerun the installer instead. Keep the asset names stable.
- The game jar is loaded ahead of the official Shattered jars on the launcher classpath, so it only needs the game's classes and changed assets.

## Mods to add next

Onyx wants more mods in the rotation. This list comes from his "Every Pixel Dungeon" index artifact (https://claude.ai/code/artifact/c3216af8-d926-46fd-9c16-d68d9a6e958f), which catalogs sprites, items and tile sheets from 145 Pixel Dungeon mods. Its `data/index.json` lists every mod's repo and tile sheets, built from the community mod lists kept by Dominowood371 and zay448345045 on GitHub. None of these have had their licenses or art credits checked yet.

**How to judge a mod.** Shattered 0.7 or later uses this game's 256x256 tile sheet layout, so its worlds port the way the current 19 did (tileset, water, painter, a small level class). Older mods use a different layout and their art would need redrawing. Check the repo's license and any in-game credits for art or music with separate terms. Music from every mod is imported, including franchise mods, by `scripts/fetch-mod-music.py` and `scripts/import-mod-music.py` (the song files are git-ignored, rerun the scripts to get them).

**More worlds from mods already in the game.** Easiest, same code and licenses.

- Magic Ling PD has unused tile sheets for gold, wild (desert), cold, zero spring, ghost, died and morpheus. Some may be unfinished, so look at them first.
- Transformation PD has UES and two surface sheets (lush, winter).

**Shattered 0.7 or later, likely straightforward.**

| Mod | Base | What it adds | Repo |
| --- | --- | --- | --- |
| Shattered PD Matt Edition | 3.2.5 | Ice and snow, guild | https://github.com/mr-matthias1829/SPD-Matt-Edition |
| Pixel Dungeon Reforged | 3.0.0 | Kobold caves and region variants | https://github.com/nathanlink169/pixel-dungeon-reforged |
| Shittened Pixel Dungeon | 2.5.4 | Christmas caves | https://github.com/Haukii/shittened-pixel-dungeon |
| Abyssalnite Pixel Dungeon | 0.8.2 | Region reskins, 36 enemies | https://github.com/MywaifuisTataraKogasa/Abyssalnite-Pixel-Dungeon |
| Cursed Pixel Dungeon (Legacy) | 0.7.3b | Heaven, beach, temple, surface | https://github.com/Smujb/cursed-pixel-dungeon-lgc |
| Tower Pixel Dungeon | Shattered (version unclear) | Goblin, mage, gates and mines areas. A tower defense mod, so its levels may not fit a normal floor | https://github.com/FixAkaTheFix/Tower-Pixel-Dungeon |

**Older bases, check the tile format first and expect redrawing.**

| Mod | Base | What it adds | Repo |
| --- | --- | --- | --- |
| Lovecraft Pixel Dungeon | 0.6.x | Overworld, eldritch theme | https://github.com/Sadsaltan/LovecraftPixelDungeon |
| Dragonic Pixel Dungeon | 0.6.1b | Dragon lair | https://github.com/duyhoang5152/DragonicPixelDungeon |
| SPecial Surprise Pixel Dungeon | 0.4.1 | Beach, forest and many more, 45 enemies | https://github.com/hmdzl001/SPS-PD |
| Goblins Pixel Dungeon | 0.3.0 | Goblin theme | https://github.com/locastan/GoblinsPixelDungeonGradle |
| Unleashed Pixel Dungeon | 0.3.0 | Region variants | https://github.com/FthrNature/unleashed-pixel-dungeon |
| Deep Pixel Dungeon | 0.4.3c | Extra regions | https://github.com/Mochiee/deep-pixel-dungeon |
| Pixel Spacebase | 0.4.3 | Sci-fi space station, art at 4x scale | https://github.com/codefitz/Pixel-Spacebase |

When adding a mod, append its worlds to the end of `WorldShuffle.WORLDS`, add the stage each was made for to `WorldShuffle.REGIONS` (check the mod's own `Dungeon.newLevel`), add name and mod strings, credit it in the file header and README, and run `WorldShuffleTest`.

## Next planned work (Onyx's sandbox plan)

He wants to playtest everything, so build these next.

1. **Sandbox mode.** A separate start option, kept apart from normal runs (badges and rankings off). Everything unlocked. A god mode panel to skip to any floor, force a specific world or mod, toggle mods on and off in the shuffle, edit stats, and give any item.
   Built so far. The Sandbox button on the title screen sets `Dungeon.sandbox`, which is saved with the run and turns off badges, rankings and remains and unlocks every hero and option. In a sandbox run the in-game menu has a Sandbox button that opens `WndSandbox`, which skips to any floor (`InterlevelScene.Mode.SANDBOX`, always builds the floor fresh) and forces a world for new floors (`Dungeon.sandboxWorld`, checked in `Dungeon.newLevel`). Boss floors and floors 25 and 26 ignore the forced world. `WorldShuffleTest` builds every world on every floor this way. God mode (`Dungeon.godMode()`, on by default, checked in `Hero.damage`, `Hero.die` and `Hunger`) stops damage, death and hunger. The panel also levels up, adds strength, heals, reveals the floor and opens `WndSandboxItems`, which gives any item from the journal catalog at a chosen upgrade level. Full sight (`Dungeon.seeAll()`, on by default, applied in `Dungeon.observe`) shows the whole floor. `WndSandboxMobs` spawns any bestiary monster next to the hero, leaving out Tengu, DM-300, its pylons and the Dwarf King, which crash outside their boss levels. A Sandbox button sits on the game screen left of the menu button (`GameScene.create`). Sandbox runs do not write to the journal catalog or bestiary. Still to do. Forcing by mod, toggling mods in the shuffle.
   Also built. Ctrl+R in a sandbox run builds the current floor again with a new layout, and re-rolls its world if none is forced. Ctrl+N jumps to a random floor in a random world from the mods that are on, without changing a forced world (`SandboxHotkeys`, `Dungeon.sandboxSalt` and `Dungeon.sandboxOnceWorld`). `WndSongs` lists every song by mod and plays the one clicked, carrying on floor after floor until the floor's own music is chosen again. It is in the game menu and the sandbox panel. `-Donyx.gallery.only=ui` runs a short tour of these.
   Also built. Mod toggles for the shuffle (`Dungeon.sandboxOffMods`, in the Floors and worlds panel), which also covers forcing one mod by turning the rest off. The world picker and compendium are scrolling lists. Devoted's warpick and wooden boomerang are in the loot tables, with pictures in row 13 of `sprites/items.png`, which is kept for items from other mods (`ItemSpriteSheet.MOD_ITEMS`).
   Blocked on 2026-10-13. Copying more mod source code into the project (tried Transformation's tower monsters) was denied by Claude Code's auto mode check for integrating untrusted code. Onyx needs to allow it before more monsters or weapons can be ported.
2. **Mod intro (built, `ui/ModIntro`, shown from `GameScene.create` on arriving in a ported world, not yet seen on screen).** Before each floor, the source mod's name and logo fade in. Most of these mods have no proper logo, so use their title text or app icon.
3. **Compendium (built, `WndCompendium`, opened from the game menu and the sandbox panel, lists worlds by mod from `WorldShuffle`).** A quick in-game catalog, by mod, of what each one contributes (worlds, tilesets, and later monsters and items). Doesn't need to be perfect.
4. **Later.** Port monsters, starting with whichever world he likes most.

Also, his promo banner art says "50+ MODS". The game currently has content from four mods, so suggest an accurate label like "24 WORLDS" if it comes up.

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
