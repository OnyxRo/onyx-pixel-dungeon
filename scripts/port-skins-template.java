/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * Shittened Pixel Dungeon (Haukii), item skins
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon;

/*IMPORTS*/
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SkinSprites;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

//Shittened PD item skins. Built by scripts/port-skins.py from the mod's Skin.java (template in
//scripts/port-skins-template.java), so edit the template and rerun the script rather than this file.
//
//Each skin is a new picture for one kind of item, made by using the PAINT action on that item and
//picking an ingredient from the bag. The ingredient is not used up. Rarity 0 is the item's usual look.
//Names and quotes are the mod's own, kept in English as it wrote them.
public enum Skin {

/*ENTRIES*/

	private final Class<? extends Item> skinFor;
	private final int sprite;
	private final int rarity;
	private final String name;
	private final Class<? extends Item> ingredient;
	private final String desc;
	private final String legendName;
	private final int arrow;

	Skin(Class<? extends Item> skinFor, int sprite, int rarity, String name, Class<? extends Item> ingredient,
	     String desc, String legendName, int arrow) {
		this.skinFor = skinFor;
		this.sprite = sprite;
		this.rarity = rarity;
		this.name = name;
		this.ingredient = ingredient;
		this.desc = desc;
		this.legendName = legendName;
		this.arrow = arrow;
	}

	public Class<? extends Item> skinFor() { return skinFor; }
	public int sprite() { return sprite; }
	public int rarityTier() { return rarity; }
	public String skinName() { return name; }
	public String desc() { return desc; }
	public Class<? extends Item> ingredient() { return ingredient; }
	//the name a legendary or mythic skin gives its item, null keeps the item's own name
	public String legendName() { return legendName; }

	//the arrow a spirit bow with this skin shoots
	public int arrowImage() {
		return arrow != -1 ? arrow : ItemSpriteSheet.SPIRIT_ARROW;
	}

	public String rarityName() {
		return Messages.get(Skin.class, "rarity_" + rarity);
	}

	public int rarityColor() {
		switch (rarity) {
			case 1: return 0xBFBFBF;
			case 2: return 0x4F8DEE;
			case 3: return 0xC54EED;
			case 4: return 0xFBB727;
			case 5: return 0xFF1717;
			default: return 0xFFFFFF;
		}
	}

	public String fullSkinName() {
		String n = Messages.titleCase(name.toLowerCase());
		String low = name.toLowerCase();
		if (skinFor == BrokenSeal.class) {
			return low.contains("seal") ? n : n + " Seal";
		} else if (skinFor == CloakOfShadows.class) {
			return low.contains("cloak") ? n : n + " Cloak";
		} else if (skinFor == SpiritBow.class) {
			return low.contains("bow") ? n : n + " Bow";
		} else if (skinFor == MagesStaff.class) {
			return low.contains("staff") || low.contains("wand") ? n : n + " Wand";
		} else if (skinFor == LeatherArmor.class) {
			return n + " Leather Armor";
		} else if (skinFor == PlateArmor.class) {
			return n + " Plate Armor";
		} else if (skinFor == UnstableSpellbook.class) {
			return low.contains("book") || low.contains("guide") ? n : n + " Spellbook";
		} else if (skinFor == ClothArmor.class) {
			return n + " Cloth Armor";
		} else if (skinFor == RunicBlade.class) {
			return low.contains("blade") || low.contains("runic") ? n : n + " Runic Blade";
		} else if (skinFor == Longsword.class) {
			return low.contains("sword") ? n : n + " Longsword";
		} else if (skinFor == Glaive.class) {
			return low.contains("glaive") ? n : n + " Glaive";
		} else if (skinFor == BattleAxe.class) {
			return low.contains("axe") ? n : n + " Battleaxe";
		} else if (skinFor == WarHammer.class) {
			return low.contains("war") || low.contains("hammer") ? n : n + " Warhammer";
		} else if (skinFor == Greatshield.class) {
			return low.contains("shield") ? n : n + " Shield";
		} else if (skinFor == Scimitar.class) {
			return low.contains("itar") ? n : n + " Scimitar";
		} else if (skinFor == Katana.class) {
			return low.contains("katana") ? n : n + " Katana";
		}
		return n;
	}

	//the empty outline the journal shows for a skin not found yet
	public int spriteOutline() {
		if (skinFor == BrokenSeal.class) return SkinSprites.EMPTY_SEAL;
		if (skinFor == CloakOfShadows.class) return SkinSprites.EMPTY_CLOAK;
		if (skinFor == SpiritBow.class) return SkinSprites.EMPTY_BOW;
		if (skinFor == MagesStaff.class) return SkinSprites.EMPTY_WAND;
		if (skinFor == LeatherArmor.class) return SkinSprites.EMPTY_LEATHER;
		if (skinFor == PlateArmor.class) return SkinSprites.EMPTY_PLATE;
		if (skinFor == ClothArmor.class) return SkinSprites.EMPTY_CLOTH;
		if (skinFor == UnstableSpellbook.class) return SkinSprites.EMPTY_BOOK;
		if (skinFor == RunicBlade.class) return SkinSprites.EMPTY_RUNIC;
		if (skinFor == Longsword.class) return SkinSprites.EMPTY_LSWORD;
		if (skinFor == Glaive.class) return SkinSprites.EMPTY_GLAIVE;
		if (skinFor == BattleAxe.class) return SkinSprites.EMPTY_AXE;
		if (skinFor == WarHammer.class) return SkinSprites.EMPTY_HAMMER;
		if (skinFor == Greatshield.class) return SkinSprites.EMPTY_SHIELD;
		if (skinFor == Scimitar.class) return SkinSprites.EMPTY_SCIMITAR;
		return sprite;
	}

	private static final HashMap<Class<?>, List<Skin>> BY_CLASS = new HashMap<>();
	static {
		for (Skin s : values()) {
			List<Skin> list = BY_CLASS.get(s.skinFor);
			if (list == null) {
				list = new ArrayList<>();
				BY_CLASS.put(s.skinFor, list);
			}
			list.add(s);
		}
	}

	public static List<Skin> all() {
		return new ArrayList<>(Arrays.asList(values()));
	}

	public static boolean hasSkins(Class<?> itemClass) {
		return BY_CLASS.containsKey(itemClass);
	}

	public static List<Skin> allFor(Class<?> itemClass) {
		List<Skin> list = BY_CLASS.get(itemClass);
		return list == null ? Collections.<Skin>emptyList() : list;
	}

	//the usual look, rarity 0
	public static Skin defaultFor(Class<?> itemClass) {
		for (Skin s : allFor(itemClass)) {
			if (s.rarity == 0) return s;
		}
		return null;
	}

	//the skin an item shows now. Items that were never painted have no skin saved and show the usual look.
	public static Skin current(Item item) {
		if (item == null) return null;
		if (item.skin != null && item.skin.skinFor == item.getClass()) return item.skin;
		return defaultFor(item.getClass());
	}

	private static boolean matches(Class<? extends Item> ingredient, Item item) {
		if (ingredient == null || item == null) return false;
		if (ingredient == item.getClass()) return true;
		//the mod named the plain Ring class, which no ring in the dungeon is, so any ring counts here
		return ingredient == Ring.class && item instanceof Ring;
	}

	public static boolean containsIngredient(Item item, Class<?> skinFor) {
		return fromIngredient(item, skinFor) != null;
	}

	public static Skin fromIngredient(Item item, Class<?> skinFor) {
		for (Skin s : allFor(skinFor)) {
			if (matches(s.ingredient, item)) return s;
		}
		return null;
	}

	public static Skin random(Class<?> itemClass) {
		List<Skin> list = allFor(itemClass);
		return list.isEmpty() ? null : list.get(Random.Int(list.size()));
	}
}
