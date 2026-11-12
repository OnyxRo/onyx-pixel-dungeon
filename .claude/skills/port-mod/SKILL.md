---
name: port-mod
description: Port monsters, weapons or items from another Pixel Dungeon mod into Onyx's Pixel Dungeon. Use for any task in TASKS.md that starts with "Port", and whenever you add content from a mod. Covers the tools, the order to use them in, how to split a port into batches, and where the lessons from earlier ports are kept.
---

# Porting from a mod

This file is the workflow. Everything earlier ports learned (compile errors and their fixes, bosses, hero classes, sheets, Kotlin mods, worktree trouble) is in `lessons.md` beside it. Do not read lessons.md whole. When you hit a problem, search it first:

```
grep -n -i "<word>" .claude/skills/port-mod/lessons.md
```

then read only the matching section with `sed -n <start>,<end>p`. Its top lists every section and good words to search.

## Step zero, fix open crashes

Before porting anything, run `python -I scripts/crashes.py`. If it lists any open crash, invoke the `fix-bugs` skill and fix them first, then start the port. Fold the fixes into the port's check and commit (or commit them on their own if they touch unrelated code). Tell Onyx at the end which crashes were fixed. If the list is empty, say nothing and go on.

## Batches and the context budget (read this first, 2026-11-12)

Every turn re-reads the whole conversation, so a long run gets more expensive with each step. In October, agents that ported a whole mod in one go ran 300 to 500 turns, grew to 600k to 900k tokens of context, and used about half of Onyx's month of usage. Doing the same work in fresh batches costs about half as much. These rules are not optional.

**When you hand out the work** (the session that plans a port and starts agents):

- Never give one agent a whole mod. Run `mod-report.py --all` yourself, then split the work into batches of about 10 to 15 classes (or one system, like a hero class or one boss with its level). Give each batch its own agent.
- Each brief names the mod key, the worktree and base commit, the exact classes in the batch, and the path of the handoff file to write. Do not paste PORTING.md, TASKS.md or this skill into the brief. The agent reads what it needs.
- Run batches of different mods in parallel if you like, but batches of the same mod one after another, each starting from the previous batch's commit and handoff.
- Keep your own context small while agents run. Read each agent's handoff summary, not its files.

**When you are the agent doing a batch:**

- Do only your batch. When it compiles and passes `check.sh --new`, commit, write the handoff and stop. Do not start on the next batch, even if it looks small.
- Stop at about 150 tool calls even if the batch is not done. Commit what compiles, write the handoff with exactly what is left, and return. A fresh agent picks it up cheaper than you can carry on.
- The handoff is `build/<mod key>/handoff.md`, at most 40 lines. It says what was done (classes, commit), what is left, compile errors still open and their likely fix, anything that surprised you. Write it so the next agent never needs your transcript.

**Reading cheaply, for everyone:**

- Find before you read. `grep -n` for the symbol, then `sed -n <a>,<b>p` on the lines you need (keep it under 150 lines). Do not `cat` a whole mod class, a properties file, PORTING.md, TASKS.md or a log.
- Send every build, test and copier run to a file and read only the end or the summary (`tail -30`, the ERRORS BY TYPE block, `errsym.py`). Never print a full compiler run.
- Look at a picture (`arena.png`, a sprite sheet) once per batch at most. Each picture you read stays in context for the rest of the run.
- Do not re-read a file you already read or wrote in this run unless it changed.
- Use the tools that already exist (`mod-report.py`, `missing-imports.py`, `errsym.py`, `moddiff.py`, `rebluearchive-hunks.py`) instead of reading source to work out the same thing by hand.

## The short version

```
python -I scripts/crashes.py                     open crashes first, fix them with the fix-bugs skill
scripts/get-mod.sh <key>                         clone the mod (scripts/get-mod.sh --list shows keys)
python -I scripts/mod-report.py ~/mod-sources/<key> --all     what is portable, what is hard
python -I scripts/port-mob.py <key> <roster key> Mob1 Mob2 ...   port the easy ones in one command
python -I scripts/port-item.py <key> Item1 Item2 ...           same for weapons and items
scripts/fastjavac.sh, then python -I scripts/errsym.py build/fj.log   compile in about 90 seconds and group the errors
scripts/check.sh --new                           headless tests on what you changed
scripts/arena.sh                                 one picture of everything you changed
```

Then update TASKS.md and PORTING.md, commit. Do not do anything else to check a monster.

## Rules that save time

- **Never use scripts/gallery.sh to check a port.** It spawns every ported monster one at a time, about ten minutes, and opens the game over and over. Use `scripts/check.sh` first and `scripts/arena.sh` once, at the end, for the whole batch. Never launch the game for a single monster. Never run the arena twice for the same batch unless you changed code because of what it showed.
- **Read the mod report before choosing what to port.** It already says which monsters are easy, which need a boss level, which use members the mod added to Shattered's own classes (like `Dungeon.cycle`), and which have no text. Do not open mod source files one by one to find that out.
- **Port a batch, not a monster.** Pass every easy monster to `port-mob.py` at once.
- **Compile with `scripts/fastjavac.sh` or `scripts/check.sh --quick`, never the copier or Gradle between fixes.** A full Gradle compile takes about 12 minutes and `check.sh --new` about an hour on a big batch. Run a single test again without compiling with `scripts/test-one.sh <Test> <args>`. Never start a second check while one runs, they share `build/check.log` and `build/fast`.
- **Edit with `scripts/edit.py`, never heredocs or sed.** Write a plain text spec with the Write tool (`@@ file`, then `<<<<` old `====` new `>>>>`), run `python -I scripts/edit.py spec.txt`. It keeps line endings and needs no backslash escaping. Write Python helpers as files with the Write tool, never as Bash heredocs (backslashes and `\n` get mangled). Never `sed -i` a message file, several are CRLF.
- **Before a big copy, check what it drags in** with `python -I scripts/missing-imports.py <key> Class... --depth=2`. A window, a hub NPC or a whole minigame in the list means add it to `scripts/port-skip.txt` first.
- **Do not `git checkout` the shared message files** (`messages/actors/actors.properties`, `items/items.properties`). They hold every earlier port's strings that are not committed yet. Fix a bad line by editing it.
- **Check the worktree's base first.** Compare `git log --oneline -1` with main. A worktree made from an old commit has none of the tools, and `git merge --ff-only main` brings it up to date. Keep helper scripts and edit specs in `build/<mod key>/` inside the worktree, never loose in a shared scratchpad.
- **Commit after each batch that compiles.** Worktrees have vanished mid run, and uncommitted work went with them.
- **If the auto mode check denies a command**, retry it once. If it keeps denying, stop and say what you were trying to do. Do not work around it.
- Run Python as `python -I`.

## What each tool does

| Tool | Use |
| --- | --- |
| `scripts/mods.txt`, `scripts/get-mod.sh` | Known mods, their repos, and cloning to `~/mod-sources/<key>` |
| `scripts/mod-report.py <folder> [--all]` | Lists what the mod adds that we lack, with a tier per class (easy, shim, medium, hard), the reason for hard, whether there is a sprite and a name string, and a ready `port-item.py` command for the easy items. Writes `build/mod-report-<mod>.md` |
| `scripts/port-mob.py <key> <roster key> <Mobs...>` | Copies the monsters and their sprites, closes dependencies (`port-copier.py`), copies strings (`port-strings.py`), writes placeholder text for monsters the mod never described, applies known API fixes, registers in `ModRoster`, runs the headless check, prints what is left |
| `scripts/port-item.py <key> <Items...>` | The same for weapons, armor, rings, wands, artifacts, potions, scrolls, enchantments and glyphs, and adds each to the journal Catalog. It does not add loot table entries |
| `scripts/renames.py`, `scripts/add-renames.py rows.txt` | The one table of fixes for old code, run by every copier. Add a row when you fix the same compile error twice |
| `scripts/check.sh [--new] [--quick] [--seeds N] [Mob...]` | Compiles, runs `WorldShuffleTest`, `MobArenaTest`, `ItemArenaTest` and the boss test. Full output in `build/check.log` |
| `scripts/test/mob-baseline.txt` | Problems that were there before the test existed, mostly things a headless run cannot do. `check.sh --write-baseline` regenerates it. Only do that after reading the new lines, a new entry may be a bug in our code |
| `scripts/arena.sh [Mobs...\|--roster]` | Puts the chosen monsters (default, the ones you changed) on one floor in the game itself, lets them act, saves `build/arena/arena.png`. Read the picture with the image reader |
| `actors/mobs/ModRoster.java` | The one place a monster is registered. A line there makes it show in the bestiary and the sandbox list and be covered by the tests. Its weight and depths no longer decide spawning (2026-11-01) |
| `scripts/mod-spawns.py` | Reads each mod's own spawn table and writes `actors/mobs/ModSpawns.java`, which decides what a mod's world spawns on each floor. Add a new mod to its `MODS` list and rerun with `--write` |
| `scripts/missing-mobs.py <key>` | At the end of a mod, lists monsters mod-report missed and name clashes |

`ModRoster` keys are the mod's icon file name without the extension (`interfaces/mods/reforged.png` is `reforged`). `WorldShuffle.modKey(level)` gives the key for a floor. Other tools (prefix copiers, boss guards, string tools, diff tools) are described in lessons.md, search it by name.

## Order of work

1. Find the task in TASKS.md (`grep -n -i <mod> TASKS.md PORTING.md`, then read only those lines).
2. `scripts/get-mod.sh <key>`. If the key is not in `mods.txt`, find the repo with a web search and add a line.
3. `mod-report.py --all`. Decide the set and split it into batches (see Batches above). Easy classes go first, in one command. Leave bosses, classes that need mod only core members, and the hard tier for later batches or a follow-up task.
4. `port-mob.py` or `port-item.py`. Fix what is left by hand with `fastjavac.sh` and `errsym.py`. Search lessons.md for each kind of error before working it out yourself. When you fix the same kind of error twice, add a rename row.
5. `scripts/check.sh --new`. A failure with a stack trace in the class's own code is a bug in our code. Fix it. A failure inside `CharSprite`, `Emitter` or `GameScene` is the headless run's limit, add it to the baseline.
6. `scripts/arena.sh`. Read the picture once. Look for missing sprites, wrong frames, monsters standing still.
7. Update TASKS.md (check it off, or leave it unchecked and list what is left under it) and PORTING.md, with edit specs, not by rewriting the files. Add the mod's monsters to the "Done per mod" line.
8. Commit, one commit per finished batch. Write the handoff if work is left.

## Definition of done

`scripts/check.sh` passes (full run, not `--quick`), the arena picture looks right, TASKS.md and PORTING.md are current, and one commit holds it. If the full check has a failure you did not cause, say so. A batch agent runs `check.sh --new` only. The full run happens once, when the last batch of a mod lands.

## The improvement loop (do this every task, it is part of the task)

1. While working, keep a running note of every step that cost more than a couple of minutes or went wrong twice.
2. Before committing, turn each into a tool change (a rule in `renames.py`, a line in `port-skip.txt`, a check, a script option). A note alone does not count.
3. If it still needs words, add a short entry to the matching section of `lessons.md` (or a new section at the end, with the date). Do not add it to this file. This file stays a short workflow, and only changes when the workflow itself changes.
4. Add a dated entry to `RETRO.md` saying what cost time and which change removes it. Keep it to a few lines.
5. Rule of thumb for the next run. If the same fix was done by hand twice, the third time it goes into a tool first.
