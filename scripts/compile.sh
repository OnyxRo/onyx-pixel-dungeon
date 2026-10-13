#!/usr/bin/env bash
# Compiles the game with plain javac, using the official Shattered desktop jar
# for third-party libraries (libGDX, LWJGL, etc). Used where Maven Central
# isn't reachable; normal builds can use gradle instead.
#   scripts/compile.sh <path to ShatteredPD-vX-Java.jar> <out dir>
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
DEPS="$(realpath "$1")"; OUT="$(realpath -m "$2")"
rm -rf "$OUT"; mkdir -p "$OUT/classes"
# keep only the third-party libraries by stripping the game's own packages out of the official jar
LIB="$OUT/lib"; mkdir -p "$LIB/x"
( cd "$LIB/x" && unzip -q -o "$DEPS" -x 'com/shatteredpixel/*' 'com/watabou/*' 'assets/*' && zip -q -r ../libs.jar . ) 
rm -rf "$LIB/x"
SRC=$(mktemp)
find "$ROOT/SPD-classes/src/main/java" "$ROOT/core/src/main/java" \
     "$ROOT/services/src/main/java" \
     "$ROOT/services/updates/debugUpdates/src/main/java" \
     "$ROOT/services/news/debugNews/src/main/java" \
     "$ROOT/desktop/src/main/java" -name '*.java' > "$SRC"
javac -nowarn -encoding UTF-8 --release 11 -cp "$LIB/libs.jar" -d "$OUT/classes" @"$SRC" 2>&1 | grep -v "^Note:" || true
test -f "$OUT/classes/com/shatteredpixel/shatteredpixeldungeon/desktop/DesktopLauncher.class"
# game assets
cp -r "$ROOT/core/src/main/assets/." "$OUT/classes/"
cp -r "$ROOT/desktop/src/main/assets/." "$OUT/classes/"
echo "compiled $(find "$OUT/classes" -name '*.class' | wc -l) classes"
