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
    ('mod package name is not ours', r'com\.(?!shatteredpixel\.shatteredpixeldungeon|badlogic|watabou)\w+\.\w+\.(?=' + OWN_AREAS + r')', PKG + '.'),
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
    #Experienced's perk checks used to be stripped here. The perk system is ported now (actors/hero/Perks), so they stay.
    ('Matt Edition createAnimation(name, fps, looped) is a plain Animation here', r'\bcreateAnimation\(\s*"\w+"\s*,\s*(\d+)\s*,\s*(true|false)(?:\s*,\s*(?:true|false))*\s*\)', r'new Animation(\1, \2)'),
    ('any other class in the mod package (Bones, Chrome and so on)', r'com\.(?!shatteredpixel\.shatteredpixeldungeon\b)\w+\.\w*pixeldungeon\w*\.(?=[A-Z])', PKG + '.'),
    ('old Level.press(cell, ch) is occupyCell(ch) now', r'public void press\(\s*int cell\s*,\s*Char (\w+)\s*\)\s*\{(\s*)super\.press\(\s*cell\s*,\s*\1\s*\);', r'public void occupyCell( Char \1 ) {\2super.occupyCell( \1 );\2int cell = \1.pos;'),
    ('standardRooms takes forceMax', r'protected int standardRooms\(\s*\)', 'protected int standardRooms( boolean forceMax )'),
    ('specialRooms takes forceMax', r'protected int specialRooms\(\s*\)', 'protected int specialRooms( boolean forceMax )'),
    ('randomRespawnCell takes the character', r'public int randomRespawnCell\(\s*\)', 'public int randomRespawnCell( Char ch )'),
    ('NPC.interact takes the character', r'public boolean interact\(\s*\)\s*\{', 'public boolean interact( Char c ) {'),
    ('Item.price is value', r'public int price\(\s*\)', 'public int value()'),
    ('Level.nMobs is mobLimit', r'public int nMobs\(\s*\)', 'public int mobLimit()'),
    ('Level.respawnTime is respawnCooldown', r'\brespawnTime\(\s*\)', 'respawnCooldown()'),
    ('Level.press(cell, ch) is occupyCell(ch)', r'\blevel\.press\(\s*[^,()]+\s*,\s*([^,()]+?)(?:\s*,\s*(?:true|false))?\s*\)', r'level.occupyCell( \1 )'),
    ('Dungeon.scaleWithDepth is the plain depth here', r'Dungeon\.scaleWithDepth\(\)', 'Dungeon.depth'),
    ('Hero.givenName is name', r'\.givenName\(\)', '.name()'),
    ('randomRespawnCell takes the character, call', r'(?<!int )randomRespawnCell\(\s*\)', 'randomRespawnCell( null )'),
    ('setHauntedIfCursed has no chance argument', r'setHauntedIfCursed\(\s*[\d.]+f?\s*\)', 'setHauntedIfCursed()'),
    ('old weapon tier 6 category', r'Generator\.Category\.WEP_T6', 'Generator.Category.WEAPON'),
    ('old multiline text class', r'RenderedTextMultiline', 'RenderedTextBlock'),
    ('old renderMultiline is renderTextBlock', r'PixelScene\.renderMultiline\(', 'PixelScene.renderTextBlock('),
    ('Bones.get returns a list now, take the first item', r'Item\s+(\w+)\s*=\s*Bones\.get\(\)\s*;', r'java.util.ArrayList<Item> \1List = Bones.get(); Item \1 = (\1List == null || \1List.isEmpty()) ? null : \1List.get(0);'),
    ('Char.move gained a travelling argument, override', r'public void move\(\s*int step\s*\)', 'public void move( int step, boolean travelling )'),
    ('Char.move gained a travelling argument, super call', r'super\.move\(\s*step\s*\)', 'super.move( step, travelling )'),
    ('Char.attack gained multipliers, override', r'public boolean attack\(\s*Char (enemy|target|defender|victim)\s*\)', r'public boolean attack( Char \1, float dmgMulti, float dmgBonus, float accMulti )'),
    ('Char.attack gained multipliers, super call', r'super\.attack\(\s*(enemy|target|defender|victim)\s*\)', r'super.attack( \1, dmgMulti, dmgBonus, accMulti )'),
    ('kill message uses name()', r'Messages\.get\(Char\.class,\s*"kill",\s*name\)', 'Messages.get(Char.class, "kill", name())'),
    ('GooSprite.pumpUp takes a warning distance', r'\(\(GooSprite\)sprite\)\.pumpUp\(\)', '((GooSprite)sprite).pumpUp( 2 )'),
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
    # Transformation PD passes damage tags through every hit, this game has none, so the tags become an empty local
    ('Transformation damage override with tags', r'(public|protected)(\s+)void(\s+)damage\(\s*int\s+(\w+)\s*,\s*Object\s+(\w+)\s*,\s*DamageTag\.\.\.\s*(\w+)\s*\)\s*\{', r'\1\2void\3damage( int \4, Object \5 ) {\n\t\tDamageTag[] \6 = new DamageTag[0];'),
    ('Transformation super.damage with tags', r'super\.damage\(\s*([^,;]+?)\s*,\s*([^,;]+?)\s*,[^;]*\)\s*;', r'super.damage( \1, \2 );'),
    ('Transformation attackProc/defenseProc override with tags', r'(public|protected)(\s+)int(\s+)(attackProc|defenseProc)\(\s*Char\s+(\w+)\s*,\s*int\s+(\w+)\s*,\s*DamageTag\.\.\.\s*(\w+)\s*\)\s*\{', r'\1\2int\3\4( Char \5, int \6 ) {\n\t\tDamageTag[] \7 = new DamageTag[0];'),
    ('Transformation super.attackProc/defenseProc with tags', r'super\.(attackProc|defenseProc)\(\s*(\w+)\s*,\s*(\w+)\s*,[^;]*\)', r'super.\1( \2, \3 )'),
    ('Transformation attack override with tags', r'(public|protected)(\s+)boolean(\s+)attack\(\s*Char\s+(\w+)\s*,\s*float\s+(\w+)\s*,\s*float\s+(\w+)\s*,\s*float\s+(\w+)\s*,\s*DamageTag\.\.\.\s*(\w+)\s*\)\s*\{', r'\1\2boolean\3attack( Char \4, float \5, float \6, float \7 ) {\n\t\tDamageTag[] \8 = new DamageTag[0];'),
    ('Transformation super.attack with tags', r'super\.attack\(\s*(\w+)\s*,\s*(\w+)\s*,\s*(\w+)\s*,\s*(\w+)\s*,[^;]*\)', r'super.attack( \1, \2, \3, \4 )'),
    ('Transformation hero talents do not exist here', r'\bhero\.pointsNegative\(Talent\.LAND_SWIMMING\)\s*(<\s*2|==\s*0)', 'true'),
    ('Transformation hero talent FISHING_TIME', r'\bhero\.(?:hasTalent|pointsInTalent)\(Talent\.FISHING_TIME\)(?:\s*==\s*2)?', 'false'),
    ('Transformation Talent.onRangedAttackHit hook', r'\bTalent\.onRangedAttackHit\([^;]*\);', '//Transformation talent hook, none here'),
    ('Dragonic, the old Dungeon.visible array is Dungeon.level.heroFOV', r'Dungeon\.visible\[', 'Dungeon.level.heroFOV['),
    # Tower PD weapons are written on Shattered 3.x duelist abilities
    ('3.x abilityChargeUse(hero) override became baseChargeUse(hero, target)', r'public\s+float\s+abilityChargeUse\(\s*Hero hero\s*\)\s*\{(\s*return[^;]*?)super\.abilityChargeUse\(\s*hero\s*\)', r'protected int baseChargeUse(Hero hero, Char target) {\1super.baseChargeUse(hero, target)'),
    ('3.x heavyBlowAbility and cleaveAbility have a damage boost argument', r'\b(Mace\.heavyBlowAbility|Sword\.cleaveAbility)\(\s*hero\s*,\s*target\s*,\s*([\d.]+f?)\s*,\s*this\s*\)', r'\1(hero, target, \2, 0, this)'),
    ('3.x sneakAbility has target and invisibility turns', r'Dagger\.sneakAbility\(\s*hero\s*,\s*(\d+)\s*,\s*this\s*\)', r'Dagger.sneakAbility(hero, target, \1, 2, this)'),
    ('3.x beforeAbilityUsed(hero) takes the target', r'\bbeforeAbilityUsed\(\s*hero\s*\)', 'beforeAbilityUsed(hero, null)'),
    # Goblins PD (Shattered 0.3 base), static Level arrays are instance fields now
    ('Goblins, Level.adjacent and distance are instance methods', r'\bLevel\.(adjacent|distance)\(', r'Dungeon.level.\1('),
    ('Goblins, static Level maps became instance fields', r'\bLevel\.(water|passable|solid|avoid|flamable|pit|losBlocking|openSpace|secret|discoverable|visited|mapped|map)\b(?=\s*[\[,);.])', r'Dungeon.level.\1'),
    ('Goblins, Level.fieldOfView is heroFOV', r'\bLevel\.fieldOfView\b', 'Dungeon.level.heroFOV'),
    ('Goblins, Level.NEIGHBOURS arrays live in PathFinder', r'\bLevel\.(NEIGHBOURS\w+)\b', r'com.watabou.utils.PathFinder.\1'),
    ('Goblins, randomDestination takes the char', r'Dungeon\.level\.randomDestination\(\s*\)', 'Dungeon.level.randomDestination( this )'),
    ('Goblins, psionic blast does not exist here', r'(?m)^[ \t]*(?:IMMUNITIES|RESISTANCES)\.add\(\s*ScrollOfPsionicBlast\.class\s*\);[ \t]*\r?\n', ''),
    ('Goblins, psionic blast import', r'(?m)^import [\w.]*\.ScrollOfPsionicBlast;[ \t]*\r?\n', ''),
    ('Goblins, ResultDescriptions import', r'(?m)^import [\w.]*\.(?:ResultDescriptions|utils\.Utils);[ \t]*\r?\n', ''),
    ('Goblins, Dungeon.findPath is Level.findStep', r'Dungeon\.findPath\(\s*this,\s*pos,\s*target,\s*([^,]+?),\s*([^,)]+?)\s*\)', r'Dungeon.findStep( this, target, \1, \2, true )'),
    ('Goblins, Dungeon.flee is Level.flee', r'Dungeon\.flee\(\s*this,\s*pos,\s*target,\s*([^,]+?),\s*([^,)]+?)\s*\)', r'Dungeon.flee( this, target, \1, \2, true )'),
    ('Goblins, updateFieldOfView call is gone', r'[ \t]*Dungeon\.level\.updateFieldOfView\(\s*this\s*\);[ \t]*\r?\n', ''),
    ('Goblins, THROWN_ITEM is PROJECTILE', r'Ballistica\.THROWN_ITEM', 'Ballistica.PROJECTILE'),
    ('Goblins, MagicMissile.shadow(parent, from, to, cb) is boltFromChar', r'MagicMissile\.shadow\(\s*parent,\s*ch\.pos,\s*(\w+),', r'MagicMissile.boltFromChar( parent, MagicMissile.SHADOW, this, \1,'),
    ('Goblins, Ballistica.cast(from, to, false, true)', r'Ballistica\.cast\(\s*(\w+)\s*,\s*([\w.]+)\s*,\s*false\s*,\s*true\s*\)', r'new Ballistica( \1, \2, Ballistica.PROJECTILE ).collisionPos'),
    ('Goblins, Dungeon.fail with ResultDescriptions', r'Dungeon\.fail\(\s*Utils\.format\(\s*ResultDescriptions\.\w+\s*,[^;]*?\)\s*\);', 'Dungeon.fail( this );'),
    ('Goblins, ResultDescriptions or Utils leftover imports', r'(?m)^import [\w.]*\.(?:ResultDescriptions|Utils);[ \t]*\r?\n', ''),
    ('Goblins, old void Mob.add(Buff) override', r'public void add\(\s*Buff buff\s*\)\s*\{', 'public boolean add( Buff buff ) {'),
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
    # the old Skeleton Key that boss levels dropped is the Worn Key now
    if 'items.keys.SkeletonKey' in t:
        t = re.sub(r'\bSkeletonKey\b', 'WornKey', t)
        fired.append('old SkeletonKey is WornKey')
    # a rule above can add a Char parameter to a file that never imported Char
    if re.search(r'\bChar\b', t) and not re.search(r'import [\w.]*\.Char;', t) and 'package ' + PKG + '.actors;' not in t:
        t = re.sub(r'(package [\w.]+;\r?\n)', r'\1\nimport ' + PKG + '.actors.Char;', t, count=1)
        fired.append('added the Char import')
    if 'Random.' in t and 'import com.watabou.utils.Random;' not in t:
        t = re.sub(r'(package [\w.]+;\r?\n)', r'\1\nimport com.watabou.utils.Random;', t, count=1)
        fired.append('added the Random import')
    return t, fired
