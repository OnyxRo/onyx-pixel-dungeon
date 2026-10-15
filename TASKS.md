# Task queue

The cloud routine does the first unchecked task and checks it off. Smallest first. Details for each item are in PORTING.md and CLAUDE.md. Tasks marked "batched" stay unchecked until finished. Each run does one batch and notes progress in PORTING.md.

- [x] Update README.md to match the current game (54 worlds from 14 mods, ported monsters and weapons, sandbox, desktop app). Keep its existing structure.
- [x] Sandbox. Add a one-click "Only this mod" button per mod in the Floors and worlds panel, which turns every other mod off in `Dungeon.sandboxOffMods`.
- [x] Add a check to `WorldShuffleTest` that every ported monster in `Bestiary.isPorted` has a name string. Fix any that are missing.
- [x] Hotkeys. Ctrl+R regenerates the current floor with a newly rolled world, which may come from a different mod. Ctrl+N jumps to a random new floor. Both work in sandbox runs. Decide whether they should work in normal runs and note the choice in PORTING.md.
- [x] Move the mod logo from the top right to the top left. Clicking it opens a panel summarizing what that mod changes, based on the mod creator's own description (README, store page or changelog). Write each summary in plain words, and credit the source.
- [x] Start screen modes. Add "Single mod" (locks every floor to one chosen mod's worlds, monsters and items) beside the current "Shuffle" mode.
- [x] Port Rat King PD 2 monsters and weapons. Checked 2026-10-14, nothing to port. The repo is Zrp200/rkpd2 (key `ratking`). It adds no monsters, and its weapon classes are Shattered's own curses plus a Dream Dart that reuses Cleansing dart art, so the earlier counts came from renamed packages. Its changes are hero and balance changes.
- [ ] Port Experienced PD. Done 2026-10-14 for seven weapons (Fantasmal Stabber, Firing Snapper, Gleaming Staff, Regrowing Slasher, Starlight Smasher, Creative Gloves, Clayball) with scripts/port-item.py. Left, the 3 npcs (Bbat, ExpGenerator, Hook) and the rest of its 48 items. Its two monsters, BlackMimic and OOFThief, were checked on 2026-10-14 and cannot be ported cleanly, they need members Experienced added to Dungeon (cycle, escalatingDepth, oofedItems) and its Perks system. Repo is TrashboxBobylev/Experienced-Pixel-Dungeon-Redone, already in scripts/mods.txt.
- [ ] Port Ryuse's PD monsters and weapons (8 monsters, 5 weapons).
- [x] Port Pixel Dungeon Reforged monsters and weapons. Done 2026-10-14 for six regular monsters (kobold, spitter, ballista, quick firing ballista, fiend, unholy priest) and the Leech curse. Its Gun is the same weapon as Matt Edition's Glock, already in the catalog.
- [ ] Reforged leftovers. Wyrm, Demon Goo and Rat Usurper (bosses with their own sewer boss levels, Demon Goo and the Usurper need `SewerBossGooLevel` and `SewerBossUsurperLevel`), and the Half Ripper and Construct Hero NPCs. Reforged's combat system was not ported, so monsters use Shattered's damage, and Kobold's Dread-near-Wyrm behaviour was left out.
- [ ] Port Abyssalnite PD. Done 2026-10-14 for ten monsters (ChronosSkull, FakeWraith, FlameDemon, GroundSpore, GroundStatue, HeavenDoor, Lavastone, PhantomSpirit, WaterFlower, WindSwarm), nine of them with placeholder text since the mod never wrote any. Left. Eight monsters the mod report marks hard (King, OldDM300, NewDM300 and other bosses that need their own levels), the 4 weapons, and proper descriptions for the placeholders when Onyx asks.
- [ ] Port Cursed PD (Legacy) monsters and weapons (23 monsters, 17 weapons).
- [ ] Batched. Port Summoning PD Reincarnated monsters and weapons (33 monsters, 12 weapons), about 15 per run.
- [ ] Batched. Port Shittened PD monsters and weapons (41 monsters, 6 weapons), about 15 per run.
- [ ] Batched. Port Re-ARranged PD monsters and weapons (14 monsters, 97 weapons), about 25 weapons per run.
- [ ] Batched. Port Transformation PD monsters (59) and weapons (35). First run only adapts its damage system and ports a few monsters as a test, then about 15 per run.
- [ ] Stop and wait for Onyx. Before Magic Ling, open an issue asking whether to port all of it or a curated set, with a short list of its most distinctive monsters.
- [ ] Batched. Port Magic Ling PD monsters (about 350 files) and weapons (74), about 30 monsters per run.
- [x] Infrastructure, done 2026-10-14 as `scripts/port-item.py` (all item kinds) and `scripts/test/ItemArenaTest.java`. Original text, build `scripts/port-weapon.py` the way `port-mob.py` works (copy the weapon and its item sprite, close dependencies, copy strings, add to the journal catalog and the loot tables, headless check), and add weapon checks to `MobArenaTest` (stats roll, enchantments, save and reload). Re-ARranged alone has 97 weapons, so this pays off quickly.
