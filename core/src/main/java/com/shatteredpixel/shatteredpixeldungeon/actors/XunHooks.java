/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Xun's Pixel Dungeon, by X-U-N-123, ported into Onyx's Pixel Dungeon
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

package com.shatteredpixel.shatteredpixeldungeon.actors;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.DMdrill;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Fetter;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.HiddenBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LightFlail;

//xun, hooks Xun's PD's items and monsters need in Shattered's shared classes (Char, Mob, Hero).
//Each call site is one line naming a method here. Talent hooks are in actors/hero/XunTalents.
public class XunHooks {

	//Char.speed, the DM drill's overload doubles speed
	public static float speed(Char ch, float speed){
		DMdrill.DMcombo combo = ch.buff(DMdrill.DMcombo.class);
		if (combo != null && combo.isOverloading()) speed *= 2f;
		return speed;
	}

	//Char.damage, the dark gold sword takes the damage off the target's max health too
	public static void afterDamage(Char ch, int dmg){
		com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Darkgoldsword.HTDecreaseTracker t
				= ch.buff(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Darkgoldsword.HTDecreaseTracker.class);
		if (t != null) {
			ch.HT = Math.max(1, ch.HT - dmg);
			t.detach();
		}
	}

	//the Zhouyi's ability swaps places with its target, as Xun's PD's Char.swapPos did
	public static void swapPos(Char a, Char b){
		if (!b.isAlive()) return;
		if (Char.hasProp(a, Char.Property.IMMOVABLE) || Char.hasProp(b, Char.Property.IMMOVABLE)) return;
		if (!com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.passable[a.pos] && !b.flying) return;
		if (a.properties().contains(Char.Property.LARGE) && !com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.openSpace[b.pos]
				|| b.properties().contains(Char.Property.LARGE) && !com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.openSpace[a.pos]) return;
		int oldPos = a.pos;
		int newPos = b.pos;
		a.pos = newPos;
		b.pos = oldPos;
		com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation.appear(a, newPos);
		com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation.appear(b, oldPos);
		com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.occupyCell(a);
		com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.occupyCell(b);
		if (a == com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero || b == com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero){
			com.shatteredpixel.shatteredpixeldungeon.Dungeon.observe();
			com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.updateFog();
		}
	}

	//the peaceful enchantment, a struck monster does not wake or turn on its attacker for that one hit
	public static boolean pacified(Char ch){
		return ch.buff(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Peaceful.PeacefulTracker.class) != null;
	}

	//Char.damage, the peace ends once the hit has landed
	public static void endPeace(Char ch){
		com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.detach(ch, com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Peaceful.PeacefulTracker.class);
	}

	//Hero.rest, waiting locks a hidden blade again
	public static void onRest(Hero hero){
		KindOfWeapon wep = hero.belongings.weapon();
		if (wep instanceof HiddenBlade) ((HiddenBlade) wep).lock(true);
		wep = hero.belongings.secondWep();
		if (wep instanceof HiddenBlade) ((HiddenBlade) wep).lock(true);
	}

	//flails of the mod, they spin like Shattered's flail and cannot surprise attack
	public static boolean isSpinFlail(Object wep){
		return wep instanceof LightFlail || wep instanceof Fetter;
	}

}
