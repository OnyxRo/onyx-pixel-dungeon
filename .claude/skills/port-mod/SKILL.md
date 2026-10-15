---
name: port-mod
description: Port monsters, weapons or items from another Pixel Dungeon mod into Onyx's Pixel Dungeon. Use for any task in TASKS.md that starts with "Port", and whenever you add content from a mod. Covers the tools, the order to use them in, and the mistakes earlier sessions made.
---

# Porting from a mod

Everything here exists because porting Reforged cost about 40 minutes, most of it waste. Follow the order below and a batch of monsters takes a few minutes.

## The short version

```
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
| `scripts/reforged-stats.py <folder> <Mobs...>` | For mods that keep stats in a table (Reforged). Prints the fields to paste into a hand written monster |
| `scripts/check.sh [--new] [--quick] [--seeds N] [Mob...]` | Compiles, runs `WorldShuffleTest` and `MobArenaTest`. Full output in `build/check.log` |
| `scripts/test/MobArenaTest.java` | The testing ground. Per monster, with no window: name and description strings, save and reload, loot, damage and accuracy rolls, 25 rounds of combat against an unkillable hero, 60 turns of its own AI, death, plus a check that sprites do not cast their character to the wrong class, and that every file named in `Assets` exists |
| `scripts/test/mob-baseline.txt` | Problems that were there before the test existed, mostly things a headless run cannot do. `check.sh --write-baseline` regenerates it. Only do that after reading the new lines, a new entry may be a bug in our code |
| `scripts/arena.sh [Mobs...\|--roster]` | Puts the chosen monsters (default, the ones you changed) on one floor in the game itself, lets them act, saves `build/arena/arena.png`. Read the picture with the image reader |
| `actors/mobs/ModRoster.java` | The one place a monster is registered. A line there makes it spawn in that mod's worlds (by depth), show in the bestiary and the sandbox list, and be covered by the tests. Devoted and Matt Edition keep their own spawners |

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
- **Weapons and items.** `port-mob.py` is for monsters. For weapons use `port-copier.py` and `port-itemsprites.py` as before (CLAUDE.md), list them in the journal catalog, and write a check for them into `MobArenaTest` if the work repeats.
- **Roster depths.** `port-mob.py` registers with weight 1 and depths 1 to 99. Set `--weight`, `--min` and `--max` from the mod's own spawner or the monster's `maxLvl`, or edit the line in `ModRoster.java` afterwards.

## Definition of done

`scripts/check.sh` passes (full run, not `--quick`), the arena picture looks right, TASKS.md and PORTING.md are current, and one commit holds it. If the full check has a failure you did not cause, say so.
