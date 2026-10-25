/*
 * Onyx's Pixel Dungeon
 *
 * magicling, the base class Magic Ling PD gives most of its bosses. Ported in a cut down form.
 * Kept: the boss properties and immunities, the stat block set by initBaseStatus and initStatus,
 * the health bar on reload, and the two champion rolls. Left out: the mod's ice coin and prop rewards,
 * its hard mode and boss rush extras (boss rush is never on here), and the night damage bonus.
 * The mod kept the stat block in static fields, so every boss shared the last one made. Here they are
 * per boss.
 */
package com.shatteredpixel.shatteredpixeldungeon.actors;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

abstract public class Boss extends Mob {

	//boss rush and the mod's developer conduct, never on here
	protected boolean SprintableModeBoolean = false;

	protected float baseMin; //smallest hit
	protected float baseMax; //largest hit
	protected float baseAcc; //accuracy
	protected float baseEva; //evasion
	protected float baseHT;  //health
	protected float baseMinDef; //smallest armor roll
	protected float baseMaxDef; //largest armor roll

	//the mod's venom gas counter, read by a few of its bosses
	protected int venodamage = 0;

	protected void initProperty() {
		properties.add(Property.BOSS);
		immunities.add(Grim.class);
		immunities.add(ScrollOfPsionicBlast.class);
		immunities.add(ScrollOfRetribution.class);
		immunities.add(Corruption.class);
	}

	protected void initBaseStatus(float min, float max, float acc, float eva, float ht, float mid, float mad) {
		baseMin = min;
		baseMax = max;
		baseAcc = acc;
		baseEva = eva;
		baseHT = ht;
		baseMinDef = mid;
		baseMaxDef = mad;
		//a boss that calls initStatus first got the previous boss's block in the mod, here it gets its own
		if (HT == 0) {
			defenseSkill = Math.round(eva);
			HP = HT = Math.round(ht);
		}
	}

	protected void initStatus(int exp) {
		defenseSkill = Math.round(baseEva);
		EXP = exp;
		HP = HT = Math.round(baseHT);
	}

	@Override
	public int damageRoll() {
		return Math.round(Random.NormalFloat( baseMin, baseMax ));
	}

	@Override
	public int attackSkill( Char target ) {
		return Math.round(baseAcc);
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Math.round(Random.NormalFloat(baseMinDef, baseMaxDef));
	}

	//the mod's boss rush reward, a few random items around the boss. Only called when boss rush is on, so never here
	public void GetBossLoot( int pos ){
		int count = Random.chances(new float[]{0, 0, 6, 3, 1});
		for (int i = 0; i < count; i++){
			int drop = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
			if (!Dungeon.level.passable[drop]) drop = pos;
			Dungeon.level.drop( Generator.random(), drop ).sprite.drop( pos );
		}
	}

	public boolean noDropIceCoin = false;

	private static final String NO_DROP_ICE_COIN_KEY = "noDropIceCoin";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(NO_DROP_ICE_COIN_KEY, noDropIceCoin);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		noDropIceCoin = bundle.getBoolean(NO_DROP_ICE_COIN_KEY);
		if (state != SLEEPING) BossHealthBar.assignBoss(this);
		if ((HP*2 <= HT)) BossHealthBar.bleed(true);
	}

	public void RollCS(){
		Class<? extends ChampionEnemy> buffCls;
		switch (Random.Int(3)){
			case 0: default: buffCls = ChampionEnemy.Blazing.class;    break;
			case 1:          buffCls = ChampionEnemy.Projecting.class; break;
			case 2:          buffCls = ChampionEnemy.Blessed.class;    break;
		}
		Buff.affect(this, buffCls);
		this.state = this.WANDERING;
	}

	//the mod's size champions are not here, this gives a second normal champion instead
	public void RollEX(){
		RollCS();
	}
}
