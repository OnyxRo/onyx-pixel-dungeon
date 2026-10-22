import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Fatigue;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndChallenges;

/**
 * Headless check of Re-ARranged PD's five challenges, Superman, Pyromaniac, Cursed Dungeon, Fatigue and Mutation,
 * and of the strings of the Re-ARranged heroes option.
 */
public class RearrangedChallengesTest {

	static int failures = 0, checks = 0;

	static void check( boolean ok, String what ) {
		checks++;
		if (!ok) {
			failures++;
			System.out.println( "FAIL: " + what );
		}
	}

	static void newRun( int challenges, long seed ) {
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = seed;
		Dungeon.sandbox = true;
		Dungeon.challenges = 0;
		com.shatteredpixel.shatteredpixeldungeon.SPDSettings.challenges( challenges );
		Dungeon.init();
		Dungeon.depth = 3;
		Level l = Dungeon.newLevel();
		Dungeon.switchLevel( l, l.entrance() );
	}

	static Rat ratNextToHero() {
		Hero hero = Dungeon.hero;
		for (int n : com.watabou.utils.PathFinder.NEIGHBOURS8) {
			int c = hero.pos + n;
			if (Dungeon.level.passable[c] && Actor.findChar( c ) == null) {
				Rat r = new Rat();
				r.pos = c;
				r.HP = r.HT = 1000;
				Dungeon.level.mobs.add( r );
				Actor.add( r );
				return r;
			}
		}
		return null;
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		int[] masks = { Challenges.SUPERMAN, Challenges.PYRO, Challenges.CURSED_DUNGEON, Challenges.FATIGUE, Challenges.MUTATION, Challenges.REARRANGED_HEROES };
		for (int i = 0; i < Challenges.NAME_IDS.length; i++) {
			String name = Messages.get( Challenges.class, Challenges.NAME_IDS[i] );
			String desc = Messages.get( Challenges.class, Challenges.NAME_IDS[i] + "_desc" );
			check( !name.contains( "NO_TEXT" ) && !desc.contains( "NO_TEXT" ) && !name.startsWith( "challenges." ), "text for challenge " + Challenges.NAME_IDS[i] );
		}
		check( Challenges.NAME_IDS.length == Challenges.MASKS.length, "names and masks line up" );
		for (int m : masks) check( Challenges.IsChallengeUnlocked( m ), "challenge " + m + " is open" );

		try {
			newRun( 0, 1 );
			Hero hero = Dungeon.hero;
			int normalHT = hero.HT;
			float acc = hero.attackSkill( hero ), eva = hero.defenseSkill( hero );
			Rat r = ratNextToHero();
			int dmg = hero.attackProc( r, 5 );
			check( r.buff( Burning.class ) == null && hero.buff( Fatigue.class ) == null, "nothing changes with the challenges off" );

			newRun( Challenges.SUPERMAN, 1 );
			hero = Dungeon.hero;
			check( hero.HT == 10, "Superman gives 10 health, not " + hero.HT + " (normal " + normalHT + ")" );
			hero.lvl = 10; hero.updateHT( false );
			check( hero.HT == 10, "Superman health does not grow with level" );
			check( hero.attackSkill( hero ) > acc * 1.5f, "Superman doubles accuracy" );
			check( hero.defenseSkill( hero ) > eva * 2f, "Superman triples evasion" );
			r = ratNextToHero();
			check( hero.attackProc( r, 5 ) >= dmg * 3, "Superman triples damage" );

			newRun( Challenges.PYRO, 1 );
			hero = Dungeon.hero;
			r = ratNextToHero();
			hero.attackProc( r, 5 );
			check( r.buff( Burning.class ) != null, "Pyromaniac sets the enemy on fire" );

			newRun( Challenges.FATIGUE, 1 );
			hero = Dungeon.hero;
			r = ratNextToHero();
			for (int k = 0; k < 12; k++) {
				hero.attackProc( r, 1 );
				hero.defenseProc( r, 1 );
			}
			check( hero.buff( Fatigue.class ) != null, "Fatigue builds" );
			Food f = new Food();
			f.collect();
			java.lang.reflect.Method sat = Food.class.getDeclaredMethod( "satisfy", Hero.class );
			sat.setAccessible( true );
			sat.invoke( f, hero );
			check( hero.buff( Fatigue.class ) == null, "eating clears Fatigue" );

			newRun( Challenges.CURSED_DUNGEON, 1 );
			hero = Dungeon.hero;
			int wraiths = 0;
			for (int k = 0; k < 40; k++) {
				r = ratNextToHero();
				if (r == null) break;
				Actor.remove( r ); Dungeon.level.mobs.remove( r );
				try { com.shatteredpixel.shatteredpixeldungeon.RearrangedChallenges.onMobDied( r ); }
				catch (NullPointerException e) { if (e.getStackTrace()[0].toString().contains( "Wraith.spawnAt" )) wraiths++; else throw e; } //headless, the wraith has no sprite
				for (Mob m : Dungeon.level.mobs.toArray( new Mob[0] )) {
					if (m instanceof Wraith) {
						wraiths++;
						Actor.remove( m );
						if (m.sprite != null) m.sprite.killAndErase();
						Dungeon.level.mobs.remove( m );
					}
				}
			}
			check( wraiths > 5, "Cursed Dungeon leaves wraiths, " + wraiths + " of 40" );

			newRun( Challenges.MUTATION, 1 );
			hero = Dungeon.hero;
			int mutated = 0;
			for (int k = 0; k < 300; k++) {
				r = ratNextToHero();
				if (r == null) break;
				try { hero.attackProc( r, 1 ); } catch (NullPointerException e) { if (!e.toString().contains( "Game.instance" )) throw e; } //headless, trampling grass needs a scene
				hero.buffs( com.shatteredpixel.shatteredpixeldungeon.actors.buffs.chargearea.MutationBuff.class ).forEach( b -> b.detach() );
				if (!Dungeon.level.mobs.contains( r )) mutated++;
				else { Actor.remove( r ); Dungeon.level.mobs.remove( r ); }
				for (Mob m : Dungeon.level.mobs.toArray( new Mob[0] )) {
					if (m.pos != 0 && com.watabou.utils.PathFinder.NEIGHBOURS8 != null && Dungeon.level.adjacent( m.pos, hero.pos )) {
						Actor.remove( m ); Dungeon.level.mobs.remove( m );
					}
				}
			}
			check( mutated > 3, "Mutation turns enemies into others, " + mutated + " of 300" );
		} catch (Throwable t) {
			failures++;
			System.out.println( "FAIL: threw " + t );
			StackTraceElement[] st = t.getStackTrace();
			for (int i = 0; i < Math.min( 10, st.length ); i++) System.out.println( "        at " + st[i] );
		}
		System.out.println( "SUMMARY rearranged challenges: " + checks + " checks, failures: " + failures );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
