import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Panic;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Reason;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Tincture;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RitualDagger;

import java.lang.reflect.Method;

/**
 * Headless check for the Reason pieces of Transformation PD that are ported without the mod's hero class,
 * Tincture and the Ritual Dagger. Both work with no Reason meter, and use it when a hero has one.
 *
 *   java -cp <classes>:<libs.jar>:<test classes> ReasonTest
 */
public class ReasonTest {

	static int failures = 0;

	static void check( boolean ok, String what ) {
		if (!ok) {
			failures++;
			System.out.println( "FAIL: " + what );
		}
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = 919191L;
		Dungeon.sandbox = true;
		Dungeon.init();
		MobArenaTest.level( 3 );
		Hero hero = Dungeon.hero;

		Method satisfy = Tincture.class.getDeclaredMethod( "satisfy", Hero.class );
		satisfy.setAccessible( true );

		//a hero with no Reason meter (every class here) just eats it
		check( hero.buff( Reason.class ) == null, "the test hero has a Reason meter at the start" );
		try {
			satisfy.invoke( new Tincture(), hero );
		} catch (Throwable t) {
			check( false, "Tincture failed for a hero without Reason, " + t );
		}

		//a hero with the meter gains 50 and loses Panic
		Reason r = Buff.affect( hero, Reason.class );
		r.reason = 20;
		Buff.affect( hero, Panic.class, Panic.DURATION );
		try {
			satisfy.invoke( new Tincture(), hero );
		} catch (Throwable t) {
			check( false, "Tincture failed for a hero with Reason, " + t );
		}
		check( r.reason == 70, "Tincture left Reason at " + r.reason + " instead of 70" );
		check( hero.buff( Panic.class ) == null, "Tincture did not remove Panic" );

		//Reason is capped at 100 and cannot go below 0
		Reason.gainReason( hero, 500 );
		check( r.reason == 100, "Reason went past 100, " + r.reason );
		Reason.loseReason( hero, 500 );
		check( r.reason == 0, "Reason went below 0, " + r.reason );
		Reason.gainReason( null, 5 );

		//the dagger counts kills only for a hero with the meter
		hero.remove( r );
		RitualDagger dagger = new RitualDagger();
		for (int i = 0; i < 20; i++) dagger.onKill( hero );
		check( !dagger.ritualReady(), "ritual counted kills for a hero without Reason" );
		Buff.affect( hero, Reason.class );
		for (int i = 0; i < 13; i++) dagger.onKill( hero );
		check( dagger.ritualReady(), "ritual not ready after 13 kills with Reason" );
		dagger.resetRitual( hero );
		check( !dagger.ritualReady(), "ritual still ready after a reset" );

		System.out.println( "SUMMARY reason failures: " + failures );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
