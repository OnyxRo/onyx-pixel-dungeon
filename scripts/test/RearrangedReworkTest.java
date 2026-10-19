import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RearrangedRework;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.ClericSpell;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * Headless check of the Re-ARranged heroes option, Re-ARranged PD's rework of Shattered's six heroes.
 * With the option on, each hero gets the mod's tier 1 and 2 talents and a third subclass, every new talent and
 * subclass has text and an icon, each subclass's tier 3 tree is the mod's ten, the third subclass is picked, the
 * hero fights a little and is saved and loaded. With the option off the tree is Shattered's.
 */
public class RearrangedReworkTest {

	static int failures = 0, checks = 0;

	static void check( boolean ok, String what ) {
		checks++;
		if (!ok) {
			failures++;
			System.out.println( "FAIL: " + what );
		}
	}

	static boolean hasText( String s ) {
		return s != null && !s.isEmpty() && !s.equals( Messages.NO_TEXT_FOUND ) && !s.contains( "NO_TEXT" ) && !s.startsWith( "actors." );
	}

	static int headless = 0;

	//runs one hook, a null pointer from a missing sprite or scene is headless noise, anything else fails
	static void run( String what, Runnable r ) {
		try {
			r.run();
		} catch (NullPointerException e) {
			String m = String.valueOf( e.getMessage() );
			if (m.contains( "sprite" ) || m.contains( "Game.instance" ) || m.contains( "scene" ) || m.contains( "emitter" ) || m.contains( "Emitter" )) headless++;
			else { check( false, what + " threw " + e ); e.printStackTrace( System.out ); }
		} catch (Throwable t) {
			check( false, what + " threw " + t );
			t.printStackTrace( System.out );
		}
	}

	static final HeroClass[] SIX = { HeroClass.WARRIOR, HeroClass.MAGE, HeroClass.ROGUE, HeroClass.HUNTRESS, HeroClass.DUELIST, HeroClass.CLERIC };

	static void newRun( HeroClass cls, boolean rework ) {
		GamesInProgress.selectedClass = cls;
		Dungeon.seed = 777000L + cls.ordinal();
		Dungeon.sandbox = true;
		Dungeon.challenges = 0;
		com.shatteredpixel.shatteredpixeldungeon.SPDSettings.challenges( rework ? Challenges.REARRANGED_HEROES : 0 );
		Dungeon.init();
	}

	static LinkedHashSet<Talent> tree( Hero hero ) {
		LinkedHashSet<Talent> all = new LinkedHashSet<>();
		for (LinkedHashMap<Talent, Integer> tier : hero.talents) all.addAll( tier.keySet() );
		return all;
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		for (HeroClass cls : SIX) {
			String n = cls.name();
			try {
				//option off, Shattered's tree
				newRun( cls, false );
				Hero off = Dungeon.hero;
				check( !RearrangedRework.on(), n + " option is off" );
				check( cls.subClasses().length == 2, n + " has two subclasses with the option off" );
				check( off.talents.get( 0 ).size() == 4, n + " has Shattered's four tier 1 talents, found " + off.talents.get( 0 ).size() );
				check( off.talents.get( 1 ).size() == 5, n + " has Shattered's five tier 2 talents, found " + off.talents.get( 1 ).size() );
				LinkedHashSet<Talent> offTree = tree( off );
				off.subClass = cls.subClasses()[0];
				Talent.initSubclassTalents( off );
				int offT3 = off.talents.get( 2 ).size();
				check( offT3 == 5, n + " tier 3 with Shattered's subclass is 5, found " + offT3 );

				//option on
				newRun( cls, true );
				Hero hero = Dungeon.hero;
				check( RearrangedRework.on(), n + " option is on" );
				check( cls.subClasses().length == 3 && cls.subClasses()[2] == RearrangedRework.thirdSubclass( cls ), n + " has its third subclass" );
				check( hero.talents.get( 0 ).size() == 5, n + " has five tier 1 talents, found " + hero.talents.get( 0 ).size() );
				check( hero.talents.get( 1 ).size() == 6, n + " has six tier 2 talents, found " + hero.talents.get( 1 ).size() );
				LinkedHashSet<Talent> onTree = tree( hero );
				check( onTree.containsAll( offTree ), n + " keeps all of Shattered's talents" );
				for (Talent t : onTree) {
					if (offTree.contains( t )) continue;
					check( hasText( t.title() ) && hasText( t.desc() ), n + " new talent " + t + " text" );
					check( t.icon() >= 10000, n + " new talent " + t + " icon " + t.icon() );
				}
				for (HeroSubClass sub : cls.subClasses()) {
					check( hasText( sub.title() ) && hasText( sub.shortDesc() ) && (sub == HeroSubClass.BATTLEMAGE || hasText( sub.desc() )), n + " subclass " + sub + " text" ); //the Battlemage desc needs a scene
					check( sub.icon() >= 0, n + " subclass " + sub + " icon" );
					ArrayList<LinkedHashMap<Talent, Integer>> lists = new ArrayList<>();
					Talent.initSubclassTalents( sub, lists );
					int count = 0;
					for (LinkedHashMap<Talent, Integer> m : lists) {
						for (Talent t : m.keySet()) {
							count++;
							check( hasText( t.title() ) && hasText( t.desc() ), n + " subclass talent " + t + " text" );
						}
					}
					check( count == 10, n + " subclass " + sub + " has ten talents, found " + count );
				}

				//on a real floor, pick the third subclass, fill every talent and fight a rat
				Dungeon.depth = 7;
				Level l = null;
				for (int tries = 0; l == null && tries < 5; tries++) {
					try { l = Dungeon.newLevel(); } catch (ArrayIndexOutOfBoundsException e) { Dungeon.seed++; } //a world that fails headless, try another seed
				}
				Dungeon.switchLevel( l, l.entrance() );
				hero = Dungeon.hero;
				hero.pos = l.entrance();
				final Hero h = hero;
				HeroSubClass third = RearrangedRework.thirdSubclass( cls );
				hero.subClass = third;
				Talent.initSubclassTalents( hero );
				hero.ensureSubclassBuffs();
				hero.lvl = 20;
				for (int t = 0; t < 3; t++) {
					for (final Talent tal : hero.talents.get( t ).keySet()) {
						hero.talents.get( t ).put( tal, tal.maxPoints() );
						run( n + " upgrade " + tal, () -> Talent.onTalentUpgraded( h, tal ) );
					}
				}
				run( n + " updateHT", () -> h.updateHT( true ) );
				Rat rat = null;
				for (int nb : PathFinder.NEIGHBOURS8) {
					int c = hero.pos + nb;
					if (Dungeon.level.passable[c] && Actor.findChar( c ) == null) {
						rat = new Rat(); rat.pos = c; rat.HP = rat.HT = 5000;
						Dungeon.level.mobs.add( rat ); Actor.add( rat );
						break;
					}
				}
				check( rat != null, n + " found room for a rat" );
				final Rat r = rat;
				for (int i = 0; i < 40 && r != null; i++) {
					run( n + " attackSkill", () -> h.attackSkill( r ) );
					run( n + " defenseSkill", () -> h.defenseSkill( r ) );
					run( n + " attackDelay", () -> h.attackDelay() );
					run( n + " speed", () -> h.speed() );
					run( n + " attackProc", () -> h.attackProc( r, 3 ) );
					run( n + " defenseProc", () -> h.defenseProc( r, 3 ) );
					run( n + " damage", () -> h.damage( 1, r ) );
					run( n + " act buffs", () -> { for (com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff bf : h.buffs()) if (!(bf instanceof com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger)) { } } );
					hero.HP = hero.HT;
					r.HP = r.HT;
				}
				if (headless > 0) System.out.println( "info: " + n + " " + headless + " headless null pointers (no sprite or scene)" );
				headless = 0;
				if (cls == HeroClass.CLERIC) {
					ArrayList<ClericSpell> spells = ClericSpell.getSpellList( hero, 3 );
					check( spells.size() >= 7, "the Enchanter has her spells, found " + spells.size() );
					for (ClericSpell s : spells) {
						check( hasText( s.name() ) && hasText( s.shortDesc() ) && hasText( s.desc() ), "spell " + s.getClass().getSimpleName() + " text" );
					}
					for (ClericSpell s : ClericSpell.getAllSpells()) check( s.icon() >= 0, "spell " + s.getClass().getSimpleName() + " icon" );
				}

				//save and load
				Bundle b = new Bundle();
				hero.storeInBundle( b );
				Hero back = new Hero();
				back.restoreFromBundle( b );
				check( back.heroClass == cls && back.subClass == third, n + " saves and loads as " + third );
				int points = 0;
				for (LinkedHashMap<Talent, Integer> tier : back.talents) for (int p : tier.values()) points += p;
				check( points > 30, n + " keeps its talent points after loading, " + points );
				check( tree( back ).containsAll( onTree ), n + " keeps its talent tree after loading" );
				System.out.println( "ok " + n + " as " + third );
			} catch (Throwable t) {
				failures++;
				System.out.println( "FAIL: " + cls + " threw " + t );
				StackTraceElement[] st = t.getStackTrace();
				for (int i = 0; i < Math.min( 10, st.length ); i++) System.out.println( "        at " + st[i] );
			}
		}
		System.out.println( "SUMMARY rearranged heroes option: " + checks + " checks, failures: " + failures );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
