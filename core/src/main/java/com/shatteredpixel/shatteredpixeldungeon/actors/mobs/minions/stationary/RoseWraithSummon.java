/*
 * Onyx's Pixel Dungeon, from Summoning Pixel Dungeon by TrashboxBobylev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.minions.stationary;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Attunement;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

//Summoning PD gave Wraith a parent rose wraith field, here the summoned wraith is its own small class
public class RoseWraithSummon extends Wraith {

	public int minDamage = 1, maxDamage = 2, accuracy = 10;

	@Override
	public int damageRoll() {
		int i = Random.NormalIntRange(minDamage, maxDamage);
		if (Dungeon.hero != null && Dungeon.hero.buff(Attunement.class) != null) i *= Attunement.empowering();
		return i;
	}

	@Override
	public int attackSkill( Char target ) {
		return accuracy;
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( "summ_min", minDamage );
		bundle.put( "summ_max", maxDamage );
		bundle.put( "summ_acc", accuracy );
		bundle.put( "summ_def", defenseSkill );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		minDamage = bundle.getInt( "summ_min" );
		maxDamage = bundle.getInt( "summ_max" );
		accuracy = bundle.getInt( "summ_acc" );
		defenseSkill = bundle.getInt( "summ_def" );
	}

	public static Wraith summonAt( RoseWraith wraith ) {
		ArrayList<Integer> points = Level.getSpawningPoints( wraith.pos );
		if (points.isEmpty()) return null;

		RoseWraithSummon w = new RoseWraithSummon();
		w.minDamage = wraith.minDamage;
		w.maxDamage = wraith.maxDamage;
		w.accuracy = wraith.attackSkill( null );
		w.adjustStats( Dungeon.depth );
		w.defenseSkill = w.accuracy * 5;
		w.pos = points.get( Random.index( points ) );
		w.state = w.HUNTING;
		GameScene.add( w );
		Buff.affect( w, Corruption.class );
		w.sprite.alpha( 0 );
		w.sprite.parent.add( new AlphaTweener( w.sprite, 1, 0.5f ) );
		w.sprite.emitter().burst( ShadowParticle.CURSE, 5 );
		Sample.INSTANCE.play( Assets.Sounds.CURSED );
		return w;
	}
}
