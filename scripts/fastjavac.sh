#!/usr/bin/env bash
# Compiles core and SPD-classes with plain javac straight from the Gradle cache jars, all errors printed (no 100 error cap).
# Much faster than gradle for porting rounds. Output is javac's own, errors in the same format the copier reads.
#   scripts/fastjavac.sh        prints errors, exit code 0 always
# (An older changed-files-only variant of this script was merged into the same file by mistake, it is gone.)
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
SEP=":"; W() { printf '%s' "$1"; }
case "$(uname -s)" in MINGW*|MSYS*|CYGWIN*) SEP=";"; W() { cygpath -w "$1"; } ;; esac
JARS=""
while IFS= read -r j; do JARS="$JARS$(W "$j")${SEP}"; done < <(find ~/.gradle/caches/modules-2/files-2.1 -name '*.jar' ! -name '*sources*' 2>/dev/null | grep -v -F 1.9.10)
mkdir -p build/fast
printf '%s\n' '-cp' "\"$(printf '%s' "$JARS" | sed 's/\\/\\\\/g')\"" > build/fast-cp.args
find SPD-classes/src/main/java core/src/main/java services/src/main/java services/updates/debugUpdates/src/main/java services/news/debugNews/src/main/java -name '*.java' > build/fast-src.txt
javac -nowarn -encoding UTF-8 -Xmaxerrs 5000 -proc:none @"$(W "$ROOT/build/fast-cp.args")" -d "$(W "$ROOT/build/fast")" @build/fast-src.txt 2>&1 | grep -v "^Note:"
exit 0
