package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;

public class Faint extends FlavourBuff{

    {
        type = Buff.buffType.POSITIVE;
        announced = true;
    }

    @Override
    public boolean attachTo( Char target ) {
        if (super.attachTo( target )) {
            target.paralysed++;
            target.invisible++;
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void detach() {
        super.detach();
        if (target.paralysed > 0)
            target.paralysed--;
        if (target.invisible > 0)
            target.invisible--;
    }



    @Override
    protected void onRemove() {
        hero.sprite.play(hero.sprite.idle, true);
        Camera.main.panFollow(hero.sprite,1f);

        hero.paralysed = 0;
        hero.invisible = 0;
        GameScene.scene.menu.active = GameScene.scene.menu.visible = true;
        hero.sprite.play(hero.sprite.idle);
        int portalpos = ((Arena)level).amuletCell;
        for (Mob m : level.mobs){
            if (m instanceof Arena.AmuletTower) portalpos = m.pos;
        }
        hero.sprite.move(hero.pos, portalpos);
        hero.pos = portalpos;
        Sample.INSTANCE.play( Assets.Sounds.MASTERY );
        CellEmitter.center(hero.pos).burst( Speck.factory( Speck.STAR),  10 );
        Dungeon.observe();
        GameScene.updateFog();
        super.onRemove();
    }

    @Override
    public int icon() {
        return BuffIndicator.FAINT;
    }


}
