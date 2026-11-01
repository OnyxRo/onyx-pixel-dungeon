/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Re-Remixed Dungeon, by QuasiStellar, ported into Onyx's Pixel Dungeon
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.WarpBeacon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.DeathMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.SmokeBomb;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Endure;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.HeroicLeap;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Shockwave;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.MysticalSkull;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.HuntressArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.MageArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.WarriorArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.ElvenDagger;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.HandAxe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.StoneSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Tomahawk;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

//reremixed, Re-Remixed Dungeon's Elf, Necromancer and Gnoll. Starting kits and passives as in the mod,
//the skills are in RemixedSkills. The mod had no talents and its subclasses did nothing yet, so their
//talents are Shattered's, picked to suit each hero (see Talent.initClassTalents).
public class RemixedHeroes {

	public static boolean isRemixed( HeroClass cls ){
		return cls == HeroClass.ELF || cls == HeroClass.NECROMANCER || cls == HeroClass.GNOLL;
	}

	public static void init( HeroClass cls, Hero hero ){
		switch (cls){
			case ELF: {
				hero.HT = hero.HP = 15;
				(hero.belongings.weapon = new ElvenDagger()).identify();
				hero.belongings.weapon.activate(hero);
				AlchemistsToolkit toolkit = new AlchemistsToolkit();
				(hero.belongings.artifact = toolkit).identify();
				hero.belongings.artifact.activate(hero);
				new PotionBandolier().collect();
				Dungeon.LimitedDrops.POTION_BANDOLIER.drop();
				new PotionOfHaste().identify();
				new ScrollOfMirrorImage().identify();
				break;
			}
			case NECROMANCER: {
				StoneSword sword = new StoneSword();
				sword.identify();
				sword.skull = new MysticalSkull();
				hero.belongings.weapon = sword;
				hero.belongings.weapon.activate(hero);
				new ScrollHolder().collect();
				Dungeon.LimitedDrops.SCROLL_HOLDER.drop();
				new ScrollOfTerror().identify();
				new PotionOfToxicGas().identify();
				break;
			}
			case GNOLL: {
				hero.STR = 15;
				hero.belongings.armor = null;
				(hero.belongings.weapon = new HandAxe()).identify();
				hero.belongings.weapon.activate(hero);
				Tomahawk tomahawks = new Tomahawk();
				tomahawks.quantity(5).collect();
				Dungeon.quickslot.setSlot(0, tomahawks);
				new MagicalHolster().collect();
				Dungeon.LimitedDrops.MAGICAL_HOLSTER.drop();
				new PotionOfStrength().identify();
				new ScrollOfRemoveCurse().identify();
				break;
			}
			default:
				return;
		}
		RemixedSkills.initHero(hero);
	}

	public static ArmorAbility[] armorAbilities( HeroClass cls ){
		switch (cls){
			case ELF: return new ArmorAbility[]{new SpiritHawk(), new SmokeBomb(), new WarpBeacon()};
			case NECROMANCER: return new ArmorAbility[]{new DeathMark(), new ShadowClone(), new WarpBeacon()};
			default: return new ArmorAbility[]{new HeroicLeap(), new Shockwave(), new Endure()};
		}
	}

	//the mod's class armors had their own specials, these borrow the closest Shattered armor
	public static ClassArmor classArmor( HeroClass cls ){
		switch (cls){
			case ELF: return new HuntressArmor();
			case NECROMANCER: return new MageArmor();
			default: return new WarriorArmor();
		}
	}

	public static String spritesheet( HeroClass cls ){
		switch (cls){
			case ELF: return Assets.Sprites.RR_ELF;
			case NECROMANCER: return Assets.Sprites.RR_NECROMANCER;
			default: return Assets.Sprites.RR_GNOLL;
		}
	}

	//the mod had no splash art, these borrow the closest ones
	public static String splashArt( HeroClass cls ){
		switch (cls){
			case ELF: return Assets.Splashes.HUNTRESS;
			case NECROMANCER: return Assets.Splashes.MAGE;
			default: return Assets.Splashes.WARRIOR;
		}
	}

	public static Image icon( HeroClass cls ){
		switch (cls){
			case ELF: return new ItemSprite(ItemSpriteSheet.ELVEN_DAGGER);
			case NECROMANCER: return new ItemSprite(ItemSpriteSheet.RAT_SKULL);
			default: return new ItemSprite(ItemSpriteSheet.TOMAHAWK);
		}
	}

	public static Badges.Badge masteryBadge( HeroClass cls ){
		switch (cls){
			case ELF: return Badges.Badge.MASTERY_ELF;
			case NECROMANCER: return Badges.Badge.MASTERY_NECROMANCER;
			default: return Badges.Badge.MASTERY_GNOLL;
		}
	}

	public static boolean isUnlocked( HeroClass cls ){
		switch (cls){
			case ELF: return Badges.isUnlocked(Badges.Badge.UNLOCK_ELF);
			case NECROMANCER: return Badges.isUnlocked(Badges.Badge.UNLOCK_NECROMANCER);
			default: return Badges.isUnlocked(Badges.Badge.UNLOCK_GNOLL);
		}
	}

	//passives. The Elf is frail and quick and cannot be held down
	public static int baseHT( Hero hero, int ht ){
		return hero.heroClass == HeroClass.ELF ? 15 + 4*(hero.lvl-1) + hero.HTBoost : ht;
	}

	public static float speedFactor( Hero hero ){
		return hero.heroClass == HeroClass.ELF ? 1.25f : 1f;
	}

	public static boolean isImmune( Hero hero, Class effect ){
		return hero.heroClass == HeroClass.ELF && (effect == Paralysis.class || effect == Roots.class);
	}

	//the Gnoll cannot read
	public static boolean cannotRead( Hero hero ){
		if (hero.heroClass != HeroClass.GNOLL) return false;
		GLog.w(Messages.get(RemixedHeroes.class, "gnoll_read"));
		return true;
	}

	//unlocks. Elf, craft three things with alchemy in one run. Necromancer, open five graves, tombs or
	//skeletons in one run. Gnoll, drink three potions of strength in one run.
	public static void validateElfUnlock(){
		if (Statistics.itemsCrafted >= 3) Badges.validateXunUnlock(Badges.Badge.UNLOCK_ELF);
	}

	public static void onHeapOpened( Heap heap, Hero hero ){
		if (hero == null || (heap.type != Heap.Type.TOMB && heap.type != Heap.Type.SKELETON && heap.type != Heap.Type.REMAINS)) return;
		Tracker t = Buff.affect(hero, Tracker.class);
		if (++t.graves >= 5) Badges.validateXunUnlock(Badges.Badge.UNLOCK_NECROMANCER);
	}

	public static void onStrength( Hero hero ){
		if (hero == null) return;
		Tracker t = Buff.affect(hero, Tracker.class);
		if (++t.strength >= 3) Badges.validateXunUnlock(Badges.Badge.UNLOCK_GNOLL);
	}

	//per run counts for the unlocks, kept on the hero so they save with the run
	public static class Tracker extends Buff {
		{
			revivePersists = true;
		}

		public int graves, strength;

		@Override
		public boolean act(){
			diactivate();
			return true;
		}

		@Override
		public void storeInBundle( Bundle bundle ){
			super.storeInBundle(bundle);
			bundle.put("graves", graves);
			bundle.put("strength", strength);
		}

		@Override
		public void restoreFromBundle( Bundle bundle ){
			super.restoreFromBundle(bundle);
			graves = bundle.getInt("graves");
			strength = bundle.getInt("strength");
		}
	}
}
