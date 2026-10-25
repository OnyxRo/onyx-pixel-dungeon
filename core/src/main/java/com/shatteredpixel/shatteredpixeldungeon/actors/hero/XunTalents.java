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

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfAwareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfChange;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfHealth;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WellWater;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AcidRain;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BrokenArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Daze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedRings;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GreaterHaste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PhysicalEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ScrollEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Speed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ToxicImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.XunBarricade;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Sheep;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Tribute;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfConcealment;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Projecting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MultiTool;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BlazingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BurningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChillingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ConfusionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CorrosionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CursingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DistortionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlashingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlockTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GatewayTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GnollRockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrippingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GuardianTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OozeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PitfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.StormTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ToxicTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WeakeningTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

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
		if (hero.hasTalent(Talent.PREPARING_MEAL)){
			if (hero.heroClass == HeroClass.EXPLORER){
				com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.XunShovel.ExplorerCooldown cd
						= hero.buff(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.XunShovel.ExplorerCooldown.class);
				if (cd != null) cd.decreaseCD(10 + 10 * hero.pointsInTalent(Talent.PREPARING_MEAL));
			} else {
				Buff.affect(hero, Swiftthistle.TimeBubble.class).reset(hero.pointsInTalent(Talent.PREPARING_MEAL) - 1);
			}
		}
		if (hero.hasTalent(Talent.TEARING_MEAL)) Buff.affect(hero, TearingMealTracker.class);
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
		if (onEmbers(hero) && hero.pointsInTalent(Talent.REKINDLED_EMBER) >= 2 && enemy.isAlive()){
			Buff.affect(enemy, Burning.class).reignite(enemy, 5);
			if (enemy.sprite != null) enemy.sprite.emitter().burst(FlameParticle.FACTORY, 2);
		}
		if (hero.hasTalent(Talent.GENERAL_DISARM) && hero.heroClass != HeroClass.ENGINEER
				&& Dungeon.level.map[hero.pos] == Terrain.INACTIVE_TRAP){
			dmg += Random.IntRange(hero.pointsInTalent(Talent.GENERAL_DISARM), 2);
		}
		TearingMealTracker tear = hero.buff(TearingMealTracker.class);
		if (tear != null && !enemy.isImmune(Bleeding.class)){
			Bleeding b = enemy.buff(Bleeding.class);
			if (b == null) b = new Bleeding();
			b.announced = false;
			b.set(dmg * (0.65f + 0.25f * hero.pointsInTalent(Talent.TEARING_MEAL)), TearingMealTracker.class);
			b.attachTo(enemy);
			if (enemy.sprite != null) enemy.sprite.showStatusWithIcon(CharSprite.WARNING, "+" + (int) b.level(), FloatingText.BLEEDING);
			dmg = 0;
			tear.detach();
		}
		if (hero.hasTalent(Talent.HOMEMADE_DRUG)){
			Poison p = hero.buff(Poison.class);
			if (p != null && !enemy.isImmune(Poison.class)){
				Buff.affect(enemy, Poison.class).set(p.left());
				p.detach();
				HomemadeDrugTracker drug = hero.buff(HomemadeDrugTracker.class);
				if (drug != null) drug.delay(3f);
			}
		}
		interruptMeal(hero);
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
		//the Plague God can carry poison (Homemade Drug needs it) but takes no harm from it, see poisonHarmless
		if (hero.subClass == HeroSubClass.PLAGUEGOD && effect == ToxicGas.class) return true;
		if ((effect == Burning.class || effect == com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire.class) && onEmbers(hero)) return true;
		return effect == com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas.class && hero.hasTalent(Talent.CORPSE_DECAY);
	}

	public static int stealthBonus( Hero hero ){
		return hero.subClass == HeroSubClass.INCUBUS ? 2 : 0;
	}

	//=====================================================================================
	//talent completion pass (2026-10-25). Everything below is copied from Xun's PD, where it sat in
	//Shattered's shared classes. Each call site is one line marked //xun.

	private static int pts( Talent t ){
		Hero h = Dungeon.hero;
		return h == null ? 0 : h.pointsInTalent(t);
	}

	//Talent.onTalentUpgraded
	public static void onTalentUpgraded( Hero hero, Talent talent ){
		if (talent == Talent.WINDING_PORCH && Dungeon.level != null){
			Dungeon.observe();
			hero.checkVisibleMobs();
		}
		if (talent == Talent.REKINDLED_EMBER && Dungeon.level != null
				&& Dungeon.level.map[hero.pos] == Terrain.EMBERS && hero.buff(Burning.class) != null){
			Buff.detach(hero, Burning.class);
		}
		if (talent == Talent.HOMEMADE_DRUG && hero.pointsInTalent(talent) >= 2){
			Buff.affect(hero, HomemadeDrugTracker.class);
		}
		if (talent == Talent.ACID_RAIN){
			AcidRain acid = hero.buff(AcidRain.class);
			if (acid == null){
				acid = Buff.affect(hero, AcidRain.class);
				ActionIndicator.setAction(acid);
			}
		}
		if (talent == Talent.CHARISMA && hero.pointsInTalent(talent) >= 3 && Dungeon.level != null){
			for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])){
				ScrollOfSirensSong.Enthralled buff = m.buff(ScrollOfSirensSong.Enthralled.class);
				if (buff != null){
					buff.timeToNow();
				}
			}
		}
	}

	//---------------------------------------------------------------- the Wraith and Wicked Growth
	//whenever a debuff on the Wraith wears off she gains a matching buff (Buff.detach). Wicked Growth makes it stronger
	public static void onBuffDetached( Buff buff ){
		if (!(buff.target instanceof Hero) || buff.type != Buff.buffType.NEGATIVE) return;
		Hero hero = (Hero) buff.target;
		if (hero.heroClass != HeroClass.WRAITH || !hero.isAlive() || Dungeon.level == null) return;
		float boost = 1 + 0.2f * hero.pointsInTalent(Talent.WICKED_GROWTH);

		if (buff instanceof Bleeding){
			Buff.affect(hero, Healing.class).setHeal(Math.round(3 + Math.round(hero.lvl / 2f) * boost), 0, 1);
		} else if (buff instanceof Blindness){
			Buff.affect(hero, MagicalSight.class, 5 * boost);
		} else if (buff instanceof BrokenArmor){
			Barkskin.conditionallyAppend(hero, 2 + hero.lvl / 3, Math.round(5 * boost));
		} else if (buff instanceof Burning){
			Buff.affect(hero, FireImbue.class).set(1 + 5 * boost);
		} else if (buff instanceof Charm){
			Buff.affect(hero, ElixirOfConcealment.Conceal.class, 5 * boost);
		} else if (buff instanceof Chill || buff instanceof Frost){
			Buff.affect(hero, FrostImbue.class, 5 * boost);
		} else if (buff instanceof Corrosion){
			Buff.affect(hero, PotionOfCleansing.Cleanse.class, 5 * boost);
		} else if (buff instanceof Cripple){
			Buff.affect(hero, Haste.class, 5 * boost);
		} else if (buff instanceof Daze || buff instanceof Hex){
			Buff.affect(hero, Bless.class, 5 * boost);
		} else if (buff instanceof Degrade){
			Buff.affect(hero, ScrollEmpower.class).reset(Math.round(boost));
			Buff.affect(hero, EnhancedRings.class, 4 * boost);
		} else if (buff instanceof Ooze){
			Buff.affect(hero, BlobImmunity.class, 5 * boost);
		} else if (buff instanceof Paralysis){
			Buff.affect(hero, Swiftthistle.TimeBubble.class).reset(Math.round(5 * boost) - 1);
		} else if (buff instanceof Poison){
			Buff.affect(hero, ToxicImbue.class).set(1 + 5 * boost);
		} else if (buff instanceof Roots){
			Buff.affect(hero, Levitation.class, 5 * boost);
		} else if (buff instanceof Slow){
			Buff.affect(hero, Speed.class, 5 * boost);
		} else if (buff instanceof Vertigo){
			Buff.affect(hero, GreaterHaste.class).set(Math.round(1 + 5 * boost));
		} else if (buff instanceof Vulnerable){
			Buff.affect(hero, Barrier.class).setShield(Math.round(3 + Math.round(hero.lvl / 2f) * boost));
		} else if (buff instanceof Weakness){
			Buff.affect(hero, PhysicalEmpower.class).set(Math.round((2 + hero.lvl / 3f) * boost), 2);
		}
	}

	//---------------------------------------------------------------- Explorer
	//Secret Foresight, a found secret identifies worn gear (ScrollOfMagicMapping.discover runs for every found secret)
	public static void secretForesight(){
		Hero hero = Dungeon.hero;
		if (hero == null || !hero.hasTalent(Talent.SECRET_FORESIGHT) || hero.buff(SecretForesightCooldown.class) != null) return;
		ArrayList<Item> toID = new ArrayList<>();
		Item[] worn = {hero.belongings.weapon, hero.belongings.armor, hero.belongings.ring, hero.belongings.misc, hero.belongings.artifact};
		for (Item i : worn) if (i != null && !i.isIdentified()) toID.add(i);
		if (!toID.isEmpty()){
			Item item = toID.get(Random.Int(toID.size()));
			ScrollOfIdentify.IDItem(item);
			GLog.p(Messages.get(XunTalents.class, "foresight", item.name()));
			Buff.affect(hero, SecretForesightCooldown.class, 499f);
		} else if (hero.pointsInTalent(Talent.SECRET_FORESIGHT) >= 2){
			for (Item e : hero.belongings){
				if (e instanceof EquipableItem && !e.cursedKnown) toID.add(e);
			}
			if (!toID.isEmpty()){
				Item item = toID.get(Random.Int(toID.size()));
				item.cursedKnown = true;
				GLog.p(Messages.get(XunTalents.class, "foresight", item.name()));
				Item.updateQuickslot();
				Buff.affect(hero, SecretForesightCooldown.class, 499f);
			}
		}
	}

	public static class SecretForesightCooldown extends FlavourBuff {
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.6f, 0.6f, 0f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 500f); }
	}

	//Winding Porch, standing on a door senses creatures nearby (Level.updateFieldOfView)
	public static int windingPorch( Hero hero ){
		if (!hero.hasTalent(Talent.WINDING_PORCH)) return 0;
		int t = Dungeon.level.map[hero.pos];
		return (t == Terrain.DOOR || t == Terrain.OPEN_DOOR) ? 1 + 2 * hero.pointsInTalent(Talent.WINDING_PORCH) : 0;
	}

	//Rekindled Ember, fire cannot touch the Explorer on embers
	private static boolean onEmbers( Hero hero ){
		return Dungeon.level != null && hero.pos >= 0 && hero.pos < Dungeon.level.length()
				&& Dungeon.level.map[hero.pos] == Terrain.EMBERS && hero.hasTalent(Talent.REKINDLED_EMBER);
	}

	//Dig the Well. An emptied magic well remembers its water here, so a waterskin dropped into it can make it flow again
	private static final Class<?>[] WATERS = {WaterOfHealth.class, WaterOfAwareness.class, WaterOfChange.class, WaterOfEnchantment.class};

	public static class DryWells extends Blob {
		@Override
		protected void evolve() {
			int cell;
			for (int i = area.top - 1; i <= area.bottom; i++) {
				for (int j = area.left - 1; j <= area.right; j++) {
					cell = j + i * Dungeon.level.width();
					if (Dungeon.level.insideMap(cell)) {
						off[cell] = cur[cell];
						volume += off[cell];
					}
				}
			}
		}
	}

	//WellWater.affectCell, after a well runs dry
	public static void wellEmptied( WellWater water, int cell ){
		for (int i = 0; i < WATERS.length; i++){
			if (water.getClass() == WATERS[i]){
				Blob b = Dungeon.level.blobs.get(DryWells.class);
				if (b != null && b.cur[cell] > 0) b.clear(cell);
				GameScene.add(Blob.seed(cell, i + 1, DryWells.class));
				return;
			}
		}
	}

	//Level.pressCell on an empty well
	public static void digTheWell( int cell ){
		Hero hero = Dungeon.hero;
		if (hero == null || !hero.hasTalent(Talent.DIG_THE_WELL)) return;
		Heap heap = Dungeon.level.heaps.get(cell);
		if (heap == null || !(heap.peek() instanceof Waterskin)) return;
		WellsDug dug = hero.buff(WellsDug.class);
		if (dug != null && (dug.count >= hero.pointsInTalent(Talent.DIG_THE_WELL) || dug.levelsLeft(hero) > 0)) return;
		Blob marker = Dungeon.level.blobs.get(DryWells.class);
		if (marker == null || marker.cur[cell] <= 0 || marker.cur[cell] > WATERS.length) return;

		Class water = WATERS[marker.cur[cell] - 1];
		marker.clear(cell);
		GameScene.add(Blob.seed(cell, 1, water));
		Level.set(cell, Terrain.WELL);
		GameScene.updateMap(cell);
		Splash.at(DungeonTilemap.tileCenterToWorld(cell), -PointF.PI / 2, PointF.PI / 2, 0x5bc1e3, 10, 0.01f);
		Sample.INSTANCE.play(Assets.Sounds.GAS, 1f, 0.8f);
		Dungeon.observe();

		dug = Buff.affect(hero, WellsDug.class);
		dug.count++;
		dug.lvl = hero.lvl;

		//the waterskin bounces out, as Xun's PD did
		int newPlace;
		do {
			newPlace = cell + PathFinder.NEIGHBOURS8[Random.Int(8)];
		} while (!Dungeon.level.passable[newPlace] && !Dungeon.level.avoid[newPlace]);
		Heap moved = Dungeon.level.drop(heap.pickUp(), newPlace);
		if (moved.sprite != null) moved.sprite.drop(cell);
	}

	//counts the wells dug this run, the next one waits two hero levels
	public static class WellsDug extends Buff {
		{ revivePersists = true; }
		public int count = 0;
		public int lvl = 0;
		public int levelsLeft( Hero hero ){ return Math.max(0, lvl + 2 - hero.lvl); }
		@Override public boolean act(){ diactivate(); return true; }
		@Override public int icon(){ return target instanceof Hero && levelsLeft((Hero) target) > 0 ? BuffIndicator.TIME : BuffIndicator.NONE; }
		@Override public void tintIcon(Image icon) { icon.hardlight(0.15f, 0.2f, 0.8f); }
		@Override public String iconTextDisplay(){ return target instanceof Hero ? Integer.toString(levelsLeft((Hero) target)) : ""; }
		@Override public String desc(){ return Messages.get(this, "desc", target instanceof Hero ? levelsLeft((Hero) target) : 0); }
		@Override public void storeInBundle(Bundle bundle){ super.storeInBundle(bundle); bundle.put("count", count); bundle.put("lvl", lvl); }
		@Override public void restoreFromBundle(Bundle bundle){ super.restoreFromBundle(bundle); count = bundle.getInt("count"); lvl = bundle.getInt("lvl"); }
	}

	//Preparing Meal and Tearing Meal eat faster (Food.eatingTime and the other eating paths)
	public static boolean fastMeal( Hero hero ){
		return hero != null && (hero.hasTalent(Talent.PREPARING_MEAL) || hero.hasTalent(Talent.TEARING_MEAL));
	}

	public static class TearingMealTracker extends Buff {
		@Override public int icon() { return BuffIndicator.AMOK; }
	}

	//---------------------------------------------------------------- Wavechaser
	//River Erosion, enemies standing in water are frostbitten (Mob.act)
	public static void riverErosion( Mob mob ){
		Hero hero = Dungeon.hero;
		if (hero == null || !hero.hasTalent(Talent.RIVER_EROSION) || mob.alignment != Char.Alignment.ENEMY) return;
		if ((!mob.flying || hero.pointsInTalent(Talent.UNDERCURRENT) >= 3) && Dungeon.level.water[mob.pos]){
			Buff.prolong(mob, Chill.class, 1 + hero.pointsInTalent(Talent.RIVER_EROSION));
		}
	}

	//---------------------------------------------------------------- Trapper
	public static class FriendlyMechanismCooldown extends FlavourBuff {
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.25f, 0.25f, 0.25f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 150f); }
	}

	//traps that change the area, the Trapper gets purity when he steps on one
	private static final Class<?>[] PURITY_TRAPS = {BlazingTrap.class, BurningTrap.class, ChillingTrap.class, ConfusionTrap.class,
			CorrosionTrap.class, FrostTrap.class, ShockingTrap.class, StormTrap.class, ToxicTrap.class};
	//traps the Trapper disarms instead at +3
	private static final Class<?>[] DISARM_TRAPS = {AlarmTrap.class, CursingTrap.class, DisarmingTrap.class, ExplosiveTrap.class,
			FlashingTrap.class, FlockTrap.class, GatewayTrap.class, GeyserTrap.class, GnollRockfallTrap.class, GrippingTrap.class,
			OozeTrap.class, PitfallTrap.class, RockfallTrap.class, TeleportationTrap.class, WeakeningTrap.class};
	//traps that summon enemies, which the Trapper charms at +2
	private static final Class<?>[] SUMMON_TRAPS = {DistortionTrap.class, GuardianTrap.class, SummoningTrap.class};

	private static boolean in( Class<?>[] list, Class<?> c ){
		for (Class<?> l : list) if (l == c) return true;
		return false;
	}

	//Friendly Mechanism (Trap.trigger calls this in place of activate)
	public static void activateTrap( Trap trap ){
		Hero hero = Dungeon.hero;
		if (hero == null || !hero.hasTalent(Talent.FRIENDLY_MECHANISM)){
			trap.activate();
			return;
		}
		int p = hero.pointsInTalent(Talent.FRIENDLY_MECHANISM);
		boolean onIt = hero.pos == trap.pos && !hero.flying;
		boolean ready = hero.buff(FriendlyMechanismCooldown.class) == null;
		Class<?> c = trap.getClass();

		if (onIt && ready && p >= 3){
			if (in(DISARM_TRAPS, c)){
				trap.disarm();
				Buff.affect(hero, FriendlyMechanismCooldown.class, 150f);
				return;
			}
			if (c == WarpingTrap.class){
				GameScene.selectCell(new CellSelector.Listener() {
					@Override public String prompt() { return Messages.get(XunTalents.class, "warp_prompt"); }
					@Override public void onSelect(Integer cell) {
						if (cell != null && ScrollOfTeleportation.teleportToLocation(Dungeon.hero, cell))
							Buff.affect(Dungeon.hero, FriendlyMechanismCooldown.class, 150f);
					}
				});
				return;
			}
		}
		if (onIt && ready && in(PURITY_TRAPS, c)){
			Buff.affect(hero, BlobImmunity.class, 3f);
			Buff.affect(hero, FriendlyMechanismCooldown.class, 150f);
		}
		HashSet<Mob> before = null;
		if (p >= 2 && ready && in(SUMMON_TRAPS, c)) before = new HashSet<>(Dungeon.level.mobs);

		trap.activate();

		if (before != null){
			boolean charmed = false;
			for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])){
				if (!before.contains(m) && m.alignment == Char.Alignment.ENEMY){
					Buff.affect(m, ScrollOfSirensSong.Enthralled.class);
					charmed = true;
				}
			}
			if (charmed) Buff.affect(hero, FriendlyMechanismCooldown.class, 150f);
		}
	}

	//aimed traps (worn dart, poison dart, disintegration, grim) skip a Trapper standing on them at +2
	public static Char aimedTrapStart( Char target ){
		Hero hero = Dungeon.hero;
		if (target != null && target == hero && !hero.flying && hero.pointsInTalent(Talent.FRIENDLY_MECHANISM) >= 2) return null;
		return target;
	}

	//and count him as out of range while the talent is ready
	public static boolean trapperUnseen( Char ch ){
		Hero hero = Dungeon.hero;
		if (ch != null && ch == hero && hero.pointsInTalent(Talent.FRIENDLY_MECHANISM) >= 2
				&& hero.buff(FriendlyMechanismCooldown.class) == null){
			Buff.affect(hero, FriendlyMechanismCooldown.class, 150f);
			return true;
		}
		return false;
	}

	//Sensitive Pedal +1, enemies press cells hard and set off hidden traps (Level.occupyCell)
	public static boolean pressesHard( Char ch ){
		return ch.alignment == Char.Alignment.ENEMY && pts(Talent.SENSITIVE_PEDAL) >= 1;
	}

	//Sensitive Pedal +2, enemies no longer walk around traps (Dungeon.findPassable)
	public static void trapperPassable( Char ch, boolean[] passable ){
		if (ch.alignment != Char.Alignment.ENEMY || pts(Talent.SENSITIVE_PEDAL) < 2) return;
		int[] map = Dungeon.level.map;
		for (int i = 0; i < passable.length && i < map.length; i++){
			if (map[i] == Terrain.TRAP) passable[i] = true;
		}
	}

	//Sensitive Pedal +3, the Trapper senses what a trap hit for 10 turns (Trap.HazardAssistTracker)
	public static void onHazard( Char ch ){
		Hero hero = Dungeon.hero;
		if (ch == null || hero == null || ch == hero || hero.pointsInTalent(Talent.SENSITIVE_PEDAL) < 3) return;
		Buff.append(hero, TalismanOfForesight.CharAwareness.class, 10).charID = ch.id();
	}

	//Liquid Collecting, enemies killed by hazards drop liquid metal (Mob.rollToDropLoot)
	public static void liquidCollecting( Mob mob ){
		Hero hero = Dungeon.hero;
		if (hero == null || !hero.hasTalent(Talent.LIQUID_COLLECTING) || !Regeneration.regenOn()
				|| mob.buff(Trap.HazardAssistTracker.class) == null) return;
		Talent.RejuvenatingStepsFurrow furrow = hero.buff(Talent.RejuvenatingStepsFurrow.class);
		if (furrow != null && furrow.count() >= 200) return;
		Heap metal = Dungeon.level.drop(new LiquidMetal().quantity(hero.pointsInTalent(Talent.LIQUID_COLLECTING)), mob.pos);
		if (metal.sprite != null) metal.sprite.drop();
		Buff.count(hero, Talent.RejuvenatingStepsFurrow.class, 3);
	}

	//---------------------------------------------------------------- Wraith
	//Ancestral Tribute, graves and bones may hold an ancestral offering (Heap.open)
	public static class TributesFound extends CounterBuff {{ revivePersists = true; }}

	public static void onHeapOpened( Heap heap ){
		Hero hero = Dungeon.hero;
		if (hero == null || !hero.hasTalent(Talent.ANCESTRAL_TRIBUTE)) return;
		if (heap.type != Heap.Type.TOMB && heap.type != Heap.Type.REMAINS && heap.type != Heap.Type.SKELETON) return;
		TributesFound found = hero.buff(TributesFound.class);
		if ((found == null ? 0 : found.count()) < 1 + hero.pointsInTalent(Talent.ANCESTRAL_TRIBUTE)){
			heap.items.add(new Tribute());
			Buff.count(hero, TributesFound.class, 1);
		}
	}

	//Blood Intuition, the waterskin can identify an item for some health
	private static final String AC_BLOOD_ID = "IDENTIFY";

	public static void waterskinActions( Hero hero, ArrayList<String> actions ){
		if (hero.hasTalent(Talent.BLOOD_INTUITION) && hero.buff(Invulnerability.class) == null) actions.add(AC_BLOOD_ID);
	}

	public static boolean waterskinAction( final Hero hero, Waterskin skin, String action ){
		if (!action.equals(AC_BLOOD_ID)) return false;
		final int dmg = hero.lvl / (1 + hero.pointsInTalent(Talent.BLOOD_INTUITION)) + 7 - 2 * hero.pointsInTalent(Talent.BLOOD_INTUITION);
		if (hero.HP + hero.shielding() <= dmg || hero.buff(Invulnerability.class) != null){
			GLog.w(Messages.get(Waterskin.class, "no_enough_hp"));
			return true;
		}
		GameScene.selectItem(new WndBag.ItemSelector() {
			@Override public String textPrompt() { return Messages.get(Waterskin.class, "prompt"); }
			@Override public boolean itemSelectable(Item item) { return item instanceof EquipableItem && !item.isIdentified(); }
			@Override public void onSelect(Item item) {
				if (item == null) return;
				ScrollOfIdentify.IDItem(item);
				hero.damage(dmg, skin);
				Sample.INSTANCE.play(Assets.Sounds.CURSED);
				hero.sprite.operate(hero.pos);
				hero.sprite.emitter().burst(ShadowParticle.CURSE, 6 - hero.pointsInTalent(Talent.BLOOD_INTUITION));
				hero.spendAndNext(com.shatteredpixel.shatteredpixeldungeon.actors.Actor.TICK);
			}
		});
		return true;
	}

	//Psionic Blast, a big hit on the Wraith hurts every enemy she can see (Hero.damage)
	public static class PsionicBlast {}

	public static void onHeroDamaged( Hero hero, int dmg, Object src ){
		if (src instanceof PsionicBlast || !hero.hasTalent(Talent.PSIONIC_BLAST) || Dungeon.level == null) return;
		if (dmg >= hero.lvl * (1.25f - 0.25f * hero.pointsInTalent(Talent.PSIONIC_BLAST))){
			for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])){
				if (Dungeon.level.heroFOV[m.pos] && m.alignment == Char.Alignment.ENEMY && m.isAlive()){
					m.damage(Math.round(dmg / 2f), new PsionicBlast());
				}
			}
			Sample.INSTANCE.play(Assets.Sounds.BLAST);
			if (hero.sprite != null && hero.sprite.parent != null) WandOfBlastWave.BlastWave.blast(hero.pos, 4);
		}
	}

	//Scapegoat, a debuffed enemy that dies in sight leaves a sheep behind (Char.die)
	public static void scapegoat( Char ch ){
		Hero hero = Dungeon.hero;
		if (hero == null || !hero.hasTalent(Talent.SCAPEGOAT) || ch == hero || Dungeon.level == null) return;
		if (ch.alignment != Char.Alignment.ENEMY || ch.pos < 0 || Dungeon.level.pit[ch.pos] || !Dungeon.level.heroFOV[ch.pos]) return;
		for (Buff buff : ch.buffs()){
			if (buff.type == Buff.buffType.NEGATIVE){
				Sheep sheep = new Sheep();
				sheep.initialize(1 + 2 * hero.pointsInTalent(Talent.SCAPEGOAT));
				sheep.pos = ch.pos;
				GameScene.add(sheep);
				Dungeon.level.occupyCell(sheep);
				CellEmitter.get(ch.pos).burst(Speck.factory(Speck.WOOL), 4);
				return;
			}
		}
	}

	//---------------------------------------------------------------- Incubus
	//Lullaby, enemies that spawn over time may be asleep (Level.spawnMob)
	public static void lullaby( Mob mob ){
		int p = pts(Talent.LULLABY);
		if (p > 0 && Random.Float() <= p / 3f) mob.state = mob.SLEEPING;
	}

	//Sleepwalking, the Incubus and her allies act in magical sleep (MagicalSleep)
	public static boolean sleepwalks( Char ch ){
		return ch.alignment == Char.Alignment.ALLY && pts(Talent.SLEEPWALKING) >= 1;
	}

	//heal per turn of magical sleep, through the buff's own counter
	public static int sleepHeal( MagicalSleep sleep ){
		int p = pts(Talent.SLEEPWALKING);
		if (p == 0) return 1;
		sleep.xunPartialHeal += 0.5f + 0.25f * p;
		int heal = (int) sleep.xunPartialHeal;
		sleep.xunPartialHeal -= heal;
		return heal;
	}

	//Sleeping In, woken enemies take longer to act (Mob.Sleeping)
	public static float wakeUpFactor(){
		return 1 + pts(Talent.SLEEPING_IN);
	}

	//Nightmare Haunting and Wrong Side of the Bed when a sleeping enemy is hurt (Mob.damage)
	public static int wokenByDamage( Mob mob, int dmg, Object src ){
		Hero hero = Dungeon.hero;
		if (hero == null) return dmg;
		if (src instanceof Char){
			if (hero.subClass != HeroSubClass.INCUBUS) return dmg;
			//the hero's own hits were already boosted in XunTalents.attackProc
			if (src != hero) dmg = Math.round(dmg * (1.3f + 0.15f * hero.pointsInTalent(Talent.NIGHTMARE_HAUNTING)));
		} else if (hero.subClass == HeroSubClass.INCUBUS){
			dmg = Math.round(dmg * (1.4f + 0.2f * hero.pointsInTalent(Talent.NIGHTMARE_HAUNTING)));
		}
		switch (hero.pointsInTalent(Talent.WRONG_SIDE_OF_THE_BED)){
			case 3:
				Buff.affect(mob, PhysicalEmpower.class).set(dmg, 1);
			case 2:
				Buff.affect(mob, Adrenaline.class, 3f);
			case 1:
				if (mob.alignment == Char.Alignment.ENEMY) Buff.affect(mob, Amok.class, 3f);
			default:
		}
		return dmg;
	}

	//and for the hero woken from magical sleep (Char.damage)
	public static void heroWoken( Char ch, int dmg ){
		if (!(ch instanceof Hero)) return;
		switch (((Hero) ch).pointsInTalent(Talent.WRONG_SIDE_OF_THE_BED)){
			case 3:
				Buff.affect(ch, PhysicalEmpower.class).set(dmg, 1);
			case 2:
				Buff.affect(ch, Adrenaline.class, 3f);
			default:
		}
	}

	//---------------------------------------------------------------- Plague God
	//the Plague God is not hurt by poison (Poison.act)
	public static boolean poisonHarmless( Char target ){
		return target instanceof Hero && ((Hero) target).subClass == HeroSubClass.PLAGUEGOD;
	}

	//Homemade Drug, poison on the Plague God does not wear off and may grow (Poison.act)
	public static float poisonLeft( Char target, float left ){
		if (target instanceof Hero){
			int p = ((Hero) target).pointsInTalent(Talent.HOMEMADE_DRUG);
			if (p == 1) return left;
			if (p >= 2) return left < p ? left + 1 : left;
		}
		return left - 1;
	}

	public static class HomemadeDrugTracker extends Buff {
		@Override
		public boolean act(){
			if (!(target instanceof Hero) || ((Hero) target).pointsInTalent(Talent.HOMEMADE_DRUG) < 2){
				detach();
			} else if (target.buff(Poison.class) == null){
				Buff.affect(target, Poison.class);
			}
			spend(TICK);
			return true;
		}
		public void delay( float time ){
			spend(time);
		}
	}

	//Magical Vent, zapping a wand may release toxic gas near the target (Wand)
	public static void magicalVent( Hero user, int target, boolean nearTarget ){
		int p = user.pointsInTalent(Talent.MAGICAL_VENT);
		if (p == 0 || Random.Float() >= p / 3f) return;
		int gasPos = target;
		if (nearTarget){
			gasPos = -1;
			for (int i : PathFinder.NEIGHBOURS8){
				int c = target + i;
				if (c < 0 || c >= Dungeon.level.length()) continue;
				if (!Dungeon.level.solid[c] && (gasPos == -1
						|| Dungeon.level.trueDistance(user.pos, c) < Dungeon.level.trueDistance(user.pos, gasPos))){
					gasPos = c;
				}
			}
			if (gasPos == -1) gasPos = target;
		}
		GameScene.add(Blob.seed(gasPos, 15, ToxicGas.class));
	}

	//---------------------------------------------------------------- Engineer
	//Eat Little and Often, more satiety and room past the maximum (Hunger.affectHunger)
	public static float hungerEnergy( float energy ){
		int p = pts(Talent.EAT_LITTLE_AND_OFTEN);
		return p > 0 ? energy * (1.05f + 0.1f * p) : energy;
	}

	public static float hungerFloor( float level ){
		return pts(Talent.EAT_LITTLE_AND_OFTEN) > 0 ? Math.max(level, -150) : 0;
	}

	//Fine Intuition (StoneOfIntuition, ScrollOfDivination)
	public static boolean keepIntuitionStone( boolean correct ){
		return correct && pts(Talent.FINE_INTUITION) >= 1;
	}

	public static int extraDivination(){
		return pts(Talent.FINE_INTUITION) >= 2 ? 2 : 0;
	}

	//Toilsome Meal, the Engineer acts while eating and gets the satiety when the meal ends
	public static boolean eatsWhileActing( Hero hero ){
		return hero.hasTalent(Talent.TOILSOME_MEAL);
	}

	public static boolean toilsomeMeal( Hero hero, float foodVal, float time ){
		if (!hero.hasTalent(Talent.TOILSOME_MEAL)) return false;
		Buff.append(hero, ToilsomeMealTracker.class, Math.max(1, time)).foodVal = foodVal;
		return true;
	}

	//fighting wastes the meal below +2 (Hero.attackProc and defenseProc)
	public static void interruptMeal( Hero hero ){
		if (hero.pointsInTalent(Talent.TOILSOME_MEAL) >= 2) return;
		for (Buff b : hero.buffs(ToilsomeMealTracker.class)) b.detach();
	}

	public static class ToilsomeMealTracker extends FlavourBuff {
		{ actPriority = HERO_PRIO + 1; }
		private static final float DURATION = 3;
		public float foodVal;
		@Override public String desc() {
			return Messages.get(this, "desc" + Math.min(2, Math.max(1, ((Hero) target).pointsInTalent(Talent.TOILSOME_MEAL))), dispTurns());
		}
		@Override public int icon() { return BuffIndicator.SPELL_FOOD; }
		@Override public String iconTextDisplay() { return Integer.toString((int) cooldown()); }
		@Override public float iconFadePercent() { return Math.max(0, (DURATION - cooldown()) / DURATION); }
		@Override public void detach() {
			super.detach();
			if (target instanceof Hero && cooldown() <= 0 && com.shatteredpixel.shatteredpixeldungeon.actors.Actor.curActorPriority() <= actPriority)
				Buff.affect(target, Hunger.class).satisfy(foodVal);
		}
		@Override public void storeInBundle(Bundle bundle) { super.storeInBundle(bundle); bundle.put("food_val", foodVal); }
		@Override public void restoreFromBundle(Bundle bundle) { super.restoreFromBundle(bundle); foodVal = bundle.getFloat("food_val"); }
	}

	//Favorite Work, modified gear works better for the Craftsman (Weapon.accuracyFactor, Armor.evasionFactor)
	public static float favoriteWork( Char owner, boolean modified ){
		if (!modified || !(owner instanceof Hero)) return 1f;
		return 1 + ((Hero) owner).pointsInTalent(Talent.FAVORITE_WORK) / 12f;
	}

	//and gives back part of a charge for modified wands (Wand.wandUsed)
	public static void favoriteWorkCharge( Wand wand ){
		Hero hero = Dungeon.hero;
		if (wand.modify == null || hero == null || !hero.hasTalent(Talent.FAVORITE_WORK)) return;
		wand.partialCharge += hero.pointsInTalent(Talent.FAVORITE_WORK) / 12f;
		if (wand.partialCharge >= 1){
			wand.curCharges++;
			wand.partialCharge--;
		}
	}

	//---------------------------------------------------------------- Grenadier
	public static class ShockwaveCooldown extends FlavourBuff {
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.6f, 0f, 0f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 50f); }
	}

	public static class BallisticaCalcTracker extends FlavourBuff {
		public int icon() { return BuffIndicator.TIME; }
	}

	//Bomb.onThrow, a Grenadier's lit bomb goes off at once. Ballistica Calculation readies a free throw
	public static boolean onBombThrown( Bomb bomb, boolean lit, Hero user ){
		Hero hero = Dungeon.hero;
		boolean gren = lit && hero != null && user == hero && hero.subClass == HeroSubClass.GRENADIER;
		if (gren && hero.hasTalent(Talent.BALLISTICA_CALC)){
			//a bomb thrown at a creature lands before the throw's time is spent, so use the throw delay then
			float until = hero.cooldown();
			if (until < 0.01f) until = bomb.castDelay(hero, hero.pos);
			Buff.prolong(hero, BallisticaCalcTracker.class, Math.max(until, 0.01f));
		}
		return gren;
	}

	//MissileWeapon.castDelay, the throw right after the bomb takes no time
	public static boolean ballisticaFree( Char user ){
		return user != null && user.buff(BallisticaCalcTracker.class) != null;
	}

	//MissileWeapon.proc, and hits harder at +2 and +3
	public static int ballisticaDamage( Char attacker, int damage ){
		if (!(attacker instanceof Hero) || attacker.buff(BallisticaCalcTracker.class) == null) return damage;
		int p = ((Hero) attacker).pointsInTalent(Talent.BALLISTICA_CALC) - 1;
		return p > 0 ? Math.round(damage * (1 + 0.15f * p)) : damage;
	}

	//MissileWeapon.onThrow, Thrown Evil and the end of the free throw
	public static void afterMissileThrown( int cell ){
		thrownEvilProc(cell);
		if (Dungeon.hero != null) Buff.detach(Dungeon.hero, BallisticaCalcTracker.class);
	}

	//Explosion Proof +3, the Grenadier's bombs leave doors, shelves and grass alone (Bomb.explode)
	public static boolean bombBurnsTerrain( Bomb bomb, int cell ){
		return !bomb.xunGrenadier || Dungeon.level.map[cell] == Terrain.BARRICADE || pts(Talent.EXPLOSION_PROOF) < 3;
	}

	//Explosion Proof +2, bombs no longer destroy items, other bombs still go off (Bomb.explode and Heap.explode)
	private static boolean sparingItems = false;

	public static void bombHeap( Bomb bomb, Heap heap ){
		sparingItems = pts(Talent.EXPLOSION_PROOF) >= 2;
		try {
			heap.explode();
		} finally {
			sparingItems = false;
		}
	}

	public static boolean bombSparesItem( Item item ){
		return sparingItems && !(item instanceof Bomb);
	}

	//damage of one bomb on one character. Upgraded bombs hit harder, the Grenadier's bombs ignore enemy armor and
	//knock enemies back, allies take less, the multitool's enchantment may join in (Bomb.explode)
	private static boolean shockProc = false;

	public static int bombDamage( Bomb bomb, Char ch, int cell, int dmg ){
		if (bomb.level() > 0) dmg = Math.round(dmg * (1 + 0.25f * bomb.level()));
		Hero hero = Dungeon.hero;
		boolean gren = bomb.xunGrenadier && hero != null;

		if (gren && hero.hasTalent(Talent.XUN_SHOCKWAVE) && ch.alignment != Char.Alignment.ALLY
				&& hero.buff(ShockwaveCooldown.class) == null && ch.isAlive()){
			int strength = 1 + hero.pointsInTalent(Talent.XUN_SHOCKWAVE);
			int dist = Dungeon.level.distance(ch.pos, cell);
			if (dist == 0 && hero.pos != ch.pos){
				Ballistica trajectory = new Ballistica(hero.pos, ch.pos, Ballistica.WONT_STOP);
				trajectory = new Ballistica(ch.pos, trajectory.collisionPos, Ballistica.PROJECTILE);
				WandOfBlastWave.throwChar(ch, trajectory, strength, false, true, bomb);
				shockProc = true;
			} else if (dist == 1){
				Ballistica trajectory = new Ballistica(ch.pos, 2 * ch.pos - cell, Ballistica.PROJECTILE);
				WandOfBlastWave.throwChar(ch, trajectory, strength / 2, false, true, bomb);
				shockProc = true;
			}
		}

		if (!gren || hero.subClass != HeroSubClass.GRENADIER || ch.alignment == Char.Alignment.ALLY){
			dmg -= ch.drRoll();
		}

		if (gren && hero.hasTalent(Talent.EXPLOSION_PROOF) && ch.alignment == Char.Alignment.ALLY){
			dmg /= 3;
		}

		if (hero != null && hero.hasTalent(Talent.MAGICAL_EXPLOSION) && ch.alignment != Char.Alignment.ALLY){
			MultiTool tool = hero.belongings.getItem(MultiTool.class);
			if (tool == null && hero.belongings.weapon() instanceof MultiTool) tool = (MultiTool) hero.belongings.weapon();
			if (tool != null && tool.enchantment != null){
				if (Random.Float() < hero.pointsInTalent(Talent.MAGICAL_EXPLOSION) * 0.3f){
					dmg = tool.enchantment.proc(tool, hero, ch, dmg);
				}
				if (tool.enchantment instanceof Projecting){
					dmg += Math.round(dmg * hero.pointsInTalent(Talent.MAGICAL_EXPLOSION) / 15f);
				}
			}
		}
		return dmg;
	}

	//after the blast, Shockwave's cooldown starts
	public static void afterBombExploded( Bomb bomb ){
		if (shockProc && Dungeon.hero != null){
			Buff.prolong(Dungeon.hero, ShockwaveCooldown.class, 50);
		}
		shockProc = false;
	}

	//---------------------------------------------------------------- talents that were only partly wired before
	//Aggressive Roadblock, enemies turn on the Explorer's barricades before his other allies (Mob.chooseEnemy)
	public static boolean barricadeInSight( Mob mob, Char enemy ){
		if (enemy == null || enemy.alignment != Char.Alignment.ALLY || enemy instanceof XunBarricade
				|| pts(Talent.AGGRESSIVE_ROADBLOCK) < 1 || mob.fieldOfView == null) return false;
		for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])){
			if (m instanceof XunBarricade && m.alignment == Char.Alignment.ALLY && m.pos >= 0
					&& m.pos < mob.fieldOfView.length && mob.fieldOfView[m.pos]) return true;
		}
		return false;
	}

	public static Char preferBarricade( Mob mob, Char closest, java.util.Collection<Char> enemies ){
		if (closest == null || closest.alignment != Char.Alignment.ALLY || closest instanceof XunBarricade
				|| pts(Talent.AGGRESSIVE_ROADBLOCK) < 1 || mob.fieldOfView == null) return closest;
		for (Char ch : enemies){
			if (ch instanceof XunBarricade && ch.alignment == Char.Alignment.ALLY && mob.fieldOfView[ch.pos]) return ch;
		}
		return closest;
	}

	//Corpse Decay +2 and +3 and Soul Caging +3, before an enemy's death is processed (Mob.die)
	public static void beforeMobDies( Mob mob ){
		Hero hero = Dungeon.hero;
		if (hero == null || Dungeon.level == null) return;
		if (mob.alignment == Char.Alignment.ENEMY && hero.pointsInTalent(Talent.CORPSE_DECAY) >= 2){
			GameScene.add(Blob.seed(mob.pos, 8 * hero.pointsInTalent(Talent.CORPSE_DECAY), com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas.class));
		}
		if (mob.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom.class) != null && hero.pointsInTalent(Talent.SOUL_CAGING) >= 3){
			Buff.affect(hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulHandle.class).gainEnergy(mob);
		}
	}

	//Charisma +2, an enemy killed by a charmed creature may rise charmed itself (Char.die)
	public static boolean charismaRevive( Char ch, Object src ){
		Hero hero = Dungeon.hero;
		if (hero == null || hero.pointsInTalent(Talent.CHARISMA) < 2 || !(ch instanceof Mob) || ch.alignment != Char.Alignment.ENEMY
				|| !(src instanceof Char) || ((Char) src).buff(ScrollOfSirensSong.Enthralled.class) == null
				|| ch.isImmune(ScrollOfSirensSong.Enthralled.class) || Random.Int(5) != 0) return false;
		for (Buff buff : ch.buffs()) if (buff.type == Buff.buffType.NEGATIVE) buff.detach();
		com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff.affectAndLoot((Mob) ch, hero, ScrollOfSirensSong.Enthralled.class);
		ch.HP = ch.HT;
		if (ch.sprite != null) ch.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(ch.HT), FloatingText.HEALING);
		return true;
	}

	//Charisma +3, charmed creatures heal 1% of their health each turn (ScrollOfSirensSong.Enthralled.act)
	public static boolean charismaHeal( ScrollOfSirensSong.Enthralled buff ){
		if (pts(Talent.CHARISMA) < 3 || buff.target == null) return false;
		Char t = buff.target;
		buff.xunToHeal += t.HT / 100f;
		int heal = (int) buff.xunToHeal;
		buff.xunToHeal -= heal;
		heal = Math.min(heal, t.HT - t.HP);
		if (heal > 0){
			t.HP += heal;
			if (t.sprite != null) t.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(heal), FloatingText.HEALING);
		}
		return true;
	}

	//Plague Eucharist +2 and +3, the Plague God casts and attacks faster in toxic gas (Wand.wandUsed, Hero.attackDelay)
	public static float plagueSpeed( Char ch, int needed ){
		if (!(ch instanceof Hero) || ((Hero) ch).pointsInTalent(Talent.PLAGUE_EUCHARIST) < needed || Dungeon.level == null) return 1f;
		Blob gas = Dungeon.level.blobs.get(ToxicGas.class);
		return gas != null && gas.volume > 0 && gas.cur[ch.pos] > 0 ? 1.12f : 1f;
	}

	//Bluring Body, weakened enemies also attack more slowly (Mob.attackDelay)
	public static float weakenedDelay( Mob mob ){
		if (mob.alignment != Char.Alignment.ENEMY || mob.buff(Weakness.class) == null) return 1f;
		return 1f + pts(Talent.BLURING_BODY) * 0.1f;
	}

	//Cursed Power. +1 cursed wands charge faster (Wand.Charger), +2 cursed rings work as if they were blessed (Ring.soloBonus),
	//+3 cursed artifacts lose their curse effects (Artifact.ArtifactBuff.isCursed and Artifact.status)
	public static float cursedWandCharge( Wand wand ){
		return wand.cursed && pts(Talent.CURSED_POWER) >= 1 ? 1.1f : 1f;
	}

	public static boolean cursedRingWorks( com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring ring ){
		return ring.cursed && pts(Talent.CURSED_POWER) >= 2;
	}

	public static boolean artifactCurseIgnored(){
		return pts(Talent.CURSED_POWER) >= 3;
	}

	//Multi Modify. The multitool's own modifications also work on the hero's other weapon (+1), armor (+2) and wands (+3)
	private static MultiTool heroTool(){
		Hero hero = Dungeon.hero;
		if (hero == null) return null;
		MultiTool tool = hero.belongings.getItem(MultiTool.class);
		if (tool == null && hero.belongings.weapon() instanceof MultiTool) tool = (MultiTool) hero.belongings.weapon();
		return tool;
	}

	public static boolean toolWeaponMod( Object item, com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon.Modification mod ){
		if (pts(Talent.MULTI_MODIFY) < 1) return false;
		MultiTool tool = heroTool();
		return tool != null && tool != item && tool.modify == mod;
	}

	public static boolean toolArmorMod( Armor.Modification mod ){
		if (pts(Talent.MULTI_MODIFY) < 2) return false;
		MultiTool tool = heroTool();
		return tool != null && tool.armorModify == mod;
	}

	public static boolean toolWandMod( Wand.Modification mod ){
		if (pts(Talent.MULTI_MODIFY) < 3) return false;
		MultiTool tool = heroTool();
		return tool != null && tool.wandModify == mod;
	}

	//weapon hits with the multitool's weapon modification (Weapon.proc)
	public static int toolWeaponProc( com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon wep, Char attacker, Char defender, int damage ){
		if (attacker != Dungeon.hero || pts(Talent.MULTI_MODIFY) < 1) return damage;
		MultiTool tool = heroTool();
		if (tool == null || tool == wep || tool.modify == null) return damage;
		com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon.Modification m = tool.modify;
		if (m == com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon.Modification.SHARP_BLADE){
			damage = Math.max(damage, Math.round((wep.min() + wep.max()) / 2f));
			tool.decreaseModDurability();
		} else if (m == com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon.Modification.LONG_HANDLE
				&& !Dungeon.level.adjacent(attacker.pos, defender.pos)){
			tool.decreaseModDurability();
		} else if (m == com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon.Modification.PNEUMATICS){
			tool.decreaseModDurability();
		}
		return damage;
	}

	//---------------------------------------------------------------- the Wavechaser's swimming
	//everything the Wavechaser does in water is 15% faster (Char.spend)
	public static float timeScale( Char ch ){
		if (ch instanceof Hero && ((Hero) ch).subClass == HeroSubClass.WAVECHASER && Dungeon.level != null
				&& ch.pos >= 0 && ch.pos < Dungeon.level.length() && Dungeon.level.map[ch.pos] == Terrain.WATER){
			return 1.15f;
		}
		return 1f;
	}
}
