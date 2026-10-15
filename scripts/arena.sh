#!/usr/bin/env bash
# Shows monsters in the game itself, all at once, in one picture. This is the visual check for a port,
# after scripts/check.sh passes. It opens the game window for about 15 seconds on its own save folder
# (never Onyx's own saves), puts every chosen monster on one floor beside a hero that cannot die, lets them
# act, saves one picture and quits.
#
#   scripts/arena.sh                  monsters changed since the last commit (uncommitted, or the last commit)
#   scripts/arena.sh Kobold Spitter   just those, by simple class name
#   scripts/arena.sh --roster         every monster on the ModRoster
#
# The picture is build/arena/arena.png. Read it with the image reader, do not relaunch for a second look.
# Do not use scripts/gallery.sh for this, it pictures every ported monster one at a time and takes ten minutes.
set -eu
ROOT="$(cd "$(dirname "$0")/.." && pwd)"; cd "$ROOT"
W() { printf '%s' "$1"; }; SEP=":"
case "$(uname -s)" in MINGW*|MSYS*|CYGWIN*) SEP=";"; W() { cygpath -m "$1"; } ;; esac

if [ $# -eq 0 ]; then
  NAMES=$( { git status --short --untracked-files=all; git show --name-only --format= HEAD; } \
     | grep -E 'actors/mobs/.*\.java' | sed 's|.*/||; s|\.java$||' | grep -vE 'Spawner|Roster|^Mob$' | sort -u | paste -sd, - )
elif [ "$1" = "--roster" ]; then
  NAMES=$(grep -oE 'add\( "[a-z0-9]+", [A-Za-z0-9]+\.class' core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/actors/mobs/ModRoster.java | sed 's/.*, //; s/\.class//' | sort -u | paste -sd, -)
else
  NAMES=$(IFS=,; echo "$*")
fi
[ -z "$NAMES" ] && { echo "no monsters to show, name some: scripts/arena.sh Kobold Spitter"; exit 1; }
echo "arena: $NAMES"

./gradlew.bat --console=plain -q desktop:compileJava >build/arena-compile.log 2>&1 || ./gradlew --console=plain -q desktop:compileJava >build/arena-compile.log 2>&1 \
  || { echo "compile failed, see build/arena-compile.log"; grep -m5 "error:" build/arena-compile.log; exit 1; }
JAR=$(ls desktop/build/libs/desktop-*.jar 2>/dev/null | head -1)
[ -z "$JAR" ] && { echo "run ./gradlew desktop:release once so the libraries have a jar"; exit 1; }
OUT="$ROOT/build/arena"; rm -rf "$OUT"; mkdir -p "$OUT"
CP="core/build/classes/java/main${SEP}SPD-classes/build/classes/java/main${SEP}services/build/classes/java/main${SEP}services/updates/debugUpdates/build/classes/java/main${SEP}services/news/debugNews/build/classes/java/main${SEP}desktop/build/classes/java/main${SEP}core/src/main/assets${SEP}desktop/src/main/assets${SEP}$JAR"
timeout 90 java "-Donyx.gallery=$(W "$OUT")" -Donyx.gallery.only=arena "-Donyx.gallery.mobs=$NAMES" \
  "-DSpecification-Title=Onyx Gallery" -DImplementation-Title=com.onyxrothman.gallery \
  -DSpecification-Version=0.0.0-INDEV -DImplementation-Version=922 -cp "$CP" \
  com.shatteredpixel.shatteredpixeldungeon.desktop.DesktopLauncher > "$ROOT/build/arena.log" 2>&1 || true
grep -E "^\[GALLERY\]" build/arena.log || true
ls "$OUT"/arena.png >/dev/null 2>&1 && echo "picture: build/arena/arena.png" || { echo "no picture was made in 90 seconds, the game probably threw an error. Last lines of build/arena.log"; grep -v "cap character" build/arena.log | grep -E "Exception|Error" | head -3; exit 1; }
