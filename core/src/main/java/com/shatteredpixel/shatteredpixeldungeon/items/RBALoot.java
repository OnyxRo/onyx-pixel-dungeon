/*
 * Onyx's Pixel Dungeon
 *
 * Loot changes of Re Blue Archive PD (https://github.com/Hoto-Mocha/Re-Blue-Archive-Pixel-Dungeon), ported 2026-10-22.
 * In the mod every student but Aris finds guns where other heroes find melee weapons, a gun of the same tier.
 * Here that happens only when the hero is one of those students, so other heroes find what they always did.
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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBAHero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAAR_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAAR_T2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAAR_T3;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAAR_T4;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAAR_T5;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAGL_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAGL_T2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAGL_T3;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAGL_T4;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAGL_T5;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAHG_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAHG_T2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAHG_T3;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAHG_T4;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAHG_T5;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG_T2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG_T3;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG_T4;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMG_T5;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBASG_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBASG_T2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBASG_T3;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBASG_T4;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBASG_T5;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBASMG_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBASMG_T2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBASMG_T3;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBASMG_T4;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBASMG_T5;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR_T1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR_T2;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR_T3;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR_T4;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR_T5;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class RBALoot {

	//the mod's GUN_T1 to GUN_T5, every kind of gun with the same weight
	public static final Class<?>[][] GUN_TIERS = {
			{RBAAR_T1.class, RBAGL_T1.class, RBAHG_T1.class, RBAMG_T1.class, RBASG_T1.class, RBASMG_T1.class, RBASR_T1.class},
			{RBAAR_T2.class, RBAGL_T2.class, RBAHG_T2.class, RBAMG_T2.class, RBASG_T2.class, RBASMG_T2.class, RBASR_T2.class},
			{RBAAR_T3.class, RBAGL_T3.class, RBAHG_T3.class, RBAMG_T3.class, RBASG_T3.class, RBASMG_T3.class, RBASR_T3.class},
			{RBAAR_T4.class, RBAGL_T4.class, RBAHG_T4.class, RBAMG_T4.class, RBASG_T4.class, RBASMG_T4.class, RBASR_T4.class},
			{RBAAR_T5.class, RBAGL_T5.class, RBAHG_T5.class, RBAMG_T5.class, RBASG_T5.class, RBASMG_T5.class, RBASR_T5.class},
	};

	/** A gun of the same tier when a gun-using student would get a melee weapon of a tier, otherwise null. */
	public static Item gunFor( Generator.Category cat ){
		if (Dungeon.hero == null || !RBAHero.usesGuns(Dungeon.hero)) return null;
		for (int t = 0; t < Generator.wepTiers.length; t++){
			if (Generator.wepTiers[t] == cat){
				return ((Item) Reflection.newInstance(Random.element(GUN_TIERS[t]))).random();
			}
		}
		return null;
	}
}
