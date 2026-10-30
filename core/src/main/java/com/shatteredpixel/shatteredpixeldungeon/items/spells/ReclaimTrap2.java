/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.watabou.utils.Random;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Trapskill;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class ReclaimTrap2 extends TargetedSpell {
	
	{
		image = ItemSpriteSheet.RECLAIM_TRAP;
	}
	
	private Class<?extends Trap> storedTrap = null;

	public boolean isTrapStored(){
		if(storedTrap==null){
			return false;
		}
		return true;
	}

	public void storeTrap(Class<?extends Trap> trapp){
		this.storedTrap = trapp;
	}

	@Override
	public ArrayList<String> actions(Hero hero ) {
		ArrayList<String> actions = new ArrayList<>();
		actions.add( AC_CAST );
		return actions;
	}

	@Override
	protected void affectTarget(Ballistica bolt, Hero hero) {
		if (storedTrap == null) {
			quantity++; //storing a trap doesn't consume the spell
			Trap t = Dungeon.level.traps.get(bolt.collisionPos);
			if (t != null && t.active && t.visible) {
				t.disarm();
				if(com.shatteredpixel.shatteredpixeldungeon.TrapDungeon.getTrapSkill()>0){
					com.shatteredpixel.shatteredpixeldungeon.TrapDungeon.setCurrentTrapSkill2(com.shatteredpixel.shatteredpixeldungeon.TrapDungeon.getTrapSkill());
					//Buff.prolong( hero, Trapskill.class, 999999f );
				}
				Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
				ScrollOfRecharging.charge(hero);
				storedTrap = t.getClass();
				
				

				Belongings b = hero.belongings;
				if (b.artifact instanceof AlchemistsToolkit){
					((AlchemistsToolkit)b.artifact).charge(hero, 100);
				}
				if (b.misc instanceof AlchemistsToolkit){
					((AlchemistsToolkit)b.misc).charge(hero, 100);
				}
			} else {
				GLog.w(Messages.get(this, "no_trap"));
			}
		} else {
			
			try {
				Trap t = storedTrap.newInstance();

				//DemonBeacon beacons = Dungeon.hero.belongings.getItem( DemonBeacon.class );
				boolean consume = true;
				if (com.shatteredpixel.shatteredpixeldungeon.TrapDungeon.getCurrentTrapSkill2() != 0 ){
					//if(Random.Int(1+beacons.quantity())!=0){
					//	GLog.p( "Crystal's energy conserves the spell!" );
					//	CellEmitter.get(Dungeon.hero.pos).start(Speck.factory(Speck.STAR), 0.02f, 14);
					com.shatteredpixel.shatteredpixeldungeon.TrapDungeon.setCurrentTrapSkill2(com.shatteredpixel.shatteredpixeldungeon.TrapDungeon.getCurrentTrapSkill2()-1);
					//Buff.prolong( hero, Trapskill.class, 99999f );
					if (com.shatteredpixel.shatteredpixeldungeon.TrapDungeon.getCurrentTrapSkill2()==0){
						Buff.detach( hero, Trapskill.class);
					}
						consume = false;
					//}
				}
				if(consume){
					storedTrap = null;
				}else{
					quantity++;
				}
				
				t.pos = bolt.collisionPos;
				t.activate();
				
			} catch (Exception e) {
				ShatteredPixelDungeon.reportException(e);
			}
		}
	}
	
	@Override
	public String desc() {
		String desc = super.desc();
		if (storedTrap != null){
			desc += "\n\n" + Messages.get(this, "desc_trap", Messages.get(storedTrap, "name"));
		}
		return desc;
	}
	
	@Override
	protected void onThrow(int cell) {
		storedTrap = null;
		super.onThrow(cell);
	}
	
	@Override
	public void doDrop(Hero hero) {
		storedTrap = null;
		super.doDrop(hero);
	}
	
	private static final ItemSprite.Glowing[] COLORS = new ItemSprite.Glowing[]{
			new ItemSprite.Glowing( 0xFF0000 ),
			new ItemSprite.Glowing( 0xFF8000 ),
			new ItemSprite.Glowing( 0xFFFF00 ),
			new ItemSprite.Glowing( 0x00FF00 ),
			new ItemSprite.Glowing( 0x00FFFF ),
			new ItemSprite.Glowing( 0x8000FF ),
			new ItemSprite.Glowing( 0xC0E1FF ),
			new ItemSprite.Glowing( 0x808080 ),
			new ItemSprite.Glowing( 0x000000 )
	};

	private static final int[] COLORS2 = new int[]{
			0xFF0000,
			0xFF8000,
			0xFFFF00 ,
			0x00FF00 ,
			0x00FFFF ,
			0x8000FF ,
			0xC0E1FF ,
			 0x808080,
			0x000000
	};
	
	@Override
	public ItemSprite.Glowing glowing() {
		if (storedTrap != null){
			try {
				return COLORS[storedTrap.newInstance().color];
			} catch (Exception e) {
				ShatteredPixelDungeon.reportException(e);
			}
		}
		return null;
	}

	public int storedColor(){
		try{
			return COLORS2[storedTrap.newInstance().color];
		}catch(Exception e){
			//lol
		}
		return 0;
	}

	@Override
	public int value() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((40 + 100) / 6f));
	}
	
	private static final String STORED_TRAP = "stored_trap";
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(STORED_TRAP, storedTrap);
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		storedTrap = bundle.getClass(STORED_TRAP);
	}
	
	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ScrollOfMagicMapping.class, MetalShard.class};
			inQuantity = new int[]{1, 1};
			
			cost = 6;
			
			output = ReclaimTrap2.class;
			outQuantity = 3;
		}
		
	}
	
}
