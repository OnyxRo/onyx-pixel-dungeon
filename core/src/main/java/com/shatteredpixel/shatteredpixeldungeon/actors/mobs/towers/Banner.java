package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Battlecry;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Inspired;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.custom.CPRed;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FlagFriendSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerTotemSprite;
import com.watabou.utils.PathFinder;

public class Banner extends TowerCTotem{
    //gives a bit of damage speed boost
    {
        HP = HT = 10;
        spriteClass = FlagFriendSprite.class;
        abTimeMax = 15;
        upgCount = 0;
        sellable = false;
    }
    protected void useAbility(int cell){
        Char ch = Char.findChar(cell);
        if (ch!=null){
            if (!(ch instanceof TowerNotliving)){
                if (ch.buff(Inspired.class) == null) Buff.affect(ch, Inspired.class);
                else ch.buff(Inspired.class).add(1);
            }
            ch.heal(1 + Dungeon.hero.lvl/3);
        }
    }
    protected void searchAndUse(){
        WandOfBlastWave.BlastWave.blast(pos);
        for (int i : PathFinder.NEIGHBOURS25) if (Char.findChar(pos+i)!=null){
            Char ch = Char.findChar(pos + i);
            if (ch.alignment==alignment && ch != this)
                useAbility(pos+i);
        }
    }
}
