package com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mari.RBAIdolConcert;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBACrossNecklace;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

public class RBAEncore extends RBAMariMiracle {
    public static RBAEncore INSTANCE = new RBAEncore();

    @Override
    public int icon() {
        return HeroIcon.RBA_ENCORE;
    }

    @Override
    public float chargeUse(Hero hero) {
        return 16-4*hero.pointsInTalent(Talent.RBA_MARI_ARMOR3_1);
    }

    @Override
    public boolean canCast(Hero hero) {
        return super.canCast(hero) && hero.hasTalent(Talent.RBA_MARI_ARMOR3_1) && hero.buff(RBAIdolConcert.Encore.class) != null;
    }

    @Override
    public void onCast(RBACrossNecklace cross, Hero hero) {
        RBAIdolConcert.concert(hero);
        hero.buff(RBAIdolConcert.Encore.class).detach();
        onMiracleCast(cross, hero);
    }
}
