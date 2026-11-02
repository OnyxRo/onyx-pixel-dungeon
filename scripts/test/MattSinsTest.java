import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Sins;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bulky;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ConsumingGreed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Greed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicInfuse;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Pride;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sloth;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPureMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSins;
import com.watabou.utils.Bundle;

//Matt Edition's Seven Sins and its two challenges, headless.
//scripts/test-one.sh MattSinsTest
public class MattSinsTest {

	static int failures = 0, checks = 0;

	static void check( boolean ok, String what ) {
		checks++;
		if (!ok) {
			failures++;
			System.out.println( "FAIL: " + what );
		}
	}

	static boolean hasText( String s ) {
		return s != null && !s.isEmpty() && !s.contains( "NO_TEXT" ) && !s.startsWith( "!!!" );
	}

	static void newRun( int sins, int challenges, long seed, int depth ) {
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = seed;
		Dungeon.sandbox = true;
		Dungeon.challenges = 0;
		SPDSettings.challenges( challenges );
		SPDSettings.sins( sins );
		Dungeon.init();
		Dungeon.depth = depth;
		Level l = Dungeon.newLevel();
		Dungeon.switchLevel( l, l.entrance() );
	}

	static boolean roundTrip( Buff b ) {
		Bundle bundle = new Bundle();
		bundle.put( "b", b );
		return bundle.get( "b" ).getClass() == b.getClass();
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		Thread watchdog = new Thread( () -> {
			try { Thread.sleep( 300_000 ); } catch (InterruptedException e) { return; }
			System.out.println( "FAIL: hang" );
			for (StackTraceElement e : Thread.getAllStackTraces().keySet().stream().filter( t -> t.getName().equals( "main" ) ).findFirst().get().getStackTrace()) System.out.println( "  at " + e );
			System.exit( 2 );
		} );
		watchdog.setDaemon( true );
		watchdog.start();

		//text
		check( Sins.NAME_IDS.length == Sins.MASKS.length, "sin names and masks line up" );
		check( Challenges.NAME_IDS.length == Challenges.MASKS.length, "challenge names and masks line up" );
		check( Challenges.MASKS.length == Challenges.MAX_CHALS, "MAX_CHALS counts every challenge" );
		for (String id : Sins.NAME_IDS) {
			check( hasText( Messages.get( Sins.class, id ) ) && hasText( Messages.get( Sins.class, id + "_desc" ) ), "text for sin " + id );
		}
		for (String id : new String[]{ "i_hate_myself", "consuming_greed" }) {
			check( hasText( Messages.get( Challenges.class, id ) ) && hasText( Messages.get( Challenges.class, id + "_desc" ) ), "text for challenge " + id );
		}
		check( hasText( Messages.get( WndSins.class, "title" ) ), "sins window title" );
		check( Challenges.IsChallengeUnlocked( Challenges.I_HATE_MYSELF ) && Challenges.IsChallengeUnlocked( Challenges.CONSUMING_GREED ), "Matt challenges are open" );

		try {
			//no sins, normal run
			newRun( 0, 0, 5, 3 );
			Hero hero = Dungeon.hero;
			check( Dungeon.sins == 0, "no sins by default" );
			check( hero.buff( Greed.class ) == null && hero.buff( Sloth.class ) == null && hero.buff( Pride.class ) == null, "no sin buffs without sins" );
			int normalExp = hero.maxExp();
			Item dagger = new Dagger();
			int normalPrice = Shopkeeper.sellPrice( dagger );

			//every sin and consuming greed
			int all = 0;
			for (int m : Sins.MASKS) all |= m;
			newRun( all, Challenges.CONSUMING_GREED, 5, 3 );
			hero = Dungeon.hero;
			check( Dungeon.sins == all, "all seven sins set" );
			check( Dungeon.isChallenged( Challenges.CONSUMING_GREED ), "consuming greed on" );
			check( hero.buff( Greed.class ) != null, "greed buff" );
			check( hero.buff( Sloth.class ) != null, "sloth buff" );
			check( hero.buff( Pride.class ) != null, "pride buff" );
			check( hero.buff( ConsumingGreed.class ) != null, "consuming greed buff" );
			check( hero.maxExp() < normalExp, "sloth lowers the experience needed, " + hero.maxExp() + " against " + normalExp );
			check( Shopkeeper.sellPrice( dagger ) >= normalPrice, "greed never lowers shop prices" );

			for (Buff b : new Buff[]{ hero.buff( Greed.class ), hero.buff( Sloth.class ), hero.buff( Pride.class ), hero.buff( ConsumingGreed.class ) }) {
				if (b == null) continue;
				check( roundTrip( b ), b.getClass().getSimpleName() + " saves and loads" );
				//Pride is hidden in the mod, no icon and no text
				if (!(b instanceof Pride)) check( hasText( b.name() ) && hasText( b.desc() ), b.getClass().getSimpleName() + " text" );
				try {
					for (int i = 0; i < 40; i++) b.act();
				} catch (NullPointerException e) {
					System.out.println( "HEADLESS (not counted) " + b.getClass().getSimpleName() + " act " + e );
				}
			}
			for (Class<? extends Buff> c : new Class[]{ Bulky.class, MagicInfuse.class }) {
				Buff b = Buff.affect( hero, (Class) c );
				check( roundTrip( b ) && hasText( b.name() ) && hasText( b.desc() ), c.getSimpleName() + " text and save" );
			}

			//the hero's damage with wrath and pride and the item rolls with desire, over several floors
			for (int d = 2; d <= 8; d++) {
				newRun( all, Challenges.I_HATE_MYSELF | Challenges.CONSUMING_GREED, 10 + d, d );
				check( Dungeon.level != null, "floor " + d + " builds with every sin" );
			}
			hero = Dungeon.hero;
			int dmg = hero.damageRoll();
			check( dmg >= 0, "damage roll with wrath and pride" );

			//the two magic armor scrolls
			ScrollOfMagicUpgrade s = new ScrollOfMagicUpgrade();
			ScrollOfPureMagic p = new ScrollOfPureMagic();
			check( hasText( s.name() ) && hasText( s.desc() ) && hasText( p.name() ) && hasText( p.desc() ), "magic scroll text" );
			LeatherArmor armor = new LeatherArmor();
			check( hasText( armor.info() ), "armor info with magic level text" );
		} catch (Throwable t) {
			failures++;
			System.out.println( "FAIL: exception " + t );
			t.printStackTrace( System.out );
		}

		SPDSettings.sins( 0 );
		SPDSettings.challenges( 0 );
		System.out.println( "MattSinsTest " + (checks - failures) + "/" + checks + " passed" );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
