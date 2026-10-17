package com.shatteredpixel.shatteredpixeldungeon.items.props;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Magic Ling PD trinket base class (its "props"). Simplified for Onyx's Pixel Dungeon.
 * The mod kept pools of props in its Statistics and gated them behind its DLC conducts,
 * here a prop is always usable and is rolled from a plain list of classes.
 */
public class Prop extends Item {

    {
        levelKnown = true;
        unique = true;
    }

    public int rareness = 0;
    public int kind = 0; // 0 positive, 1 negative, 2 chaotic

    @Override
    public ArrayList<String> actions(Hero hero) {
        return super.actions(hero);
    }

    public String kindRules() {
        switch (kind) {
            case 2:
                return Messages.get(this, "chaos");
            case 1:
                return Messages.get(this, "bad");
            default:
                return Messages.get(this, "good");
        }
    }

    @Override
    public String desc() {
        String c = Messages.get(this, "rareness", rareness + 1, kindRules());
        c += "\n\n" + super.desc();
        return c;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    private static final Class<?>[] POOL = {
            ArmorScalesOfBzmdr.class, BlockingDrug.class, BottleWraith.class, BrokenBone.class, BrokenRing.class,
            CatGirlCosplay.class, CloakFragmentsOfBzmdr.class, DeadOrAlive.class, DeliciousRecipe.class,
            Dirt_KnifeStand.class, DreamSeed.class, EmotionalAggregation.class, HeartOfCrystalFractal.class,
            HellButterfly.class, KnightStabbingSword.class, LuckyGlove.class, Monocular.class, NoteOfBzmdr.class,
            PortableWhetstone.class, RustedGoldCoin.class, StarDust.class, StarSachet.class, TerrorDoll.class,
            TheGriefOfSpeechless.class
    };

    /** A random prop of the wanted kind (0 positive, 1 negative, 2 chaotic), or Trash when none fits. */
    public static Prop randomProp(int wantedKind) {
        ArrayList<Prop> fits = new ArrayList<>();
        for (Class<?> c : POOL) {
            try {
                Prop p = (Prop) c.newInstance();
                if (p.kind == wantedKind) fits.add(p);
            } catch (Exception e) {
                //skip
            }
        }
        return fits.isEmpty() ? new Trash() : Random.element(fits);
    }

    public static Prop randomPropA(boolean noChaotic) {
        return randomProp(!noChaotic && Random.Int(4) == 0 ? 2 : 0);
    }

    public static Prop randomPropB(boolean noChaotic) {
        return randomProp(!noChaotic && Random.Int(4) == 0 ? 2 : 1);
    }
}
