/*
 * Onyx's Pixel Dungeon
 * The Rat King's Wrath from DERanged PD (from Rat King PD 2 by zrp200), GPL-3.0. Rewritten for this game
 * (2026-10-22), since the mod built it from helpers it added to Smoke Bomb, Shockwave, Elemental Blast and
 * Spectral Blades. Same order of events. A smoke bomb jump, a shockwave where the King lands, a blast of
 * magic at the enemies around him, then spectral blades at everyone he can see.
 */
package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ratking;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.SmokeBomb;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class KingsWrath extends ArmorAbility {

	{
		baseChargeUse = 50;
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(SmokeBomb.class, "prompt");
	}

	@Override
	public boolean useTargeting() {
		return false;
	}

	@Override
	public int icon() {
		return HeroIcon.DR_WRATH;
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.RK_AFTERSHOCK, Talent.RK_RAT_BLAST, Talent.RK_SMOKE_AND_MIRRORS, Talent.RK_SEA_OF_BLADES};
	}

	private static boolean shadowStep( Hero hero ){
		return hero.invisible > 0 && hero.hasTalent(Talent.RK_SMOKE_AND_MIRRORS);
	}

	@Override
	public float chargeUse( Hero hero ) {
		float use = super.chargeUse(hero);
		//shadow step, 16%/30%/41%/50% less charge
		if (shadowStep(hero)) use *= Math.pow(0.84, hero.pointsInTalent(Talent.RK_SMOKE_AND_MIRRORS));
		return use;
	}

	@Override
	public String shortDesc() {
		if (Dungeon.hero != null && shadowStep(Dungeon.hero)) return Messages.get(this, "desc_shadow_step");
		return super.shortDesc();
	}

	@Override
	protected void activate( ClassArmor armor, Hero hero, Integer target ) {
		if (target == null) return;

		boolean jump = target != hero.pos;
		if (jump && hero.rooted){
			PixelScene.shake( 1, 1f );
			return;
		}
		if (jump){
			PathFinder.buildDistanceMap(hero.pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null), 6);
			if (PathFinder.distance[target] == Integer.MAX_VALUE || !Dungeon.level.heroFOV[target]
					|| Actor.findChar(target) != null){
				GLog.w( Messages.get(SmokeBomb.class, "fov") );
				return;
			}
		}

		boolean shadow = shadowStep(hero);
		armor.charge -= chargeUse(hero);
		Item.updateQuickslot();

		//smoke bomb
		if (jump){
			if (!shadow){
				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
					if (Dungeon.level.adjacent(mob.pos, hero.pos) && mob.alignment != Char.Alignment.ALLY) {
						Buff.prolong(mob, Blindness.class, Blindness.DURATION / 2f);
						if (mob.state == mob.HUNTING) mob.state = mob.WANDERING;
						mob.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 4);
					}
				}
			}
			CellEmitter.get( hero.pos ).burst( Speck.factory( Speck.WOOL ), 10 );
			ScrollOfTeleportation.appear( hero, target );
			Sample.INSTANCE.play( Assets.Sounds.PUFF );
			Dungeon.level.occupyCell( hero );
			Dungeon.observe();
			GameScene.updateFog();
			if (shadow){
				//shadow step ends it here
				hero.next();
				return;
			}
		}

		Invisibility.dispel();

		//shockwave, 1/1-2/2/2-3/3 tiles, only after a jump
		if (jump){
			int radius = Math.round((2 + hero.pointsInTalent(Talent.RK_AFTERSHOCK)) / 2f);
			PixelScene.shake( 2, 0.5f );
			Sample.INSTANCE.play( Assets.Sounds.BLAST, 1f, 0.8f );
			CellEmitter.center( hero.pos ).burst( com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle.FACTORY, 30 );
			for (Char ch : enemiesNear(hero, radius)){
				ch.damage( Random.NormalIntRange(4 + hero.lvl / 2, 8 + hero.lvl), this );
				if (ch.isAlive() && Random.Int(5) < 1 + hero.pointsInTalent(Talent.RK_AFTERSHOCK)){
					Buff.prolong( ch, Paralysis.class, 2f );
				}
			}
		}

		//blast of magic at the enemies close by, stronger with Rat Blast
		ArrayList<Char> near = enemiesNear(hero, 2);
		if (!near.isEmpty()){
			Sample.INSTANCE.play( Assets.Sounds.CHARGEUP, 0.5f );
			float mult = 1f + 0.25f * hero.pointsInTalent(Talent.RK_RAT_BLAST);
			for (Char ch : near){
				if (!ch.isAlive()) continue;
				ch.sprite.emitter().burst( ShadowParticle.UP, 6 );
				ch.damage( Math.round(Random.NormalIntRange(3 + hero.lvl / 3, 6 + hero.lvl * 2 / 3) * mult), this );
			}
		}

		//spectral blades at everyone the King can see, 1x/1.5x/2x/2.5x/3x in total split between them
		ArrayList<Char> seen = enemiesNear(hero, 4 + 2 * hero.pointsInTalent(Talent.RK_SEA_OF_BLADES));
		ArrayList<Char> targets = new ArrayList<>();
		for (Char ch : seen) if (ch.isAlive() && hero.fieldOfView[ch.pos]) targets.add(ch);
		if (!targets.isEmpty()){
			float mult = Math.min(1f, (1 + 0.5f * hero.pointsInTalent(Talent.RK_SEA_OF_BLADES)) / targets.size());
			for (Char ch : targets){
				hero.attack( ch, mult, 0, Char.INFINITE_ACCURACY );
			}
		}

		if (jump && hero.hasTalent(Talent.RK_SMOKE_AND_MIRRORS)){
			float duration = 0.67f + hero.pointsInTalent(Talent.RK_SMOKE_AND_MIRRORS);
			Buff.affect(hero, Haste.class, duration);
			Buff.affect(hero, Invisibility.class, duration);
		}

		hero.sprite.idle();
		hero.spendAndNext( jump ? 2f : 1f );
	}

	private static ArrayList<Char> enemiesNear( Hero hero, int radius ){
		ArrayList<Char> out = new ArrayList<>();
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
			if (mob.alignment == Char.Alignment.ENEMY && mob.isAlive()
					&& Dungeon.level.distance(hero.pos, mob.pos) <= radius){
				out.add(mob);
			}
		}
		return out;
	}
}
