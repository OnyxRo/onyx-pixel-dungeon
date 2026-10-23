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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

//Sandbox PD's wand of summoning. In the mod its level editor picks what the wand summons,
//with nothing picked it calls up a wraith that fights for the hero, which is what this port does.
public class WandOfSummoning extends Wand {

	{
		image = ItemSpriteSheet.WAND_SUMMONING;

		collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_SOLID;
	}

	@Override
	public boolean tryToZap(Hero owner, int target) {
		Ballistica shot = new Ballistica(owner.pos, target, collisionProperties(target));
		int cell = shot.collisionPos;
		if (Actor.findChar(cell) != null && (shot.dist <= 1 || Actor.findChar(shot.path.get(shot.dist - 1)) != null)) {
			if (owner == Dungeon.hero) GLog.w(Messages.get(this, "bad_location"));
			return false;
		}
		return super.tryToZap(owner, target);
	}

	@Override
	public void onZap(Ballistica bolt) {
		int target = bolt.collisionPos;
		if (Actor.findChar(target) != null && bolt.dist > 1) target = bolt.path.get(bolt.dist - 1);

		if (Actor.findChar(target) != null || Dungeon.level.solid[target]) {
			GLog.w(Messages.get(this, "bad_location"));
			return;
		}

		Wraith w = Wraith.spawnAt(target, Wraith.class);
		if (w == null) {
			GLog.w(Messages.get(this, "bad_location"));
			return;
		}
		w.EXP = 0;
		Buff.affect(w, Corruption.class);
	}

	@Override
	public void fx(Ballistica bolt, Callback callback) {
		MagicMissile.boltFromChar( curUser.sprite.parent,
				MagicMissile.SHADOW,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play( Assets.Sounds.ZAP );
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		//lvl 0 20%, lvl 1 33%, lvl 2 43%, a wraith rises beside the struck enemy
		int level = Math.max( 0, staff.buffedLvl() );
		float procChance = (level+1f)/(level+5f) * procChanceMultiplier(attacker);
		if (Random.Float() < procChance * 0.25f) {
			Wraith w = Wraith.spawnAt(defender.pos, Wraith.class);
			if (w != null) {
				w.EXP = 0;
				Buff.affect(w, Corruption.class);
			}
		}
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color( 0 );
		particle.am = 0.6f;
		particle.setLifespan(2f);
		particle.speed.set(0, 5);
		particle.setSize( 0.5f, 2f);
		particle.shuffleXY(1f);
	}

	@Override
	public String statsDesc() {
		return Messages.get(this, "stats_desc_wraith", Messages.get(Wraith.class, "name"));
	}
}
