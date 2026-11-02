# Balance notes

**Nothing in this file is to be changed in the game until Onyx says so.** These are findings only. Do not weaken, strengthen or move any monster or world listed here unless Onyx asks for that specific change.

Checked 2026-11-01 from 100 seeded runs. The pages are `build/balance-floors-1-5.html`, `-6-10`, `-11-15`, `-16-20` and `-21-26`, rebuilt with `python -I scripts/balance-pages.py` after `BalanceReport` and `FightReport` (see the `port-mod` skill and PORTING.md).

## How monsters are judged

Shattered is the baseline. A smooth curve runs through the average fight hit of Shattered's regular monsters on each floor. It is about 2.5 at floor 1, 4 at floor 5, 7 at floor 10, 13 at floor 15, 23 at floor 20 and 45 at floor 26. Each monster's "Times Shattered" is its average fight hit divided by that curve on the floors it spawns, weighted by how often it is there.

Shattered's own regulars land between 0.47 and 1.53 times. A monster is marked too strong at 1.91 times or more and too weak at 0.37 times or less, a quarter past the furthest Shattered monster. Shattered's own monsters, mimics and statues included, are the baseline and never marked.

Fight hits are measured in the game's own turns, so zaps, gas, poison, burning and summons count. The test hero is the usual level for the floor, has no armor, is healed every turn and never fights back.

## Too strong

| Floors | Monster | Mod | Times Shattered | How often |
| --- | --- | --- | --- | --- |
| 1 to 5 | Gnoll Thief | Deep | 3.6 | about 6 per 100 floors |
| 1 to 5 | Gnoll Knight | Deep | 3.5 | about 5 per 100 floors |
| 1 to 5 | Gnoll Marksman | Deep | 2.9 | about 5 per 100 floors |
| 6 to 10 | Black Ghost | Magic Ling | 2.4 | very rare |
| 6 to 10 | animated statue | Dragonic | 2.2 | rare, only attacks if disturbed |
| 6 to 10 | siphon drone | Pixel Spacebase | 2.0 | very rare |
| 6 to 10 | burglar | Matt Edition | 2.0 | about 4 per 100 floors |
| 11 to 15 | brown wolf | Unleashed | 3.3 | Frozen Caves world |
| 11 to 15 | gray wolf | Unleashed | 3.2 | Frozen Caves world |
| 11 to 15 | Ballista | Reforged | 2.6 | Kobold Caves world |
| 11 to 15 | Ice climber | SPS | 2.3 | about 13 per 100 floors |
| 11 to 15 | ice golem | Matt Edition | 2.2 | Snowfield world |

Floors 16 to 26 have none. Deep's gnolls on floors 2 and 3 are the most likely sudden early death. The caves list is all frozen or kobold world monsters, which their mods made for later, harder stages.

Not marked but worth knowing. Matt's Snowfield puts Shattered's frost elemental on floor 13, where it hits about twice the caves curve. It is not marked because the monster is Shattered's own.

## Too weak

- Wind spirit (Community), every section, weaker the deeper it goes (0.18 times in the halls).
- Replicator swarm and xenomorph (Pixel Spacebase), caves and city.
- Tribe gnoll (Summoning, 0.13) and Demon shooter (SPS, 0.21), halls.
- Flying cat (Deep), floor 6, hits for 1.
- Rare ones. Ground spore (Abyssalnite), Pure Crystal Element and Red Ghost (Magic Ling), Lucifer (SPS), unholy priest (Reforged), free bee (Tower).

## Could not be measured

The test cannot measure these, so their damage is unknown, not safe. Piranhas need water and the test floor has none. Capybaras only fight back once below half health and the test never hits them (2 to 4 a hit, 12 health with 3 to 8 armor). Prison guards pull rather than hit. Necromancers attack through skeletons they summon.

## Items

Nothing out of line in any section. The biggest weapons (a greataxe at 63, a war scythe at 80 in the halls) are rare upgraded finds, about once in 100 to 500 floors.

## Options to bring up when Onyx asks

- Too strong. Move the frozen worlds (Frozen Caves, Snowfield, SPS's snowy caves) to floors 16 to 20, and Deep's gnolls to floors 4 and 5, or cut their damage by about half.
- Too weak. Raise their damage two to three times.
- Test improvements. Give the hero armor for the floor and have him fight back, so monsters that react to being hurt are measured.
