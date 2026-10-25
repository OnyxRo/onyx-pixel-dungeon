/*
 * Too Cruel Pixel Dungeon Trials (GPL-3.0), ported into Onyx's Pixel Dungeon.
 * Based on Shattered Pixel Dungeon by Evan Debenham.
 */

package com.shatteredpixel.shatteredpixeldungeon.items.toocruel;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

//Protected Goods, a note left where a protected item would have been
public class IOU extends Item {

	{
		image = ItemSpriteSheet.GUIDE_PAGE;
	}

	private String itemName = "";

	public IOU() {
	}

	public IOU( Item item ) {
		itemName = item.name();
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public String name() {
		return Messages.get( this, "name", itemName );
	}

	@Override
	public String desc() {
		return Messages.get( this, "desc", Messages.titleCase( itemName ) );
	}

	@Override
	public boolean doPickUp( Hero hero, int pos ) {
		GLog.w( desc() );
		Sample.INSTANCE.play( Assets.Sounds.ITEM );
		hero.spendAndNext( TIME_TO_PICK_UP );
		return true;
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( "item_name", itemName );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		itemName = bundle.getString( "item_name" );
	}
}
