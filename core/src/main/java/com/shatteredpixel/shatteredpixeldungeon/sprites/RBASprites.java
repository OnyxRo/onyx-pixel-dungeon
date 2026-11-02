/*
 * Onyx's Pixel Dungeon
 *
 * Sprite members Re Blue Archive PD (https://github.com/Hoto-Mocha/Re-Blue-Archive-Pixel-Dungeon) added to
 * Shattered's CharSprite and HeroSprite, kept here so those classes stay as they were. Ported 2026-10-22.
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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mari.RBAMariTrinity;
import com.shatteredpixel.shatteredpixeldungeon.effects.RBARGBBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

import java.util.WeakHashMap;

public class RBASprites {

	/** CharSprite.BLUE in the mod, the colour of its blue status text. */
	public static final int BLUE = 0x5454FF;

	/** The students' sprites are 17 pixels tall, Shattered's heroes 15. */
	public static final int STUDENT_HEIGHT = 17;

	// ---- the colour cycle Mari's Trinity puts on a character ---------------------------------------

	private static final WeakHashMap<CharSprite, RBARGBBlock> rgb = new WeakHashMap<>();

	public static void rgb( CharSprite sprite, RBAMariTrinity.TrinityBuff buff ){
		RBARGBBlock old = rgb.remove(sprite);
		if (old != null) old.darken();
		rgb.put(sprite, RBARGBBlock.lighten(sprite, buff));
	}

	public static void clearRgb( CharSprite sprite ){
		RBARGBBlock old = rgb.remove(sprite);
		if (old != null) old.darken();
	}

	// ---- Yuzu riding Avant-Garde Kun -----------------------------------------------------------------

	public static void updateSprite( HeroSprite sprite ){
		sprite.texture( Dungeon.hero.heroClass.spritesheet() );
		sprite.updateArmor();
		sprite.place( Dungeon.hero.pos );
	}

	public static void replaceAvantGardeSprite( HeroSprite sprite ){
		sprite.texture( Assets.Sprites.RBA_AVANT_GARDE_KUN );
		TextureFilm film = new TextureFilm( sprite.texture, 20, 20 );

		sprite.idle = new MovieClip.Animation( 10, true );
		sprite.idle.frames( film, 0, 1 );
		sprite.run = new MovieClip.Animation( 10, true );
		sprite.run.frames( film, 2, 3 );
		sprite.die = new MovieClip.Animation( 3, false );
		sprite.die.frames( film, 1 );
		sprite.attack = new MovieClip.Animation( 15, false );
		sprite.attack.frames( film, 4, 5, 6, 0 );
		sprite.zap = new MovieClip.Animation( 15, false );
		sprite.zap.frames( film, 7, 8, 7, 8, 7, 8, 9, 9, 10, 10, 9, 9, 10, 10, 0 );
		sprite.operate = sprite.die.clone();
		sprite.fly = sprite.die.clone();
		sprite.read = sprite.die.clone();

		if (Dungeon.hero.isAlive()) sprite.idle();
		else sprite.die();
		sprite.place( Dungeon.hero.pos );
	}

	// ---- missile flight, the mod's changes to MissileSprite (added 2026-11-02) --------------------

	/** Bullets, lances and shots fly straight instead of spinning. */
	public static void angularSpeeds( java.util.HashMap<Class<? extends com.shatteredpixel.shatteredpixeldungeon.items.Item>, Integer> speeds ){
		speeds.put(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun.Bullet.class, 0);
		speeds.put(RBAYukinoSprite.YukinoShot.class, 0);
		speeds.put(RBANikoSprite.NikoShot.class, 0);
		speeds.put(RBAOtogiSprite.OtogiShot.class, 0);
		speeds.put(com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.shiroko.RBAPenetrationShot.PenetrationBullet.class, 0);
		speeds.put(RBAHologramSprite.HologramShot.class, 0);
		speeds.put(com.shatteredpixel.shatteredpixeldungeon.actors.hero.console.fantasy.RBAIceLance.IceLanceVFX.class, 0);
		speeds.put(com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.izuna.RBAThrowingThunder.PotOThunder.class, 0);
		speeds.put(com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles.RBALightBullet.LightBulletItem.class, 0);
		speeds.put(com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles.RBASilverBullet.SilverBulletItem.class, 0);
		speeds.put(com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles.RBAHolyBullet.HolyBulletItem.class, 0);
		speeds.put(com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles.RBAThunderLance.ThunderItem.class, 0);
	}

	/** How much faster than a thrown item each of the mod's missiles flies, 1 for everything else. */
	public static float missileSpeed( com.shatteredpixel.shatteredpixeldungeon.items.Item item ){
		if (item instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun.Bullet){
			return ((com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun.Bullet) item).projectileSpeed();
		}
		if (item instanceof RBAYukinoSprite.YukinoShot
				|| item instanceof RBANikoSprite.NikoShot
				|| item instanceof RBAOtogiSprite.OtogiShot
				|| item instanceof com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.shiroko.RBAPenetrationShot.PenetrationBullet
				|| item instanceof RBAHologramSprite.HologramShot
				|| item instanceof com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles.RBALightBullet.LightBulletItem
				|| item instanceof com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles.RBASilverBullet.SilverBulletItem
				|| item instanceof com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles.RBAHolyBullet.HolyBulletItem){
			return 3f;
		}
		if (item instanceof com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles.RBAThunderLance.ThunderItem){
			return 1.5f;
		}
		return 1f;
	}

	// ---- small icons the mod drew on its own icon sheet (interfaces/rebluearchive/icons.png) ----------

	public static Image icon( int x, int y, int w, int h ){
		Image icon = new Image( Assets.Interfaces.RBA_ICONS );
		icon.frame( icon.texture.uvRectBySize( x, y, w, h ) );
		return icon;
	}
}
