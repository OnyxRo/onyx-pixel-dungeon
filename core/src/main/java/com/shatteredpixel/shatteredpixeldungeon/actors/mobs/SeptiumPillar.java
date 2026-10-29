package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChaosTime;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MaxGuard;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.FinalLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewCavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PillarSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PylonSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;



import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

public abstract class SeptiumPillar extends Mob {





    {
        HP = HT = 100;

        viewDistance = 99;

        //for doomed resistance
        EXP = 25;
        maxLvl = -2;
        baseSpeed = 0;

        state = PASSIVE;
        alignment = Alignment.NEUTRAL;

        properties.add(Property.BOSS);
        properties.add(Property.KISEKI);
        properties.add(Property.IMMOVABLE);
        properties.add(Property.INORGANIC);
        immunities.add( Terror.class );
        immunities.add( Amok.class );
        immunities.add( Charm.class );
        immunities.add( Sleep.class );
        immunities.add( Vertigo.class );
        immunities.add( Paralysis.class );
        immunities.add( SoulMark.class );
        immunities.add( Weakness.class );
        immunities.add( Blindness.class );
    }

    //Each lit pillar uses its art every ZAP_EVERY turns, as Abyssalnite meant (its zap() spent 15 turns).
    //Abyssalnite's act() called zap() every turn and called next() inside its loops, so only the arts are kept.
    //Arts aimed at the hero miss an invisible hero, like every other monster
    protected static final int ZAP_EVERY = 15;
    protected int zapCD = Random.IntRange( 4, ZAP_EVERY );

    @Override
    protected boolean act() {
        if (alignment == Alignment.ENEMY && --zapCD <= 0){
            zapCD = ZAP_EVERY;
            if (zap() && sprite != null && Dungeon.level.heroFOV[pos]){
                sprite.flash();
                GLog.w( Messages.get( this, "message" ) );
            }
        }
        spend( TICK );
        return true;
    }

    //the monsters a pillar's art helps, everything hostile but the pillars themselves
    protected static ArrayList<Mob> monsters(){
        ArrayList<Mob> list = new ArrayList<>();
        for (Mob m : Dungeon.level.mobs.toArray( new Mob[0] )){
            if (m.isAlive() && m.alignment == Alignment.ENEMY && !m.properties().contains( Property.KISEKI )) list.add( m );
        }
        return list;
    }

    protected static boolean heroTargetable(){
        return hero != null && hero.isAlive() && hero.invisible <= 0;
    }

    @Override
    public void notice() {
        //do nothing
    }



    @Override
    public boolean add(Buff buff) {
        //immune to all buffs/debuffs when inactive
        if (alignment != Alignment.NEUTRAL) {
            return super.add(buff);
        }
        return false;
    }

    private static final String ALIGNMENT = "alignment";
    private static final String ZAP_CD = "zap_cd";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ALIGNMENT, alignment);
        bundle.put(ZAP_CD, zapCD);


    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        alignment = bundle.getEnum(ALIGNMENT, Alignment.class);
        if (bundle.contains(ZAP_CD)) zapCD = bundle.getInt(ZAP_CD);

    }

    @Override
    public boolean interact(Char c) {
        return true;
    }

    //uses the pillar's art, false when it had nothing to do
    protected abstract boolean zap();

    @Override
    public void die(Object cause) {
        super.die(cause);
        Statistics.killcount++;
        if (Statistics.killcount == 2) {
            GLog.n(Messages.get(this, "elements"));
            ((FinalLevel)Dungeon.level).progress();
        }
        if (Statistics.killcount == 4) {
            GLog.n(Messages.get(this, "elements"));
            ((FinalLevel)Dungeon.level).progress();
        }
        if (Dungeon.level instanceof FinalLevel) ((FinalLevel)Dungeon.level).checkPillars();
    }


    public void activate(){
        alignment = Alignment.ENEMY;
        ((PillarSprite) sprite).activate();
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        //immune to damage when inactive
        return (alignment == Alignment.NEUTRAL);
    }

    @Override
    public String description() {
        return Messages.get(SeptiumPillar.class, "desc") + "\n\n" + Messages.get(this, "desc");
    }

    {
        immunities.add( Terror.class );
        immunities.add( Amok.class );
        immunities.add( Charm.class );
        immunities.add( Sleep.class );
        immunities.add( Vertigo.class );
        immunities.add( Paralysis.class );
        immunities.add( SoulMark.class );
        immunities.add( Weakness.class );
        immunities.add( Blindness.class );
    }

    //sets every monster's attacks on fire
    public static class FlamePillar extends SeptiumPillar {
        {
            spriteClass = PillarSprite.Flame.class;
        }

        @Override
        public void die(Object cause) {
            super.die(cause);
            Statistics.ThirdActive=true;
        }

        @Override
        protected boolean zap() {
            ArrayList<Mob> list = monsters();
            for (Mob m : list) Buff.affect( m, FireImbue.class ).set( 10f );
            return !list.isEmpty();
        }
    }

    //shields every monster. Abyssalnite gave them its Max Guard buff, which nothing in its code ever checked
    public static class GroundPillar extends SeptiumPillar {
        {
            spriteClass = PillarSprite.Ground.class;
        }

        @Override
        public void die(Object cause) {
            super.die(cause);
            Statistics.FirstActive=true;
        }

        @Override
        protected boolean zap() {
            ArrayList<Mob> list = monsters();
            for (Mob m : list) Buff.affect( m, Barrier.class ).setShield( 15 );
            return !list.isEmpty();
        }
    }

    //heals every monster below a third of its health
    public static class WaterPillar extends SeptiumPillar {
        {
            spriteClass = PillarSprite.Water.class;
        }

        @Override
        public void die(Object cause) {
            super.die(cause);
            Statistics.SecondActive=true;
        }

        @Override
        protected boolean zap() {
            boolean any = false;
            for (Mob m : monsters()){
                if (m.HP < m.HT / 3){
                    Buff.affect( m, Healing.class ).setHeal( 20, 0, 10 );
                    any = true;
                }
            }
            return any;
        }
    }

    //speeds every monster up
    public static class WindPillar extends SeptiumPillar {
        {
            spriteClass = PillarSprite.Wind.class;
        }

        @Override
        public void die(Object cause) {
            super.die(cause);
            Statistics.FourthActive=true;
        }

        @Override
        protected boolean zap() {
            ArrayList<Mob> list = monsters();
            for (Mob m : list) Buff.affect( m, Adrenaline.class, 3f );
            return !list.isEmpty();
        }
    }

    //twists the hero's time, so they lose turns
    public static class ChronosPillar extends SeptiumPillar {
        {
            spriteClass = PillarSprite.Chronos.class;
        }

        @Override
        protected boolean zap() {
            if (!heroTargetable()) return false;
            Buff.affect( hero, ChaosTime.class ).extend( 6f );
            return true;
        }
    }

    //marks a line through the hero in red, then fires a beam along it next turn
    public static class HeavenPillar extends SeptiumPillar {
        {
            spriteClass = PillarSprite.Heaven.class;
        }

        private int beamTarget = -1;

        @Override
        protected boolean act() {
            if (alignment == Alignment.ENEMY && beamTarget != -1){
                fire();
                spend( TICK );
                return true;
            }
            return super.act();
        }

        @Override
        protected boolean zap() {
            if (!heroTargetable()) return false;
            beamTarget = hero.pos;
            Ballistica b = new Ballistica( pos, beamTarget, Ballistica.WONT_STOP );
            if (sprite != null && sprite.parent != null){
                for (int c : b.subPath( 1, b.dist )){
                    TargetedCell t = new TargetedCell();
                    t.reset( c, Actor.now() + 1 );
                    sprite.parent.add( t );
                }
            }
            hero.interrupt();
            return true;
        }

        private void fire(){
            Ballistica b = new Ballistica( pos, beamTarget, Ballistica.WONT_STOP );
            beamTarget = -1;
            HashSet<Char> hit = new HashSet<>();
            for (int c : b.subPath( 1, b.dist )){
                Char ch = Actor.findChar( c );
                if (ch != null && ch.alignment != alignment && ch.invisible <= 0) hit.add( ch );
            }
            if (sprite != null && sprite.parent != null){
                sprite.parent.add( new Beam.DeathRay( sprite.center(), DungeonTilemap.raisedTileCenterToWorld( b.collisionPos ) ) );
            }
            for (Char ch : hit){
                ch.damage( Random.NormalIntRange( 20, 30 ), new Eye.DeathGaze() );
                if (ch.sprite != null && Dungeon.level.heroFOV[ch.pos]){
                    ch.sprite.flash();
                    CellEmitter.center( ch.pos ).burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
                }
                if (ch == hero && !ch.isAlive()){
                    Dungeon.fail( this );
                    GLog.n( Messages.get( Char.class, "kill", name() ) );
                }
            }
        }

        private static final String BEAM = "beam";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put( BEAM, beamTarget );
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            if (bundle.contains( BEAM )) beamTarget = bundle.getInt( BEAM );
        }
    }

    //makes the hero dizzy and calls every monster to them
    public static class PhantomPillar extends SeptiumPillar {
        {
            spriteClass = PillarSprite.Phantom.class;
        }

        @Override
        protected boolean zap() {
            if (!heroTargetable()) return false;
            Buff.affect( hero, Vertigo.class, 3f );
            for (Mob m : monsters()) m.beckon( hero.pos );
            if (hero.sprite != null) hero.sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
            return true;
        }
    }
}
