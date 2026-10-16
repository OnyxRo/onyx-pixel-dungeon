package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.dragonknight;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

//from Dragonic PD, the Dragon Knight's armor ability. Every enemy in sight bleeds and is crippled, and the hero loses a third of their health.
public class DeafeningRoar extends ArmorAbility {

	{
		baseChargeUse = 35f;
	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {
		boolean any = false;
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (mob.alignment == Char.Alignment.ALLY || !Dungeon.level.heroFOV[mob.pos]) continue;
			any = true;
			if (mob.buff(Bleeding.class) == null && !mob.properties().contains(Char.Property.BOSS)) {
				Buff.affect(mob, Bleeding.class).set(Math.max(1, 2 * mob.HP / 7));
			}
			Buff.prolong(mob, Cripple.class, Cripple.DURATION - 1);
		}
		if (!any) {
			GLog.w(Messages.get(this, "no_enemies"));
			return;
		}

		armor.charge -= chargeUse(hero);
		armor.updateQuickslot();

		hero.HP -= hero.HP / 3;
		if (hero.HP < 1) hero.HP = 1;

		hero.sprite.zap(hero.pos);
		hero.spendAndNext(Actor.TICK);
	}

	@Override
	public int icon() {
		return HeroIcon.SHOCKWAVE;
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.EXPANDING_WAVE, Talent.SHOCK_FORCE, Talent.HEROIC_ENERGY};
	}
}
