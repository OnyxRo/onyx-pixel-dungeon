---
name: port-mod
description: Port monsters, weapons or items from another Pixel Dungeon mod into Onyx's Pixel Dungeon. Use for any task in TASKS.md that starts with "Port", and whenever you add content from a mod. Covers the tools, the order to use them in, and the mistakes earlier sessions made.
---

# Porting from a mod

Everything here exists because porting Reforged cost about 40 minutes, most of it waste. Follow the order below and a batch of monsters takes a few minutes.

## Step zero, fix open crashes

Before porting anything, run `python -I scripts/crashes.py`. If it lists any open crash, invoke the `fix-bugs` skill and fix them first, then start the port. Fold the fixes into the port's check and commit (or commit them on their own if they touch unrelated code). Tell Onyx at the end which crashes were fixed. If the list is empty, say nothing and go on.

## The short version

```
python -I scripts/crashes.py                     open crashes first, fix them with the fix-bugs skill
scripts/get-mod.sh <key>                         clone the mod (scripts/get-mod.sh --list shows keys)
python -I scripts/mod-report.py ~/mod-sources/<key> --all     what is portable, what is hard
python -I scripts/port-mob.py <key> <roster key> Mob1 Mob2 ...   port the easy ones in one command
scripts/check.sh --new                           headless tests on what you changed, seconds
scripts/arena.sh                                 one picture of everything you changed, 16 seconds
```

Then update TASKS.md and PORTING.md, commit. Do not do anything else to check a monster.

## Rules that save time

- **Never use scripts/gallery.sh to check a port.** It spawns every ported monster one at a time, about ten minutes, and opens the game over and over. Use `scripts/check.sh` first and `scripts/arena.sh` once, at the end, for the whole batch. Never launch the game for a single monster. Never run the arena twice for the same batch unless you changed code because of what it showed.
- **Read the mod report before choosing what to port.** It already says which monsters are easy, which need a boss level, which use members the mod added to Shattered's own classes (like `Dungeon.cycle`), and which have no text. Do not open mod source files one by one to find that out.
- **Port a batch, not a monster.** Pass every easy monster to `port-mob.py` at once.
- **Use the Write and Edit tools for any file that contains backslashes.** The shell tool mangles them in heredocs and in `python - <<EOF` scripts, which broke regexes three times. Shell heredocs are fine for text without backslashes.
- **Do not `git checkout` the shared message files** (`messages/actors/actors.properties`, `items/items.properties`). They hold every earlier port's strings that are not committed yet. Fix a bad line by editing it.
- **If the auto mode check denies a command**, retry it once. It denied harmless commands on our own files at random. If it keeps denying, stop and tell Onyx what you were trying to do. Do not work around it.
- Run Python as `python -I`. Keep long output in files (`build/*.log`) and read only the tail.

## What each tool does

| Tool | Use |
| --- | --- |
| `scripts/mods.txt`, `scripts/get-mod.sh` | Known mods, their repos, and cloning to `~/mod-sources/<key>` |
| `scripts/mod-report.py <folder> [--all]` | Lists what the mod adds that we lack, with a tier per monster (easy, medium, hard), the reason for hard, whether there is a sprite and a name string. Writes `build/mod-report-<mod>.md` |
| `scripts/port-mob.py <key> <roster key> <Mobs...>` | Copies the monsters and their sprites, closes dependencies (`port-copier.py`), copies strings (`port-strings.py`), writes placeholder text for monsters the mod never described, applies known API fixes, registers in `ModRoster`, runs the headless check, prints what is left |
| `scripts/port-item.py <key> <Items...>` | Ports weapons, armor, rings, wands, artifacts, potions, scrolls, enchantments and glyphs in one command, see Special cases |
| `scripts/test/ItemArenaTest.java` | The item testing ground, run by `check.sh` |
| `scripts/reforged-stats.py <folder> <Mobs...>` | For mods that keep stats in a table (Reforged). Prints the fields to paste into a hand written monster |
| `scripts/check.sh [--new] [--quick] [--seeds N] [Mob...]` | Compiles, runs `WorldShuffleTest` and `MobArenaTest`. Full output in `build/check.log` |
| `scripts/test/MobArenaTest.java` | The testing ground. Per monster, with no window: name and description strings, save and reload, loot, damage and accuracy rolls, 25 rounds of combat against an unkillable hero, 60 turns of its own AI, death, plus a check that sprites do not cast their character to the wrong class, and that every file named in `Assets` exists |
| `scripts/test/mob-baseline.txt` | Problems that were there before the test existed, mostly things a headless run cannot do. `check.sh --write-baseline` regenerates it. Only do that after reading the new lines, a new entry may be a bug in our code |
| `scripts/arena.sh [Mobs...\|--roster]` | Puts the chosen monsters (default, the ones you changed) on one floor in the game itself, lets them act, saves `build/arena/arena.png`. Read the picture with the image reader |
| `actors/mobs/ModRoster.java` | The one place a monster is registered. A line there makes it show in the bestiary and the sandbox list and be covered by the tests. Its weight and depths no longer decide spawning (2026-11-01) |
| `scripts/mod-spawns.py` | Reads each mod's own spawn table (MobSpawner or Bestiary) and writes `actors/mobs/ModSpawns.java`, which decides what a mod's world spawns on each floor. Add a new mod to its `MODS` list and rerun with `--write`. Devoted, Matt, Lovecraft and Magic Ling keep their own spawner classes |

`ModRoster` keys are the mod's icon file name without the extension (`interfaces/mods/reforged.png` is `reforged`). `WorldShuffle.modKey(level)` gives the key for a floor.

## Order of work

1. Read PORTING.md and TASKS.md. Pick the task.
2. `scripts/get-mod.sh <key>`. If the key is not in `mods.txt`, find the repo with a web search and add a line.
3. `mod-report.py --all`. Decide the set. Easy monsters go in together. Leave bosses (they need their level), monsters that need mod only core members, and the ones in the "hard" tier for the end or for a follow-up task.
4. `port-mob.py`. It prints how many compile errors are left. Fix them by hand, using the drift table below. When you fix the same kind of error twice, add a rule to `RULES` in `port-mob.py`.
5. `scripts/check.sh --new`. A failure with a stack trace in the monster's own class is a bug in our code. Fix it. A failure inside `CharSprite`, `Emitter` or `GameScene` is the headless run's limit, add it to the baseline.
6. `scripts/arena.sh`. Read the picture once. Look for missing sprites, wrong frames, monsters standing still.
7. Update TASKS.md (check it off, or leave it unchecked and list what is left under it, adding new tasks for leftovers) and PORTING.md. Add the mod's monsters to the "Done per mod" line.
8. Commit, one commit per finished mod or feature.

## The rename table and the error summary

- `scripts/renames.py` is the one table of fixes for old code (renamed methods, `Assets.SND_X` to `Assets.Sounds.X`, flat `Assets.KUPUA` to `Assets.Sprites.KUPUA`, a mod's own package name to ours, long damage to int, `Dungeon.cycle`, helpers like `TargetedCell.colored`). `port-mob.py`, `port-item.py` and `port-copier.py` run it over every file they copy. Add a row when you fix the same compile error twice. The rows come from errors met so far, they were not mined from Shattered's history.
- Mods whose code lives under another package (Cursed PD is `cursedpixeldungeon`, Rat King 2 is `zrp200.rkpd2`) are found automatically (`renames.mod_package_dir`) and the package name is rewritten. Text kept under `resources/<package>/messages` is found by `port-strings.py`.
- After a failed compile the tools print "ERRORS BY TYPE", at most 14 lines, one per kind with its files. The full compiler output is in `build/compile-errors.log`.
- A ported file that fails to compile is cheaper to fix in place than to delete and port again. Run `python -I scripts/port-copier.py . ~/mod-sources/<key> <key> 6` after each round of hand fixes, it compiles and prints the summary.

## Known drift from older Shattered bases

Mods on Shattered 0.8 and older hit these. The first two are fixed by `port-mob.py` automatically.

| Compile error | Fix |
| --- | --- |
| `attackDelay() ... attempting to assign weaker access privileges` | make it `public float attackDelay()` |
| `throwChar ... required: Char,Ballistica,int,boolean,boolean,Object` | add `, this` as the last argument |
| `add(Buff) in X cannot override add(Buff) in Mob, return type void is not compatible with boolean` | return boolean. An empty override becomes `{ return false; }` |
| `move(int) in X cannot override move(int) in Char, overridden method is final` | override `onMove` style hooks instead, or drop the override and port the effect another way |
| `cannot find symbol class SkeletonKey` and other vanilla classes removed since 0.8 (`WndBag.Mode`, `WndBag.Listener`, `Terrain.SIGN`, `Speck.KIT`) | the mod cannot supply them, stub or remove. Usually a sign the class belongs to a boss level or an item and is not worth porting |
| `Bones.get()` returns a list now | take the first item |
| `BuffIndicator.<Something>` the mod added | use an existing icon that is close |
| `Dungeon.cycle`, `Dungeon.escalatingDepth`, `Dungeon.oofedItems` and other core members the mod added | the monster belongs to that mod's own game mode, skip it (mod-report marks these hard) |
| `damageRoll()` returns long, `EXP = 45000000000L` | an Experienced PD monster built for its giant numbers, skip it |

## Special cases

- **Stats in a table (Reforged).** `GetConstants()` and `Constants.mobs.<name>` mean the monster class has no numbers in it. Run `reforged-stats.py`, write the class by hand on Shattered's `Mob` (`HP`, `defenseSkill`, `EXP`, `maxLvl`, `damageRoll`, `attackSkill`, `drRoll`, `loot`). Reforged's `maxLvl` of -2 is not a level cap, pick one from the depth the monster belongs to. Its combat system (`combat/`) is not ported, so on-hit effects become `attackProc` overrides and ranged attacks keep Shattered's flow, which calls `onAttackComplete()` from the sprite's missile callback.
- **Monsters with no text.** Some mods never wrote strings (nine of Abyssalnite's). `port-mob.py` writes a plain name and a description that says so. Read them in PORTING.md later and write better ones when Onyx asks.
- **Borrowed sprites.** A monster that uses another monster's sprite can crash, because sprites often cast their character (`((Warlock)ch).onZapComplete()`). `MobArenaTest` catches this. Fix it with a small sprite subclass, like `ChronosSkullSprite`.
- **Bosses.** They need their boss level, and `WorldShuffle.bossWorldFor`, `modBossLevel` and `dropProgressionItem` for floors 10 and 20. The Devoted bosses are the model. Leave them for a task of their own.
- **Weapons and items.** `python -I scripts/port-item.py <mod key> Item1 Item2 ...` does for items what `port-mob.py` does for monsters. It copies the classes, fixes drift (long damage, `Dungeon.cycle`, `Dungeon.Float`, `internalTier`, `abilityChargeUse`, `durabilityPerUse`), copies item pictures, closes dependencies (never copying Perks, TreasureBag or GambleBag, add more with `--skip=`), copies strings, writes placeholder text, adds each item to the journal Catalog in the right category (which also puts it in the sandbox item list) and runs the check. It does not add loot table entries. `scripts/check.sh` then runs `ItemArenaTest` on changed items (strings, picture, save and reload, damage at +0 and +3, a hit on a rat, enchantment and glyph procs). `ItemArenaTest --all` covers every catalog item. Known headless limits (emitters on the stub sprite) go in `scripts/test/mob-baseline.txt` as "Class what". Test run on 2026-10-14, Experienced's seven weapons needed two hand fixes after the tool (a loot bag replaced by Gold, one by-hand edit that is now a rule).
- **Shims added for mods.** `Hero.enemy()` returns the enemy being attacked. `TargetedCell.colored(pos, color)` replaces the mod's `new TargetedCell(pos, color)`. `Random` is imported automatically when a copied file uses it.
- **Mod reports.** `mod-report.py` no longer counts a class as new when we have the same class name anywhere (Rat King 2 showed Shattered's own curses as new).
- **Spawning follows the mod (2026-11-01).** Never give monsters depth 1 to 99 to make them spawn. Cursed's and Abyssalnite's were set that way and put 100 damage monsters on floor 2. Spawning comes from `scripts/mod-spawns.py --write`, which copies the mod's own table and lines its regions up with ours (`MOD_STARTS`, `WORLD_TABLES` for levels from regions we lack). Check `python -I scripts/mod-spawns.py -v` and the NOT IN GAME line after a port.

## Definition of done

`scripts/check.sh` passes (full run, not `--quick`), the arena picture looks right, TASKS.md and PORTING.md are current, and one commit holds it. If the full check has a failure you did not cause, say so.

## Faster editing, triage and shims (added after the four-mod completion run)

- **Edit with `scripts/edit.py`, never heredocs or sed.** Write a plain text spec with the Write tool (`@@ file`, then `<<<<` old `====` new `>>>>`), run `python -I scripts/edit.py spec.txt`. It keeps each file's line endings, stops if an old text is missing or appears twice, and needs no backslash escaping. Use `--dry` to test. The shell mangled backslashes (`\1` became a control character) four times in one session, each cost several round trips.
- **Triage before porting.** `mod-report.py --all` now tiers every class, not only monsters. Items and weapons come out as easy, shim or hard, plus a ready `port-item.py` command for the easy batch. Port easy first, in one command. Then write the shim (next point), then the shim tier. Leave hard (bosses, levels) for a live session.
- **One compat shim per mod, written on purpose.** When several errors name the same missing core member (`Dungeon.fireDamage`, `Hero.MP`, `Mob.spawnAt`), add it once to the core class as a small additive field or method with a comment naming the mod, then port again. Do not patch each copied class. Shims already in core, so do not write them again
  - Cursed PD, `Hero.MP`, `MAX_MP`, `magicSkill`, `loseMP`, `increaseMagicSkill` with the `MpRegen` buff, `MeleeWeapon.damageMultiplier`, `Char.isEnemy`, `HPPercent`, `missingHP`, `Mob.spawnAt`, `spawnAround4`, `followHero`, `Hero.ready` made public.
  - Abyssalnite, `Food.clean` and `cureinfc`, reverse lookups only in `ExoticPotion.exoToReg` and `ExoticScroll.exoToReg` for exotic items that have no brewing recipe.
  - Experienced PD, `Dungeon.respawnBoost`, `additionalMobs`, `fireDamage`, `oofedItems`, `LimitedDrops` entries, `Hero.grinding` and `totalExp`, branches `BRANCH_ARENA` and `BRANCH_BLACK` in `Dungeon.newLevel`, `MimicSprite.Black`.
- **Exotic potions and scrolls need an `exoToReg` entry** or `ItemArenaTest` reports "no public constructor". Add the reverse lookup only.
- **`port-skip.txt` is the never copy list.** If `port-copier.py` keeps bringing back a class you deleted, add its name to `scripts/port-skip.txt`. `mod-report.py` marks those classes as skip.
- **The item sheet grows by itself** (`port-itemsprites.py` adds eight rows when full). No manual step.
- **Headless crashes** from a null emitter, sprite or field of view print as `HEADLESS (not counted)` in `MobArenaTest` and no longer fail the run. Use `-Dstrict=true` to see them as failures. Check that the monster really behaves in `arena.sh` when one shows up.
- **Dead code left by renames.** `Dungeon.cycle` becomes `0`, so Experienced code shows `0 > 0` branches. They compile and do nothing. Leave them, a rewrite is not worth the time.

## The improvement loop (do this every task, it is part of the task)

1. While working, keep a running note of every step that cost more than a couple of minutes or went wrong twice.
2. Before committing, turn each into a tool change (a rule in `renames.py`, a line in `port-skip.txt`, a check, a script option) or a skill paragraph. A note alone does not count.
3. Add a dated entry to `RETRO.md` saying what cost time and which change removes it. Keep it to a few lines.
4. Rule of thumb for the next run. If the same fix was done by hand twice, the third time it goes into a tool first.

## Bosses, levels and special floors (added after the Abyssalnite and Cursed boss ports)

- **Classes that share a name with Shattered's own** (a mod's CavesBossLevel, King, Yog, Goo, HeroicLeap) are skipped by every other tool, so they never show in mod-report. Use `python -I scripts/port-prefix.py <mod> <Prefix> levels/CavesBossLevel actors/mobs/King ...`. It copies them as `<Prefix>Name`, renames every reference, copies their text under the new key, and closes dependencies. Then register them yourself.
- **Registering a boss floor.** `WorldShuffle.modBossVariants(world, depth)` lists every fight a mod gives for a boss floor, `modBossLevel` picks one by seed. Add the mod to `modBossVariants`. A new mod's boss floors stay sandbox only until `bossPlaytested` lists it, which only Onyx decides after playing them.
- **Levels that are not boss floors** (hubs, challenge floors, arenas) go in `levels/SpecialFloors.java`. They show in the sandbox Floors panel under Special floors, and `SpecialFloors.travel()` moves the hero to one.
- **Old style levels** (mods on Shattered 0.7 and older) set `entrance` and `exit` directly and swap maps with their own `changeMap`. `Level.create` now gives them transitions and `Level.changeMap` exists. Renames handle the rest (`press` to `occupyCell`, `standardRooms`, `nMobs`, `respawnTime`, `Bones.get`, `SkeletonKey` to `WornKey`, `scaleWithDepth`). A level built on `RegularLevel` with rooms is faster to rebuild from this game's own matching boss level, as `CursedSewerBossLevel` was.
- **Always run the boss test after registering a floor.** `scripts/test/BossArenaTest.java` runs in every full `check.sh`. `BOSS_ONLY=Name scripts/check.sh --new` runs one. FAIL lines are real, a hang prints its stack. Check what it prints as WARN, those are fights it could not finish headless, play them in the sandbox. Do not add a walk from entrance to exit as a check, Shattered's own arenas fail it.
- **Sandbox full sight breaks "spawn the boss out of sight" loops.** The rename rule `boss placement out of sight` handles the usual form. Any new loop of that kind needs `&& !Dungeon.seeAll()`.
- **Trial copies.** `port-copier.py` follows references into whole systems (Reforged's combat package came along with the Wyrm). Commit first, read the COPIED list, and add what should never come in to `scripts/port-skip.txt`. Undo a trial by deleting what `git status --short` shows as new.
- **`edit.py` tolerates trailing whitespace** differences when the block is unique, but not different line counts. Prefer several one line edits to one long block when a file's blank lines are unknown.
- **Do not trust a copier result printed after a failed edit step.** Chaining commands with `&&` and then reading a log can show the old log. Read the log's time, or run the steps one at a time.

## Added after the Matt Edition, Experienced perks and Rat King finish (2026-10-15)

- **Sprites are checked by `scripts/check-sprites.py`** (part of every `check.sh`). A sprite that names a frame its picture does not have crashes the game when the monster is made, and no headless test can see it. When a port borrows another sheet, check the frame numbers against the picture size, the OOF Thief used rows the normal thief sheet does not have.
- **`TX_HEIGHT` in `ItemSpriteSheet` must equal the height of `sprites/items.png`.** `port-itemsprites.py` keeps it in step when it grows the sheet. If a sheet is ever grown by hand, change the constant too, or every icon is stretched.
- **Same simple name, different package.** `port-copier.py` and `mod-report.py` skip a mod class when this game has a class with that simple name anywhere. Matt's `levels/IceCavesLevel` was skipped because Combusted has `levels/worlds/IceCavesLevel`. Use `port-prefix.py <mod> <Prefix> levels/IceCavesLevel` and then change every reference by hand. A class that is named in a reference but never copied is the sign of this.
- **Branches.** Branch 2 holds Experienced's arena (floor 28), Matt's Thieves Guild (floors 8 and 9) and Matt's Ice Caves (floors 14 to 18), picked by depth in `Dungeon.newLevel`. Named transitions (`transitionId`, `targetTransitionId`) pair one door with one door, an unnamed lookup prefers unnamed transitions. A door that must work in a mod's own boss level and ours should leave the target id null. `scripts/test/BranchLevelTest.java` builds every transition and its way back, add new branches to it.
- **Perks exist now.** `Hero.perks`, `Perks.Perk`, and the hooks listed in `Perks.java`. `renames.py` no longer strips perk checks from copied Experienced code. A new perk needs its enum entry, its strings under `actors.hero.perks.*`, its hook, and a line in `PerksTest`.
- **The auto desktop update hook rewrites `desktop/build/libs/desktop-*.jar` after each commit.** For a few minutes `arena.sh` and `gallery.sh` fail with `NoClassDefFoundError`. Wait until the jar size stops changing, then run them again.
- **Seeing windows.** `JAVA_TOOL_OPTIONS=-Donyx.gallery.only=ui scripts/gallery.sh <folder>` takes about 40 seconds and pictures the sandbox windows, the hero window, the perks window and the display settings. Add a case to `Gallery.java` for any new window, then read the pictures with the image reader. Check `build/gallery-ui.log` for exceptions, a window that fails to build shows up there.
- **Shims added in this round** (do not write them again). `LevelTransition.transitionId`, `Level.getTransition(type, id)`, `Level.canSpawnItems`, `canSpawnFood`, `canSpawnGurItems`, `canFeeling`, `splashRegionOverride`, `RegularLevel.setItemsToSpawn` and the per kind counts, depth overrides on `StandardRoom.createRoom`, `EntranceRoom.createEntrance`, `ExitRoom.createExit`, `Weapon.special` and `Armor.special` (Matt's special augments), `Enchantment.removableByUpgrade` and `Glyph.removableByUpgrade`, `Hero.vitalityBonus`, `Dungeon.vitalityPotionsUsed`, `SPDSettings.animSpeed`.
- **Left out on purpose, so a report that lists them is not a mistake.** Matt's Seven Sins mode, armor magic levels (Scroll of Magic Upgrade, Scroll of Pure Magic, Magic Infuse), Experienced's cycle system and Efficient Bartering perk, Armor Kit and Tome of Mastery. `port-skip.txt` lists the ones a tool would otherwise copy.

## Crash hunting and testing efficiently (2026-10-15)

- Headless tests (`check.sh`) cannot see sprite, effect, font or scene crashes. Real window bots can. Read the Working faster section in CLAUDE.md for the modes, and use them before writing any new harness.
- A bot that kills bosses with `die()` finds crashes in death handlers (the shared Pylon cast to a Caves boss level). Damage bosses in phases first, then kill. Check every shared mob class for casts of `Dungeon.level` to one boss level class when a mod adds its own boss level.
- Special floors and mod levels built on depth 25 or above need clamps wherever a table is indexed by depth (`SecretRoom.secretsForFloor`, room pools).
- Do not compile or run gradle while a tour is running.

## Added after Ryuse's PD (2026-10-16)

- **A hero class is a long list of touchpoints.** Copy the Zealot or Summoner ones, `HeroClass` (enum, init, mastery badge, armor abilities, sprite, splash, unlock), `HeroSubClass`, `Badges` (mastery, unlock, boss slain, victory, the two class maps, the mastery switch), `Talent` (three tiers, subclass tier, the Heroic Energy icon case), `Icons.get(HeroClass)`, `ClassArmor`, `Assets` sprite and splash, strings in `actors.properties` (heroclass short desc, desc, unlock, subclass short desc and desc, ability strings, `shopkeeper.talk_prison_<class>`) and `misc.properties` (badge).
- **A mod with no talents for its hero** gets Shattered talents picked to suit the class, say so in PORTING.md.
- **Kotlin mods.** The tools cannot copy `.kt`. Read the effect in each class and write it in Java.
- **A new equipment slot** needs `Belongings` (field, accessor, bundle, clear, iterator, randomEquipped, uncurse), `WndBag` (the slot and the `equipped` count), and one static helper class that the rest of the game calls, so each hook is a single line. Generator needs a `Category` and its `classes` and `defaultProbs`. Put the items in the Catalog so the sandbox lists them.
- **Check where a copied file sits against its package line.** `port-item.py` made a `tier1` folder for a class whose package line said `melee`, and its strings were keyed to the wrong package. Move the file, then fix the strings.
- **If `check.sh` says 'Argument list too long'** the classpath outgrew one command line. It now uses argument files, do the same in any new script that builds a classpath from Gradle's cache.

## Added after Transformation PD (2026-10-16)

- **Compile with `scripts/check.sh --quick`, not the copier.** The copier goes through Gradle several times and takes over ten minutes. `check.sh --quick` compiles in 35 seconds, its errors are in `build/check.log`. Use the copier once for the copy, then fix by hand with check.sh.
- **Damage tags.** Transformation passes `DamageTag...` through every hit. `renames.py` rewrites its overrides of `damage`, `attackProc`, `defenseProc` and `attack` to this game's signatures, and `Char` has shims for calls. A mod with a different combat API needs the same kind of rules, write them first.
- **The copier pulls hard dependencies in.** After a copy, read `git status --short` and delete the ones you did not choose (Obscura came with Wild Dread, hero replicas with Twisted Mirror). Use `rm -f "${S:?}/${f:?}.java"`, a bare variable path is blocked by the safety check. Remove their roster and catalog lines too.
- **Monsters in a subpackage** (`actors/mobs/tmobs`) need an import in `ModRoster`.
- **Fixed stat mods.** Transformation's monsters have 100 to 250 HP at any depth, so register them deep (floor 11 and below).
- **Shims added for Transformation**, do not write again. See PORTING.md under Transformation PD.

## Added after Transformation run 3 (2026-10-16)

- **A mod system tied to its hero class** (Reason, the Friar) can be ported without the class. Gate the effects on the buff being present instead of on the subclass, stand in existing icons (`BuffIndicator`, `HeroIcon`, `SpellSprite`), turn floating text icons into plain `showStatus`, drop the talent hooks, and list every cut in PORTING.md. Do not add talent entries that no class can ever pick.
- **Bookkeeping a mod adds to `Item.upgrade`** (Extraction's `upgradeScrollUses`) stays additive. Only the scroll and the infusion write it, it is saved only when above 0, and a test (`ExtractionTest`) compares plain upgrades before and after.
- **Check `git worktree list` before starting** when a brief names a worktree. `git worktree add ../<name> -b <branch> <base>` makes it. A commit runs the post-commit hook, which starts the desktop update, so use `git -c core.hooksPath=/dev/null commit` when the brief says to leave the desktop app alone.
- **Python helpers must open mod source with `encoding='utf-8'`**, several Transformation files hold Chinese text.
## Added after Lovecraft PD (2026-10-16)

- **Very old mods** (0.5 and 0.6 era) keep tiles under `tiles/`, mobs under `mobs/` and the item sheet at `ui/items.png`. `port-itemsprites.py` finds all three now. Their mobs extend classes from other packages, so check each copied mob for missing imports of Rat, Thief, Scorpio.
- **Core systems a mod rewrote** (Lovecraft sanity and knowledge) get a small shim on Hero and Mob, not a port of their windows. Say what was left out in PORTING.md.
- **Monster spawning for a mod with few monsters** needs its own spawner (see LovecraftSpawner), because a mod with roster entries replaces the whole rotation otherwise. Give the roster lines weight 0.
- **New potions need potion labels.** `Potion.colors` holds 17 now. Make two more by hue shifting an existing sprite pair (regular and exotic, 16 cells apart) with PIL.
- **Floors that never shuffle** (1, 4, 9, 14, 19, 5k) cannot hold a mod room, add it from `WorldLevel.initRooms` to any floor of that mod instead.

## Added after Re-ARranged PD (2026-10-18)

- **A mod on Shattered 3.x copies best in one go.** Write a small copier like `scripts/rearranged-copy.py` (every class new to the mod, its own rename rules, a skip list for classes this game already has at the same path), copy all, then fix compile errors in rounds with `scripts/cyc.sh` and `python -I scripts/errsym.py`, which groups missing symbols by where they were looked up.
- **Hooks into Shattered's classes go in helper classes**, one line per hook in the shared file (see `RearrangedHero`, `RearrangedTalents`, `RearrangedChar`, `RearrangedWeapons`). Parallel ports then merge with few conflicts.
- **Diff the mod against its Shattered base** to find those hooks. Clone the base tag beside the mod sources (shallow, with long paths on) and diff file by file.
- **Icons on a separate sheet.** `TalentIcon`, `HeroIcon` and `BuffIcon` draw icon numbers of 10000 and up from `interfaces/rearranged/*`. Another mod can use the same trick with its own offset.
- **When Gradle is stuck** (an interrupted check leaves its daemon writing class files), run `FAST=1 scripts/check.sh`, it tests the plain javac build. `scripts/test-list.sh <Test> <list file>` runs one test on many classes.
- **Mods with two message bundles** (Re-ARranged reads `new_messages` before `messages`). Merge them into one bundle in the scratchpad and point `port-strings.py` at that folder.
- **Keep tool calls small.** One edit spec per few files, never one huge file in one call, and commit as soon as a batch compiles. Two stalls in this port came from long silent calls.
## Added after Summoning PD (2026-10-18)

- **When port-mob.py or port-item.py go quiet, copy by folder.** A mod system that lives in its own packages (Summoning's `actors/mobs/minions`, `items/weapon/melee/staffs`, `items/magic`) is faster to copy whole, then close with `FASTJAVAC=1 python -I scripts/port-copier.py . ~/mod-sources/<key> <key> 10`, which compiles with `fastjavac.sh` instead of Gradle. Then `python -I scripts/apply-renames.py <files>` on what you copied by hand.
- **Old signatures in bulk.** `python -I scripts/void-damage.py <files>` turns `public int damage(int, Object)` and `public void add(Buff)` overrides into this game's forms. `python -I scripts/drop-override.py build/fj.log` removes every @Override javac says overrides nothing (read its list, a hook that matters needs a shim instead).
- **Classes that share a simple name with ours in another package** (Summoning's `minions.Imp`, `minions.Mimic`, `minions.Slime`) can be copied by hand under their own package, the tools skip them but Java does not mind.
- **Message files.** `python -I scripts/check-props.py` lists lines whose trailing backslash swallows the next key (a monster then shows "has no name string"), `--fix` removes the backslash.
- **Git Bash rewrites arguments that look like paths.** A replacement holding `; //` became `/ported` and `/n`. Put `export MSYS_NO_PATHCONV=1` in front of any command that passes code as an argument, or write the edit as a spec file for `edit.py`.
- **The worktree guard** refuses lines with `$VAR` before sed or awk, `$(...)` command lists and python heredocs with regexes. Quote variables (`"$F"`), put python in a file in the scratchpad, and put long argument lists in a small wrapper script.
- **New scroll or potion.** A rune needs a free cell for the regular picture and the cell 16 below it for the exotic one, an entry in `Scroll.runes`, the class appended to the Generator deck with a weight in both prob arrays, and both `regToExo` and `exoToReg`.
- **Mod rooms with four size categories** (older Shattered) must drop the last weight, this game has three, or the level builder crashes on a null size.
## Added after the Transformation final run (2026-10-18)

- **A mod system scattered over many classes** (a talent catalog) ports fastest as one hooks class (TransformationTalents) with one line calls. Find every place first with scripts/talent-dossier.py, then write all hooks, then wire the calls with scripts/plib.py patch files.
- **Never put an abstract class on ModRoster**, and run MobArenaTest on boss monsters before an NPC may spawn them away from their arena.

## Added after the Tower PD finish (2026-10-22)

- **Item sheets wider than 16 cells.** `port-itemsprites.py` reads the mod's `WIDTH` from its ItemSpriteSheet (Tower PD uses 32). If a copied picture looks wrong or "EMPTY CELL" shows for an item the mod draws, check that constant first.
- **Hundreds of item pictures (2026-11-01, Shittened skins).** Do not grow `items.png`, other agents grow it in parallel. Copy the mod's sheet whole to `sprites/<key>/` and give its pictures numbers from a base past the end of items.png, as `SkinSprites` does (`BASE = 0x8000`, checked first in `ItemSprite.frame` and `pick`). `scripts/port-skins.py` shows how to make the constants and sizes from the mod's ItemSpriteSheet. Strip `//` comments before reading a mod's sheet, Shittened had commented out lines that reuse names like SCIMITAR.
- **A system the mod spread over many item classes** (an action, a picker, a name rule) goes in one helper class that `Item` calls, keyed on the item's exact class. Nothing in the 16 items had to change for the skins.
- **Text color markers.** Tower PD writes `#1text#1` for colored text. This game only reads `_text_`, convert them when copying strings.
- **A mod hero with no subclasses or talents** (Tower PD's BETA and ALPHA placeholders) gets Shattered subclasses, talents and armor abilities that work without that class's own gear, picked to suit. Run `TransformationHeroesTest <CLASS> ...` on it, the test now takes class names and casts each starting hero spell.

## Added after Polished PD (2026-10-22)

- **A mod that only retunes Shattered** (Polished) ports as an option, not as content. Put the changes behind one `Challenges` bit marked as an option (skip it in the random picker in `HeroSelectScene`), route each hook through one helper class (`PolishedPD`) and write a small headless test that checks the option off and on.
- **3.x mods add helpers to Shattered's classes** (`CharSprite.doAfterAnim`, a callback on `Transmuting`, `Buff.Polished.prolongAligned`). Grep the mod's copy of a Shattered class for the method before calling it from ported code, and write the small local version instead.
- **If the worktree vanishes**, make a new one from the base commit named in the brief with `git worktree add -b <branch> <path> <commit>` and carry on there.
## Added after Elektrocheckers PD (2026-10-22)

- **Parallel runs share the scratchpad.** Put edit specs and helper scripts in `scratchpad/<mod key>/`, not loose files with short names. Another agent overwrote `e1.txt` between my write and my run.
- **Recipes go in the list matching their ingredient count** in `Recipe.java` (one, two or three). A recipe in the wrong list compiles and is never found.
- **A mod rune or item effect that ends on a sprite effect** runs fine headless up to that line. Wrap casts in a test helper that reports a NullPointerException as info, then check the result (see `ElektroRunesTest`).
## Added after Sandbox PD (2026-10-22)

- **Mods that are mostly a tool** (a level editor, a debug console). When `mod-report.py` says an item "drags in windows and ui code", the copier will follow it into the whole tool. Add the tool's folders to `port-skip.txt` first (a line ending in `/` skips a folder), or copy the few classes by hand and fix their editor calls with small shims.
- **Rings, scrolls and potions outside their deck** keep the picture set in their initializer, because `reset()` runs in the parent constructor first. Override `isKnown()` to return true, and for a ring also `reset()` and `setKnown()`, or the handler throws on an unknown class.
- **Traps have a sandbox list now.** `WndSandboxTraps` lists `Bestiary.TRAP`. Add a ported trap there and it can be placed from the sandbox panel.
## Added after Community PD (2026-10-22)

- **A mod with no worlds whose monsters fit any floor** (Community's level feeling monsters) gets a small spawner that edits the rotation from `Level.createMob` (see `CommunitySpawner`), with its roster lines at weight 0.
- **A mod that keeps a variant inside a Shattered class** (Community's `Elemental.PoisonElemental`) becomes its own class with a prefix, and a sprite subclass can swap only the texture when the mod's sheet is the same size as ours.
- **Check borrowed sheets against frame numbers.** Community's guard sheet is wider than ours, so the Failed Defender uses the mod's copy under `sprites/community/`.
## Added after Blue Archive PD (2026-10-22)

- **Check the mod's own build.gradle for its base**, the survey can be wrong (Blue Archive said 2.1.4, its build is 2.3.2). Clone that Shattered tag to the scratchpad and diff against it.
- **Mods that rewrite all of Shattered's heroes** (Blue Archive) port as new classes beside the old ones. Prefix every class, talent, subclass, icon and asset constant, put the hooks in a few helper classes, and keep the one line calls in Shattered's files in one rerunnable script (`scripts/bluearchive-hooks.py`). Shattered's `case WARRIOR: default:` switches give new classes the Warrior's talents and items, clear the list in the hook first.
- **Korean or Chinese only text.** Some mods only filled their `_ko` or `_zh` bundles. Extract the needed lines under this game's keys (`scripts/bluearchive-strings.py extract`), split them in chunks, translate them with parallel agents that get Onyx's writing rules, then `append`.
- **Copier rules must be idempotent.** A rule whose output still matches its own pattern grows on every `--refix`.

## Added after Re Blue Archive PD (2026-10-22)

- **A mod whose class names clash with ours or with a parallel port** ports fastest with a prefix copier, see `scripts/rebluearchive-copy.py` (every new class gets the prefix, imports, fully qualified names and enum entries are renamed, the mod's added members of Shattered classes go to shim calls by rule). Write hook code in the mod's own names and run `--fix` on it.
- **Find the hooks with `rebluearchive-hunks.py`** (copy it and point it at the new mod), it shows only the changed hunks that name something the mod added, a few hundred lines instead of thousands.
- **Icon offsets in use.** Re-ARranged 10000, Blue Archive 30000, Re Blue Archive 50000. Check the highest offset first in `TalentIcon`, `BuffIcon` and `HeroIcon.pick`.
- **Merging main after a parallel port** that appended to the same enum or list, take main's file and reapply your own small edits, keep both sides only for plain appended lines.
## Added after DERanged PD (2026-10-22)

- **A hero that counts as other classes** (DERanged's Rat King) needs no edits in each effect. `HeroClass.is` and `HeroSubClass.is` answer for him, and `python -I scripts/deranged-loosen.py` rewrites `x.heroClass == HeroClass.Y` and `x.subClass == HeroSubClass.Y` into `is()` calls in game logic folders. Extend its lists if another hero shares classes.
- **Talents that stand for other talents** go in one table with one hook in `Hero.pointsInTalent` (see `DerangedKing.BORROWED`), built from the mod's two argument `hasTalent` and `pointsInTalent` calls with a small script.
- **Python helpers on Windows files.** Normalize CRLF before multi line replaces, or use `edit.py` specs, which handle it.
## Added after Xun's PD (2026-10-23)
- Before writing a new helper class with the Write tool, check the file does not exist yet (`ls`). Write replaces a file without a word, and a later round's `XunTalents` replaced round 1's version this way. Look for a mod's earlier helpers with `grep -rl "//xun"` first.
- A mod hero whose icons sit on its own sheets needs no sheet edits. Copy its hero, buff and talent sheets to `interfaces/<key>/` and draw icon numbers of 40000 and up from them in `HeroIcon`, `BuffIcon` and `TalentIcon` (the Re-ARranged sheets use 10000 the same way). Pick a new base for the next mod.
- A mod terrain tile (Xun's underpass) does not port, every world sheet would need the tile. Keep the cells in a small `Blob` subclass whose `evolve` copies `cur` to `off`, and show the cell as an existing terrain.
- `TransformationHeroesTest` takes hero class names and checks text, talents, subclasses and save and load. Heroes with a different shape (three subclasses, five talents) need their shape added to the test, as was done for Xun's.
- Python heredocs inside Bash turn a backslash n inside a Java string into a real line break. Write Python edit scripts to the scratchpad with the Write tool and run them from there.
## Added after Magic Ling batch 2 (2026-10-23)
- **Before a big copy, list each monster's missing imports** (a short loop over its import lines, checking which classes we lack). A monster whose list names a boss, a boss level or a window will drag the whole system in. Drop it from the batch or put that class in `port-skip.txt` first, then fix the few broken lines by hand.
- **If the copied files still show the mod's names** (`Statistics.bossRushMode` and the like) after a port, the rename rows did not run. Check which list in `renames.py` holds them. Only the plain `RENAMES` list runs on every mod.
- **Mod sprites that name a picture by a plain path** (`texture("Boss/SLMKING0.png")`) are not covered by the Assets check. `check-sprites.py` lists them now, copy each picture to `sprites/<key>/` and change the path.
## Added after Magic Ling batch 4, bosses (2026-10-24)
- **Magic Ling bosses extend `actors/Boss`**, which is in core now with `GameScene.bossReady` and `Level.playBossMusic`. Port a boss with `port-mob.py ... --weight=0`, then copy its level by hand and run `apply-renames.py` on it. A short list of each boss's missing imports first (as in batch 2) shows which ones drag in windows.
- **Read every mod boss level for three traps.** A `Random.popGenerator()` with no push before it (crashes the build), an exit transition in cell 0 or only added after the fight (the hero lands in a wall coming back up), and casts of `Dungeon.level` to the boss's own level in the boss's `die` (crashes in the sandbox anywhere else).
- **`BOSS_ONLY=A,B,C scripts/check.sh --new`** runs the boss test on several arenas in one go.
- **A boss test warning "never started a fight by walking"** can come from a door check, the test moves the hero without closing doors behind it. Accept `OPEN_DOOR` in the level's trigger.

## Added after Magic Ling batch 5 (2026-10-24)
- **A full compile takes about 12 minutes now and `check.sh --new` about an hour.** For a boss batch run `BOSS_ONLY=A,B FAST=1 scripts/check.sh --quick Boss1 Boss2` once, then rerun single tests without compiling with `scripts/test-one.sh BossArenaTest A,B`, `test-one.sh WorldShuffleTest 30` or `test-one.sh MobArenaTest Boss1`. Never start a second check while one runs, they share `build/check.log` and `build/fast`.
- **Guard every cast of the floor to a boss's own arena** in one go with a small regex script (state reads fall back to null or a won state, statement calls get `if (Dungeon.level instanceof X)`, position reads fall back to -1). MobArenaTest spawns bosses on an ordinary floor and fails on each unguarded cast.
- **A boss arena whose exit sits in cell 0 until the fight ends** fails WorldShuffleTest. Use the cell the mod opens as stairs later, sealed in the walls until then.

## Added after Magic Ling batch 3 (2026-10-24)
- **mod-report's "easy monster" list can hold items.** Magic Ling keeps weapons and books under `actors/mobs/bosses/galaxy`. Check that each class extends a Mob before `port-mob.py`, and that the class is not commented out (Nxhy Guard was).
- **If port-item.py leaves many "cannot find symbol ItemSpriteSheet.X" errors,** the picture step failed quietly. Run `port-itemsprites.py . ~/mod-sources/<key>/ NAMES...` by hand and read its error.

## Added after the Magic Ling finish (2026-10-24)
- **mod-report misses monsters.** It skips a class when any class of that name exists here (Magic Ling's Fire Dragon, its Golden City gnolls). Run `python -I scripts/missing-mobs.py <key>` at the end of a mod, it walks the class tree and lists missing monsters and name clashes. A clash in another package can be copied under its own package with the same name (Java keeps them apart), or with `port-prefix.py`.
- **Plan with `python -I scripts/missing-imports.py <key> Class... --depth=2`.** It shows what each class drags in, nested. A window, a hub NPC or a whole minigame deep in the list means add it to `port-skip.txt` first.
- **`scripts/fastjavac.sh` compiles everything in about 90 seconds now.** Use it between hand fixes, then `python -I scripts/errsym.py build/fj.log` groups the errors. Do not start it while a `check.sh` run is in its test stage.
- **New rename rows go through `python -I scripts/add-renames.py rows.txt [--before=...]`**, rows written with the Write tool. It skips rows already there and undoes the change if renames.py stops importing. Shell heredocs broke backslashes in rows three times this run. Order matters, a row that removes an assignment must come before the row that turns the same name into `false`.
- **`python -I scripts/guard-casts.py <Arena> files...`** guards every cast of the floor to a boss's own arena in one go.
- **Untranslated text.** `python -I scripts/find-untranslated.py --out=f.txt` lists English lines still in Chinese, split the file and give each part to a translator agent with Onyx's writing rules, check keys and placeholders, then `--apply=translated.txt`. Magic Ling's English file left 840 lines in Chinese.
- **A mod's deep floors (past 26) and side branches become special floors.** Replace each jump to them (`InterlevelScene.curTransition.destDepth = 33`) with `SpecialFloors.travel(...)` or `SpecialFloors.leave()`. Give special floor labels the word "boss" so BossArenaTest plays them, and pass one word per name in `BOSS_ONLY` (it splits on spaces).
- **Static imports of constants break renames.** A row that turns `Window.X_COLOR` into a number also turns `import static ...Window.X_COLOR;` into broken code. For named constants add them to our class instead of renaming.

## Added after the Magic Ling town (2026-10-24)
- **A mod's missing counters and settings** (`Statistics.X`, `SPDSettings.X()`) go in with `python -I scripts/stats-shim.py build/fj.log` after a compile. It copies each declaration from the mod into `MagicLingStats` (point it at another class for another mod), rewrites the references and adds rename rows. It took 200 errors to 50 in one step.
- **When the copier pulls in more than 150 files, undo and block first.** Delete the untracked files, `git checkout` Assets.java, trace who drags each system in from the `missing-imports.py --depth=5` tree, add those to `port-skip.txt`, then copy again.
- **See one special floor with the tour.** `JAVA_TOOL_OPTIONS="-Donyx.gallery.only=allbosses -Donyx.gallery.label=<text>" scripts/gallery.sh "Onyx Gallery"`, it compiles with Gradle first. The pictures land in a folder of that name in the repo, delete it afterwards.

## Added after Too Cruel PD Trials (2026-10-25)

- A Kotlin mod cannot go through the copier. Read its hook files first (`hooks/*.kt`), list every place the mod's Java calls them, and give each its own one line call into a single helper class. All 71 shared edits then fit one `edit.py` spec.
- Anything that runs inside `Level.create` must only use the level it is handed. `Level.getTransition(int)`, `Dungeon.level` and `Dungeon.hero.pos` still belong to the previous floor there.
- Water and grass fill rates near 1 never finish painting (`Patch.generate` loops). Cap any multiplier on them.
- A headless test for modes should call `TestBoot.stubWindow()`, give the hero a large health pool, spawn Shattered's own monsters (`MobSpawner.getMobRotation`) and count sprite or game screen crashes as HEADLESS. Give it a watchdog thread so a hang prints where it is stuck.

## Added after the game modes (2026-10-25)
- **Mods that only change how a run plays** (shorter dungeons, starting kits, stat sliders, challenge packs) go in as game modes. `GameModes.register(id, mod, maxLevel, group)` adds one, `GameModes.on(id)` checks it, the Game modes window lists it with text from `gamemodes.<id>` and `gamemodes.<id>_desc` in misc.properties. A changed floor order goes through `GameModes.nextDepth` and `prevDepth`, never `depth + 1`.
- **Static blocks must not call into another new class** that has its own static state. Two classes that read each other while loading throw ExceptionInInitializerError on first use, which crashes every new run.
- **Upgrading a thrown weapon resets its stack size.** Call `upgrade(n)` before `quantity(n)`.
- **`Dungeon.init` resets the random generators.** Code that builds runs in a loop (the Radar search) must use its own `java.util.Random`, not push and pop the game's.

## Added after the six small mods (2026-10-29)
- **Check the worktree's base first.** Compare `git log --oneline -1` with main. A worktree made from an old commit has none of the tools, and `git merge --ff-only main` brings it up to date.
- **Diff a single import mod against its Shattered base** with `python -I scripts/moddiff.py <mod> <shattered tag clone> build/diff-<key>.txt <mod package>`. It renames the package back, ignores whitespace, and lists new classes, changed classes by size and new assets. Shattered's 0.7 tags are v0.7.3b and v0.7.4d (there is no plain v0.7.4), check with `git ls-remote --tags`.
- **Copy every new class at once** with `python -I scripts/copyall.py build/diff-<key>.txt <mod java root> <mod package> [names to skip]`. It skips names this game already has. Then `python -I scripts/newfiles.py --renames --sub OLD NEW ...` runs the renames and regex fixes on the untracked files only.
- **Old mods with flat asset names** (`Assets.TILES_TOWN = "tiles_town.png"`) go in with `python -I scripts/flatassets.py <key> <mod assets dir> NAME...`, which copies each file to `assets/<key>/` and puts the path into the copied code. Android era mods keep assets in `android/src/main/assets`, which `port-itemsprites.py` now reads too.
- **A mod whose content only makes sense together** (Trap Dungeon's traps and monster rotation, a speedrun timer) goes in as a game mode with one helper class, and its monsters, items and traps also go in the sandbox lists and the catalog. A hero class mini game (Lore's Goo) can be a mode that turns any hero into it, which needs no badge, splash or class armor work.
- **Reskinned boss arenas from 0.7 mods** (Re-Remixed's) are easiest as a subclass of the matching Abyssalnite `Old*BossLevel` with only `tilesTex` and `waterTex` changed.
## Added after Music Implant SPD (2026-10-29)
- **Mods uploaded as one commit.** `python -I scripts/mod-diff.py <mod root> <shattered tag root> <mod package path> <out.txt>` lists new and changed files against a Shattered tag (clone it shallow, try two nearby tags and keep the one with fewer diff lines) and writes the unified diffs to `<out.txt>.diff`. Split the diff per file with awk to read one class at a time.
- **`scripts/test-recompile.sh <Test> [args]`** recompiles one test class with TestBoot and runs it, `test-one.sh` alone runs the old class file.
- **A whole run rework** (light, horror, satiation, perks) goes in as many small game modes from one helper class, with a conflict table in `onSet`, like Too Cruel and `MusicImplant`.
