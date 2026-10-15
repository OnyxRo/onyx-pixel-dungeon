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
