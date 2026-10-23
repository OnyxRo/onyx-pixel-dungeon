package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BAElementalBullet;
import com.watabou.noosa.Image;
public class BAExplosiveBullet extends BAElementalBulletBuff {
    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(BAElementalBullet.ElementalReload.EXPLOSIVE_BULLET.tintColor);
    }
}
