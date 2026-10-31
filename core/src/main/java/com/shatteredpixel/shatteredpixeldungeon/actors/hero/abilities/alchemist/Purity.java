package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.alchemist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

//Elemental PD's Alchemist armor power. The Alchemist becomes immune to gases, sets the cells around him
//alight and fills the area with toxic gas. The three talents were written for this game, the mod had none.
public class Purity extends ArmorAbility {

	{
		baseChargeUse = 35f;
	}

	@Override
	protected void activate( ClassArmor armor, Hero hero, Integer target ) {
		armor.charge -= chargeUse(hero);
		armor.updateQuickslot();

		Buff.affect(hero, BlobImmunity.class, BlobImmunity.DURATION + 2 * hero.pointsInTalent(Talent.CLEANSING_VAPOR));
		if (hero.hasTalent(Talent.CLEANSING_VAPOR)){
			for (Buff b : hero.buffs()){
				if (b.type == Buff.buffType.NEGATIVE && !(b instanceof com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger)){
					if (hero.pointsInTalent(Talent.CLEANSING_VAPOR) >= 2 || b instanceof com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff) b.detach();
				}
			}
		}

		int[] ring = hero.pointsInTalent(Talent.RING_OF_FLAME) >= 2 ? PathFinder.NEIGHBOURS25 : PathFinder.NEIGHBOURS9;
		for (int i : ring){
			int c = hero.pos + i;
			if (c < 0 || c >= Dungeon.level.length() || Dungeon.level.solid[c]) continue;
			GameScene.add(Blob.seed(c, 2 + hero.pointsInTalent(Talent.RING_OF_FLAME), Fire.class));
			CellEmitter.get(c).burst(FlameParticle.FACTORY, 5);
		}

		int amount = 300 + 20 * Dungeon.scalingDepth();
		if (hero.hasTalent(Talent.CAUSTIC_CLOUD)){
			int caustic = amount * hero.pointsInTalent(Talent.CAUSTIC_CLOUD) / 4;
			CorrosiveGas gas = Blob.seed(hero.pos, caustic, CorrosiveGas.class);
			gas.setStrength(1 + Dungeon.scalingDepth() / 4);
			GameScene.add(gas);
			amount -= caustic / 2;
		}
		GameScene.add(Blob.seed(hero.pos, amount, ToxicGas.class));
		Sample.INSTANCE.play(Assets.Sounds.GAS);

		hero.sprite.operate(hero.pos);
		Invisibility.dispel();
		hero.spendAndNext(Actor.TICK);
	}

	@Override
	public int icon() {
		return HeroIcon.NONE;
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.CAUSTIC_CLOUD, Talent.RING_OF_FLAME, Talent.CLEANSING_VAPOR, Talent.HEROIC_ENERGY};
	}
}
