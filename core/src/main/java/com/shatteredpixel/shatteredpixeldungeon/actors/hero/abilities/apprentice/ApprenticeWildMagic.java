/*
 * Clairvoyant Pixel Dungeon, ported into Onyx's Pixel Dungeon (key clairvoyant)
 * https://github.com/ifritdiezel/Clairvoyant-Pixel-Dungeon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.apprentice;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.StableElemental;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

//Clairvoyant PD, the Apprentice's armor ability. One chaotic spell from a list.
//The mod left most of its slots empty; here each slot holds one of the effects it did write,
//plus the stable elemental its class armor called.
public class ApprenticeWildMagic extends ArmorAbility {

	{
		baseChargeUse = 35f;
	}

	@Override
	protected void activate( ClassArmor armor, Hero hero, Integer target ) {
		armor.charge -= chargeUse(hero);
		armor.updateQuickslot();
		Invisibility.dispel();

		int roll = Random.Int(8);
		GLog.p(Messages.get(this, "effect_" + roll));
		switch (roll) {
			case 0:
				Buff.affect(hero, Healing.class).setHeal(10, 0.25f, 0);
				break;
			case 1:
				Potion potion = (Potion) Generator.randomUsingDefaults(Generator.Category.POTION);
				if (potion != null) potion.apply(hero);
				break;
			case 2:
				ScrollOfTeleportation.teleportChar(hero);
				break;
			case 3:
				if (hero.belongings.weapon() instanceof Weapon) ((Weapon) hero.belongings.weapon()).enchant();
				break;
			case 4:
				StableElemental.summonAround(hero.pos);
				break;
			case 5:
				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
					if (Dungeon.level.heroFOV[mob.pos] && Random.Int(2) == 0) Buff.affect(mob, Burning.class).reignite(mob);
				}
				Buff.affect(hero, Burning.class).reignite(hero);
				break;
			case 6:
				Plant.Seed seed = (Plant.Seed) Generator.randomUsingDefaults(Generator.Category.SEED);
				if (seed != null) Dungeon.level.plant(seed, hero.pos);
				break;
			default:
				break;
		}
		if (hero.sprite != null) hero.sprite.operate(hero.pos);
		hero.spendAndNext(1f);
	}

	@Override
	public int icon() {
		return HeroIcon.WILD_MAGIC;
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.BLAST_RADIUS, Talent.ELEMENTAL_POWER, Talent.REACTIVE_BARRIER, Talent.HEROIC_ENERGY};
	}
}
