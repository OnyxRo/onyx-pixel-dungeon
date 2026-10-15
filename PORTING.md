# Porting status

Update this at the end of every task. Details live in CLAUDE.md.

## Done per mod
- Combusted PD: 3 worlds, 4 throwable weapons (catalog only). No monsters exist.
- Devoted PD: 6 worlds, all 54 monsters, 5 boss levels, 2 weapons, 11 items. Complete except the stubs below.
- Matt Edition: 2 worlds, 13 monsters plus Dark King, 9 blacksmith weapons, Glock (catalog only).
- Magic Ling PD: 13 worlds. No monsters or weapons yet.
- Transformation PD: 8 worlds. No monsters or weapons yet.
- Reforged, Shittened, Summoning, Re-ARranged, Experienced, Cursed Legacy, Ryuse, Abyssalnite, Rat King 2: worlds only (see CLAUDE.md table).
- Mod songs: fetched and imported by scripts/fetch-mod-music.py and scripts/import-mod-music.py. Files are git-ignored, rerun the scripts. Browsable in WndSongs.

## Left to do
- Monsters and weapons from Magic Ling (about 350 monster files, 74 weapons) and Transformation (59, 35).
- Monsters, weapons and items for the other ten mods (counts in CLAUDE.md).
- Mods not started: Tower PD, Lovecraft, Dragonic, SPS, Goblins, Unleashed, Deep, Pixel Spacebase.
- Sandbox: forcing a single mod by one click (toggling the rest off works today).

## Known issues
- World names and regions for the second wave were guessed from tile sheets, correct as Onyx playtests.
- Licenses and art credits unchecked for every mod. Fine for personal use, check before public release.
- Stubbed in Devoted: Void side level, bounty hunter infusion, Zealot class, ally summons, some icons.
- Matt Edition: friendly thief quest, augments, animation speed setting left out.
- Mod intro (ui/ModIntro) built but not yet seen on screen.
- Transformation monster port was blocked by the auto mode check on 2026-10-13. Onyx must allow copying mod source.
- Tools: port-copier.py, port-strings.py, port-itemsprites.py, gallery.sh, scripts/test/WorldShuffleTest.java.

## Open feature requests
- Two modes on the start screen. "Single mod" locks every floor to one chosen mod's worlds, monsters and items. "Shuffle" is the current floor-by-floor mix.
- Already built, no action needed. Mod logo top left with summary panel (WndModInfo). Ctrl+R and Ctrl+N (SandboxHotkeys, sandbox runs only).

## Cloud routine
- TASKS.md is the queue, ROUTINE.md holds the routine prompt and helper setup, scripts/cloud-setup.sh prepares the cloud environment. Not yet tested in a cloud run.
- 2026-10-14. Sandbox mod panel now has an "Only X" row under each mod. WorldShuffleTest checks every ported monster has a name string. Mod logo was already top left, and WndModInfo now ends with a credit line for the summary source.
- Hotkeys decision. Ctrl+R and Ctrl+N stay sandbox only. In normal runs a reroll would break seeded fairness and let players dodge bad floors.
