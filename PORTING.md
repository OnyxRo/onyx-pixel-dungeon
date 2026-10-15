# Porting status

Update this at the end of every task. Details live in CLAUDE.md.

## Done per mod
- Combusted PD: 3 worlds, 4 throwable weapons (catalog only). No monsters exist.
- Devoted PD: 6 worlds, all 54 monsters, 5 boss levels, 2 weapons, 11 items. Complete except the stubs below.
- Matt Edition: 2 worlds, 13 monsters plus Dark King, 9 blacksmith weapons, Glock (catalog only).
- Magic Ling PD: 13 worlds. No monsters or weapons yet.
- Transformation PD: 8 worlds. No monsters or weapons yet.
- Reforged PD: 1 world, 7 monsters (Kobold, Spitter, Ballista, QuickFiringBallista, Fiend, UnholyPriest, Wyrm), Leech curse, Cursed and Miasma buffs, MiasmaGas, Demon Goo, the Rat Usurper boss with `SewerBossUsurperLevel`, Rat Claw. The Wyrm has weight 0 (sandbox only, it is a mining quest boss). Left out on purpose. Construct Hero needs Reforged's hero talents (CONSTRUCT_MOBILITY, CONSTRUCT_VISION, CONSTRUCT_LETHALITY), which do not exist here. Half Ripper is an escort quest that needs Reforged's quest and level hooks (`RegularLevel.GetExitRoom`, its own Quest spawn). Its combat package is not ported, stats were rebuilt on Shattered's damage.
- Abyssalnite PD: 5 worlds, 10 monsters with real descriptions, 4 weapons, 19 items, and all its bosses. Old and new Tengu, DM-300, King, Yog with their eight arena levels (Old and New Prison, Caves, City, Halls), plus the final arena (`FinalLevel`, septium pillars and Cthulhu) and the ritual items. Each boss floor of an Abyssalnite world picks the old or the new fight from the dungeon seed (`WorldShuffle.modBossVariants`). Not played yet, so these boss floors are sandbox only (`WorldShuffle.bossPlaytested`). The final arena is reached from the sandbox Floors panel, Special floors. Left out on purpose, Armor Kit and Tome of Mastery (class upgrade systems Shattered 4 removed).
- Experienced PD: everything except its cycle system and Perks. All items, bags, rods, boosters, exotic items, Hook, Bbat, Exp Generator, the OOF Thief, the arena branch, the black mimic branch, the Fate Lock with grinding mode.
- Cursed PD (Legacy): 20 weapons with the relic enchantments, 12 power spells on a mana pool, bags, instruments, dragon crystal allies, the lucky badge, Wandmaker 2 and Hau Tul, its 24 monsters with real descriptions, its own boss floors and bosses (under a Cursed prefix) and all its challenge and hub floors. The challenge floors are special floors, see below.
- Shittened, Summoning, Re-ARranged, Ryuse, Rat King 2: worlds only (see CLAUDE.md table).
- Mod songs: fetched and imported by scripts/fetch-mod-music.py and scripts/import-mod-music.py. Files are git-ignored, rerun the scripts. Browsable in WndSongs.

## Left to do
- Monsters and weapons from Magic Ling (about 350 monster files, 74 weapons) and Transformation (59, 35).
- Monsters, weapons and items for the other ten mods (counts in CLAUDE.md).
- Mods not started: Tower PD, Lovecraft, Dragonic, SPS, Goblins, Unleashed, Deep, Pixel Spacebase.
- Sandbox: forcing a single mod by one click (toggling the rest off works today).

## Known issues
- World names and regions for the second wave were guessed from tile sheets, correct as Onyx playtests.
- Licenses and art credits unchecked for every mod. Fine for personal use, check before public release.
- Stubbed in Devoted: Void side level, bounty hunter infusion, Zealot class, ally summons, some icons.
- Matt Edition: friendly thief quest, augments, animation speed setting left out.
- Mod intro (ui/ModIntro) built but not yet seen on screen.
- Transformation monster port was blocked by the auto mode check on 2026-10-13. Onyx must allow copying mod source.
- Tools: port-copier.py, port-strings.py, port-itemsprites.py, gallery.sh, scripts/test/WorldShuffleTest.java.

## Open feature requests
- Two modes on the start screen. "Single mod" locks every floor to one chosen mod's worlds, monsters and items. "Shuffle" is the current floor-by-floor mix.
- Already built, no action needed. Mod logo top left with summary panel (WndModInfo). Ctrl+R and Ctrl+N (SandboxHotkeys, sandbox runs only).

## Cloud routine
- TASKS.md is the queue, ROUTINE.md holds the routine prompt and helper setup, scripts/cloud-setup.sh prepares the cloud environment. Not yet tested in a cloud run.
- 2026-10-14. Sandbox mod panel now has an "Only X" row under each mod. WorldShuffleTest checks every ported monster has a name string. Mod logo was already top left, and WndModInfo now ends with a credit line for the summary source.
- Hotkeys decision. Ctrl+R and Ctrl+N stay sandbox only. In normal runs a reroll would break seeded fairness and let players dodge bad floors.
- Single mod mode built 2026-10-14. Game Options pane on the hero select screen has a Game Mode button (Shuffle or one mod), saved in `Dungeon.singleMod`, used by `WorldShuffle.worldFor`. Worlds and monsters follow the mod (monsters only where that mod has ported ones). Items are not restricted yet, loot tables are still Shattered's. Fixed floors still use Shattered's home worlds. WorldShuffleTest covers it. Not seen on screen.
- 2026-10-14. README rewritten to match the game (54 worlds, monsters, sandbox, single mod mode). Tried to start the Reforged monster port, cloned the repo to ~/mod-sources/reforged, but the auto mode check denied reading it again. Onyx needs to add a Bash permission rule that allows it.
- 2026-10-14. Reforged monsters ported by hand, since its Constants.mobs stats and combat package do not fit Shattered's Mob. Cursed adds -10% accuracy, -30% evasion, +10% damage taken (Char.hit, Char.damage), Bless removes it. Miasma halves Healing. WorldShuffleTest passes on 150 seeds, 80 ported monsters have names. The auto mode check denied mod source reads at random, retrying worked.
- 2026-10-14. Infrastructure built after porting Reforged by hand took 40 minutes. See the port-mod skill (.claude/skills/port-mod/SKILL.md) and CLAUDE.md. New, scripts/check.sh (headless tests in about 35 seconds, 150 seeds and every ported monster), scripts/test/MobArenaTest.java and TestBoot.java, scripts/arena.sh (one picture of many monsters, 16 seconds), scripts/mod-report.py, scripts/port-mob.py, scripts/get-mod.sh with scripts/mods.txt, scripts/reforged-stats.py, ModRoster (one line registers a monster). Bestiary, WorldShuffle.mobRotation and the sandbox now read ModRoster, so ReforgedSpawner was deleted. port-copier.py now uses an absolute path for gradle and stops if gradle fails without javac errors. Trial on Abyssalnite ported ten monsters in a few minutes.
- MobArenaTest known problems live in scripts/test/mob-baseline.txt (19 lines), mostly sprite, emitter and teleport code a headless run cannot do, plus RedBrawler whose BrawlerSprite casts to the wrong brawler. Looking at those by eye is the next improvement if they ever matter.
- 2026-10-14. Rat King PD 2 checked with mod-report. Repo Zrp200/rkpd2, added to scripts/mods.txt as `ratking`. It has no monsters, and its weapon list is Shattered curses plus a Dream Dart (sleep dart). Nothing ported. The Dream Dart could be added later if Onyx wants it.
- 2026-10-14. Item infrastructure built. scripts/port-item.py (copies items, fixes drift, pictures, strings, catalog, check), scripts/test/ItemArenaTest.java (run by check.sh on changed items), port-copier.py skips Perks, TreasureBag and GambleBag (PORT_SKIP env), mod-report.py ignores classes we already have by name. Used on Experienced, seven weapons ported (catalog only, no loot tables). Shims, Hero.enemy() and TargetedCell.colored(). Gleaming Staff drops Gold instead of Experienced's GambleBag. The auto mode check denied writing .claude/settings.json (self modification), Onyx should add allow rules for scripts/port-*.py himself.
- 2026-10-15. Port tools now share scripts/renames.py (rename table, package rewrite, old flat Assets names, long to int) and print a short ERRORS BY TYPE summary after a failed compile (full text in build/compile-errors.log). port-copier finds flat asset names from pre 0.8 mods, port-strings finds text under resources. Used on Cursed PD, 14 monsters ported, the Gnoll Battlemage has its own sprite class (GnollBattlemageSprite) because ShamanSprite is abstract and casts to Shaman. EarthenGuardian and MageCrab lost the limited-drop counter, they drop their loot 50% of the time. NonBossGoo and GnollBattlemage headless turn crashes are in the baseline (stub sprite). Ryuse's PD repo is still unknown.
- 2026-10-15. Ported Abyssalnite's four weapons and 14 Experienced items, all pass check.sh. The item sheet had no free cells, so items.png grew from 512 to 576 pixels high and MOD_ROWS in port-itemsprites.py now covers rows 33 to 36 as well. Cleaned out Experienced items that need its Perks, RageShield, Cheese or exotic potion mapping. Reforged's three bosses and Abyssalnite's eight bosses were not started, each needs its own boss level and the whole set is its own large task. Ryuse's is still blocked on the repo link.
- 2026-10-15. Reforged boss work. Only the Rat Usurper is a boss floor boss in Reforged, Demon Goo is a regular deep monster and the Wyrm belongs to its mining quest. `WorldShuffle.modBossLevel` returns the Usurper level for Reforged worlds on floor 5. Kobold Caves is in the CAVES region so a shuffled run never rolls it on floors 1 to 4, which means the Usurper only turns up in single mod mode and when forced in the sandbox. Ported RatClaw, CrossRoom, UsurperRoom and two sprites. DemonGoo clones drop no loot and the original drops a potion half the time. DemonGoo is in the mob baseline (headless emitter limit). RatUsurper is not on the roster, so MobArenaTest does not cover it.
- 2026-10-15. Abyssalnite bosses surveyed, none ported. Its OldTengu, NewTengu, OldDM300, NewDM300, King and Yog are the 0.7 and 0.8 versions of Shattered's own bosses, used on its extra floors 21 to 27. Cthulhu and SeptiumPillar have no sprites and Septium needs Statistics.killcount. Eleven boss levels of about 4700 lines would add nothing new to play, so this is waiting on Onyx.

## Core shims and systems added for mods (2026-10-15)
See the port-mod skill for the list of shims and what they do. In short, Cursed mana (`Hero.MP`, `MpRegen`), Experienced boosters and branches (`Dungeon.respawnBoost`, `BRANCH_ARENA`, `BRANCH_BLACK`), grinding mode, `Level.changeMap`, legacy `entrance` and `exit` fields turned into transitions in `Level.create`, `Terrain.SP_RUNE` and `Terrain.SIGN`, `Statistics` counters for the septium pillars, `Mob.multiplier`, `spawnAt`, `spawnAround4`, `followHero`.

## Special floors
`levels/SpecialFloors` lists levels that are not part of a normal run (Cursed hub, grinding, safe, amulet and the four challenge floors with their bosses, Abyssalnite final arena). A sandbox run opens them from Floors and worlds, Special floors (`WndSpecialFloors`). `SpecialFloors.travel(Supplier<Level>)` is also what Wandmaker 2 and the Lucky Badge use. Only works in sandbox runs.

## Boss floors from mods
`WorldShuffle.modBossLevel` and `modBossVariants`. Only Devoted and Reforged are in `bossPlaytested`, so their boss floors can appear in normal runs. Abyssalnite and Cursed boss floors appear in the sandbox only until Onyx has played them and they are added there. `scripts/test/BossArenaTest.java` walks every one into its arena, kills the boss and checks the floor unseals. Warnings in its output name the fights it could not finish headless, play those in the sandbox. The multi-phase ones (prison and Tengu fights, the final arena) are listed in `MULTI_PHASE`.

## Open questions for Onyx
- Play the Abyssalnite and Cursed boss floors in the sandbox and say which are safe for normal runs.
- Cursed PD mana bar was built into `StatusPane` but not seen on screen. Check it, and say if it should sit somewhere else.
- Reforged Construct Hero and Half Ripper were not ported, see the Reforged line above.
