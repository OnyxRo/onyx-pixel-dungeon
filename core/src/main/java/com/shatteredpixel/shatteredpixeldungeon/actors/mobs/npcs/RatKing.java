/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.Holiday;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class RatKing extends NPC {

	{
		spriteClass = RatKingSprite.class;
		
		state = SLEEPING;
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return INFINITE_EVASION;
	}
	
	@Override
	public float speed() {
		return 2f;
	}
	
	@Override
	protected Char chooseEnemy() {
		return null;
	}

	@Override
	public void damage( int dmg, Object src ) {
		//do nothing
	}

	@Override
	public boolean add( Buff buff ) {
		if (buff instanceof Barter) return super.add( buff );
		return false;
	}
	
	@Override
	public boolean reset() {
		return true;
	}

	//***This functionality is for when rat king may be summoned by a distortion trap

	@Override
	protected void onAdd() {
		super.onAdd();
		if (firstAdded && Dungeon.depth != 5){
			yell(Messages.get(this, "confused"));
		}
	}

	@Override
	public Notes.Landmark landmark() {
		return Dungeon.depth == 5 ? Notes.Landmark.RAT_KING : null;
	}

	@Override
	protected boolean act() {
		if (Dungeon.depth < 5){
			if (pos == Dungeon.level.exit()){
				destroy();
				sprite.killAndErase();
			} else {
				target = Dungeon.level.exit();
			}
		} else if (Dungeon.depth > 5){
			if (pos == Dungeon.level.entrance()){
				destroy();
				sprite.killAndErase();
			} else {
				target = Dungeon.level.entrance();
			}
		}
		if (Dungeon.cyclesOn() || Dungeon.hero.perks.contains(com.shatteredpixel.shatteredpixeldungeon.actors.hero.Perks.Perk.BETTER_BARTERING)) {
			barterAct();
		}
		return super.act();
	}

	//Experienced PD's Rat King barter. Items thrown onto the Rat King are traded for random items thrown
	//back, more of them the further he has been fed. Only open in an Endless Cycles run or with the
	//Efficient Bartering perk, so a normal run keeps the quiet Rat King.
	public int counter = 0;
	public int level = 1;

	private void barterAct(){
		java.util.ArrayList<com.shatteredpixel.shatteredpixeldungeon.items.Item> unused = null;
		com.shatteredpixel.shatteredpixeldungeon.items.Heap heap = Dungeon.level.heaps.get( pos );
		Barter barter = Buff.affect( this, Barter.class );
		if (heap != null && !(heap.peek() instanceof com.shatteredpixel.shatteredpixeldungeon.items.Amulet)){
			com.shatteredpixel.shatteredpixeldungeon.items.Item item = heap.pickUp();
			barter.stick( item );
			com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter.get( pos ).burst( com.shatteredpixel.shatteredpixeldungeon.effects.Speck.factory( com.shatteredpixel.shatteredpixeldungeon.effects.Speck.WOOL ), 6 );
			com.watabou.noosa.audio.Sample.INSTANCE.play( com.shatteredpixel.shatteredpixeldungeon.Assets.Sounds.PUFF );
		}
		if (!barter.items.isEmpty()){
			if (!Dungeon.hero.perks.contains(com.shatteredpixel.shatteredpixeldungeon.actors.hero.Perks.Perk.BETTER_BARTERING) || com.watabou.utils.Random.Int(3) != 0) {
				barter.items.remove( barter.items.size() - 1 );
			}
			for (int i = 0; i < level * (Dungeon.cycle + 1); i++) {
				com.shatteredpixel.shatteredpixeldungeon.items.Item item;
				do {
					item = com.shatteredpixel.shatteredpixeldungeon.items.Generator.random();
				} while (item instanceof com.shatteredpixel.shatteredpixeldungeon.items.Gold);
				if (++counter >= 30 * level) {
					counter = 0;
					level = Math.min( level + 1, 15 );
					item = new com.shatteredpixel.shatteredpixeldungeon.items.food.Cheese();
				}
				//the Rat King tosses the trade to the hero's feet
				Dungeon.level.drop( item, Dungeon.hero.pos ).sprite.drop( pos );
			}
			spend( 2f );
		}
	}

	public static class Barter extends Buff {

		public java.util.ArrayList<com.shatteredpixel.shatteredpixeldungeon.items.Item> items = new java.util.ArrayList<>();

		public void stick( com.shatteredpixel.shatteredpixeldungeon.items.Item heh ){
			for (com.shatteredpixel.shatteredpixeldungeon.items.Item item : items){
				if (item.isSimilar( heh )){
					item.merge( heh );
					return;
				}
			}
			items.add( heh );
		}

		@Override
		public void detach() {
			for (com.shatteredpixel.shatteredpixeldungeon.items.Item item : items) {
				Dungeon.level.drop( item, target.pos ).sprite.drop();
			}
			super.detach();
		}

		@Override
		public void storeInBundle( com.watabou.utils.Bundle bundle ) {
			bundle.put( "items", items );
			super.storeInBundle( bundle );
		}

		@Override
		public void restoreFromBundle( com.watabou.utils.Bundle bundle ) {
			items = new java.util.ArrayList<>();
			for (com.watabou.utils.Bundlable b : bundle.getCollection( "items" )) {
				items.add( (com.shatteredpixel.shatteredpixeldungeon.items.Item)b );
			}
			super.restoreFromBundle( bundle );
		}
	}

	//***

	@Override
	public boolean interact(Char c) {
		sprite.turnTo( pos, c.pos );

		if (c != Dungeon.hero){
			return super.interact(c);
		}

		KingsCrown crown = Dungeon.hero.belongings.getItem(KingsCrown.class);
		if (state == SLEEPING) {
			notice();
			yell( Messages.get(this, "not_sleeping") );
			state = WANDERING;
			com.shatteredpixel.shatteredpixeldungeon.TrapDungeon.ratKingWoken(); //trap dungeon
		} else if (com.shatteredpixel.shatteredpixeldungeon.TrapDungeon.ratKingRitual(this)){
			//trap dungeon, a rat beacon starts the ritual
		} else if (crown != null){
			if (Dungeon.hero.belongings.armor() == null){
				yell( Messages.get(RatKing.class, "crown_clothes") );
			} else {
				Badges.validateRatmogrify();
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show(new WndOptions(
								sprite(),
								Messages.titleCase(name()),
								Messages.get(RatKing.class, "crown_desc"),
								Messages.get(RatKing.class, "crown_yes"),
								Messages.get(RatKing.class, "crown_info"),
								Messages.get(RatKing.class, "crown_no")
						){
							@Override
							protected void onSelect(int index) {
								if (index == 0){
									crown.upgradeArmor(Dungeon.hero, Dungeon.hero.belongings.armor(), new Ratmogrify());
									Statistics.qualifiedForRandomVictoryBadge = false;
									((RatKingSprite)sprite).resetAnims();
									yell(Messages.get(RatKing.class, "crown_thankyou"));
								} else if (index == 1) {
									GameScene.show(new WndInfoArmorAbility(Dungeon.hero.heroClass, new Ratmogrify()));
								} else {
									yell(Messages.get(RatKing.class, "crown_fine"));
								}
							}
						});
					}
				});
			}
		} else if (Dungeon.hero.armorAbility instanceof Ratmogrify) {
			yell( Messages.get(RatKing.class, "crown_after") );
		} else {
			yell( Messages.get(this, "what_is_it") );
		}
		return true;
	}
	
	@Override
	public String description() {
		if (Dungeon.hero != null && Dungeon.hero.armorAbility instanceof Ratmogrify){
			return Messages.get(this, "desc_crown");
		} else if (Holiday.getCurrentHoliday() == Holiday.APRIL_FOOLS){
			return Messages.get(this, "desc_birthday");
		} else if (Holiday.getCurrentHoliday() == Holiday.WINTER_HOLIDAYS){
			return Messages.get(this, "desc_winter");
		} else {
			return super.description();
		}
	}
}
