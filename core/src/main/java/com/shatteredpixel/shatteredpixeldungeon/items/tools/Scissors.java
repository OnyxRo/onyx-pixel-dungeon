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

package com.shatteredpixel.shatteredpixeldungeon.items.tools;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
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
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.BlandfruitItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.BlindweedItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.DewcatcherItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.DreamfoilItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.EarthrootItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.FadeleafItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.FirebloomItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.IcecapItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.RotberryItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.SeedpodItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.SorrowmossItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.StarflowerItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.SteamWeedItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.StormvineItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.SungrassItem;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class Scissors extends Item {

	public static final String AC_CUT = "CUT";

	{
		image = ItemSpriteSheet.SCISSORS;
		defaultAction = AC_CUT;
		stackable = false;
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
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		actions.add( AC_CUT );
		return actions;
	}

	@Override
	public void execute( final Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_CUT )) {
			GameScene.selectCell(informer);
		}
	}

	public static Boolean getPlantOnCell(Integer cell){
		for(Plant plant : Dungeon.level.plants.values()){
			if(plant.pos == cell){
				return true;
			}
		}
		return false;
	}

	private static CellSelector.Listener informer = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer cell ) {
			if(cell != null){
				boolean isinrange = false;
				for (int i : PathFinder.NEIGHBOURS9){
					if(Dungeon.hero.pos+i == cell)
						isinrange = true;
				}
				if(isinrange){
					if(getPlantOnCell(cell) &&
							!Dungeon.level.solid[cell]){
						Plant plant = Dungeon.level.plants.get(cell);
						Dungeon.level.uproot(cell);
						CellEmitter.get( cell ).burst(LeafParticle.GENERAL, 4);
						if(plant instanceof BlandfruitBush){
							Dungeon.level.drop(new BlandfruitItem(), cell);
						} else if(plant instanceof Blindweed){
							Dungeon.level.drop(new BlindweedItem(), cell);
						} else if(plant instanceof WandOfRegrowth.Dewcatcher){
							Dungeon.level.drop(new DewcatcherItem(), cell);
						} else if(plant instanceof Dreamfoil){
							Dungeon.level.drop(new DreamfoilItem(), cell);
						} else if(plant instanceof Earthroot){
							Dungeon.level.drop(new EarthrootItem(), cell);
						} else if(plant instanceof Fadeleaf){
							Dungeon.level.drop(new FadeleafItem(), cell);
						} else if(plant instanceof Firebloom){
							Dungeon.level.drop(new FirebloomItem(), cell);
						} else if(plant instanceof Icecap){
							Dungeon.level.drop(new IcecapItem(), cell);
						} else if(plant instanceof Rotberry){
							Dungeon.level.drop(new RotberryItem(), cell);
						} else if(plant instanceof WandOfRegrowth.Seedpod){
							Dungeon.level.drop(new SeedpodItem(), cell);
						} else if(plant instanceof Sorrowmoss){
							Dungeon.level.drop(new SorrowmossItem(), cell);
						} else if(plant instanceof Starflower){
							Dungeon.level.drop(new StarflowerItem(), cell);
						} else if(plant instanceof Stormvine){
							Dungeon.level.drop(new StormvineItem(), cell);
						} else if(plant instanceof Sungrass){
							Dungeon.level.drop(new SungrassItem(), cell);
						} else if(plant instanceof Steamweed){
							Dungeon.level.drop(new SteamWeedItem(), cell);
						}
					} else if(Actor.findChar(cell) != null){
						Buff.affect(Actor.findChar(cell), Bleeding.class).set(1);
						CellEmitter.get( cell ).burst(BloodParticle.BURST, 4);
						curUser.reduceMentalHealth(1);
					} else {
						GLog.i(Messages.get(Scissors.class, "nothing"));
					}
				} else {
					GLog.i(Messages.get(Scissors.class, "standnext"));
				}
			}
		}

		@Override
		public String prompt() {
			return Messages.get(Scissors.class, "plant_cut");
		}
	};
	
	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
	
	@Override
	public int value() {
		return 0;
	}
}
