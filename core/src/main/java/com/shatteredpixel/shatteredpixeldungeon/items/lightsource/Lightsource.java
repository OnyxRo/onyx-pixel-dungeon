/*
 * Music Implant SPD, ported into Onyx's Pixel Dungeon (key musicimplant)
 * https://github.com/CatAzreal/Jinkeloid-MISPD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.items.lightsource;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.MusicImplant;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LightsourceLight;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

//Music Implant's light sources. In the mod they are held in an offhand slot, which this game
//does not have, so here they are lit straight from the pack. Only one burns at a time.
public abstract class Lightsource extends Item {

	private static final String ACTIVE = "active";
	private static final String CHARGE = "charge";

	public static final String AC_LIGHT = "LIGHT";
	public static final String AC_SNUFF = "SNUFF";

	public boolean active;
	public int charge;
	public int maxCharge;

	public float TIME_TO_LIT   = 5f;
	public float TIME_TO_SNUFF = 5f;

	{
		bones = false;
		unique = false;
		defaultAction = AC_LIGHT;
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(ACTIVE, active);
		bundle.put(CHARGE, charge);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		active = bundle.getBoolean(ACTIVE);
		charge = bundle.getInt(CHARGE);
		updateSprite();
	}

	public int getCharge() {
		return charge;
	}

	public boolean isActivated() {
		return active;
	}

	//how much light it gives, see LightsourceLight.intensity
	public abstract int brightness();

	public void updateSprite(){}

	public void spendCharge() {
		charge--;
		if (charge <= 0){
			charge = 0;
			if (Dungeon.hero != null) deactivate(Dungeon.hero, false);
		}
		updateQuickslot();
	}

	public void addCharge( int add ) {
		charge += add;
		updateQuickslot();
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(isActivated() ? AC_SNUFF : AC_LIGHT);
		actions.remove(AC_THROW);
		if (isActivated()) actions.remove(AC_DROP);
		return actions;
	}

	@Override
	public String defaultAction() {
		return isActivated() ? AC_SNUFF : AC_LIGHT;
	}

	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals(AC_LIGHT)){
			if (charge <= 0){
				GLog.w(Messages.get(Lightsource.class, "empty"));
			} else if (LightsourceLight.litSource(hero) != null){
				GLog.w(Messages.get(Lightsource.class, "another_lit"));
			} else {
				activate(hero, true);
			}
		} else if (action.equals(AC_SNUFF)){
			if (isActivated()) deactivate(hero, true);
		} else {
			super.execute(hero, action);
		}
	}

	public void activate( Hero hero, boolean voluntary ) {
		active = true;
		updateSprite();
		if (voluntary){
			float time = MusicImplant.equipTime(null, TIME_TO_LIT);
			hero.spend(time);
			charge += (int)time;
			hero.busy();
			GLog.i(Messages.get(this, "lit"));
			hero.sprite.operate(hero.pos);
		}
		Buff.affect(hero, LightsourceLight.class);
		Sample.INSTANCE.play(Assets.Sounds.BURNING);
		updateQuickslot();
		Dungeon.observe();
	}

	public void deactivate( Hero hero, boolean voluntary ) {
		if (voluntary){
			hero.spend(TIME_TO_SNUFF);
			hero.busy();
			hero.sprite.operate(hero.pos);
			GLog.i(Messages.get(this, "snuff"));
		} else {
			GLog.i(Messages.get(this, "snuffed"));
		}
		active = false;
		updateSprite();
		LightsourceLight light = hero.buff(LightsourceLight.class);
		if (light != null) light.detach();
		Sample.INSTANCE.play(Assets.Sounds.PUFF);
		updateQuickslot();
		Dungeon.observe();
	}

	@Override
	public String status() {
		return Integer.toString(charge);
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public String desc() {
		String state;
		if (charge <= 0) state = Messages.get(this, "state_depleted");
		else if (active) state = Messages.get(this, "state_lit");
		else state = Messages.get(this, "state_unlit");
		return super.desc() + "\n\n" + state + "\n\n" + Messages.get(this, "descturn", charge);
	}
}
