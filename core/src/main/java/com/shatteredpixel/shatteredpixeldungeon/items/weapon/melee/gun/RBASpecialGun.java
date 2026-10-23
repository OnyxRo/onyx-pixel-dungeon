package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun;

import com.shatteredpixel.shatteredpixeldungeon.items.RBAGunSmithingTool;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBATacticalTherapy;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.RBAUniqueIdea;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBAFunnyFirework;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.RBASpringPunch;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAChistka;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAPiety;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBAMulligan;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.RBATrickOrTrick;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MT.RBAFancyLight;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBAMTET;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.RBAShootingStar;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBABeyondTheLumination;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.RBATwinDragon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBAJusticeIncarnate;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBAWineRedAdmire;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickRecipe;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

public interface RBASpecialGun {
    abstract class BaseRecipe extends Recipe {
        public abstract Class<? extends RBAGun> ingredients();

        public abstract Class<? extends RBAGun> result();

        public boolean isIngredient(Item item) {
            return ingredients().isInstance(item) && !(item instanceof RBASpecialGun);
        }

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            boolean tool = false;
            boolean gun = false;

            for (Item i : ingredients){
                if (!i.isIdentified()) return false;
                if (i.getClass().equals(RBAGunSmithingTool.class)){
                    tool = true;
                } else if (isIngredient(i)){
                    gun = true;
                }
            }

            return tool && gun;
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 8;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            Item result = null;

            for (Item i : ingredients){
                i.quantity(i.quantity()-1);
                if (isIngredient(i)){
                    result = brewGun(i);
                }
            }

            if (result != null) {
                result.identify();
            }

            return result;
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            for (Item i : ingredients){
                if (isIngredient(i)){
                    return brewGun(i);
                }
            }
            return null;
        }

        public RBAGun brewGun(Item item) {
            if (!(item instanceof RBAGun)) return null;

            RBAGun n = Reflection.newInstance(result());
            if (n == null) return null;

            RBAGun g = (RBAGun)item;
            int level = Math.min((g.checkKit() != null && g.checkKit().level() > 0) ? 3:2, g.level());
            n.identify().upgrade(level);
            if (g.checkKit() != null) {
                //만약 키트가 강화되어 있다면 키트에 의해 추가로 증가한 강화수치를 1 감소시킴
                if (g.checkKit().level() > 0) {
                    n.degrade(g.checkKit().level());
                }
                n.affixKit(g.checkKit());
            }

            n.enchantment = g.enchantment;
            n.barrelMod = g.barrelMod;
            n.magazineMod = g.magazineMod;
            n.bulletMod = g.bulletMod;
            n.weightMod = g.weightMod;
            n.attachMod = g.attachMod;
            n.enchantMod = g.enchantMod;
            n.inscribeMod = g.inscribeMod;

            return n;
        }
    }

    Recipe[] gunRecipes = new Recipe[]{
        new RBAUniqueIdea.Recipe(),
        new RBATacticalTherapy.Recipe(),
        new RBAFunnyFirework.Recipe(),
        new RBAPiety.Recipe(),
        new RBAChistka.Recipe(),
        new RBAMulligan.Recipe(),
        new RBATrickOrTrick.Recipe(),
        new RBASpringPunch.Recipe(),
        new RBAShootingStar.Recipe(),
        new RBAMTET.Recipe(),
        new RBATwinDragon.Recipe(),
        new RBABeyondTheLumination.Recipe(),
        new RBAJusticeIncarnate.Recipe(),
        new RBAWineRedAdmire.Recipe(),
        new RBAFancyLight.Recipe()
    };

    static ArrayList<QuickRecipe> quickRecipes() {
        ArrayList<QuickRecipe> result = new ArrayList<>();
        for (Recipe recipe : gunRecipes){
            result.add(new QuickRecipe( recipe,
                    new ArrayList<Item>(Arrays.asList(Reflection.newInstance(((BaseRecipe)recipe).ingredients()).getPlaceHolder(), new RBAGunSmithingTool())),
                    Reflection.newInstance(((BaseRecipe)recipe).result())));
        }
        return result;
    }
}
