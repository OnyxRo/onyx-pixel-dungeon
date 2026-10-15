"""The rename table, one place for every fix to code from older Shattered bases.

port-mob.py and port-item.py import this and run apply() over every file they copy.
Add a row when a port hits the same compile error twice. Each row is (what it fixes, regex, replacement).

Rows so far come from errors met while porting Devoted, Reforged, Abyssalnite and Experienced, they were not
mined from Shattered's history, so a missing row is expected. A change that a regex cannot express (Bones.get()
returning a list, WndBag.Mode removed) is listed in the port-mod skill instead.
"""
import re

PKG = 'com.shatteredpixel.shatteredpixeldungeon'
OWN_AREAS = r'(?:actors|items|levels|sprites|messages|effects|plants|scenes|ui|windows|utils|journal|mechanics|tiles|Assets|Dungeon|Statistics|Badges|Challenges|GamesInProgress|ShatteredPixelDungeon)'

def mod_package_dir(mod):
    """The folder holding the mod's game code, like core/src/main/java/com/shatteredpixel/cursedpixeldungeon/"""
    import os
    base = os.path.join(mod, 'core', 'src', 'main', 'java')
    best = None
    for dp, dirs, files in os.walk(base):
        if 'actors' in dirs and 'items' in dirs:
            rel = os.path.relpath(dp, mod).replace(os.sep, '/') + '/'
            if rel.endswith('/shatteredpixeldungeon/'): return rel
            best = best or rel
    return best or 'core/src/main/java/' + PKG.replace('.', '/') + '/'

RENAMES = [
    ('mod package name is not ours', r'com\.(?!shatteredpixel\.shatteredpixeldungeon)\w+\.\w+\.(?=' + OWN_AREAS + r')', PKG + '.'),
    # Shattered before 0.8 kept every asset name flat in Assets
    ('old flat sound names moved to Assets.Sounds', r'\bAssets\.SND_(\w+)', r'Assets.Sounds.\1'),
    ('old flat sprite sheet names moved to Assets.Sprites', r'\bAssets\.(?!Sounds|Sprites|Music|Environment|Interfaces|Effects|Splashes|Messages|Fonts)([A-Z][A-Z0-9_]+)\b', r'Assets.Sprites.\1'),
    ('createLoot is public', r'protected(\s+Item\s+createLoot\s*\()', r'public\1'),
    # Char, Mob and Hero API
    ('attackDelay became public', r'protected(\s+float\s+attackDelay\s*\()', r'public\1'),
    ('Mob.add(Buff) returns boolean, empty override', r'public\s+void\s+add\(\s*Buff\s+buff\s*\)\s*\{\s*\}', 'public boolean add( Buff buff ) { return false; }'),
    ('throwChar gained a cause argument', r'(WandOfBlastWave\.throwChar\([^;]*?,\s*(?:true|false),\s*(?:true|false))\)', r'\1, this)'),
    ('buff icon the mod added, use the Terror icon', r'BuffIndicator\.DreadPlague', 'BuffIndicator.TERROR'),
    # items
    ('abilityChargeUse is final, override baseChargeUse', r'public\s+float\s+abilityChargeUse\(\s*Hero hero,\s*Char target\s*\)\s*\{\s*return\s*\((.*?)\);', 'protected int baseChargeUse(Hero hero, Char target) {\n\t\treturn (int)(\\1);'),
    ('durabilityPerUse takes the level', r'public float durabilityPerUse\(\s*\)\s*\{', 'public float durabilityPerUse( int level ) {'),
    ('internalTier does not exist here', r'internalTier\s*=\s*tier\s*=', 'tier ='),
    # Experienced's own members and wrappers
    ('Experienced perk checks, the perk system is not ported', r'[^\n]*\bhero\.perks\.contains\([^\n]*;[ \t]*\r?\n', ''),
    ('Experienced perks import', r'import [\w.]*\.Perks;[ \t]*\r?\n', ''),
    ('Bones.get returns a list now, take the first item', r'Item\s+(\w+)\s*=\s*Bones\.get\(\)\s*;', r'java.util.ArrayList<Item> \1List = Bones.get(); Item \1 = (\1List == null || \1List.isEmpty()) ? null : \1List.get(0);'),
    ('Char.move gained a travelling argument', r'public void move\(\s*int step\s*\)\s*\{(\s*)super\.move\(\s*step\s*\);', r'public void move( int step, boolean travelling ) {\1super.move( step, travelling );'),
    ('boss placement out of sight never ends when sandbox full sight is on', r'(?<![\w.(])heroFOV\[(\w+\.pos)\]', r'(heroFOV[\1] && !Dungeon.seeAll())'),
    ('escalatingDepth is Experienced only, use depth', r'Dungeon\.escalatingDepth\(\)', 'Dungeon.depth'),
    ('doPickUp lost its time argument', r'doPickUp\(\s*(Hero\s+\w+|\w+)\s*,\s*(int\s+\w+|[\w.]+)\s*,\s*(?:float\s+\w+|[\w.]+)\s*\)', r'doPickUp(\1, \2)'),
    ('Dungeon.cycle arithmetic is Experienced only, use 0', r'\(?\s*Dungeon\.cycle\s*\*\s*\d+L?\s*\)?', '0'),
    ('Dungeon.cycle is Experienced only, use 0', r'Dungeon\.cycle', '0'),
    ('Dungeon.Float, Int and ranges moved to Random', r'Dungeon\.(Float|Int|IntRange|NormalIntRange)\(', r'Random.\1('),
    ('NormalLongRange became NormalIntRange', r'(?:Dungeon|Random)\.NormalLongRange\(', 'Random.NormalIntRange('),
    ('Random.LongRange became IntRange', r'(?:Dungeon|Random)\.LongRange\(', 'Random.IntRange('),
    # helpers this game added for mod code
    ('TargetedCell(pos, color) is TargetedCell.colored here', r'new TargetedCell\(', 'TargetedCell.colored('),
    ('enchantment proc takes a Weapon', r'public\s+int\s+proc\(\s*Item\s+(\w+)\s*,\s*Char\s+(\w+)\s*,\s*Char\s+(\w+)\s*,\s*int', r'public int proc( Weapon \1, Char \2, Char \3, int'),
    ('prolong on a deferred damage buff is extend', r'(\bdmg\.)prolong\(', r'\1extend('),
]

def convert_long(s):
    """Mods built for huge numbers (Experienced) use long for damage. This game uses int."""
    if not re.search(r'\blong\s+(?:min|max|damageRoll|proc|value|STRReq|defenseFactor)\b|long\s+damage\b', s):
        return s
    s = re.sub(r'\blong\b', 'int', s)
    s = re.sub(r'\bLong\b', 'Integer', s)
    return re.sub(r'(\d)L\b', r'\1', s)

def apply(s):
    """Returns the fixed text and the list of reasons that fired."""
    fired = []
    t = convert_long(s)
    if t != s: fired.append('long damage became int')
    for why, pat, rep in RENAMES:
        t2 = re.sub(pat, rep, t)
        if t2 != t: fired.append(why); t = t2
    if 'Random.' in t and 'import com.watabou.utils.Random;' not in t:
        t = re.sub(r'(package [\w.]+;\r?\n)', r'\1\nimport com.watabou.utils.Random;', t, count=1)
        fired.append('added the Random import')
    return t, fired
