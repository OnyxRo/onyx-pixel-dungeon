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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EarthParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Camera;

public class EarthrootHerb extends Herb {

    {
        image = ItemSpriteSheet.EARTHROOT_HERB;
    }

    public Herb setSeed(Plant.Seed seed){
        this.seed = seed;
        return this;
    }

    @Override
    public void eatEffect(Hero hero) {
       effectChar(hero);
    }

    private void effectChar(Char ch){

        Buff.affect( ch, Earthroot.Armor.class ).level(ch.HT*2);

        if (Dungeon.level.heroFOV[ch.pos]) {
            CellEmitter.bottom( ch.pos ).start( EarthParticle.FACTORY, 0.05f, 4 );
            Camera.main.shake( 0.5f, 0.2f );
        }
    }

    @Override
    public String name() {
        return Messages.get(Earthroot.class, "name")+ " " + Messages.get(Herb.class, "herb_name");
    }

    @Override
    public String desc() {
        if (seed == null || com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion.SeedToPotion.types.get(seed.getClass()) == null) return Messages.get(this, "desc");
        return Messages.get(this, "desc") + "\n\n" + Messages.get(Herb.class, "seed_used", this.seed.name()) + "\n" + Messages.get(com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion.SeedToPotion.types.get(this.seed.getClass()), "herbdesc");
    }
}
