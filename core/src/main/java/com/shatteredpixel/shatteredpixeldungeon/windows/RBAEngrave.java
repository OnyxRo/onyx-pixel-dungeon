/*
 * Onyx's Pixel Dungeon
 *
 * Re Blue Archive PD's engraving at the blacksmith (Hoto-Mocha/Re-Blue-Archive-Pixel-Dungeon). For 2000 favor
 * the blacksmith carves Red Winter's mark onto an identified, uncursed gun, which then fires an extra shot
 * (RBAGun.InscribeMod). The button only shows for a student who fights with guns. Added 2026-11-02.
 * The mod opened a window with an item slot first, here the bag opens straight away.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBAHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class RBAEngrave {

	public static final int COST = 2000;

	//WndBlacksmith, before the cash out button
	public static void addButton( final WndBlacksmith wnd, ArrayList<RedButton> buttons ){
		if (!RBAHero.usesGuns(Dungeon.hero)) return;
		RedButton engrave = new RedButton(Messages.get(WndBlacksmith.class, "rba_engrave", COST), 6){
			@Override
			protected void onClick() {
				GameScene.selectItem(new WndBag.ItemSelector() {
					@Override
					public String textPrompt() {
						return Messages.get(WndBlacksmith.class, "rba_engrave_prompt");
					}

					@Override
					public Class<? extends Bag> preferredBag() {
						return Belongings.Backpack.class;
					}

					@Override
					public boolean itemSelectable( Item item ) {
						return item instanceof RBAGun && item.isIdentified() && !item.cursed
								&& ((RBAGun) item).inscribeMod == RBAGun.InscribeMod.NORMAL;
					}

					@Override
					public void onSelect( Item item ) {
						if (!(item instanceof RBAGun) || Blacksmith.Quest.favor < COST) return;
						Sample.INSTANCE.play( Assets.Sounds.EVOKE );
						ScrollOfUpgrade.upgrade( Dungeon.hero );
						Item.evoke( Dungeon.hero );
						((RBAGun) item).inscribeMod = RBAGun.InscribeMod.INSCRIBED;
						Item.updateQuickslot();
						Blacksmith.Quest.favor -= COST;
						if (!Blacksmith.Quest.rewardsAvailable()){
							Notes.remove( Notes.Landmark.TROLL );
						}
						wnd.hide();
					}
				});
			}
		};
		engrave.enable(Blacksmith.Quest.favor >= COST);
		buttons.add(engrave);
	}
}
