package com.shatteredpixel.shatteredpixeldungeon.actors;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AbilityCooldown;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Battlecry;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GoldArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.KeenEye;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Protected;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Rush;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Strength;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Undying;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WallStance;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.noosa.audio.Sample;

//Tower PD's hero spell buffs and the Tank's helmet, one line calls from Char
public class TowerChar {

	//an attacker's damage before armor. Strength and Battlecry raise it, Keen Eye makes every hit critical
	public static float attackDamage( Char attacker, float dmg ){
		if (attacker.buff( Strength.class ) != null) dmg *= 1.5f;
		if (attacker.buff( Battlecry.class ) != null) dmg *= 1.2f;
		if (attacker.buff( KeenEye.class ) != null){
			float mult = 1.5f;
			if (attacker instanceof Hero && ((Hero) attacker).belongings.weapon() instanceof MeleeWeapon){
				mult *= Math.max(1f, ((MeleeWeapon) ((Hero) attacker).belongings.weapon()).critMult);
			}
			dmg *= mult;
		}
		return dmg;
	}

	//after armor. Protected halves hits from anything that is not magical
	public static int afterArmor( Char attacker, Char enemy, int dmg ){
		if (enemy.buff( Protected.class ) != null && !DamageSource.MAGICAL.contains( attacker.getClass() )){
			dmg = Math.round( dmg * 0.5f );
		}
		return dmg;
	}

	public static float speed( Char ch, float speed ){
		if (ch.buff( Rush.class ) != null) speed *= 5f;
		return speed;
	}

	//Wall Stance halves how fast its holder acts
	public static float timeScale( Char ch, float timeScale ){
		if (ch.buff( WallStance.class ) != null) timeScale *= 0.5f;
		return timeScale;
	}

	//damage taken. Returns -1 when Gold Armor paid for the whole hit
	public static int damageTaken( Char ch, int dmg, Object src ){
		boolean magical = src != null && DamageSource.MAGICAL.contains( src.getClass() );
		if (ch.buff( WallStance.class ) != null && !magical) dmg /= 2;
		if (ch == Dungeon.hero && Dungeon.hero.heroClass == HeroClass.TANK && !magical){
			dmg = Math.round( dmg * 0.75f );
		}
		if (ch.buff( GoldArmor.class ) != null && dmg > 0){
			int cost = Math.round( dmg * 1.5f );
			if (Dungeon.gold >= cost){
				Dungeon.gold -= cost;
				Item.updateQuickslot();
				if (ch.sprite != null) ch.sprite.showStatus( CharSprite.WARNING, Integer.toString( dmg ) );
				return -1;
			} else {
				Buff.detach( ch, GoldArmor.class );
			}
		}
		return dmg;
	}

	//Undying brings the hero back at half health once, instead of dying
	public static boolean cheatDeath( Char ch ){
		if (!(ch instanceof Hero) || ch.buff( Undying.class ) == null) return false;
		ch.buff( Undying.class ).detach();
		ch.HP = Math.max( 1, ch.HT / 2 );
		if (ch.sprite != null) CellEmitter.center( ch.pos ).burst( ShadowParticle.CURSE, 20 );
		Sample.INSTANCE.play( Assets.Sounds.CURSED );
		Buff.affect( ch, Healing.class ).setHeal( ch.HT * 2, 0.05f, 5 );
		Buff.affect( ch, AbilityCooldown.class, 200 );
		return true;
	}
}
