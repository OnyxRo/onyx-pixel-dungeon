---
name: fix-bugs
description: Fix the crashes Onyx has hit while playing. Use when Onyx says "fix bugs", "fix the crashes", "fix crashes" or similar. Reads the crash list the game writes on every crash, fixes each open crash, marks it fixed.
---

# Fix bugs from the crash list

Every time the game crashes on this computer, `CrashLog.java` (desktop module) adds a line to
`crashes/crashes.jsonl` in the main project folder. The folder is git-ignored. Screenshot tour crashes are not
recorded there, read those from the tour log.

## Steps

1. Run `python -I scripts/crashes.py`. It groups identical crashes and lists the open ones, most frequent first,
   each with an 8 letter id, a count, the last time and game version, the error and the top game frames.
   If nothing is open, tell Onyx the list is clear and stop.
2. For each open group, most frequent first, run `python -I scripts/crashes.py show ID` for the full stack.
   Line numbers in the stack are from the version that crashed, so find the method, not the line.
3. Fix the cause, not the symptom. Look for the same mistake in sibling classes (for example every item in a
   catalog, every monster of a mod) and fix those too. Where a headless check could catch it, add one to
   `scripts/test/` and `scripts/check.sh`, the way `ItemSpriteTest` was added for the journal crash.
4. Check with `FAST=1 scripts/check.sh --quick` (or the matching test). Do not open the game once per bug.
5. Commit, then mark each fixed group with
   `python -I scripts/crashes.py fixed ID "what was wrong and the fix, commit abc1234"`.
   A fixed crash that comes back later shows as CAME BACK, look at the note and dig deeper that time.
6. Tell Onyx plainly how many crash kinds were fixed, which ones could not be reproduced or fixed and why,
   and whether the desktop app updated. Then open the game once.

## Things to know

- The list only fills when the game crashes with the Windows crash box. Errors the game catches by itself are
  not in it.
- If another session is merging into main (`.git/MERGE_HEAD` exists), work in a worktree on a new branch.
- `crashes.py all` lists fixed ones too, with their notes.
