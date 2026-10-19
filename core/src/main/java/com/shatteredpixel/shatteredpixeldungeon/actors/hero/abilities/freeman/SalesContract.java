/*
 * Onyx's Pixel Dungeon
 * Sales Contract, the Freeman's armor ability, from Transformation PD (dongyusangyu).
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.freeman;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DemonSpawner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class SalesContract extends ArmorAbility {

	{
		baseChargeUse = 90f;
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {
		if (target == null){
			return;
		}
		if (target == hero.pos){
			GLog.w(Messages.get(this, "self_target"));
			return;
		}
		Char c = Actor.findChar(target);
		if (c == null){
			GLog.w(Messages.get(this, "no_target"));
			return;
		}
		if (Dungeon.level.distance(target, hero.pos) > 1){
			GLog.w(Messages.get(this, "more_distance"));
			return;
		}
		if (!(c instanceof Mob) || c.alignment != Char.Alignment.ENEMY || c instanceof DemonSpawner
				|| c.properties().contains(Char.Property.BOSS)
				|| c.properties().contains(Char.Property.MINIBOSS)
				|| !c.buffs(ChampionEnemy.class).isEmpty()) {
			GLog.w(Messages.get(this, "strong_target"));
			return;
		}
		AllyBuff.affectAndLoot((Mob) c, hero, SaleSelf.class);
		if (hero.hasTalent(Talent.ELITE_RECRUIT)){
			int price = 250 - 50*hero.pointsInTalent(Talent.ELITE_RECRUIT);
			if (Dungeon.gold >= price){
				giveChampion((Mob) c);
				Dungeon.gold -= price;
				hero.sprite.showStatusWithIcon(CharSprite.NEUTRAL, Integer.toString(-price), FloatingText.GOLD);
			}
		}
		armor.charge -= chargeUse(hero);
		Talent.onArmorAbility(hero, chargeUse(hero));
		armor.updateQuickslot();
		hero.sprite.operate(hero.pos);
		hero.busy();
		hero.spendAndNext(Actor.TICK);
	}

	//Soul Contract, a contracted enemy comes back as a working ghost
	public static void onContractedDeath(Mob mob){
		Hero hero = Dungeon.hero;
		if (hero == null || mob instanceof WorkerWraith || mob.buff(SaleSelf.class) == null || !hero.hasTalent(Talent.SOUL_CONTRACT)) return;
		Wraith w = Wraith.spawnAt(mob.pos, WorkerWraith.class);
		if (w == null) return;
		Dungeon.gold += 100;
		Statistics.goldCollected += 100;
		hero.sprite.showStatusWithIcon(CharSprite.NEUTRAL, "100", FloatingText.GOLD);
		if (hero.pointsInTalent(Talent.SOUL_CONTRACT) > 1){
			Buff.affect(w, SaleSelf.class);
		} else {
			Buff.affect(w, Corruption.class);
		}
		if (hero.pointsInTalent(Talent.SOUL_CONTRACT) > 2){
			giveChampion(w);
		}
		if (hero.pointsInTalent(Talent.SOUL_CONTRACT) > 3){
			w.HT = mob.HT;
			w.HP = w.HT;
		}
		if (Dungeon.level.heroFOV[mob.pos]) {
			CellEmitter.get(mob.pos).burst(ShadowParticle.CURSE, 6);
			Sample.INSTANCE.play(Assets.Sounds.CURSED);
		}
	}

	//a random elite trait, always given (the game's own roll only fires under the champion challenge)
	private static void giveChampion(Mob m){
		Class<? extends ChampionEnemy> cls;
		switch (com.watabou.utils.Random.Int(6)){
			case 0: default: cls = ChampionEnemy.Blazing.class; break;
			case 1: cls = ChampionEnemy.Projecting.class; break;
			case 2: cls = ChampionEnemy.AntiMagic.class; break;
			case 3: cls = ChampionEnemy.Giant.class; break;
			case 4: cls = ChampionEnemy.Blessed.class; break;
			case 5: cls = ChampionEnemy.Growing.class; break;
		}
		Buff.affect(m, cls);
	}

	@Override
	public int icon() {
		return HeroIcon.SALESCONTRACT;
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.ELITE_RECRUIT, Talent.PRIM_ACCU, Talent.SOUL_CONTRACT, Talent.HEROIC_ENERGY};
	}

	public static class SaleSelf extends ScrollOfSirensSong.Enthralled {
		{
			type = buffType.NEGATIVE;
			announced = true;
		}
	}

	public static class WorkerWraith extends Wraith {
	}
}
