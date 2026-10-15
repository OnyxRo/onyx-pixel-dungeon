# Task queue

The cloud routine does the first unchecked task and checks it off. Smallest first. Details for each item are in PORTING.md and CLAUDE.md. Tasks marked "batched" stay unchecked until finished. Each run does one batch and notes progress in PORTING.md.

- [x] Update README.md to match the current game (54 worlds from 14 mods, ported monsters and weapons, sandbox, desktop app). Keep its existing structure.
- [ ] Sandbox. Add a one-click "Only this mod" button per mod in the Floors and worlds panel, which turns every other mod off in `Dungeon.sandboxOffMods`.
- [ ] Add a check to `WorldShuffleTest` that every ported monster in `Bestiary.isPorted` has a name string. Fix any that are missing.
- [ ] Hotkeys. Ctrl+R regenerates the current floor with a newly rolled world, which may come from a different mod. Ctrl+N jumps to a random new floor. Both work in sandbox runs. Decide whether they should work in normal runs and note the choice in PORTING.md.
- [ ] Move the mod logo from the top right to the top left. Clicking it opens a panel summarizing what that mod changes, based on the mod creator's own description (README, store page or changelog). Write each summary in plain words, and credit the source.
- [ ] Start screen modes. Add "Single mod" (locks every floor to one chosen mod's worlds, monsters and items) beside the current "Shuffle" mode.
- [ ] Port Rat King PD 2 monsters and weapons (2 monsters, 1 weapon). Find the repo on GitHub and clone it into the mod sources folder.
- [ ] Port Experienced PD monsters and weapons (5 monsters, 9 weapons).
- [ ] Port Ryuse's PD monsters and weapons (8 monsters, 5 weapons).
- [ ] Port Pixel Dungeon Reforged monsters and weapons (11 monsters, 2 weapons).
- [ ] Port Abyssalnite PD monsters and weapons (18 monsters, 4 weapons).
- [ ] Port Cursed PD (Legacy) monsters and weapons (23 monsters, 17 weapons).
- [ ] Batched. Port Summoning PD Reincarnated monsters and weapons (33 monsters, 12 weapons), about 15 per run.
- [ ] Batched. Port Shittened PD monsters and weapons (41 monsters, 6 weapons), about 15 per run.
- [ ] Batched. Port Re-ARranged PD monsters and weapons (14 monsters, 97 weapons), about 25 weapons per run.
- [ ] Batched. Port Transformation PD monsters (59) and weapons (35). First run only adapts its damage system and ports a few monsters as a test, then about 15 per run.
- [ ] Stop and wait for Onyx. Before Magic Ling, open an issue asking whether to port all of it or a curated set, with a short list of its most distinctive monsters.
- [ ] Batched. Port Magic Ling PD monsters (about 350 files) and weapons (74), about 30 monsters per run.
