/*
 * Onyx's Pixel Dungeon
 * DERanged PD's changes to Shattered's characters (2026-10-22), each one a single call from the shared class.
 * Warp (the Kromer curse) warps enemies, the Scam buff spoils sell prices, a Nuclear Hatchet's Exposed doubles
 * damage over time, a Blooming Pick's vines soften blows. GPL-3.0.
 */
package com.shatteredpixel.shatteredpixeldungeon.actors;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HPDebuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AmnesiaBuff;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.GodfireParticle;
import com.watabou.noosa.audio.Sample;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.DamageOverTimeEffect;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ScamDebuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WarpDebuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WarpedEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.rka.BloomingPick;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.rka.NuclearHatchet;
import com.watabou.utils.Random;

public class DerangedChar {

	//damage taken, called from Char.damage
	public static float damageMult( Char ch, Object src ){
		float mult = 1f;
		if (ch.buff(NuclearHatchet.Exposed.class) != null
				&& (src instanceof DamageOverTimeEffect || src instanceof Buff.DOTbuff || src instanceof Blob)){
			mult *= 2.5f;
		}
		if (ch.buff(BloomingPick.VineCovered.class) != null) mult *= 0.25f;
		if (ch.buff(WarpedEnemy.class) != null) mult *= 0.75f;
		return mult;
	}

	//warped enemies act a quarter faster, called from Char.spend
	public static float timeScale( Char ch ){
		return ch.buff(WarpedEnemy.class) != null ? 1.25f : 1f;
	}

	//warped enemies weaken what they hit, called from Char.attackProc
	public static void attackProc( Char attacker, Char enemy ){
		if (attacker.buff(WarpedEnemy.class) != null && enemy != null && enemy.isAlive()){
			Buff.affect(enemy, Weakness.class, 3f);
		}
	}

	//a hero carrying warp sometimes meets warped enemies, called from ChampionEnemy.rollForChampion.
	//No roll at all without warp, so level generation stays the same
	public static void rollWarp( Mob m ){
		if (Dungeon.hero == null) return;
		WarpDebuff warp = Dungeon.hero.buff(WarpDebuff.class);
		if (warp == null) return;
		int amount = Math.round(5 + Math.min(35, warp.getStacks() / 4));
		if (Char.hasProp(m, Char.Property.BOSS)){
			amount = Math.round(10 + Math.min(45, warp.getStacks() / 5));
		}
		if (Random.Int(100) < amount){
			if (Char.hasProp(m, Char.Property.BOSS)) Buff.affect(m, WarpedEnemy.BossEffect.class);
			else Buff.affect(m, WarpedEnemy.class);
		}
	}

	//the No HP challenge, damage taken also lowers max HP for a while
	public static void onHeroDamaged( Hero hero, int dmg ){
		if (dmg > 0 && Dungeon.isChallenged(Challenges.NO_HP)){
			if (hero.sprite != null) hero.sprite.emitter().burst(GodfireParticle.FACTORY, 14);
			Sample.INSTANCE.play(Assets.Sounds.BLAST, 1f, 1.5f);
			Buff.count(hero, HPDebuff.class, dmg / 2f);
		}
	}

	public static int heroHT( Hero hero, int ht ){
		HPDebuff d = hero.buff(HPDebuff.class);
		return d == null ? ht : Math.max(1, ht - (int) d.count());
	}

	//the Forget Path challenge, the map slowly fades from memory
	public static void live( Hero hero ){
		if (Dungeon.isChallenged(Challenges.FORGET_PATH)){
			Buff.affect(hero, AmnesiaBuff.class);
		}
	}

	//shopkeepers pay anything from 1 gold up while the hero is scammed, called from Shopkeeper.sellPrice
	public static int sellPrice( int price ){
		if (Dungeon.hero != null && Dungeon.hero.buff(ScamDebuff.class) != null && price > 1){
			return Random.Int(1, (int)(price * 1.25f));
		}
		return price;
	}
}
