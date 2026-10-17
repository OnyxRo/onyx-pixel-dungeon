/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Pixel Towers / Towers Pixel Dungeon
 * Copyright (C) 2024-2025 FixAkaTheFix (initials R. A. A.)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Berry;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.AlchemicalCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfHoneyedHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShielding;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CeremonialCandle;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorruptedOoze;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Vilebloom;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.AquaBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ArcaneCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.CurseInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Recycle;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.SummonElemental;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.TelekineticGrab;
import com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerTotemAttack;
import com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerTotemHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerTotemNecrotic;
import com.shatteredpixel.shatteredpixeldungeon.items.towerspawners.SpawnerTotemShield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena7;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.towerlist.TowerInfo;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndModes;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTradeItem;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class NormalShopKeeper extends NewShopKeeper {

    {
        spriteClass = ShopkeeperSprite.class;

        properties.add(Property.IMMOVABLE);
    }



    @Override
    public ArrayList<Item> generateItems() {

        ArrayList<Item> itemsToSpawn = new ArrayList<>();
        if(Dungeon.level instanceof Arena7 && Dungeon.level.mode == WndModes.Modes.CHALLENGE){
            itemsToSpawn.add(Random.oneOf(Generator.randomUsingDefaults(Generator.Category.SCROLL)));
            itemsToSpawn.add(Random.oneOf(Generator.randomUsingDefaults(Generator.Category.SCROLL)));
            itemsToSpawn.add(Random.oneOf(Generator.randomUsingDefaults(Generator.Category.SCROLL)));
            itemsToSpawn.add(Random.oneOf(Generator.randomUsingDefaults(Generator.Category.SCROLL)));
            itemsToSpawn.add(Random.oneOf(Generator.randomUsingDefaults(Generator.Category.SCROLL)));
        } else if (Dungeon.depth < 16) {
            itemsToSpawn.add(eliteShopItem());
            itemsToSpawn.add(normalShopItem());
            itemsToSpawn.add(normalShopItem());
            itemsToSpawn.add(normalShopItem());
            itemsToSpawn.add(normalShopItem());
        } else {
            itemsToSpawn.add(eliteShopItem());
            itemsToSpawn.add(normalSecondTierShopItem());
            itemsToSpawn.add(normalSecondTierShopItem());
            itemsToSpawn.add(normalSecondTierShopItem());
            itemsToSpawn.add(normalSecondTierShopItem());
        }
        return itemsToSpawn;
    }

    private Item normalShopItem(){
        return Random.oneOf(
                Generator.randomUsingDefaults(Generator.Category.POTION),
                Generator.randomUsingDefaults(Generator.Category.SEED),
                Generator.randomUsingDefaults(Generator.Category.FOOD),
                Generator.randomUsingDefaults(Generator.Category.SCROLL),
                Generator.randomUsingDefaults(Generator.Category.RING).upgrade(Random.NormalIntRange(0,1 + Dungeon.depth/5)),
                Generator.randomUsingDefaults(Generator.Category.STONE),
                Generator.randomUsingDefaults(Generator.Category.WAND).upgrade(Random.NormalIntRange(0,1 + Dungeon.depth/10)),
                new com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart().quantity(8),
                Generator.randomUsingDefaults(Generator.Category.MISSILE),
                new com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb(),
                Random.oneOf(
                        //random garbage
                        new MetalShard(), new GooBlob(),//these 2 are useless
                        new CorpseDust(), new CeremonialCandle(),//these 2 give achievements on being picked up
                        new Vilebloom(), new CorruptedOoze(),//these 2 may give a glass cannon effect
                        //non-garbage random item
                        new Stylus(), new ArcaneResin(),
                        new AlchemicalCatalyst(), new ArcaneCatalyst(), new Honeypot(),
                        new LiquidMetal(), new LiquidMetal(), new LiquidMetal(), new LiquidMetal()),//higher chance for tower-healing liquid metal
                Generator.randomUsingDefaults(Generator.Category.ARMOR).upgrade(Random.NormalIntRange(0,1 + Dungeon.depth/5)),
                Generator.randomUsingDefaults(Generator.Category.WEAPON).upgrade(Random.NormalIntRange(0,1 + Dungeon.depth/5))
        );
    }
    public static Item normalSecondTierShopItem(){
        return Random.oneOf(
                Generator.randomUsingDefaults(Generator.Category.POTION),
                Generator.randomUsingDefaults(Generator.Category.SEED),
                Generator.randomUsingDefaults(Generator.Category.FOOD),
                Generator.randomUsingDefaults(Generator.Category.SCROLL),
                Generator.randomUsingDefaults(Generator.Category.RING).upgrade(Random.NormalIntRange(0,1 + Dungeon.depth/5)),
                Reflection.newInstance(ExoticPotion.regToExo.get(Generator.randomUsingDefaults(Generator.Category.POTION).getClass())),
                Generator.randomUsingDefaults(Generator.Category.STONE),
                Generator.randomUsingDefaults(Generator.Category.WAND).upgrade(Random.NormalIntRange(0,1 + Dungeon.depth/10)),
                new com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart().quantity(8),
                Generator.randomUsingDefaults(Generator.Category.MISSILE),
                new com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb(),
                new LiquidMetal(),
                Random.oneOf(
                        //random garbage
                        new Dart(), new Torch(),//these 2 are changed from blob & shard to be more.. functional
                        new CorpseDust(), new CeremonialCandle(),
                        new Vilebloom(), new CorruptedOoze(),

                        //non-garbage random item
                        new Stylus(), new ArcaneResin(),
                        new AlchemicalCatalyst(), new ArcaneCatalyst(), new Honeypot(),
                        new LiquidMetal(), new LiquidMetal(), new LiquidMetal(), new LiquidMetal(), new LiquidMetal()),//higher (30%) chance for tower-healing liquid metal

                Random.oneOf(new TelekineticGrab(), new SummonElemental(), new AquaBlast(), new CurseInfusion(), new Recycle(),
                        new SpawnerTotemAttack(), new SpawnerTotemNecrotic(), new SpawnerTotemHealing(), new SpawnerTotemShield()),
                Generator.randomUsingDefaults(Generator.Category.ARMOR).upgrade(Random.NormalIntRange(0,1 + Dungeon.depth/5)),
                Generator.randomUsingDefaults(Generator.Category.WEAPON).upgrade(Random.NormalIntRange(0,1 + Dungeon.depth/5))
        );
    }
    public static Item eliteShopItem(){
        return Random.oneOf(
                new ElixirOfHoneyedHealing(),
                new PotionOfShielding(),
                new PotionOfStrength(),
                new MeatPie(),
                new ScrollOfUpgrade(),
                new ScrollOfEnchantment(),
                new Ankh(),
                TowerInfo.getHeroAbility(Dungeon.level.slot1),
                TowerInfo.getHeroAbility(Dungeon.level.slot2),
                TowerInfo.getHeroAbility(Dungeon.level.slot3),
                TowerInfo.getHeroAbility(Dungeon.level.slot4),
                Generator.randomUsingDefaults(Generator.Category.ARTIFACT)

        );

    }


    public static WndBag sell() {
        return GameScene.selectItem( itemSelector );
    }

    public static boolean canSell(Item item){
        if (item.value() <= 0)                                              return false;
        if (item.unique && !item.stackable)                                 return false;
        if (item instanceof Armor && ((Armor) item).checkSeal() != null)    return false;
        if (item.isEquipped(Dungeon.hero) && item.cursed)                   return false;
        return true;
    }
    public static WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(Shopkeeper.class, "sell");
        }

        @Override
        public boolean itemSelectable(Item item) {
            return canSell(item);
        }

        @Override
        public void onSelect( Item item ) {
            if (item != null) {
                WndBag parentWnd = sell();
                GameScene.show( new WndTradeItem( item, parentWnd ) );
            }
        }
    };

    @Override
    public boolean interact(Char c) {
        if (c != Dungeon.hero) {
            return true;
        }
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                sell();
            }
        });
        return true;
    }

}