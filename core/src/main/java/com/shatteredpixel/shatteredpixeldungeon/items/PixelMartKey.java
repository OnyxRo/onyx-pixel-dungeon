/*
 * Buttered Pixel Dungeon's Pixel Mart Key (its ArenaShopKey). Dropped by each boss wave of the Arena
 * game mode, it takes the hero once to a Pixel Mart shop floor, whose stairs lead back to the arena.
 */
package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.ButteredArena;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.levels.ButteredArenaLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class PixelMartKey extends Item {

	public static final String AC_ACTIVATE = "ACTIVATE";

	{
		image = ItemSpriteSheet.GOLDEN_KEY;
		defaultAction = AC_ACTIVATE;
		unique = true;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_ACTIVATE );
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute( hero, action );
		if (!action.equals( AC_ACTIVATE )) return;

		if (!ButteredArena.on()) {
			GLog.n( Messages.get(this, "vanish") );
			detach( hero.belongings.backpack );
			return;
		}
		for (int n : PathFinder.NEIGHBOURS4) {
			if (Actor.findChar( hero.pos + n ) != null) {
				GLog.w( Messages.get(this, "creatures") );
				return;
			}
		}
		if (!(Dungeon.level instanceof ButteredArenaLevel) || ((ButteredArenaLevel) Dungeon.level).bossWave()) {
			GLog.n( Messages.get(this, "fail") );
			return;
		}

		int depth = ButteredArena.shopDepth( ((ButteredArenaLevel) Dungeon.level).wave );
		//the mod built the shop fresh on every visit
		Dungeon.generatedLevels.remove( (Integer) depth );
		detach( hero.belongings.backpack );
		InterlevelScene.curTransition = new LevelTransition( Dungeon.level, hero.pos, LevelTransition.Type.REGULAR_EXIT,
				depth, 0, LevelTransition.Type.REGULAR_ENTRANCE );
		InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
		Game.switchScene( InterlevelScene.class );
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
	public ItemSprite.Glowing glowing() {
		return new ItemSprite.Glowing( 0xFFFF00 );
	}
}
