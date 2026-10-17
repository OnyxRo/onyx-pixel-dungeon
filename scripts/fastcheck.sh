#!/usr/bin/env bash
# Quick headless check for a port, on top of scripts/fastjavac.sh (build/fast holds the compiled classes).
#   scripts/fastcheck.sh mobs Mob1 Mob2 ...     MobArenaTest on those monsters
#   scripts/fastcheck.sh items Item1 Item2 ...   ItemArenaTest on those items
# Output goes to build/fastcheck.log, only the FAIL, WARN and SUMMARY lines are printed.
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
SEP=":"; W() { printf '%s' "$1"; }
case "$(uname -s)" in MINGW*|MSYS*|CYGWIN*) SEP=";"; W() { cygpath -w "$1"; } ;; esac
KIND="$1"; shift
JARS=""
while IFS= read -r j; do JARS="$JARS$(W "$j")${SEP}"; done < <(find ~/.gradle/caches/modules-2/files-2.1 -name '*.jar' ! -name '*sources*' 2>/dev/null | grep -v -F 1.9.10)
CP="$(W "$ROOT/build/fast")${SEP}${JARS}"
cpargs() { printf '%s\n' '-cp' "\"$(printf '%s' "$2" | sed 's/\\/\\\\/g')\"" > "$1"; }
LOG="$ROOT/build/fastcheck.log"; : > "$LOG"
TESTOUT="$ROOT/build/test"; rm -rf "$TESTOUT"; mkdir -p "$TESTOUT"
cpargs "$ROOT/build/cp-javac.args" "$CP"
javac -nowarn -encoding UTF-8 @"$(W "$ROOT/build/cp-javac.args")" -d "$(W "$TESTOUT")" scripts/test/*.java >> "$LOG" 2>&1 || { echo "test compile FAILED"; grep -m8 "error:" -A2 "$LOG"; exit 1; }
cpargs "$ROOT/build/cp-run.args" "$(W "$TESTOUT")${SEP}${CP}"
CLASSES="$ROOT/build/fast"
rm -rf "$CLASSES/messages" 2>/dev/null
cp -r "$ROOT/core/src/main/assets/." "$CLASSES/" ; cp -r "$ROOT/desktop/src/main/assets/." "$CLASSES/" 2>/dev/null
case "$KIND" in
  mobs) T=MobArenaTest ;;
  items) T=ItemArenaTest ;;
  *) T="$KIND" ;;
esac
( cd "$CLASSES" && java -Dsrc.root="$(W "$ROOT")" -Dmob.baseline="$(W "$ROOT/scripts/test/mob-baseline.txt")" @"$(W "$ROOT/build/cp-run.args")" "$T" "$@" ) >> "$LOG" 2>&1
echo "exit $?"
grep -E "^(FAIL|WARN|SUMMARY|HEADLESS)" "$LOG" | tail -40
