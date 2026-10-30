package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Regrowth;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.VenomGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Displacing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

//Easier Sprouted Pixel Dungeon's Unstable Wand (RandomWand). Each zap does one random effect.
//The mod only ever rolled its common effects, the rest was left commented out, so only those are here.
public class UnstableWand extends Wand {

	{
		image = ItemSpriteSheet.UNSTABLE_WAND;
		collisionProperties = Ballistica.PROJECTILE;
	}

	@Override
	public void onZap(Ballistica bolt) {
		int cell = bolt.collisionPos;
		Char target = Actor.findChar(cell);
		switch (Random.Int(3)) {
			case 0: default:
				if (target != null) {
					if (Random.Int(2) == 0) Buff.affect(target, Burning.class).reignite(target);
					else Buff.affect(target, Frost.class, Frost.DURATION * Random.Float(3f, 5f));
					wandProc(target, chargesPerCast());
				}
				break;
			case 1:
				int c = Dungeon.level.map[cell];
				if (c == Terrain.EMPTY || c == Terrain.EMBERS || c == Terrain.EMPTY_DECO
						|| c == Terrain.GRASS || c == Terrain.HIGH_GRASS) {
					GameScene.add(Blob.seed(cell, 30, Regrowth.class));
				}
				break;
			case 2:
				switch (Random.Int(5)) {
					case 0: GameScene.add(Blob.seed(cell, 800, ConfusionGas.class)); break;
					case 1: GameScene.add(Blob.seed(cell, 500, ToxicGas.class)); break;
					case 2: GameScene.add(Blob.seed(cell, 200, ParalyticGas.class)); break;
					case 3:
						Blob venom = Blob.seed(cell, 40 + 20 * level(), VenomGas.class);
						((VenomGas) venom).setStrength(level() + 1);
						GameScene.add(venom);
						break;
					case 4: default:
						Blob gas = Blob.seed(cell, 40, CorrosiveGas.class);
						((CorrosiveGas) gas).setStrength(level() + 1);
						GameScene.add(gas);
						break;
				}
				break;
		}
	}

	@Override
	public void fx(Ballistica bolt, Callback callback) {
		MagicMissile.boltFromChar(curUser.sprite.parent, MagicMissile.RAINBOW, curUser.sprite, bolt.collisionPos, callback);
		Sample.INSTANCE.play(Assets.Sounds.ZAP);
	}

	//ESPD's battlemage proc was the Instability enchantment, this game's nearest is the Displacing curse
	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		if (Random.Int(4) == 0) new Displacing().proc(staff, attacker, defender, damage);
	}
}
