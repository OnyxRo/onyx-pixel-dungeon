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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Pulse;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;

//xun, talent and subclass effects of Xun's PD's heroes that live in Shattered's shared hero code, copied from the mod
//and kept here so the shared files only gain one line each
public class XunTalents {

	//Thrown Evil (Wraith), a thrown weapon may also fire a cursed wand effect at its target
	public static void thrownEvilProc(int cell){
		final Hero hero = Dungeon.hero;
		if (hero == null || !hero.hasTalent(Talent.THROWN_EVIL)) return;
		if (Random.Float() < 0.1f * hero.pointsInTalent(Talent.THROWN_EVIL) && hero.buff(MagicImmune.class) == null){
			com.shatteredpixel.shatteredpixeldungeon.actors.Actor.add(new com.shatteredpixel.shatteredpixeldungeon.actors.Actor() {
				{ actPriority = VFX_PRIO; }
				@Override
				protected boolean act() {
					com.shatteredpixel.shatteredpixeldungeon.actors.Actor.remove(this);
					com.shatteredpixel.shatteredpixeldungeon.items.wands.CursedWand.cursedZap(null, hero,
							new com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica(hero.pos, cell, com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica.MAGIC_BOLT), this::next);
					return false;
				}
			});
		}
	}

	public static void onFoodEaten( Hero hero ){
		if (hero.hasTalent(Talent.KEEN_MEAL)){
			int DIST = 2 + 3*hero.pointsInTalent(Talent.KEEN_MEAL);
			Point c = Dungeon.level.cellToPoint(hero.pos);
			ArrayList<Trap> disarmCandidates = new ArrayList<>();
			for (int y = Math.max(0, c.y - DIST); y <= Math.min(Dungeon.level.height()-1, c.y + DIST); y++){
				for (int x = Math.max(0, c.x - DIST); x <= Math.min(Dungeon.level.width()-1, c.x + DIST); x++){
					Trap t = Dungeon.level.traps.get(y*Dungeon.level.width() + x);
					if (t != null && t.active) disarmCandidates.add(t);
				}
			}
			Collections.shuffle(disarmCandidates);
			Collections.sort(disarmCandidates, (o1, o2) -> Float.compare(
					Dungeon.level.trueDistance(hero.pos, o1.pos), Dungeon.level.trueDistance(hero.pos, o2.pos)));
			//disarms at most 4/6 traps
			int max = 2 + 2*hero.pointsInTalent(Talent.KEEN_MEAL);
			while (disarmCandidates.size() > max) disarmCandidates.remove(max);
			for (Trap t : disarmCandidates){
				t.reveal();
				t.disarm();
				CellEmitter.get(t.pos).burst(Speck.factory(Speck.STEAM), 6);
			}
			if (!disarmCandidates.isEmpty()) Sample.INSTANCE.play( Assets.Sounds.TELEPORT );
		}
	}

	public static void onItemIdentified( Hero hero, Item item ){
		if (hero.hasTalent(Talent.TESTED_AWARENESS)){
			Buff.affect(hero, MagicalSight.class, 1 + hero.pointsInTalent(Talent.TESTED_AWARENESS));
			Dungeon.observe();
			hero.checkVisibleMobs();
		}
		if (hero.hasTalent(Talent.TESTED_ANTIMAGIC)){
			Buff.affect(hero, MagicImmune.class, 1 + hero.pointsInTalent(Talent.TESTED_ANTIMAGIC));
		}
		if (hero.hasTalent(Talent.TESTED_MAINTENANCE)){
			int pts = 1 + hero.pointsInTalent(Talent.TESTED_MAINTENANCE);
			if (hero.heroClass != HeroClass.ENGINEER) {
				Dungeon.energy += pts;
				Sample.INSTANCE.play(Assets.Sounds.ITEM);
				hero.sprite.showStatusWithIcon( 0x44CCFF, Integer.toString(pts), FloatingText.ENERGY );
			}
			if (hero.belongings.weapon() != null && hero.belongings.weapon().modify != null){
				hero.belongings.weapon().modDurability += pts;
			}
			Armor armor = hero.belongings.armor();
			if (armor != null && armor.modify != null){
				if (armor.modify == Armor.Modification.CONDUCTIVE) armor.modDurability += 5 * pts;
				else if (armor.modify != Armor.Modification.EXPLOSIVE || Random.Int(10) == 0) armor.modDurability += pts;
			}
		}
	}

	public static void onPotionUsed( Hero hero, float factor ){
		if (hero.hasTalent(Talent.LIQUID_CLAIRVOYANCE)){
			//copied from stone of clairvoyance
			Point c = Dungeon.level.cellToPoint(hero.pos);
			int DIST = Math.min(ShadowCaster.MAX_DISTANCE, (int)(3 * hero.pointsInTalent(Talent.LIQUID_CLAIRVOYANCE) * factor));
			int[] rounding = ShadowCaster.rounding[DIST];
			int left, right, curr;
			boolean noticed = false;
			for (int y = Math.max(0, c.y - DIST); y <= Math.min(Dungeon.level.height()-1, c.y + DIST); y++) {
				if (rounding[Math.abs(c.y - y)] < Math.abs(c.y - y)) {
					left = c.x - rounding[Math.abs(c.y - y)];
				} else {
					left = DIST;
					while (rounding[left] < rounding[Math.abs(c.y - y)]) left--;
					left = c.x - left;
				}
				right = Math.min(Dungeon.level.width()-1, c.x + c.x - left);
				left = Math.max(0, left);
				for (curr = left + y * Dungeon.level.width(); curr <= right + y * Dungeon.level.width(); curr++){
					CheckedCell cc = new CheckedCell(); cc.reset( curr, hero.pos ); GameScene.effectOverFog( cc );
					Dungeon.level.mapped[curr] = true;
					if (Dungeon.level.secret[curr]) {
						Dungeon.level.discover(curr);
						if (Dungeon.level.heroFOV[curr]) {
							GameScene.discoverTile(curr, Dungeon.level.map[curr]);
							ScrollOfMagicMapping.discover(curr);
							noticed = true;
						}
					}
				}
			}
			if (noticed) Sample.INSTANCE.play( Assets.Sounds.SECRET );
			Sample.INSTANCE.play( Assets.Sounds.TELEPORT );
			GameScene.updateFog();
		}
		if (hero.hasTalent(Talent.IONIC_LIQUID)){
			Pulse pulse = hero.buff(Pulse.class);
			if (pulse != null) {
				pulse.decreaseCD(Math.round((5 + 10 * hero.pointsInTalent(Talent.IONIC_LIQUID)) * factor));
				ActionIndicator.refresh();
			} else {
				Buff.prolong( hero, Recharging.class, (1 + 2*hero.pointsInTalent(Talent.IONIC_LIQUID)) * factor );
			}
		}
	}

	public static void onScrollUsed( Hero hero, float factor ){
		if (hero.hasTalent(Talent.INSCRIBED_REGENERATION)){
			//25/40% of level
			int toHeal = Math.round( factor * hero.lvl * (0.1f + 0.15f*hero.pointsInTalent(Talent.INSCRIBED_REGENERATION)));
			toHeal = Math.min(hero.HT - hero.HP, toHeal);
			if (toHeal > 0) {
				hero.HP += toHeal;
				hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(toHeal), FloatingText.HEALING);
			}
		}
	}

	public static void onRest( Hero hero ){
		//barrier to a max of 2/4
		if (hero.hasTalent(Talent.SAFE_SURVEY)){
			Barrier barrier = Buff.affect(hero, Barrier.class);
			if (barrier.shielding() < 2*hero.pointsInTalent(Talent.SAFE_SURVEY)) {
				barrier.incShield(1);
			} else if (barrier.shielding() == 2*hero.pointsInTalent(Talent.SAFE_SURVEY)){
				barrier.incShield(0);
			}
		}
	}

	public static int attackProc( Hero hero, Char enemy, int dmg ){
		if (hero.hasTalent(Talent.HOME_ADVANTAGE) && Dungeon.level.map[hero.pos] != Dungeon.level.map[enemy.pos]){
			dmg += Random.IntRange(1, hero.pointsInTalent(Talent.HOME_ADVANTAGE));
		}
		if (hero.hasTalent(Talent.BURIAL_CEREMONY) && enemy.HP <= enemy.HT * (0.3f + 0.3f * hero.pointsInTalent(Talent.BURIAL_CEREMONY))){
			dmg += 1;
		}
		if (hero.subClass == HeroSubClass.INCUBUS && enemy instanceof Mob && ((Mob) enemy).state == ((Mob) enemy).SLEEPING){
			dmg = Math.round(dmg * (1.3f + 0.15f * hero.pointsInTalent(Talent.NIGHTMARE_HAUNTING)));
		}
		if (hero.subClass == HeroSubClass.PLAGUEGOD){
			int gasPos = -1;
			for (int i : PathFinder.NEIGHBOURS8){
				if (!Dungeon.level.solid[enemy.pos+i] && (gasPos == -1
						|| Dungeon.level.trueDistance(hero.pos, enemy.pos+i) < Dungeon.level.trueDistance(hero.pos, gasPos))){
					gasPos = enemy.pos+i;
				}
			}
			if (gasPos == -1) gasPos = enemy.pos;
			GameScene.add( Blob.seed( gasPos, 15, ToxicGas.class ) );
		}
		return dmg;
	}

	public static float accuracy( Hero hero, Char target ){
		if (target != null && (Char.hasProp(target, Char.Property.UNDEAD) || Char.hasProp(target, Char.Property.DEMONIC))){
			switch (hero.pointsInTalent(Talent.VICIOUS_BETRAYAL)){
				case 1: return 1.2f;
				case 2: return 1.5f;
				case 3: return 2f;
				default:
			}
		}
		return 1f;
	}

	public static float speed( Hero hero ){
		Blob gas = Dungeon.level == null ? null : Dungeon.level.blobs.get(ToxicGas.class);
		if (hero.pointsInTalent(Talent.PLAGUE_EUCHARIST) >= 1 && gas != null && gas.volume > 0 && gas.cur[hero.pos] > 0) return 1.12f;
		return 1f;
	}

	public static boolean isImmune( Hero hero, Class effect ){
		if (hero.subClass == HeroSubClass.PLAGUEGOD && (effect == ToxicGas.class
				|| effect == com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison.class)) return true;
		return effect == com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas.class && hero.hasTalent(Talent.CORPSE_DECAY);
	}

	public static int stealthBonus( Hero hero ){
		return hero.subClass == HeroSubClass.INCUBUS ? 2 : 0;
	}
}
