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

package com.shatteredpixel.shatteredpixeldungeon.journal;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Skin;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.BattleAxe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Glaive;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatshield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Katana;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Longsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RunicBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Scimitar;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WarHammer;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

//Shittened PD's journal of skins. Which skins were ever applied, and how often, kept across runs with
//the rest of the journal. Shown in the journal's catalog under the fifth button (ui/SkinJournal).
public enum SkinCatalog {
	SEALS,
	CLOAKS,
	BOWS,
	WANDS,
	ARMORS,
	BOOKS,
	RUNICS,
	LSWORDS,
	GLAIVES,
	AXES,
	HAMMERS,
	SHIELDS,
	SCIMITARS,
	KATANAS;

	private final LinkedHashMap<Skin, Boolean> seen = new LinkedHashMap<>();
	private final LinkedHashMap<Skin, Integer> applyCount = new LinkedHashMap<>();

	public Collection<Skin> skins() {
		return seen.keySet();
	}

	private void addSkins(Class<?> itemClass) {
		for (Skin skin : Skin.allFor(itemClass)) {
			//Dracula's cloak only comes with the rogue's cloak at Halloween, the mod kept it out of the journal
			if (skin == Skin.DRACULACLOAK) continue;
			seen.put(skin, false);
			applyCount.put(skin, 0);
		}
	}

	public String title() {
		return Messages.get(this, name() + ".title");
	}

	//the usual looks are not counted
	public int totalSkins() {
		int total = 0;
		for (Skin skin : seen.keySet()) {
			if (skin.rarityTier() != 0) total++;
		}
		return total;
	}

	public int totalSeen() {
		int total = 0;
		for (Skin skin : seen.keySet()) {
			if (skin.rarityTier() != 0 && seen.get(skin)) total++;
		}
		return total;
	}

	public boolean allSeen() {
		return totalSeen() == totalSkins();
	}

	static {
		SEALS.addSkins(BrokenSeal.class);
		CLOAKS.addSkins(CloakOfShadows.class);
		BOWS.addSkins(SpiritBow.class);
		WANDS.addSkins(MagesStaff.class);
		ARMORS.addSkins(ClothArmor.class);
		ARMORS.addSkins(LeatherArmor.class);
		ARMORS.addSkins(PlateArmor.class);
		BOOKS.addSkins(UnstableSpellbook.class);
		RUNICS.addSkins(RunicBlade.class);
		LSWORDS.addSkins(Longsword.class);
		GLAIVES.addSkins(Glaive.class);
		AXES.addSkins(BattleAxe.class);
		HAMMERS.addSkins(WarHammer.class);
		SHIELDS.addSkins(Greatshield.class);
		SCIMITARS.addSkins(Scimitar.class);
		KATANAS.addSkins(Katana.class);
	}

	//the mod's badge for finding every skin of one kind. Shields, scimitars and katanas never had one.
	public Badges.Badge badge() {
		switch (this) {
			case SEALS:   return Badges.Badge.ALL_SEAL_SKINS;
			case CLOAKS:  return Badges.Badge.ALL_CLOAK_SKINS;
			case BOWS:    return Badges.Badge.ALL_BOW_SKINS;
			case WANDS:   return Badges.Badge.ALL_WAND_SKINS;
			case ARMORS:  return Badges.Badge.ALL_ARMOR_SKINS;
			case BOOKS:   return Badges.Badge.ALL_BOOK_SKINS;
			case RUNICS:  return Badges.Badge.ALL_RUNIC_SKINS;
			case LSWORDS: return Badges.Badge.ALL_LSWORD_SKINS;
			case GLAIVES: return Badges.Badge.ALL_GLAIVE_SKINS;
			case AXES:    return Badges.Badge.ALL_AXE_SKINS;
			case HAMMERS: return Badges.Badge.ALL_HAMMER_SKINS;
			default:      return null;
		}
	}

	public static boolean isSeen(Skin skin) {
		if (skin.rarityTier() == 0) return true;
		for (SkinCatalog cat : values()) {
			if (cat.seen.containsKey(skin)) return cat.seen.get(skin);
		}
		return false;
	}

	public static void setSeen(Skin skin) {
		if (Dungeon.sandbox) return;
		for (SkinCatalog cat : values()) {
			if (cat.seen.containsKey(skin) && !cat.seen.get(skin)) {
				cat.seen.put(skin, true);
				Journal.saveNeeded = true;
			}
		}
		Badges.validateSkinsFound();
	}

	public static int applyCount(Skin skin) {
		for (SkinCatalog cat : values()) {
			if (cat.applyCount.containsKey(skin)) return cat.applyCount.get(skin);
		}
		return 0;
	}

	public static void countApply(Skin skin) {
		if (Dungeon.sandbox) return;
		for (SkinCatalog cat : values()) {
			if (cat.applyCount.containsKey(skin) && cat.applyCount.get(skin) != Integer.MAX_VALUE) {
				cat.applyCount.put(skin, cat.applyCount.get(skin) + 1);
				Journal.saveNeeded = true;
			}
		}
	}

	public static int totalFound() {
		int found = 0;
		for (SkinCatalog cat : values()) found += cat.totalSeen();
		return found;
	}

	public static int totalAll() {
		int all = 0;
		for (SkinCatalog cat : values()) all += cat.totalSkins();
		return all;
	}

	//for tests, forgets every skin seen
	public static void reset() {
		for (SkinCatalog cat : values()) {
			for (Skin skin : cat.seen.keySet()) {
				cat.seen.put(skin, false);
				cat.applyCount.put(skin, 0);
			}
		}
	}

	private static final String CATALOG_SKINS   = "skincatalog_skins";
	private static final String CATALOG_SEEN    = "skincatalog_seen";
	private static final String CATALOG_APPLIES = "skincatalog_applies";

	public static void store(Bundle bundle) {
		ArrayList<String> names = new ArrayList<>();
		ArrayList<Boolean> seenList = new ArrayList<>();
		ArrayList<Integer> uses = new ArrayList<>();
		for (SkinCatalog cat : values()) {
			for (Skin skin : cat.skins()) {
				if (cat.seen.get(skin) || cat.applyCount.get(skin) > 0) {
					names.add(skin.name());
					seenList.add(cat.seen.get(skin));
					uses.add(cat.applyCount.get(skin));
				}
			}
		}
		String[] storeNames = new String[names.size()];
		boolean[] storeSeen = new boolean[names.size()];
		int[] storeUses = new int[names.size()];
		for (int i = 0; i < storeNames.length; i++) {
			storeNames[i] = names.get(i);
			storeSeen[i] = seenList.get(i);
			storeUses[i] = uses.get(i);
		}
		bundle.put(CATALOG_SKINS, storeNames);
		bundle.put(CATALOG_SEEN, storeSeen);
		bundle.put(CATALOG_APPLIES, storeUses);
	}

	public static void restore(Bundle bundle) {
		if (!bundle.contains(CATALOG_SKINS)) return;
		String[] names = bundle.getStringArray(CATALOG_SKINS);
		boolean[] seenArr = bundle.getBooleanArray(CATALOG_SEEN);
		int[] uses = bundle.getIntArray(CATALOG_APPLIES);
		for (int i = 0; i < names.length; i++) {
			Skin skin;
			try {
				skin = Skin.valueOf(names[i]);
			} catch (IllegalArgumentException e) {
				continue;
			}
			for (SkinCatalog cat : values()) {
				if (cat.seen.containsKey(skin)) {
					cat.seen.put(skin, seenArr[i]);
					cat.applyCount.put(skin, uses[i]);
				}
			}
		}
	}
}
