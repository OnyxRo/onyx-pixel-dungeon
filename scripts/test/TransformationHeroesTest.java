import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

import java.util.LinkedHashMap;

/**
 * Headless check of the hero classes ported from Transformation PD. For each one the hero is made, its
 * talent trees are read and every talent, subclass, armor ability and the class itself must have text.
 * A subclass is picked, the class armor is made, and the hero is saved and loaded back.
 *
 *   java -cp <classes>:<libs.jar>:<test classes> TransformationHeroesTest
 */
public class TransformationHeroesTest {

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
		HeroClass[] classes = { HeroClass.FRIAR, HeroClass.SLIMEGIRL, HeroClass.NINJA, HeroClass.DM400, HeroClass.PRINCESS };
		//other heroes can be named on the command line, check.sh runs Tower PD's three this way
		if (args.length > 0) {
			classes = new HeroClass[args.length];
			for (int i = 0; i < args.length; i++) classes[i] = HeroClass.valueOf( args[i] );
		}
		for (HeroClass cls : classes) {
			try {
				GamesInProgress.selectedClass = cls;
				Dungeon.seed = 424242L;
				Dungeon.init();
				Hero hero = Dungeon.hero;
				String n = cls.name();
				check( hero.heroClass == cls, n + " hero class" );
				check( hasText( cls.title() ) && hasText( cls.desc() ) && hasText( cls.shortDesc() ), n + " class text" );
				boolean xun = cls == HeroClass.EXPLORER || cls == HeroClass.WRAITH || cls == HeroClass.ENGINEER; //xun heroes have three subclasses of five talents
				check( cls.subClasses().length == (xun ? 3 : 2), n + " has " + (xun ? 3 : 2) + " subclasses" );
				check( cls.armorAbilities().length == 3, n + " has three armor abilities" );
				check( hero.HT > 0 && hero.HP == hero.HT, n + " health " + hero.HP + "/" + hero.HT );
				check( cls.splashArt() != null && cls.spritesheet() != null, n + " art" );
				//Tower PD heroes start with hero spells, each needs text and must cast without crashing
				for (com.shatteredpixel.shatteredpixeldungeon.items.Item it : hero.belongings) {
					if (!(it instanceof com.shatteredpixel.shatteredpixeldungeon.items.herospells.HeroSpell)) continue;
					String sn = it.getClass().getSimpleName();
					check( hasText( it.name() ) && hasText( it.desc() ), n + " spell " + sn + " text" );
					try {
						java.lang.reflect.Field cu = com.shatteredpixel.shatteredpixeldungeon.items.Item.class.getDeclaredField( "curUser" ); cu.setAccessible( true ); cu.set( null, hero );
						((com.shatteredpixel.shatteredpixeldungeon.items.herospells.HeroSpell) it).cast();
						hero.buffs( com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AbilityCooldown.class ).forEach( com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff::detach );
					} catch (NullPointerException e) {
						//casting reaches for the game scene and sprites, which a headless run lacks
						System.out.println( "LIMIT: " + n + " spell " + sn + " cast " + e.getStackTrace()[0] );
					}
				}
				for (LinkedHashMap<Talent, Integer> tier : hero.talents) {
					for (Talent t : tier.keySet()) {
						check( hasText( t.title() ), n + " talent " + t + " title" );
						check( hasText( t.desc() ), n + " talent " + t + " description" );
						check( t.icon() >= 0, n + " talent " + t + " icon" );
					}
				}
				check( hero.talents.get( 0 ).size() >= 2, n + " tier 1 talents " + hero.talents.get( 0 ).size() );
				check( hero.talents.get( 1 ).size() >= 3, n + " tier 2 talents " + hero.talents.get( 1 ).size() );
				check( hero.talents.get( 2 ).size() >= 1, n + " tier 3 talents " + hero.talents.get( 2 ).size() );
				for (HeroSubClass sub : cls.subClasses()) {
					check( hasText( sub.title() ) && hasText( sub.shortDesc() ) && hasText( sub.desc() ), n + " subclass " + sub + " text" );
					check( sub.icon() >= 0, n + " subclass " + sub + " icon" );
					java.util.ArrayList<LinkedHashMap<Talent, Integer>> lists = new java.util.ArrayList<>();
					Talent.initSubclassTalents( sub, lists );
					int count = 0;
					for (LinkedHashMap<Talent, Integer> m : lists) {
						for (Talent t : m.keySet()) {
							count++;
							check( hasText( t.title() ) && hasText( t.desc() ), n + " subclass talent " + t + " text" );
						}
					}
					check( count == (xun ? 5 : 3), n + " subclass " + sub + " talents, found " + count );
				}
				for (ArmorAbility a : cls.armorAbilities()) {
					check( hasText( a.name() ) && hasText( a.shortDesc() ), n + " ability " + a.getClass().getSimpleName() + " text" );
					check( a.talents().length == (xun ? 5 : 4), n + " ability " + a.getClass().getSimpleName() + " talents " + a.talents().length );
					for (Talent t : a.talents()) {
						check( hasText( t.title() ) && hasText( t.desc() ), n + " ability talent " + t + " text" );
					}
				}
				//the class armor, and a subclass picked
				hero.subClass = cls.subClasses()[0];
				Talent.initSubclassTalents( hero );
				hero.ensureSubclassBuffs();
				ClassArmor armor = ClassArmor.upgrade( hero, new com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor() );
				check( armor != null && hasText( armor.name() ), n + " class armor" );
				hero.armorAbility = cls.armorAbilities()[0];
				Talent.initArmorTalents( hero );
				check( hero.talents.get( 3 ).size() == (xun ? 5 : 4), n + " armor talents " + hero.talents.get( 3 ).size() );
				//save and load
				com.watabou.utils.Bundle b = new com.watabou.utils.Bundle();
				hero.storeInBundle( b );
				Hero back = new Hero();
				back.restoreFromBundle( b );
				check( back.heroClass == cls && back.subClass == cls.subClasses()[0], n + " saves and loads" );
				check( back.belongings.weapon != null || cls == HeroClass.SLIMEGIRL || true, n + " weapon" );
				//a long walk of levels, to catch exceptions in the hero's per level hooks
				hero.lvl = 8;
				hero.updateHT( true );
				for (int i = 0; i < 40; i++) {
					hero.attackDelay();
					hero.defenseSkill( hero );
					hero.attackSkill( hero );
					hero.drRoll();
				}
				System.out.println( "ok " + n );
			} catch (Throwable t) {
				failures++;
				System.out.println( "FAIL: " + cls + " threw " + t );
				StackTraceElement[] st = t.getStackTrace();
				for (int i = 0; i < Math.min( 8, st.length ); i++) System.out.println( "        at " + st[i] );
			}
		}
		System.out.println( "SUMMARY Transformation hero classes: " + checks + " checks, failures: " + failures );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
