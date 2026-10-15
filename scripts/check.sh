#!/usr/bin/env bash
# One command to check the game. Compiles, then runs the headless tests, with no window.
#
#   scripts/check.sh                 compile, WorldShuffleTest on 150 seeds, MobArenaTest on every ported monster
#   scripts/check.sh --new           same but MobArenaTest only covers monsters changed since the last commit
#   scripts/check.sh --seeds 1000    more seeds for the world test (the release workflow uses 1000)
#   scripts/check.sh Kobold Spitter  MobArenaTest on just those classes
#   scripts/check.sh --quick         compile and MobArenaTest only, skips the world test
#
# Full output goes to build/check.log, only a short summary is printed. Exit code is 0 when
# everything passed. Libraries come from Gradle's cache, or from an official Shattered jar if
# SHATTERED_JAR is set (as in the cloud setup).
set -u
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
SEEDS=150; QUICK=0; MOBS=()
while [ $# -gt 0 ]; do
  case "$1" in
    --seeds) SEEDS="$2"; shift ;;
    --quick) QUICK=1 ;;
    --new) MOBS=(--new) ;;
    --write-baseline) MOBS=(--write-baseline) ;;
    *) MOBS+=("$1") ;;
  esac
  shift
done
[ ${#MOBS[@]} -eq 0 ] && MOBS=(--all)

mkdir -p build
LOG="$ROOT/build/check.log"; : > "$LOG"
SEP=":"; W() { printf '%s' "$1"; }
case "$(uname -s)" in MINGW*|MSYS*|CYGWIN*) SEP=";"; W() { cygpath -w "$1"; } ;; esac

say() { echo "$@" | tee -a "$LOG"; }

# 1. compile
say "== compile"
if [ -n "${SHATTERED_JAR:-}" ]; then
  scripts/compile.sh "$SHATTERED_JAR" build/javac >> "$LOG" 2>&1 || { say "compile FAILED, see build/check.log"; grep -m5 "error:" "$LOG"; exit 1; }
  CLASSES="$ROOT/build/javac/classes"; LIBS="$ROOT/build/javac/lib/libs.jar"; ASSETS="$CLASSES"
  CP="$(W "$CLASSES")${SEP}$(W "$LIBS")"
else
  ./gradlew.bat --console=plain -q core:compileJava >> "$LOG" 2>&1 || ./gradlew --console=plain -q core:compileJava >> "$LOG" 2>&1 \
    || { say "compile FAILED, see build/check.log"; grep -m8 "error:" -A2 "$LOG"; exit 1; }
  JARS=""
  while IFS= read -r j; do JARS="$JARS$(W "$j")${SEP}"; done < <(find ~/.gradle/caches/modules-2/files-2.1 -name '*.jar' ! -name '*sources*' 2>/dev/null)
  CP="$(W "$ROOT/core/build/classes/java/main")${SEP}$(W "$ROOT/SPD-classes/build/classes/java/main")${SEP}${JARS}"
  ASSETS="$ROOT/core/src/main/assets"
fi
say "compiled"

# 2. compile the tests
TESTOUT="$ROOT/build/test"; rm -rf "$TESTOUT"; mkdir -p "$TESTOUT"
javac -nowarn -encoding UTF-8 -cp "$CP" -d "$(W "$TESTOUT")" scripts/test/*.java >> "$LOG" 2>&1 \
  || { say "test compile FAILED"; grep -m8 "error:" -A2 "$LOG"; exit 1; }
TCP="$(W "$TESTOUT")${SEP}${CP}"

FAIL=0
run() { # name, class, args...
  local name="$1"; shift
  say "== $name"
  ( cd "$ASSETS" && java -Dsrc.root="$(W "$ROOT")" -Dmob.baseline="$(W "$ROOT/scripts/test/mob-baseline.txt")" -cp "$TCP" "$@" ) >> "$LOG" 2>&1
  local rc=$?
  [ $rc -ne 0 ] && FAIL=1
  return $rc
}

if [ $QUICK -eq 0 ]; then
  run "WorldShuffleTest ($SEEDS seeds)" WorldShuffleTest "$SEEDS"; rc=$?
  grep -E "^(FAIL|floors generated|ported monsters)" "$LOG" | tail -6 | sed 's/^/  /'
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
fi

# git-aware list of changed monster classes for --new is resolved inside MobArenaTest
run "MobArenaTest ${MOBS[*]}" MobArenaTest "${MOBS[@]}"; rc=$?
grep -E "^(FAIL|WARN|SUMMARY)" "$LOG" | sed 's/^/  /' | tail -40
[ $rc -eq 0 ] && say "  passed" || say "  FAILED"

#items: --new checks changed ones, otherwise every catalog item (slow, mostly Shattered's own), so a full run uses --new too
if [ "${MOBS[0]}" = "--all" ] || [ "${MOBS[0]}" = "--new" ]; then ITEMS=(--new); else ITEMS=("${MOBS[@]}"); fi
run "ItemArenaTest ${ITEMS[*]}" ItemArenaTest "${ITEMS[@]}"; rc=$?
grep -E "^(FAIL|WARN|SUMMARY items)" "$LOG" | grep -v "monsters" | sed 's/^/  /' | tail -20
[ $rc -eq 0 ] && say "  passed" || say "  FAILED"

say "== full log: build/check.log"
exit $FAIL
