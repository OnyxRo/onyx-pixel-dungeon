#!/usr/bin/env bash
# Shows what a mod's own authors changed, for mods cloned with history (get-mod.sh <key> --full).
#   scripts/mod-history.sh <key>                 commit count per author, written to build/history-<key>-authors.txt
#   scripts/mod-history.sh <key> Name1 Name2     their patches in build/history-<key>.patch,
#                                                files they touched in build/history-<key>-files.txt
#   scripts/mod-history.sh <key> --diff <tag>    no history, so diff the tree against a Shattered tag
#                                                (clones it shallow beside the mods, scripts/mod-diff.py pairs files
#                                                across package renames), build/history-<key>-summary.txt and .patch
set -eu
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
MODS="${MOD_SOURCES:-$HOME/mod-sources}"
key="$1"; shift
OUT="$ROOT/build"; mkdir -p "$OUT"
cd "$MODS/$key"
git log --format='%an' | sort | uniq -c | sort -rn > "$OUT/history-$key-authors.txt"
if [ "${1:-}" = "--diff" ]; then
  tag="$2"; base="$MODS/shattered-$tag"
  [ -d "$base" ] || git -c core.longpaths=true clone -q --depth 1 --branch "$tag" https://github.com/00-Evan/shattered-pixel-dungeon "$base"
  python -I "$ROOT/scripts/mod-diff.py" "$base" "$MODS/$key" "$OUT/history-$key"
  echo "see build/history-$key-summary.txt and build/history-$key.patch"; exit 0
fi
if [ $# -gt 0 ]; then
  args=(); for a in "$@"; do args+=("--author=$a"); done
  git log "${args[@]}" -p --reverse --format='commit %h %an %ad %s' --date=short > "$OUT/history-$key.patch"
  git log "${args[@]}" --name-only --format='' | sort | uniq -c | sort -rn > "$OUT/history-$key-files.txt"
  git log "${args[@]}" --format='%h %ad %s' --date=short --reverse > "$OUT/history-$key-log.txt"
fi
cat "$OUT/history-$key-authors.txt"
