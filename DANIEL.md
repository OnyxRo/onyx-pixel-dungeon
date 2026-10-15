# Daniel's task queue

Only Daniel's cloud routine works on the tasks marked cloud. Onyx's own sessions work on the tasks marked live and never edit the cloud ones. The routine does one batch per run, from the first cloud task that is not done, not claimed and not blocked.

Markers. `[ ]` open, `[x]` finished, `[!]` stuck (the reason is written under it, and the routine skips it). A task with partial progress stays `[ ]` and says what is left under it. Details on the tools are in the `port-mod` skill and PORTING.md.

Batch sizes. About 15 monsters or 25 weapons or items per run, fewer if a batch is hard. Never take two tasks in one run.

Every run starts with triage. Run `python -I scripts/mod-report.py ~/mod-sources/<key> --all`, port the easy batch it prints, then the shim tier, and leave the hard tier alone.

## Cloud tasks, mechanical batches

- [ ] Cloud. Batched. Port Summoning PD Reincarnated monsters and weapons (33 monsters, 12 weapons), about 15 per run.
- [ ] Cloud. Batched. Port Shittened PD monsters and weapons (41 monsters, 6 weapons), about 15 per run.
- [ ] Cloud. Batched. Port Re-ARranged PD monsters and weapons (14 monsters, 97 weapons), about 25 weapons per run.
- [x] Cloud. Small leftovers. Done 2026-10-15 in a live session. EarthImbue, WaterOfTransmutation, Defense, Resurrection, LuckyBadgeBuff and the guard, earth imbue and lucky badge hooks.

## Live session tasks, judgment calls (the routine skips these)

- [x] Live. Experienced PD. Finished 2026-10-15. All items, bags, rods, boosters, exotic items, NPCs (Hook, Bbat, Exp Generator), the OOF Thief, the arena branch with its ticket, the black mimic branch with its chest, and the Fate Lock with grinding mode. Left out on purpose, its cycle system (Dungeon.cycle is replaced by 0), Perks, and the quest resets and escalating damage of the Fate Lock.
- [!] Live. Pixel Dungeon Reforged. The Wyrm was ported 2026-10-15 (sandbox only). Construct Hero needs Reforged's hero talents CONSTRUCT_MOBILITY, CONSTRUCT_VISION and CONSTRUCT_LETHALITY, which do not exist here, so it cannot work without a Reforged hero class. Half Ripper is an escort quest that needs Reforged's quest spawn and `RegularLevel.GetExitRoom`, it can be ported once Onyx decides whether new quest NPCs may appear in normal runs.
- [x] Live. Abyssalnite PD. Finished 2026-10-15. All eight bosses, all its boss and final arenas, the ritual items. Boss floors are sandbox only until Onyx has played them. Not done, Armor Kit and Tome of Mastery on purpose.
- [x] Live. Cursed PD (Legacy). Finished 2026-10-15. Weapons, powers with a mana pool, challenge floors and hub as special floors, its own boss floors, NPCs and buffs. Left out on purpose, the Cursed hero class and its subclasses (Medic, Necromancer, Cultist) and the mana bar on screen.
