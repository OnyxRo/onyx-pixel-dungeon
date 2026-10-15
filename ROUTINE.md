# Cloud routine

## The prompt

Paste this as the routine's prompt.

```
You are working on Onyx's Pixel Dungeon, a Shattered Pixel Dungeon fork. This is an unattended run.

1. Read CLAUDE.md and PORTING.md first and follow them. Where they mention playtesting, auto-opening or relaunching the game, ignore that. Never launch the game and never run the screenshot tour.
2. Open TASKS.md and do only the first unchecked task. Do not start a second task.
3. Mod sources are in ~/mod-sources (or $MOD_SOURCES) and the official Shattered jar is in ~/vendor. If a source is missing, clone it with git -c core.longpaths=true clone --depth 1.
4. For any port task, invoke the port-mod skill first and use its tools (scripts/mod-report.py, scripts/port-mob.py). Check with `SHATTERED_JAR=~/vendor/ShatteredPD-v4.0.2-Java.jar scripts/check.sh --new`, and run it without --new before the pull request. It compiles and runs WorldShuffleTest and MobArenaTest headless. Keep command output short and send long output to a file.
5. If the task is finished and compiles, change its checkbox in TASKS.md to [x]. If it is only partly done, leave it unchecked and write what is left under it.
6. Update PORTING.md to match what you did. Keep TASKS.md current as you go, and add any follow-up work you discover as new unchecked tasks.
7. Create a branch named routine/<short-task-name> from sandbox-and-mod-content, commit, push, and open a pull request into sandbox-and-mod-content. The description must say what was done, what failed or was left out, and which tasks remain in TASKS.md.
8. If TASKS.md has no unchecked tasks, open a pull request or issue titled "Task queue is empty" and stop.

Writing rules for commit messages, PR text and in-game strings. No colons in the middle of a sentence, no em dashes, no interpuncts, never "real" as an intensifier, never "genuinely".
```

## For the helper

1. Accept the invite to the OnyxRo/onyx-pixel-dungeon repo on GitHub, or fork it and use the fork.
2. In Claude Code on the web (claude.ai/code), connect your GitHub account and install the Claude GitHub app on that repo.
3. Create a cloud environment with network access set to allow GitHub. Set its setup script to `bash scripts/cloud-setup.sh`.
4. Create a routine. Choose the repo and the branch `sandbox-and-mod-content`, choose the environment from step 3, and paste the prompt above.
5. Set the schedule you want (daily is fine) or run it once by hand to test.
6. Review each pull request it opens and merge it into `sandbox-and-mod-content`. The next run picks up the next unchecked task.
