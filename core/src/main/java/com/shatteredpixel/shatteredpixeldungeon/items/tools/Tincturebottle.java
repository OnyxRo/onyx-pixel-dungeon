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

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.herbs.BlandfruitHerb;
import com.shatteredpixel.shatteredpixeldungeon.plants.herbs.BlindweedHerb;
import com.shatteredpixel.shatteredpixeldungeon.plants.herbs.DewcatcherHerb;
import com.shatteredpixel.shatteredpixeldungeon.plants.herbs.DreamfoilHerb;
import com.shatteredpixel.shatteredpixeldungeon.plants.herbs.EarthrootHerb;
import com.shatteredpixel.shatteredpixeldungeon.plants.herbs.FadeleafHerb;
import com.shatteredpixel.shatteredpixeldungeon.plants.herbs.FirebloomHerb;
import com.shatteredpixel.shatteredpixeldungeon.plants.herbs.IcecapHerb;
import com.shatteredpixel.shatteredpixeldungeon.plants.herbs.RotberryHerb;
import com.shatteredpixel.shatteredpixeldungeon.plants.herbs.SeedpodHerb;
import com.shatteredpixel.shatteredpixeldungeon.plants.herbs.SorrowmossHerb;
import com.shatteredpixel.shatteredpixeldungeon.plants.herbs.StarflowerHerb;
import com.shatteredpixel.shatteredpixeldungeon.plants.herbs.SteamweedHerb;
import com.shatteredpixel.shatteredpixeldungeon.plants.herbs.StormvineHerb;
import com.shatteredpixel.shatteredpixeldungeon.plants.herbs.SungrassHerb;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.BlandfruitItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.BlindweedItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.DewcatcherItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.DreamfoilItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.EarthrootItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.FadeleafItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.FirebloomItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.IcecapItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.PlantItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.RotberryItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.SeedpodItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.SorrowmossItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.StarflowerItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.SteamWeedItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.StormvineItem;
import com.shatteredpixel.shatteredpixeldungeon.plants.itemplants.SungrassItem;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;

import java.util.ArrayList;

public class Tincturebottle extends Item {

	public static final String AC_BREW = "BREW";

	{
		image = ItemSpriteSheet.TINCTUREBOTTLE;
		defaultAction = AC_BREW;
		stackable = false;
	}

	public PlantItem plant;
	public Plant.Seed seed;
	
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
		actions.add( AC_BREW );
		return actions;
	}

	@Override
	public void execute( final Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_BREW )) {
			GameScene.selectItem( itemSelectorPlant );
		}
	}

	public void brew(){
		if(plant instanceof FirebloomItem){
			new FirebloomHerb().setSeed(seed).collect();
		}
		if(plant instanceof FadeleafItem){
			new FadeleafHerb().setSeed(seed).collect();
		}
		if(plant instanceof EarthrootItem){
			new EarthrootHerb().setSeed(seed).collect();
		}
		if(plant instanceof DreamfoilItem){
			new DreamfoilHerb().setSeed(seed).collect();
		}
		if(plant instanceof DewcatcherItem){
			new DewcatcherHerb().setSeed(seed).collect();
		}
		if(plant instanceof BlindweedItem){
			new BlindweedHerb().setSeed(seed).collect();
		}
		if(plant instanceof BlandfruitItem){
			new BlandfruitHerb().setSeed(seed).collect();
		}
		if(plant instanceof IcecapItem){
			new IcecapHerb().setSeed(seed).collect();
		}
		if(plant instanceof RotberryItem){
			new RotberryHerb().setSeed(seed).collect();
		}
		if(plant instanceof SeedpodItem){
			new SeedpodHerb().setSeed(seed).collect();
		}
		if(plant instanceof SorrowmossItem){
			new SorrowmossHerb().setSeed(seed).collect();
		}
		if(plant instanceof StarflowerItem){
			new StarflowerHerb().setSeed(seed).collect();
		}
		if(plant instanceof StormvineItem){
			new StormvineHerb().setSeed(seed).collect();
		}
		if(plant instanceof SungrassItem){
			new SungrassHerb().setSeed(seed).collect();
		}
		if(plant instanceof SteamWeedItem){
			new SteamweedHerb().setSeed(seed).collect();
		}
	}

	private final WndBag.ItemSelector itemSelectorPlant = new WndBag.ItemSelector() {
		@Override
		public String textPrompt() {
			return Messages.get(Tincturebottle.class, "prompt_plant");
		}

		@Override
		public boolean itemSelectable( Item item ) {
			return item instanceof PlantItem;
		}

		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				plant = (PlantItem) item;
				GameScene.selectItem( itemSelectorSeed );
			}
		}
	};

	private final WndBag.ItemSelector itemSelectorSeed = new WndBag.ItemSelector() {
		@Override
		public String textPrompt() {
			return Messages.get(Tincturebottle.class, "prompt_seed");
		}

		@Override
		public boolean itemSelectable( Item item ) {
			return item instanceof Plant.Seed;
		}

		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				seed = (Plant.Seed) item;
				item.detach(curUser.belongings.backpack);
				plant.detach(curUser.belongings.backpack);
				brew();
			}
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
