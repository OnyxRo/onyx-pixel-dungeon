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

	//Ryuse's PD, extra equipment slots. Old saves have none of these, so they load empty
	public static final int EXTRA_ARMORS = 5;
	public static final int EXTRA_ARTIFACTS = 4;
	public static final int EXTRA_RINGS = 4;
	public Armor[] extraArmors = new Armor[EXTRA_ARMORS];
	public Artifact[] extraArtifacts = new Artifact[EXTRA_ARTIFACTS];
	public Ring[] extraRings = new Ring[EXTRA_RINGS];

	private static int freeIn( Object[] arr ){
		for (int i = 0; i < arr.length; i++) if (arr[i] == null) return i;
		return -1;
	}
	public int freeExtraArmor()    { return freeIn(extraArmors); }
	public int freeExtraArtifact() { return freeIn(extraArtifacts); }
	public int freeExtraRing()     { return freeIn(extraRings); }

	public boolean inExtraSlot( Item item ){
		for (Object o : extraArmors)    if (o == item && item != null) return true;
		for (Object o : extraArtifacts) if (o == item && item != null) return true;
		for (Object o : extraRings)     if (o == item && item != null) return true;
		return false;
	}

	//empties whichever extra slot holds the item, true if one did
	public boolean clearExtraSlot( Item item ){
		boolean found = false;
		for (int i = 0; i < extraArmors.length; i++)    if (item != null && extraArmors[i] == item)    { extraArmors[i] = null; found = true; }
		for (int i = 0; i < extraArtifacts.length; i++) if (item != null && extraArtifacts[i] == item) { extraArtifacts[i] = null; found = true; }
		for (int i = 0; i < extraRings.length; i++)     if (item != null && extraRings[i] == item)     { extraRings[i] = null; found = true; }
		return found;
	}

	//the main armor plus any worn in extra slots, ignoring nothing lost to the lost inventory debuff
	public ArrayList<Armor> allArmors(){
		ArrayList<Armor> result = new ArrayList<>();
		if (armor() != null) result.add(armor());
		for (Armor a : extraArmors){
			if (a != null && (!lostInventory() || a.keptThroughLostInventory())) result.add(a);
		}
		return result;
	}

	//only the armor worn in extra slots
	public ArrayList<Armor> extraArmorsWorn(){
		ArrayList<Armor> result = new ArrayList<>();
		for (Armor a : extraArmors){
			if (a != null && (!lostInventory() || a.keptThroughLostInventory())) result.add(a);
		}
		return result;
	}

	private ArrayList<Item> extraItems(){
		ArrayList<Item> result = new ArrayList<>();
		for (Item i : extraArmors)    if (i != null) result.add(i);
		for (Item i : extraArtifacts) if (i != null) result.add(i);
		for (Item i : extraRings)     if (i != null) result.add(i);
		return result;
	}

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
		for (Item i : extraItems()) {
			if (!lostInventory() || i.keptThroughLostInventory()) equipped.add(i);
		}
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
		for (int i = 0; i < EXTRA_ARMORS; i++)    if (extraArmors[i] != null)    bundle.put( "extra_armor" + i, extraArmors[i] );
		for (int i = 0; i < EXTRA_ARTIFACTS; i++) if (extraArtifacts[i] != null) bundle.put( "extra_artifact" + i, extraArtifacts[i] );
		for (int i = 0; i < EXTRA_RINGS; i++)     if (extraRings[i] != null)     bundle.put( "extra_ring" + i, extraRings[i] );
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

		for (int i = 0; i < EXTRA_ARMORS; i++) {
			extraArmors[i] = bundle.contains("extra_armor" + i) ? (Armor) bundle.get("extra_armor" + i) : null;
			if (extraArmors[i] != null) extraArmors[i].activate(owner);
		}
		for (int i = 0; i < EXTRA_ARTIFACTS; i++) {
			extraArtifacts[i] = bundle.contains("extra_artifact" + i) ? (Artifact) bundle.get("extra_artifact" + i) : null;
			if (extraArtifacts[i] != null) extraArtifacts[i].activate(owner);
		}
		for (int i = 0; i < EXTRA_RINGS; i++) {
			extraRings[i] = bundle.contains("extra_ring" + i) ? (Ring) bundle.get("extra_ring" + i) : null;
			if (extraRings[i] != null) extraRings[i].activate(owner);
		}

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
		extraArmors = new Armor[EXTRA_ARMORS];
		extraArtifacts = new Artifact[EXTRA_ARTIFACTS];
		extraRings = new Ring[EXTRA_RINGS];
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
		for (Item extra : extraItems()) {
			extra.identify();
			Badges.validateItemLevelAquired(extra);
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
		for (Item extra : extraItems()) {
			ScrollOfRemoveCurse.uncurse( owner, extra );
		}
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
		
		private Item[] equipped = buildEquipped();

		private Item[] buildEquipped(){
			ArrayList<Item> list = new ArrayList<>();
			list.add(weapon); list.add(armor); list.add(artifact); list.add(misc);
			list.add(ring); list.add(secondWep); list.add(helmet);
			for (Item i : extraArmors)    list.add(i);
			for (Item i : extraArtifacts) list.add(i);
			for (Item i : extraRings)     list.add(i);
			return list.toArray(new Item[0]);
		}
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
				int slot = index - 1;
				if (slot >= 7 && slot < backpackIndex){
					Item gone = equipped[slot];
					equipped[slot] = null;
					clearExtraSlot(gone);
				} else {
					backpackIterator.remove();
				}
			}
		}
	}
}
