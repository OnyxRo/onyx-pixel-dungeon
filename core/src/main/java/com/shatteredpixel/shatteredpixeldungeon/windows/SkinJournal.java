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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Skin;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.SkinPaint;
import com.shatteredpixel.shatteredpixeldungeon.journal.SkinCatalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingGridPane;
import com.watabou.noosa.Image;
import com.watabou.utils.Reflection;

//The skins page of the journal catalog, Shittened PD's journal tab. Opened by the fifth catalog button.
public class SkinJournal {

	public static void fill(ScrollingGridPane grid) {
		grid.addHeader("_" + Messages.get(SkinJournal.class, "title") + "_ ("
				+ SkinCatalog.totalFound() + "/" + SkinCatalog.totalAll() + ")", 9, true);
		for (SkinCatalog cat : SkinCatalog.values()) {
			grid.addHeader("_" + Messages.titleCase(cat.title()) + "_ (" + cat.totalSeen() + "/" + cat.totalSkins() + "):");
			for (Skin skin : cat.skins()) {
				add(grid, skin);
			}
		}
	}

	private static void add(ScrollingGridPane grid, Skin skin) {
		Item item = (Item) Reflection.newInstance(skin.skinFor());
		if (item == null) return;
		SkinPaint.change(item, skin);

		final boolean seen = SkinCatalog.isSeen(skin);
		ItemSprite sprite;
		String title;
		String desc;
		if (!seen) {
			sprite = new ItemSprite(skin.spriteOutline(), null);
			sprite.lightness(0);
			title = "???";
			desc = Messages.get(SkinJournal.class, "not_seen", skin.rarityName());
		} else {
			sprite = new ItemSprite(item.image, item.glowing());
			title = Messages.titleCase(skin.fullSkinName());
			if (skin.rarityTier() == 0) {
				desc = Messages.get(SkinJournal.class, "default_skin");
			} else {
				desc = Messages.get(SkinJournal.class, "rarity", skin.rarityName(), item.trueName());
				if (!skin.desc().isEmpty()) desc += "\n\n\"" + skin.desc() + "\"";
			}
			if (SkinCatalog.applyCount(skin) > 1) {
				desc += "\n\n" + Messages.get(SkinJournal.class, "apply_count", SkinCatalog.applyCount(skin));
			}
		}

		final String finalTitle = title;
		final String finalDesc = desc;
		ScrollingGridPane.GridItem gridItem = new ScrollingGridPane.GridItem(sprite) {
			@Override
			public boolean onClick(float x, float y) {
				if (inside(x, y)) {
					Image copy = new ItemSprite();
					copy.copy(icon);
					if (ShatteredPixelDungeon.scene() instanceof GameScene) {
						GameScene.show(new WndJournalItem(copy, finalTitle, finalDesc));
					} else {
						ShatteredPixelDungeon.scene().addToFront(new WndJournalItem(copy, finalTitle, finalDesc));
					}
					return true;
				}
				return false;
			}
		};
		if (!seen) {
			gridItem.hardLightBG(2f, 1f, 2f);
		} else if (skin.rarityTier() > 0) {
			//the frame takes the rarity's colour, grey, blue, purple, gold or red
			int c = skin.rarityColor();
			gridItem.hardLightBG(1f + ((c >> 16) & 0xFF) / 255f, 1f + ((c >> 8) & 0xFF) / 255f, 1f + (c & 0xFF) / 255f);
		}
		grid.addItem(gridItem);
	}
}
