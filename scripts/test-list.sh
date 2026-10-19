#!/usr/bin/env bash
# Runs a headless test class on every class named in a list file, one name per line. No recompile.
#   scripts/test-list.sh ItemArenaTest build/ra-items.txt
#   scripts/test-list.sh MobArenaTest build/ra-mobs.txt
cd "$(dirname "$0")/.."
T="$1"; L="$2"
NAMES=$(tr '\r\n' '  ' < "$L")
bash scripts/test-one.sh "$T" $NAMES
