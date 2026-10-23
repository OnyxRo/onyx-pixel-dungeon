/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dobrokhotov
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Challenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpectralBlades;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.WarpBeacon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.DeathMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.SmokeBomb;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Endure;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.HeroicLeap;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Shockwave;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BABicycle;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BABlastGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BAClaymore;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BAFlashGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BAHPGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BAHandGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BAIronHorus;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BALaser;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BALevelAnalyzer;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BARainbowGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BARegrowGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BASmokeGrenade;
import com.shatteredpixel.shatteredpixeldungeon.items.active.BATrashBin;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.BAArisArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.BAHoshinoArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.BAIzunaArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.BAMiyakoArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.BAMiyuArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.BANoaArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.BANonomiArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.BAShirokoArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.BAYuzuArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.BAThrowingSet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.BASuperNova;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WornShortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.AR.BAAR_tier1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.GL.BAGL_tier1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.BAHG_tier1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.MG.BAMG_tier1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SG.BASG_tier1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SMG.BASMG_tier1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.BASR_tier1;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Image;

/** Blue Archive PD's nine students, added beside the other heroes. HeroClass and the hero windows call in here. */
public class BAHeroes {

	public static boolean isBA( HeroClass cls ){
		return cls != null && cls.name().startsWith("BA_");
	}

	public static boolean isBA( Hero hero ){
		return hero != null && isBA(hero.heroClass);
	}

	/** True when the hero is a Blue Archive student other than Aris. Those students find guns instead of melee weapons. */
	public static boolean usesGuns(){
		return Dungeon.hero != null && isBA(Dungeon.hero.heroClass) && Dungeon.hero.heroClass != HeroClass.BA_ARIS;
	}

	public static void initHero( HeroClass cls, Hero hero ){
		switch (cls){
			case BA_ARIS: default:      initAris(hero); break;
			case BA_NONOMI:  gunStart(hero, new BAMG_tier1(), new BASmokeGrenade());
				new ScrollOfUpgrade().identify(); new PotionOfLiquidFlame().identify(); break;
			case BA_MIYAKO:  gunStart(hero, new BASMG_tier1(), new BAHandGrenade(), new BAClaymore());
				new ScrollOfMagicMapping().identify(); new PotionOfInvisibility().identify(); break;
			case BA_HOSHINO: gunStart(hero, new BASG_tier1(), new BAIronHorus(), new BABlastGrenade());
				new PotionOfMindVision().identify(); new ScrollOfLullaby().identify(); break;
			case BA_SHIROKO: gunStart(hero, new BAAR_tier1(), new BAHPGrenade(), new BABicycle());
				new PotionOfStrength().identify(); new ScrollOfMirrorImage().identify(); break;
			case BA_NOA:     gunStart(hero, new BAHG_tier1(), new BALaser(), new BAFlashGrenade());
				new PotionOfMindVision().identify(); new ScrollOfMagicMapping().identify(); break;
			case BA_MIYU:    gunStart(hero, new BASR_tier1(), new BATrashBin(), new BARegrowGrenade());
				new PotionOfStrength().identify(); new ScrollOfUpgrade().identify(); break;
			case BA_YUZU:    gunStart(hero, new BAGL_tier1(), new BALevelAnalyzer(), new BARainbowGrenade());
				new PotionOfLiquidFlame().identify(); new ScrollOfMagicMapping().identify(); break;
			case BA_IZUNA:   initIzuna(hero); break;
		}
	}

	private static void initAris( Hero hero ){
		(hero.belongings.weapon = new WornShortsword()).identify();
		ThrowingStone stones = new ThrowingStone();
		stones.quantity(3).collect();
		Dungeon.quickslot.setSlot(1, stones);
		BASuperNova superNova = new BASuperNova();
		superNova.collect();
		Dungeon.quickslot.setSlot(0, superNova);
		if (hero.belongings.armor != null){
			hero.belongings.armor.affixSeal(new BrokenSeal());
		}
		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
	}

	//the gun goes in hand, each gadget in the next quick slot, and three throwing knives after them
	private static void gunStart( Hero hero, MeleeWeapon gun, com.shatteredpixel.shatteredpixeldungeon.items.Item... gadgets ){
		(hero.belongings.weapon = gun).identify();
		hero.belongings.weapon.activate(hero);
		Dungeon.quickslot.setSlot(0, gun);
		int slot = 1;
		for (com.shatteredpixel.shatteredpixeldungeon.items.Item g : gadgets){
			g.collect();
			Dungeon.quickslot.setSlot(slot++, g);
		}
		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();
		Dungeon.quickslot.setSlot(slot, knives);
	}

	private static void initIzuna( Hero hero ){
		BASMG_tier1 smg = new BASMG_tier1();
		(hero.belongings.weapon = smg).identify();
		hero.belongings.weapon.activate(hero);
		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.artifact = cloak).identify();
		hero.belongings.artifact.activate( hero );
		BAThrowingSet throwingSet = new BAThrowingSet();
		throwingSet.collect();
		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();
		Dungeon.quickslot.setSlot(0, smg);
		Dungeon.quickslot.setSlot(1, cloak);
		Dungeon.quickslot.setSlot(2, throwingSet);
		Dungeon.quickslot.setSlot(3, knives);
		new PotionOfLiquidFlame().identify();
		new ScrollOfMagicMapping().identify();
	}

	public static Badges.Badge masteryBadge( HeroClass cls ){
		return Badges.Badge.valueOf("MASTERY_" + cls.name());
	}

	//Blue Archive PD's own armor abilities were empty placeholders, so each student gets three of Shattered's, picked to suit her
	public static ArmorAbility[] armorAbilities( HeroClass cls ){
		switch (cls){
			case BA_ARIS: default: return new ArmorAbility[]{new HeroicLeap(), new Shockwave(), new Endure()};
			case BA_NONOMI:  return new ArmorAbility[]{new Shockwave(), new Endure(), new Challenge()};
			case BA_MIYAKO:  return new ArmorAbility[]{new SmokeBomb(), new DeathMark(), new SpiritHawk()};
			case BA_HOSHINO: return new ArmorAbility[]{new Endure(), new HeroicLeap(), new Challenge()};
			case BA_SHIROKO: return new ArmorAbility[]{new HeroicLeap(), new SpiritHawk(), new SmokeBomb()};
			case BA_NOA:     return new ArmorAbility[]{new WarpBeacon(), new SpectralBlades(), new DeathMark()};
			case BA_MIYU:    return new ArmorAbility[]{new SpiritHawk(), new SmokeBomb(), new SpectralBlades()};
			case BA_YUZU:    return new ArmorAbility[]{new Shockwave(), new WarpBeacon(), new SpectralBlades()};
			case BA_IZUNA:   return new ArmorAbility[]{new SmokeBomb(), new ShadowClone(), new DeathMark()};
		}
	}

	public static String spritesheet( HeroClass cls ){
		switch (cls){
			case BA_ARIS: default: return Assets.Sprites.BA_ARIS;
			case BA_NONOMI:  return Assets.Sprites.BA_NONOMI;
			case BA_MIYAKO:  return Assets.Sprites.BA_MIYAKO;
			case BA_HOSHINO: return Assets.Sprites.BA_HOSHINO;
			case BA_SHIROKO: return Assets.Sprites.BA_SHIROKO;
			case BA_NOA:     return Assets.Sprites.BA_NOA;
			case BA_MIYU:    return Assets.Sprites.BA_MIYU;
			case BA_YUZU:    return Assets.Sprites.BA_YUZU;
			case BA_IZUNA:   return Assets.Sprites.BA_IZUNA;
		}
	}

	public static String splashArt( HeroClass cls ){
		switch (cls){
			case BA_ARIS: default: return Assets.Splashes.BA_ARIS;
			case BA_NONOMI:  return Assets.Splashes.BA_NONOMI;
			case BA_MIYAKO:  return Assets.Splashes.BA_MIYAKO;
			case BA_HOSHINO: return Assets.Splashes.BA_HOSHINO;
			case BA_SHIROKO: return Assets.Splashes.BA_SHIROKO;
			case BA_NOA:     return Assets.Splashes.BA_NOA;
			case BA_MIYU:    return Assets.Splashes.BA_MIYU;
			case BA_YUZU:    return Assets.Splashes.BA_YUZU;
			case BA_IZUNA:   return Assets.Splashes.BA_IZUNA;
		}
	}

	public static ClassArmor classArmor( HeroClass cls ){
		switch (cls){
			case BA_ARIS: default: return new BAArisArmor();
			case BA_NONOMI:  return new BANonomiArmor();
			case BA_MIYAKO:  return new BAMiyakoArmor();
			case BA_HOSHINO: return new BAHoshinoArmor();
			case BA_SHIROKO: return new BAShirokoArmor();
			case BA_NOA:     return new BANoaArmor();
			case BA_MIYU:    return new BAMiyuArmor();
			case BA_YUZU:    return new BAYuzuArmor();
			case BA_IZUNA:   return new BAIzunaArmor();
		}
	}

	//the picture on the hero select tab, the mod's own choice for each student
	public static Image icon( HeroClass cls ){
		switch (cls){
			case BA_ARIS: default: return new ItemSprite(ItemSpriteSheet.BA_SUPER_NOVA, null);
			case BA_NONOMI:  return new ItemSprite(ItemSpriteSheet.BA_MG_TIER_1, null);
			case BA_MIYAKO:  return new ItemSprite(ItemSpriteSheet.BA_SMG_TIER_1, null);
			case BA_HOSHINO: return new ItemSprite(ItemSpriteSheet.BA_SG_TIER_1, null);
			case BA_SHIROKO: return new ItemSprite(ItemSpriteSheet.BA_AR_TIER_1, null);
			case BA_NOA:     return new ItemSprite(ItemSpriteSheet.BA_HG_TIER_1, null);
			case BA_MIYU:    return new ItemSprite(ItemSpriteSheet.BA_SR_TIER_1, null);
			case BA_YUZU:    return new ItemSprite(ItemSpriteSheet.BA_GL_TIER_1, null);
			case BA_IZUNA:   return new ItemSprite(ItemSpriteSheet.BA_THROWING_SET, null);
		}
	}

	//one picture beside each paragraph of the hero description
	public static Image[] infoIcons( HeroClass cls, int count ){
		int[] pics;
		switch (cls){
			case BA_ARIS: default: pics = new int[]{ItemSpriteSheet.SEAL, ItemSpriteSheet.WORN_SHORTSWORD, ItemSpriteSheet.BA_SUPER_NOVA}; break;
			case BA_NONOMI:  pics = new int[]{ItemSpriteSheet.BA_SMOKE_GRENADE, ItemSpriteSheet.BA_MG_TIER_5, ItemSpriteSheet.BA_MG_TIER_1}; break;
			case BA_MIYAKO:  pics = new int[]{ItemSpriteSheet.BA_CLAYMORE, ItemSpriteSheet.BA_SMG_TIER_5, ItemSpriteSheet.BA_HAND_GRENADE}; break;
			case BA_HOSHINO: pics = new int[]{ItemSpriteSheet.BA_IRON_HORUS, ItemSpriteSheet.BA_SG_TIER_5, ItemSpriteSheet.BA_BLAST_GRENADE}; break;
			case BA_SHIROKO: pics = new int[]{ItemSpriteSheet.BA_BICYCLE, ItemSpriteSheet.BA_AR_TIER_5, ItemSpriteSheet.BA_HP_GRENADE}; break;
			case BA_NOA:     pics = new int[]{ItemSpriteSheet.BA_LASER, ItemSpriteSheet.BA_HG_TIER_5, ItemSpriteSheet.BA_FLASH_GRENADE}; break;
			case BA_MIYU:    pics = new int[]{ItemSpriteSheet.BA_TRASH_BIN, ItemSpriteSheet.BA_SR_TIER_5, ItemSpriteSheet.BA_REGROW_GRENADE}; break;
			case BA_YUZU:    pics = new int[]{ItemSpriteSheet.BA_ANALYZER, ItemSpriteSheet.BA_GL_TIER_5, ItemSpriteSheet.BA_RAINBOW_GRENADE}; break;
			case BA_IZUNA:   pics = new int[]{ItemSpriteSheet.BA_THROWING_SET, ItemSpriteSheet.BA_SPIRIT_SHURIKEN, ItemSpriteSheet.BA_SPIRIT_KUNAI}; break;
		}
		Image[] icons = new Image[count];
		for (int i = 0; i < count; i++){
			//the last paragraph is about identified items, as for Shattered's heroes
			icons[i] = new ItemSprite(i == count - 1 ? ItemSpriteSheet.SCROLL_ISAZ : pics[Math.min(i, pics.length - 1)]);
		}
		return icons;
	}
}
