"""Turn build/balance.tsv (from scripts/test/BalanceReport.java) into build/balance.json for the balance charts.

    python -I scripts/balance-report.py [build/balance.tsv] [build/balance.json]

Sections are floors 1-5, 6-10, 11-15, 16-20 and 21-26. A monster is flagged when its average hit is
more than 1.5 times the strongest Shattered monster of that section, or its top hit is more than
the hero's usual health at the end of that section. An item is flagged when it turns up in a section
two or more sections before the one where it is most common per floor, and is under a third as common there.
"""
import json, sys, statistics
from collections import defaultdict, Counter

src = sys.argv[1] if len(sys.argv) > 1 else "build/balance.tsv"
dst = sys.argv[2] if len(sys.argv) > 2 else "build/balance.json"
SECTIONS = ["Floors 1-5", "Floors 6-10", "Floors 11-15", "Floors 16-20", "Floors 21-26"]
#hero max health at the last floor of each section, a level of about 2.5 per floor (Shattered's rough pace)
HERO_HP = [30, 55, 80, 105, 130]

def sec(d): return min((d - 1) // 5, 4)

floors = Counter()            #section -> floors built
seeds_floors = Counter()      #depth -> floors built
mobs = {}                     #(section, cls) -> data
items = defaultdict(lambda: {"name": "", "by_sec": Counter(), "by_depth": Counter(), "heaps": Counter()})

for line in open(src, encoding="utf-8"):
    p = line.rstrip("\n").split("\t")
    if p[0] == "FLOOR":
        d = int(p[1]); floors[sec(d)] += 1; seeds_floors[d] += 1
    elif p[0] == "MOB":
        d = int(p[1]); world, mod, cls, name = p[2], p[3], p[4], p[5]
        hp, acc, dfn, avg, mx = int(p[6]), int(p[7]), int(p[8]), float(p[9]), int(p[10])
        k = (sec(d), cls)
        m = mobs.setdefault(k, {"cls": cls.rsplit(".", 1)[-1], "pkg": cls, "name": name, "count": 0,
                                "avg": [], "max": 0, "hp": [], "acc": [], "depths": Counter(), "mods": Counter(), "worlds": Counter(), "home": 0})
        m["count"] += 1; m["avg"].append(avg); m["max"] = max(m["max"], mx); m["hp"].append(hp); m["acc"].append(acc)
        m["depths"][d] += 1; m["mods"][mod] += 1; m["worlds"][world] += 1
        #seen on one of Shattered's own regular floors (not a boss floor), for the baseline
        if mod == "shattered" and d % 5 != 0 and d < 25: m["home"] += 1
    elif p[0] == "ITEM":
        d = int(p[1]); cls, name, heap = p[3], p[4], p[5]
        it = items[cls]; it["name"] = name; it["by_sec"][sec(d)] += 1; it["by_depth"][d] += 1; it["heaps"][heap] += 1

out = {"sections": SECTIONS, "heroHp": HERO_HP, "floors": [floors[s] for s in range(5)], "mobs": [], "items": []}
#baseline, the strongest monster on Shattered's own regular floors per section (seen on 2 percent of floors or more)
base = [0.0] * 5
for (s, cls), m in mobs.items():
    if m["home"] >= floors[s] * 0.02:
        base[s] = max(base[s], statistics.mean(m["avg"]))
out["baseline"] = [round(b, 1) for b in base]

for (s, cls), m in sorted(mobs.items(), key=lambda kv: (kv[0][0], -statistics.mean(kv[1]["avg"]))):
    avg = statistics.mean(m["avg"])
    flags = []
    if base[s] and avg > base[s] * 1.5: flags.append("hits %.1fx harder than Shattered's strongest here" % (avg / base[s]))
    if m["max"] >= HERO_HP[s]: flags.append("top hit %d is a full health bar here" % m["max"])
    if min(m["depths"]) <= s * 5 + 2 and m["max"] >= HERO_HP[s] * 0.6 and not any("full health" in f for f in flags):
        flags.append("top hit %d on floor %d" % (m["max"], min(m["depths"])))
    out["mobs"].append({
        "section": s, "cls": m["cls"], "name": m["name"], "count": m["count"],
        "perFloor": round(m["count"] / floors[s], 3),
        "avg": round(avg, 1), "max": m["max"], "hp": round(statistics.mean(m["hp"])), "acc": round(statistics.mean(m["acc"])),
        "minDepth": min(m["depths"]), "depths": sorted(m["depths"]),
        "mods": [k for k, _ in m["mods"].most_common(3)], "worlds": [k for k, _ in m["worlds"].most_common(3)],
        "flags": flags,
    })

for cls, it in items.items():
    total = sum(it["by_sec"].values())
    #the section where this item turns up most often per floor
    rate = [it["by_sec"].get(s, 0) / floors[s] for s in range(5)]
    home = max(range(5), key=lambda s: rate[s])
    for s in range(5):
        n = it["by_sec"].get(s, 0)
        if not n: continue
        flags = []
        if home - s >= 2 and rate[s] < rate[home] * 0.35:
            flags.append("mostly a %s item, %.0f times as common there" % (SECTIONS[home], rate[home] / rate[s]))
        out["items"].append({
            "section": s, "cls": cls.rsplit(".", 1)[-1], "group": cls.split(".items.")[-1].rsplit(".", 1)[0] if ".items." in cls else "",
            "name": it["name"], "count": n, "perFloor": round(n / floors[s], 3), "share": round(n / total, 3),
            "home": home, "heaps": [k for k, _ in it["heaps"].most_common(2)], "flags": flags,
        })
out["items"].sort(key=lambda r: (r["section"], -r["count"]))

json.dump(out, open(dst, "w", encoding="utf-8"), separators=(",", ":"))
fm = [m for m in out["mobs"] if m["flags"]]; fi = [i for i in out["items"] if i["flags"]]
print("SUMMARY %d monsters by section, %d flagged. %d items by section, %d flagged. baseline %s" % (len(out["mobs"]), len(fm), len(out["items"]), len(fi), out["baseline"]))
