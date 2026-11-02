package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerItemSprites;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

//tower, Tower PD's Curved Knife. The Duelist's ability sacrifices her, which ends the run and makes every curved
//knife stronger for good (up to six sacrifices, kept in the settings like the mod kept them). The mod's Seppuku
//badge is left out, badges are shared with every other port.
public class CurvedKnife extends MeleeWeapon {

	public static final int MAX_SACRIFICES = 6;
	private static final String SETTING = "td_knife_level";

	{
		image = TowerItemSprites.CURVED_KNIFE;
		hitSound = Assets.Sounds.HIT_STAB;
		hitSoundPitch = 1.1f;
		DLY = 0.7f;
		ACC = 1.2f;
		critChance = 0.05f;
		tier = 2;
		rarity = 2;
	}

	/** How many Duelists gave themselves to the knife, across all runs. */
	public static int sacrifices(){
		return Math.max( 0, Math.min( MAX_SACRIFICES, SPDSettings.getInt( SETTING, 0 ) ) );
	}

	@Override
	public int min(int lvl) {
		return Math.round(5 * damageModifier() + 3*lvl * damageModifier() + (int)Math.sqrt(sacrifices()));
	}

	@Override
	public int max(int lvl) {
		return Math.round(10 * damageModifier() + 4*lvl * damageModifier() + sacrifices());
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		if (level() > 0) Buff.affect( defender, Hex.class, 4 * level() );
		return super.proc(attacker, defender, damage);
	}

	@Override
	public int damageRoll(Char owner) {
		if (owner instanceof Hero) {
			Hero hero = (Hero) owner;
			Char enemy = hero.enemy();
			if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
				//70% toward max to max on a surprise attack
				int diff = max() - min();
				int damage = augment.damageFactor(Random.NormalIntRange(
						min() + Math.round(diff * 0.7f),
						max()));
				int exStr = hero.STR() - STRReq();
				if (exStr > 0) {
					damage += Random.IntRange(0, exStr);
				}
				return damage;
			}
		}
		return super.damageRoll(owner);
	}

	@Override
	protected int baseChargeUse(Hero hero, Char target) {
		return 2 * super.baseChargeUse(hero, target);
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		sacrifice( hero, this );
	}

	public static void sacrifice( Hero hero, MeleeWeapon wep ){
		wep.beforeAbilityUsed( hero, null );
		SPDSettings.put( SETTING, Math.min( sacrifices() + 1, MAX_SACRIFICES ) );
		Sample.INSTANCE.play( Assets.Sounds.HIT_STAB );
		wep.afterAbilityUsed( hero );
		GLog.n( Messages.get( CurvedKnife.class, "sacrificed" ) );
		hero.die( wep );
	}
}
