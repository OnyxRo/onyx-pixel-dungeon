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
# FAST=1 runs against build/fast (scripts/fastjavac.sh) when the last check compiled with Gradle,
# so a fix compiled with fastjavac is tested without a full check (2026-11-01)
ARGS="$ROOT/build/cp-run.args"
if [ -n "${FAST:-}" ]; then
  python -I -c "import sys; s=open(sys.argv[1]).read(); b=chr(92)*2; s=s.replace('core'+b+'build'+b+'classes'+b+'java'+b+'main', 'build'+b+'fast').replace('core/build/classes/java/main', 'build/fast'); open(sys.argv[2],'w').write(s)" "$ARGS" "$ROOT/build/cp-fast-run.args"
  ARGS="$ROOT/build/cp-fast-run.args"
fi
cd "$ROOT/core/src/main/assets" || exit 1
java -Dsrc.root="$(W "$ROOT")" -Dmob.baseline="$(W "$ROOT/scripts/test/mob-baseline.txt")" @"$(W "$ARGS")" "$@"
