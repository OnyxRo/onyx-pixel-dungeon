/*
 * Onyx's Pixel Dungeon
 * Transformation PD's boss talents. A scroll of sublimation from a boss fills one of the Freeman's boss talent
 * slots with a talent of that boss. Called from the hooks in TransformationTalents.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogDzewa;
import com.shatteredpixel.shatteredpixeldungeon.items.ScrollOfSublimation;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TransformationBossTalents {

	//---- slots and the scroll of sublimation -------------------------------------------------

	public static boolean isSlot(Talent t){
		return t == Talent.BOSS_TALENT_SLOT_1 || t == Talent.BOSS_TALENT_SLOT_2 || t == Talent.BOSS_TALENT_SLOT_3
				|| t == Talent.BOSS_TALENT_SLOT_4 || t == Talent.BOSS_TALENT_SLOT_5;
	}

	public static Talent slotFor(String type){
		switch (type == null ? "" : type.toUpperCase(java.util.Locale.ENGLISH)){
			case "GOO": case "WARRIOR": return Talent.BOSS_TALENT_SLOT_1;
			case "TENGU": case "ROGUE": return Talent.BOSS_TALENT_SLOT_2;
			case "DM300": case "HUNTRESS": return Talent.BOSS_TALENT_SLOT_3;
			case "DWARFKING": case "KING": return Talent.BOSS_TALENT_SLOT_4;
			case "YOG": return Talent.BOSS_TALENT_SLOT_5;
			default: return null;
		}
	}

	public static Talent[] talentsFor(String type){
		switch (type == null ? "" : type.toUpperCase(java.util.Locale.ENGLISH)){
			case "GOO": return new Talent[]{Talent.AQUATIC_RECOVER, Talent.PUMP_ATTACK, Talent.OOZE_ATTACK};
			case "WARRIOR": return new Talent[]{Talent.STRONGEST_SHIELD, Talent.COMBO_PACKAGE, Talent.BREAK_ENEMY_RANKS};
			case "TENGU": return new Talent[]{Talent.SURPRISE_THROW, Talent.SMOKE_MASK, Talent.RUSH};
			case "ROGUE": return new Talent[]{Talent.SHADOW_KILLER, Talent.KILL_SPREE, Talent.SEAOFPEOPLE, Talent.PHANTOM_STEP};
			case "DM300": return new Talent[]{Talent.FASTING, Talent.THUNDER_STRIKE, Talent.DIRECTIONAL_COLLAPSE};
			case "HUNTRESS": return new Talent[]{Talent.HUNTING_TECHNIQUE, Talent.NATURAL_CHILD, Talent.FALCON_EYE};
			case "DWARFKING": case "KING": return new Talent[]{Talent.KING_PROTECT, Talent.SUMMON_FOLLOWER, Talent.WOLFISH_GAZE, Talent.ENERGY_CONVERSION};
			case "YOG": return new Talent[]{Talent.YOG_LARVA, Talent.YOG_FIST, Talent.YOG_RAY};
			default: return new Talent[0];
		}
	}

	//true when the hero has the empty slot this scroll fills
	public static boolean canSublimate(Hero h, String type){
		Talent slot = slotFor(type);
		if (slot == null) return false;
		for (LinkedHashMap<Talent, Integer> tier : h.talents) if (tier.containsKey(slot)) return true;
		return false;
	}

	public static void fillSlot(Hero h, String type, Talent chosen){
		Talent slot = slotFor(type);
		for (int i = 0; i < h.talents.size(); i++){
			LinkedHashMap<Talent, Integer> tier = h.talents.get(i);
			if (!tier.containsKey(slot)) continue;
			LinkedHashMap<Talent, Integer> newTier = new LinkedHashMap<>();
			for (Talent t : tier.keySet()) newTier.put(t == slot ? chosen : t, t == slot ? 0 : tier.get(t));
			h.talents.set(i, newTier);
			h.metamorphedTalents.put(slot, chosen);
			break;
		}
	}

	public static final String[] TYPES = {"GOO", "WARRIOR", "TENGU", "ROGUE", "DM300", "HUNTRESS", "DWARFKING", "YOG"};

	//the other talents of the boss this talent came from
	public static Talent[] groupOf(Talent t){
		for (String type : TYPES){
			for (Talent o : talentsFor(type)) if (o == t) return talentsFor(type);
		}
		return new Talent[0];
	}

	public static void replaceBossTalent(Hero h, Talent old, Talent replacement){
		for (int i = 0; i < h.talents.size(); i++){
			LinkedHashMap<Talent, Integer> tier = h.talents.get(i);
			if (!tier.containsKey(old)) continue;
			LinkedHashMap<Talent, Integer> newTier = new LinkedHashMap<>();
			for (Talent t : tier.keySet()) newTier.put(t == old ? replacement : t, tier.get(t));
			h.talents.set(i, newTier);
			break;
		}
		for (Talent slot : new ArrayList<>(h.metamorphedTalents.keySet())){
			if (h.metamorphedTalents.get(slot) == old) h.metamorphedTalents.put(slot, replacement);
		}
	}

	public static void dropSublimation(String type, int pos){
		Hero h = Dungeon.hero;
		if (h == null || Dungeon.level == null || !canSublimate(h, type)) return;
		Dungeon.level.drop(new ScrollOfSublimation().type(type), pos).sprite.drop();
	}

	//---- buffs -------------------------------------------------------------------------------

	public static class AquaticCounter extends CounterBuff {}
	public static class ComboCounter extends CounterBuff {}
	public static class JustMoved extends FlavourBuff {}
	public static class PhantomStep extends FlavourBuff {}
	public static class YogFistCooldown extends TransformationTalents.TimedCooldown {}
	public static class YogFistPower extends FlavourBuff {}
	public static class YogRayCooldown extends TransformationTalents.TimedCooldown {}

	//---- hooks -------------------------------------------------------------------------------

	public static void onHeroTurn(Hero h){
		if (h.hasTalent(Talent.AQUATIC_RECOVER) && Dungeon.level != null && h.pos >= 0 && Dungeon.level.water[h.pos] && !h.flying && h.HP < h.HT){
			Buff.count(h, AquaticCounter.class, h.pointsInTalent(Talent.AQUATIC_RECOVER));
			AquaticCounter c = h.buff(AquaticCounter.class);
			if (c != null && c.count() >= 10){
				h.heal(1);
				c.detach();
			}
		}
	}

	public static void onMove(Hero h){
		if (h.hasTalent(Talent.SURPRISE_THROW)) Buff.prolong(h, JustMoved.class, 1.5f);
	}

	public static float accuracy(Hero h){
		float acc = 1f;
		if (h.pointsInTalent(Talent.WOLFISH_GAZE) == 2) return Float.POSITIVE_INFINITY;
		if (h.pointsInTalent(Talent.WOLFISH_GAZE) == 1) acc *= 4f;
		if (h.hasTalent(Talent.SURPRISE_THROW) && h.buff(JustMoved.class) != null && h.belongings.attackingWeapon() instanceof MissileWeapon) acc *= 0.5f;
		return acc;
	}

	public static boolean phantomDodge(Hero h){
		return h.buff(PhantomStep.class) != null;
	}

	public static int armor(Hero h, int dr){
		if (h.hasTalent(Talent.BREAK_ENEMY_RANKS) && Dungeon.level != null){
			int n = visibleEnemies(h);
			for (int i = 0; i < n; i++) dr += Random.Int(4) + h.pointsInTalent(Talent.BREAK_ENEMY_RANKS) - 1;
		}
		return dr;
	}

	private static int visibleEnemies(Hero h){
		int n = 0;
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
			if (mob.alignment == Char.Alignment.ENEMY && Dungeon.level.heroFOV != null && Dungeon.level.heroFOV[mob.pos]) n++;
		}
		return n;
	}

	public static float speed(Hero h){
		float s = 1f;
		if (h.pointsInTalent(Talent.RUSH) == 1) s *= 1.5f;
		else if (h.pointsInTalent(Talent.RUSH) == 2) s *= 1.75f;
		if (h.hasTalent(Talent.SEAOFPEOPLE) && Dungeon.level != null){
			s *= 1f + (0.15f + 0.15f*h.pointsInTalent(Talent.SEAOFPEOPLE))*visibleEnemies(h);
		}
		return s;
	}

	public static boolean fasting(Hero h){
		int f = h.pointsInTalent(Talent.FASTING);
		return (f == 1 && h.HP >= h.HT) || (f == 2 && h.HP >= h.HT*0.8f);
	}

	public static int onAttackProc(Hero h, Char enemy, int dmg){
		boolean melee = !(h.belongings.attackingWeapon() instanceof MissileWeapon);
		if (melee && h.hasTalent(Talent.PUMP_ATTACK) && Random.Int(5) == 0) dmg *= 1 + h.pointsInTalent(Talent.PUMP_ATTACK);
		if (!melee && h.hasTalent(Talent.SURPRISE_THROW) && h.buff(JustMoved.class) != null) dmg = Math.round(dmg*(1f + 0.2f*h.pointsInTalent(Talent.SURPRISE_THROW)));
		if (h.hasTalent(Talent.SHADOW_KILLER) && enemy instanceof Mob && ((Mob) enemy).surprisedBy(h)){
			dmg = Math.round(dmg*(1f + 0.25f*h.pointsInTalent(Talent.SHADOW_KILLER))) + Math.round(enemy.drRoll()*(0.5f + 0.5f*h.pointsInTalent(Talent.SHADOW_KILLER)));
		}
		if (melee && h.hasTalent(Talent.OOZE_ATTACK) && Random.Int(4) <= h.pointsInTalent(Talent.OOZE_ATTACK)){
			Buff.affect(enemy, Ooze.class).set(15);
			Buff.affect(enemy, Viscosity.DeferedDamage.class).extend(10);
		}
		if (melee && h.hasTalent(Talent.COMBO_PACKAGE)){
			Buff.count(h, ComboCounter.class, 1);
			ComboCounter c = h.buff(ComboCounter.class);
			if (c != null && c.count() >= 8 - 2*h.pointsInTalent(Talent.COMBO_PACKAGE)){
				c.detach();
				Talent.onFoodEaten(h, 0, null);
			}
		}
		if (h.buff(YogFistPower.class) != null){
			Buff.affect(enemy, Blindness.class, 5);
			if (h.pointsInTalent(Talent.YOG_FIST) == 3) Buff.affect(enemy, Roots.class, 5);
		}
		if (h.hasTalent(Talent.THUNDER_STRIKE)){
			ArrayList<Char> hit = new ArrayList<>();
			if (h.pointsInTalent(Talent.THUNDER_STRIKE) >= 2) hit.add(enemy);
			for (int i : PathFinder.NEIGHBOURS8){
				Char ch = Actor.findChar(enemy.pos + i);
				if (ch != null && ch != h && ch.alignment != Char.Alignment.ALLY) hit.add(ch);
			}
			for (Char ch : hit) if (ch.isAlive()) ch.damage(Math.round(dmg*0.5f), new com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity());
			if (!hit.isEmpty()) Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
		}
		return dmg;
	}

	public static int onDefenseProc(Hero h, Char enemy, int damage){
		if (h.hasTalent(Talent.STRONGEST_SHIELD) && Random.Int(2) == 0) damage = Math.max(0, damage - h.drRoll());
		return damage;
	}

	public static int onHeroDamage(Hero h, int dmg, Object src){
		if (dmg <= 0) return dmg;
		if (h.pointsInTalent(Talent.STRONGEST_SHIELD) == 2 && !(src instanceof Char) && !(src instanceof Hunger) && Random.Int(2) == 0){
			dmg = Math.max(0, dmg - h.drRoll());
		}
		if (dmg > 4 && h.hasTalent(Talent.YOG_LARVA) && Dungeon.level != null){
			int count = Math.min(2, h.pointsInTalent(Talent.YOG_LARVA));
			for (int i = 0; i < count; i++){
				int cell = freeCell(h.pos);
				if (cell == -1) break;
				YogDzewa.Larva l = new YogDzewa.Larva();
				l.pos = cell;
				Buff.affect(l, ScrollOfSirensSong.Enthralled.class);
				GameScene.add(l);
				if (h.pointsInTalent(Talent.YOG_LARVA) == 3 && Random.Int(2) == 0) Buff.affect(l, ChampionEnemy.Giant.class);
			}
		}
		if (h.hasTalent(Talent.KING_PROTECT) && !(src instanceof Hunger) && !(src instanceof Viscosity.DeferedDamage)
				&& h.pointsInTalent(Talent.KING_PROTECT) + 1 >= Random.Int(4)){
			Buff.affect(h, Viscosity.DeferedDamage.class).extend(dmg);
			return 0;
		}
		return dmg;
	}

	public static void onKill(Hero h, Mob mob){
		if (h.hasTalent(Talent.KILL_SPREE)){
			Buff.affect(h, Healing.class).setHeal((int)((0.025f + 0.025f*h.pointsInTalent(Talent.KILL_SPREE))*mob.HT), 0.25f, 0);
			Buff.affect(h, ArtifactRecharge.class).extend(1 + h.pointsInTalent(Talent.KILL_SPREE));
		}
	}

	public static void onInvisibilityEnded(Hero h){
		if (h.hasTalent(Talent.PHANTOM_STEP)){
			Buff.affect(h, Haste.class, 1 + 2*h.pointsInTalent(Talent.PHANTOM_STEP));
			Buff.affect(h, PhantomStep.class, h.pointsInTalent(Talent.PHANTOM_STEP));
		}
	}

	public static void onArmorAbility(Hero h){
		int n = h.pointsInTalent(Talent.SUMMON_FOLLOWER);
		for (int i = 0; i < n; i++){
			int cell = freeCell(h.pos);
			if (cell == -1) break;
			Mob mob = Random.Int(2) == 0 ? new Monk() : new Warlock();
			mob.pos = cell;
			Buff.affect(mob, ScrollOfSirensSong.Enthralled.class);
			mob.state = mob.HUNTING;
			GameScene.add(mob);
		}
	}

	public static void onArtifactUsed(Hero h){
		if (!h.hasTalent(Talent.YOG_FIST) || h.buff(YogFistCooldown.class) != null) return;
		Buff.affect(h, YogFistCooldown.class, 20);
		Buff.affect(h, YogFistPower.class, 5);
		Buff.affect(h, Light.class, 40);
		if (h.pointsInTalent(Talent.YOG_FIST) >= 2) Buff.affect(h, FireImbue.class).set(10);
		if (h.pointsInTalent(Talent.YOG_FIST) == 3) Barkskin.conditionallyAppend(h, h.lvl/2, 1);
	}

	public static void onWandProc(Hero h, Char target){
		if (h.buff(YogFistPower.class) != null){
			Buff.affect(target, Blindness.class, 5);
			if (h.pointsInTalent(Talent.YOG_FIST) == 3) Buff.affect(target, Roots.class, 5);
		}
		if (!h.hasTalent(Talent.YOG_RAY) || h.buff(YogRayCooldown.class) != null || Dungeon.level == null) return;
		Buff.affect(h, YogRayCooldown.class, 20);
		int rays = 2*h.pointsInTalent(Talent.YOG_RAY) - 1;
		java.util.HashSet<Char> struck = new java.util.HashSet<>();
		for (int i = 0; i < rays; i++){
			int aim = i == 0 ? target.pos : target.pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
			Ballistica b = new Ballistica(h.pos, aim, Ballistica.WONT_STOP);
			if (h.sprite != null && h.sprite.parent != null){
				h.sprite.parent.add(new com.shatteredpixel.shatteredpixeldungeon.effects.Beam.DeathRay(h.sprite.center(),
						com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap.raisedTileCenterToWorld(b.collisionPos)));
			}
			for (int c : b.path){
				Char ch = Actor.findChar(c);
				if (ch != null && ch != h && ch.alignment != Char.Alignment.ALLY && ch.isAlive() && struck.add(ch)){
					ch.damage(Random.NormalIntRange(40, 50 + 10*h.pointsInTalent(Talent.YOG_RAY)), new com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye.DeathGaze());
				}
			}
		}
	}

	private static int freeCell(int pos){
		ArrayList<Integer> cells = new ArrayList<>();
		for (int i : PathFinder.NEIGHBOURS8){
			int c = pos + i;
			if (Actor.findChar(c) == null && Dungeon.level.passable[c]) cells.add(c);
		}
		return cells.isEmpty() ? -1 : Random.element(cells);
	}
}
