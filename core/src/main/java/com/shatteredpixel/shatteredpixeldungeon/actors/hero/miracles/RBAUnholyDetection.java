package com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles;


import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBAHero;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Identification;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBACrossNecklace;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;

public class RBAUnholyDetection extends RBAInventoryMariMiracle {
    public static final RBAUnholyDetection INSTANCE = new RBAUnholyDetection();

    @Override
    public int icon() {
        return HeroIcon.RBA_UNHOLY_DETECTION;
    }

    @Override
    protected boolean usableOnItem(Item item) {
        return (item instanceof EquipableItem || item instanceof Wand) && !item.isIdentified() && !item.cursedKnown;
    }

    @Override
    public float chargeUse(Hero hero) {
        return 7-hero.pointsInTalent(Talent.RBA_MARI_T1_2);
    }

    @Override
    public boolean canCast(Hero hero) {
        return super.canCast(hero) && hero.hasTalent(Talent.RBA_MARI_T1_2);
    }

    @Override
    protected void onItemSelected(RBACrossNecklace cross, Hero hero, Item item) {
        if (item == null){
            return;
        }

        item.cursedKnown = true;

        if (item.cursed){
            RBAHero.yellW(hero, "cursed");
        } else {
            RBAHero.yellI(hero, "uncursed");
        }

        hero.spend( 1f );
        hero.busy();
        hero.sprite.operate(hero.pos);
        hero.sprite.parent.add( new Identification( hero.sprite.center().offset( 0, -16 ) ) );

        Sample.INSTANCE.play( Assets.Sounds.READ );
        onMiracleCast(cross, hero);
    }

}
