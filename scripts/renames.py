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
    ('SPS blob seed signature', r'public\s+void\s+seed\(\s*int\s+cell\s*,\s*int\s+amount\s*\)', 'public void seed( Level level, int cell, int amount ) { if (cur == null) cur = new int[level.length()]; if (off == null) off = new int[cur.length];'),
    ('SPS blob super seed', r'super\.seed\(\s*cell\s*,\s*amount\s*\)', 'super.seed( level, cell, amount )'),
    ('SPS floating text with an icon', r'showStatusWithIcon\(\s*[\w.]+\s*,\s*([^;]+?)\s*,\s*FloatingText2\.\w+\s*\)', r'showStatus( CharSprite.NEGATIVE, \1 )'),
    ('SPS belongings.discharge does not exist', r'[\w.]*belongings\.discharge\(\)\s*;', '//belongings.discharge() has no use here'),
    # Magic Ling PD gives every hit a damage type. Char here has the type enum and three argument calls, but overrides keep the plain form
    ('Magic Ling damage override with a type', r'(public|protected)(\s+)void(\s+)damage\(\s*int\s+(\w+)\s*,\s*Object\s+(\w+)\s*,\s*(?:Char\.)?DamageType\s+(\w+)\s*\)\s*\{', r'\1\2void\3damage( int \4, Object \5 ) {\n\t\tDamageType \6 = DamageType.PHYSICAL;'),
    ('Magic Ling attack override with a type', r'(public|protected)(\s+)boolean(\s+)attack\(\s*Char\s+(\w+)\s*,\s*float\s+(\w+)\s*,\s*float\s+(\w+)\s*,\s*float\s+(\w+)\s*,\s*(?:Char\.)?DamageType\s+(\w+)\s*\)\s*\{', r'\1\2boolean\3attack( Char \4, float \5, float \6, float \7 ) {\n\t\tDamageType \8 = DamageType.PHYSICAL;'),
    ('Magic Ling inner sprite classes of vanilla sprites', r'\b(?:BatSprite\.BatEDSprite|CrabSprite\.NewBornCrabSprite|MimicSprite\.Dimand)\b', lambda m: 'com.shatteredpixel.shatteredpixeldungeon.sprites.MagicLingSprites.' + m.group(0).split('.')[1]),
    ('Magic Ling counters kept in MagicLingStats', r'\bStatistics\.(survey_research_points|unLockedFireDargon|doNotLookLing|sakaBackStage|spawnersIce|spawnersTombTownAlive|lanterfireactive|crivusfruitslevel2|crivusfruitslevel3|dimandchestmazeCollected|skydeadFight|defalult_deaddog|bossRushMode|dwarfKill|killYogMobsAnargy|attackIFGirl|RandomQuest|GoldMobDead|GoldMobLimit|TPDoorDieds|KillMazeMimic|goldRefogreCount)\b', r'com.shatteredpixel.shatteredpixeldungeon.MagicLingStats.\1'),
    ('Magic Ling ancient city badge, not ported', r'\bBadges\.validateAncityProgress\(\)\s*;', ';'),
    ('Magic Ling red dragon quest, not ported', r'\bRedDragon\.Quest\.process\(\)\s*;', ';'),
    ('Magic Ling password badges, not ported', r'\bPaswordBadges\.\w+\(\)\s*;', ';'),
    ('Magic Ling DLC conducts are never on',r'\bDungeon\.isDLC\(\s*Conducts\.Conduct\.\w+\s*\)', 'false'),
    ('Magic Ling Random.Boolean',r'\bRandom\.Boolean\(\)', '(Random.Int(2) == 0)'),
    ('Magic Ling Terrain.SALT_WATER', r'\bTerrain\.SALT_WATER\b', 'Terrain.WATER'),
    ('Magic Ling mob.properties field', r'\b(mob|enemy|ch)\.properties\.contains', r'\1.properties().contains'),
    ('Magic Ling ColorTargetedCell', r'new ColorTargetedCell\(', 'com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell.colored('),
    ('Magic Ling Speck.ROT', r'\bSpeck\.ROT\b', 'Speck.TOXIC'),
    ('Magic Ling Beam.DeathRayS', r'\bBeam\.DeathRayS\b', 'Beam.DeathRay'),
    ('Magic Ling GameScene.scene.add', r'\bGameScene\.scene\.add\(', 'com.watabou.noosa.Game.scene().add('),
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
