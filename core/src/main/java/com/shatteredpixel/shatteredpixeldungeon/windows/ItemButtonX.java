package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;
import java.util.ArrayList;

//magicling, the item button from Magic Ling's blacksmith window, the pac-man start window uses it
public class ItemButtonX extends Component {

	protected NinePatch bg;
	protected ItemSlot slot;

	public Item item = null;

	@Override
	protected void createChildren() {
		super.createChildren();

		bg = Chrome.get( Chrome.Type.RED_BUTTON);
		add( bg );

		slot = new ItemSlot() {
			@Override
			protected void onPointerDown() {
				bg.brightness( 1.2f );
				Sample.INSTANCE.play( Assets.Sounds.CLICK );
			}
			@Override
			protected void onPointerUp() {
				bg.resetColor();
			}
			@Override
			protected void onClick() {
				ItemButtonX.this.onClick();
			}
		};
		slot.enable(true);
		add( slot );
	}

	protected void onClick() {}

	@Override
	protected void layout() {
		super.layout();

		bg.x = x;
		bg.y = y;
		bg.size( width, height );

		slot.setRect( x + 2, y + 2, width - 4, height - 4 );
	}

	public void item( Item item ) {
		slot.item( this.item = item );
	}

	public void clear(){
		slot.clear();
	}
}
