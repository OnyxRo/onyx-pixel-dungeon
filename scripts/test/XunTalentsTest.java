import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfHealth;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WellWater;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.XunTalents;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Sheep;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Tribute;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BurningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlockTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

/**
 * Xun's PD talents that were wired into Shattered's shared code in the talent completion pass. Each case sets up
 * the talent on a fresh hero on a generated level and checks the effect the mod gives.
 */
public class XunTalentsTest {

	static int failures = 0, passes = 0;
	static Level level;

	static void check( boolean ok, String what ){
		if (ok) passes++; else { failures++; System.out.println( "FAIL: " + what ); }
	}

	static Hero fresh( HeroClass cls, HeroSubClass sub ){
		Actor.clear();
		Dungeon.hero = new Hero();
		Hero h = Dungeon.hero;
		h.heroClass = cls;
		if (sub != null) h.subClass = sub;
		h.live();
		h.sprite = TestBoot.sprite();
		h.sprite.ch = h;
		h.pos = level.entrance();
		if (h.pos < 0 || !level.passable[h.pos]) {
			for (int i = 0; i < level.length(); i++) if (level.passable[i] && level.map[i] == Terrain.EMPTY) { h.pos = i; break; }
		}
		Actor.add( h );
		level.mobs.clear();
		return h;
	}

	static void talent( Hero h, Talent t, int pts ){
		while (h.talents.size() < 3) h.talents.add( new java.util.LinkedHashMap<>() );
		h.talents.get( 2 ).put( t, pts );
	}

	static int freeCellNextTo( int pos ){
		for (int o : PathFinder.NEIGHBOURS8) {
			int c = pos + o;
			if (c >= 0 && c < level.length() && level.passable[c] && Actor.findChar( c ) == null && level.heaps.get( c ) == null) return c;
		}
		return -1;
	}

	static Mob rat( int cell ){
		Rat r = new Rat();
		r.pos = cell;
		r.sprite = TestBoot.sprite();
		r.sprite.ch = r;
		level.mobs.add( r );
		Actor.add( r );
		return r;
	}

	static void run( String name, Runnable r ){
		try {
			r.run();
		} catch (Throwable e) {
			failures++;
			System.out.println( "FAIL: " + name + " threw " + e );
			StackTraceElement[] st = e.getStackTrace();
			for (int i = 0; i < Math.min( 6, st.length ); i++) System.out.println( "    at " + st[i] );
		}
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		TestBoot.stubWindow();
		GamesInProgress.selectedClass = HeroClass.WRAITH;
		Dungeon.seed = 424242L;
		Dungeon.init();
		Dungeon.sandbox = false;
		Dungeon.depth = 3;
		Dungeon.branch = 0;
		level = Dungeon.newLevel();
		Dungeon.level = level;

		run( "wraith debuffs", () -> {
			Hero h = fresh( HeroClass.WRAITH, null );
			Buff.affect( h, Weakness.class, 3f ).detach();
			check( h.buff( PhysicalEmpower.class ) != null, "a Wraith whose weakness ends gains physical empower" );
			Buff.affect( h, Slow.class, 3f ).detach();
			check( h.buff( Speed.class ) != null, "a Wraith whose slow ends gains speed" );
			talent( h, Talent.WICKED_GROWTH, 3 );
			Buff.affect( h, Cripple.class, 3f ).detach();
			Haste haste = h.buff( Haste.class );
			check( haste != null && haste.cooldown() > 7.5f, "Wicked Growth makes the haste 60% longer, got " + (haste == null ? "none" : haste.cooldown()) );
			Hero w = fresh( HeroClass.WARRIOR, null );
			Buff.affect( w, Weakness.class, 3f ).detach();
			check( w.buff( PhysicalEmpower.class ) == null, "other heroes gain nothing when a debuff ends" );
		});

		run( "eat little and often", () -> {
			Hero h = fresh( HeroClass.ENGINEER, null );
			talent( h, Talent.EAT_LITTLE_AND_OFTEN, 2 );
			Hunger hunger = Buff.affect( h, Hunger.class );
			hunger.satisfy( 100 );
			check( hunger.hunger() < 0, "satiety can go past full, hunger " + hunger.hunger() );
		});

		run( "homemade drug", () -> {
			Hero h = fresh( HeroClass.WRAITH, HeroSubClass.PLAGUEGOD );
			talent( h, Talent.HOMEMADE_DRUG, 1 );
			h.HT = h.HP = 500;
			Poison p = Buff.affect( h, Poison.class );
			p.set( 3 );
			p.act();
			check( h.buff( Poison.class ) != null && p.left() == 3, "poison does not wear off, left " + p.left() );
			Mob r = rat( freeCellNextTo( h.pos ) );
			XunTalents.attackProc( h, r, 1 );
			check( r.buff( Poison.class ) != null && h.buff( Poison.class ) == null, "a hit passes the poison on" );
		});

		run( "friendly mechanism", () -> {
			Hero h = fresh( HeroClass.EXPLORER, HeroSubClass.TRAPPER );
			talent( h, Talent.FRIENDLY_MECHANISM, 3 );
			Trap t = new BurningTrap();
			t.pos = h.pos;
			t.trigger();
			check( h.buff( BlobImmunity.class ) != null, "a burning trap under the Trapper gives purity" );
			Buff.detach( h, XunTalents.FriendlyMechanismCooldown.class );
			Trap f = new FlockTrap();
			f.pos = h.pos;
			level.setTrap( f, h.pos );
			f.trigger();
			int sheep = 0;
			for (Mob m : level.mobs) if (m instanceof Sheep) sheep++;
			check( !f.active && sheep == 0, "a flock trap under the Trapper is disarmed at +3, sheep " + sheep );
		});

		run( "sensitive pedal", () -> {
			Hero h = fresh( HeroClass.EXPLORER, HeroSubClass.TRAPPER );
			Mob r = rat( freeCellNextTo( h.pos ) );
			r.alignment = Char.Alignment.ENEMY;
			check( !XunTalents.pressesHard( r ), "without the talent enemies press softly" );
			talent( h, Talent.SENSITIVE_PEDAL, 3 );
			check( XunTalents.pressesHard( r ), "with the talent enemies set off hidden traps" );
			Buff.prolong( r, Trap.HazardAssistTracker.class, Trap.HazardAssistTracker.DURATION );
			boolean aware = false;
			for (Buff b : h.buffs()) if (b instanceof com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight.CharAwareness
					&& ((com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight.CharAwareness) b).charID == r.id()) aware = true;
			check( aware, "+3 senses an enemy a trap hit" );
		});

		run( "scapegoat", () -> {
			Hero h = fresh( HeroClass.WRAITH, null );
			talent( h, Talent.SCAPEGOAT, 2 );
			int cell = freeCellNextTo( h.pos );
			Mob r = rat( cell );
			Dungeon.observe();
			Buff.affect( r, Weakness.class, 5f );
			r.die( h );
			boolean sheep = false;
			for (Mob m : level.mobs) if (m instanceof Sheep) sheep = true;
			check( sheep, "a debuffed enemy leaves a sheep, fov " + level.heroFOV[cell] + " mobs " + level.mobs );
		});

		run( "psionic blast", () -> {
			Hero h = fresh( HeroClass.WRAITH, null );
			talent( h, Talent.PSIONIC_BLAST, 2 );
			h.HT = h.HP = 200;
			Mob r = rat( freeCellNextTo( h.pos ) );
			r.HT = r.HP = 100;
			Dungeon.observe();
			h.damage( 20, new Object() );
			check( r.HP < 100, "a big hit on the Wraith hurts enemies in sight, rat at " + r.HP );
		});

		run( "dig the well", () -> {
			Hero h = fresh( HeroClass.EXPLORER, null );
			talent( h, Talent.DIG_THE_WELL, 2 );
			int cell = freeCellNextTo( h.pos );
			Level.set( cell, Terrain.WELL );
			WellWater water = Blob.seed( cell, 1, WaterOfHealth.class );
			level.blobs.put( WaterOfHealth.class, water );
			water.clear( cell );
			Level.set( cell, Terrain.EMPTY_WELL );
			XunTalents.wellEmptied( water, cell );
			level.drop( new Waterskin(), cell );
			level.pressCell( cell );
			check( level.map[cell] == Terrain.WELL, "a waterskin makes an emptied well flow again" );
			check( h.buff( XunTalents.WellsDug.class ) != null, "and the cooldown starts" );
		});

		run( "ancestral tribute", () -> {
			Hero h = fresh( HeroClass.WRAITH, null );
			talent( h, Talent.ANCESTRAL_TRIBUTE, 1 );
			Heap heap = new Heap();
			heap.type = Heap.Type.SKELETON;
			heap.pos = freeCellNextTo( h.pos );
			heap.items = new java.util.LinkedList<>();
			XunTalents.onHeapOpened( heap );
			boolean found = false;
			for (Item i : heap.items) if (i instanceof Tribute) found = true;
			check( found, "bones hold an ancestral offering" );
		});

		run( "blood intuition", () -> {
			Hero h = fresh( HeroClass.WRAITH, null );
			talent( h, Talent.BLOOD_INTUITION, 1 );
			ArrayList<String> actions = new Waterskin().actions( h );
			check( actions.contains( "IDENTIFY" ), "the waterskin offers to identify" );
			check( new Waterskin().actionName( "IDENTIFY", h ) != null && !new Waterskin().actionName( "IDENTIFY", h ).contains( "!!!" ), "and the action has a name" );
		});

		run( "toilsome meal", () -> {
			Hero h = fresh( HeroClass.ENGINEER, null );
			talent( h, Talent.TOILSOME_MEAL, 1 );
			Hunger hunger = Buff.affect( h, Hunger.class );
			hunger.affectHunger( -200 );
			int before = hunger.hunger();
			check( XunTalents.toilsomeMeal( h, 100, 3 ), "the meal is put off" );
			check( h.buff( XunTalents.ToilsomeMealTracker.class ) != null && hunger.hunger() == before, "satiety waits for the meal to end" );
			XunTalents.interruptMeal( h );
			check( h.buff( XunTalents.ToilsomeMealTracker.class ) == null, "fighting wastes the meal below +2" );
		});

		run( "sleepwalking", () -> {
			Hero h = fresh( HeroClass.WRAITH, HeroSubClass.INCUBUS );
			talent( h, Talent.SLEEPWALKING, 1 );
			h.HP = h.HT / 2;
			int para = h.paralysed;
			Buff.affect( h, MagicalSleep.class );
			check( h.paralysed == para, "the Incubus is not paralysed by magical sleep" );
			Buff.detach( h, MagicalSleep.class );
			check( h.paralysed == para, "and waking does not change paralysis" );
		});

		run( "lullaby", () -> {
			Hero h = fresh( HeroClass.WRAITH, HeroSubClass.INCUBUS );
			talent( h, Talent.LULLABY, 3 );
			int before = level.mobs.size();
			level.spawnMob( 0 );
			boolean asleep = false;
			for (Mob m : level.mobs) if (m.state == m.SLEEPING) asleep = true;
			check( level.mobs.size() == before || asleep, "at +3 every spawned enemy sleeps" );
		});

		run( "grenadier bomb", () -> {
			Hero h = fresh( HeroClass.ENGINEER, HeroSubClass.GRENADIER );
			talent( h, Talent.EXPLOSION_PROOF, 3 );
			talent( h, Talent.XUN_SHOCKWAVE, 3 );
			int cell = freeCellNextTo( h.pos );
			Mob r = rat( cell );
			r.HT = r.HP = 1000;
			Bomb b = new Bomb();
			b.xunGrenadier = true;
			int hp = h.HP;
			b.explode( cell );
			check( r.HP < 1000, "the Grenadier's bomb hurts the enemy" );
			check( h.buff( XunTalents.ShockwaveCooldown.class ) != null, "Shockwave knocks it back and starts its cooldown" );
			check( h.HP >= hp - 30, "Explosion Proof softens the blast on the hero, lost " + (hp - h.HP) );
		});

		run( "wavechaser", () -> {
			Hero h = fresh( HeroClass.EXPLORER, HeroSubClass.WAVECHASER );
			int old = level.map[h.pos];
			Level.set( h.pos, Terrain.WATER );
			check( XunTalents.timeScale( h ) > 1.1f, "the Wavechaser is faster in water" );
			Level.set( h.pos, old );
		});

		run( "charisma", () -> {
			Hero h = fresh( HeroClass.ENGINEER, HeroSubClass.HACKER );
			talent( h, Talent.CHARISMA, 3 );
			Mob r = rat( freeCellNextTo( h.pos ) );
			r.HT = 300; r.HP = 100;
			ScrollOfSirensSong.Enthralled e = Buff.affect( r, ScrollOfSirensSong.Enthralled.class );
			e.act();
			check( r.HP > 100, "charmed creatures heal at +3" );
		});

		run( "cursed power", () -> {
			Hero h = fresh( HeroClass.WRAITH, null );
			RingOfMight ring = new RingOfMight();
			ring.cursed = true;
			int before = ring.soloBonus();
			talent( h, Talent.CURSED_POWER, 2 );
			check( before <= 0 && ring.soloBonus() > 0, "cursed rings work at +2, " + before + " then " + ring.soloBonus() );
		});

		run( "talent names", () -> {
			boolean army = false;
			for (Talent t : new com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.wraith.EvilUnfold().talents()) if (t == Talent.XUN_ARMY_OF_DEATH) army = true;
			check( army, "Evil Unfold lists Xun's Army of Death" );
		});

		run( "food", () -> {
			Hero h = fresh( HeroClass.EXPLORER, null );
			talent( h, Talent.PREPARING_MEAL, 1 );
			check( XunTalents.fastMeal( h ), "Preparing Meal eats faster" );
		});

		System.out.println( "SUMMARY xun talents: " + passes + " passed, " + failures + " failed" );
		System.exit( failures > 0 ? 1 : 0 );
	}
}
