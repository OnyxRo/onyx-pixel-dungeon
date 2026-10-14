#!/bin/sh
# Runs the screenshot tour. The game starts a sandbox run by itself, visits every ported
# world, saves a picture of each plus the sandbox windows into the given folder, then quits.
# It uses its own save folder (.onyxrothman/Onyx Gallery), so real saves are not touched.
#
#   scripts/gallery.sh <output folder>
#
# Needs the classes compiled (./gradlew desktop:compileJava) and a release jar for the
# libraries (./gradlew desktop:release, run once).
set -e
OUT="${1:?usage: scripts/gallery.sh <output folder>}"
mkdir -p "$OUT"
JAR=$(ls desktop/build/libs/desktop-*.jar | head -1)
SEP=":"; case "$(uname -s)" in MINGW*|MSYS*|CYGWIN*) SEP=";"; OUT=$(cygpath -m "$OUT");; esac
CP="core/build/classes/java/main${SEP}SPD-classes/build/classes/java/main${SEP}services/build/classes/java/main${SEP}services/updates/debugUpdates/build/classes/java/main${SEP}services/news/debugNews/build/classes/java/main${SEP}desktop/build/classes/java/main${SEP}core/src/main/assets${SEP}desktop/src/main/assets${SEP}$JAR"
java "-Donyx.gallery=$OUT" "-DSpecification-Title=Onyx Gallery" -DImplementation-Title=com.onyxrothman.gallery 	-DSpecification-Version=0.0.0-INDEV -DImplementation-Version=922 -cp "$CP" 	com.shatteredpixel.shatteredpixeldungeon.desktop.DesktopLauncher
echo "pictures saved in $OUT"
