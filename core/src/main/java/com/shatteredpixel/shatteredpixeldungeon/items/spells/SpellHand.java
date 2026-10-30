/*
 * Clairvoyant Pixel Dungeon, ported into Onyx's Pixel Dungeon (key clairvoyant)
 * https://github.com/ifritdiezel/Clairvoyant-Pixel-Dungeon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StormCloud;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;

import java.util.ArrayList;

//Clairvoyant PD, the Apprentice's spell hand. A free, endless spell with a chaotic effect.
//On an ally it heals, on an enemy it may hurt or dizzy, on the caster it gives light (and a
//shield with Magic Shield), on a tile it may plant a seed, freeze, set off a trap, drop a
//runestone or call a storm.
public class SpellHand extends TargetedSpell {

	{
		image = ItemSpriteSheet.SPELL_HAND;
		usesTargeting = true;
		unique = true;
		bones = false;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		actions.remove(AC_DROP);
		actions.remove(AC_THROW);
		return actions;
	}

	@Override
	protected void affectTarget( Ballistica bolt, Hero hero ) {
		int cell = bolt.collisionPos;
		Char target = Actor.findChar(cell);
		int stab = hero.pointsInTalent(Talent.ARCANE_STABILIZATION);

		if (target instanceof Mob) {
			if (target.alignment == Char.Alignment.ALLY) {
				Emitter e = target.sprite == null ? null : target.sprite.emitter();
				if (e != null) e.burst(Speck.factory(Speck.HEALING), 3);
				target.HP = Math.min(target.HT, target.HP + Random.Int(5) + 1);
			} else {
				switch (Random.Int(4 - stab)) {
					case 0:
						target.damage(Random.NormalIntRange(0, 2 + hero.lvl / 2), this);
						break;
					case 1:
						Buff.affect(target, Vertigo.class, 1);
						break;
					default:
						break;
				}
			}
		} else if (target instanceof Hero) {
			Buff.affect(hero, Light.class, 8);
			Emitter e = hero.sprite == null ? null : hero.sprite.emitter();
			if (e != null) e.burst(Speck.factory(Speck.EVOKE), 1);
			if (hero.hasTalent(Talent.MAGIC_SHIELD)) {
				Buff.affect(hero, Barrier.class).setShield((int)(1 + hero.pointsInTalent(Talent.MAGIC_SHIELD)));
			}
		} else {
			int terr = Dungeon.level.map[cell];
			switch (Random.Int(9) - stab) {
				case 0: default:
					CellEmitter.get(cell).burst(ShadowParticle.MISSILE, 6);
					break;
				case 1:
					if (terr == Terrain.HIGH_GRASS) {
						Dungeon.level.plant((Plant.Seed) Generator.randomUsingDefaults(Generator.Category.SEED), cell);
						CellEmitter.get(cell).burst(LeafParticle.GENERAL, 6);
					}
					break;
				case 2:
					Splash.at(cell, 0x9999FF, 5);
					GameScene.add(Blob.seed(cell, 3, Freezing.class));
					break;
				case 3:
					Dungeon.level.pressCell(cell);
					break;
				case 4:
					if (terr == Terrain.EMPTY_DECO || hero.hasTalent(Talent.RUNE_IMBUEMENT)) {
						if (Random.Int(4 - hero.pointsInTalent(Talent.RUNE_IMBUEMENT)) == 0) {
							Dungeon.level.drop(Generator.random(Generator.Category.STONE), cell).sprite.drop();
						}
						break;
					}
				case 5:
					if (terr == Terrain.WATER) {
						GameScene.add(Blob.seed(cell, 10, StormCloud.class));
					}
					break;
			}
		}

		//the hand is never used up
		Invisibility.dispel();
		updateQuickslot();
		hero.spendAndNext(timeToCast());
	}

	@Override
	public int value() {
		return 0;
	}
}
