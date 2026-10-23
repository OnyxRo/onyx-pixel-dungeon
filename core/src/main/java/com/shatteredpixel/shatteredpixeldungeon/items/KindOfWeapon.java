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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.items.MetalPart;
import com.shatteredpixel.shatteredpixeldungeon.effects.Transmuting;
import com.watabou.utils.Bundle;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.engineer.ForceField;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PhysicalEmpower;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.DamageTag;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

abstract public class KindOfWeapon extends EquipableItem {
	//Tower PD, hero spells cool down faster or slower with the weapon
	public float spellCooldownModifier = 1f;


	protected String hitSound = Assets.Sounds.HIT;
	protected float hitSoundPitch = 1f;
	
	@Override
	public void execute(Hero hero, String action) {
		if ((hero.subClass.is(HeroSubClass.CHAMPION) || (hero.subClass == HeroSubClass.BA_NOA_EX_DOUBLE_BARREL && this instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.BAHG)) && action.equals(AC_EQUIP)){ //bluearchive
			usesTargeting = false;
			String primaryName = Messages.titleCase(hero.belongings.weapon != null ? hero.belongings.weapon.trueName() : Messages.get(KindOfWeapon.class, "empty"));
			String secondaryName = Messages.titleCase(hero.belongings.secondWep != null ? hero.belongings.secondWep.trueName() : Messages.get(KindOfWeapon.class, "empty"));
			if (primaryName.length() > 18) primaryName = primaryName.substring(0, 15) + "...";
			if (secondaryName.length() > 18) secondaryName = secondaryName.substring(0, 15) + "...";
			GameScene.show(new WndOptions(
					new ItemSprite(this),
					Messages.titleCase(name()),
					Messages.get(KindOfWeapon.class, "which_equip_msg"),
					Messages.get(KindOfWeapon.class, "which_equip_primary", primaryName),
					Messages.get(KindOfWeapon.class, "which_equip_secondary", secondaryName)
			){
				@Override
				protected void onSelect(int index) {
					super.onSelect(index);
					if (index == 0 || index == 1){
						//In addition to equipping itself, item reassigns itself to the quickslot
						//This is a special case as the item is being removed from inventory, but is staying with the hero.
						int slot = Dungeon.quickslot.getSlot( KindOfWeapon.this );
						slotOfUnequipped = -1;
						if (index == 0) {
							doEquip(hero);
						} else {
							equipSecondary(hero);
						}
						if (slot != -1) {
							Dungeon.quickslot.setSlot( slot, KindOfWeapon.this );
							updateQuickslot();
						//if this item wasn't quickslotted, but the item it is replacing as equipped was
						//then also have the item occupy the unequipped item's quickslot
						} else if (slotOfUnequipped != -1 && defaultAction() != null) {
							Dungeon.quickslot.setSlot( slotOfUnequipped, KindOfWeapon.this );
							updateQuickslot();
						}
					}
				}
			});
		} else {
			super.execute(hero, action);
		}
	}

	@Override
	public boolean isEquipped( Hero hero ) {
		return hero != null && (hero.belongings.weapon() == this || hero.belongings.secondWep() == this);
	}

	private static boolean isSwiftEquipping = false;

	protected float timeToEquip( Hero hero ) {
		return isSwiftEquipping ? 0f : super.timeToEquip(hero);
	}
	
	@Override
	public boolean doEquip( Hero hero ) {

		if (Dungeon.isChallenged(com.shatteredpixel.shatteredpixeldungeon.Challenges.CUBE)) {
			return false;
		}

		isSwiftEquipping = false;
		if (hero.belongings.contains(this) && hero.hasTalent(Talent.SWIFT_EQUIP)){
			if (hero.buff(Talent.SwiftEquipCooldown.class) == null
					|| hero.buff(Talent.SwiftEquipCooldown.class).hasSecondUse()){
				isSwiftEquipping = true;
			}
		}

		// 15/25% chance
		if (!hero.heroClass.is(HeroClass.CLERIC) && hero.hasTalent(Talent.HOLY_INTUITION)
				&& cursed && !cursedKnown
				&& Random.Int(20) < 1 + 2*hero.pointsInTalent(Talent.HOLY_INTUITION)){
			cursedKnown = true;
			GLog.p(Messages.get(this, "curse_detected"));
			return false;
		}

		detachAll( hero.belongings.backpack );
		
		if (hero.belongings.weapon == null || hero.belongings.weapon.doUnequip( hero, true )) {
			
			hero.belongings.weapon = this;
			activate( hero );
			Talent.onItemEquipped(hero, this);
			Badges.validateDuelistUnlock();
			updateQuickslot();

			cursedKnown = true;
			if (cursed) {
				equipCursed( hero );
				GLog.n( Messages.get(KindOfWeapon.class, "equip_cursed") );
			}

			hero.spendAndNext( timeToEquip(hero) );
			if (isSwiftEquipping) {
				GLog.i(Messages.get(this, "swift_equip"));
				if (hero.buff(Talent.SwiftEquipCooldown.class) == null){
					Buff.affect(hero, Talent.SwiftEquipCooldown.class, 19f)
							.secondUse = hero.pointsInTalent(Talent.SWIFT_EQUIP) == 2;
				} else if (hero.buff(Talent.SwiftEquipCooldown.class).hasSecondUse()) {
					hero.buff(Talent.SwiftEquipCooldown.class).secondUse = false;
				}
				isSwiftEquipping = false;
			}
			return true;
			
		} else {
			isSwiftEquipping = false;
			collect( hero.belongings.backpack );
			return false;
		}
	}

	public boolean equipSecondary( Hero hero ){

		if (Dungeon.isChallenged(com.shatteredpixel.shatteredpixeldungeon.Challenges.CUBE)) {
			return false;
		}

		isSwiftEquipping = false;
		if (hero.belongings.contains(this) && hero.hasTalent(Talent.SWIFT_EQUIP)){
			if (hero.buff(Talent.SwiftEquipCooldown.class) == null
					|| hero.buff(Talent.SwiftEquipCooldown.class).hasSecondUse()){
				isSwiftEquipping = true;
			}
		}

		boolean wasInInv = hero.belongings.contains(this);
		detachAll( hero.belongings.backpack );

		if (hero.belongings.secondWep == null || hero.belongings.secondWep.doUnequip( hero, true )) {

			hero.belongings.secondWep = this;
			activate( hero );
			Talent.onItemEquipped(hero, this);
			Badges.validateDuelistUnlock();
			updateQuickslot();

			cursedKnown = true;
			if (cursed) {
				equipCursed( hero );
				GLog.n( Messages.get(KindOfWeapon.class, "equip_cursed") );
			}

			hero.spendAndNext( timeToEquip(hero) );
			if (isSwiftEquipping) {
				GLog.i(Messages.get(this, "swift_equip"));
				if (hero.buff(Talent.SwiftEquipCooldown.class) == null){
					Buff.affect(hero, Talent.SwiftEquipCooldown.class, 19f)
							.secondUse = hero.pointsInTalent(Talent.SWIFT_EQUIP) == 2;
				} else if (hero.buff(Talent.SwiftEquipCooldown.class).hasSecondUse()) {
					hero.buff(Talent.SwiftEquipCooldown.class).secondUse = false;
				}
				isSwiftEquipping = false;
			}
			return true;

		} else {
			isSwiftEquipping = false;
			collect( hero.belongings.backpack );
			return false;
		}
	}

	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		boolean second = hero.belongings.secondWep == this;

		if (second){
			//do this first so that the item can go to a full inventory
			hero.belongings.secondWep = null;
		}

		if (super.doUnequip( hero, collect, single )) {

			if (!second){
				hero.belongings.weapon = null;
			}
			return true;

		} else {

			if (second){
				hero.belongings.secondWep = this;
			}
			return false;

		}
	}

	public int min(){
		return min(buffedLvl());
	}

	public int max(){
		return max(buffedLvl());
	}

	abstract public int min(int lvl);
	abstract public int max(int lvl);

	public int damageRoll( Char owner ) {
		if (owner instanceof Hero){
			return Hero.heroDamageIntRange(min(), max());
		} else {
			return Random.NormalIntRange(min(), max());
		}
	}
	
	public float accuracyFactor( Char owner, Char target ) {
		return 1f;
	}
	
	public float delayFactor( Char owner ) {
		return 1f;
	}

	public int reachFactor( Char owner ){
		return 1;
	}
	
	public boolean canReach( Char owner, int target){
		return canReach(owner, target, 0);
	}

	//from Transformation PD, extra reach from a talent or buff
	public boolean canReach( Char owner, int target, int extraReach ){
		int reach = reachFactor(owner) + extraReach;
		reach += com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RearrangedWeapons.canReachBonus(owner); //rearranged
		if (Dungeon.level.distance( owner.pos, target ) > reach){
			return false;
		} else {
			boolean[] passable = BArray.not(Dungeon.level.solid, null);
			for (Char ch : Actor.chars()) {
				if (ch != owner) passable[ch.pos] = false;
			}
			
			PathFinder.buildDistanceMap(target, passable, reach);
			
			return PathFinder.distance[owner.pos] <= reach;
		}
	}

	public int defenseFactor( Char owner ) {
		return 0;
	}
	
	public int proc( Char attacker, Char defender, int damage ) {
		return damage;
	}

	//Transformation PD, called after the hero's attack with this weapon is fully resolved
	public void onAttackResolved( Hero attacker, Char defender, boolean hit, int damageDealt, DamageTag... damageTags ) {
	}

	public void hitSound( float pitch ){
		Sample.INSTANCE.play(hitSound, 1, pitch * hitSoundPitch);
	}
	

	//xun, Xun's PD's modifications, fitted by the Engineer with metal parts
	public enum Modification{
		LONG_HANDLE, //only for melee
		SHARP_BLADE,
		SMOG_ATTACH, //only for thrown
		CONDUCTIVE,
		BATTLE_MODULE,
		PNEUMATICS;

		public int partCost(){
			switch (this){
				case LONG_HANDLE:     return 3;
				case SMOG_ATTACH:     return 3;
				case SHARP_BLADE:     return 4;
				case CONDUCTIVE:      return 5;
				case BATTLE_MODULE:   return 8;
				case PNEUMATICS:      return 8;
				default:              return 0;
			}
		}

		public int maxDurability(){
			switch (this){
				case LONG_HANDLE:
				case SMOG_ATTACH:     return 15;
				case SHARP_BLADE:
				case BATTLE_MODULE:
				case PNEUMATICS:
				case CONDUCTIVE:
				default:              return 25;
			}
		}

		public String title(){return Messages.get(Modification.class, toString());}
		public String desc() {return Messages.get(Modification.class, this + "_desc");}

		public boolean craftsman(){
			return this == BATTLE_MODULE || this == PNEUMATICS;
		}
	}

	public void decreaseModDurability(){
		if (Dungeon.hero.buff(ForceField.Field.class) == null
				|| Random.Int(4) >= Dungeon.hero.pointsInTalent(Talent.REPAIR_ABILITY))
			modDurability = Math.max(modDurability - 1, 0);
		if (modDurability <= 0) modify(null);
	}

	public void modify(Modification mod){
		boolean activeRepair = modify == mod && Dungeon.hero.hasTalent(Talent.ACTIVE_REPAIR);
		modify = mod;
		if (mod == null){
			modDurability = 0;
			GLog.n(Messages.get(this, "modify_break"));
			float chance = Dungeon.hero.pointsInTalent(Talent.PART_RECYCLING) / 2f;
			while (Random.Float() < chance){
				MetalPart part = new MetalPart();
				if (!part.collect()) Dungeon.level.drop(part, Dungeon.hero.pos).sprite.drop();
				chance --;
			}
			if (Dungeon.hero.hasTalent(Talent.KINETIC_FRAGMENT)){
				Buff.affect(Dungeon.hero, PhysicalEmpower.class).set((int)(Dungeon.hero.lvl * 0.5f), 2);
			}
		} else {
			if (!activeRepair)
				modDurability = 0;

			float duraToInc = mod.maxDurability() * (1 + 0.15f * Dungeon.hero.pointsInTalent(Talent.DURABLE_MODIFIES));
			if (Dungeon.hero.pointsInTalent(Talent.ACTIVE_REPAIR) >= 2 && activeRepair) duraToInc *= 1.2f;
			modDurability += Math.round(duraToInc);

			Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
			Transmuting.show(curUser, this, this);
			curUser.sprite.operate(curUser.pos);

			if (Dungeon.hero.pointsInTalent(Talent.ACTIVE_REPAIR) >= 3 && activeRepair){
				MetalPart part = new MetalPart();
				if (!part.collect()) Dungeon.level.drop(part, Dungeon.hero.pos).sprite.drop();
			}
		}
	}

	public static class BattleModule{} //xun, only used to mark damage

	public Modification modify = null;
	public int modDurability = 0;

	private static final String XUN_MODIFY = "modify";
	private static final String XUN_MODDURA = "mod_durability";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		if (modify != null) bundle.put(XUN_MODIFY, modify);
		bundle.put(XUN_MODDURA, modDurability);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(XUN_MODIFY)) modify = bundle.getEnum(XUN_MODIFY, Modification.class);
		modDurability = bundle.getInt(XUN_MODDURA);
	}

}
