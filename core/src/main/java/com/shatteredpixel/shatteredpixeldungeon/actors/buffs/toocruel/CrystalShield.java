/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.toocruel;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.ShieldHalo;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;

//Crystalline Shelter and Crystalline Blood, a barrier that nullifies weak hits
public class CrystalShield extends Buff {

	{
		type = buffType.POSITIVE;
	}

	private static boolean seen( Char target ) {
		return Dungeon.level != null && target.sprite != null && Dungeon.level.heroFOV[target.pos];
	}

	public static void applyShieldingLayers( Char target, int amount, String text ) {
		for (int i = 0; i < amount; i++) {
			Buff.append( target, Layer.class ).fx( true );
		}
		if (seen( target )) {
			target.sprite.showStatusWithIcon( CharSprite.POSITIVE, text, FloatingText.SHIELDING );
			Sample.INSTANCE.play( Assets.Sounds.CHARGEUP, 1f, 1.5f );
		}
	}

	//returns true if the damage was blocked
	public static boolean blockIncomingDamage( Char target, int damage ) {
		Layer layer = target.buff( Layer.class );
		if (layer == null) return false;

		CrystalShield crystal = target.buff( CrystalShield.class );
		if (crystal != null && damage <= crystal.damageThreshold()) {
			if (seen( target )) target.sprite.showStatus( CharSprite.POSITIVE, Messages.get( CrystalShield.class, "blocked" ) );
			return true;
		}
		layer.detach();
		boolean more = target.buff( Layer.class ) != null;
		if (!more) {
			if (target.buff( DeathMarker.class ) != null) {
				//the damage call this came from handles the death
				target.HP = 0;
				return false;
			}
			if (crystal != null) crystal.breakShield();
		} else if (seen( target )) {
			Sample.INSTANCE.play( Assets.Sounds.SHATTER, 0.25f, 1.5f );
			target.sprite.showStatus( CharSprite.NEGATIVE, Messages.get( CrystalShield.class, "layer_broken" ) );
		}
		return true;
	}

	public int damageThreshold() {
		return Dungeon.scalingDepth() / 2 + 1;
	}

	@Override
	public boolean act() {
		if (target.buff( Layer.class ) == null) {
			applyShieldingLayers( target, 1, Messages.get( this, "restored" ) );
		}
		diactivate();
		return true;
	}

	public boolean active() {
		return target.buff( Layer.class ) != null;
	}

	public void breakShield() {
		timeToNow();
		postpone( 10f );
		if (seen( target )) {
			Splash.at( target.sprite.center(), 0x22ffe1, 10 );
			Sample.INSTANCE.play( Assets.Sounds.SHATTER );
			target.sprite.showStatus( CharSprite.NEGATIVE, Messages.get( CrystalShield.class, "shattered" ) );
		}
	}

	@Override
	public int icon() {
		return active() ? BuffIndicator.ARMOR : BuffIndicator.VULNERABLE;
	}

	@Override
	public void tintIcon( Image icon ) {
		icon.hardlight( 1f, 0.5f, 1.5f );
	}

	@Override
	public String desc() {
		if (active()) return Messages.get( this, "desc", damageThreshold() );
		return Messages.get( this, "desc_inactive", dispTurns( visualcooldown() ) );
	}

	//one layer, breaks on any hit the barrier did not nullify
	public static class Layer extends Buff implements TcBuff.DefRoll {

		{
			type = buffType.POSITIVE;
		}

		private ShieldHalo shield;

		@Override
		public void fx( boolean on ) {
			if (on) {
				if (target.sprite == null || target.sprite.parent == null || GameScene.scene == null) return;
				if (shield != null) shield.killAndErase();
				shield = new ShieldHalo( target.sprite );
				shield.hardlight( 0.5f, 1f, 2f );
				GameScene.effect( shield );
			} else if (shield != null) {
				shield.putOut();
				shield = null;
			}
		}

		@Override
		public int icon() {
			return BuffIndicator.ARMOR;
		}

		@Override
		public void tintIcon( Image icon ) {
			icon.hardlight( 0.5f, 1f, 2f );
		}

		@Override
		public String desc() {
			return target.buff( CrystalShield.class ) != null ? Messages.get( this, "desc" ) : Messages.get( this, "desc_standalone" );
		}

		//no dodging while a layer is up
		@Override
		public float defRollMultiplier( Char attacker ) {
			return 0f;
		}
	}

	//Crystalline Blood, the monster dies when its last layer breaks
	public static class DeathMarker extends Buff {
		{
			type = buffType.NEUTRAL;
		}

		@Override
		public int icon() {
			return BuffIndicator.CORRUPT;
		}

		@Override
		public void tintIcon( Image icon ) {
			icon.hardlight( 0.5f, 1f, 2f );
		}
	}
}
