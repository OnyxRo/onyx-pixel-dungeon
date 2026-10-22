import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Necronomicon;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.RubbingsTome;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.watabou.utils.Bundle;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Headless check for Transformation PD's Necronomicon. The recipe, binding a soul and turning the death into an ally
 * and a wraith, that the mark does nothing once the book is gone, summoning wraiths on empty ground, the corpse
 * dust spawner of a cursed copy, and saving.
 */
public class NecronomiconTest {

	static int failures = 0, checks = 0;

	static void check( boolean ok, String what ) {
		checks++;
		if (!ok) {
			failures++;
			System.out.println( "FAIL: " + what );
		}
	}

	interface Step { void run() throws Exception; }

	//wraiths need the game screen for their sprite, so a headless run stops right after the first one appears
	static void headless( Step s ) throws Exception {
		try {
			s.run();
		} catch (Exception e) {
			Throwable c = e instanceof java.lang.reflect.InvocationTargetException ? e.getCause() : e;
			if (!(c instanceof NullPointerException) || !String.valueOf( c.getMessage() ).contains( "sprite" )) throw e;
		}
	}

	static int wraiths() {
		int n = 0;
		for (Mob m : Dungeon.level.mobs) if (m instanceof Wraith) n++;
		return n;
	}

	static Rat rat( Hero hero ) {
		Rat r = new Rat();
		r.pos = MobArenaTest.freeCellNextTo( hero.pos );
		r.sprite = TestBoot.sprite();
		r.sprite.ch = r;
		Dungeon.level.mobs.add( r );
		Actor.add( r );
		return r;
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = 616161L;
		Dungeon.init();
		MobArenaTest.level( 7 );
		Hero hero = Dungeon.hero;

		//text
		Necronomicon book = new Necronomicon();
		check( !book.name().contains( "NO TEXT" ) && !book.desc().contains( "NO TEXT" ), "book text" );

		//the recipe, corpse dust, a rubbings tome and an identified wand
		ArrayList<Item> in = new ArrayList<>();
		CorpseDust dust = new CorpseDust();
		RubbingsTome tome = new RubbingsTome();
		WandOfMagicMissile wand = new WandOfMagicMissile();
		wand.identify();
		in.add( dust ); in.add( tome ); in.add( wand );
		Necronomicon.Recipe recipe = new Necronomicon.Recipe();
		check( recipe.testIngredients( in ), "recipe accepts dust, tome and wand" );
		check( recipe.cost( in ) == 10, "recipe costs 10 energy" );
		boolean found = false;
		for (Recipe r : Recipe.findRecipes( in )) found |= r instanceof Necronomicon.Recipe;
		check( found, "the alchemy pot finds the recipe" );
		Item made = recipe.brew( in );
		check( made instanceof Necronomicon, "recipe makes a Necronomicon" );
		check( dust.quantity() == 0 && tome.quantity() == 0 && wand.quantity() == 0, "recipe uses the ingredients" );
		ArrayList<Item> bad = new ArrayList<>();
		bad.add( new CorpseDust() ); bad.add( new RubbingsTome() ); bad.add( new WandOfMagicMissile() );
		check( !recipe.testIngredients( bad ), "recipe refuses an unidentified wand" );

		//a rat without the mark dies as normal, the book changes nothing
		book = (Necronomicon) made;
		book.collect( hero.belongings.backpack );
		Rat plain = rat( hero );
		plain.die( hero );
		check( !Dungeon.level.mobs.contains( plain ), "an unmarked rat still dies" );

		//a soul bound rat comes back as an ally and leaves a wraith while the book is held
		int before = wraiths();
		final Rat bound = rat( hero );
		check( Necronomicon.isValidSoulBoundTarget( bound ), "a rat can be soul bound" );
		Buff.affect( bound, Necronomicon.SoulBound.class );
		headless( () -> bound.die( hero ) );
		check( Dungeon.level.mobs.contains( bound ) && bound.isAlive(), "the bound rat did not die" );
		check( bound.alignment == Char.Alignment.ALLY, "the bound rat serves the hero" );
		check( bound.buff( Necronomicon.SoulBound.class ) == null, "the mark is used up" );
		check( wraiths() == before + 1, "a corrupted wraith rose beside it" );
		bound.die( hero );
		check( !Dungeon.level.mobs.contains( bound ), "the ally dies for good the second time" );

		//without the book the mark does nothing
		book.detach( hero.belongings.backpack );
		check( !Necronomicon.held(), "the book is gone" );
		Rat lost = rat( hero );
		Buff.affect( lost, Necronomicon.SoulBound.class );
		lost.die( hero );
		check( !Dungeon.level.mobs.contains( lost ), "a marked rat dies when no one holds the book" );

		//summoning on empty ground, two charges for two or more corrupted wraiths
		book.collect( hero.belongings.backpack );
		hero.belongings.artifact = book;
		book.activate( hero );
		int charge = book.charge;
		before = wraiths();
		Method cast = Necronomicon.class.getDeclaredMethod( "resolveCast", Hero.class, int.class, boolean.class );
		cast.setAccessible( true );
		int empty = -1;
		for (int i = 0; i < Dungeon.level.length(); i++) {
			if (Dungeon.level.passable[i] && Actor.findChar( i ) == null && Dungeon.level.distance( i, hero.pos ) >= 3
					&& Necronomicon.summonCandidates( Dungeon.level, i ).size() >= 3) { empty = i; break; }
		}
		final int at = empty;
		final Necronomicon fb = book;
		headless( () -> cast.invoke( fb, hero, at, false ) );
		check( book.charge == charge - 2, "summoning uses two charges" );
		//without a game screen the wraith sprite stops the loop after the first one
		check( wraiths() >= before + 1, "summoning called a wraith" );

		//a cursed copy keeps the corpse dust spawner going
		book.cursed = true;
		Necronomicon.syncGhostSpawner( hero );
		check( hero.buff( CorpseDust.DustGhostSpawner.class ) != null, "a cursed book calls the dust wraiths" );
		book.cursed = false;

		//saving
		Bundle b = new Bundle();
		b.put( "book", book );
		Necronomicon back = (Necronomicon) b.get( "book" );
		check( back != null && back.charge == book.charge, "the book saves and loads" );

		System.out.println( "SUMMARY necronomicon: " + checks + " checks, failures: " + failures );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
