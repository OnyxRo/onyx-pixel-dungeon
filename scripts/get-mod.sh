#!/usr/bin/env bash
# Clones a mod's source into ~/mod-sources/<key> (or $MOD_SOURCES), shallow, with long paths on.
#   scripts/get-mod.sh reforged        one mod from scripts/mods.txt
#   scripts/get-mod.sh --list          what is known and what is already cloned
set -eu
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
MODS="${MOD_SOURCES:-$HOME/mod-sources}"; mkdir -p "$MODS"
if [ "${1:---list}" = "--list" ]; then
  grep -v '^#' "$ROOT/scripts/mods.txt" | while IFS='|' read -r key repo what base; do
    [ -d "$MODS/$key/.git" ] && s="cloned " || s="missing"
    printf '%s  %-15s %s\n' "$s" "$key" "$what"
  done; exit 0
fi
line=$(grep "^$1|" "$ROOT/scripts/mods.txt") || { echo "unknown mod $1, see scripts/mods.txt"; exit 1; }
repo=$(echo "$line" | cut -d'|' -f2)
[ -d "$MODS/$1/.git" ] && { echo "already at $MODS/$1"; exit 0; }
git -c core.longpaths=true clone --depth 1 -q "$repo" "$MODS/$1" && echo "cloned to $MODS/$1"
