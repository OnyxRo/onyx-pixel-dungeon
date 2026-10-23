package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BAElementalBullet;
import com.watabou.noosa.Image;
public class BAIceBullet extends BAElementalBulletBuff {
    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(BAElementalBullet.ElementalReload.ICE_BULLET.tintColor);
    }
}
