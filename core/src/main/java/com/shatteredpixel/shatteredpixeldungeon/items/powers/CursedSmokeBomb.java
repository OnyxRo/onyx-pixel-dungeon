package com.shatteredpixel.shatteredpixeldungeon.items.powers;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.RogueArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class CursedSmokeBomb extends ActivatedPower {

    {
        image = ItemSpriteSheet.SMOKEBOMB;
        mp_cost = 4;
    }

    @Override
    public boolean usesTargeting() {
        return false;//Does actually use targeting, but it's unique enough to have to be hardcoded here.
    }

    @Override
    public void affectCell(int pos) {}

    @Override
    public void activatePower(Hero hero) {
        GameScene.selectCell( teleporter );
    }

    private static CellSelector.Listener teleporter = new  CellSelector.Listener() {

        @Override
        public void onSelect( Integer target ) {
            if (target != null) {

                PathFinder.buildDistanceMap(curUser.pos, Dungeon.level.passable, 8);

                if ( PathFinder.distance[target] == Integer.MAX_VALUE ||
                        !Dungeon.level.heroFOV[target] ||
                        !(Dungeon.level.passable[target] || Dungeon.level.avoid[target]) ||
                        Actor.findChar( target ) != null) {

                    GLog.w( Messages.get(CursedSmokeBomb.class, "fov") );
                    return;
                }

                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[Dungeon.level.mobs.size()])) {
                    if (Dungeon.level.heroFOV[mob.pos]) {
                        Buff.prolong( mob, Blindness.class, 2 );
                        if (mob.state == mob.HUNTING) mob.state = mob.WANDERING;
                        mob.sprite.emitter().burst( Speck.factory( Speck.LIGHT ), 4 );
                    }
                }

                ScrollOfTeleportation.appear( curUser, target );
                CellEmitter.get( target ).burst( Speck.factory( Speck.WOOL ), 10 );
                Sample.INSTANCE.play( Assets.Sounds.PUFF );
                Dungeon.level.occupyCell( curUser );
                Dungeon.observe();
                GameScene.updateFog();
            }
        }

        @Override
        public String prompt() {
            return Messages.get(RogueArmor.class, "prompt");
        }
    };
}
