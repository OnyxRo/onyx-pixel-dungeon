import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.DerangedChar;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WarpDebuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.DerangedKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ratking.OmniAbility;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Kromer;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.RedCrystal;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Headless check of DERanged PD's Rat King and its systems (2026-10-22). The King is made and must hold the class
 * items, count as every class he shares, take on a subclass only once its RK_ talent has a point, lend his talent
 * points to the talents they stand for and earn half again as many points. Every talent, ability and challenge has
 * text, Omni-Ability rolls an ability, the alchemy recipes are found, and a Warrior is never counted as a Mage.
 */
public class DerangedTest {

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

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		try {
			//a Warrior is only a Warrior
			GamesInProgress.selectedClass = HeroClass.WARRIOR;
			Dungeon.seed = 424242L;
			Dungeon.init();
			check( Dungeon.hero.heroClass.is( HeroClass.WARRIOR ) && !Dungeon.hero.heroClass.is( HeroClass.MAGE ), "warrior is not a mage" );
			check( !Dungeon.hero.subClass.is( HeroSubClass.BERSERKER ), "no subclass yet" );

			GamesInProgress.selectedClass = HeroClass.RAT_KING;
			Dungeon.init();
			Hero hero = Dungeon.hero;
			HeroClass k = HeroClass.RAT_KING;
			check( hero.heroClass == k, "hero is the rat king" );
			check( hasText( k.title() ) && hasText( k.desc() ) && hasText( k.shortDesc() ), "class text" );
			check( k.spritesheet() != null && k.splashArt() != null, "class art" );
			check( hero.belongings.weapon instanceof MagesStaff, "starts with the mage's staff" );
			check( hero.belongings.artifact != null && hero.belongings.misc != null, "starts with the cloak and the tome" );
			for (HeroClass c : new HeroClass[]{ HeroClass.WARRIOR, HeroClass.MAGE, HeroClass.CLERIC, HeroClass.GUNNER, HeroClass.ARCHER }) {
				check( hero.heroClass.is( c ), "king counts as " + c );
			}
			check( !hero.heroClass.is( HeroClass.ZEALOT ), "king does not count as the zealot" );
			check( hero.talents.get( 0 ).size() == 10 && hero.talents.get( 1 ).size() == 12, "king tiers 1 and 2 " + hero.talents.get( 0 ).size() + " " + hero.talents.get( 1 ).size() );
			check( hero.talents.get( 2 ).isEmpty(), "no tier 3 before the subclass " + hero.talents.get( 2 ).keySet() );
			for (LinkedHashMap<Talent, Integer> tier : hero.talents) {
				for (Talent t : tier.keySet()) {
					check( hasText( t.title() ) && hasText( t.desc() ), "talent " + t + " text" );
				}
			}

			//talent points, half again as many
			hero.lvl = 6;
			check( hero.talentPointsAvailable( 1 ) == 7, "tier 1 points at level 6 " + hero.talentPointsAvailable( 1 ) );

			//borrowed talents
			check( hero.pointsInTalent( Talent.HEARTY_MEAL ) == 0, "no hearty meal yet" );
			hero.upgradeTalent( Talent.RK_ROYAL_PRIVILEGE );
			check( hero.pointsInTalent( Talent.HEARTY_MEAL ) == 1 && hero.hasTalent( Talent.EMPOWERING_MEAL ), "royal privilege lends hearty and empowering meal" );

			//the subclass
			check( hasText( HeroSubClass.KING.title() ) && hasText( HeroSubClass.KING.desc() ), "king subclass text" );
			hero.subClass = HeroSubClass.KING;
			Talent.initSubclassTalents( hero );
			check( hero.talents.get( 2 ).size() == 36, "36 subclass talents " + hero.talents.get( 2 ).size() );
			for (Talent t : hero.talents.get( 2 ).keySet()) {
				check( hasText( t.title() ) && hasText( t.desc() ), "subclass talent " + t + " text" );
			}
			check( !hero.subClass.is( HeroSubClass.BERSERKER ), "not a berserker before the talent" );
			hero.upgradeTalent( Talent.RK_BERSERKER );
			check( hero.subClass.is( HeroSubClass.BERSERKER ) && !hero.subClass.is( HeroSubClass.SNIPER ), "a berserker after the talent, not a sniper" );
			hero.upgradeTalent( Talent.RK_ENGINEER );
			check( hero.subClass.is( HeroSubClass.ENGINEER ), "an engineer after the talent" );

			//abilities
			for (ArmorAbility a : k.armorAbilities()) {
				check( hasText( a.name() ) && hasText( a.shortDesc() ) && hasText( a.desc() ), "ability " + a.getClass().getSimpleName() + " text" );
				for (Talent t : a.talents()) check( hasText( t.title() ) && hasText( t.desc() ), "ability talent " + t + " text" );
			}
			ClassArmor armor = ClassArmor.upgrade( hero, new com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor() );
			check( armor != null && hasText( armor.name() ), "class armor" );
			OmniAbility omni = new OmniAbility();
			hero.armorAbility = omni;
			Talent.initArmorTalents( hero );
			for (int i = 0; i < 20; i++) {
				omni.roll( hero );
				check( omni.activeAbility() != null && !(omni.activeAbility() instanceof OmniAbility), "omni rolls an ability" );
				check( hero.talents.get( 3 ).size() == omni.activeAbility().talents().length, "omni moves tier 4 to " + omni.activeAbility().getClass().getSimpleName() );
			}

			//save and load
			com.watabou.utils.Bundle b = new com.watabou.utils.Bundle();
			hero.storeInBundle( b );
			Hero back = new Hero();
			back.restoreFromBundle( b );
			check( back.heroClass == k && back.subClass == HeroSubClass.KING && back.pointsInTalent( Talent.RK_BERSERKER ) == 1, "saves and loads" );
			check( back.armorAbility instanceof OmniAbility && ((OmniAbility) back.armorAbility).activeAbility() != null, "omni ability saves" );

			hero.lvl = 12;
			hero.updateHT( true );
			for (int i = 0; i < 40; i++) {
				hero.attackDelay();
				hero.defenseSkill( hero );
				hero.attackSkill( hero );
				hero.drRoll();
			}

			//challenges
			for (String id : new String[]{ "forget_path", "no_hp" }) {
				check( hasText( Messages.get( Challenges.class, id ) ) && hasText( Messages.get( Challenges.class, id + "_desc" ) ), "challenge " + id + " text" );
			}

			//warp
			WarpDebuff.inflict( 50, 1f );
			check( hero.buff( WarpDebuff.class ) != null && hasText( hero.buff( WarpDebuff.class ).desc() ), "warp" );
			check( DerangedChar.sellPrice( 100 ) >= 1, "sell price with warp" );

			//recipes
			ArrayList<Item> in = new ArrayList<>();
			in.add( new ScrollOfUpgrade().identify() );
			in.add( new Kromer() );
			check( Recipe.findRecipes( in ).size() > 0, "kromer scroll recipe" );
			in.clear();
			in.add( new RedCrystal().quantity( 3 ) );
			check( Recipe.findRecipes( in ).size() > 0, "red crystal recipe" );
			check( DerangedKing.talentList( HeroClass.MAGE, 1 ).length > 0, "kromer finds other classes' talents" );
		} catch (Throwable t) {
			failures++;
			System.out.println( "FAIL: threw " + t );
			StackTraceElement[] st = t.getStackTrace();
			for (int i = 0; i < Math.min( 10, st.length ); i++) System.out.println( "        at " + st[i] );
		}
		System.out.println( "SUMMARY deranged: " + checks + " checks, failures: " + failures );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
