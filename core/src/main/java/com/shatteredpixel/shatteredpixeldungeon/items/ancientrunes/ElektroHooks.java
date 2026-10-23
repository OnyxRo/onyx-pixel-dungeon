/*
 * Onyx's Pixel Dungeon
 * Hooks for the parts of Elektrocheckers PD (by Elektrochecker, GPL-3.0) that live in Shattered's own classes.
 * Each hook is a single line call from the shared file, so parallel ports merge cleanly.
 */

package com.shatteredpixel.shatteredpixeldungeon.items.ancientrunes;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfChaos;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCosmicTravel;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Corroding;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import java.util.ArrayList;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CrackedGeode;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class ElektroHooks {

	//a random ancient rune, using the mod's own weights (AncientRune.probabilities)
	public static AncientRune randomRune() {
		switch (Random.chances(AncientRune.probabilities)) {
			case 0: return new ContravariantAncientRune();
			case 1: return new CovariantAncientRune();
			case 2: return new NablaAncientRune();
			case 3: return new FieldOperatorAncientRune();
			case 4: return new PartialAncientRune();
			case 5: return new LaplaceAncientRune();
			case 6: return new PropagationAncientRune();
			default: return new ExistenceAncientRune();
		}
	}

	//Goo and Yog-Dzewa drop one ancient rune when they die, as in the mod
	public static void dropBossRune(Char boss) {
		if (Dungeon.level == null || boss == null) return;
		if (boss.sprite != null && boss.sprite.parent != null) {
			new Flare(3, 32).color(0xf7cb1b, true).show(boss.sprite, 2f);
		}
		Dungeon.level.drop(randomRune(), boss.pos);
	}

	//the Propagation rune gives extra talent points for a while
	public static int bonusTalentPoints(Hero hero, int tier) {
		PropagationAncientRune.AncientRunePropagationTracker t = hero.buff(PropagationAncientRune.AncientRunePropagationTracker.class);
		if (t != null && t.isBoosted(tier)) {
			return tier;
		}
		return 0;
	}

	//the Mage's Elemental Blast with the mod's two wands, true when the target counts as hit
	public static boolean elementalBlast(Class<?> wandCls, Char mob, float effectMulti) {
		if (wandCls == WandOfChaos.class) {
			if (mob.isAlive() && mob.alignment != Char.Alignment.ALLY) {
				Buff.affect(mob, Hex.class, 5 + effectMulti * 5f);
				return true;
			}
		} else if (wandCls == WandOfCosmicTravel.class) {
			if (mob instanceof Mob && ScrollOfTeleportation.teleportChar(mob)) {
				if (mob.isAlive() && mob.alignment != Char.Alignment.ALLY) {
					Mob m = (Mob) mob;
					if (m.state == m.HUNTING) m.state = m.WANDERING;
					m.beckon(Dungeon.level.randomDestination(mob));
					return true;
				}
			}
		}
		return false;
	}

	//the Duelist's Elemental Strike with a Corroding weapon fills the cone with corrosive gas
	public static boolean elementalStrike(Weapon.Enchantment ench, ArrayList<Integer> cells, float powerMulti) {
		if (!(ench instanceof Corroding)) return false;
		for (int cell : cells) {
			GameScene.add(Blob.seed(cell, Math.round(8 * powerMulti), CorrosiveGas.class)
					.setStrength((int) Math.floor((2 + Dungeon.scalingDepth() / 5) * powerMulti)));
		}
		return true;
	}

	//a cursed Cracked Geode stops the hero from casting spells
	public static boolean spellsBlocked(Hero hero) {
		CrackedGeode.geodeRecharge g = hero.buff(CrackedGeode.geodeRecharge.class);
		if (g != null && g.isCursed()) {
			GLog.n(Messages.get(CrackedGeode.class, "cursed_spells"));
			return true;
		}
		return false;
	}
}
