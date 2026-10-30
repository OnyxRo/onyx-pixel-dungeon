/*
 * Music Implant SPD, ported into Onyx's Pixel Dungeon (key musicimplant)
 * https://github.com/CatAzreal/Jinkeloid-MISPD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.lightsource.FireTorch;
import com.shatteredpixel.shatteredpixeldungeon.items.lightsource.Lightsource;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

//Music Implant's Light buff, renamed as this game keeps Shattered's own Light.
//It lasts while a candle or torch in the pack is lit and burns one charge a turn.
//A candle gives dim light (1 more tile of sight), a torch moderate light (2 more).
public class LightsourceLight extends Buff {

	{
		type = buffType.POSITIVE;
		announced = false;
	}

	public static Lightsource litSource( Char ch ){
		if (!(ch instanceof Hero)) return null;
		for (Item i : ((Hero) ch).belongings){
			if (i instanceof Lightsource && ((Lightsource) i).isActivated()) return (Lightsource) i;
		}
		return null;
	}

	//0 none, 1 dim, 2 moderate, 3 bright (the mod named it, nothing gives it yet)
	public static int intensity( Char ch ){
		if (ch == null || ch.buff(LightsourceLight.class) == null) return 0;
		Lightsource s = litSource(ch);
		return s == null ? 0 : s.brightness();
	}

	public static int visionBonus( Char ch ){
		return intensity(ch);
	}

	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo(target)){
			if (Dungeon.level != null && target == Dungeon.hero) Dungeon.observe();
			return true;
		}
		return false;
	}

	@Override
	public void detach() {
		super.detach();
		if (Dungeon.level != null && target == Dungeon.hero) Dungeon.observe();
	}

	@Override
	public boolean act() {
		Lightsource s = litSource(target);
		if (s == null || s.getCharge() <= 0){
			if (s != null) s.deactivate((Hero) target, false);
			detach();
			return true;
		}
		s.spendCharge();
		spend(TICK);
		return true;
	}

	@Override
	public void fx( boolean on ) {
		if (on) target.sprite.add(CharSprite.State.ILLUMINATED);
		else target.sprite.remove(CharSprite.State.ILLUMINATED);
	}

	private boolean moderate(){
		return litSource(target) instanceof FireTorch;
	}

	@Override
	public int icon() {
		return BuffIndicator.LIGHT;
	}

	@Override
	public void tintIcon( Image icon ) {
		if (!moderate()) icon.hardlight(0.7f, 0.6f, 0.3f);
	}

	@Override
	public float iconFadePercent() {
		Lightsource s = litSource(target);
		if (s == null || s.maxCharge <= 0) return 0;
		return Math.max(0, 1f - s.getCharge() / (float)s.maxCharge);
	}

	@Override
	public String name() {
		return Messages.get(this, moderate() ? "modlight" : "dimlight");
	}

	@Override
	public String desc() {
		Lightsource s = litSource(target);
		return Messages.get(this, moderate() ? "modlight_desc" : "dimlight_desc", s == null ? 0 : s.getCharge());
	}
}
