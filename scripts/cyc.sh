#!/usr/bin/env bash
# One compile round for a port session. Compiles everything with plain javac, writes the compact error list to
# build/fc.sum and a final "done" line. Run it in the background and read build/fc.sum when it says done.
cd "$(dirname "$0")/.."
rm -f build/fc.sum
bash scripts/fastjavac.sh > build/fc.log 2>&1
python -I scripts/errsum.py build/fc.log > build/fc.tmp 2>&1
echo done >> build/fc.tmp
mv build/fc.tmp build/fc.sum
