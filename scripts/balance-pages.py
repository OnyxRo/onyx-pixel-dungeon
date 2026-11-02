"""Balance pages, every monster and item of each section of the dungeon with its picture and its mod's icon.

    (cd core/src/main/assets && java @../../../../build/cp-run.args BalanceReport 100 <repo>/build/balance-all.tsv 26)
    (cd core/src/main/assets && java @../../../../build/cp-run.args FightReport <repo>/build/balance-all.tsv <repo>/build/fight-all.tsv)
    python -I scripts/balance-pages.py          all five sections
    python -I scripts/balance-pages.py 2        just floors 11 to 15

Reads build/balance-all.tsv and build/fight-all.tsv, writes build/balance-floors-1-5.html and the other four
(open them in a browser, no account needed). Each page links to the others.
Monster pictures are the first idle frame, read from each sprite class's texture and TextureFilm lines.
Item pictures are cut from sprites/items.png. An item's mod is worked out from its source file, files that
were in the first commit are Shattered's, the rest are matched by mod names in the file or the commit that added it.
"""
import base64, io, json, os, re, subprocess, sys
from collections import Counter, defaultdict
from PIL import Image

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
JAVA = os.path.join(ROOT, "core/src/main/java")
ASSETS = os.path.join(ROOT, "core/src/main/assets")
SRC = os.path.join(ROOT, "build/balance-all.tsv")
#the five sections, their floors, the hero's usual health at the end of each, and Shattered's regular monsters
#there (its MobSpawner), which set the comparison line
SECTIONS = [
    ("Sewers", 1, 5, 30, ["Rat", "Snake", "Gnoll", "Swarm", "Crab", "Slime"]),
    ("Prison", 6, 10, 55, ["Skeleton", "Thief", "DM100", "Guard", "Necromancer"]),
    ("Caves", 11, 15, 80, ["Bat", "Brute", "Shaman$RedShaman", "Shaman$BlueShaman", "Shaman$PurpleShaman", "Spinner", "DM200"]),
    ("City", 16, 20, 105, ["Ghoul", "Elemental$FireElemental", "Elemental$FrostElemental", "Elemental$ShockElemental", "Warlock", "Monk", "Golem"]),
    ("Halls", 21, 26, 130, ["Succubus", "Eye", "Scorpio"]),
]
if len(sys.argv) == 1:
    #every section, each in its own run so nothing carries over
    for i in range(len(SECTIONS)):
        subprocess.run([sys.executable, "-I", os.path.abspath(__file__), str(i)], check=True)
    sys.exit(0)
SECTION = int(sys.argv[1])
NAME, LOW, HIGH, HERO_HP, SHATTERED_REGULAR = SECTIONS[SECTION]
page_name = lambda i: "balance-floors-%d-%d.html" % (SECTIONS[i][1], SECTIONS[i][2])
OUT = os.path.join(ROOT, "build", page_name(SECTION))
FLOORS_PER_SEED = HIGH - LOW + 1

MODS = {  #key -> name shown on the page
    "shattered": "Shattered", "abyssalnite": "Abyssalnite", "combusted": "Combusted", "cursed": "Cursed",
    "deep": "Deep", "deistic": "Deistic", "devoted": "Devoted", "dragonic": "Dragonic", "experienced": "Experienced",
    "goblins": "Goblins", "lovecraft": "Lovecraft", "magicling": "Magic Ling", "matt": "Matt Edition",
    "rearranged": "Re-ARranged", "reforged": "Reforged", "rkpd2": "Rat King 2", "ryuse": "Ryuse's", "shittened": "Shittened",
    "spacebase": "Spacebase", "sps": "SPS", "summoning": "Summoning", "tower": "Tower", "transformation": "Transformation",
    "unleashed": "Unleashed", "xun": "Xun", "bluearchive": "Blue Archive", "rebluearchive": "Re Blue Archive",
    "buttered": "Buttered", "elemental": "Elemental", "espd": "ESPD", "regrown": "Regrown", "soft": "Soft", "zoidberg": "Zoidberg",
    "elektrocheckers": "Elektrocheckers", "deranged": "Deranged", "reremixed": "Re-Remixed", "community": "Community", "clairvoyant": "Clairvoyant", "trap": "Trap",
}
#words in a source file or commit message that name a mod, longest first so "re-arranged" wins over "arranged"
WORDS = [("elektrocheckers", "elektrocheckers"), ("community pd", "community"), ("magic ling", "magicling"), ("magicling", "magicling"), ("re-arranged", "rearranged"), ("rearranged", "rearranged"),
         ("rat king", "rkpd2"), ("rkpd2", "rkpd2"), ("matt edition", "matt"), ("re-remixed", "reremixed"), ("reremixed", "reremixed"),
         ("rebluearchive", "rebluearchive"), ("blue archive", "bluearchive"), ("bluearchive", "bluearchive"),
         ("summoning", "summoning"), ("shittened", "shittened"), ("abyssalnite", "abyssalnite"), ("transformation", "transformation"),
         ("combusted", "combusted"), ("devoted", "devoted"), ("reforged", "reforged"), ("experienced", "experienced"),
         ("cursed", "cursed"), ("ryuse", "ryuse"), ("dragonic", "dragonic"), ("lovecraft", "lovecraft"), ("spacebase", "spacebase"),
         ("goblins", "goblins"), ("unleashed", "unleashed"), ("deistic", "deistic"), ("deranged", "deranged"), ("buttered", "buttered"),
         ("regrown", "regrown"), ("zoidberg", "zoidberg"), ("elemental", "elemental"), ("espd", "espd"), ("tower", "tower"),
         ("xun", "xun"), ("sps", "sps"), ("deep", "deep"), ("matt", "matt")]

def png_uri(img, scale=3):
    img = img.resize((img.width * scale, img.height * scale), Image.NEAREST)
    b = io.BytesIO(); img.save(b, "PNG")
    return "data:image/png;base64," + base64.b64encode(b.getvalue()).decode()

def java_file(cls):
    return os.path.join(JAVA, cls.split("$")[0].replace(".", "/") + ".java")

def read(path):
    try: return open(path, encoding="utf-8", errors="replace").read()
    except OSError: return ""

#--- Assets constants, "Sprites.RAT" -> "sprites/rat.png"
ASSET = {}
for m in re.finditer(r'class (\w+)|(\w+)\s*=\s*"([^"]+\.png)"', read(os.path.join(JAVA, "com/shatteredpixel/shatteredpixeldungeon/Assets.java"))):
    if m.group(1): cur = m.group(1)
    else: ASSET[cur + "." + m.group(2)] = m.group(3); ASSET.setdefault(m.group(2), m.group(3))

def sprite_body(cls):
    """The source text of a sprite class, the nested class when the name has a $."""
    text = read(java_file(cls))
    if "$" in cls:
        inner = cls.split("$")[-1]
        m = re.search(r"class\s+" + inner + r"\b", text)
        if m: return text[m.start():], text
    return text, text

def mob_picture(sprite_cls, depth=0):
    if not sprite_cls or depth > 3: return None
    body, whole = sprite_body(sprite_cls)
    tex = re.search(r"texture\s*\(\s*([^)]+?)\s*\)", body) or re.search(r"texture\s*\(\s*([^)]+?)\s*\)", whole)
    if not tex:
        #a subclass that only changes frames, use its parent's picture
        sup = re.search(r"extends\s+(\w+)", body)
        if sup:
            pkg = sprite_cls.rsplit(".", 1)[0]
            imp = re.search(r"import\s+([\w.]+\." + sup.group(1) + r");", whole)
            return mob_picture(imp.group(1) if imp else pkg + "." + sup.group(1), depth + 1)
        return None
    arg = tex.group(1)
    path = arg.strip('"') if arg.startswith('"') else ASSET.get(".".join(arg.split(".")[-2:])) or ASSET.get(arg.split(".")[-1])
    if not path:
        const = re.search(arg.split(".")[-1] + r'\s*=\s*"([^"]+)"', whole)
        path = const.group(1) if const else None
    if not path or not os.path.exists(os.path.join(ASSETS, path)): return None
    img = Image.open(os.path.join(ASSETS, path)).convert("RGBA")
    film = re.search(r"new\s+TextureFilm\s*\(\s*\w+\s*,\s*(\d+)\s*(?:,\s*(\d+))?", body) or re.search(r"new\s+TextureFilm\s*\(\s*\w+\s*,\s*(\d+)\s*(?:,\s*(\d+))?", whole)
    w = int(film.group(1)) if film else min(img.width, 16)
    h = int(film.group(2)) if film and film.group(2) else (img.height if film else min(img.height, 16))
    fr = re.search(r"idle\.frames\s*\(\s*\w+\s*,\s*(\d+)", body) or re.search(r"idle\.frames\s*\(\s*\w+\s*,\s*(\d+)", whole)
    n = int(fr.group(1)) if fr else 0
    cols = max(1, img.width // w)
    x, y = (n % cols) * w, (n // cols) * h
    if y + h > img.height: x, y = 0, 0
    crop = img.crop((x, y, x + w, y + h))
    if crop.getbbox() is None: crop = img.crop((0, 0, w, h))  #frame left blank on this sheet, use the first one
    if crop.getbbox() is None: return None
    return png_uri(crop.crop(crop.getbbox()) if crop.width > 24 else crop, 3)

ITEMS_PNG = Image.open(os.path.join(ASSETS, "sprites/items.png")).convert("RGBA")
def item_picture(index):
    x, y = (index % 16) * 16, (index // 16) * 16
    if y + 16 > ITEMS_PNG.height: return None
    crop = ITEMS_PNG.crop((x, y, x + 16, y + 16))
    return png_uri(crop, 3) if crop.getbbox() else None

def mod_icon(key):
    p = os.path.join(ASSETS, "interfaces/mods", key + ".png")
    if not os.path.exists(p): p = os.path.join(ROOT, "desktop/src/main/assets/icons/icon_32.png")
    return png_uri(Image.open(p).convert("RGBA").resize((32, 32), Image.NEAREST), 1)

#--- which mod a class comes from
FIRST = set(subprocess.run(["git", "ls-tree", "-r", "--name-only", "0e810728"], cwd=ROOT, capture_output=True, text=True).stdout.split("\n"))
ROSTER = {}
for m in re.finditer(r'add\(\s*"(\w+)"\s*,\s*([\w.]+)\.class', read(os.path.join(JAVA, "com/shatteredpixel/shatteredpixeldungeon/actors/mobs/ModRoster.java"))):
    ROSTER.setdefault(m.group(2).split(".")[-1], m.group(1))
SPAWNERS = {"matt": "MattSpawner.java", "lovecraft": "LovecraftSpawner.java", "magicling": "MagicLingSpawner.java", "devoted": "altregion/DevotedSpawner.java"}
SPAWNER_TEXT = {k: read(os.path.join(JAVA, "com/shatteredpixel/shatteredpixeldungeon/actors/mobs", v)) for k, v in SPAWNERS.items()}

def words_mod(text):
    low = text.lower()
    for w, key in WORDS:
        if re.search(r"\b" + re.escape(w) + r"\b", low): return key
    return None

_mod_cache = {}
def mod_of(cls, is_mob):
    if cls in _mod_cache: return _mod_cache[cls]
    simple = cls.split(".")[-1].split("$")[0]
    rel = "core/src/main/java/" + cls.split("$")[0].replace(".", "/") + ".java"
    key = None
    if rel in FIRST: key = "shattered"
    elif is_mob and simple in ROSTER: key = ROSTER[simple]
    elif is_mob and ".altregion." in cls: key = "devoted"
    if key is None and is_mob:
        for k, t in SPAWNER_TEXT.items():
            if re.search(r"\b" + simple + r"\.class", t): key = k; break
    if key is None:
        key = words_mod("\n".join(read(os.path.join(ROOT, rel)).split("\n")[:40]))
    if key is None:
        log = subprocess.run(["git", "log", "--diff-filter=A", "--format=%s", "--", rel], cwd=ROOT, capture_output=True, text=True).stdout
        key = words_mod(log) or "unknown"
    _mod_cache[cls] = key
    return key

#--- read the report
floors, mobs, items = 0, {}, {}
for line in open(SRC, encoding="utf-8"):
    p = line.rstrip("\n").split("\t")
    if not LOW <= int(p[1]) <= HIGH: continue
    if p[0] == "FLOOR": floors += 1
    elif p[0] == "MOB":
        d, cls = int(p[1]), p[4]
        m = mobs.setdefault(cls, {"name": p[5], "count": 0, "avg": [], "max": 0, "hp": int(p[6]), "depths": Counter(), "worlds": Counter(), "sprite": p[11] if len(p) > 11 else ""})
        m["count"] += 1; m["avg"].append(float(p[9])); m["max"] = max(m["max"], int(p[10])); m["depths"][d] += 1; m["worlds"][p[2]] += 1
    elif p[0] == "ITEM":
        d, cls = int(p[1]), p[3]
        it = items.setdefault(cls, {"name": p[4], "count": 0, "image": int(p[6]), "dmg": [], "min": [], "max": [], "depths": Counter(), "heaps": Counter()})
        it["count"] += 1; it["depths"][d] += 1; it["heaps"][p[5]] += 1
        if int(p[7]) >= 0: it["min"].append(int(p[7])); it["max"].append(int(p[8])); it["dmg"].append((int(p[7]) + int(p[8])) / 2)

#--- what each monster did in a real fight (scripts/test/FightReport.java), by class and floor
FIGHT = {}
fight_path = os.path.join(ROOT, "build/fight-all.tsv")
if os.path.exists(fight_path):
    for line in open(fight_path, encoding="utf-8"):
        c, d, ok, hits, avg, mx, pt, start, kills = (line.rstrip("\n").split("\t") + ["0"])[:9]
        FIGHT[(c, int(d))] = (int(ok), int(hits), float(avg), int(mx), float(pt), int(start), int(kills))

seeds = floors // FLOORS_PER_SEED
mean = lambda xs: sum(xs) / len(xs) if xs else 0
icons, out_m, out_i = {}, [], []
for cls, m in mobs.items():
    key = mod_of(cls, True); icons.setdefault(key, mod_icon(key))
    melee = mean(m["avg"]); per = m["count"] / floors
    #the fight numbers, averaged over the floors it was seen on, weighted by how often it was seen there
    w_sum = hit = top = turn = kills = fights = 0; fought = []
    for d, n in m["depths"].items():
        f = FIGHT.get((cls, d))
        if not f or f[0] == 0: continue
        w_sum += n; hit += f[2] * n; turn += f[4] * n; top = max(top, f[3]); fought.append(f)
        kills += f[6]; fights += f[0]
    if w_sum:
        hit /= w_sum; turn /= w_sum
        #harmless, never hurt the hero. near, only hurt him from right beside it (it does not move). fight, came and fought
        how = "harmless" if top == 0 else ("near" if all(f[5] == 1 for f in fought if f[1] > 0) else "fight")
    else:
        hit, top, turn, how = melee, m["max"], None, "melee"
    out_m.append({"name": re.sub(r"#\d", "", m["name"]), "cls": cls.split(".")[-1], "mod": key, "pic": mob_picture(m["sprite"]),
                  "avg": round(hit, 1), "max": top, "perTurn": round(turn, 2) if turn is not None else None, "melee": round(melee, 1),
                  "how": how, "kills": kills, "fights": fights, "hp": m["hp"], "perFloor": round(per, 4), "weighted": round(hit * per, 3),
                  "floors": sorted(m["depths"]), "worlds": [w for w, _ in m["worlds"].most_common(3)]})
base = max([x["avg"] for x in out_m if x["cls"] in SHATTERED_REGULAR] or [0])
#monsters worth changing, and short reasons for the ones that look worse or safer than they are
WORRY = {"GnollThief", "GnollKnight", "GnollMarksman", "IceBug", "BrownWolf", "GrayWolf", "IceGolem", "Ballista"}
NOTES = {"Statue": "Shattered design, only attacks if disturbed", "StatueArmor": "Only attacks if disturbed", "ArmoredStatue": "Shattered design, only attacks if disturbed",
         "Mimic": "Shattered design, hidden in chests", "GoldenMimic": "Shattered design, hidden in chests", "CrystalMimic": "Shattered design, hidden in chests",
         "RotLasher": "Only hits if you stand next to it", "GnollThief": "Deep's gnolls, strong for this stage", "GnollKnight": "Deep's gnolls, strong for this stage",
         "GnollMarksman": "Deep's gnolls, strong for this stage", "IceBug": "Frozen world monster, strong for this stage", "BrownWolf": "Unleashed Frozen Caves, strong for this stage",
         "GrayWolf": "Unleashed Frozen Caves, strong for this stage", "IceGolem": "Matt's Snowfield, strong for this stage", "Ballista": "Reforged's Kobold Caves, strong for this stage",
         "Piranha": "Test floor has no water, damage unknown", "PhantomPiranha": "Test floor has no water, damage unknown", "Capybara": "Only fights back once hurt, the test never hits it",
         "CapybaraPile": "Only fights back once hurt, the test never hits it", "Guard": "Pulls you rather than hits", "Necromancer": "Attacks through skeletons it summons, not measured",
         "Emperor": "Deep's floor 5 boss"}
for x in out_m:
    x["flag"] = x["kills"] > 0 or x["max"] >= HERO_HP or x["avg"] > base * 1.5
    worry = x["cls"] in WORRY or (x["cls"] == "Elemental$FrostElemental" and 11 <= LOW <= 15)
    x["worry"] = "Worry" if worry else ("Watch" if x["flag"] else "")
    x["note"] = "Matt's Snowfield, strong for this stage" if x["cls"] == "Elemental$FrostElemental" and worry else NOTES.get(x["cls"], "")
    if not x["note"] and x["how"] == "harmless": x["note"] = "Did no damage in the test, damage unknown"
for cls, it in items.items():
    key = mod_of(cls, False); icons.setdefault(key, mod_icon(key))
    per = it["count"] / floors
    dmg = mean(it["dmg"]) if it["dmg"] else None
    out_i.append({"name": it["name"], "cls": cls.split(".")[-1], "kind": cls.split(".items.")[-1].rsplit(".", 1)[0] if "." in cls.split(".items.")[-1] else "other",
                  "mod": key, "pic": item_picture(it["image"]), "perFloor": round(per, 4), "count": it["count"],
                  "dmg": round(dmg, 1) if dmg is not None else None, "min": round(mean(it["min"]), 1) if it["min"] else None,
                  "max": round(mean(it["max"]), 1) if it["max"] else None, "weighted": round(dmg * per, 3) if dmg is not None else None,
                  "floors": sorted(it["depths"]), "where": [h.lower().replace("_", " ") for h, _ in it["heaps"].most_common(2)]})

data = {"seeds": seeds, "floors": floors, "heroHp": HERO_HP, "base": base, "name": NAME, "first": LOW, "last": HIGH,
        "regular": SHATTERED_REGULAR, "pages": [[sec[0], sec[1], sec[2], page_name(i), i == SECTION] for i, sec in enumerate(SECTIONS)], "mods": {k: [MODS.get(k, k), v] for k, v in icons.items()},
        "mobs": sorted(out_m, key=lambda x: -x["avg"]), "items": sorted(out_i, key=lambda x: -x["perFloor"])}
page = open(os.path.join(ROOT, "scripts/balance-section-page.html"), encoding="utf-8").read().replace("Sewers Balance Check", NAME + " Balance Check").replace("/*DATA*/null", json.dumps(data, separators=(",", ":")))
open(OUT, "w", encoding="utf-8").write(page)
print("SUMMARY %d monsters (%d without picture), %d items (%d without picture), %d unknown mods: %s" % (
    len(out_m), sum(1 for x in out_m if not x["pic"]), len(out_i), sum(1 for x in out_i if not x["pic"]),
    sum(1 for x in out_m + out_i if x["mod"] == "unknown"), ", ".join(x["cls"] for x in out_m + out_i if x["mod"] == "unknown")[:400]))
print("no picture:", ", ".join(x["cls"] for x in out_m if not x["pic"]))
