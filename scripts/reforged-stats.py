"""Turns a Reforged style monster stat table into the fields Shattered's Mob class uses.

  python -I scripts/reforged-stats.py <reforged folder> Kobold Spitter ...

Reforged keeps every monster's numbers in Constants.mobs.<name> and its loot in Constants.loot.<name>
instead of in the monster class. This prints, for each name, the lines to paste into the instance block
and the damageRoll, attackSkill and drRoll methods. The damage types are dropped, Shattered has none.
Check by hand. maxLvl of -1 or -2 in Reforged means something other than a cap, pick a level from the
depth the monster belongs to. Loot is printed as Reforged wrote it.
"""
import sys, re, io, glob, os
mod = sys.argv[1]
names = sys.argv[2:]
hits = glob.glob(mod + '/core/src/main/java/**/Constants.java', recursive=True)
if not hits:
    print('no Constants.java found'); sys.exit(1)
s = io.open(hits[0], encoding='utf-8', errors='replace').read()
i = s.index('abstract static class mobsBase')
mobs = s[i:]
loot_i = s.index('public static class loot') if 'public static class loot' in s else None
PROP = {'Undead': 'UNDEAD', 'Demonic': 'DEMONIC', 'Inorganic': 'INORGANIC', 'Fiery': 'FIERY', 'Icy': 'ICY',
        'Acidic': 'ACIDIC', 'Electric': 'ELECTRIC', 'Large': 'LARGE', 'Immovable': 'IMMOVABLE', 'Static': 'STATIC',
        'Boss': 'BOSS', 'Miniboss': 'MINIBOSS', 'Boss_Minion': 'BOSS_MINION'}
for n in names:
    try:
        k = mobs.index('public static class %s extends mobsBase' % n)
    except ValueError:
        print('// %s: not in the table' % n); continue
    e = mobs.index('public static ', k + 60)
    b = mobs[k:e]
    v = {}
    for g in re.finditer(r'public \S+ get(\w+)\(\)\s*\{\s*return ([^;]+);', b):
        v[g.group(1)] = g.group(2).strip()
    num = lambda key, d='0': v.get(key, d)
    print('// ---- ' + n)
    print('\t\tHP = HT = %s;' % num('HP'))
    if num('DefenseSkill') != '0': print('\t\tdefenseSkill = %s;' % num('DefenseSkill'))
    if num('MovementSpd', '1.0f') not in ('1.0f', '1.00f'): print('\t\tbaseSpeed = %s;' % num('MovementSpd'))
    print('\t\tEXP = %s;' % num('XP'))
    print('\t\tmaxLvl = %s;   // reforged says %s, check' % (num('MaxLvl'), num('MaxLvl')))
    if num('Flying', 'false') == 'true': print('\t\tflying = true;')
    for key, p in PROP.items():
        if v.get('Property' + key) == 'true': print('\t\tproperties.add(Property.%s);' % p)
    if num('AtkDelay', '1.0f') not in ('1.0f', '1.00f'): print('\t// attackDelay() should multiply by %s' % num('AtkDelay'))
    lo, hi = (num('MeleeDmgMin'), num('MeleeDmgMax')) if num('MeleeDmgMax') != '0' else (num('RangedDmgMin'), num('RangedDmgMax'))
    print('\n\t@Override public int damageRoll() { return Random.NormalIntRange( %s, %s ); }' % (lo, hi))
    print('\t@Override public int attackSkill( Char target ) { return %s; }' % num('AtkSkill'))
    if num('DmgReductionMax') != '0':
        print('\t@Override public int drRoll() { return super.drRoll() + Random.NormalIntRange( %s, %s ); }' % (num('DmgReductionMin'), num('DmgReductionMax')))
    lk = re.search(r'Loot=?', '')
    if loot_i:
        m = re.search(r'public static class %s extends lootBase\s*\{(.*?)\n        \}' % n, s[loot_i:], re.S)
        if m:
            vals = re.findall(r'return ([^;]+);', m.group(1))
            if vals and vals[0] != 'null':
                print('\t// loot %s, chance %s' % (vals[0], vals[1]))
    print()
