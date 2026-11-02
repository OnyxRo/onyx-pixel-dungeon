import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.BlueArchiveModes;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GameModes;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBAHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RBASandbag;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.levels.LastLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBlacksmith;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUpgrade;

import java.lang.reflect.Field;
import java.util.Map;

//bluearchive and rebluearchive, the two Students Only modes, the students' badges and the punching bag on floor 26
public class BlueArchiveModesTest {

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

	@SuppressWarnings("unchecked")
	static Map<Object, Badges.Badge> map( String name ) throws Exception {
		Field f = Badges.class.getDeclaredField( name );
		f.setAccessible( true );
		return (Map<Object, Badges.Badge>) f.get( null );
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();

		//text
		for (String id : new String[]{ BlueArchiveModes.BA_STUDENTS, BlueArchiveModes.RBA_STUDENTS }) {
			check( GameModes.get( id ) != null, "mode registered " + id );
			check( hasText( GameModes.get( id ).title() ) && hasText( GameModes.get( id ).desc() ), "text for " + id );
		}
		for (String k : new String[]{ "rba_bullet", "rba_assassination_damage", "rba_purge", "rba_justice", "rba_explosion" }) {
			check( hasText( Messages.get( WndUpgrade.class, k ) ), "upgrade window text " + k );
		}
		for (String k : new String[]{ "rba_engrave", "rba_engrave_prompt" }) {
			check( hasText( Messages.get( WndBlacksmith.class, k ) ), "blacksmith text " + k );
		}
		check( hasText( Messages.get( RBASandbag.class, "name" ) ), "punching bag name" );

		//hero select
		GameModes.active.clear();
		check( !BlueArchiveModes.hides( HeroClass.WARRIOR ) && !BlueArchiveModes.hides( HeroClass.RBA_ARIS ), "no mode hides nobody" );
		GameModes.set( BlueArchiveModes.BA_STUDENTS, 1 );
		check( BlueArchiveModes.hides( HeroClass.WARRIOR ) && !BlueArchiveModes.hides( HeroClass.BA_ARIS )
				&& BlueArchiveModes.hides( HeroClass.RBA_ARIS ), "Blue Archive mode shows only its nine" );
		check( !HeroClass.WARRIOR.isUnlocked() && HeroClass.BA_IZUNA.isUnlocked(), "Blue Archive mode locks the others for the random pick" );
		int shown = 0;
		for (HeroClass c : HeroClass.values()) if (!BlueArchiveModes.hides( c )) shown++;
		check( shown == 9, "nine heroes shown, saw " + shown );
		check( BlueArchiveModes.studentNpcs() && !BlueArchiveModes.akiraImp(), "Sora and Cherino, no Akira" );
		GameModes.set( BlueArchiveModes.RBA_STUDENTS, 1 );
		check( !GameModes.on( BlueArchiveModes.BA_STUDENTS ), "the two rosters replace each other" );
		shown = 0;
		for (HeroClass c : HeroClass.values()) if (!BlueArchiveModes.hides( c )) shown++;
		check( shown == 11, "eleven heroes shown, saw " + shown );
		check( BlueArchiveModes.akiraImp(), "Akira with Re Blue Archive's mode" );
		check( !BlueArchiveModes.rosterChanged(), "roster remembered" );
		GameModes.active.clear();
		check( BlueArchiveModes.rosterChanged(), "roster change seen" );
		BlueArchiveModes.hides( HeroClass.WARRIOR );

		//badges
		Map<Object, Badges.Badge> first = map( "firstBossClassBadges" );
		Map<Object, Badges.Badge> victory = map( "victoryClassBadges" );
		Map<Object, Badges.Badge> third = map( "thirdBossSubclassBadges" );
		for (HeroClass c : HeroClass.values()) {
			if (!c.name().startsWith( "BA_" ) && !c.name().startsWith( "RBA_" )) continue;
			check( first.get( c ) != null && first.get( c ).name().equals( "BOSS_SLAIN_1_" + c.name() ), "first boss badge " + c );
			check( victory.get( c ) != null && victory.get( c ).name().equals( "VICTORY_" + c.name() ), "victory badge " + c );
			check( c.masteryBadge() != null, "mastery badge " + c );
		}
		int subs = 0;
		for (HeroSubClass s : HeroSubClass.values()) if (s.name().startsWith( "RBA_" ) && third.get( s ) != null) subs++;
		check( subs == 20, "20 subclass badges as in the mod, saw " + subs );

		//damage icons
		check( RBAHero.damageIcon( null, new Object(), FloatingText.PHYS_DMG, -1 ) == FloatingText.PHYS_DMG, "plain damage keeps its icon" );

		//the punching bag on floor 26
		try {
			GamesInProgress.selectedClass = HeroClass.RBA_NOA;
			Dungeon.seed = 5;
			Dungeon.sandbox = true;
			Dungeon.init();
			Dungeon.depth = 26;
			Level l = Dungeon.newLevel();
			RBASandbag bag = null;
			for (Mob m : l.mobs) if (m instanceof RBASandbag) bag = (RBASandbag) m;
			check( l instanceof LastLevel, "floor 26 is the last level" );
			check( bag != null, "punching bag on floor 26" );
			if (bag != null) check( l.passable[bag.pos], "punching bag on a floor cell" );
		} catch (Throwable t) {
			check( false, "floor 26 build " + t );
			t.printStackTrace( System.out );
		}

		System.out.println( "SUMMARY bluearchive " + checks + " checks, " + failures + " failures" );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
