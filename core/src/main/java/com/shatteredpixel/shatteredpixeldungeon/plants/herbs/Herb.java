/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
 *
 * Lovecraft Pixel Dungeon
 * Copyright (C) 2016-2017 Leon Horn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This Program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without eben the implied warranty of
 * GNU General Public License for more details.
 *
 * You should have have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses>
 */

package com.shatteredpixel.shatteredpixeldungeon.plants.herbs;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EarthImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ToxicImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlandFruitBushPoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlindweedPoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.DreamfoilPoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EarthrootPoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FadeleafPoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FirebloomPoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.IceCapPoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RotBerryPoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SorrowMossPoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.StarFlowerPoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SteamweedPoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.StormVinePoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SunGrassPoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.plants.BlandfruitBush;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Dreamfoil;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.plants.Steamweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.utils.RandomL;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.HashSet;

public class Herb extends Item {

    public static final String AC_EAT = "EAT";
    public float energy = Hunger.HUNGRY;

    public Plant.Seed seed;

    HashSet<Plant.Seed> seedArrayList = new HashSet<>();

    {
        stackable = false;
        //a herb always carries some seed, which sets the potion effect it gives when eaten
        seed = (Plant.Seed) Generator.random(Generator.Category.SEED);
    }

    public Herb setSeed(Plant.Seed seed){
        this.seed = seed;
        return this;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions(hero);
        actions.add( AC_EAT );
        return actions;
    }

    private static final String SEED		= "seed";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        seedArrayList.add(seed);
        bundle.put(SEED, seedArrayList);
        seedArrayList.clear();
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        for (Bundlable b : bundle.getCollection( SEED )) {
            if (b != null) {
                setSeed((Plant.Seed) b);
            }
        }
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action.equals( AC_EAT )) {
            hero.buff( Hunger.class ).satisfy( energy );
            this.detach(hero.belongings.backpack);
            if(RandomL.randomBoolean()){
                hero.increaseMentalHealth(2);
                hero.loseKnowl(2);
            } else {
                hero.reduceMentalHealth(2);
                hero.gainKnowl(2);
            }
            eatEffect(hero);
            try {
                Potion potion = (Potion) com.watabou.utils.Reflection.newInstance( Potion.SeedToPotion.types.get( seed.getClass() ) );
                if(potion instanceof PotionOfLiquidFlame){
                    Buff.affect(hero, FireImbue.class).set(FireImbue.DURATION);
                    new PotionOfLiquidFlame().shatter(hero.pos);
                } else
                if(potion instanceof PotionOfToxicGas){
                    Buff.affect(hero, ToxicImbue.class).set(ToxicImbue.DURATION);
                    new PotionOfToxicGas().shatter(hero.pos);
                } else
                if(potion instanceof PotionOfParalyticGas){
                    Buff.affect(hero, EarthImbue.class, EarthImbue.DURATION);
                    new PotionOfParalyticGas().shatter(hero.pos);
                } else {
                    potion.apply(hero);
                }
            } catch (Exception e) {
                ShatteredPixelDungeon.reportException(e);
            }
        }
    }

    public void eatEffect(Hero hero){

    }

    @Override
    public boolean collect( com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag container ) {
        if(seed == null){
            seed = (Plant.Seed) Generator.random(Generator.Category.SEED);
        }
        return super.collect( container );
    }

    @Override
    public Emitter emitter() {
        if(seed == null) return null;
        Emitter emitter = new Emitter();
        emitter.pos(12.5f, 3);
        emitter.fillTarget = false;
        if(seed instanceof Blindweed.Seed){
            emitter.pour(BlindweedPoisonParticle.FACTORY, 1f);
        } else if(seed instanceof Dreamfoil.Seed){
            emitter.pour(DreamfoilPoisonParticle.FACTORY, 1f);
        } else if(seed instanceof Earthroot.Seed){
            emitter.pour(EarthrootPoisonParticle.FACTORY, 1f);
        } else if(seed instanceof Fadeleaf.Seed){
            emitter.pour(FadeleafPoisonParticle.FACTORY, 1f);
        } else if(seed instanceof Firebloom.Seed){
            emitter.pour(FirebloomPoisonParticle.FACTORY, 1f);
        } else if(seed instanceof Icecap.Seed){
            emitter.pour(IceCapPoisonParticle.FACTORY, 1f);
        } else if(seed instanceof Rotberry.Seed){
            emitter.pour(RotBerryPoisonParticle.FACTORY, 1f);
        } else if(seed instanceof Sorrowmoss.Seed){
            emitter.pour(SorrowMossPoisonParticle.FACTORY, 1f);
        } else if(seed instanceof Starflower.Seed){
            emitter.pour(StarFlowerPoisonParticle.FACTORY, 1f);
        } else if(seed instanceof Stormvine.Seed){
            emitter.pour(StormVinePoisonParticle.FACTORY, 1f);
        } else if(seed instanceof Sungrass.Seed){
            emitter.pour(SunGrassPoisonParticle.FACTORY, 1f);
        } else if(seed instanceof BlandfruitBush.Seed){
            emitter.pour(BlandFruitBushPoisonParticle.FACTORY, 1f);
        } else if(seed instanceof Steamweed.Seed){
            emitter.pour(SteamweedPoisonParticle.FACTORY, 1f);
        }
        return emitter;
    }

    @Override
    public int value() {
        return 5 * quantity;
    }
}
