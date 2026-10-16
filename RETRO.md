# Retrospective log

One short entry per task or session. What cost the time, and the change that removes it. Newest last. The rule is in `.claude/skills/port-mod/SKILL.md` under the improvement loop.

## 2026-10-15, finishing Cursed PD, Abyssalnite and Experienced

Ported about 90 items, weapons and levels, two branches (arena, black mimic) and the Fate Lock. What cost the most time and the fix.

- Hand fixing compile errors that came from the mod's own systems (mana, perks, cycles, grinding, branches). Fix, write one shim per mod up front and list the shims in the skill, so the next port starts with them. The mod report now flags mod added members for items too.
- Shell and regex edits that lost backslashes, four times. Fix, `scripts/edit.py`, and a rule in CLAUDE.md.
- `port-copier.py` copying deleted classes back. Fix, `scripts/port-skip.txt`.
- The item picture sheet filling up in the middle of a batch. Fix, it now grows by itself.
- Headless crashes (null emitter, sprite, field of view) showing as failures and needing baseline edits one by one. Fix, they are counted apart and no longer fail the run.
- Not knowing in advance what was easy. Fix, `mod-report.py` triages every class and prints the batch to run first.
- Long silent stretches. Fix, give a one line status before every long tool run.
- Still open. Checking each ported class by eye is not possible headless, `arena.sh` only covers monsters. Items rely on `ItemArenaTest` alone.
- The full `scripts/check.sh` only tested items changed since the last commit, so a pull request check on a committed batch tested nothing. Fix, a full run now tests every catalog item (477, about 30 seconds). Six known failures from Devoted and Shattered's darts were added to the baseline.
- I broke my own new test with a backslash in a heredoc edit, minutes after writing the rule against it. Fix, none needed beyond the rule, but patterns in new code should avoid backslashes entirely, for example `[.]` instead of an escaped dot.

## 2026-10-15, Abyssalnite legacy bosses and a boss fight test

Ported OldTengu, OldDM300, King and Yog with their four sealed arena levels. Boss levels from Abyssalnite are only reachable in the sandbox until Onyx has played them (`WorldShuffle.bossPlaytested`).

- Boss levels were the first thing that could trap a run, so they got a real test. `scripts/test/BossArenaTest.java` walks the hero into every mod boss arena, kills each boss and checks the floor unseals and that floors 10 and 20 give the mask or crown. It runs in every full `check.sh`, `BOSS_ONLY=Name` runs one level. It has a 60 second watchdog that prints where a hang is.
- The test found a real bug in the first minutes, not a test artifact. Sandbox full sight makes every cell visible, so any "put the boss where the hero cannot see" loop never ends and freezes the game. Fix, a rule in `renames.py` that makes those loops ignore sight when full sight is on.
- Headless limits (null emitter, scene, sprite) hid the interesting part of every boss death. Fix, `GameScene.emitter()` returns a loose emitter without a scene, `Char.die`, `GameScene.bossSlain` and `dropProgressionItem` skip the sprite when there is none. A death that still stops at a limit is a WARN, not a pass, and not a failure.
- A walk from entrance to exit is not a valid check for boss floors, Shattered's own arenas fail it. Do not add it back.
- Legacy levels set `entrance` and `exit` directly and swap maps. `Level.create` now turns those into transitions and `Level.changeMap` exists, so old style levels port with few edits. A Skeleton Key becomes a Worn Key, which opens locked exits in 4.0.2.
- Trial copies that turn out wrong (Reforged's combat package came along with the Wyrm) were removed by hand from `git status`. Commit before a trial, then undo by deleting what `git status --short` lists as new.

## 2026-10-15, Cursed PD bosses and floors, Reforged Wyrm, special floors

- Mods reuse Shattered's class names for their own versions (CavesBossLevel, King, Yog, HeroicLeap), and every tool skipped them silently, so a whole set of content never appeared in any report. Fix, `scripts/port-prefix.py` copies them as `<Prefix>Name`, with references and text, and its first use ported 16 classes.
- Old style levels cost the most in one place. Fix, shims in `Level` (legacy entrance and exit, `changeMap`) and ten rename rules, so each later old style level needs only a few hand edits.
- Content that is not a normal floor (hub, challenge floors, final arena) had nowhere to live. Fix, `SpecialFloors` and a sandbox list, so any future mod can add such floors with one line.
- `check.sh` printed one test's WARN lines under the next test. Fix, each test's output is read from its own part of the log.
- A false "no errors" came from reading an old log after an edit step failed inside a `&&` chain. Fix, noted in the skill, run steps separately when the next step reads a log.
- `edit.py` stops on a whitespace difference in blank lines. Fix, it now tolerates trailing whitespace when the block is unique. Still best to use short anchors.
- Reforged's Construct Hero depends on a hero talent tree and its Half Ripper on quest hooks. A port tool cannot tell this from a class list. Open improvement, make `mod-report.py` flag classes that mention `Talent.<MOD_ONLY>` or a mod only Quest.

## 2026-10-15 afternoon, item picture fix, Matt Edition branches, Experienced perks, Dream Dart

- The item sheet grew to 704 pixels but `ItemSpriteSheet.TX_HEIGHT` stayed 512, so every icon below the top row was cut from the wrong place and Onyx found it by eye. Fix, `port-itemsprites.py` now rewrites `TX_HEIGHT` itself when it grows the sheet.
- The OOF Thief borrowed the normal thief sheet and asked for frames it does not have, so the game threw when it was made. No headless test could see it. It was found only because the gallery tour pictured the sandbox windows. Fix, `scripts/check-sprites.py` reads each sprite class and PNG header and runs in every `check.sh`, so this class of mistake now fails before a window opens.
- The auto desktop update hook rewrites the 700 MB jar after every commit, and `arena.sh` and `gallery.sh` fail with a missing class if they start in that window. Fix, noted in the skill, wait for the jar size to stop changing.
- A mod class that shares a simple name with one of ours but sits in another package (Matt's `levels/IceCavesLevel` against Combusted's `levels/worlds/IceCavesLevel`) was skipped by `port-copier.py` with no message, so the compile just said the class was missing. Fix, noted in the skill, use `port-prefix.py` for it.
- `renames.py` had two rows that stripped every Experienced perk check from copied files. That was right while perks were not ported and wrong once they were. Fix, the rows are gone and the file says why. Rows that delete code need a note saying what must happen before they are removed.
- Matt Edition's levels needed a named transition system (`transitionId`), item count overrides and room depth overrides in core. Each was a small shim written once, and `BranchLevelTest` now builds both branches from twelve seeds and checks every transition has a way back, which found three real problems (a missing boss entrance id, a test that rebuilt a floor it had changed, a thief door that only exists after the quest dialog).
- Shell heredoc and `sed 'a\'` with `$` variables left a literal `$L` in five files. Fix, use the Edit tool or a `sed s///` with the text spelled out.

- 2026-10-15 evening, Devoted and Reforged leftovers. Copying one class and running `port-copier.py` closed most of the dependencies in two rounds, which was fast. Cost time, the Edit tool wants a Read of every file first, `scripts/edit.py` with a spec file avoided that. Change made, none needed, the skill already says to use it. Cost time, a Half Ripper style quest needs hooks in many places and the mod put them in level classes that shuffled worlds do not share. Change made, one `spawn(level)` call from `Dungeon.switchLevel` covers every world and boss floor. `DevotedLeftoversTest` now covers all of it.

- 2026-10-15 night, hero classes. Cost time, Reforged and Devoted both put their hero talent icons in cells 192 to 217, which surfaced only after the Zealot was in. Change made, `scripts/check-sprites.py` is untouched but the icon moves are written down in CLAUDE.md, check cell clashes with Python before pasting a second mod's icons. Cost time, reading each mod's talent hooks one by one. Change made, `grep` every talent name across the mod once and write one spec file for all hooks, which cut the Artificer to one pass. The two bots (`-Donyx.gallery.class`, `-Donyx.gallery.subclass`) now make a hero class cheap to smoke test.

- 2026-10-15 night, Shittened. Cost time, `renames.py` rewrote `com.badlogic.gdx.utils.Timer` into our own utils package, and the tool said zero errors while `check.sh` showed more, because the copier compiles through Gradle and `check.sh` compiles with javac. Change made, the rewrite rule skips badlogic and watabou, and read `check.log` before trusting a copier summary. Cost time, `check-sprites.py` missed sprites that use `int c = 63; frames(c+0, ...)`, the arena crashed instead. Change made, the checker fills in simple local numbers. Cost time, sprites that borrow a shared sheet the mod had extended. Change made, copy the mod's sheet to `sprites/shittened/` with a `SHIT_` constant, there is a loop for it in this log's git history (`scripts` has none, write one if a fourth mod needs it).
