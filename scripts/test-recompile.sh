#!/bin/bash
# runtest.sh <TestClass> [args]  recompile one test (plus TestBoot) and run it, from the worktree root
T="$1"; shift
javac -nowarn -encoding UTF-8 @"$(cygpath -w build/cp-javac.args)" -d "$(cygpath -w build/test)" "scripts/test/$T.java" scripts/test/TestBoot.java > build/one-javac.log 2>&1 || { tail -5 build/one-javac.log; exit 1; }
scripts/test-one.sh "$T" "$@" > "build/$T.log" 2>&1
grep -E "^(FAIL|HEADLESS|SUMMARY|WARN)" "build/$T.log" | head -20
