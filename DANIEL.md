# Daniel's task queue

Only Daniel's cloud routine works on the tasks marked cloud. Onyx's own sessions work on the tasks marked live and never edit the cloud ones. The routine does one batch per run, from the first cloud task that is not done, not claimed and not blocked.

Markers. `[ ]` open, `[x]` finished, `[!]` stuck (the reason is written under it, and the routine skips it). A task with partial progress stays `[ ]` and says what is left under it. Details on the tools are in the `port-mod` skill and PORTING.md.

Batch sizes. About 15 monsters or 25 weapons or items per run, fewer if a batch is hard. Never take two tasks in one run.

Every run starts with triage. Run `python -I scripts/mod-report.py ~/mod-sources/<key> --all`, port the easy batch it prints, then the shim tier, and leave the hard tier alone.

## Cloud tasks, mechanical batches

- [ ] Cloud. Batched. Port Summoning PD Reincarnated monsters and weapons (33 monsters, 12 weapons), about 15 per run.
- [ ] Cloud. Batched. Port Shittened PD monsters and weapons (41 monsters, 6 weapons), about 15 per run.
- [ ] Cloud. Batched. Port Re-ARranged PD monsters and weapons (14 monsters, 97 weapons), about 25 weapons per run.
- [ ] Cloud. Small leftovers. Abyssalnite's EarthImbue buff, WaterOfTransmutation blob and TestItem, and Cursed PD's buffs Defense, EarthImbue and Resurrection, and blob SpawnerGas. Check that each one is used by something first.

## Live session tasks, judgment calls (the routine skips these)

- [x] Live. Experienced PD. Finished 2026-10-15. All items, bags, rods, boosters, exotic items, NPCs (Hook, Bbat, Exp Generator), the OOF Thief, the arena branch with its ticket, the black mimic branch with its chest, and the Fate Lock with grinding mode. Left out on purpose, its cycle system (Dungeon.cycle is replaced by 0), Perks, and the quest resets and escalating damage of the Fate Lock.
- [ ] Live. Pixel Dungeon Reforged. Left, the Wyrm (a mining quest monster that needs Reforged's combat system), and the Half Ripper and Construct Hero NPCs.
- [ ] Live. Abyssalnite PD. Left, the eight bosses and their levels (old and new copies of Tengu, DM-300, Dwarf King and Yog, plus Cthulhu and the Septium Pillar), and Mystery Heart which needs the new Tengu fight. 19 items and 4 weapons are done, and descriptions for the nine placeholder monsters are written.
- [ ] Live. Cursed PD (Legacy). Left, the bosses King, SewerGolem, Yog, Tengu2, the test boss and Gullin, its challenge levels, grinding level and amulet level, the NPCs Hau_tul and Wandmaker_2, and the Lucky Badge. 20 weapons, the relic enchantments, the dragon crystal allies, 12 power spells with a mana pool, bags and instruments are done.
