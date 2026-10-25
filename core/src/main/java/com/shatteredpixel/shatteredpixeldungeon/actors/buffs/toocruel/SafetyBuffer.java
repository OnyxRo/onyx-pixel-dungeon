/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.TooCruel;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

//Safety Buffer, charges that stun and then strike every monster the hero sees
public class SafetyBuffer extends Buff {

	{
		type = buffType.POSITIVE;
	}

	public static final int SMITE_DAMAGE = 10;
	private static final int MAX_STACKS = 10;

	public int stacks = MAX_STACKS;
	public int damage = SMITE_DAMAGE;

	public SafetyBuffer refresh() {
		stacks = MAX_STACKS;
		return this;
	}

	public SafetyBuffer set( int damage ) {
		if (damage > this.damage) this.damage = damage;
		return this;
	}

	@Override
	public boolean act() {
		if (stacks <= 0) {
			detach();
			return true;
		}
		boolean[] fov = TooCruel.fovOf( target );
		ArrayList<Mob> valid = new ArrayList<>();
		for (Mob mob : Dungeon.level.mobs) {
			if (mob != target && fov[mob.pos] && mob.alignment != Char.Alignment.ALLY && !(mob instanceof NPC)
					&& mob.buff( Smite.class ) == null && mob.buff( SmitePrep.class ) == null) {
				valid.add( mob );
			}
		}
		Random.shuffle( valid );
		for (Mob mob : valid) {
			Buff.affect( mob, SmitePrep.class ).set( damage );
		}
		spend( TICK );
		return true;
	}

	@Override
	public int icon() {
		return BuffIndicator.WEAPON;
	}

	@Override
	public void tintIcon( Image icon ) {
		icon.hardlight( 0x33FFFF );
	}

	@Override
	public float iconFadePercent() {
		return Math.max( 0f, (MAX_STACKS - stacks) / (float) MAX_STACKS );
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString( stacks );
	}

	@Override
	public String desc() {
		return Messages.get( this, "desc", stacks );
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( "stacks", stacks );
		bundle.put( "damage", damage );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		stacks = bundle.getInt( "stacks" );
		if (bundle.contains( "damage" )) damage = bundle.getInt( "damage" );
	}

	public static class SmitePrep extends Buff {
		public int damage = SMITE_DAMAGE;

		public SmitePrep set( int damage ) {
			this.damage = damage;
			return this;
		}

		@Override
		public boolean attachTo( Char target ) {
			if (super.attachTo( target )) {
				spend( TICK * Random.Float( 1f, 1.01f ) );
				return true;
			}
			return false;
		}

		@Override
		public boolean act() {
			Hero hero = Dungeon.hero;
			SafetyBuffer safety = hero == null ? null : hero.buff( SafetyBuffer.class );
			if (safety == null || safety.stacks <= 0 || !Dungeon.level.heroFOV[target.pos]) {
				detach();
				return true;
			}
			Buff.affect( target, Smite.class ).set( damage );
			Buff.affect( target, Paralysis.class, Paralysis.DURATION );
			Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
			if (hero.sprite != null && target.sprite != null && target.sprite.parent != null) {
				target.sprite.parent.add( new Lightning( hero.sprite.center(), target.sprite.center(), null ) );
			}
			safety.stacks--;
			detach();
			return true;
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( "damage", damage );
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			if (bundle.contains( "damage" )) damage = bundle.getInt( "damage" );
		}
	}

	public static class Smite extends Buff {
		public int damage = SMITE_DAMAGE;

		public Smite set( int damage ) {
			this.damage = damage;
			return this;
		}

		@Override
		public boolean attachTo( Char target ) {
			if (super.attachTo( target )) {
				spend( TICK );
				return true;
			}
			return false;
		}

		@Override
		public boolean act() {
			Paralysis p = target.buff( Paralysis.class );
			if (p != null) p.detach();
			target.damage( damage, this );
			if (target.sprite != null && target.sprite.parent != null && Dungeon.level.heroFOV[target.pos]) {
				PointF origin = target.sprite.center();
				origin.y -= target.sprite.camera() != null ? target.sprite.camera().screenHeight() : 100;
				target.sprite.parent.add( new Lightning( origin, target.sprite.center(), null ) );
				Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
				Sample.INSTANCE.play( Assets.Sounds.BLAST );
			}
			detach();
			return true;
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( "damage", damage );
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			if (bundle.contains( "damage" )) damage = bundle.getInt( "damage" );
		}
	}
}
