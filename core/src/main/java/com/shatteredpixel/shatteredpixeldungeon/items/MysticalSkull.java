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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

//reremixed, the Necromancer's skull. Set into a melee weapon, every kill with that weapon gives him a point of mana.
//It moves between weapons like the Warrior's broken seal, without carrying an upgrade (the mod's did).
public class MysticalSkull extends Item {

	public static final String AC_EMBED = "EMBED";
	public static final String AC_DETACH = "DETACH_SKULL";

	{
		image = ItemSpriteSheet.RAT_SKULL;
		cursedKnown = levelKnown = true;
		unique = true;
		bones = false;
		defaultAction = AC_EMBED;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_EMBED );
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute( hero, action );
		if (action.equals( AC_EMBED )) {
			curItem = this;
			GameScene.selectItem( weaponSelector );
		}
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	protected static WndBag.ItemSelector weaponSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get( MysticalSkull.class, "prompt" );
		}

		@Override
		public boolean itemSelectable( Item item ) {
			return item instanceof MeleeWeapon && ((MeleeWeapon) item).skull == null;
		}

		@Override
		public void onSelect( Item item ) {
			if (!(item instanceof MeleeWeapon) || !(curItem instanceof MysticalSkull)) return;
			MeleeWeapon weapon = (MeleeWeapon) item;
			if (weapon.cursed && weapon.cursedKnown) {
				GLog.w( Messages.get( MysticalSkull.class, "cursed_weapon" ) );
				return;
			}
			GLog.p( Messages.get( MysticalSkull.class, "embedded" ) );
			Dungeon.hero.sprite.operate( Dungeon.hero.pos );
			Sample.INSTANCE.play( Assets.Sounds.UNLOCK );
			weapon.skull = (MysticalSkull) curItem;
			curItem.detach( Dungeon.hero.belongings.backpack );
			weapon.updateQuickslot();
		}
	};

	//hooks from MeleeWeapon, so each one there is a single line

	public static void store( MeleeWeapon weapon, Bundle bundle ) {
		if (weapon.skull != null) bundle.put( "rr_skull", weapon.skull );
	}

	public static void restore( MeleeWeapon weapon, Bundle bundle ) {
		if (bundle.contains( "rr_skull" )) weapon.skull = (MysticalSkull) bundle.get( "rr_skull" );
	}

	public static void addActions( MeleeWeapon weapon, ArrayList<String> actions ) {
		if (weapon.skull != null) actions.add( AC_DETACH );
	}

	public static void execute( MeleeWeapon weapon, Hero hero, String action ) {
		if (!action.equals( AC_DETACH ) || weapon.skull == null) return;
		GLog.i( Messages.get( MysticalSkull.class, "detached" ) );
		if (hero.sprite != null) hero.sprite.operate( hero.pos );
		Sample.INSTANCE.play( Assets.Sounds.UNLOCK );
		if (!weapon.skull.collect()) Dungeon.level.drop( weapon.skull, hero.pos );
		weapon.skull = null;
		weapon.updateQuickslot();
	}

	public static String info( MeleeWeapon weapon ) {
		return weapon.skull == null ? "" : "\n\n" + Messages.get( MysticalSkull.class, "in_weapon" );
	}
}
