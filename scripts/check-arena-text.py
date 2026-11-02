#Every Messages.get(Some.class, "key") with a written key in the tower defense arenas must have its text, or the
#speech bubble shows "no text" (2026-11-02, 205 Rat King and arena lines were missing). Prints SUMMARY, exit 1 on a miss.
import glob, os, re, sys
ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
JAVA = os.path.join(ROOT, "core/src/main/java/com/shatteredpixel/shatteredpixeldungeon")
MSG = os.path.join(ROOT, "core/src/main/assets/messages")
keys = set()
for f in glob.glob(os.path.join(MSG, "*", "*.properties")):
    if os.path.basename(f).count("_"): continue
    for line in open(f, encoding="utf-8", errors="replace"):
        if "=" in line and not line.startswith("#"): keys.add(line.split("=", 1)[0].strip().lower())
#shared level keys (grass_name and the like) come from the Level class when a region level has none
fallback = ("levels.level.", "levels.regularlevel.")
files = glob.glob(os.path.join(JAVA, "levels", "Arena*.java")) + glob.glob(os.path.join(JAVA, "levels", "EndlessArena*.java")) + [os.path.join(JAVA, "actors/mobs/RatKingAvatar.java")]
miss = []
for f in files:
    src = open(f, encoding="utf-8", errors="replace").read()
    for cls, k in set(re.findall(r'Messages\.get\(\s*(\w+)\.class\s*,\s*"(\w+)"', src)):
        suffix = "." + cls.lower() + "." + k.lower()
        if any(x.endswith(suffix) for x in keys) or any((p + k.lower()) in keys for p in fallback): continue
        miss.append("%s %s.%s" % (os.path.basename(f), cls, k))
for m in sorted(miss): print("MISSING", m)
print("SUMMARY arena text keys missing: %d" % len(miss))
sys.exit(1 if miss else 0)
