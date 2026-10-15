# Daniel's task queue

Only Daniel's cloud routine works on this list. Onyx's own sessions never touch these tasks and never edit this file. The routine does one batch per run, from the first task that is not done, not claimed and not blocked.

Markers. `[ ]` open, `[x]` finished, `[!]` stuck (the reason is written under it, and the routine skips it). A task with partial progress stays `[ ]` and says what is left under it. Details on the tools are in the `port-mod` skill and PORTING.md.

Batch sizes. About 15 monsters or 25 weapons or items per run, fewer if a batch is hard. Never take two tasks in one run.

## Large batched ports

- [ ] Batched. Port Summoning PD Reincarnated monsters and weapons (33 monsters, 12 weapons), about 15 per run.
- [ ] Batched. Port Shittened PD monsters and weapons (41 monsters, 6 weapons), about 15 per run.
- [ ] Batched. Port Re-ARranged PD monsters and weapons (14 monsters, 97 weapons), about 25 weapons per run.

## Finish the partly done mods

These four were each ported only in part, about 10 to 25 pieces each. Onyx wants every one of them completed. Port everything that is left, including pieces that were skipped earlier because they tangle with the mod's own systems. For those, port the small piece of the system they need, or swap in Shattered's equivalent and say so in PORTING.md. Mark a single piece `[!]` only when it truly cannot work, and give the reason.

- [ ] Finish Experienced PD. Left out earlier and now wanted, Cheese and CheeseChunk, TenguBomb, TenguShuriken, ScrollOfDetermination and ScrollOfMidas (they need RageShield and its perks), PotionOfOverload (needs an exotic potion mapping), the Hook npc, the fishing rods, loot bags, chests, boosters, and the Bbat and ExpGenerator npcs. Also its two monsters, BlackMimic and OOFThief, which need members Experienced added to Dungeon (cycle, escalatingDepth, oofedItems) and its Perks system. Repo is TrashboxBobylev/Experienced-Pixel-Dungeon-Redone, already in scripts/mods.txt. Done so far, seven weapons and 14 items.
- [ ] Finish Pixel Dungeon Reforged. Left, the Wyrm (a mining quest monster that needs Reforged's combat system), and the Half Ripper and Construct Hero NPCs. Port the parts of Reforged's combat system the Wyrm needs, and keep the Kobold's Dread-near-Wyrm behaviour once the Wyrm exists. Done so far, six regular monsters, Demon Goo, the Rat Usurper with `SewerBossUsurperLevel`, the Leech curse and Rat Claw.
- [ ] Finish Abyssalnite PD. Left, its 26 items, real descriptions for the nine monsters that have placeholder text (write them in plain words from how each one behaves), and the eight bosses. The bosses are old and new copies of Shattered's Tengu, DM-300, Dwarf King and Yog (the mod keeps legacy versions for floors 21 to 27), plus Cthulhu and the Septium Pillar, which have no sprite. Port the legacy variants as boss levels the way Devoted's were done. For Cthulhu and the Septium Pillar, reuse an existing sprite and say so, or mark them `[!]`. Done so far, ten monsters and four weapons.
- [ ] Finish Cursed PD (Legacy). Left, the bosses King, SewerGolem, Yog, the test boss and Gullin, 24 weapons and 48 items. Replace the placeholder text on the nine monsters that have it. Skip NewbornElemental, Shattered already has it. Done so far, 14 monsters plus the EvilGas blob and DeferredDeath buff.
