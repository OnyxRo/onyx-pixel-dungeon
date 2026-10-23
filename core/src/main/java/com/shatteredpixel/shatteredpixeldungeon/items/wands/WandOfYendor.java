/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Ported from Sandbox PD (AlphaDraxonis/SandboxPixelDungeon), GPL-3.0.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//Sandbox PD's wand of Yendor, brewed from the Amulet, Tengu's mask and the King's crown.
//Each zap fires the effect of every other wand at once, and it still ends the game like the Amulet.
public class WandOfYendor extends Wand {

	{
		image = ItemSpriteSheet.WAND_YENDOR;

		unique = true;

		collisionProperties = Ballistica.WONT_STOP;
	}

	private static final Set<Class<? extends Wand>> WAND_EFFECTS = new HashSet<>();
	static {
		WAND_EFFECTS.add(WandOfBlastWave.class);
		WAND_EFFECTS.add(WandOfCorrosion.class);
		WAND_EFFECTS.add(WandOfCorruption.class);
		WAND_EFFECTS.add(WandOfDisintegration.class);
		WAND_EFFECTS.add(WandOfFireblast.class);
		WAND_EFFECTS.add(WandOfFrost.class);
		WAND_EFFECTS.add(WandOfLightning.class);
		WAND_EFFECTS.add(WandOfLivingEarth.class);
		WAND_EFFECTS.add(WandOfMagicMissile.class);
		WAND_EFFECTS.add(WandOfPrismaticLight.class);
		WAND_EFFECTS.add(WandOfRegrowth.class);
		WAND_EFFECTS.add(WandOfSummoning.class);
		WAND_EFFECTS.add(WandOfTransfusion.class);
		WAND_EFFECTS.add(WandOfWarding.class);
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (hero.buff(AscensionChallenge.class) != null){
			actions.clear();
		} else {
			actions.add(Amulet.AC_END);
		}
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals(Amulet.AC_END)) {
			Amulet.showAmuletScene(false);
		}
	}

	@Override
	public String actionName(String action, Hero hero) {
		if (action.equals(Amulet.AC_END)) {
			return Messages.get(Amulet.class, "ac_" + action);
		}
		return super.actionName(action, hero);
	}

	private int target = -1;

	@Override
	public boolean tryToZap(Hero owner, int target) {
		if (super.tryToZap(owner, target)) {
			this.target = target;
			return true;
		}
		return false;
	}

	@Override
	public void onZap(Ballistica beam) {
		//the wands fired in fx do the work
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		for (Class<? extends Wand> wandClass : WAND_EFFECTS) {
			Wand w = Reflection.newInstance(wandClass);
			w.level(level());
			w.curCharges = 1;
			w.onHit(staff, attacker, defender, damage);
		}
	}

	@Override
	public void fx(Ballistica beam, Callback callback) {
		int cell = target == -1 ? beam.collisionPos : target;

		if (cursed) {
			CursedWand.cursedZap(this, curUser, beam, callback);
			return;
		}

		ArrayList<Wand> wands = new ArrayList<>();
		for (Class<? extends Wand> wandEffect : WAND_EFFECTS) {
			Wand w = Reflection.newInstance(wandEffect);
			w.level(level());
			w.curCharges = 1;
			wands.add(w);
		}
		Random.shuffle(wands);

		//every wand fires at once, each landing its own effect when its bolt arrives
		for (Wand w : wands) {
			final Ballistica shot = new Ballistica( curUser.pos, cell, w.collisionProperties(cell));
			if (shot.collisionPos == curUser.pos) continue;
			w.fx(shot, new Callback() {
				@Override
				public void call() {
					w.onZap(shot);
				}
			});
		}
		callback.call();
	}

	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{Amulet.class, KingsCrown.class, TengusMask.class};
			inQuantity = new int[]{1, 1, 1};

			cost = 10;

			output = WandOfYendor.class;
			outQuantity = 1;
		}

	}

}
