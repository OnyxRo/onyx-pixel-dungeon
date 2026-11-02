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
    if not os.path.isdir(base): base = os.path.join(mod, 'src', 'main', 'java')  # single module mods (Unleashed)
    if not os.path.isdir(base): base = os.path.join(mod, 'java')  # Android era mods with sources at the top (SPS)
    best = None
    for dp, dirs, files in os.walk(base):
        if 'actors' in dirs and 'items' in dirs:
            rel = os.path.relpath(dp, mod).replace(os.sep, '/') + '/'
            if rel.endswith('/shatteredpixeldungeon/'): return rel
            best = best or rel
    return best or 'core/src/main/java/' + PKG.replace('.', '/') + '/'

SUMMONING_TALENTS = ('SPECIAL_DELIVERY|COLD_FRONT|ACUTENESS|HYPERSPACE|PERDERE_CRIMEN|SCRAP_BRAIN|WELCOME_TO_EARTH|THE_SANDSTORM|TIME_TOGETHER|DIRECTIVE|GOOD_INTENTIONS|LIFE_ON_AXIOM|UNSIGHTED|PLAGUEBRINGER|WILD_SORCERY|TOXIC_RELATIONSHIP|DOG_BREEDING|NUCLEAR_RAGE|SNIPER_PATIENCE|ARCANE_CLOAK|ARMORED_ARMADA|TIMEBENDING|LUST_AND_DUST|TOWER_OF_POWER|JUST_ONE_MORE_TILE|NEVER_GONNA_GIVE_YOU_UP|ASSASSINATION|QUICK_HANDS|BREAD_AND_CIRCUSES|COMET_FALL|SUFFERING_AWAY|DETERMINED|MY_SUNSHINE|OLYMPIC_SKILLS|REAL_KNIFE_MASTER|BLOOD_DRIVE|UNSETTLING_GAZE|SUPPORT_POTION|WITCHING_STRIKE|SILENCE_OF_LAMBS|BLESSING_OF_SANITY|GUIDANCE_FLAME|THAUMATURGY|SHARP_VISION|CHEMISTRY_DEGREE')

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
    # Unleashed PD (Shattered 0.3) keeps level data in static fields
    ('0.3 Level.adjacent is an instance method', r'\bLevel\.adjacent\(', 'Dungeon.level.adjacent('),
    ('0.3 static Level.water, passable, avoid, solid, pit, losBlocking arrays', r'\bLevel\.(water|passable|avoid|solid|pit|losBlocking)\b(?=\s*[\[,;)])', r'Dungeon.level.\1'),
    ('0.3 static Level.fieldOfView is the hero field of view', r'\bLevel\.fieldOfView\b', 'Dungeon.level.heroFOV'),
    ('0.3 Level.NEIGHBOURS moved to PathFinder', r'\bLevel\.(NEIGHBOURS\w+)\b', r'PathFinder.\1'),
    ('0.3 Level.WIDTH is width()', r'\bLevel\.WIDTH\b', 'Dungeon.level.width()'),
    ('0.3 Level.randomDestination() takes a Char', r'Dungeon\.level\.randomDestination\(\s*\)', 'Dungeon.level.randomDestination( this )'),
    ('0.3 ScrollOfPsionicBlast is exotic now', r'items\.scrolls\.ScrollOfPsionicBlast\b', 'items.scrolls.exotic.ScrollOfPsionicBlast'),
    ('0.3 weapon Death enchantment is Grim', r'items\.weapon\.enchantments\.Death\b', 'items.weapon.enchantments.Grim'),
    ('0.3 weapon Luck enchantment is Lucky', r'items\.weapon\.enchantments\.Luck\b', 'items.weapon.enchantments.Lucky'),
    ('0.3 weapon Instability enchantment is Unstable', r'items\.weapon\.enchantments\.Instability\b', 'items.weapon.enchantments.Unstable'),
    ('0.3 weapon Shock enchantment is Shocking', r'items\.weapon\.enchantments\.Shock\b', 'items.weapon.enchantments.Shocking'),
    ('0.3 armor Bounce glyph is Repulsion', r'items\.armor\.glyphs\.Bounce\b', 'items.armor.glyphs.Repulsion'),
    ('0.3 Dungeon.limitedDrops.swarmHP is LimitedDrops.SWARM_HP', r'Dungeon\.limitedDrops\.swarmHP\b', 'Dungeon.LimitedDrops.SWARM_HP'),
    ('0.3 a mob that logs its own TXT_KILL', r'GLog\.n\(\s*TXT_KILL\s*,\s*name\s*\)', 'GLog.n( "You were killed by " + name + "..." )'),
    ('0.3 weapon Fire enchantment is Blazing', r'items\.weapon\.enchantments\.Fire\b', 'items.weapon.enchantments.Blazing'),
    ('0.3 weapon Leech enchantment is Vampiric', r'items\.weapon\.enchantments\.Leech\b', 'items.weapon.enchantments.Sapping'),
    ('0.3 mob death message via ResultDescriptions', r'Dungeon\.fail\(\s*Utils\.format\(\s*ResultDescriptions\.\w+\s*,\s*[^;]*?\)\s*\)\s*;', 'Dungeon.fail( getClass() );'),
    ('0.3 ResultDescriptions import', r'import [\w.]*\.ResultDescriptions;\r?\n', ''),
    ('0.3 resistances() set override becomes an instance initializer', r'@Override\s*public\s+HashSet<Class<\?>>\s+resistances\(\)\s*\{\s*return\s+RESISTANCES;\s*\}', '{ resistances.addAll( RESISTANCES ); }'),
    ('0.3 immunities() set override becomes an instance initializer', r'@Override\s*public\s+HashSet<Class<\?>>\s+immunities\(\)\s*\{\s*return\s+IMMUNITIES;\s*\}', '{ immunities.addAll( IMMUNITIES ); }'),
    # Pixel Spacebase calls Dungeon SpacebaseRun and the app class PixelSpacebase
    ('Spacebase, SpacebaseRun.visible is the hero field of view', r'SpacebaseRun\.visible\b', 'Dungeon.level.heroFOV'),
    ('Spacebase, SpacebaseRun is Dungeon', r'\bSpacebaseRun\b', 'Dungeon'),
    ('Spacebase, PixelSpacebase is the app class', r'\bPixelSpacebase\b', 'ShatteredPixelDungeon'),
    ('Spacebase, old static Level arrays are instance fields now', r'\bLevel\.(passable|solid|avoid|water|pit|losBlocking|flamable|secret|discoverable)\b', r'Dungeon.level.\1'),
    ('Spacebase, old static Level.fieldOfView is the hero field of view', r'\bLevel\.fieldOfView\b', 'Dungeon.level.heroFOV'),
    ('Spacebase, MACHINE property is INORGANIC', r'Property\.MACHINE\b', 'Property.INORGANIC'),
    ('Spacebase, weapon enhancements are enchantments', r'weapon\.enhancements\b', 'weapon.enchantments'),
    ('Spacebase, levels.Room moved to levels.rooms.Room', r'shatteredpixeldungeon\.levels\.Room;', 'shatteredpixeldungeon.levels.rooms.Room;'),
    ('Spacebase, its hero classes do not exist here, a class that is never the hero', r'HeroClass\.(?:DM3000|SHAPESHIFTER|HUNTER|LOADER|CAPTAIN)\b', '((HeroClass) null)'),
    ('Spacebase, DungeonTilemap lives in tiles', r'shatteredpixeldungeon\.DungeonTilemap;', 'shatteredpixeldungeon.tiles.DungeonTilemap;'),
    ('Spacebase, healing plasmid is the potion of healing', r'items\.plasmids\.HealingPlasmid\b', 'items.potions.PotionOfHealing'),
    ('Spacebase, healing plasmid is the potion of healing 2', r'\bHealingPlasmid\b', 'PotionOfHealing'),
    ('Spacebase, loot category PLASMID', r'Category\.PLASMID\b', 'Category.POTION'),
    ('Spacebase, loot category UPGRADE', r'Category\.UPGRADE\b', 'Category.SCROLL'),
    ('Spacebase, loot category MODULE', r'Category\.MODULE\b', 'Category.RING'),
    ('Spacebase, Parts is plain gold here', r'\bParts\b', 'Gold'),
    ('Spacebase, doPickUp(Hero) is final, override the one with a position', r'public boolean doPickUp\(\s*Hero hero\s*\)', 'public boolean doPickUp(Hero hero, int pos)'),
    ('Spacebase, super.doPickUp(hero)', r'super\.doPickUp\(\s*hero\s*\)', 'super.doPickUp(hero, pos)'),
    ('Spacebase, Item.cost() is value()', r'public int cost\(\)', 'public int value()'),
    ('Spacebase, weapon defenseFactor takes a Char', r'public int defenseFactor\(\s*Hero \w+\s*\)', 'public int defenseFactor(Char owner)'),
    ('Spacebase, weapon convert is augment', r'\bconvert\.damageFactor\b', 'augment.damageFactor'),
    ('Spacebase, weapon.enhancement is enchantment', r'\.enhancement\b', '.enchantment'),
    ('Spacebase, result.convert is augment', r'\.convert\b', '.augment'),
    ('Spacebase, malfunction() is curse()', r'public boolean malfunction\(\)', 'public boolean curse()'),
    ('Spacebase, immunities().contains is isImmune', r'(\w+)\.immunities\(\)\.contains\((\w+)\.class\)', r'\1.isImmune(\2.class)'),
    ('Spacebase, buff icon TIRED', r'BuffIndicator\.TIRED', 'BuffIndicator.WEAKNESS'),
    ('Spacebase, SpacebaseTilemap is DungeonTilemap', r'\bSpacebaseTilemap\b', 'DungeonTilemap'),
    ('Spacebase, buff icon CAMOFLAGE', r'BuffIndicator\.CAMOFLAGE', 'BuffIndicator.INVISIBLE'),
    ('Spacebase, buff icon DOMINATION', r'BuffIndicator\.DOMINATION', 'BuffIndicator.AMOK'),
    ('Spacebase, buff icon JETPACK', r'BuffIndicator\.JETPACK', 'BuffIndicator.LEVITATION'),
    ('Spacebase, buff icon KNOCKOUT', r'BuffIndicator\.KNOCKOUT', 'BuffIndicator.DROWSY'),
    ('Spacebase, buff icon LOCKDOWN', r'BuffIndicator\.LOCKDOWN', 'BuffIndicator.ROOTS'),
    ('Spacebase, Mob.hostile read is alignment ENEMY', r'(\b\w+(?:\.\w+)*)\.hostile\b(?!\s*=[^=])', r'(\1.alignment == Char.Alignment.ENEMY)'),
    ('Spacebase, Mob.ally read is alignment ALLY', r'(?<![\w.])(?!com\.)(\b\w+(?:\.\w+)*)\.ally\b(?!\s*=[^=])', r'(\1.alignment == Char.Alignment.ALLY)'),
    ('Spacebase, Weapon.Enhancement is Weapon.Enchantment', r'\bWeapon\.Enhancement\b', 'Weapon.Enchantment'),
    ('Spacebase, any other package of the mod is ours', r'com\.wafitz\.pixelspacebase\.', PKG + '.'),
    #summoning, Summoning PD's own talents (a shared Rogue pool) are not here, checks for them are off
    ('Summoning, talent the mod added is never learned', r'(?:\(\(Hero\)\s*\w+\)|\b[\w.]+)\.hasTalent\(\s*Talent\.(?:' + SUMMONING_TALENTS + r')\s*\)', 'false'),
    ('Summoning, points in a talent the mod added are 0', r'(?:\(\(Hero\)\s*\w+\)|\b[\w.]+)\.pointsInTalent\(\s*Talent\.(?:' + SUMMONING_TALENTS + r')\s*\)', '0'),
    ('Summoning, Dungeon.mode is always NORMAL', r'Dungeon\.mode\s*==\s*Dungeon\.GameMode\.(?!NORMAL)\w+', 'false'),
    ('Summoning, Dungeon.mode is never another mode', r'Dungeon\.mode\s*!=\s*Dungeon\.GameMode\.(?!NORMAL)\w+', 'true'),
    ('Summoning, its conducts are never on', r'Dungeon\.isChallenged\(\s*Conducts\.Conduct\.(?!(?:NULL|EASY|NORMAL|HARD|DEV|SEED)\b)\w+\s*\)', 'false'),
]

# Pixel Spacebase keeps its limited drops in the lower case enum Dungeon.limitedDrops, ours is Dungeon.LimitedDrops
for _n, _o in [('strengthTech', 'STRENGTH_POTIONS'), ('upgradeDrops', 'UPGRADE_SCROLLS'), ('arcaneStyli', 'ARCANE_STYLI'),
               ('warlockHP', 'WARLOCK_HP'), ('replicatorSwarmHP', 'SB_REPLICATOR_HP'), ('siphonDroneHP', 'SB_SIPHON_HP'),
               ('scorpioHP', 'SB_SCORPIO_HP'), ('makingHP', 'SB_MAKING_HP'), ('alienTechDevice', 'SB_ALIEN_TECH'),
               ('armband', 'SB_ARMBAND'), ('airTank', 'SB_AIR_TANK'), ('ordnanceKit', 'SB_ORDNANCE_KIT'),
               ('utilityKit', 'SB_UTILITY_KIT'), ('plasmidKit', 'SB_PLASMID_KIT'), ('blasterHolster', 'SB_BLASTER_HOLSTER'),
               ('guardHP', 'SB_GUARD_HP'), ('allGearUpgrade', 'SB_ALL_GEAR_UPGRADE')]:
    RENAMES.append(('Spacebase, limitedDrops.' + _n, r'\bDungeon\.limitedDrops\.' + _n + r'\b', 'Dungeon.LimitedDrops.' + _o))
SPS_RULES = [
    # SPS (hmdzl) calls the level class Floor and keeps it in Dungeon.depth, the depth number is Dungeon.dungeondepth
    ('SPS Dungeon.depth is the level, order matters, before dungeondepth', r'\bDungeon\.depth\b', 'Dungeon.level'),
    ('SPS dungeondepth is the plain depth', r'Dungeon\.dungeondepth', 'Dungeon.depth'),
    ('SPS Floor static array is a level field', r'\bFloor\.(passable|flamable|solid|avoid|water|pit|losBlocking|map|discoverable|visited|mapped|openSpace|secret)\b', r'Dungeon.level.\1'),
    ('SPS Floor.fieldOfView is the hero FOV', r'\bFloor\.fieldOfView\b', 'Dungeon.level.heroFOV'),
    ('SPS Floor.NEIGHBOURS', r'\bFloor\.(NEIGHBOURS\d+)', r'PathFinder.\1'),
    ('SPS Floor.getLength', r'\bFloor\.getLength\(\)', 'Dungeon.level.length()'),
    ('SPS Floor.getWidth', r'\bFloor\.getWidth\(\)', 'Dungeon.level.width()'),
    ('SPS Floor.WIDTH', r'\bFloor\.WIDTH\b', 'Dungeon.level.width()'),
    ('SPS Floor.distance is an instance method', r'\bFloor\.(distance|adjacent)\(', r'Dungeon.level.\1('),
    ('SPS bare WIDTH', r'(?<![\w.])WIDTH\b', 'Dungeon.level.width()'),
    ('SPS bare LENGTH', r'(?<![\w.])LENGTH\b', 'Dungeon.level.length()'),
    ('SPS effect blob Fire is our Fire', r'actors\.blobs\.effectblobs\.Fire\b', 'actors.blobs.Fire'),
    ('SPS three argument damage override', r'public\s+void\s+damage\(\s*int\s+(\w+)\s*,\s*Object\s+(\w+)\s*,\s*int\s+\w+\s*\)', r'public void damage( int \1, Object \2 )'),
    ('SPS three argument super.damage', r'super\.damage\(\s*(\w+)\s*,\s*(\w+)\s*,\s*[^;]*?\)\s*;', r'super.damage( \1, \2 );'),
    ('SPS Floor class is Level', r'\bFloor\b', 'Level'),
    ('SPS Mob evadeSkill field is defenseSkill', r'\bevadeSkill\s*=', 'defenseSkill ='),
    ('SPS evadeSkill(Char) is defenseSkill(Char)', r'\bevadeSkill\(\s*Char\b', 'defenseSkill( Char'),
    ('SPS hitSkill(Char) is attackSkill(Char)', r'\bhitSkill\(\s*Char\b', 'attackSkill( Char'),
    ('SPS old MagicMissile call is boltFromChar', r'MagicMissile\.(blueLight|coldLight|earth|fire|force|poison|shadow)\(\s*parent\s*,\s*[\w.]+\s*,\s*([\w.]+)\s*,', lambda m: 'MagicMissile.boltFromChar( parent, MagicMissile.' + {'blueLight': 'FROST', 'coldLight': 'FROST', 'earth': 'EARTH', 'fire': 'FIRE', 'force': 'FORCE', 'poison': 'POISON', 'shadow': 'SHADOW'}[m.group(1)] + ', this, ' + m.group(2) + ','),
    ('SPS Burning.set is reignite', r'Buff\.affect\(\s*([\w.]+)\s*,\s*Burning\.class\s*\)\.set\([^)]*\)', r'Buff.affect(\1, Burning.class).reignite(\1)'),
    ('SPS blob seed signature', r'public\s+void\s+seed\(\s*int\s+cell\s*,\s*int\s+amount\s*\)\s*\{','public void seed( Level level, int cell, int amount ) { if (cur == null) cur = new int[level.length()]; if (off == null) off = new int[cur.length];'),
    ('SPS blob super seed', r'super\.seed\(\s*cell\s*,\s*amount\s*\)', 'super.seed( level, cell, amount )'),
    ('SPS floating text with an icon', r'showStatusWithIcon\(\s*[\w.]+\s*,\s*([^;]+?)\s*,\s*FloatingText2\.\w+\s*\)', r'showStatus( CharSprite.NEGATIVE, \1 )'),
    ('SPS belongings.discharge does not exist', r'[\w.]*belongings\.discharge\(\)\s*;', '//belongings.discharge() has no use here'),
    # Magic Ling PD gives every hit a damage type. Char here has the type enum and three argument calls, but overrides keep the plain form
    ('Magic Ling damage override with a type', r'(public|protected)(\s+)void(\s+)damage\(\s*int\s+(\w+)\s*,\s*Object\s+(\w+)\s*,\s*(?:Char\.)?DamageType\s+(\w+)\s*\)\s*\{', r'\1\2void\3damage( int \4, Object \5 ) {\n\t\tDamageType \6 = DamageType.PHYSICAL;'),
    ('Magic Ling attack override with a type', r'(public|protected)(\s+)boolean(\s+)attack\(\s*Char\s+(\w+)\s*,\s*float\s+(\w+)\s*,\s*float\s+(\w+)\s*,\s*float\s+(\w+)\s*,\s*(?:Char\.)?DamageType\s+(\w+)\s*\)\s*\{', r'\1\2boolean\3attack( Char \4, float \5, float \6, float \7 ) {\n\t\tDamageType \8 = DamageType.PHYSICAL;'),
    ('Magic Ling inner sprite classes of vanilla sprites', r'\b(?:BatSprite\.BatEDSprite|CrabSprite\.NewBornCrabSprite|MimicSprite\.Dimand)\b', lambda m: 'com.shatteredpixel.shatteredpixeldungeon.sprites.MagicLingSprites.' + m.group(0).split('.')[1]),
    ('Magic Ling static imports of its counters', r'\bimport\s+static\s+com\.shatteredpixel\.shatteredpixeldungeon\.Statistics\.(survey_research_points|unLockedFireDargon|doNotLookLing|sakaBackStage|spawnersIce|spawnersTombTownAlive|lanterfireactive|crivusfruitslevel2|crivusfruitslevel3|dimandchestmazeCollected|skydeadFight|defalult_deaddog|bossRushMode|dwarfKill|killYogMobsAnargy|attackIFGirl|RandomQuest|GoldMobDead|GoldMobLimit|TPDoorDieds|KillMazeMimic|goldRefogreCount);', r'import static com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.\1;'),
    ('Magic Ling counters kept in MagicLingStats', r'(?<!shatteredpixeldungeon\.)\bStatistics\.(survey_research_points|unLockedFireDargon|doNotLookLing|sakaBackStage|spawnersIce|spawnersTombTownAlive|lanterfireactive|crivusfruitslevel2|crivusfruitslevel3|dimandchestmazeCollected|skydeadFight|defalult_deaddog|bossRushMode|dwarfKill|killYogMobsAnargy|attackIFGirl|RandomQuest|GoldMobDead|GoldMobLimit|TPDoorDieds|KillMazeMimic|goldRefogreCount)\b', r'com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.\1'),
    ('Magic Ling ancient city badge, not ported', r'\bBadges\.validateAncityProgress\(\)\s*;', ';'),
    ('Magic Ling red dragon quest, not ported', r'\bRedDragon\.Quest\.process\(\)\s*;', ';'),
    ('Magic Ling password badges, not ported', r'\bPaswordBadges\.\w+\(\)\s*;', ';'),
    ('Magic Ling DLC conducts are never on',r'\bDungeon\.isDLC\(\s*Conducts\.Conduct\.\w+\s*\)', 'false'),
    ('Magic Ling Random.Boolean',r'\bRandom\.Boolean\(\)', '(Random.Int(2) == 0)'),
    ('Magic Ling Terrain.SALT_WATER', r'\bTerrain\.SALT_WATER\b', 'Terrain.WATER'),
    ('Magic Ling mob.properties field', r'\b(mob|enemy|ch)\.properties\.contains', r'\1.properties().contains'),
    ('Magic Ling ColorTargetedCell', r'new ColorTargetedCell\(', 'com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell.colored('),
    ('Magic Ling Speck.ROT', r'\bSpeck\.ROT\b', 'Speck.TOXIC'),
    ('Magic Ling Speck.DIED', r'\bSpeck\.DIED\b', 'Speck.DUST'),
    ('Magic Ling 7x7 area list', r'\bPathFinder\.NEIGHBOURS49\b', 'PathFinder.NEIGHBOURS25'),
    ('Magic Ling halo fire bolt', r'\bMagicMissile\.HALOFIRE\b', 'MagicMissile.FIRE'),
    ('Magic Ling broken colour imports left by older runs', r'(?m)^import\s+static\s+com\.shatteredpixel\.shatteredpixeldungeon\.ui\.0x[0-9A-Fa-f]+;[ \t]*\r?\n', ''),
    ('Magic Ling glass ray', r'\bBeam\.GlassRayS\b', 'Beam.LightRay'),
    ('Magic Ling Random.getRandomElement', r'\bRandom\.getRandomElement\(', 'Random.element('),
    ('Magic Ling static import of getRandomElement', r'(?m)^import\s+static\s+com\.watabou\.utils\.Random\.getRandomElement;[ \t]*\r?\n', ''),
    ('Magic Ling bare getRandomElement', r'(?<![\w.])getRandomElement\(', 'com.watabou.utils.Random.element('),
    ('Magic Ling equipped item level field', r'\b(belongings\.(?:armor|weapon|ring|artifact|misc))\.level\b(?!\s*\()', r'\1.level()'),
    ('Magic Ling purity potion kept inside PotionOfPurity', r'\bPotionOfPurity\.PotionOfPurityLing\b', 'com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurityLing'),
    ('Magic Ling holidays are never on', r'\bRegularLevel\.(?:chinaHoliday\s*==\s*RegularLevel\.ChinaHoliday|birthday\s*==\s*RegularLevel\.DevBirthday)\.\w+', 'false'),
    ('Magic Ling ice coins become gold', r'\bnew\s+IceCyanBlueSquareCoin\(', 'new com.shatteredpixel.shatteredpixeldungeon.items.Gold('),
    ('Magic Ling ice coin import', r'(?m)^import\s+[\w.]+\.IceCyanBlueSquareCoin;[ \t]*\r?\n', ''),
    ('Magic Ling boss weapon counter', r'\bSPDSettings\.BossWeaponCount(\d)\(', r'com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.BossWeaponCount\1('),
    ('Magic Ling kill badges, not ported', r'\bBadges\.isUnlocked\(\s*Badges\.Badge\.KILL_\w+\s*\)', 'false'),
    ('Magic Ling kill badge calls, not ported', r'\bBadges\.KILLS\w*\(\)\s*;', ';'),
    ('Magic Ling GLog colours', r'\bGLog\.(?:b|pink)\(', 'GLog.p('),
    ('Magic Ling Music.playModeBGM', r'\bMusic\.playModeBGM\(', 'Music.INSTANCE.play('),
    ('Magic Ling champions this game lacks', r'\bChampionEnemy\.(?:Bomber|Halo|HealRight|Sider|LongSider|Middle|Big)\.class\b', 'ChampionEnemy.Blazing.class'),
    ('Magic Ling Blindness.set', r'\(\(Blindness\)\s*Buff\.affect\(\s*(\w+)\s*,\s*Blindness\.class\s*\)\)\.set\(\s*([^)]+?)\s*\)', r'Buff.prolong( \1, Blindness.class, \2 )'),
    ('Magic Ling Beam.DeathRayS', r'\bBeam\.DeathRayS\b', 'Beam.DeathRay'),
    ('Magic Ling GameScene.scene.add', r'\bGameScene\.scene\.add\(', 'com.watabou.noosa.Game.scene().add('),
    # added for the Magic Ling finish (2026-10-24)
    ('Magic Ling dragon kill setter', r'\bSPDSettings\.KillDragon\(\s*\w+\s*\)\s*;', ';'),
    ('Magic Ling fire dragon kill flag set', r'(?m)^[ \t]*(?:com\.shatteredpixel\.shatteredpixeldungeon\.)?Statistics\.GameKillFireDargon\s*=[^=;]*;[ \t]*\r?\n', ''),
    ('Magic Ling mage hand checks', r'\b\w+\s+instanceof\s+MageHand\b', 'false'),
    ('Magic Ling mage hand import', r'(?m)^import\s+[\w.]+\.MageHand;[ \t]*\r?\n', ''),
    ('Magic Ling blessed ankh', r'\b(\w+)\.blessed\s*=\s*true\s*;', r'\1.bless();'),
    ('Magic Ling item quantity field set', r'\b(\w+)\.quantity\s*=\s*([^;=][^;]*);', r'\1.quantity(\2);'),
    ('Magic Ling item quantity field read', r'\b(?!this\b)([a-z]\w*)\.quantity\b(?!\s*[(=])', r'\1.quantity()'),
    ('Magic Ling sword ling bolt', r'\bMagicMissile\.SWORDLING\b', 'MagicMissile.MAGIC_MISSILE'),
    ('Magic Ling dragon kill setting', r'\bSPDSettings\.KillDragon\(\)', 'false'),
    ('Magic Ling fire dragon kill flag', r'(?<![\w.])(?:com\.shatteredpixel\.shatteredpixeldungeon\.)?Statistics\.GameKillFireDargon\b', 'false'),
    ('Magic Ling badge calls in capitals, not ported', r'\bBadges\.[A-Z][A-Z0-9_]+\(\)\s*;', ';'),
    ('Magic Ling badges this game lacks', r'\bBadges\.isUnlocked\(\s*Badges\.Badge\.(?:WAR_RIP|KILL_\w+|SWORDDRAGON|DRAWF_\w+|GODD_MAKE|HAPPY_END|NYZ_SHOP|BIG_X|CLEAR_\w+|SAKA_\w+|FIREGIRL|TOWER_\w+)\s*\)', 'false'),
    ('Magic Ling challenges this game lacks', r'\bDungeon\.isChallenged\(\s*(?:Challenges\.)?(?:EXSG|RLPT|SBSG|DHXD|MOREROOM|CS|AQUAPHOBIA|PRO|ALLCHALLENGE)\s*\)', 'false'),
    ('Magic Ling static challenge imports', r'(?m)^import\s+static\s+com\.shatteredpixel\.shatteredpixeldungeon\.Challenges\.(?:EXSG|RLPT|SBSG|DHXD|MOREROOM|CS|AQUAPHOBIA|PRO|ALLCHALLENGE);[ \t]*\r?\n', ''),
    ('Magic Ling password badge import', r'(?m)^import\s+com\.shatteredpixel\.shatteredpixeldungeon\.PaswordBadges;[ \t]*\r?\n', ''),
    ('Magic Ling item animation tag', r'\s*,\s*Item\.AnimationItem\b|\s+implements\s+Item\.AnimationItem\b(?!\s*,)', ''),
    ('Magic Ling blue burning sprite state', r'\bState\.HALOMETHANEBURNING\b', 'State.BURNING'),
    ('Magic Ling halo fist bolt', r'\bnew\s+YogFist\.HaloFist\.DarkBolt\(\)', 'new com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogFist.DarkFist.DarkBolt()'),
    ('Magic Ling level glass flag', r'(?m)^[ \t]*extraGlass\s*=\s*\w+\s*;[ \t]*\r?\n', ''),
    ('Magic Ling dwarf kill setting', r'\bSPDSettings\.KillDwarf\(\)', 'false'),
    ('Magic Ling dwarf kill setter', r'\bSPDSettings\.KillDwarf\(\s*true\s*\)\s*;', ';'),
    ('Magic Ling black keys become golden keys', r'\bnew\s+BlackKey\(', 'new com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey('),
    ('Magic Ling black key import', r'(?m)^import\s+[\w.]+\.BlackKey;[ \t]*\r?\n', ''),
    ('Magic Ling black chests become locked chests', r'\bHeap\.Type\.BLACK\b', 'Heap.Type.LOCKED_CHEST'),
    ('Magic Ling static import of the sign tile', r'(?m)^import\s+static\s+com\.shatteredpixel\.shatteredpixeldungeon\.levels\.Terrain\.SIGN(?:_SP)?;[ \t]*\r?\n', ''),
    ('Magic Ling sign tiles', r'(?<![\w.])(?:Terrain\.)?SIGN_SP\b', 'Terrain.EMPTY_DECO'),
    ('Magic Ling second weapon charges', r'\bcharger\.secondCharges\s*\+\s*charger\.secondPartialCharge\b', 'charger.charges + charger.partialCharge'),
    ('Magic Ling counters added in the finish', r'(?<![\w.])(?:com\.shatteredpixel\.shatteredpixeldungeon\.)?Statistics\.(RandMode|NoTime|fireGirlAnary|TrueYogNoDied|winGame|LenaAnaryTotal|beatMerchant|stoleItem|Tomb_Reach|AbyssCityRules|isSmallLeaf|mustTengu|mimicking)\b', r'com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.\1'),
    ('Magic Ling hell burning is ordinary burning', r'\bHellBurning\b', 'Burning'),
    ('Magic Ling blue ray effect', r'\bEffects\.Type\.BLUE_RAY\b', 'Effects.Type.LIGHT_RAY'),
    ('Magic Ling deferred damage prolong', r'\b(deferred|DeferedDamage\.class\s*\))\s*\.prolong\(', r'\1.extend('),
    ('Magic Ling soul mark proc', r'(?<![\w.])processSoulMark\(', 'wandProc('),
    ('Magic Ling toxic vent bolt', r'\bMagicMissile\.TOXIC_VENT\b', 'MagicMissile.CORROSION'),
    ('Magic Ling debuff down icon', r'\bBuffIndicator\.DEBUFF_DOWN\b', 'BuffIndicator.WEAKNESS'),
    ('Magic Ling rose shield sprite state', r'\bState\.ROSESHIELDED\b', 'State.SHIELDED'),
    ('Magic Ling 7x7 circle list', r'\bPathFinder\.CIRCLE7\b', 'PathFinder.NEIGHBOURS25'),
    ('Magic Ling Lena anger', r'(?m)^[ \t]*(?:Dungeon\.)?hero\.FireAngry\([^;]*\);[ \t]*\r?\n', ''),
    ('Magic Ling white day mode', r'\bDungeon\.whiteDaymode\b', 'false'),
    ('Magic Ling drop loops that never end in a cramped spot', r'(int ofs;\s*)do \{(\s*ofs = [^;]+;\s*)\} while \(!Dungeon\.level\.passable\[pos \+ ofs\]\);', r'\1int ofsTries = 50;\n            do {\2} while (!Dungeon.level.passable[pos + ofs] && --ofsTries > 0);\n            if (ofsTries <= 0) ofs = 0;'),
    ('Magic Ling DLC difficulty level', r'(?<![\w.])(?:com\.shatteredpixel\.shatteredpixeldungeon\.)?Statistics\.difficultyDLCEXLevel\b', 'com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.difficultyDLCEXLevel'),
    ('Magic Ling final move override', r'public void move\(\s*int (\w+)\s*\)(\s*\{\s*super\.move\()\1\);', r'public void move(int \1, boolean travelling)\2\1, travelling);'),
    ('Magic Ling green mimic sprite', r'\bMimicSprite\.Green\b', 'com.shatteredpixel.shatteredpixeldungeon.sprites.MagicLingSprites.Green'),
    ('Magic Ling glass chain effect', r'\bEffects\.Type\.GLASSCHAIN\b', 'Effects.Type.CHAIN'),
    ('Magic Ling dragon king breath potion', r'\bnew\s+PotionOfDragonKingBreath\(', 'new com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDragonsBreath('),
    ('Magic Ling crivus teleporter and goo fight counters', r'(?<![\w.])(?:com\.shatteredpixel\.shatteredpixeldungeon\.)?Statistics\.(CrivusbossTeleporter|gooFight)\b', r'com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.\1'),
    ('Magic Ling static imports of later counters', r'\bimport\s+static\s+com\.shatteredpixel\.shatteredpixeldungeon\.Statistics\.(gooFight|CrivusbossTeleporter|RandMode|NoTime|TrueYogNoDied|winGame|difficultyDLCEXLevel);', r'import static com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.\1;'),
    ('Magic Ling bare salt water tile', r'(?<![\w.])SALT_WATER\b', 'Terrain.WATER'),
    ('Magic Ling static import of salt water', r'(?m)^import\s+static\s+com\.shatteredpixel\.shatteredpixeldungeon\.levels\.Terrain\.SALT_WATER;[ \t]*\r?\n', ''),
    ('Magic Ling property changes on another monster', r'\b(?!this\b)(\w+)\.properties\.(add|remove)\(', lambda m: m.group(1) + ('.addProperty(' if m.group(2) == 'add' else '.removeProperty(')),
    ('Magic Ling water soul elixir', r'\bnew\s+WaterSoul\(\)', 'new com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.AquaBrew()'),
    ('Magic Ling water soul import', r'(?m)^import\s+[\w.]+\.WaterSoul;[ \t]*\r?\n', ''),
    ('Magic Ling frost fire bolt', r'\bMagicMissile\.FROSTFIRE\b', 'MagicMissile.FROST'),
    ('Magic Ling plain sign tiles', r'(?<![\w.])(?:Terrain\.)?SIGN\b(?!_)', 'Terrain.EMPTY_DECO'),
    ('Magic Ling lock sword as a stand in weapon', r'\bnew\s+LockSword\(\)', 'new com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatsword()'),
    ('Magic Ling lock sword import', r'(?m)^import\s+[\w.]+\.LockSword;[ \t]*\r?\n', ''),
    ('Magic Ling stone curse as a damage source', r'\bnew\s+Stone\(\)', 'this'),
    ('Magic Ling stone curse import', r'(?m)^import\s+com\.shatteredpixel\.shatteredpixeldungeon\.items\.armor\.curses\.Stone;[ \t]*\r?\n', ''),
    ('Magic Ling hollow counters', r'(?<![\w.])(?:com\.shatteredpixel\.shatteredpixeldungeon\.)?Statistics\.(RepaierTowerCount|miniGamesTotalLevel|getMoveBoxScore|readBooks|getPacManScore|getAlLSearchScore|moveBoxScoreMax|godGirl)\b', r'com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.\1'),
    ('Magic Ling minigame records', r'\bSPDSettings\.(MoveBoxScore|PacManScore)\(', r'com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.\1('),
    ('Magic Ling minigame and book badges', r'\bBadges\.(?:valiReadBooks|MINIGAME_TOTAL)\([^;]*\)\s*;', ';'),
    ('Magic Ling coloured chains', r'\bEffects\.Type\.(?:RED|M|D|P|L)_CHAIN\b', 'Effects.Type.CHAIN'),
    ('Magic Ling old day flag set', r'(?m)^[ \t]*[\w.]+\.isOldDay\s*=[^=;]*;[ \t]*\r?\n', ''),
    ('Magic Ling old day flag', r'\b\w+\.isOldDay\b', 'false'),
    ('Magic Ling password badge lists', r'!\s*\(\s*passwordbadges\.contains\([^;{]*?\)\s*\)', 'false'),
    ('Magic Ling password badges unlock town characters', r'\bpasswordbadges\.contains\(\s*PaswordBadges\.Badge\.\w+\s*\)', 'true'),
    ('Magic Ling password badge list fetch', r'(?m)^[ \t]*(?:public\s+|private\s+)?List<PaswordBadges\.Badge>\s+passwordbadges\s*=\s*PaswordBadges\.filtered\(\s*true\s*\);[ \t]*\r?\n', ''),
    ('Magic Ling password challenge badges', r'\bPaswordBadges\.(?!filtered\b)\w+\([^;]*\)\s*;', ';'),
    ('Magic Ling alt magic missile wand', r'\b(?:WandOfMagicMissile\.)?AltWandOfMagicMissile\b', 'WandOfMagicMissile'),
    ('Magic Ling champion roll', r'\bChampionEnemy\.rollForStateLing\(', 'ChampionEnemy.rollForChampion('),
    ('Magic Ling champions this game lacks, later', r'\bChampionEnemy\.(?:Small|DelayMob|AloneCity)\.class\b', 'ChampionEnemy.Blazing.class'),
    ('Magic Ling minigame and blessing icons', r'\bBuffIndicator\.(?:PACMAN_GAME|BOX_GAME|ALL_SEARCH|SLICE_BLESS|LANTERFIRE_FIVE)\b', 'BuffIndicator.MIND_VISION'),
    ('Magic Ling true invisible sprite state', r'\bState\.TRUE_INVISIBLE\b', 'State.INVISIBLE'),
    ('Magic Ling fade to black or white', r'\bGameScene\.fadeTo(Black|White)\([^;]*\)\s*;', lambda m: 'GameScene.flash(' + ('0x000000' if m.group(1) == 'Black' else '0xFFFFFF') + ');'),
    ('Magic Ling heart bolt', r'\bMagicMissile\.HEART_ANMY\b', 'MagicMissile.SHADOW'),
    ('Magic Ling props score', r'\bGameRules\.\w+\([^;]*\)\s*;', ';'),
    ('Magic Ling game rules import', r'(?m)^import\s+com\.shatteredpixel\.shatteredpixeldungeon\.GameRules;[ \t]*\r?\n', ''),
    ('Magic Ling lava tiles are embers here', r'\bTerrain\.LAVA\b', 'Terrain.EMBERS'),
    ('Magic Ling boss scores past the fifth', r'(?m)^[ \t]*Statistics\.bossScores\[(?:[5-9]|\d\d+)\]\s*\+=[^;]*;[ \t]*\r?\n', ''),
    ('Magic Ling Kusumi checks', r'\s*\|\|\s*\w+\s+instanceof\s+KusumiMagicGirl\b', ''),
    ('Magic Ling Kusumi import', r'(?m)^import\s+[\w.]+\.KusumiMagicGirl;[ \t]*\r?\n', ''),
    ('Magic Ling endless mode flag set', r'(?m)^[ \t]*[\w.()]*isEndLess\s*=[^=;]*;[ \t]*\r?\n', ''),
    ('Magic Ling endless mode flag', r'\b(?:\w+\.)?isEndLess\b', 'false'),
    ('Magic Ling chaic blood counter', r'(?<![\w.])(?:com\.shatteredpixel\.shatteredpixeldungeon\.)?Statistics\.ChaicBlood\b', 'com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.ChaicBlood'),
    ('Magic Ling frost duration', r'\bFrost\.duration\([^)]*\)', 'Frost.DURATION'),
    ('Magic Ling legend and thanks item tags', r'\s*,\s*(?:Item\.)?(?:LengedsItem|ThanksItem|AnimationItem)\b|\s+implements\s+(?:Item\.)?(?:LengedsItem|ThanksItem|AnimationItem)\b(?!\s*,)', ''),
    ('Magic Ling item unlock setting', r'\bSPDSettings\.isItemUnlock\((?:[^()]|\([^()]*\))*\)', 'true'),
    ('Magic Ling cursed down icon', r'\bBuffIndicator\.CURSED_DOWN\b', 'BuffIndicator.WEAKNESS'),
    ('Magic Ling item level field set', r'\b(w|wep|weapon|item|wand|ring|armor|staff|it|i|n|r1|r2|r3|x3|mw4|w5)\.level\s*=\s*([^;=][^;]*);', r'\1.level(\2);'),
    ('Magic Ling fields from stats-shim 67963', r'(?<![\w.])(?:com\.shatteredpixel\.shatteredpixeldungeon\.)?Statistics\.(BzmdrCJHeroSTR|BzmdrCJHeroViewDistance|CatFirst|OnlyBloodUpgrade|PinkFox|gdzHelpDungeon|hcDialogLevel|moonlowgetAloneRoom|onlyLing|zeroItemLevel)\b', r'com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.\1'),
    ('Magic Ling settings from stats-shim 65055', r'(?<![\w.])(?:com\.shatteredpixel\.shatteredpixeldungeon\.)?SPDSettings\.(CatSee|RecordFlowerChallengs|SmallLeafGetCoin|SmallLeafHCGetCoin|blood|currentBGM|unlockItem)\(', r'com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.\1('),
    ('Magic Ling developer build checks', r'\bDeviceCompat\.(?:isMDP|isDesktop_Dev)\(\)', 'false'),
    ('Magic Ling ice coin heaps sell for gold', r'\bHeap\.Type\.FOR_ICE\b', 'Heap.Type.FOR_SALE'),
    ('Magic Ling holiday static imports', r'(?m)^import\s+static\s+com\.shatteredpixel\.shatteredpixeldungeon\.levels\.RegularLevel\.(?:chinaHoliday|holiday|birthday|westHoliday);[ \t]*\r?\n', ''),
    ('Magic Ling bare holiday checks', r'(?<![\w.])(?:chinaHoliday|holiday|birthday)\s*==\s*(?:RegularLevel\.)?(?:ChinaHoliday|WestHoliday|DevBirthday)\.\w+', 'false'),
    ('Magic Ling qualified holiday checks', r'\bRegularLevel\.(?:chinaHoliday|holiday|birthday)\s*==\s*RegularLevel\.(?:ChinaHoliday|WestHoliday|DevBirthday)\.\w+', 'false'),
    ('Magic Ling random chests in shops', r'\bnew\s+RandomChest\(\)', 'com.shatteredpixel.shatteredpixeldungeon.items.Generator.random()'),
    ('Magic Ling random chest import', r'(?m)^import\s+[\w.]+\.RandomChest;[ \t]*\r?\n', ''),
    ('Magic Ling smoker sprite state', r'\bState\.SMOKER\b', 'State.BURNING'),
    ('Magic Ling ice heal on the hero', r'\b(\w+)\.healIcehp\(([^;]*)\);', r'\1.HP = Math.min(\1.HT, \1.HP + (\2));'),
    ('Magic Ling android game records', r'\bAndroidGameRecords\.\w+\([^;]*\)\s*;', ';'),
    ('Magic Ling android records import', r'(?m)^import\s+com\.shatteredpixel\.shatteredpixeldungeon\.android\.\w+;[ \t]*\r?\n', ''),
    ('Magic Ling town and story badges', r'\bBadges\.isUnlocked\(\s*Badges\.Badge\.(?:HOLLOWCITY|GOOD_MAKER|RLPT_WIN|KILL_MG|KILL_FIREGIRL|NYZ_SHOP|ALLCHSX|GODCHSX|GOOD_BLUE)\s*\)', 'false'),
    ('Magic Ling rice liquor pasty', r'\bnew\s+Pasty\.RiceLiquor\(\)', 'new Pasty()'),
    ('Magic Ling lit bomb flag', r'(?m)^[ \t]*\w+\.isLit\s*=\s*\w+;[ \t]*\r?\n', ''),
    ('Magic Ling bomb fuse helper', r'\b(\w+)\.createFuse\(\)\.ignite\(', 'new Bomb.Fuse().ignite('),
    ('Magic Ling time flower gift', r'\bnew\s+TimeFlower\(\)', 'com.shatteredpixel.shatteredpixeldungeon.items.Generator.random()'),
    ('Magic Ling time flower import', r'(?m)^import\s+[\w.]+\.TimeFlower;[ \t]*\r?\n', ''),
    ('Magic Ling dialog chrome', r'\bdiagulewindow\.chrome\.', 'diagulewindow.chromeBox().'),
    ('Magic Ling blessed ankh constructor', r'\bnew\s+Ankh\(\s*true\s*\)', 'new com.shatteredpixel.shatteredpixeldungeon.items.Ankh()'),
    ('Magic Ling random prop with a tier', r'\bProp\.randomPropA\(\s*\d+\s*,', 'Prop.randomPropA('),
    ('Trap Dungeon animation speed setting', r'SPDSettings\.speeder2?\(\)', '1f'),
    ('Trap Dungeon exotic scroll category', r'Generator\.Category\.EXOTICSCROLL\b', 'Generator.Category.SCROLL'),
    ('SPS Ballistica STOP_TERRAIN is STOP_SOLID', r'Ballistica\.STOP_TERRAIN', 'Ballistica.STOP_SOLID'),
    ('SPS challenge floor kill counters live in SpsStats', r'\bStatistics\.(albinoPiranhasKilled|skeletonsKilled|archersKilled|goldThievesKilled|assassinsKilled)\b', r'com.shatteredpixel.shatteredpixeldungeon.SpsStats.\1'),
    ('SPS ScrollOfPsionicBlast is an exotic scroll here', r'items\.scrolls\.ScrollOfPsionicBlast;', 'items.scrolls.exotic.ScrollOfPsionicBlast;'),
    ('SPS damage types by full name, Char has its own DamageType enum that hides the import in monsters', r'(?<![\w.])DamageType\.(?=[A-Z])', 'com.shatteredpixel.shatteredpixeldungeon.actors.damagetype.DamageType.'),
    ('SPS WandOfFlow.throwChar is the blast wave throw', r'WandOfFlow\.throwChar\(\s*(\w+)\s*,\s*(\w+)\s*,\s*(\w+)\s*\)', r'com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave.throwChar(\1, \2, \3, false, true, this)'),
    ('SPS KindOfArmor is Armor', r'\bKindOfArmor\b', 'Armor'),
    ('SPS randomRespawnCellMob is randomRespawnCell', r'\.randomRespawnCellMob\(\)', '.randomRespawnCell(null)'),
    ('SPS Actor.freeCell has no use here', r'\n[ \t]*Actor\.freeCell\(\s*\w+\s*\);', ''),
    ('SPS TEST_TIME challenge does not exist', r'Dungeon\.isChallenged\(\s*Challenges\.TEST_TIME\s*\)', 'false'),
    ('SPS boss killed flags on Dungeon', r'\n[ \t]*Dungeon\.\w+killed\s*=\s*true;', ''),
    ('SPS escape knife missile picture', r'new EscapeKnive\(\)', 'new com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife()'),
    ('SPS poison dart missile picture', r'new PoisonDart\(\)', 'new com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.PoisonDart()'),
    ('SPS wave missile picture', r'new Wave\(\)', 'new com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Shuriken()'),
    ('SPS skull missile picture', r'new Skull\(\)', 'new com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone()'),
    ('SPS bola missile picture', r'new EmpBola\(\)', 'new com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Bolas()'),
    ('SPS old high grass is high grass', r'Terrain\.OLD_HIGH_GRASS', 'Terrain.HIGH_GRASS'),
    ('SPS shrub is high grass', r'Terrain\.SHRUB\b', 'Terrain.HIGH_GRASS'),
    ('SPS ground A tile is special floor', r'Terrain\.GROUND_A\b', 'Terrain.EMPTY_SP'),
    ('SPS glass wall shows as a wall', r'Terrain\.GLASS_WALL\b', 'Terrain.WALL'),
    ('SPS NEIGHBOURS4OUT are the four diagonals', r'PathFinder\.NEIGHBOURS4OUT', 'new int[]{ +1 + com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.width(), +1 - com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.width(), -1 + com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.width(), -1 - com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.width() }'),
    ('SPS static Level.insideMap', r'(?<![\w.])Level\.insideMap\(', 'Dungeon.level.insideMap('),
    ('SPS bomb pictures we lack', r'ItemSpriteSheet\.(HUGE_BOMB|MINI_BOMB)\b', 'ItemSpriteSheet.BOMB'),
    ('Magic Ling blue potion icon', r'\bItemSpriteSheet\.Icons\.POTION_BLUE\b', 'ItemSpriteSheet.Icons.POTION_DRGBREATH'),
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
    # Deep PD, written on the 0.4 base where Level fields were static
    ('Deep, Level.water/passable/solid/flamable static arrays', r'\bLevel\.(water|passable|solid|flamable|avoid|losBlocking|pit)\b(?=\[|\.clone|;|\))', r'Dungeon.level.\1'),
    ('Deep, Level.fieldOfView is heroFOV', r'\bLevel\.fieldOfView\b', 'Dungeon.level.heroFOV'),
    ('Old immunities()/resistances() methods became fields', r'@Override\s+public\s+HashSet<Class<\?>>\s+(immunities|resistances)\s*\(\s*\)\s*\{\s*return\s+(\w+)\s*;\s*\}', r'{ \1.addAll( \2 ); }'),
    ('Deep, King.Undead.count',r'King\.Undead\.count', '0'),
    ('Deep, sprite folders flattened into sprites.deep', r'sprites\.(?:CavesMobsSprites|ForestMobsSprites|GnollMobsSprites|PrisonMobSprites|SewerMobSprites)\.(DarkRat|Goolem|LesserRat|Sludge|GnollKnight|GnollMarksman|BlueSoulBomb|RedSoulBomb|ShamanLeader|FemaleGangster|MaleGangster|GangBoss|NewbornElemental|Terminator|PurpleMushroom|RedMushroom|YellowMushroom|ElderShroom|Goblin)Sprite\b', r'sprites.deep.\1Sprite'),
    ('Deep, sprites kept at the top of sprites are in sprites.deep here', r'sprites\.(Assassin|GnollThiefBoss|GnollThief|Grieflord|Idol|KeyCreature|Mandrake|FlyingCat)Sprite\b', r'sprites.deep.\1Sprite'),
    ('Deep, other nested sprite folders hold Shattered sprites', r'sprites\.(?:CavesMobsSprites|ForestMobsSprites|GnollMobsSprites|PrisonMobSprites|SewerMobSprites|CityMobsSprites|HallsMobsSprites|MiscMobsSprites)\.(\w+)', r'sprites.\1'),
]

def convert_long(s):
    """Mods built for huge numbers (Experienced) use long for damage. This game uses int."""
    if not re.search(r'\blong\s+(?:min|max|damageRoll|proc|STRReq|defenseFactor)\b|long\s+damage\b', s):
        return s
    s = re.sub(r'\blong\b', 'int', s)
    s = re.sub(r'\bLong\b', 'Integer', s)
    return re.sub(r'(\d)L\b', r'\1', s)

def apply(s, sps=False):
    """Returns the fixed text and the list of reasons that fired."""
    fired = []
    t = convert_long(s)
    if t != s: fired.append('long damage became int')
    # the Magic Ling rows sit in SPS_RULES but are for Magic Ling code, so they always run
    rules = SPS_RULES if (sps or 'com.hmdzl.spspd' in s) else [r for r in SPS_RULES if r[0].startswith('Magic Ling')]
    for why, pat, rep in rules + RENAMES:
        t2 = re.sub(pat, rep, t)
        if t2 != t: fired.append(why); t = t2
    # old style resistance and immunity sets (Pixel Spacebase) become the fields of this game's Char
    for _kind, _field in (('RESISTANCES', 'resistances'), ('IMMUNITIES', 'immunities')):
        _m = re.search(r'private static final HashSet<Class<\?>> ' + _kind + r' = new HashSet<>\(\);\s*static \{(.*?)\n    \}', t, re.S)
        if _m:
            body = _m.group(1).replace(_kind + '.add(', _field + '.add(')
            t = t.replace(_m.group(0), '{' + body + '\n    }')
            t = re.sub(r'\n\s*@Override\s*public HashSet<Class<\?>> ' + _field + r'\(\)\s*\{\s*return ' + _kind + r';\s*\}', '', t)
            # a 0.3 rule above may already have turned the override into an addAll of the set that is now gone
            t = re.sub(r'\n\s*\{ ' + _field + r'\.addAll\( ' + _kind + r' \); \}', '', t)
            fired.append('old ' + _kind + ' set became the ' + _field + ' field')
    if re.search(r'\bDungeon\.', t) and not re.search(r'import [\w.]*\.Dungeon;', t) and 'package ' + PKG + ';' not in t:
        t = re.sub(r'(package [\w.]+;\r?\n)', r'\1\nimport ' + PKG + '.Dungeon;', t, count=1)
        fired.append('added the Dungeon import')
    # the old Skeleton Key that boss levels dropped is the Worn Key now
    if 'items.keys.SkeletonKey' in t:
        t = re.sub(r'\bSkeletonKey\b', 'WornKey', t)
        fired.append('old SkeletonKey is WornKey')
    # a rule above can add a Char parameter to a file that never imported Char
    if re.search(r'\bChar\b', t) and not re.search(r'import [\w.]*\.Char;', t) and 'package ' + PKG + '.actors;' not in t:
        t = re.sub(r'(package [\w.]+;\r?\n)', r'\1\nimport ' + PKG + '.actors.Char;', t, count=1)
        fired.append('added the Char import')
    if re.search(r'\bDungeon\.', t) and not re.search(r'import [\w.]*\.Dungeon;', t) and not re.search(r'package ' + re.escape(PKG) + r';', t):
        t = re.sub(r'(package [\w.]+;\r?\n)', r'\1\nimport ' + PKG + '.Dungeon;', t, count=1)
        fired.append('added the Dungeon import')
    if 'Random.' in t and 'import com.watabou.utils.Random;' not in t:
        t = re.sub(r'(package [\w.]+;\r?\n)', r'\1\nimport com.watabou.utils.Random;', t, count=1)
        fired.append('added the Random import')
    if re.search(r'\bDungeon\.', t) and not re.search(r'import [\w.]*\.Dungeon;', t) and 'package ' + PKG + ';' not in t:
        t = re.sub(r'(package [\w.]+;\r?\n)', r'\1\nimport ' + PKG + '.Dungeon;', t, count=1)
        fired.append('added the Dungeon import')
    if re.search(r'\bPathFinder\.', t) and 'import com.watabou.utils.PathFinder;' not in t:
        t = re.sub(r'(package [\w.]+;\r?\n)', r'\1\nimport com.watabou.utils.PathFinder;', t, count=1)
        fired.append('added the PathFinder import')
    if sps or 'com.hmdzl.spspd' in s:
        t, f2 = sps_fixups(t)
        fired += f2
    return t, fired


_CONST_CACHE = {}
def _our_constants(rel, kind):
    key = (rel, kind)
    if key not in _CONST_CACHE:
        import os
        p = os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', 'core', 'src', 'main', 'java', *PKG.split('.'), *rel.split('/'))
        try:
            txt = open(p, encoding='utf-8', errors='replace').read()
        except OSError:
            txt = ''
        if kind == 'int':
            _CONST_CACHE[key] = set(re.findall(r'(?:public\s+)?static\s+final\s+int\s+(\w+)\s*=', txt))
        else:
            m = re.search(r'enum\s+\w+\s*\{(.*?)[;}]', txt, re.S)
            _CONST_CACHE[key] = set(re.findall(r'\b([A-Z][A-Z0-9_]+)\b', m.group(1))) if m else set()
    return _CONST_CACHE[key]

_BUFF_NAMES = None
def _our_buff_icons():
    global _BUFF_NAMES
    if _BUFF_NAMES is None:
        import os
        p = os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', 'core', 'src', 'main', 'java', *PKG.split('.'), 'ui', 'BuffIndicator.java')
        try:
            _BUFF_NAMES = set(re.findall(r'public static final int (\w+)\s*=', open(p, encoding='utf-8', errors='replace').read()))
        except OSError:
            _BUFF_NAMES = set()
    return _BUFF_NAMES

def sps_fixups(t):
    """SPS only. Buff icons we do not have become no icon, and the imports its short files leave out are added."""
    fired = []
    for cls, rel, fallback, kind in (('BuffIndicator', 'ui/BuffIndicator.java', 'NONE', 'int'), ('Speck', 'effects/Speck.java', 'STENCH', 'int'),
                                     ('CharSprite', 'sprites/CharSprite.java', 'NEGATIVE', 'int'), ('HeroSubClass', 'actors/hero/HeroSubClass.java', 'null', 'enum')):
        names = _our_constants(rel, kind)
        if names:
            t2 = re.sub(r'\b' + cls + r'\.([A-Z][A-Z0-9_]+)\b', lambda m: m.group(0) if m.group(1) in names else (cls + '.' + fallback if fallback != 'null' else 'null'), t)
            if t2 != t: fired.append('SPS ' + cls + ' constant we lack'); t = t2
    pk = re.search(r'package ([\w.]+);', t)
    pkg = pk.group(1) if pk else ''
    for sym, imp, pat in (('Dungeon', PKG + '.Dungeon', r'\bDungeon\.'), ('PathFinder', 'com.watabou.utils.PathFinder', r'\bPathFinder\.'),
                          ('DungeonTilemap', PKG + '.DungeonTilemap', r'\bDungeonTilemap\.|\bnew DungeonTilemap'),
                          ('Level', PKG + '.levels.Level', r'\bLevel\b(?!\s*\()')):
        if re.search(pat, t) and not re.search(r'import [\w.]*\.' + sym + r';', t) and pkg + '.' + sym != imp and not re.search(r'class ' + sym + r'\b', t):
            t = re.sub(r'(package [\w.]+;\r?\n)', r'\1\nimport ' + imp + ';\n', t, count=1)
            fired.append('SPS added the ' + sym + ' import')
    return t, fired
