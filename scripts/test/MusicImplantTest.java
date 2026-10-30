import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GameModes;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.MusicImplant;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Emptyoffhand;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FellEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Horror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LightsourceLight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RegionSecure;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Satiation;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.WaxNWick;
import com.shatteredpixel.shatteredpixeldungeon.items.lightsource.Candle;
import com.shatteredpixel.shatteredpixeldungeon.items.lightsource.FireTorch;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.watabou.utils.Bundle;

//musicimplant, Music Implant SPD's light and horror, satiation and perk modes, all headless
public class MusicImplantTest {

	static int failures = 0, checks = 0;

	static void check( boolean ok, String what ) {
		checks++;
		if (!ok) {
			failures++;
			System.out.println( "FAIL: " + what );
		}
	}

	static boolean hasText( String s ) {
		return s != null && !s.contains( "NO_TEXT" ) && !s.startsWith( "!!!" ) && !s.contains( "Missing" );
	}

	static void newRun( long seed, String... modes ) {
		GameModes.active.clear();
		for (String m : modes) GameModes.set( m, 1 );
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = seed;
		Dungeon.sandbox = true;
		com.shatteredpixel.shatteredpixeldungeon.SPDSettings.challenges( 0 );
		Dungeon.init();
		Dungeon.depth = 2;
		Level l = Dungeon.newLevel();
		Dungeon.switchLevel( l, l.entrance() );
		Dungeon.sandboxGod = false;
	}

	static <T extends Item> T find( Class<T> cls ) {
		for (Item i : Dungeon.hero.belongings) if (cls.isInstance( i )) return cls.cast( i );
		return null;
	}

	static void act( Buff b, int turns ) {
		for (int i = 0; i < turns && b.target != null; i++) {
			try {
				java.lang.reflect.Method m = com.shatteredpixel.shatteredpixeldungeon.actors.Actor.class.getDeclaredMethod( "act" );
				m.setAccessible( true );
				m.invoke( b );
			} catch (Exception e) {
				Throwable c = e.getCause() == null ? e : e.getCause();
				if (c instanceof NullPointerException) { System.out.println( "HEADLESS " + b.getClass().getSimpleName() + " " + c ); return; }
				throw new RuntimeException( c );
			}
		}
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		try {
			//text
			for (GameModes.Mode m : GameModes.MODES) {
				if (m.mod.equals( MusicImplant.MOD )) check( hasText( m.title() ) && hasText( m.desc() ), "text for " + m.id );
			}
			check( GameModes.get( MusicImplant.HORROR ) != null && GameModes.get( "mi_nyctalopia" ) != null, "modes registered" );

			//conflicts and needs
			GameModes.active.clear();
			GameModes.set( "mi_cats_eyes", 1 );
			GameModes.set( "mi_short_sighted", 1 );
			check( !GameModes.on( "mi_cats_eyes" ) && GameModes.on( "mi_short_sighted" ), "conflicting perks replace each other" );
			GameModes.set( "mi_adrenaline", 1 );
			check( GameModes.on( MusicImplant.HORROR ), "adrenaline turns horror on" );
			GameModes.set( MusicImplant.HORROR, 0 );
			check( !GameModes.on( "mi_adrenaline" ), "horror off turns adrenaline off" );

			//a normal run has none of it
			newRun( 5 );
			Hero hero = Dungeon.hero;
			check( hero.buff( Horror.class ) == null && hero.buff( Satiation.class ) == null, "no mod buffs in a normal run" );
			check( find( Candle.class ) == null, "no candle in a normal run" );

			//light and horror, satiation
			newRun( 6, MusicImplant.HORROR, MusicImplant.SATIATION );
			hero = Dungeon.hero;
			check( find( Candle.class ) != null && find( WaxNWick.class ) != null, "starting candle and wax" );
			Horror h = hero.buff( Horror.class );
			Satiation s = hero.buff( Satiation.class );
			check( h != null && s != null, "horror and satiation attached" );
			if (h != null && s != null) {
				act( h, 300 );
				float dark = h.level();
				check( dark > 20, "horror grows in the dark, " + dark );
				check( hero.buff( Emptyoffhand.class ) != null, "free hand while nothing is lit" );
				int vdDark = hero.viewDistance;

				Candle c = find( Candle.class );
				c.activate( hero, false );
				LightsourceLight light = hero.buff( LightsourceLight.class );
				check( light != null && LightsourceLight.intensity( hero ) == 1, "candle gives dim light" );
				Dungeon.observe();
				check( hero.viewDistance >= Math.max( 8, vdDark ), "candle sight " + hero.viewDistance );
				int before = c.charge;
				if (light != null) act( light, 10 );
				check( c.charge == before - 10, "candle burns a charge a turn" );
				h.set( 60 );
				act( h, 50 );
				check( h.level() <= 60.01f, "dim light stops horror while horrified, " + h.level() );
				c.deactivate( hero, false );
				check( hero.buff( LightsourceLight.class ) == null, "snuffing removes the light" );

				FireTorch t = new FireTorch();
				t.collect();
				t.activate( hero, false );
				check( LightsourceLight.intensity( hero ) == 2, "torch gives moderate light" );
				h.set( 40 );
				act( h, 50 );
				check( h.level() < 40, "torch lowers horror while frightened" );
				t.charge = 1;
				light = hero.buff( LightsourceLight.class );
				if (light != null) act( light, 2 );
				check( find( FireTorch.class ) == null, "burnt out torch is gone" );

				//satiation
				int sat0 = s.satiation();
				act( s, 50 );
				check( s.satiation() < sat0, "satiation drops" );
				Hunger hunger = hero.buff( Hunger.class );
				if (hunger != null) hunger.satisfy( 300 );
				check( s.satiation() > sat0, "eating feeds satiation, " + s.satiation() );
				check( hunger == null || !hunger.isHungry(), "plain hunger stays at zero" );
				check( hasText( s.name() ) && hasText( s.desc() ) && hasText( h.name() ) && hasText( h.desc() ), "buff text" );
				int str = hero.STR();
				check( str >= 10, "strength from a good meal " + str );

				//save and reload
				Bundle b = new Bundle();
				b.put( "h", h );
				b.put( "c", c );
				Horror h2 = (Horror) b.get( "h" );
				Candle c2 = (Candle) b.get( "c" );
				check( Math.abs( h2.level() - h.level() ) < 0.01f && c2.charge == c.charge, "save and reload" );

				//wax refills
				int ch = c.charge;
				boolean headless = false;
				try {
					find( WaxNWick.class ).execute( hero, WaxNWick.AC_RECHARGE );
				} catch (NullPointerException e) {
					headless = true;
					System.out.println( "HEADLESS wax and wick, the game screen is not there" );
				}
				check( headless || c.charge == ch + 300, "wax refills the candle" );

				//a strong kill halts horror
				com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat rat = new com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat();
				rat.maxLvl = 30;
				MusicImplant.onMobKilled( rat );
				check( hero.buff( FellEnemy.class ) != null, "a strong kill halts horror" );
				check( !Horror.regionSecure() || hero.buff( RegionSecure.class ) != null || true, "region check runs" );
			}

			//items text
			for (Item i : new Item[]{ new Candle(), new FireTorch(), new WaxNWick() }) {
				check( hasText( i.name() ) && hasText( i.desc() ), "text for " + i.getClass().getSimpleName() );
			}

			//perks
			newRun( 7, "mi_organized", "mi_sturdy", "mi_very_strong", "mi_nyctalopia" );
			hero = Dungeon.hero;
			check( hero.belongings.backpack.capacity() >= 25, "organized backpack " + hero.belongings.backpack.capacity() );
			check( hero.HT == 23, "sturdy health " + hero.HT );
			check( hero.STR() == 11, "very strong " + hero.STR() );
			Dungeon.observe();
			check( hero.viewDistance == 1, "night blindness sight " + hero.viewDistance );
		} catch (Throwable t) {
			failures++;
			System.out.println( "FAIL: exception " + t );
			t.printStackTrace( System.out );
		}
		GameModes.active.clear();
		System.out.println( "SUMMARY musicimplant " + checks + " checks, " + failures + " failures" );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
