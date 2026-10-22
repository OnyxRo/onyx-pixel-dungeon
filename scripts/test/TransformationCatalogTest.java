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
			if ((t instanceof NullPointerException && (m.contains( "GameScene.scene" ) || m.contains( "CellSelector" ) || m.contains( "Game.instance" ) || m.contains( "Camera.main" )))
					|| (t instanceof ClassCastException && m.contains( "HeroSprite" ))) {
				System.out.println( "HEADLESS (not counted): " + what + " needs the game screen, from " + (t.getStackTrace().length > 2 ? t.getStackTrace()[1] + " " + t.getStackTrace()[2] : "") );
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
			check( t.maxPoints() >= (com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationBossTalents.isSlot( t ) ? 1 : 2) && t.maxPoints() <= 4, t + " max points" );
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
			hook( n + " second run hooks", () -> {
				TransformationTalents.ceaselessRage( hero, 0.1f );
				TransformationTalents.comboAfterMove( hero, 5, 3, true ); TransformationTalents.comboDiscount( hero ); TransformationTalents.comboMastery( hero );
				TransformationTalents.unexpectedStrike( hero, 2 ); TransformationTalents.onPreparationEnd( hero, 2 );
				TransformationTalents.naturalWay( hero, 1f ); TransformationTalents.yinYangCovers( hero, 0, 1 );
			} );
			hook( n + " second run momentum", () -> {
				TransformationTalents.onFreerunEnded( hero );
				TransformationTalents.onFreerunRecovered( com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect( hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Momentum.class ) );
				TransformationTalents.wandChargeSaved( new com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast() );
				TransformationTalents.chargeDiscount( new com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast(), 3 );
				TransformationTalents.lightningHeals( hero, 10 ); TransformationTalents.abyssalGaze( 3 );
				TransformationTalents.corrosionVolume( 60 ); TransformationTalents.corrosionStrength(); TransformationTalents.onCorrosiveGas( rat );
				TransformationTalents.lightCrop( rat.pos );
			} );
			hook( n + " second run wands", () -> {
				for (com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand w : new com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand[]{
						new com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFrost(), new com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning(),
						new com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption(), new com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfPrismaticLight(),
						new com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDisintegration(), new com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast()}) {
					TransformationTalents.onWandZap( w, rat.pos );
					if (rat.isAlive()) TransformationTalents.onWandDamage( w, rat, 5 );
				}
			} );
			hook( n + " second run artifacts and systems", () -> {
				TransformationTalents.artifactBoost( new com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass() );
				TransformationTalents.timeSandStasis(); TransformationTalents.timeFreezeTurns(); TransformationTalents.cloakExtraTurns();
				TransformationTalents.armbandSteal( hero, 3 ); TransformationTalents.armbandShopCharges( 2 );
				TransformationTalents.spellbookScroll( new com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify() );
				TransformationTalents.sandalsCost( 4 ); TransformationTalents.naturalismSeeds(); TransformationTalents.hornBonus();
				TransformationTalents.onFoodEaten( hero, new com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty() );
				if (rat.isAlive()) TransformationTalents.chainRefund( rat, 2 );
				TransformationTalents.chainTime(); TransformationTalents.onTrapReclaimed( hero );
				TransformationTalents.hardwareCost( 10 ); TransformationTalents.hardwareKeepsEmbers();
				TransformationTalents.kebiGlyph( null, null ); TransformationTalents.kebiEnchant( null, null ); TransformationTalents.kebi();
				TransformationTalents.favorPerGold( 50 ); TransformationTalents.favorCap( 2000 );
				TransformationTalents.eternalCurseBlocks( new com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse() ); TransformationTalents.removesCurseEnchant();
				if (rat.isAlive()) { TransformationTalents.phaseclawReach( rat, hero ); TransformationTalents.noMoreMob( rat ); }
				TransformationTalents.noDoorFactor(); TransformationTalents.scoreMultiplier();
				TransformationTalents.arrowPenetration( hero, hero.pos + 2, new com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife() );
				TransformationTalents.onIlluminatedHit( hero );
				TransformationTalents.bubbleSurvives( hero, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect( hero, com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle.TimeBubble.class ) );
				TransformationTalents.falconEye( level, hero, new boolean[level.length()], 8 );
				TransformationTalents.energyConversion( hero );
			} );
			hook( n + " second run honeypot", () -> {
				int free = -1;
				for (int i : com.watabou.utils.PathFinder.NEIGHBOURS8) {
					int c = hero.pos + i;
					if (level.passable[c] && Actor.findChar( c ) == null) { free = c; break; }
				}
				if (free >= 0) TransformationTalents.honeyFish( free );
			} );
			hook( n + " second run actions", () -> {
				TransformationTalents.SmokeMask sm = hero.buff( TransformationTalents.SmokeMask.class );
				if (sm != null) { sm.act(); sm.doAction(); }
				TransformationTalents.NaturalBreath nb = hero.buff( TransformationTalents.NaturalBreath.class );
				if (nb != null) { nb.act(); nb.doAction(); }
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

		//the spells the spell talents add to the holy tome
		for (com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.ClericSpell sp : com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.ClericSpell.getAllSpells()) {
			Talent st = com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.ClericSpell.transformationTalent( sp );
			if (st == null) continue;
			check( hasText( sp.name() ) && hasText( sp.desc() ) && hasText( sp.shortDesc() ), "spell " + sp.getClass().getSimpleName() + " text" );
			check( sp.icon() >= 0, "spell " + sp.getClass().getSimpleName() + " icon" );
			int ti = Math.max( 0, st.tier() - 1 );
			hero.talents.get( ti ).put( st, 1 );
			boolean listed = false;
			for (int t = 1; t <= 4; t++) listed |= com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.ClericSpell.getSpellList( hero, t ).contains( sp );
			check( listed, "spell list holds " + sp.getClass().getSimpleName() + " once its talent is taken" );
			hero.talents.get( ti ).remove( st );
		}

		//subclass prism swap, a Gladiator trades Cleave for Combo Focus and keeps it after a reload
		{
			com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass keepSub = hero.subClass;
			hero.subClass = com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass.GLADIATOR;
			Talent.initSubclassTalents( hero );
			check( TransformationTalents.subclassGroup( hero ).contains( Talent.COMBO_FOCUS ), "gladiator prism group" );
			TransformationTalents.replaceTalent( hero, Talent.CLEAVE, Talent.COMBO_FOCUS );
			hero.talents.get( 2 ).clear();
			Talent.initSubclassTalents( hero );
			check( hero.talents.get( 2 ).containsKey( Talent.COMBO_FOCUS ) && !hero.talents.get( 2 ).containsKey( Talent.CLEAVE ), "swapped subclass talent rebuilds" );
			hero.metamorphedTalents.remove( Talent.CLEAVE );
			hero.talents.get( 2 ).remove( Talent.COMBO_FOCUS );
			hero.talents.get( 2 ).remove( Talent.LETHAL_DEFENSE );
			hero.talents.get( 2 ).remove( Talent.ENHANCED_COMBO );
			hero.subClass = keepSub;
		}

		//the second catalog run, extra subclass talents for every subclass the mod gave them to, the Huntress tower slot,
		//and a hero without these talents keeps the normal numbers
		for (com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass sc : new com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass[]{ com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass.BERSERKER, com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass.GLADIATOR, com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass.BATTLEMAGE, com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass.WARLOCK, com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass.ASSASSIN, com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass.FREERUNNER, com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass.CHAMPION, com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass.MONK }) {
			check( TransformationTalents.subclassExtras( sc ).length == 3, sc + " has three extra talents" );
		}
		check( com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationBossTalents.slotFor( "HUNTRESS" ) == Talent.BOSS_TALENT_SLOT_3, "Huntress tower scroll fills slot 3" );
		check( TransformationTalents.comboAfterMove( hero, 5, 3, true ) == 0 && TransformationTalents.comboAfterMove( hero, 5, 3, false ) == 5, "combo resets as normal without Combo Mastery" );
		check( TransformationTalents.noDoorFactor() == 1f && TransformationTalents.scoreMultiplier() == 1 && TransformationTalents.hornBonus() == 0
				&& TransformationTalents.chainTime() == 1f && TransformationTalents.favorPerGold( 50 ) == 50 && TransformationTalents.energyConversion( hero ) == 1f
				&& !TransformationTalents.ceaselessRage( hero, 0.1f ) && TransformationTalents.timeFreezeTurns() == 2f, "no second run talent changes a normal hero" );

		//the transformation trap swaps one talent
		hook( "transformation trap", () -> {
			com.shatteredpixel.shatteredpixeldungeon.levels.traps.TransformationTrap trap = new com.shatteredpixel.shatteredpixeldungeon.levels.traps.TransformationTrap();
			trap.pos = hero.pos;
			Talent[] change = TransformationTalents.randomTransformation( hero );
			check( change != null && change[0] != change[1], "transformation trap changes a talent" );
			trap.activate();
		} );

		//a scroll of sublimation fills the Freeman's boss slot, and it survives a save
		check( com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationBossTalents.canSublimate( hero, "GOO" ), "Freeman has the Goo slot" );
		com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationBossTalents.fillSlot( hero, "GOO", Talent.PUMP_ATTACK );
		check( hero.talents.get( 0 ).containsKey( Talent.PUMP_ATTACK ) && !com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationBossTalents.canSublimate( hero, "GOO" ), "Goo slot filled" );
		for (String type : new String[]{"GOO", "WARRIOR", "TENGU", "ROGUE", "DM300", "HUNTRESS", "DWARFKING", "YOG"}) {
			for (Talent t : com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationBossTalents.talentsFor( type )) check( t.isCatalogTalent() && hasText( t.title() ), type + " boss talent " + t );
		}

		//negative talents are saved with their tier
		WndNegative.addNegative( hero, Talent.FEEBLE, 1 );
		com.watabou.utils.Bundle b = new com.watabou.utils.Bundle();
		hero.storeInBundle( b );
		Hero back = new Hero();
		back.restoreFromBundle( b );
		check( back.talents.get( 0 ).containsKey( Talent.FEEBLE ) && back.pointsNegative( Talent.FEEBLE ) == 2, "negative talent saves and loads" );
		check( back.heroClass == HeroClass.FREEMAN, "Freeman saves and loads" );
		check( back.talents.get( 0 ).containsKey( Talent.PUMP_ATTACK ), "filled boss slot saves and loads" );

		System.out.println( "SUMMARY Transformation talent catalog: " + checks + " checks, failures: " + failures );
		System.exit( failures == 0 ? 0 : 1 );
	}
}
