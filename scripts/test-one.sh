#!/usr/bin/env bash
# Runs one headless test class against the classes the last scripts/check.sh built, no recompile.
#
#   scripts/test-one.sh MobArenaTest Ghoul
#   JAVA_TOOL_OPTIONS=-Drandomizer.test=buff:2 scripts/test-one.sh MobArenaTest Rat
#   scripts/test-one.sh ReforgedChallengeTest
#
# Run scripts/check.sh --quick once first, so build/cp-run.args and the test classes exist.
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
W() { case "$(uname -s)" in MINGW*|MSYS*|CYGWIN*) cygpath -w "$1" ;; *) printf '%s' "$1" ;; esac; }
cd "$ROOT/core/src/main/assets" || exit 1
java -Dsrc.root="$(W "$ROOT")" -Dmob.baseline="$(W "$ROOT/scripts/test/mob-baseline.txt")" @"$(W "$ROOT/build/cp-run.args")" "$@"
