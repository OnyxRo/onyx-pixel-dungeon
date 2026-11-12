# Game notes

History and reference moved out of CLAUDE.md on 2026-11-12, because CLAUDE.md loads into every session and every agent. Search this file for a mod or system (`grep -n -i <word> docs/game-notes.md`) and read only that part.

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

Old tile layout (2026-11-01). Shattered rearranged its tile grid around version 2.0 (walls up a row, doors up two, chasm into the ground rows). Mods on Shattered 0.6 to 1.1 keep the old grid, so their sheets drew doors where walls belong (rows of locked doors). Check a mod's `DungeonTileSheet.java` against ours before using its sheet. `scripts/remap-oldsheets.py` rebuilds a sheet from the mod's original into the new grid, add any new old-grid sheet to its list. 33 sheets were rebuilt this way (Combusted, Cursed, Ryuse, Abyssalnite, Dragon Lair, Tower, Lovecraft, Magic Ling's Golden City). Onyx reviewed every world's stage the same day and the stages themselves were right.

Missing tiles. Sheets from older Shattered bases lacked up to 60 tiles that newer Shattered added, which would draw as nothing. Every world sheet now has those cells filled from Shattered's own sheet for the same stage, so a few decorations in older-base worlds are in Shattered's style. Do the same for any new sheet. Older-base sheets also had solid wall overhangs (cells 192 to 207), which hid the floor along the bottom of every room and any stairs there. `scripts/fix-overhangs.py` rebuilds them in the sheet's own wall colours, and `check.sh` fails on any new sheet that needs it (2026-10-27, 33 sheets fixed).

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

Ryuse's PD is ported (2026-10-16, repo Ryuse/My-Pixel-Dungeon, key `ryuse`). It is a small mod. It adds the Summoner hero class (`HeroClass.SUMMONER`, subclasses Eidolon and Attractor, armor ability `PhaseShift`, unlocked by reading three scrolls of mirror image in one run, Dried Rose ghost with its own level and experience in `DriedRose`, better Ghost quest rewards, Ryuse's lover dialogue), a helmet slot (`Belongings.helmet`, 16 helmets and 2 inscriptions in `items/helmets` and `items/inscriptions`, effects hooked through the static helpers in `Helmet`, found as `Generator.Category.HELMET`), Training Whip and Summoner Whip, the Eye of Wisdom artifact and its level (a special floor), the Rat Nest room on floor 4, Polluted Rat and Polluted Crab. Left out on purpose. Its 5 ring, 5 artifact and 6 armor slots and the Equipment Page bag (they rewrite the core equipment code), its Damage combat class, HP, EXP and shield bar art and loading screens. Also in, the ghost's helmet and ring slots, its Polluted Sewers chat, the three songs (`music/ryuse`), the Sorceress splash as a second Summoner picture and the demon sprite for the Imp when the hero is the Summoner. The mod gave the Summoner no talents, so hers are Shattered talents picked to suit her. Not played on screen by Onyx yet.

Not ported yet. Combusted is done. It adds no monsters, and its four throwable melee weapons (`items/weapon/thrown/`) are in the journal catalog only, since Combusted itself never put them in its loot tables. They drop at the enemy's feet instead of sticking. Magic Ling is finished (2026-10-24), every monster and boss of its dungeon with its arenas, its hollow and side fights as special floors, only its town characters left out (see PORTING.md). Shittened is done. Every non-Devoted world still uses Shattered's monsters for its depth. Pixel Spacebase, Deep and Unleashed worlds came in on 2026-10-19 from art Onyx redrew in Claude Design (see PORTING.md), SPS came in the same day from its redrawn sheets (14 worlds).

Abyssalnite, Cursed PD and Experienced are ported in full, finished 2026-10-15, with Reforged nearly so (details in PORTING.md). They bring bosses and special levels, so three things are new. Boss floors from Abyssalnite and Cursed are sandbox only until Onyx has played them (`WorldShuffle.bossPlaytested`). Levels that are not part of a normal run (Cursed's hub and challenge floors, Abyssalnite's final arena) are listed in `levels/SpecialFloors` and opened from the sandbox Floors panel under Special floors. Experienced's arena and black mimic fights are reached with its ticket and chest items. `scripts/test/BossArenaTest.java` plays the start and end of every mod boss fight headless and runs in every full check, read its WARN lines and play those fights in the sandbox. Cursed PD gives the hero a mana pool for its power items, shown as a thin blue bar under the health bar (built, not seen on screen yet).

### Fixes made to Shattered code

- `HallwayRoom.merge` null-guard, since Caves and Halls painters merge rooms that share no door.
- `CityPainter` and the ported Citadel and Jungle painters clamp `Random.Int(21 - depth)` to at least 2. Below floor 20 it decorated every wall.

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
