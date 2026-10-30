package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.DeisticPD;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

//Deistic Pixel Dungeon's race artifacts. Each one works in full only for its own race. Humans can use the
//basic power of the others. In a run with no Deistic race picked, anyone can use them in full.
public abstract class DeisticArtifact extends Artifact {

	{
		levelCap = 5;
		chargeCap = 3;
		charge = 3;
	}

	protected abstract String race();

	//turns for one charge
	protected float rechargeTime(){
		return 40f;
	}

	protected boolean full(){
		return DeisticPD.race() == null || DeisticPD.is(race());
	}

	protected boolean basic(){
		return full() || DeisticPD.is(DeisticPD.HUMAN);
	}

	protected boolean ready( Hero hero ){
		if (!isEquipped(hero)) { GLog.i(Messages.get(Artifact.class, "need_to_equip")); return false; }
		if (cursed) { GLog.w(Messages.get(DeisticArtifact.class, "cursed")); return false; }
		if (!basic()) { GLog.w(Messages.get(DeisticArtifact.class, "wrong_race")); return false; }
		if (charge <= 0) { GLog.i(Messages.get(DeisticArtifact.class, "no_charge")); return false; }
		return true;
	}

	protected void used( int expGain ){
		exp += expGain;
		if (exp >= 10 + level() * 10 && level() < levelCap){
			exp = 0;
			upgrade();
			chargeCap = 3 + level() / 2;
			GLog.p(Messages.get(DeisticArtifact.class, "levelup"));
		}
		updateQuickslot();
	}

	@Override
	public void charge( Hero target, float amount ) {
		if (charge < chargeCap){
			partialCharge += amount / rechargeTime();
			while (partialCharge >= 1 && charge < chargeCap){
				partialCharge--;
				charge++;
			}
			updateQuickslot();
		}
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new Recharge();
	}

	@Override
	public String desc() {
		String desc = super.desc();
		if (full()) desc += "\n\n" + Messages.get(this, "desc_full");
		else if (basic()) desc += "\n\n" + Messages.get(this, "desc_basic");
		else desc += "\n\n" + Messages.get(DeisticArtifact.class, "desc_none");
		if (cursed && isEquipped(Dungeon.hero)) desc += "\n\n" + Messages.get(DeisticArtifact.class, "desc_cursed");
		return desc;
	}

	public class Recharge extends ArtifactBuff {
		@Override
		public boolean act() {
			if (!isCursed() && charge < chargeCap && target instanceof Hero){
				charge((Hero) target, RingOfEnergy.artifactChargeMultiplier(target));
			}
			onTurn();
			spend(TICK);
			return true;
		}
	}

	protected void onTurn(){
	}
}
