/*
 * Onyx's Pixel Dungeon
 * Hooks for content ported from Summoning Pixel Dungeon Reincarnated (TrashboxBobylev).
 * Every call site in a Shattered class is one line marked //summoning that calls a method here.
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;

public class SummoningHooks {

	//Summoning PD's two guide pages are found by play, the first summon and the first tier change
	public static void findGuidePage( String page ){
		if (Dungeon.sandbox) return;
		if (com.shatteredpixel.shatteredpixeldungeon.journal.Document.ADVENTURERS_GUIDE.findPage(page)) {
			try {
				com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.flashForDocument(
						com.shatteredpixel.shatteredpixeldungeon.journal.Document.ADVENTURERS_GUIDE, page);
			} catch (Exception e) {
				//no game scene in headless tests
			}
		}
	}

	//the Conjurer heals 5 health whenever she eats, as in the mod (not in the Hell mode)
	public static void onFoodEaten( Hero hero ){
		if (hero.heroClass == HeroClass.CONJURER && hero.HP < hero.HT && Dungeon.mode != Dungeon.GameMode.HELL) {
			hero.HP = Math.min( hero.HP + 5, hero.HT );
			if (hero.sprite != null) hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
		}
	}

}
