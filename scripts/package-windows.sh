#!/usr/bin/env bash
# Builds a ready-to-play Windows zip (bundled Java runtime, double-click launcher).
#
# It reuses the official Shattered Pixel Dungeon Windows release for the launcher
# and Java runtime, and swaps in this fork's compiled game.
#
#   scripts/package-windows.sh <ShatteredPD-vX-Java.jar> <ShatteredPD-vX-Windows.zip> <out dir>
#
# It makes two zips, plus the game jar and update.json that Play.bat uses to update itself.
# The full zip has everything (about 90 MB). The installer one is smaller (about 16 MB) and
# downloads the official Windows release itself when run.
#
# Both inputs come from https://github.com/00-Evan/shattered-pixel-dungeon/releases
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
DEPS="$(realpath "$1")"; WINZIP="$(realpath "$2")"; OUT="$(realpath -m "$3")"

NAME="Onyx's Pixel Dungeon"
FILE_NAME="Onyx Pixel Dungeon"            # used for the .exe, kept free of apostrophes
PACKAGE="com.onyxrothman.onyxpixeldungeon"
VERSION="$(grep -oP "appVersionName = '\K[^']+" "$ROOT/build.gradle")"
VERSION_CODE="$(grep -oP "appVersionCode = \K[0-9]+" "$ROOT/build.gradle")"

WORK="$OUT/work"; rm -rf "$OUT"; mkdir -p "$WORK"

# 1. compile
"$ROOT/scripts/compile.sh" "$DEPS" "$WORK/build"

# 2. one jar with the game plus its libraries
mkdir -p "$WORK/jar"
( cd "$WORK/jar" && unzip -q -o "$WORK/build/lib/libs.jar" && rm -rf META-INF/*.SF META-INF/*.RSA META-INF/*.DSA META-INF/MANIFEST.MF )
cp -r "$WORK/build/classes/." "$WORK/jar/"
cat > "$WORK/manifest.txt" <<EOF
Main-Class: com.shatteredpixel.shatteredpixeldungeon.desktop.DesktopLauncher
Specification-Title: $NAME
Implementation-Title: $PACKAGE
Specification-Version: $VERSION
Implementation-Version: $VERSION_CODE
EOF
JAR="$OUT/OnyxPixelDungeon-$VERSION.jar"
( cd "$WORK/jar" && jar cfm "$JAR" "$WORK/manifest.txt" . )

# 3. Windows app, using the official launcher and runtime with our jar
APP="$WORK/$NAME"
mkdir -p "$APP" && ( cd "$APP" && unzip -q "$WINZIP" )
rm -f "$APP"/app/*.jar "$APP"/app/*.cfg
mv "$APP/Shattered Pixel Dungeon.exe" "$APP/$FILE_NAME.exe"
mv "$APP/Shattered Pixel Dungeon.ico" "$APP/$FILE_NAME.ico"
sed -i "s|<main-launcher>.*</main-launcher>|<main-launcher>$FILE_NAME</main-launcher>|; s|<app-version>.*</app-version>|<app-version>$VERSION</app-version>|" "$APP/app/.jpackage.xml"
cp "$JAR" "$APP/app/OnyxPixelDungeon.jar"
printf '%s\r\n' \
  "[Application]" \
  "app.classpath=\$APPDIR\\OnyxPixelDungeon.jar" \
  "app.mainclass=com.shatteredpixel.shatteredpixeldungeon.desktop.DesktopLauncher" \
  "" \
  "[JavaOptions]" \
  "java-options=-Djpackage.app-version=$VERSION" \
  "java-options=-XX:+IgnoreUnrecognizedVMOptions" > "$APP/app/$FILE_NAME.cfg"
cp "$ROOT/LICENSE.txt" "$APP/LICENSE.txt"
cp "$ROOT/scripts/windows-installer/Play.ps1" "$ROOT/scripts/windows-installer/Play.bat" "$APP/"
printf '%s\r\n' \
  "$NAME $VERSION" \
  "" \
  "Double-click Play.bat to play. It checks for a new version each time and updates" \
  "itself, so you only download this once. \"$FILE_NAME.exe\" also starts the game," \
  "without checking for updates." \
  "" \
  "If Windows SmartScreen warns that the app is unrecognized, click" \
  "\"More info\" and then \"Run anyway\". The launcher isn't code-signed." \
  "" \
  "Saves are kept in %APPDATA%\\.onyxrothman, separate from Shattered Pixel Dungeon." \
  "" \
  "Based on Shattered Pixel Dungeon by Evan Debenham, and Pixel Dungeon by Oleg Dolya." \
  "Licensed under the GNU GPL v3 (see LICENSE.txt)." > "$APP/README.txt"

# 4. small installer: only this game's classes and the assets it changed, placed ahead
#    of the official jars on the classpath. Install.ps1 fetches the official build.
BASE_TAG="$(basename "$WINZIP" | grep -oP 'v[0-9][^-]*(?=-Windows)')"
BASE_URL="https://github.com/00-Evan/shattered-pixel-dungeon/releases/download/$BASE_TAG/$(basename "$WINZIP")"
BASE_SHA="$(sha256sum "$WINZIP" | cut -d' ' -f1 | tr a-f A-F)"
SLIM="$WORK/slim"; mkdir -p "$SLIM/x" "$WORK/official"
( cd "$WORK/official" && unzip -q "$DEPS" )
( cd "$WORK/build/classes" && find . -type f ) | while read -r f; do
  if [[ "$f" == *.class ]] || ! cmp -s "$WORK/build/classes/$f" "$WORK/official/$f"; then
    mkdir -p "$SLIM/x/$(dirname "$f")" && cp "$WORK/build/classes/$f" "$SLIM/x/$f"
  fi
done
INST="$WORK/installer/Onyx's Pixel Dungeon Installer"; mkdir -p "$INST/files"
( cd "$SLIM/x" && jar cfm "$INST/files/OnyxPixelDungeon.jar" "$WORK/manifest.txt" . )
sed -e "s|__BASE_URL__|$BASE_URL|" -e "s|__BASE_SHA256__|$BASE_SHA|" -e "s|__VERSION__|$VERSION|" -e "s|__BASE_TAG__|$BASE_TAG|" \
  "$ROOT/scripts/windows-installer/Install.ps1" > "$INST/files/Install.ps1"
cp "$ROOT/scripts/windows-installer/Install.bat" "$INST/Install.bat"
cp "$ROOT/scripts/windows-installer/Play.ps1" "$ROOT/scripts/windows-installer/Play.bat" "$INST/files/"

# the full zip runs the whole game jar on its own classpath. Point its launcher at the
# official libraries plus the slim jar instead, so Play.bat can update it the same way
cp "$INST/files/OnyxPixelDungeon.jar" "$APP/app/OnyxPixelDungeon.jar"
unzip -q -o "$WINZIP" "app/*.jar" "app/Shattered Pixel Dungeon.cfg" -d "$WORK/officialwin"
cp "$WORK/officialwin/app/"*.jar "$APP/app/"
{ printf '%s\r\n' "[Application]" "app.classpath=\$APPDIR\\OnyxPixelDungeon.jar"
  grep "^app.classpath=" "$WORK/officialwin/app/Shattered Pixel Dungeon.cfg" | tr -d '\r' | sed 's/$/\r/'
  printf '%s\r\n' "app.mainclass=com.shatteredpixel.shatteredpixeldungeon.desktop.DesktopLauncher" "" "[JavaOptions]" \
    "java-options=-Djpackage.app-version=$VERSION" "java-options=-XX:+IgnoreUnrecognizedVMOptions"
} > "$APP/app/$FILE_NAME.cfg"
printf '%s\r\n' "$VERSION" "$BASE_TAG" > "$APP/app/onyx-version.txt"
( cd "$WORK" && zip -q -r -9 "$OUT/OnyxPixelDungeon-$VERSION-Windows.zip" "$NAME" )

# 5. what the updater downloads, a version-independent game jar and a small manifest
cp "$INST/files/OnyxPixelDungeon.jar" "$OUT/OnyxPixelDungeon-game.jar"
GAME_SHA="$(sha256sum "$OUT/OnyxPixelDungeon-game.jar" | cut -d' ' -f1)"
printf '{"version": "%s", "base": "%s", "jar": "OnyxPixelDungeon-game.jar", "sha256": "%s"}\n' \
  "$VERSION" "$BASE_TAG" "$GAME_SHA" > "$OUT/update.json"
cp "$ROOT/LICENSE.txt" "$INST/files/LICENSE.txt"
printf '%s\r\n' \
  "$NAME $VERSION installer" \
  "" \
  "Double-click Install.bat. It downloads the game's Java runtime from GitHub" \
  "(the official Shattered Pixel Dungeon release, about 100 MB), sets everything up" \
  "in a folder next to this file, and puts a shortcut on your desktop." \
  "" \
  "If Windows SmartScreen warns about Install.bat, click \"More info\" and then \"Run anyway\"." \
  "" \
  "After that, play from the desktop shortcut or Play.bat. They check for a new version" \
  "each time and update the game for you." \
  "" \
  "Run Install.bat again any time to reinstall. Saves are kept in %APPDATA%\\.onyxrothman." > "$INST/README.txt"
( cd "$WORK/installer" && zip -q -r -9 "$OUT/OnyxPixelDungeon-$VERSION-Windows-Installer.zip" "$(basename "$INST")" )
rm -rf "$WORK"
ls -la "$OUT"
