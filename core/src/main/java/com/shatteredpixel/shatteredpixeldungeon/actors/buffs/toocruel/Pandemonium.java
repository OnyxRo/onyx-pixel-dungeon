/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.TooCruel;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

//Pandemonium, weapons, wands and thrown weapons may change into others after use
public class Pandemonium extends Buff implements TcBuff.AttackProc {

	private static final float WEP_BASE = 0.05f, WEP_INC = 0.05f;
	private static final float RANGED_BASE = 0.1f, RANGED_INC = 0.1f;

	private float wepChance = WEP_BASE;
	private float wandChance = RANGED_BASE;
	private float misChance = RANGED_BASE;

	@Override
	public boolean attachTo( Char target ) {
		return target instanceof Hero && super.attachTo( target );
	}

	private boolean rollWep() {
		if (Random.Float() < wepChance) { wepChance = WEP_BASE; return true; }
		wepChance += WEP_INC;
		return false;
	}

	private boolean rollWand() {
		if (Random.Float() < wandChance) { wandChance = RANGED_BASE; return true; }
		wandChance += RANGED_INC;
		return false;
	}

	private boolean rollMis() {
		if (Random.Float() < misChance) { misChance = RANGED_BASE; return true; }
		misChance += RANGED_INC;
		return false;
	}

	public void wandUsed( Wand wand ) {
		if (!rollWand()) return;
		Hero hero = (Hero) target;
		MagesStaff staff = hero.belongings.getItem( MagesStaff.class );
		if (staff != null && staff.wand == wand) {
			Item changed = ScrollOfTransmutation.changeItem( wand );
			if (changed instanceof Wand) {
				Wand newWand = (Wand) changed;
				newWand.level( 0 );
				newWand.identify();
				newWand.curCharges = 0;
				TooCruel.showTransmuted( hero, wand, newWand );
				staff.imbueWand( newWand, null );
			}
		} else {
			TooCruel.transmute( wand, hero );
		}
	}

	public MissileWeapon missileUsed( MissileWeapon missile ) {
		if (rollMis()) {
			Item changed = ScrollOfTransmutation.changeItem( missile );
			if (changed instanceof MissileWeapon) {
				TooCruel.showTransmuted( (Hero) target, missile, changed );
				return (MissileWeapon) changed;
			}
		}
		return missile;
	}

	public void spiritBowUsed( SpiritBow bow ) {
		if (!rollMis()) return;
		Hero hero = (Hero) target;
		MissileWeapon mis = Generator.randomMissile();
		mis.quantity( 1 );
		bow.detach( hero.belongings.backpack );
		if (!mis.collect()) Dungeon.level.drop( mis, hero.pos );
		mis.upgrade( bow.level() );
		if (bow.enchantment != null) mis.enchant();
		TooCruel.showTransmuted( hero, bow, mis );
	}

	@Override
	public void attackProc( Char enemy, int damage ) {
		Hero hero = (Hero) target;
		if (!rollWep()) return;
		KindOfWeapon weapon = hero.belongings.attackingWeapon();
		if (weapon instanceof MissileWeapon || weapon instanceof SpiritBow) return;
		if (weapon != null && weapon == hero.belongings.abilityWeapon) {
			TooCruel.transmute( weapon, hero );
		} else {
			if (hero.belongings.weapon() != null) TooCruel.transmute( hero.belongings.weapon(), hero );
			if (hero.belongings.secondWep() != null) TooCruel.transmute( hero.belongings.secondWep(), hero );
		}
	}

	public static MissileWeapon rerollMissile( MissileWeapon weapon ) {
		Pandemonium p = Dungeon.hero == null ? null : Dungeon.hero.buff( Pandemonium.class );
		return p == null ? weapon : p.missileUsed( weapon );
	}

	public static void spiritBowShot( SpiritBow bow ) {
		Pandemonium p = Dungeon.hero == null ? null : Dungeon.hero.buff( Pandemonium.class );
		if (p != null) p.spiritBowUsed( bow );
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( "weapon_reroll_chance", wepChance );
		bundle.put( "wand_reroll_chance", wandChance );
		bundle.put( "missile_reroll_chance", misChance );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		wepChance = bundle.getFloat( "weapon_reroll_chance" );
		wandChance = bundle.getFloat( "wand_reroll_chance" );
		if (bundle.contains( "missile_reroll_chance" )) misChance = bundle.getFloat( "missile_reroll_chance" );
	}
}
