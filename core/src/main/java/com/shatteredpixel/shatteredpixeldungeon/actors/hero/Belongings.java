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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.KindofMisc;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ShardOfOblivion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Iterator;

public class Belongings implements Iterable<Item> {

	private Hero owner;

	public static class Backpack extends Bag {
		{
			image = ItemSpriteSheet.BACKPACK;
		}
		public int capacity(){
			int cap = super.capacity();
			for (Item item : items){
				if (item instanceof Bag){
					cap++;
				}
			}
			if (Dungeon.hero != null && Dungeon.hero.belongings.secondWep != null){
				//secondary weapons still occupy an inv. slot
				cap--;
			}
			return cap;
		}
	}

	public Backpack backpack;
	
	public Belongings( Hero owner ) {
		this.owner = owner;
		
		backpack = new Backpack();
		backpack.owner = owner;
	}

	public KindOfWeapon weapon = null;
	public Armor armor = null;
	public Artifact artifact = null;
	public KindofMisc misc = null;
	public Ring ring = null;
	//Ryuse's PD, helmets have a slot of their own
	public com.shatteredpixel.shatteredpixeldungeon.items.helmets.Helmet helmet = null;

	//used when thrown weapons temporary become the current weapon
	public KindOfWeapon thrownWeapon = null;

	//used to ensure that the duelist always uses the weapon she's using the ability of
	public KindOfWeapon abilityWeapon = null;

	//used by the champion subclass
	public KindOfWeapon secondWep = null;

	//from Transformation PD, called when the second weapon slot changes. Nothing to rebalance here
	public void onSecondaryWeaponChanged() {}

	//*** these accessor methods are so that worn items can be affected by various effects/debuffs
	// we still want to access the raw equipped items in cases where effects should be ignored though,
	// such as when equipping something, showing an interface, or dealing with items from a dead hero

	//normally the primary equipped weapon, but can also be a thrown weapon or an ability's weapon
	public KindOfWeapon attackingWeapon(){
		if (thrownWeapon != null) return thrownWeapon;
		if (abilityWeapon != null) return abilityWeapon;
		return weapon();
	}

	//we cache whether belongings are lost to avoid lots of calls to hero.buff(LostInventory.class)
	private boolean lostInvent;
	public void lostInventory( boolean val ){
		lostInvent = val;
	}

	public boolean lostInventory(){
		return lostInvent;
	}

	//a random equipped item, or null with nothing equipped. Used by thieves from Matt Edition
	public Item randomEquipped() {
		ArrayList<Item> equipped = new ArrayList<>();
		if (weapon() != null)    equipped.add(weapon());
		if (armor() != null)     equipped.add(armor());
		if (artifact() != null)  equipped.add(artifact());
		if (misc() != null)      equipped.add(misc());
		if (ring() != null)      equipped.add(ring());
		if (secondWep() != null) equipped.add(secondWep());
		if (helmet() != null)    equipped.add(helmet());
		return equipped.isEmpty() ? null : Random.element(equipped);
	}

	public KindOfWeapon weapon(){
		if (!lostInventory() || (weapon != null && weapon.keptThroughLostInventory())){
			return weapon;
		} else {
			return null;
		}
	}

	public Armor armor(){
		if (!lostInventory() || (armor != null && armor.keptThroughLostInventory())){
			return armor;
		} else {
			return null;
		}
	}

	public Artifact artifact(){
		if (!lostInventory() || (artifact != null && artifact.keptThroughLostInventory())){
			return artifact;
		} else {
			return null;
		}
	}

	public KindofMisc misc(){
		if (!lostInventory() || (misc != null && misc.keptThroughLostInventory())){
			return misc;
		} else {
			return null;
		}
	}

	public Ring ring(){
		if (!lostInventory() || (ring != null && ring.keptThroughLostInventory())){
			return ring;
		} else {
			return null;
		}
	}

	public com.shatteredpixel.shatteredpixeldungeon.items.helmets.Helmet helmet(){
		if (!lostInventory() || (helmet != null && helmet.keptThroughLostInventory())){
			return helmet;
		} else {
			return null;
		}
	}

	public KindOfWeapon secondWep(){
		if (!lostInventory() || (secondWep != null && secondWep.keptThroughLostInventory())){
			return secondWep;
		} else {
			return null;
		}
	}

	// ***
	
	private static final String WEAPON		= "weapon";
	private static final String ARMOR		= "armor";
	private static final String ARTIFACT   = "artifact";
	private static final String MISC       = "misc";
	private static final String RING       = "ring";

	private static final String SECOND_WEP = "second_wep";
	private static final String HELMET     = "helmet";

	public void storeInBundle( Bundle bundle ) {
		
		backpack.storeInBundle( bundle );
		
		bundle.put( WEAPON, weapon );
		bundle.put( ARMOR, armor );
		bundle.put( ARTIFACT, artifact );
		bundle.put( MISC, misc );
		bundle.put( RING, ring );
		bundle.put( SECOND_WEP, secondWep );
		bundle.put( HELMET, helmet );
	}

	public static boolean bundleRestoring = false;
	
	public void restoreFromBundle( Bundle bundle ) {
		bundleRestoring = true;
		backpack.clear();
		backpack.restoreFromBundle( bundle );
		
		weapon = (KindOfWeapon) bundle.get(WEAPON);
		if (weapon() != null)       weapon().activate(owner);
		
		armor = (Armor)bundle.get( ARMOR );
		if (armor() != null)        armor().activate( owner );

		artifact = (Artifact) bundle.get(ARTIFACT);
		if (artifact() != null)     artifact().activate(owner);

		misc = (KindofMisc) bundle.get(MISC);
		if (misc() != null)         misc().activate( owner );

		ring = (Ring) bundle.get(RING);
		if (ring() != null)         ring().activate( owner );

		secondWep = (KindOfWeapon) bundle.get(SECOND_WEP);
		if (secondWep() != null)    secondWep().activate(owner);

		helmet = (com.shatteredpixel.shatteredpixeldungeon.items.helmets.Helmet) bundle.get(HELMET);
		if (helmet() != null)       helmet().activate(owner);

		bundleRestoring = false;
	}

	public void clear(){
		backpack.clear();
		weapon = secondWep = null;
		helmet = null;
		armor = null;
		artifact = null;
		misc = null;
		ring = null;
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		if (bundle.contains( ARMOR )){
			Armor armor = ((Armor)bundle.get( ARMOR ));
			if (armor instanceof ClassArmor){
				info.armorTier = 6;
			} else {
				info.armorTier = armor.tier;
			}
		} else {
			info.armorTier = 0;
		}
	}

	//ignores lost inventory debuff
	public ArrayList<Bag> getBags(){
		ArrayList<Bag> result = new ArrayList<>();

		result.add(backpack);

		for (Item i : this){
			if (i instanceof Bag){
				result.add((Bag)i);
			}
		}

		return result;
	}
	
	@SuppressWarnings("unchecked")
	public<T extends Item> T getItem( Class<T> itemClass ) {

		boolean lostInvent = lostInventory();

		for (Item item : this) {
			if (itemClass.isInstance( item )) {
				if (!lostInvent || item.keptThroughLostInventory()) {
					return (T) item;
				}
			}
		}
		
		return null;
	}

	public<T extends Item> ArrayList<T> getAllItems( Class<T> itemClass ) {
		ArrayList<T> result = new ArrayList<>();

		boolean lostInvent = lostInventory();

		for (Item item : this) {
			if (itemClass.isInstance( item )) {
				if (!lostInvent || item.keptThroughLostInventory()) {
					result.add((T) item);
				}
			}
		}

		return result;
	}
	
	public boolean contains( Item contains ){

		boolean lostInvent = lostInventory();
		
		for (Item item : this) {
			if (contains == item) {
				if (!lostInvent || item.keptThroughLostInventory()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public Item getSimilar( Item similar ){

		boolean lostInvent = lostInventory();
		
		for (Item item : this) {
			if (similar != item && similar.isSimilar(item)) {
				if (!lostInvent || item.keptThroughLostInventory()) {
					return item;
				}
			}
		}
		
		return null;
	}
	
	public ArrayList<Item> getAllSimilar( Item similar ){
		ArrayList<Item> result = new ArrayList<>();

		boolean lostInvent = lostInventory();
		
		for (Item item : this) {
			if (item != similar && similar.isSimilar(item)) {
				if (!lostInvent || item.keptThroughLostInventory()) {
					result.add(item);
				}
			}
		}
		
		return result;
	}

	//triggers when a run ends, so ignores lost inventory effects
	public void identify() {
		for (Item item : this) {
			item.identify(false);
		}
	}
	
	public void observe() {
		if (weapon() != null) {
			if (ShardOfOblivion.passiveIDDisabled() && weapon() instanceof Weapon){
				((Weapon) weapon()).setIDReady();
			} else {
				weapon().identify();
				Badges.validateItemLevelAquired(weapon());
			}
		}
		if (secondWep() != null){
			if (ShardOfOblivion.passiveIDDisabled() && secondWep() instanceof Weapon){
				((Weapon) secondWep()).setIDReady();
			} else {
				secondWep().identify();
				Badges.validateItemLevelAquired(secondWep());
			}
		}
		if (armor() != null) {
			if (ShardOfOblivion.passiveIDDisabled()){
				armor().setIDReady();
			} else {
				armor().identify();
				Badges.validateItemLevelAquired(armor());
			}
		}
		if (artifact() != null) {
			//oblivion shard does not prevent artifact IDing
			artifact().identify();
			Badges.validateItemLevelAquired(artifact());
		}
		if (misc() != null) {
			if (ShardOfOblivion.passiveIDDisabled() && misc() instanceof Ring){
				((Ring) misc()).setIDReady();
			} else {
				misc().identify();
				Badges.validateItemLevelAquired(misc());
			}
		}
		if (ring() != null) {
			if (ShardOfOblivion.passiveIDDisabled()){
				ring().setIDReady();
			} else {
				ring().identify();
				Badges.validateItemLevelAquired(ring());
			}
		}
		if (ShardOfOblivion.passiveIDDisabled()){
			GLog.p(Messages.get(ShardOfOblivion.class, "identify_ready_worn"));
		}
		for (Item item : backpack) {
			if (item instanceof EquipableItem || item instanceof Wand) {
				item.cursedKnown = true;
			}
		}
		Item.updateQuickslot();
	}
	
	public void uncurseEquipped() {
		ScrollOfRemoveCurse.uncurse( owner, armor(), weapon(), artifact(), misc(), ring(), secondWep(), helmet());
	}
	
	public Item randomUnequipped() {
		if (owner.buff(LostInventory.class) != null) return null;

		return Random.element( backpack.items );
	}
	
	public int charge( float charge ) {
		
		int count = 0;
		
		for (Wand.Charger charger : owner.buffs(Wand.Charger.class)){
			charger.gainCharge(charge);
			count++;
		}
		
		return count;
	}

	@Override
	public Iterator<Item> iterator() {
		return new ItemIterator();
	}
	
	private class ItemIterator implements Iterator<Item> {

		private int index = 0;
		
		private Iterator<Item> backpackIterator = backpack.iterator();
		
		private Item[] equipped = {weapon, armor, artifact, misc, ring, secondWep, helmet};
		private int backpackIndex = equipped.length;
		
		@Override
		public boolean hasNext() {
			
			for (int i=index; i < backpackIndex; i++) {
				if (equipped[i] != null) {
					return true;
				}
			}
			
			return backpackIterator.hasNext();
		}

		@Override
		public Item next() {
			
			while (index < backpackIndex) {
				Item item = equipped[index++];
				if (item != null) {
					return item;
				}
			}
			
			return backpackIterator.next();
		}

		@Override
		public void remove() {
			switch (index) {
			case 0:
				equipped[0] = weapon = null;
				break;
			case 1:
				equipped[1] = armor = null;
				break;
			case 2:
				equipped[2] = artifact = null;
				break;
			case 3:
				equipped[3] = misc = null;
				break;
			case 4:
				equipped[4] = ring = null;
				break;
			case 5:
				equipped[5] = secondWep = null;
				break;
			case 6:
				equipped[6] = helmet = null;
				break;
			default:
				backpackIterator.remove();
			}
		}
	}

	/*****************************************************************
	 * Pixel Dungeon Reforged, the Adaptive challenge. Every time an enemy dies all of the hero's gear is swapped
	 * for random gear of the same kind, keeping upgrades, curses and whether it was identified. Unique things
	 * (class items, the waterskin, keys, potions of strength) are left alone.
	 *****************************************************************/

	public void TriggerAdaptive() {
		ArrayList<Item> items = getAllItems(Item.class);

		// detach all items first, just so that we don't drop items that don't need to
		// be dropped because we're removing later stuff
		for (Item item : items) {
			if (item.unique) {
				continue;
			}

			if (item == weapon || item == armor || item == artifact || item == ring || item == misc || item == secondWep) {
				continue;
			}

			if (item instanceof Armor) {
				// because of basic armour, we should only shuffle its glyph/curse
				continue;
			}

			item.detachAll(backpack);
		}

		for (Item item : items) {
			if (item.unique) {
				continue;
			}

			int totalCount = item.quantity();
			for (int i = 0; i < totalCount; ++i) {
				if (item instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon) {
					ReplaceMeleeWeapon((com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon)item);
				} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon) {
					ReplaceMissile((com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon) item);
				} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll) {
					ReplaceScroll();
				} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion) {
					ReplacePotion();
				} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.items.food.Food) {
					giveRandom(com.shatteredpixel.shatteredpixeldungeon.items.Generator.random(com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category.FOOD));
				} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone
						|| item instanceof com.shatteredpixel.shatteredpixeldungeon.plants.Plant.Seed) {
					giveRandom(com.shatteredpixel.shatteredpixeldungeon.items.Generator.random(
							Random.Int(2) == 0 ? com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category.SEED
									: com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category.STONE));
				} else if (item instanceof Artifact) {
					ReplaceArtifacts((Artifact) item);
				} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb) {
					giveRandom(randomFromCatalog(com.shatteredpixel.shatteredpixeldungeon.journal.Catalog.BOMBS));
				} else if (item instanceof Ring) {
					ReplaceRing((Ring) item);
				} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.items.spells.Spell) {
					giveRandom(randomFromCatalog(com.shatteredpixel.shatteredpixeldungeon.journal.Catalog.SPELLS));
				} else if (item instanceof com.shatteredpixel.shatteredpixeldungeon.items.trinkets.Trinket) {
					giveRandom(com.shatteredpixel.shatteredpixeldungeon.items.Generator.random(com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category.TRINKET));
				} else if (item instanceof Wand) {
					ReplaceWand((Wand) item);
				} else if (item instanceof Armor) {
					ShuffleArmor((Armor) item);
				} else {
					item.collect();
					break;
				}
			}
		}
	}

	private void giveRandom(Item item) {
		if (item == null) return;
		if (!item.collect()) {
			Dungeon.level.drop( item, Dungeon.hero.pos ).sprite.drop();
		}
	}

	//a uniformly picked item class from one of the journal catalogs, for kinds of item the generator has no deck for
	private static Item randomFromCatalog(com.shatteredpixel.shatteredpixeldungeon.journal.Catalog catalog) {
		ArrayList<Class<?>> classes = new ArrayList<>(catalog.items());
		for (int tries = 0; tries < 10 && !classes.isEmpty(); tries++) {
			Class<?> cls = Random.element(classes);
			try {
				Object o = com.watabou.utils.Reflection.newInstance(cls);
				if (o instanceof Item) return (Item) o;
			} catch (Exception e) {
				//not every catalog entry can be made from nothing, try another
			}
		}
		return null;
	}

	private void ReplaceMeleeWeapon(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon wep) {
		int tier = wep.tier;
		com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category c = com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category.WEP_T1;
		switch (tier) {
			case 2: c = com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category.WEP_T2; break;
			case 3: c = com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category.WEP_T3; break;
			case 4: c = com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category.WEP_T4; break;
			case 5: c = com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category.WEP_T5; break;
			default: break;
		}
		boolean enchanted = wep.hasGoodEnchant();
		boolean cursed = wep.cursed;
		boolean curseKnown = wep.cursedKnown;
		boolean identified = wep.isIdentified();
		int level = wep.level();

		com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon w =
				(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon) com.shatteredpixel.shatteredpixeldungeon.items.Generator.random(c);
		w.enchantment = null;
		w.cursed = false;
		if (enchanted) {
			w.enchant();
		} else if (cursed) {
			w.enchant(Weapon.Enchantment.randomCurse());
			w.cursed = true;
		}
		if (curseKnown) {
			w.cursedKnown = true;
		}
		w.level(level);
		if (identified) {
			w.identify();
		}

		if (wep == this.weapon) {
			wep.doUnequip(Dungeon.hero, false, false);
			weapon = w;
			w.activate(Dungeon.hero);
		} else if (wep == this.secondWep) {
			wep.doUnequip(Dungeon.hero, false, false);
			secondWep = w;
		} else {
			giveRandom(w);
		}
	}

	private void ReplaceMissile(com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon missile) {
		com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon w =
				com.shatteredpixel.shatteredpixeldungeon.items.Generator.randomMissile();
		boolean enchanted = missile.hasGoodEnchant();
		boolean cursed = missile.cursed;
		boolean identified = missile.isIdentified();
		int upgrades = missile.level();

		if (enchanted) {
			w.enchant();
		}
		if (cursed) {
			w.enchant(Weapon.Enchantment.randomCurse());
			w.cursed = true;
		}
		w.level(upgrades);
		w.quantity(1);
		if (identified) {
			w.identify();
		}
		giveRandom(w);
	}

	private void ReplacePotion() {
		com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion p = null;

		switch (Random.Int(4)) {
			case 0: default:
				p = (com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion) com.shatteredpixel.shatteredpixeldungeon.items.Generator.random(com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category.POTION);
				break;
			case 1:
				p = (com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion) com.shatteredpixel.shatteredpixeldungeon.items.Generator.random(com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category.POTION);
				if (com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion.regToExo.containsKey(p.getClass())){
					p = com.watabou.utils.Reflection.newInstance(com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion.regToExo.get(p.getClass()));
				}
				break;
			case 2: case 3:
				Item b = randomFromCatalog(com.shatteredpixel.shatteredpixeldungeon.journal.Catalog.BREWS_ELIXIRS);
				if (b instanceof com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion) {
					p = (com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion) b;
				} else {
					p = (com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion) com.shatteredpixel.shatteredpixeldungeon.items.Generator.random(com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category.POTION);
				}
				break;
		}
		giveRandom(p);
	}

	private void ReplaceScroll() {
		com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll s =
				(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll) com.shatteredpixel.shatteredpixeldungeon.items.Generator.random(com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category.SCROLL);

		if (Random.Float() >= 0.5f && com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll.regToExo.containsKey(s.getClass())){
			s = com.watabou.utils.Reflection.newInstance(com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll.regToExo.get(s.getClass()));
		}
		giveRandom(s);
	}

	private void ReplaceArtifacts(Artifact oldArtifact) {
		Artifact a = com.shatteredpixel.shatteredpixeldungeon.items.Generator.randomArtifact();

		if (a == null) { // keep this artifact
			if (!(oldArtifact == this.artifact || oldArtifact == this.misc)) {
				oldArtifact.collect();
			}
			return;
		}

		com.shatteredpixel.shatteredpixeldungeon.items.Generator.readdArtifact(oldArtifact.getClass());
		a.cursed = oldArtifact.cursed;
		a.upgrade(oldArtifact.level());
		a.cursedKnown = oldArtifact.cursedKnown;

		if (oldArtifact == this.artifact) {
			oldArtifact.doUnequip(Dungeon.hero, false, false);
			this.artifact = a;
			a.activate(Dungeon.hero);
		} else if (oldArtifact == this.misc) {
			oldArtifact.doUnequip(Dungeon.hero, false, false);
			this.misc = a;
			a.activate(Dungeon.hero);
		} else {
			giveRandom(a);
		}
	}

	private void ReplaceRing(Ring oldRing) {
		Ring r = (Ring) com.shatteredpixel.shatteredpixeldungeon.items.Generator.random(com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category.RING);
		r.cursed = oldRing.cursed;
		r.upgrade(oldRing.level());
		r.cursedKnown = oldRing.cursedKnown;

		if (oldRing == this.ring) {
			oldRing.doUnequip(Dungeon.hero, false, false);
			this.ring = r;
			r.activate(Dungeon.hero);
		} else if (oldRing == this.misc) {
			oldRing.doUnequip(Dungeon.hero, false, false);
			this.misc = r;
			r.activate(Dungeon.hero);
		} else {
			giveRandom(r);
		}
	}

	private void ReplaceWand(Wand oldWand) {
		Wand w = (Wand) com.shatteredpixel.shatteredpixeldungeon.items.Generator.random(com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category.WAND);
		w.cursed = oldWand.cursed;
		w.cursedKnown = oldWand.cursedKnown;
		w.upgrade(oldWand.level());
		w.curCharges = oldWand.curCharges;
		w.partialCharge = oldWand.partialCharge;
		if (oldWand.isIdentified()) {
			w.identify();
		}
		giveRandom(w);
	}

	private void ShuffleArmor(Armor a) {
		if (a.hasGoodGlyph()) {
			a.inscribe();
		}
		if (a.hasCurseGlyph()) {
			a.glyph = Armor.Glyph.randomCurse();
		}
	}


	//Pixel Dungeon Reforged Randomizer, what a thief goes for. Master Pickpocket looks for the best loot,
	//Bold Fingers sometimes takes something that is being worn.
	public Item getThiefItemToSteal() {
		if (owner.buff(LostInventory.class) != null) return null;

		Item toReturn = null;

		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Thief.class) == 3 && Random.Int(2) == 0) {
			int attempts = 0;
			do {
				Item candidate = null;
				switch (Random.Int(5)) {
					case 0: candidate = weapon(); break;
					case 1: candidate = armor(); break;
					case 2: candidate = artifact(); break;
					case 3: candidate = misc(); break;
					case 4: candidate = ring(); break;
				}
				if (candidate != null && !candidate.unique && !candidate.cursed
						&& !(candidate instanceof ClassArmor)
						&& !(candidate instanceof Armor && ((Armor) candidate).checkSeal() != null)
						&& candidate instanceof EquipableItem
						&& ((EquipableItem) candidate).doUnequip(Dungeon.hero, false, false)) {
					toReturn = candidate;
				}
			} while (toReturn == null && ++attempts < 20);
			if (toReturn != null) {
				Dungeon.quickslot.clearItem(toReturn);
				toReturn.updateQuickslot();
				return toReturn;
			}
		}

		if (com.shatteredpixel.shatteredpixeldungeon.Randomizer.getCreatureBuff(com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Thief.class) == 1) {
			ArrayList<Item> highestPriority = new ArrayList<>();
			ArrayList<Item> secondPriority = new ArrayList<>();

			for (Item item : backpack.items) {
				if (item.unique) continue;
				if (item instanceof com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade
						|| item instanceof com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDivineInspiration
						|| item instanceof com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength
						|| item instanceof com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfMight
						|| item instanceof com.shatteredpixel.shatteredpixeldungeon.items.Ankh
						|| item.level() > 5) {
					highestPriority.add(item);
				} else if (item instanceof Artifact
						|| item instanceof com.shatteredpixel.shatteredpixeldungeon.items.trinkets.Trinket
						|| item instanceof com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing
						|| item instanceof com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfHoneyedHealing
						|| item instanceof com.shatteredpixel.shatteredpixeldungeon.items.Honeypot
						|| item instanceof com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass.Seed
						|| item instanceof com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience
						|| item instanceof com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation) {
					secondPriority.add(item);
				}
			}

			if (!highestPriority.isEmpty()) return Random.element(highestPriority);
			if (!secondPriority.isEmpty()) return Random.element(secondPriority);
		}

		//otherwise any plain item, nothing unique or upgraded
		int attempts = 0;
		do {
			toReturn = randomUnequipped();
			if (toReturn != null && !toReturn.unique && toReturn.level() < 1) return toReturn;
		} while (++attempts < 100);
		return null;
	}
}
