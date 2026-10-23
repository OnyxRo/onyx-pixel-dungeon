"""Copies DERanged PD's English text for what was ported (2026-10-22), with the key renames this port made.

  python -I scripts/deranged-strings.py [--dry]

Reads the mod's new_messages bundle first and messages second (as the mod does), keeps keys that start with one of
PREFIXES, renames them with RENAMES, skips keys this game already has, and appends the rest to the matching file
under messages/ with a comment naming the mod. Prints keys it could not find.
"""
import io, os, re, sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
OURS = os.path.join(ROOT, 'core', 'src', 'main', 'assets', 'messages')
MOD = os.path.join(os.path.expanduser('~'), 'mod-sources', 'deranged', 'core', 'src', 'main', 'assets')

KING = ('royal_privilege royal_focus royal_intuition royal_perceptivity kings_wisdom ambush noble_cause well_protected '
        'extra_power extra_bulk royal_meal royal_feast restoration temporary_draught power_within perfect_collection '
        'kings_vision kings_wrath pursuit the_protector energy_surge noble_call aftershock rat_blast smoke_and_mirrors '
        'sea_of_blades').split()

PREFIXES = [
    'actors.buffs.warp.', 'actors.buffs.scam.', 'actors.buffs.nodeath.', 'actors.buffs.amnesiabuff.',
    'actors.buffs.hpdebuff.', 'actors.buffs.warpedenemy.', 'actors.buffs.powerfuldegrade.',
    'actors.hero.abilities.ratking.', 'actors.hero.heroclass.rat_king', 'actors.hero.herosubclass.king',
    'actors.hero.talent.rk_', 'actors.mobs.darkslime.', 'actors.mobs.ech.', 'actors.mobs.ech$',
    'actors.mobs.npcs.mysterymerchant.', 'items.weapon.melee.rka.', 'items.bags.cheesycheest.',
    'items.potions.elixirs.donotdieelixir.', 'items.potions.elixirs.kromerpotion.', 'items.quest.kromer.',
    'items.quest.redcrystal.', 'items.spells.kromerscroll.', 'items.spells.pipisfusion.', 'items.spells.scammingspell.',
    'items.weapon.melee.meleeweapon.cursed_kromer', 'utils.warppile', 'challenges.forget_path', 'challenges.no_hp',
    'badges$badge.mastery_rat_king', 'badges$badge.boss_slain_1_rat_king', 'badges$badge.victory_rat_king',
] + ['actors.hero.talent.' + k + '.' for k in KING]

RENAMES = [
    (r'^actors\.buffs\.warp\.', 'actors.buffs.warpdebuff.'),
    (r'^actors\.buffs\.scam\.', 'actors.buffs.scamdebuff.'),
    (r'^actors\.buffs\.nodeath\.', 'actors.buffs.kromernodeath.'),
    (r'^actors\.hero\.abilities\.ratking\.wrath\.', 'actors.hero.abilities.ratking.kingswrath.'),
    (r'^actors\.hero\.talent\.(' + '|'.join(KING) + r')\.', r'actors.hero.talent.rk_\1.'),
]

def LP(p):
    p = os.path.abspath(p)
    return p if p.startswith('\\\\') else '\\\\?\\' + p

def read_props(path):
    out = []
    if not os.path.exists(LP(path)): return out
    lines = io.open(LP(path), encoding='utf-8').read().split('\n')
    i = 0
    while i < len(lines):
        line = lines[i].rstrip('\r')
        full = line
        while full.endswith('\\') and not full.endswith('\\\\') and i + 1 < len(lines):
            i += 1
            full = full + '\n' + lines[i].rstrip('\r')
        i += 1
        if not full.strip() or full.lstrip().startswith('#') or '=' not in full: continue
        k, v = full.split('=', 1)
        out.append((k.strip(), v))
    return out

def main():
    dry = '--dry' in sys.argv
    files = {}
    for bundle in ('new_messages', 'messages'):
        base = os.path.join(MOD, bundle)
        for sub in sorted(os.listdir(LP(base))):
            p = os.path.join(base, sub, sub + '.properties')
            for k, v in read_props(p):
                if not any(k.startswith(x) for x in PREFIXES): continue
                nk = k
                for a, b in RENAMES: nk = re.sub(a, b, nk)
                files.setdefault(sub, {})
                if nk not in files[sub]: files[sub][nk] = v
    total = 0
    for sub, kv in files.items():
        ours = os.path.join(OURS, sub, sub + '.properties')
        have = set(k for k, _ in read_props(ours))
        new = [(k, v) for k, v in kv.items() if k not in have]
        if not new: continue
        total += len(new)
        print(sub, len(new))
        if dry: continue
        s = io.open(ours, encoding='utf-8', newline='').read()
        nl = '\r\n' if '\r\n' in s else '\n'
        add = nl + '###deranged, DERanged PD' + nl + nl.join(k + '=' + v.replace('\n', nl) for k, v in new) + nl
        if not s.endswith(nl): add = nl + add
        io.open(ours, 'w', encoding='utf-8', newline='').write(s + add)
    print('total', total)

main()
