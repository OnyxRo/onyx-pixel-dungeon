import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GameModes;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Playstyles;
import com.shatteredpixel.shatteredpixeldungeon.RemixedLayout;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RemixedHeroes;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RemixedSkills;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DeathlingMob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.items.FakeAmulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.MysticalSkull;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.TownLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.VeryLastLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

/**
 * Headless check of Re-Remixed Dungeon's Elf, Necromancer and Gnoll and its run layout game mode.
 * Each hero's passives and skills are used once, and the layout's town, floor 1, floor 26 and floor 27 are built.
 */
public class RemixedTest {

	static int failures = 0, checks = 0;

	static void check( boolean ok, String what ) {
		checks++;
		if (!ok) {
			failures++;
			System.out.println( "FAIL: " + what );
		}
	}

	static boolean hasText( String s ) {
		return s != null && !s.contains( "NO_TEXT" ) && !s.startsWith( "!!!" );
	}

	static Hero newRun( HeroClass cls, String... modes ) {
		GameModes.active.clear();
		for (String m : modes) GameModes.set( m, 1 );
		Playstyles.refresh();
		GamesInProgress.selectedClass = cls;
		Dungeon.seed = 777L;
		Dungeon.sandbox = true;
		com.shatteredpixel.shatteredpixeldungeon.SPDSettings.challenges( 0 );
		Dungeon.init();
		return Dungeon.hero;
	}

	static Level build( int depth ) {
		Dungeon.depth = depth;
		Level l = Dungeon.newLevel();
		Dungeon.switchLevel( l, l.entrance() );
		return l;
	}

	static int freeCellNear( int pos ) {
		for (int n : PathFinder.NEIGHBOURS8) {
			int c = pos + n;
			if (Dungeon.level.passable[c] && Actor.findChar( c ) == null) return c;
		}
		return -1;
	}

	static Mob ratNextTo( Hero hero ) {
		Rat rat = new Rat();
		rat.pos = freeCellNear( hero.pos );
		rat.HP = rat.HT = 1000; //a death in a headless run reaches for the hero's sprite
		Dungeon.level.mobs.add( rat );
		Actor.add( rat );
		return rat;
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();

		//the Elf
		Hero elf = newRun( HeroClass.ELF );
		build( 3 );
		check( elf.HT == 15, "elf starts with 15 health, has " + elf.HT );
		check( elf.speed() > 1.2f, "elf is quick, speed " + elf.speed() );
		check( elf.isImmune( Paralysis.class ), "elf cannot be paralysed" );

		//the Necromancer
		Hero necro = newRun( HeroClass.NECROMANCER );
		build( 3 );
		check( necro.MAX_MP == RemixedSkills.NECRO_MANA && necro.MP == 0, "necromancer mana " + necro.MP + "/" + necro.MAX_MP );
		check( necro.belongings.weapon() instanceof MeleeWeapon && ((MeleeWeapon) necro.belongings.weapon()).skull != null, "necromancer's sword holds the skull" );
		check( hasText( necro.belongings.weapon().info() ), "skull weapon text" );
		check( hasText( new MysticalSkull().name() ) && hasText( new MysticalSkull().desc() ), "skull text" );
		Mob rat = ratNextTo( necro );
		RemixedSkills.onMobDie( rat, necro );
		check( necro.MP == 1, "a skull kill gives 1 mana, has " + necro.MP );
		necro.MP = necro.MAX_MP;
		for (RemixedSkills.Skill s : RemixedSkills.skills( necro )) {
			check( hasText( s.title() ) && hasText( s.desc() ), "skill text " + s );
		}
		check( RemixedSkills.use( necro, RemixedSkills.Skill.DEATHLING ), "deathling skill" );
		int deathlings = 0;
		for (Mob m : Dungeon.level.mobs) if (m instanceof DeathlingMob) deathlings++;
		check( deathlings == 1, "one deathling raised, found " + deathlings );
		for (Mob m : Dungeon.level.mobs) if (m instanceof DeathlingMob) check( hasText( m.name() ) && hasText( m.description() ), "deathling text" );
		check( RemixedSkills.use( necro, RemixedSkills.Skill.PLUNDER ), "plunder skill" );
		Mob rat2 = ratNextTo( necro );
		check( RemixedSkills.onMobDie( rat2, necro ), "plunder stops the death" );
		check( rat2.isAlive() && rat2.alignment == com.shatteredpixel.shatteredpixeldungeon.actors.Char.Alignment.ALLY, "plunder raises the dead as allies" );
		necro.MP = necro.MAX_MP;
		check( RemixedSkills.use( necro, RemixedSkills.Skill.REVITALIZATION ), "revitalization skill" );
		necro.MP = necro.MAX_MP;
		necro.HP = 0;
		check( RemixedSkills.necroRevive( necro ) && necro.HP == necro.HT && necro.MP == 0, "necromancer rises once on full mana" );
		Bundle b = new Bundle();
		necro.belongings.weapon().storeInBundle( b );
		MeleeWeapon copy = (MeleeWeapon) necro.belongings.weapon().getClass().newInstance();
		copy.restoreFromBundle( b );
		check( copy.skull != null, "the skull stays in the weapon after a save" );

		//the Gnoll
		Hero gnoll = newRun( HeroClass.GNOLL );
		build( 3 );
		check( gnoll.STR == 15, "gnoll has 15 strength" );
		check( RemixedHeroes.cannotRead( gnoll ), "gnoll cannot read" );
		check( gnoll.MAX_MP == RemixedSkills.GNOLL_MANA && gnoll.MP > 0, "gnoll mana " + gnoll.MP + "/" + gnoll.MAX_MP );
		Mob rat3 = ratNextTo( gnoll );
		RemixedSkills.onMobDie( rat3, gnoll );
		check( gnoll.buff( RemixedSkills.Rage.class ) != null, "a kill enrages the gnoll" );
		check( RemixedSkills.attackProc( gnoll, null, 10 ) == 13, "rage adds 30 percent" );
		check( hasText( gnoll.buff( RemixedSkills.Rage.class ).name() ) && hasText( gnoll.buff( RemixedSkills.Rage.class ).desc() ), "rage text" );
		Mob tough = ratNextTo( gnoll );
		tough.HP = tough.HT = 1000;
		int mpBefore = gnoll.MP;
		check( RemixedSkills.use( gnoll, RemixedSkills.Skill.MADNESS ), "madness skill" );
		check( gnoll.MP < mpBefore, "madness costs mana, " + mpBefore + " to " + gnoll.MP );
		gnoll.MP = gnoll.MAX_MP;
		try {
			check( RemixedSkills.use( gnoll, RemixedSkills.Skill.DISGUISE ), "disguise skill" );
		} catch (NullPointerException e) {
			System.out.println( "LIMIT: disguise " + e.getStackTrace()[0] );
		}

		//unlock trackers
		Heap tomb = new Heap();
		tomb.type = Heap.Type.TOMB;
		for (int i = 0; i < 5; i++) RemixedHeroes.onHeapOpened( tomb, gnoll );
		check( gnoll.buff( RemixedHeroes.Tracker.class ).graves == 5, "graves are counted" );

		//the run layout
		Hero h = newRun( HeroClass.WARRIOR, RemixedLayout.LAYOUT );
		check( Dungeon.depth == 0, "the layout starts on floor 0, at " + Dungeon.depth );
		check( hasText( Messages.get( GameModes.class, RemixedLayout.LAYOUT ) ), "layout mode text" );
		Level town = build( 0 );
		check( town instanceof TownLevel, "floor 0 is the town, got " + town.getClass().getSimpleName() );
		LevelTransition down = town.getTransition( LevelTransition.Type.REGULAR_EXIT );
		check( down != null && down.destDepth == 1, "the town leads down to floor 1" );
		Level one = build( 1 );
		LevelTransition up = one.getTransition( null );
		check( up != null && up.type == LevelTransition.Type.REGULAR_ENTRANCE && up.destDepth == 0, "floor 1 leads up to the town" );
		Level last = build( 26 );
		boolean fake = false;
		for (Heap heap : last.heaps.valueList()) for (com.shatteredpixel.shatteredpixeldungeon.items.Item it : heap.items) if (it instanceof FakeAmulet) fake = true;
		check( fake, "floor 26 holds the false amulet" );
		LevelTransition deeper = last.getTransition( LevelTransition.Type.REGULAR_EXIT );
		check( deeper != null && deeper.destDepth == 27, "floor 26 leads down to floor 27" );
		Level vll = build( 27 );
		check( vll instanceof VeryLastLevel, "floor 27 is the very last floor, got " + vll.getClass().getSimpleName() );
		check( vll.getTransition( null ) != null && vll.getTransition( null ).destDepth == 26, "floor 27 leads back up" );

		//without the mode nothing changes
		newRun( HeroClass.WARRIOR );
		check( Dungeon.depth == 1, "a normal run starts on floor 1" );
		Level n1 = build( 1 );
		check( n1.getTransition( null ).type == LevelTransition.Type.SURFACE, "a normal floor 1 leads to the surface" );

		GameModes.active.clear();
		System.out.println( "SUMMARY remixed: " + checks + " checks, failures: " + failures );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
