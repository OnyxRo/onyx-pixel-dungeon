import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndNegative;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Headless check of Transformation PD's talent catalog and the Freeman. Every catalog talent must have text, an
 * icon in the mod's sheet, a tier and a kind. Then each one is given to a hero at full points on a real floor and
 * every hook in TransformationTalents is run with it, so a talent that throws shows up here, not in a run.
 */
public class TransformationCatalogTest {

	static int failures = 0, checks = 0;

	static void check( boolean ok, String what ) {
		checks++;
		if (!ok) {
			failures++;
			System.out.println( "FAIL: " + what );
		}
	}

	static boolean hasText( String s ) {
		return s != null && !s.isEmpty() && !s.contains( "NO TEXT" ) && !s.startsWith( "actors." );
	}

	interface Hook { void run() throws Exception; }

	static void hook( String what, Hook h ) {
		checks++;
		try {
			h.run();
		} catch (Throwable t) {
			String m = String.valueOf( t.getMessage() );
			if (t instanceof NullPointerException && (m.contains( "GameScene.scene" ) || m.contains( "CellSelector" ) || m.contains( "Game.instance" ))) {
				System.out.println( "HEADLESS (not counted): " + what + " needs the game screen" );
				return;
			}
			failures++;
			System.out.println( "FAIL: " + what + " threw " + t );
			StackTraceElement[] st = t.getStackTrace();
			for (int i = 0; i < Math.min( 5, st.length ); i++) System.out.println( "        at " + st[i] );
		}
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();

		//the Freeman
		GamesInProgress.selectedClass = HeroClass.FREEMAN;
		Dungeon.seed = 515151L;
		Dungeon.init();
		Dungeon.challenges = Challenges.CURSED_TALENTS;
		final Hero hero = Dungeon.hero;
		HeroClass cls = HeroClass.FREEMAN;
		check( hero.heroClass == cls, "Freeman hero class" );
		check( hasText( cls.title() ) && hasText( cls.desc() ) && hasText( cls.shortDesc() ), "Freeman class text" );
		check( cls.subClasses().length == 1 && hasText( cls.subClasses()[0].title() ) && hasText( cls.subClasses()[0].desc() ), "Freeman subclass" );
		check( cls.armorAbilities().length == 2, "Freeman has two armor abilities" );
		for (ArmorAbility a : cls.armorAbilities()) {
			check( hasText( a.name() ) && hasText( a.shortDesc() ) && hasText( a.desc() ), "ability " + a.getClass().getSimpleName() + " text" );
			for (Talent t : a.talents()) check( hasText( t.title() ) && hasText( t.desc() ), "ability talent " + t + " text" );
		}
		check( cls.splashArt() != null && cls.spritesheet() != null, "Freeman art" );
		check( hero.talents.get( 0 ).containsKey( Talent.POTENTIAL_1 ) && hero.talents.get( 2 ).containsKey( Talent.POTENTIAL_9 ), "Freeman potential slots" );
		check( hero.talentPointsAvailable( 3 ) == 0 || hero.lvl >= 12, "tier 3 locked below level 12" );
		check( hero.belongings.weapon != null, "Freeman starts with a weapon" );

		//every catalog talent
		int count = 0;
		for (Talent t : Talent.values()) {
			if (!t.isCatalogTalent()) continue;
			count++;
			check( hasText( t.title() ), t + " title" );
			check( hasText( t.desc() ), t + " description" );
			check( t.icon() >= 4096 && t.icon() < 4096 + 1024, t + " icon " + t.icon() );
			check( t.tier() >= 0 && t.tier() <= 4, t + " tier " + t.tier() );
			check( t.maxPoints() >= 2 && t.maxPoints() <= 4, t + " max points" );
		}
		check( count > 150, "catalog has " + count + " talents" );
		for (int tier = 1; tier <= 3; tier++) {
			for (Talent.TalentType k : TransformationTalents.KINDS) {
				ArrayList<Talent> c = TransformationTalents.metamorphChoices( hero, tier, k );
				check( !c.isEmpty(), "metamorphosis offers talents at tier " + tier + " for " + k );
				for (Talent t : c) check( t.tier() == tier && TransformationTalents.inPool( t ), "metamorph choice " + t + " fits tier " + tier );
			}
		}
		check( WndNegative.pool().size() >= 40, "negative pool has " + WndNegative.pool().size() );

		//a floor to run the hooks on
		Dungeon.depth = 4;
		Level level = Dungeon.newLevel();
		Dungeon.level = level;
		hero.pos = level.entrance();
		hero.sprite = TestBoot.sprite();
		Actor.add( hero );
		hero.lvl = 14;
		hero.updateHT( true );

		for (Talent t : Talent.values()) {
			if (!t.isCatalogTalent()) continue;
			final int ti = Math.max( 0, Math.min( 3, t.tier() - 1 ) );
			LinkedHashMap<Talent, Integer> keep = new LinkedHashMap<>( hero.talents.get( ti ) );
			hero.talents.get( ti ).put( t, t.type() == Talent.TalentType.NEGATIVE ? 0 : t.maxPoints() );
			final Mob rat = new Rat();
			int cell = -1;
			for (int i : com.watabou.utils.PathFinder.NEIGHBOURS8) {
				int c = hero.pos + i;
				if (level.passable[c] && Actor.findChar( c ) == null) { cell = c; break; }
			}
			rat.pos = cell;
			rat.sprite = TestBoot.sprite();
			level.mobs.add( rat );
			Actor.add( rat );
			final String n = t.name();
			hook( n + " upgraded", () -> Talent.onTalentUpgraded( hero, t ) );
			hook( n + " stats", () -> {
				TransformationTalents.maxHP( hero, 50 ); TransformationTalents.strBonus( hero );
				TransformationTalents.accuracy( hero, rat ); TransformationTalents.evasionBonus( hero );
				TransformationTalents.armor( hero, 5 ); TransformationTalents.speed( hero );
				hero.updateHT( false ); hero.STR(); hero.attackSkill( rat ); hero.defenseSkill( rat ); hero.drRoll();
			} );
			hook( n + " meals and potions", () -> {
				TransformationTalents.onFoodEaten( hero, new Food() );
				TransformationTalents.onPotionDrunk( hero, new PotionOfHealing() );
				TransformationTalents.onPotionThrown( hero, rat.pos );
				TransformationTalents.onDewDrunk( hero, 3 );
				TransformationTalents.onScrollUsed( hero );
				TransformationTalents.onItemCollected( hero, new Food() );
				TransformationTalents.onGoldPickedUp( hero );
			} );
			hook( n + " fighting", () -> {
				TransformationTalents.onAttackProc( hero, rat, 10 );
				TransformationTalents.onDefenseProc( hero, rat, 10 );
				TransformationTalents.onHeroDamage( hero, 10, rat );
				TransformationTalents.onHeroDodged( hero, rat );
				TransformationTalents.mobAttackProc( rat, hero, 5 );
				TransformationTalents.mobSpeed( rat );
				TransformationTalents.mobDamageTaken( rat, 5 );
				TransformationTalents.onWandProc( rat );
				TransformationTalents.onWandDamage( new WandOfMagicMissile(), rat, 5 );
				TransformationTalents.onWandZap( new WandOfMagicMissile(), rat.pos );
			} );
			hook( n + " moving and resting", () -> {
				TransformationTalents.onMove( hero );
				TransformationTalents.onLevelUp( hero );
				TransformationTalents.onChasmLand( hero );
				TransformationTalents.onGrassTrampled( hero.pos, hero );
				TransformationTalents.onNewFloor( hero );
			} );
			hook( n + " a kill", () -> {
				TransformationTalents.preventsDeath( rat, hero );
				TransformationTalents.onMobDie( rat, hero );
			} );
			hero.HP = hero.HT;
			for (com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff b : hero.buffs().toArray( new com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff[0] )) {
				if (!(b instanceof com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger)
						&& !(b instanceof com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration)) {
					try { b.detach(); } catch (NullPointerException e) { hero.remove(b); }
				}
			}
			for (Mob m : level.mobs.toArray( new Mob[0] )) { level.mobs.remove( m ); Actor.remove( m ); }
			hero.talents.set( ti, keep );
		}

		//negative talents are saved with their tier
		WndNegative.addNegative( hero, Talent.FEEBLE, 1 );
		com.watabou.utils.Bundle b = new com.watabou.utils.Bundle();
		hero.storeInBundle( b );
		Hero back = new Hero();
		back.restoreFromBundle( b );
		check( back.talents.get( 0 ).containsKey( Talent.FEEBLE ) && back.pointsNegative( Talent.FEEBLE ) == 2, "negative talent saves and loads" );
		check( back.heroClass == HeroClass.FREEMAN, "Freeman saves and loads" );

		System.out.println( "SUMMARY Transformation talent catalog: " + checks + " checks, failures: " + failures );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
