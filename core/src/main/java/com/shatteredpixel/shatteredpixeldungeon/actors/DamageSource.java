package com.shatteredpixel.shatteredpixeldungeon.actors;

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Shaman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDisintegration;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfPrismaticLight;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfTransfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;

import java.util.HashSet;

/**
 * Tower PD tags damage by the class that dealt it, so some enemies resist magic or poison. This is the part of
 * its table that uses classes from this game, the tower and arena entries were left out.
 */
public class DamageSource {

	public static final HashSet<Class> MAGICAL = new HashSet<>();
	static {
		MAGICAL.add( Charm.class );
		MAGICAL.add( Weakness.class );
		MAGICAL.add( Vulnerable.class );
		MAGICAL.add( Hex.class );
		MAGICAL.add( WandOfBlastWave.class );
		MAGICAL.add( WandOfDisintegration.class );
		MAGICAL.add( WandOfFireblast.class );
		MAGICAL.add( WandOfFrost.class );
		MAGICAL.add( WandOfLightning.class );
		MAGICAL.add( WandOfLivingEarth.class );
		MAGICAL.add( WandOfMagicMissile.class );
		MAGICAL.add( WandOfPrismaticLight.class );
		MAGICAL.add( WandOfTransfusion.class );
		MAGICAL.add( WandOfCorruption.class );
		MAGICAL.add( Blazing.class );
		MAGICAL.add( Shocking.class );
		MAGICAL.add( Grim.class );
		MAGICAL.add( DM100.LightningBolt.class );
		MAGICAL.add( Shaman.EarthenBolt.class );
		MAGICAL.add( Warlock.DarkBolt.class );
		MAGICAL.add( Eye.DeathGaze.class );
	}

	public static final HashSet<Class> ELEMENTAL = new HashSet<>();
	static {
		ELEMENTAL.add( Burning.class );
		ELEMENTAL.add( Fire.class );
		ELEMENTAL.add( Chill.class );
		ELEMENTAL.add( Frost.class );
		ELEMENTAL.add( Ooze.class );
		ELEMENTAL.add( Paralysis.class );
		ELEMENTAL.add( Poison.class );
		ELEMENTAL.add( Corrosion.class );
		ELEMENTAL.add( ToxicGas.class );
		ELEMENTAL.add( Electricity.class );
		ELEMENTAL.add( WandOfFireblast.class );
		ELEMENTAL.add( Blazing.class );
		ELEMENTAL.add( WandOfFrost.class );
		ELEMENTAL.add( Shocking.class );
		ELEMENTAL.add( DM100.LightningBolt.class );
		ELEMENTAL.add( WandOfLightning.class );
	}

	public static final HashSet<Class> POISON = new HashSet<>();
	static {
		POISON.add( Poison.class );
		POISON.add( ToxicGas.class );
		POISON.add( Corrosion.class );
	}
}
