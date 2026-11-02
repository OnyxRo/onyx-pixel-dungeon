/*
 * Onyx's Pixel Dungeon
 *
 * Re Blue Archive PD's lines in the upgrade window (Hoto-Mocha/Re-Blue-Archive-Pixel-Dungeon). A gun shows its
 * bullet damage, a few special guns their own chance, the assassin's kunai its sneak attack damage, and
 * Aris's third tier two talent at two points keeps an enchantment safe. Added 2026-11-02.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAChistka;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBABeyondTheLumination;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBAJusticeIncarnate;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBAWineRedAdmire;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.quick.RBAAssassinsKunai;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class RBAUpgradeLines {

	private static String pct( float f ){
		return Messages.decimalFormat("#", f*100) + "%";
	}

	//WndUpgrade, after the weapon damage line
	public static float fields( WndUpgrade w, Item item, int from, int to, float bottom ){
		if (item instanceof RBAGun){
			RBAGun gun = (RBAGun) item;
			bottom = w.fillFields(Messages.get(WndUpgrade.class, "rba_bullet"),
					gun.bulletMin(from) + "-" + gun.bulletMax(from),
					gun.bulletMin(to) + "-" + gun.bulletMax(to), bottom);

			if (item instanceof RBABeyondTheLumination){
				RBABeyondTheLumination g = (RBABeyondTheLumination) item;
				bottom = w.fillFields(Messages.get(WndUpgrade.class, "blocking"),
						"0-" + g.DRMax(from), "0-" + g.DRMax(to), bottom);
			}
			if (item instanceof RBAChistka){
				RBAChistka g = (RBAChistka) item;
				bottom = w.fillFields(Messages.get(WndUpgrade.class, "rba_purge"),
						pct(g.purgeChance(from)), pct(g.purgeChance(to)), bottom);
			}
			if (item instanceof RBAJusticeIncarnate){
				RBAJusticeIncarnate g = (RBAJusticeIncarnate) item;
				bottom = w.fillFields(Messages.get(WndUpgrade.class, "rba_justice"),
						pct(g.justiceChance(from)), pct(g.justiceChance(to)), bottom);
			}
			if (item instanceof RBAWineRedAdmire){
				RBAWineRedAdmire g = (RBAWineRedAdmire) item;
				bottom = w.fillFields(Messages.get(WndUpgrade.class, "rba_explosion"),
						pct(g.explosionChance(from)), pct(g.explosionChance(to)), bottom);
			}
		}

		if (item instanceof RBAAssassinsKunai){
			RBAAssassinsKunai k = (RBAAssassinsKunai) item;
			bottom = w.fillFields(Messages.get(WndUpgrade.class, "rba_assassination_damage"),
					pct(k.assassinationMin(from)) + "-" + pct(k.assassinationMax(from)),
					pct(k.assassinationMin(to)) + "-" + pct(k.assassinationMax(to)), bottom);
		}
		return bottom;
	}

	//WndUpgrade, the warning about losing an enchantment
	public static int lossChance( int chance ){
		if (Dungeon.hero != null && Dungeon.hero.pointsInTalent(Talent.RBA_ARIS_T2_3) == 2) return 0;
		return chance;
	}
}
