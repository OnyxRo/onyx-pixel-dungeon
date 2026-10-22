import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.BarricadeRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

/**
 * Summoning PD Reincarnated leftovers, headless. Food healing, the Barricade room, the two badges, the guide pages,
 * and (added as they are ported) the ring artifacts, cloak glyphs, Soul of Yendor, talents, the Adventurer,
 * conducts, game modes and the tier challenge.
 */
public class SummoningLeftoversTest {

	static int failures = 0;
	static void fail( String what ) { failures++; System.out.println( "FAIL: " + what ); }
	static void check( boolean ok, String what ) { if (!ok) fail( what ); }

	static Hero startHero( HeroClass cls ) {
		GamesInProgress.selectedClass = cls;
		Dungeon.seed = 515151L;
		Dungeon.sandbox = true;
		Dungeon.init();
		Hero hero = Dungeon.hero;
		MobArenaTest.levelDepth = -1;
		MobArenaTest.level( 3 );
		Actor.remove( Dungeon.hero );
		int start = Dungeon.hero.pos;
		Dungeon.hero = hero;
		hero.live();
		hero.sprite = TestBoot.sprite();
		hero.sprite.ch = hero;
		hero.pos = start;
		Actor.add( hero );
		return hero;
	}

	public static void main( String[] args ) throws Exception {
		TestBoot.boot();
		Hero hero = startHero( HeroClass.CONJURER );

		//food heals the Conjurer
		hero.HP = 1;
		com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.onFoodEaten( hero );
		check( hero.HP == 6, "Conjurer should heal 5 from food, HP " + hero.HP );

		//the Barricade room
		BarricadeRoom room = new BarricadeRoom();
		room.set( 2, 2, 12, 12 );
		room.connected.put( new BarricadeRoom(), new com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room.Door( 2, 7 ) );
		room.paint( Dungeon.level );
		int bars = 0;
		for (int x = 3; x < 12; x++) for (int y = 3; y < 12; y++) {
			if (Dungeon.level.map[x + y * Dungeon.level.width()] == Terrain.BARRICADE) bars++;
		}
		check( bars > 0 && bars < 81, "Barricade room should hold some barricades, has " + bars );

		//badges and guide pages have their text
		for (Badges.Badge b : new Badges.Badge[]{Badges.Badge.NIGHTMARE_SLAIN, Badges.Badge.SPIRIT_SLAIN}) {
			check( !b.title().contains( "!!!" ) && !b.desc().contains( "!!!" ), "badge text missing for " + b );
		}
		for (String page : new String[]{Document.GUIDE_SUMMONING, Document.GUIDE_TIERING}) {
			check( Document.ADVENTURERS_GUIDE.pageNames().contains( page ), "guide page missing " + page );
			check( !Document.ADVENTURERS_GUIDE.pageTitle( page ).contains( "!!!" ), "guide page title missing " + page );
			check( !Document.ADVENTURERS_GUIDE.pageBody( page ).contains( "!!!" ), "guide page body missing " + page );
		}

		ringArtifacts( hero );
		soulOfYendor( hero );

		finish();
	}

	//Momentum Boots, Fuel Container and Silky Quiver, and that nothing changes without them
	static void ringArtifacts( Hero hero ) {
		hero.subClass = com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass.NONE;
		float plain = 1f;
		com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.onStep( hero );
		check( hero.buff( com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.BootsMomentum.class ) == null, "momentum without the boots" );

		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.MomentumBoots boots = new com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.MomentumBoots();
		boots.level( 2 );
		boots.activate( hero );
		for (int i = 0; i < 4; i++) com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.onStep( hero );
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.BootsMomentum m = hero.buff( com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.BootsMomentum.class );
		check( m != null && m.momentumStacks > 0, "walking in the boots should build momentum" );
		if (m != null) {
			m.doAction();
			check( m.freerunning(), "the boots should start freerunning" );
			check( Math.abs( com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.speed( hero, 1f ) - plain * 1.5f ) < 0.01f, "freerunning should be 1.5x speed, " + com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.speed( hero, 1f ) + " vs " + plain );
			check( com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.evasion( hero ) > 0, "freerunning should add evasion" );
			m.detach();
		}
		boots.cursed = true;
		check( com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.speed( hero, 3f ) <= 1f, "cursed boots should cap speed at normal, " + com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.speed( hero, 1f ) );
		for (com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff b : hero.buffs( com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.MomentumBoots.momentumBuff.class )) b.detach();

		//fuel container, a wand with no charge zaps from the container
		com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile wand = new com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile();
		wand.curCharges = 0;
		check( !com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.fuelCanZap( hero, wand, 1 ), "a wand zaps without the container" );
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.FuelContainer fuel = new com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.FuelContainer();
		fuel.charge = 30;
		fuel.activate( hero );
		check( com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.fuelCanZap( hero, wand, 1 ), "the container should pay for an empty wand" );
		wand.curCharges = -1;
		com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.fuelAfterZap( hero, wand );
		check( wand.curCharges == 0 && fuel.charge == 20, "the container should spend 10 charge, has " + fuel.charge + ", wand " + wand.curCharges );
		check( com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.wandCanRecharge( hero ), "an uncursed container stops recharging" );
		fuel.cursed = true;
		check( !com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.wandCanRecharge( hero ), "a cursed container should stop recharging" );
		for (com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff b : hero.buffs( com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.FuelContainer.fuelBuff.class )) b.detach();

		//silky quiver, thrown weapons charge it and the mark makes them hit
		com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife dart = new com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife();
		com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat rat = new com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat();
		check( com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.accuracy( hero, rat, dart, 1f ) == 1f, "accuracy changed without the quiver" );
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.SilkyQuiver quiver = new com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.SilkyQuiver();
		quiver.charge = 0;
		quiver.activate( hero );
		for (int i = 0; i < 4; i++) com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.onMissileUsed( 0.25f );
		check( quiver.charge == 1, "four thrown weapons should give one quiver charge, has " + quiver.charge );
		com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect( rat, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.powers.QuiverMark.class, 5f );
		check( com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.accuracy( hero, rat, dart, 1f ) == Float.POSITIVE_INFINITY, "the quiver mark should make thrown weapons hit" );
		quiver.cursed = true;
		com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat rat2 = new com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat();
		check( com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.accuracy( hero, rat2, dart, 1f ) < 0.5f, "a cursed quiver should spoil the aim" );
		for (com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff b : hero.buffs( com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ringartifacts.SilkyQuiver.quiverBuff.class )) b.detach();
		for (Object o : new Object[]{boots, fuel, quiver}) {
			com.shatteredpixel.shatteredpixeldungeon.items.Item it = (com.shatteredpixel.shatteredpixeldungeon.items.Item) o;
			check( !it.name().contains( "!!!" ) && !it.desc().contains( "!!!" ), "text missing for " + it.getClass().getSimpleName() );
		}
	}

	//the Soul of Yendor, its passive hooks only act while worn
	static void soulOfYendor( Hero hero ) {
		check( com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.soulRegenDelay( hero, 10f ) == 10f, "regen changed without the soul" );
		check( com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.soulNaturalism( hero, 0 ) == 0, "grass changed without the soul" );
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SoulOfYendor soul = new com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SoulOfYendor();
		soul.level( 5 );
		soul.activate( hero );
		check( com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.soulRegenDelay( hero, 10f ) < 10f, "the soul should speed up regeneration" );
		check( com.shatteredpixel.shatteredpixeldungeon.actors.hero.SummoningHooks.soulNaturalism( hero, 0 ) > 0, "the soul should act like the sandals" );
		int before = soul.availableEnergy();
		int left = soul.consumeEnergy( 10 );
		check( left == 0 && soul.availableEnergy() == before - 10, "the soul should pay alchemy energy" );
		com.watabou.utils.Bundle b = new com.watabou.utils.Bundle();
		soul.storeInBundle( b );
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SoulOfYendor copy = new com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SoulOfYendor();
		copy.restoreFromBundle( b );
		check( copy.availableEnergy() == soul.availableEnergy() && copy.level() == 5, "the soul lost its charge or level on reload" );
		check( !soul.name().contains( "!!!" ) && !soul.desc().contains( "!!!" ), "text missing for the soul" );
		for (com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff bf : hero.buffs( com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SoulOfYendor.omniBuff.class )) bf.detach();
	}

	static void finish() {
		System.out.println( "SUMMARY summoning leftovers failures=" + failures );
		System.exit( failures > 0 ? 1 : 0 );
	}
}
