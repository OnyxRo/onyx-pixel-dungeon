import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.InfusionCD;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.HalfRipper;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Gun;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.VoidTrapLevel;

import java.lang.reflect.Field;

/**
 * Headless check for the pieces ported after Devoted and Reforged were otherwise finished. The Void trap floor,
 * the bounty hunter's item infusion, the Half Ripper escort quest across floors 24 to 20, and the Gun.
 *
 *   java -cp <classes>:<libs.jar>:<test classes> DevotedLeftoversTest
 */
public class DevotedLeftoversTest {

	static int failures = 0;
	static int checks = 0;

	static void check( boolean ok, String what ) {
		checks++;
		if (!ok) {
			failures++;
			System.out.println( "FAIL: " + what );
		}
	}

	static void set( String field, Object value ) throws Exception {
		Field f = HalfRipper.Quest.class.getDeclaredField( field );
		f.setAccessible( true );
		f.set( null, value );
	}

	static boolean isZealotTalent( com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent t ) {
		switch (t) {
			case ARCANE_MEAL: case DEMONIC_INTUITION: case WRATHFUL_STRIKE: case PAIN_IS_GAIN:
			case CLEANSING_MEAL: case CONFIRMED_BELIEF: case HIDDEN_POWER: case INSTANT_GRATIFICATION: case ESOTERIC_MIND:
			case DEMOLITIONIST: case UNHOLY_WRATH: case ELDRITCH_ENERGY: case HIVE_MIND: case TAG_TEAM:
			case NIHILISM: case GOURMAND: case DEVOUR_RITUAL:
			case FORBIDDEN_KNOWLEDGE: case FORTIFIED_CONCIOUSNESS: case CAUTION_TO_THE_WIND:
			case SUSTAINED_PRESSURE: case SUBZERO_KICK: case GRAY_MATTER:
			case WOE_IS_ME: case AND_WOE_IS_YOU: case WEIGHING_THE_DIE:
				return true;
			default:
				return false;
		}
	}

	static int countRippers( Level l ) {
		int n = 0;
		for (Mob m : l.mobs) if (m instanceof HalfRipper) n++;
		return n;
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = 777001L;
		Dungeon.init();

		//the Void trap, one orb for each hero level and one exit portal
		try {
			Dungeon.depth = 12;
			Dungeon.branch = 0;
			Dungeon.hero.lvl = 4;
			Level v = new VoidTrapLevel();
			v.create();
			int orbs = 0, portals = 0;
			for (Mob m : v.mobs) {
				if (m instanceof VoidTrapLevel.VoidOrb) orbs++;
				if (m instanceof VoidTrapLevel.ExitPortal) portals++;
			}
			check( orbs == 4, "Void trap orbs " + orbs + " for hero level 4" );
			check( portals == 1, "Void trap exit portals " + portals );
			check( Dungeon.hero.lvl == 1, "Void trap drops the hero to level 1" );
		} catch (Throwable t) {
			failures++;
			System.out.println( "FAIL: Void trap threw " + t );
			t.printStackTrace( System.out );
		}

		//infusion, an infused item that leaves the pack is owed back, and the flag survives saving
		try {
			Dungeon.hero.lvl = 10;
			Item scroll = new ScrollOfIdentify();
			scroll.isInfused = true;
			Dungeon.hero.belongings.backpack.items.add( scroll );
			scroll.detach( Dungeon.hero.belongings.backpack );
			InfusionCD cd = Dungeon.hero.buff( InfusionCD.class );
			check( cd != null, "infused item leaving the pack starts the cooldown" );
			check( cd != null && cd.ToGiveBack == scroll, "cooldown remembers the item" );
			check( scroll.duplicate().isInfused, "infusion survives a save" );
			check( !new ScrollOfIdentify().isInfused, "plain items are not infused" );
		} catch (Throwable t) {
			failures++;
			System.out.println( "FAIL: infusion threw " + t );
			t.printStackTrace( System.out );
		}

		//the Gun, loaded by default, unloaded after the flag is cleared, saved and loaded
		try {
			Gun g = new Gun();
			check( g.isLoaded(), "Gun starts loaded" );
			check( "1/1".equals( g.status() ), "Gun status when loaded" );
			g.setLoaded( false );
			check( "0/1".equals( g.status() ), "Gun status when empty" );
			check( !((Gun) g.duplicate()).isLoaded(), "Gun empty state survives a save" );
			check( g.max() >= g.min() && g.min() > 0, "Gun damage range" );
		} catch (Throwable t) {
			failures++;
			System.out.println( "FAIL: Gun threw " + t );
			t.printStackTrace( System.out );
		}

		//the Half Ripper, floor 24 spawns it once, the quest follows the hero up and pays out on floor 20
		try {
			HalfRipper.Quest.reset();
			Dungeon.branch = 0;

			Dungeon.depth = 24;
			Level l24 = Dungeon.newLevel();
			HalfRipper.Quest.spawn( l24 );
			check( countRippers( l24 ) == 1, "floor 24 has one Half Ripper, found " + countRippers( l24 ) );
			HalfRipper.Quest.spawn( l24 );
			check( countRippers( l24 ) == 1, "arriving again does not add a second Half Ripper" );

			//save and load of the quest state
			com.watabou.utils.Bundle b = new com.watabou.utils.Bundle();
			set( "given", true );
			set( "started", true );
			set( "depth", 23 );
			HalfRipper.Quest.storeInBundle( b );
			HalfRipper.Quest.reset();
			check( !HalfRipper.Quest.started(), "reset clears the quest" );
			HalfRipper.Quest.restoreFromBundle( b );
			check( HalfRipper.Quest.started() && HalfRipper.Quest.depth() == 23, "quest state survives a save" );

			Dungeon.depth = 23;
			Level l23 = Dungeon.newLevel();
			HalfRipper.Quest.spawn( l23 );
			check( countRippers( l23 ) == 1, "floor 23 gets the Half Ripper at the exit" );

			//finished, the reward ripper waits on floor 20
			HalfRipper.Quest.complete();
			check( HalfRipper.Quest.completed() && HalfRipper.Quest.depth() == 20, "complete() sends the quest to floor 20" );
			Dungeon.depth = 20;
			Level l20 = Dungeon.newLevel();
			HalfRipper.Quest.spawn( l20 );
			check( countRippers( l20 ) == 1, "floor 20 has the reward Half Ripper" );

			//failing it brings back an enemy ripper where the quest ended
			HalfRipper.Quest.reset();
			set( "spawned", true );
			set( "given", true );
			set( "started", true );
			set( "depth", 22 );
			HalfRipper.Quest.abandon();
			check( HalfRipper.Quest.failed(), "abandon() fails the quest" );
			Dungeon.depth = 22;
			Level l22 = Dungeon.newLevel();
			HalfRipper.Quest.spawn( l22 );
			boolean ripperDemon = false;
			for (Mob m : l22.mobs) if (m instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RipperDemon) ripperDemon = true;
			check( ripperDemon, "the abandoned ripper returns as an enemy" );
			HalfRipper.Quest.reset();
		} catch (Throwable t) {
			failures++;
			System.out.println( "FAIL: Half Ripper threw " + t );
			t.printStackTrace( System.out );
		}

		//the Zealot class, its kit, talents, subclasses, armor abilities and Blood Vial
		try {
			GamesInProgress.selectedClass = HeroClass.ZEALOT;
			Dungeon.seed = 777002L;
			Dungeon.init();
			com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero h = Dungeon.hero;
			check( h.heroClass == HeroClass.ZEALOT, "hero is a Zealot" );
			check( h.belongings.weapon instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Warpick, "Zealot starts with a warpick" );
			check( h.belongings.artifact instanceof com.shatteredpixel.shatteredpixeldungeon.items.artifacts.BloodVial, "Zealot starts with the Blood Vial" );
			check( h.talents.get(0).size() == 4, "Zealot tier 1 talents " + h.talents.get(0).size() );
			check( h.talents.get(1).size() == 5, "Zealot tier 2 talents " + h.talents.get(1).size() );
			check( h.talents.get(2).size() == 2, "Zealot tier 3 talents " + h.talents.get(2).size() );
			check( h.talents.get(0).containsKey( com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.ARCANE_MEAL ), "Zealot has Arcane Meal" );

			for ( com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass sub : HeroClass.ZEALOT.subClasses() ) {
				h.subClass = sub;
				com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.initSubclassTalents( h );
				check( h.talents.get(2).size() >= 5, sub + " adds three talents, tier 3 has " + h.talents.get(2).size() );
			}

			com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility[] abils = HeroClass.ZEALOT.armorAbilities();
			check( abils.length == 3, "Zealot has three armor abilities" );
			for ( com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility a : abils ) {
				check( a.talents().length == 4, a.getClass().getSimpleName() + " has three talents and heroic energy" );
				check( !com.shatteredpixel.shatteredpixeldungeon.messages.Messages.get( a, "name" ).startsWith( "!!!" ), a.getClass().getSimpleName() + " has a name" );
			}

			//every Zealot string resolves
			for ( com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent t : com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.values() ) {
				if (t.name().length() > 0 && t.maxPoints() > 0 && isZealotTalent( t )) {
					check( !t.title().startsWith( "!!!" ), "talent " + t + " has a title" );
					check( !t.desc().startsWith( "!!!" ), "talent " + t + " has a description" );
				}
			}
			check( !HeroClass.ZEALOT.title().startsWith( "!!!" ) && !HeroClass.ZEALOT.desc().startsWith( "!!!" ), "Zealot class text" );

			//class armor made from plain armor
			com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor ca = com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor.upgrade( h, new com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor() );
			check( ca instanceof com.shatteredpixel.shatteredpixeldungeon.items.armor.ZealotArmor, "Zealot class armor" );

			//the vial survives a save and carries its state
			com.shatteredpixel.shatteredpixeldungeon.items.artifacts.BloodVial vial = (com.shatteredpixel.shatteredpixeldungeon.items.artifacts.BloodVial) h.belongings.artifact;
			vial.DevoteeCharges = 2;
			check( ((com.shatteredpixel.shatteredpixeldungeon.items.artifacts.BloodVial) vial.duplicate()).DevoteeCharges == 2, "Blood Vial keeps its Devotee charges" );
		} catch (Throwable t) {
			failures++;
			System.out.println( "FAIL: Zealot threw " + t );
			t.printStackTrace( System.out );
		}

		//the Artificer class from Reforged, its kit, talents, subclasses, abilities and the Construct
		try {
			GamesInProgress.selectedClass = HeroClass.ARTIFICER;
			Dungeon.seed = 777003L;
			Dungeon.init();
			com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero h = Dungeon.hero;
			check( h.heroClass == HeroClass.ARTIFICER, "hero is an Artificer" );
			check( h.belongings.getItem( com.shatteredpixel.shatteredpixeldungeon.items.weapon.Gun.class ) != null, "Artificer starts with the Gun" );
			check( h.talents.get(0).size() == 4, "Artificer tier 1 talents " + h.talents.get(0).size() );
			check( h.talents.get(1).size() == 5, "Artificer tier 2 talents " + h.talents.get(1).size() );
			check( h.talents.get(2).size() == 2, "Artificer tier 3 talents " + h.talents.get(2).size() );

			for ( com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass sub : HeroClass.ARTIFICER.subClasses() ) {
				h.subClass = sub;
				com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.initSubclassTalents( h );
				check( h.talents.get(2).size() >= 5, sub + " adds three talents, tier 3 has " + h.talents.get(2).size() );
				check( !sub.title().startsWith( "!!!" ) && !sub.desc().startsWith( "!!!" ), sub + " text" );
			}

			com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility[] abils = HeroClass.ARTIFICER.armorAbilities();
			check( abils.length == 3, "Artificer has three armor abilities" );
			for ( com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility a : abils ) {
				check( a.talents().length == 4, a.getClass().getSimpleName() + " has talents and heroic energy" );
				check( !com.shatteredpixel.shatteredpixeldungeon.messages.Messages.get( a, "name" ).startsWith( "!!!" ), a.getClass().getSimpleName() + " has a name" );
			}

			for ( com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent t : com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.values() ) {
				if (t.ordinal() >= com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.TINKERERS_MEAL.ordinal()
						&& t.ordinal() <= com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.POWERFUL_REFLECTION.ordinal()) {
					check( !t.title().startsWith( "!!!" ), "talent " + t + " has a title" );
					check( !t.desc().startsWith( "!!!" ), "talent " + t + " has a description" );
				}
			}
			check( !HeroClass.ARTIFICER.title().startsWith( "!!!" ) && !HeroClass.ARTIFICER.desc().startsWith( "!!!" ), "Artificer class text" );

			com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor ca = com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor.upgrade( h, new com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor() );
			check( ca instanceof com.shatteredpixel.shatteredpixeldungeon.items.armor.ArtificerArmor, "Artificer class armor" );

			//the Construct
			h.lvl = 10;
			h.updateHT( true );
			com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.ConstructHero c = new com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.ConstructHero();
			check( c.HT == Math.max( 1, (int)(h.HT * 0.75f) ), "Construct health is three quarters of the hero's, " + c.HT + " of " + h.HT );
			check( c.damageRoll() > 0, "Construct hits for something" );
			check( !com.shatteredpixel.shatteredpixeldungeon.messages.Messages.get( c, "name" ).startsWith( "!!!" ), "Construct name" );
			check( !com.shatteredpixel.shatteredpixeldungeon.messages.Messages.get( c, "directed_follow" ).startsWith( "!!!" ), "Construct lines" );
			com.watabou.utils.Bundle b = new com.watabou.utils.Bundle();
			c.storeInBundle( b );
			check( b.contains( "health_regen" ), "Construct saves its regeneration" );

			//hero fields survive a save
			h.constructRespawnTimer = 7;
			h.PatternRecognitionPotions.add( com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing.class );
			com.watabou.utils.Bundle hb = new com.watabou.utils.Bundle();
			h.storeInBundle( hb );
			com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero h2 = new com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero();
			h2.restoreFromBundle( hb );
			check( h2.constructRespawnTimer == 7, "construct respawn timer survives a save" );
			check( h2.PatternRecognitionPotions.size() == 1, "Pattern Recognition potions survive a save" );

			//the Gun and the Artificer's talents
			com.shatteredpixel.shatteredpixeldungeon.items.weapon.Gun g = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.Gun();
			check( g.damageRoll( h ) >= 0, "Gun rolls damage with talents in play" );
		} catch (Throwable t) {
			failures++;
			System.out.println( "FAIL: Artificer threw " + t );
			t.printStackTrace( System.out );
		}

		System.out.println( "SUMMARY devoted leftovers checks: " + checks + ", failures: " + failures );
		if (failures > 0) System.exit( 1 );
	}
}
