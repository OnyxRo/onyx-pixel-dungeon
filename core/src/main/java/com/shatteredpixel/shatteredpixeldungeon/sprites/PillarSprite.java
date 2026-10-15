/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Pylon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SeptiumPillar;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Yog;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogFist;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.CorrosionParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Camera;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

public abstract class PillarSprite extends MobSprite {

    private static final float SLAM_TIME	= 0.33f;

    protected int boltType;

    private Animation activeIdle;

    protected abstract int texOffset();

    private Emitter particles;
    protected abstract Emitter createEmitter();

    public PillarSprite() {
        super();

        texture( Assets.Sprites.PILLAR );

        int c = texOffset();

        TextureFilm frames = new TextureFilm( texture, 24,48  );

        activeIdle = new Animation( 4, true );
        activeIdle.frames( frames, c+0);

        idle = new Animation( 4, true );
        idle.frames( frames, 7);

        run = idle.clone();

        attack = idle.clone();

        zap = idle.clone();

        die = new Animation( 0, false );
        die.frames( frames, c+0 );

        play( idle );
    }

    @Override
    public void link( Char ch ) {
        super.link( ch );

        if ((ch instanceof SeptiumPillar && ch.alignment == Char.Alignment.ENEMY) && particles == null){
            particles = createEmitter();
            activate();
        }
    }

    @Override
    public void update() {
        super.update();

        if (particles != null){
            particles.visible = visible;
        }
    }

    @Override
    public void die() {
        super.die();
        if (particles != null){
            particles.on = false;
        }
        emitter().burst(BlastParticle.FACTORY, 20);
        Sample.INSTANCE.play(Assets.Sounds.BLAST);
    }

    @Override
    public void kill() {
        super.kill();
        if (particles != null){
            particles.killAndErase();
        }
    }



    public void zap( int cell ) {

    }

    @Override
    public void onComplete( Animation anim ) {
        super.onComplete( anim );
        if (anim == attack) {
            Camera.main.shake( 4, 0.2f );
        } else if (anim == zap) {
            idle();
        }
    }

    public static class Flame extends PillarSprite {

        @Override
        protected int texOffset() {
            return 2;
        }

        @Override
        protected Emitter createEmitter() {
            Emitter emitter = emitter();
            emitter.pour( FlameParticle.FACTORY, 0.06f );
            return emitter;
        }

        @Override
        public int blood() {
            return 0xFFFFDD34;
        }

    }

    public static class Wind extends PillarSprite {

        @Override
        protected int texOffset() {
            return 3;
        }

        @Override
        protected Emitter createEmitter() {
            Emitter emitter = emitter();
            emitter.pour( LeafParticle.GENERAL, 0.06f );
            return emitter;
        }

        @Override
        public int blood() {
            return 0xFF7F5424;
        }

    }

    public static class Water extends PillarSprite {


        @Override
        protected int texOffset() {
            return 1;
        }

        @Override
        protected Emitter createEmitter() {
            Emitter emitter = emitter();
            emitter.pour(Speck.factory(Speck.HEALING), 0.25f );
            return emitter;
        }

        @Override
        public int blood() {
            return 0xFFB8BBA1;
        }

    }

    public static class Ground extends PillarSprite {


        @Override
        protected int texOffset() {
            return 0;
        }

        @Override
        protected Emitter createEmitter() {
            Emitter emitter = emitter();
            emitter.pour(CorrosionParticle.MISSILE, 0.06f );
            return emitter;
        }

        @Override
        public int blood() {
            return 0xFF7F7F7F;
        }

    }

    public static class Phantom extends PillarSprite {

        @Override
        protected int texOffset() {
            return 6;
        }

        @Override
        protected Emitter createEmitter() {
            Emitter emitter = emitter();
            emitter.pour(SparkParticle.STATIC, 0.06f );
            return emitter;
        }

        @Override
        public int blood() {
            return 0xFFFFFFFF;
        }

    }

    public static class Chronos extends PillarSprite {


        @Override
        protected int texOffset() {
            return 4;
        }

        @Override
        protected Emitter createEmitter() {
            Emitter emitter = emitter();
            emitter.pour(ShadowParticle.MISSILE, 0.06f );
            return emitter;
        }

        @Override
        public int blood() {
            return 0xFF4A2F53;
        }

    }

    public static class Heaven extends PillarSprite {


        @Override
        protected int texOffset() {
            return 5;
        }

        @Override
        protected Emitter createEmitter() {
            Emitter emitter = emitter();
            emitter.pour(ShadowParticle.MISSILE, 0.06f );
            return emitter;
        }

        @Override
        public int blood() {
            return 0xFF4A2F53;
        }

    }




    public void activate(){
        idle = activeIdle.clone();
        idle();
    }

}
