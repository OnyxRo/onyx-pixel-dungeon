/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Experienced Pixel Dungeon
 * Copyright (C) 2019-2024 Trashbox Bobylev
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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

//From Experienced PD. A random perk is earned at hero levels 5, 11, 18, 26 and so on, see nextPerkLevel.
//Efficient Bartering is last in the list so older saves and seeds keep their order. It works with the Rat King barter, see RatKing.barterAct.
public class Perks {
	public enum Perk {
		SUCKER_PUNCH,
		DIRECTIVE,
		FOLLOW_UP_STRIKE,
		HOLD_FAST{
			public String desc() {
				return Messages.get(Perks.class, name() + ".desc", PerkHoldFast.minArmor(), PerkHoldFast.armor());
			}
		},
		PROTEIN_INFUSION,
		FISHING_PRO,
		WAND_PRO,
		IRON_WILL,
		MYSTICAL_MEAL,
		ADDITIONAL_MONEY,
		POTIONS,
		MORE_BAG,
		NO_ONESHOTS,
		COLLECT_EVERYTHING,
		RAT_SUMMONS,
		GRASS_HEALING,
		FRIENDLY_BEES,
		BETTER_BARTERING;

		public String desc() {
			return Messages.get(Perks.class, name() + ".desc");
		}

		@Override
		public String toString() {
			return Messages.titleCase(Messages.get(Perks.class, name() + ".name"));
		}
	}

	public static class SuckerPunchTracker extends Buff{}
	public static class DirectiveTracker extends FlavourBuff {}
	public static class FollowupStrikeTracker extends Buff{}

	//Experienced's Hold Fast perk. Shattered's own HoldFast buff belongs to the warrior talent, so this is a second one.
	public static class PerkHoldFast extends Buff {

		{
			type = buffType.POSITIVE;
		}

		public int pos = -1;

		@Override
		public boolean act() {
			if (pos != target.pos) {
				detach();
			} else {
				spend(TICK);
			}
			return true;
		}

		public int armorBonus(){
			if (pos == target.pos && target instanceof Hero){
				return Random.NormalIntRange(minArmor(), armor());
			} else {
				detach();
				return 0;
			}
		}

		@Override
		public int icon() {
			return BuffIndicator.ARMOR;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(1.9f, 2.4f, 3.25f);
		}

		public static int armor() {
			return Dungeon.hero.lvl/2;
		}

		public static int minArmor(){
			return Dungeon.hero.lvl/4;
		}

		@Override
		public String toString() {
			return Messages.get(this, "name");
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", minArmor(), armor());
		}

		private static final String POS = "pos";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(POS, pos);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			pos = bundle.getInt(POS);
		}
	}

	public static class DirectiveMovingTracker extends CounterBuff {
		public int duration() { return 3;}
		public float iconFadePercent() { return Math.max(0, 1f - ((count()) / (duration()))); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(duration() - (count()))); }
		public int icon() { return BuffIndicator.MOMENTUM; }
		public void tintIcon(Image icon) { icon.hardlight(0.15f, 0.7f, 0.5f); }
	}

	public static int onAttackProc(Hero hero, Char enemy, int damage){
		if (hero.perks.contains(Perk.SUCKER_PUNCH)
				&& enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)
				&& enemy.buff(SuckerPunchTracker.class) == null){
			damage += hero.damageRoll()/4;
			Buff.affect(enemy, SuckerPunchTracker.class);
		}
		if (hero.perks.contains(Perk.DIRECTIVE)
				&& enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)
				&& enemy.buff(DirectiveTracker.class) == null){
			Buff.affect(hero, Haste.class, 1.5f);
		}
		if (hero.perks.contains(Perk.FOLLOW_UP_STRIKE)) {
			if (hero.belongings.weapon instanceof MissileWeapon) {
				Buff.affect(enemy, FollowupStrikeTracker.class);
			} else if (enemy.buff(FollowupStrikeTracker.class) != null){
				damage += hero.damageRoll()/4;
				if (!(enemy instanceof Mob) || !((Mob) enemy).surprisedBy(hero)){
					Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG, 0.75f, 1.2f);
				}
				enemy.buff(FollowupStrikeTracker.class).detach();
			}
		}
		return damage;
	}

	//Iron Reflection. A blast of force around the hero for a third to half of the damage that was just taken
	public static void ironWillBlast( Hero hero, int dmg, Object src ){
		if (hero.sprite == null || hero.sprite.parent == null || dmg <= 0) return;

		Camera.main.shake(4, 0.25f);
		Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY);

		int aoeSize = 5;
		int projectileProps = Ballistica.STOP_SOLID | Ballistica.STOP_TARGET;
		Ballistica aim;
		if (hero.pos % Dungeon.level.width() > 10){
			aim = new Ballistica(hero.pos, hero.pos - 1, Ballistica.WONT_STOP);
		} else {
			aim = new Ballistica(hero.pos, hero.pos + 1, Ballistica.WONT_STOP);
		}

		final ConeAOE aoe = new ConeAOE(aim, aoeSize, 360, projectileProps);

		for (Ballistica ray : aoe.outerRays){
			((MagicMissile)hero.sprite.parent.recycle( MagicMissile.class )).reset(
					MagicMissile.FORCE_CONE,
					hero.sprite,
					ray.path.get(ray.dist),
					null
			);
		}
		final int finalDmg = dmg;
		((MagicMissile)hero.sprite.parent.recycle( MagicMissile.class )).reset(
				MagicMissile.FORCE_CONE,
				hero.sprite,
				aim.path.get(aoeSize / 2),
				new Callback() {
					@Override
					public void call() {
						for (int cell : aoe.cells) {
							Char mob = Actor.findChar(cell);
							int damage = Random.NormalIntRange(finalDmg / 3, finalDmg / 2);
							if (mob != null && damage > 0 && mob.alignment != Char.Alignment.ALLY){
								mob.damage(damage, src);
							}
						}
					}
				}
		);
	}

	//Restored Nature. Drinking a potion of healing roots nearby enemies and turns the 5x5 around the hero into grass
	public static void growNature( Hero hero ){
		int radius = 2;
		int w = Dungeon.level.width();
		int cx = hero.pos % w;
		int cy = hero.pos / w;
		for (int y = cy - radius; y <= cy + radius; y++) {
			for (int x = cx - radius; x <= cx + radius; x++) {
				if (x < 0 || y < 0 || x >= w || y >= Dungeon.level.height()) continue;
				int cell = x + y * w;
				Char ch = Actor.findChar(cell);
				if (ch != null && ch.alignment == Char.Alignment.ENEMY) {
					Buff.affect(ch, Roots.class, 3f);
				}
				int t = Dungeon.level.map[cell];
				if (t == Terrain.EMPTY || t == Terrain.EMBERS || t == Terrain.EMPTY_DECO) {
					Level.set(cell, Terrain.GRASS);
					GameScene.updateMap(cell);
					t = Terrain.GRASS;
				}
				CellEmitter.get(cell).burst(LeafParticle.LEVEL_SPECIFIC, 4);
				if ((t == Terrain.GRASS || t == Terrain.FURROWED_GRASS) && Dungeon.level.plants.get(cell) == null) {
					Level.set(cell, Terrain.HIGH_GRASS);
					GameScene.updateMap(cell);
				}
			}
		}
	}

	public static int nextPerkLevel(){
		int num = 5;
		for (int i = 0; i < Dungeon.hero.perks.size(); i++){
			num += 5 + i;
		}
		return num;
	}

	public static void debugEarnPerk(String string){
		Perk perk = Perk.valueOf(string);
		Dungeon.hero.perks.add(perk);
		GLog.p(Messages.get(Perks.class, "perk_obtain", perk.toString()));
		if (Dungeon.hero.sprite != null)
			Dungeon.hero.sprite.emitter().burst(Speck.factory(Speck.STAR), 20);
	}

	public static void earnPerk(Hero hero){
		if (hero.perks.size() < Perk.values().length && hero.lvl == nextPerkLevel()){
			Perk perk;
			do {
				perk = Random.element(Perk.values());
			} while (hero.perks.contains(perk));
			hero.perks.add(perk);
			GLog.p(Messages.get(Perks.class, "perk_obtain", perk.toString()));
			if (hero.sprite != null)
				hero.sprite.emitter().burst(Speck.factory(Speck.STAR), 20);
		}
	}

	public static void storeInBundle(Bundle bundle, ArrayList<Perk> perks) {
		ArrayList<String> perkIds = new ArrayList<>();
		for (Perk perk : perks){
			perkIds.add(perk.name());
		}
		bundle.put("perks", perkIds.toArray(new String[0]));
	}

	public static void restoreFromBundle(Bundle bundle, ArrayList<Perk> perks) {
		perks.clear();
		if (bundle.getStringArray("perks") != null) {
			for (String id : bundle.getStringArray("perks")) {
				perks.add(Perk.valueOf(id));
			}
		}
	}
}
