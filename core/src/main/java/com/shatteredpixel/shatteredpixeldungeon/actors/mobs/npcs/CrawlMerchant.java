package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndCrawlShop;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

/**
 * The boss crawl's merchant, standing by the glowing pool in the town. He sells every item in the game
 * and never runs out, see WndCrawlShop.
 */
public class CrawlMerchant extends NPC {

	{
		spriteClass = ShopkeeperSprite.class;
		properties.add(Property.IMMOVABLE);
	}

	@Override
	protected boolean act() {
		throwItem();
		return super.act();
	}

	@Override
	public int defenseSkill( Char enemy ) {
		return INFINITE_EVASION;
	}

	@Override
	public void damage( int dmg, Object src ) {
	}

	@Override
	public boolean add( Buff buff ) {
		return false;
	}

	@Override
	public boolean reset() {
		return true;
	}

	@Override
	public boolean interact( Char c ) {
		if (c != com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero) return true;
		Game.runOnRenderThread( new Callback() {
			@Override
			public void call() {
				GameScene.show( new WndCrawlShop() );
			}
		} );
		return true;
	}
}
