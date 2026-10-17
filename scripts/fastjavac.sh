#!/usr/bin/env bash
# Fast compile check for a porting session. Compiles only the files git says changed, against the
# class files of an already built checkout (default, the main checkout two folders up from a worktree),
# with the worktree sources on the sourcepath. Takes about a minute where gradle takes eight.
#   scripts/fastjavac.sh [built checkout]      errors go to build/fast.log
set -u
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
BUILT="${1:-$ROOT/../../..}"
SEP=";"; W() { cygpath -w "$1"; }
JARS=""
while IFS= read -r j; do JARS="$JARS$(W "$j")${SEP}"; done < <(find ~/.gradle/caches/modules-2/files-2.1 -name '*.jar' ! -name '*sources*' 2>/dev/null)
CP="$(W "$BUILT/core/build/classes/java/main")${SEP}$(W "$BUILT/SPD-classes/build/classes/java/main")${SEP}${JARS}"
mkdir -p build/fast
git status --short -uall | grep -E '\.java"?$' | grep -v '^ D\|^D ' | sed 's/^...//; s/"//g' > build/fast-files.txt
printf '%s\n' '-cp' "\"$(printf '%s' "$CP" | sed 's/\\/\\\\/g')\"" > build/fast-cp.args
SRC="$(W "$ROOT/core/src/main/java")"
javac -nowarn -proc:none -encoding UTF-8 -Xmaxerrs 1000 @"$(W "$ROOT/build/fast-cp.args")" -sourcepath "$SRC" -d build/fast @"$(W "$ROOT/build/fast-files.txt")" > build/fast.log 2>&1
echo "errors: $(grep -c 'error:' build/fast.log)"
