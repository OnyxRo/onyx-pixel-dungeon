package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BAElementalBullet;
import com.watabou.noosa.Image;
public class BAElectricBullet extends BAElementalBulletBuff {
    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(BAElementalBullet.ElementalReload.ELECTRIC_BULLET.tintColor);
    }
}
