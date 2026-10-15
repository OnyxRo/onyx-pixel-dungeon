#!/usr/bin/env bash
# Setup script for the Claude Code cloud environment. Safe to run again.
# Installs JDK 17 if missing, fetches the official Shattered jar the compile script needs,
# and shallow clones the mod sources the porter uses into a folder outside the repo.
set -u
MODS="${MOD_SOURCES:-$HOME/mod-sources}"
VENDOR="${VENDOR_DIR:-$HOME/vendor}"
TAG="v4.0.2"
mkdir -p "$MODS" "$VENDOR"

# JDK 17
if ! command -v javac >/dev/null 2>&1; then
  if command -v apt-get >/dev/null 2>&1; then
    SUDO=""; [ "$(id -u)" -ne 0 ] && SUDO="sudo"
    $SUDO apt-get update -y && $SUDO apt-get install -y openjdk-17-jdk-headless unzip zip
  else
    echo "No javac and no apt-get, install JDK 17 by hand" >&2
  fi
fi

# Official Shattered jar, used by scripts/compile.sh for third party libraries
JAR="$VENDOR/ShatteredPD-$TAG-Java.jar"
if [ ! -s "$JAR" ]; then
  curl -fsSL -o "$JAR" \
    "https://github.com/00-Evan/shattered-pixel-dungeon/releases/download/$TAG/ShatteredPD-$TAG-Java.jar" \
    || { rm -f "$JAR"; echo "jar download failed" >&2; }
fi

# Mod sources. Folder name first, then repo.
clone() {
  [ -d "$MODS/$1/.git" ] && return 0
  git -c core.longpaths=true clone --depth 1 "$2" "$MODS/$1" >/dev/null 2>&1 \
    || echo "clone failed for $1" >&2
}
clone devoted        https://github.com/Arackulele/devoted-pixel-dungeon
clone combusted      https://github.com/Inferno214221/Combusted-Pixel-Dungeon
clone magicling      https://github.com/LingASDJ/Magic_Ling_Pixel_Dungeon
clone transformation https://github.com/dongyusangyu/transformation-pixel-dungeon-master
clone mattedition    https://github.com/mr-matthias1829/SPD-Matt-Edition
clone reforged       https://github.com/nathanlink169/pixel-dungeon-reforged
clone shittened      https://github.com/Haukii/shittened-pixel-dungeon
clone abyssalnite    https://github.com/MywaifuisTataraKogasa/Abyssalnite-Pixel-Dungeon
clone cursed         https://github.com/Smujb/cursed-pixel-dungeon-lgc
clone tower          https://github.com/FixAkaTheFix/Tower-Pixel-Dungeon

echo "Java: $(javac -version 2>&1)"
echo "Shattered jar: $JAR"
echo "Mod sources: $MODS"
