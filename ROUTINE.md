# Cloud routine for Daniel

This file holds the full instructions for Daniel's unattended runs. His queue is `DANIEL.md`. Onyx's own tasks are in `TASKS.md` and his sessions never use this queue.

## The prompt

Paste this as the routine's prompt.

```
You are working on Onyx's Pixel Dungeon, a Shattered Pixel Dungeon fork. This is an unattended run for Daniel's queue.

1. Start from the branch sandbox-and-mod-content. Make sure your checkout is at its latest commit.
2. Read CLAUDE.md, PORTING.md and ROUTINE.md first and follow them. Where they mention playtesting, auto-opening or relaunching the game, ignore that. Never launch the game and never run the screenshot tour or scripts/gallery.sh.
3. Work only from DANIEL.md. Never take a task from TASKS.md and never edit TASKS.md.
4. Run `git ls-remote --heads origin` and list open pull requests with gh. Skip any task that already has a remote branch starting with claude/ or routine/ or an open pull request that is about it. Also skip tasks marked [x] or [!]. Pick the first task left. If none is left, open an issue titled "Daniel queue is empty" and stop.
5. Do one batch of that one task and no more. Mod sources are in ~/mod-sources (or $MOD_SOURCES) and the official Shattered jar is in ~/vendor. If a source is missing, clone it with git -c core.longpaths=true clone --depth 1, using scripts/get-mod.sh and scripts/mods.txt.
6. For any port, invoke the port-mod skill first and use its tools (scripts/mod-report.py, scripts/port-mob.py, scripts/port-item.py). Check with `SHATTERED_JAR=~/vendor/ShatteredPD-v4.0.2-Java.jar scripts/check.sh --new` while you work, and run `scripts/check.sh` with no flag before the pull request. Keep command output short and send long output to a file.
7. Update DANIEL.md. Mark the task [x] only if all of it is finished and compiles. If it is partly done, leave it [ ] and write what is done and what is left under it. If you are stuck, mark it [!], write the reason under it and stop. Add follow-up work you find as new tasks in DANIEL.md.
8. Update PORTING.md to match what you did, including anything you learned that would save the next run time.
9. Create a branch named routine/<short-task-name> from sandbox-and-mod-content, commit, push, and open a pull request into sandbox-and-mod-content. The description is a short status. It says what was done, what failed or was left out, and what remains of the task in DANIEL.md.
10. Stop after the pull request is open.

Writing rules for commit messages, PR text, docs and in-game strings. No colons in the middle of a sentence, no em dashes, no interpuncts, never "real" as an intensifier, never "genuinely".
```

## For the helper

1. Accept the invite to the OnyxRo/onyx-pixel-dungeon repo on GitHub, or fork it and use the fork.
2. In Claude Code on the web (claude.ai/code), connect your GitHub account and install the Claude GitHub app on that repo.
3. Create a cloud environment with network access set to allow GitHub. Set its setup script to `bash scripts/cloud-setup.sh`.
4. Create a routine. Choose the repo and the branch `sandbox-and-mod-content`, choose the environment from step 3, and paste the prompt above.
5. Set the schedule you want (daily is fine) or run it once by hand to test.
6. Review each pull request it opens and merge it into `sandbox-and-mod-content`. The next run picks up the next task that has no open branch or pull request.
