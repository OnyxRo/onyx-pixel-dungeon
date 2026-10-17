#!/usr/bin/env bash
# One round of the SPS porting loop, meant to run in the background.
#   scripts/sps-cycle.sh [--fix]      with --fix, first applies scripts/sps-fix.py to the last compile log
# Compiles (scripts/fastjavac.sh), writes the short error list to build/errs.txt, and when there are no errors in
# the ported code, runs MobArenaTest on the names in build/check-mobs.txt and ItemArenaTest on build/check-items.txt.
# Writes build/cycle.done last, so a waiting loop can look for that file.
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
rm -f build/cycle.done
[ "$1" = "--fix" ] && python -I scripts/sps-fix.py > build/fix.txt 2>&1
scripts/fastjavac.sh > build/fast.log 2>&1
python -I scripts/errsum.py build/fast.log | grep -v "ControllerHandler\|DeviceCompat\|NewsScene" > build/errs.txt
if [ "$(grep -vc " errors$" build/errs.txt)" = "0" ]; then
  [ -s build/check-mobs.txt ] && scripts/fastcheck.sh mobs $(cat build/check-mobs.txt) > build/mobcheck.txt 2>&1
  [ -s build/check-items.txt ] && scripts/fastcheck.sh items $(cat build/check-items.txt) > build/itemcheck.txt 2>&1
fi
date > build/cycle.done
