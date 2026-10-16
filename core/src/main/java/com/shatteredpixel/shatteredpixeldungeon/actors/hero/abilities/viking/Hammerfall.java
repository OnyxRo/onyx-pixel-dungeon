package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.viking;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Fortify;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

//from Dragonic PD, the Viking's armor ability. It costs a third of the hero's health and fortifies for a while.
public class Hammerfall extends ArmorAbility {

	{
		baseChargeUse = 35f;
	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {
		armor.charge -= chargeUse(hero);
		armor.updateQuickslot();

		Buff.prolong(hero, Fortify.class, Fortify.DURATION);
		hero.HP -= hero.HP / 3;
		if (hero.HP < 1) hero.HP = 1;

		hero.sprite.operate(hero.pos);
		hero.spendAndNext(Actor.TICK);
	}

	@Override
	public int icon() {
		return HeroIcon.ENDURE;
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.SHRUG_IT_OFF, Talent.EVEN_THE_ODDS, Talent.HEROIC_ENERGY};
	}
}
