/*
 * Onyx's Pixel Dungeon
 * The shop owner from Transformation PD (dongyusangyu). In the mod he waits in the city boss arena for a hero
 * coming back up with the Amulet and then turns into the Great Shopper. Here he does the same wherever he stands,
 * and in a sandbox run he does not need the Amulet.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GreatShoper;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Callback;

public class ShopBoss extends NPC {

	{
		spriteClass = ShopkeeperSprite.class;
		properties.add(Property.IMMOVABLE);
	}

	@Override
	public int defenseSkill( Char enemy ) {
		return INFINITE_EVASION;
	}

	@Override
	public void damage( int dmg, Object src ) {
		//cannot be harmed
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
	public boolean interact(Char c) {
		sprite.turnTo( pos, c.pos );
		if (c != Dungeon.hero) return true;
		if (Dungeon.hero.belongings.getItem( Amulet.class ) == null && !Dungeon.sandbox){
			yell(Messages.get(this, "hello", Messages.titleCase(Dungeon.hero.name())));
			return true;
		}
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.show(new WndOptions(new ItemSprite(new Amulet()),
						Messages.titleCase(name()),
						Messages.get(ShopBoss.class, "warning"),
						Messages.get(ShopBoss.class, "yes"),
						Messages.get(ShopBoss.class, "no")) {
					@Override
					protected void onSelect( int index ) {
						if (index == 0) begin();
					}
				});
			}
		});
		return true;
	}

	private void begin(){
		int at = pos;
		die(null);
		GreatShoper boss = new GreatShoper();
		boss.pos = at;
		GameScene.add(boss);
		BossHealthBar.assignBoss(boss);
		Dungeon.hero.interrupt();
		Music.INSTANCE.play(Assets.Music.BACKS1_2, true);
	}
}
