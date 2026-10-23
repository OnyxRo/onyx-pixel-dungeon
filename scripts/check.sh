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
if [ -n "${FAST:-}" ]; then
  #FAST=1 scripts/check.sh uses the classes scripts/cyc.sh (plain javac) built in build/fast, no Gradle.
  #Use it when Gradle's daemon is busy or stuck, or several worktrees compile at once.
  bash scripts/fastjavac.sh > build/fast.log 2>&1
  grep -q "error:" build/fast.log && { say "compile FAILED, see build/fast.log"; grep -m8 "error:" -A2 build/fast.log; exit 1; }
  JARS=""
  while IFS= read -r j; do JARS="$JARS$(W "$j")${SEP}"; done < <(find ~/.gradle/caches/modules-2/files-2.1 -name '*.jar' ! -name '*sources*' 2>/dev/null | grep -v -F 1.9.10)
  CP="$(W "$ROOT/build/fast")${SEP}${JARS}"
  ASSETS="$ROOT/core/src/main/assets"
elif [ -n "${SHATTERED_JAR:-}" ]; then
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

# the classpath is too long for one command line once Gradle's cache holds several lwjgl versions, so it goes in an argument file
cpargs() { printf '%s\n' '-cp' "\"$(printf '%s' "$2" | sed 's/\\/\\\\/g')\"" > "$1"; }

# 2. compile the tests
TESTOUT="$ROOT/build/test"; rm -rf "$TESTOUT"; mkdir -p "$TESTOUT"
cpargs "$ROOT/build/cp-javac.args" "$CP"
javac -nowarn -encoding UTF-8 @"$(W "$ROOT/build/cp-javac.args")" -d "$(W "$TESTOUT")" scripts/test/*.java >> "$LOG" 2>&1 \
  || { say "test compile FAILED"; grep -m8 "error:" -A2 "$LOG"; exit 1; }
TCP="$(W "$TESTOUT")${SEP}${CP}"
cpargs "$ROOT/build/cp-run.args" "$TCP"

FAIL=0
run() { # name, class, args...
  local name="$1"; shift
  say "== $name"
  SEG_START=$(( $(wc -l < "$LOG") + 1 ))
  ( cd "$ASSETS" && java -Dsrc.root="$(W "$ROOT")" -Dmob.baseline="$(W "$ROOT/scripts/test/mob-baseline.txt")" @"$(W "$ROOT/build/cp-run.args")" "$@" ) >> "$LOG" 2>&1
  local rc=$?
  [ $rc -ne 0 ] && FAIL=1
  return $rc
}

#every sprite asks only for frames its picture has, a bad one crashes the game when the monster is made
say "== sprite frames"
python -I scripts/check-sprites.py 2>&1 | grep -E "^(.*\.java:|SUMMARY)" | sed 's/^/  /' | tail -12
[ "${PIPESTATUS[0]}" -eq 0 ] && say "  passed" || { say "  FAILED"; FAIL=1; }

if [ $QUICK -eq 0 ]; then
  run "WorldShuffleTest ($SEEDS seeds)" WorldShuffleTest "$SEEDS"; rc=$?
  tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|floors generated|ported monsters)" | tail -6 | sed 's/^/  /'
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
  #Experienced PD's perks, the parts that need no window
  run "PerksTest" PerksTest; rc=$?
  tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|SUMMARY perks)" | tail -8 | sed 's/^/  /'
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
  #Transformation's Scroll of Extraction, and that ordinary upgrades are untouched by its bookkeeping
  run "ExtractionTest" ExtractionTest; rc=$?
  tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|info|SUMMARY extraction)" | tail -12 | sed 's/^/  /'
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
  #the Reason pieces of Transformation PD (Tincture, Ritual Dagger) that work without its hero class
  run "ReasonTest" ReasonTest; rc=$?
  tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|SUMMARY reason)" | tail -12 | sed 's/^/  /'
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
  #Ryuse's extra equipment slots, saving, old saves, rings, artifacts and armor in them
  run "ExtraSlotsTest" ExtraSlotsTest; rc=$?
  tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|SUMMARY extraslots)" | tail -8 | sed 's/^/  /'
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
  #side branches (Thieves Guild, Ice Caves), every transition must lead somewhere and back
  run "BranchLevelTest" BranchLevelTest 12; rc=$?
  tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|SUMMARY branch)" | tail -8 | sed 's/^/  /'
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
  #the hero classes of Transformation PD (Friar, Slime Girl, Ninja, DM400, Princess), text, talents, subclasses, abilities
  run "TransformationHeroesTest" TransformationHeroesTest; rc=$?
  tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|SUMMARY Transformation)" | tail -12 | sed 's/^/  /'
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
  #tower, Tower PD's Necromancer, Tank and Priest, with their starting hero spells
  run "TowerHeroesTest" TransformationHeroesTest NECROHERO TANK TOWERPRIEST; rc=$?
  tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|LIMIT|SUMMARY Transformation)" | tail -12 | sed 's/^/  /'
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
  #rearranged, the six hero classes of Re-ARranged PD, text, talents, subclasses, abilities
  run "RearrangedHeroesTest" RearrangedHeroesTest; rc=$?
  tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|SUMMARY rearranged)" | tail -12 | sed 's/^/  /'
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
  #rearranged, its five challenges and the Re-ARranged heroes option
  run "RearrangedChallengesTest" RearrangedChallengesTest; rc=$?
  tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|SUMMARY rearranged)" | tail -12 | sed 's/^/  /'
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
  #polished, Honeyed Meat, the Polished PD changes option, Brittle, Electrified, blast trap, Well of Change
  run "PolishedTest" PolishedTest; rc=$?
  tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|HEADLESS|SUMMARY polished)" | tail -10 | sed 's/^/  /'
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
  #rearranged, the Re-ARranged heroes option, its rework of Shattered's six heroes
  run "RearrangedReworkTest" RearrangedReworkTest; rc=$?
  tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|SUMMARY rearranged)" | tail -12 | sed 's/^/  /'
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
  #Transformation PD's talent catalog and the Freeman, every catalog talent run through every hook
  run "TransformationCatalogTest" TransformationCatalogTest; rc=$?
  tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|SUMMARY Transformation)" | tail -14 | sed 's/^/  /'
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
  #transformation, the Necronomicon, its recipe, soul bound deaths and the wraiths it calls
  run "NecronomiconTest" NecronomiconTest; rc=$?
  tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|SUMMARY necronomicon)" | tail -10 | sed 's/^/  /'
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
  #elektro, Elektrocheckers PD's ancient runes from boss drop to use, its wells and its loot in the decks
  run "ElektroRunesTest" ElektroRunesTest; rc=$?
  tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|SUMMARY elektro)" | tail -12 | sed 's/^/  /'
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
  #the Void trap floor, item infusion, the Gun and the Half Ripper quest
  run "DevotedLeftoversTest" DevotedLeftoversTest; rc=$?
  tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|SUMMARY devoted)" | tail -12 | sed 's/^/  /'
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
  #Reforged's seven challenges, trial rooms, difficulty, Randomizer and Scroll of Decay
  run "ReforgedChallengeTest" ReforgedChallengeTest; rc=$?
  tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|info|SUMMARY reforged)" | tail -14 | sed 's/^/  /'
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
  #summoning, the Conjurer, staff minions and attunement, mana, soul spells, staffs in the loot
  run "SummoningTest" SummoningTest; rc=$?
  tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|SUMMARY summoning)" | tail -12 | sed 's/^/  /'
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
fi

# mod boss fights, walked through headless. Skipped in --quick runs.
if [ $QUICK -eq 0 ]; then
  #BOSS_ONLY=Void scripts/check.sh --new runs only the boss levels whose class name contains Void
  run "BossArenaTest" BossArenaTest ${BOSS_ONLY:-}; rc=$?
  tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|WARN|HEADLESS|SUMMARY boss)" | sed 's/^/  /' | tail -30
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
fi

# git-aware list of changed monster classes for --new is resolved inside MobArenaTest
run "MobArenaTest ${MOBS[*]}" MobArenaTest "${MOBS[@]}"; rc=$?
tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|WARN|SUMMARY)" | sed 's/^/  /' | tail -40
[ $rc -eq 0 ] && say "  passed" || say "  FAILED"

#the Randomizer, every one of its 150 changes switched on in turn for the 25 creatures it covers
RZ=(Rat Snake Gnoll Crab Swarm Slime Skeleton Thief DM100 Guard Necromancer Bat Brute RedShaman Spinner DM200 Ghoul FireElemental FrostElemental ShockElemental Warlock Monk Golem RipperDemon Succubus Eye Scorpio)
for RT in buff:1 buff:2 buff:3 nerf:1 nerf:2 nerf:3; do
  JAVA_TOOL_OPTIONS="-Drandomizer.test=$RT" run "MobArenaTest randomizer $RT" MobArenaTest "${RZ[@]}"; rc=$?
  tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|SUMMARY)" | sed 's/^/  /' | tail -6
  [ $rc -eq 0 ] && say "  passed" || say "  FAILED"
done

#items: --new checks changed ones, otherwise every catalog item (slow, mostly Shattered's own), so a full run uses --new too
if [ "${MOBS[0]}" = "--all" ]; then ITEMS=(--all); elif [ "${MOBS[0]}" = "--new" ]; then ITEMS=(--new); else ITEMS=("${MOBS[@]}"); fi
#ITEMS_ALL=1 scripts/check.sh runs every catalog item, use it once before a pull request
if [ -n "${ITEMS_ALL:-}" ]; then ITEMS=(--all); fi
run "ItemArenaTest ${ITEMS[*]}" ItemArenaTest "${ITEMS[@]}"; rc=$?
tail -n +$SEG_START "$LOG" | grep -E "^(FAIL|WARN|SUMMARY items)" | grep -v "monsters" | sed 's/^/  /' | tail -20
[ $rc -eq 0 ] && say "  passed" || say "  FAILED"

say "== full log: build/check.log"
exit $FAIL
