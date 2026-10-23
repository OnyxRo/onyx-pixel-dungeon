/*
 * Onyx's Pixel Dungeon
 *
 * Item, level and shop hooks of Re Blue Archive PD (https://github.com/Hoto-Mocha/Re-Blue-Archive-Pixel-Dungeon),
 * ported 2026-10-22. Shattered's classes call these with one line each. They only act for the mod's students,
 * their talents, buffs and items.
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

package com.shatteredpixel.shatteredpixeldungeon;


import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBATalents;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBABankRobber;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAConversation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBALittleAngry;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAQuestEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBARabbitSquadBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAShootAllBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBASnipe;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBASpreadShotBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBASupportDrone;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBASuperNovaCharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBASwiftMovement;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAAvantGardeKunBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RBAYuzuStatus;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBAHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mari.RBAMariTrinity;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.miracles.RBAMindBind;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.RBAGunSmithingTool;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RBANinjaCape;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.food.RBAHardTack;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBAGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.HG.RBAHG_T3;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.quick.RBAAssassinsKunai;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class RBAHooks {

	private static Hero hero(){
		return Dungeon.hero;
	}

	// ---- thrown weapons, Izuna's talents and her kunai ------------------------------------------------

	public static int missileStrReq(){
		return hero() != null && hero().heroClass == HeroClass.RBA_IZUNA ? -1 : 0;
	}

	public static int missileLevel( int lvl ){
		Hero h = hero();
		if (h != null && h.hasTalent(Talent.RBA_IZUNA_EX2_3) && h.belongings.getItem(RBAAssassinsKunai.class) != null) {
			int lvlBonus = Math.min(h.belongings.getItem(RBAAssassinsKunai.class).buffedLvl(), 2*h.pointsInTalent(Talent.RBA_IZUNA_EX2_3));
			lvl += Math.max(0, lvlBonus-lvl);
		}
		return lvl;
	}

	public static void onMissileThrown( Hero user ){
		if (user != null && user.buff(RBATalents.InstantThrowTracker.class) != null) {
			user.buff(RBATalents.InstantThrowTracker.class).onThrow();
		}
	}

	public static boolean instantThrow( Char user ){
		return user.buff(RBATalents.InstantThrowTracker.class) != null;
	}

	public static float missileUses( float usages ){
		Hero h = hero();
		if (h != null && h.hasTalent(Talent.RBA_IZUNA_T1_1)) {
			usages += 1+2*h.pointsInTalent(Talent.RBA_IZUNA_T1_1);
		}
		return usages;
	}

	/** Izuna's quick identification, true when the thrown weapon should be known at once. */
	public static boolean instantMissileID( Char attacker ){
		return attacker == hero() && hero().pointsInTalent(Talent.RBA_IZUNA_T1_2) == 2;
	}

	public static void afterMissileHit( MissileWeapon w, Char attacker, Char defender ){
		if (attacker == hero() && hero().hasTalent(Talent.RBA_IZUNA_T3_1) && !(w instanceof RBAGun.Bullet)) {
			Buff.append(attacker, TalismanOfForesight.CharAwareness.class, 5*hero().pointsInTalent(Talent.RBA_IZUNA_T3_1)).charID = defender.id();
		}
	}

	// ---- armor and weapons ---------------------------------------------------------------------------

	public static int armorMaxBonus( int tier ){
		Hero h = hero();
		if (tier <= 3 && h != null && h.hasTalent(Talent.RBA_HOSHINO_T1_3)) {
			return 1+h.pointsInTalent(Talent.RBA_HOSHINO_T1_3);
		}
		return 0;
	}

	/** Aris's talent keeps a glyph or an enchantment through every upgrade. */
	public static boolean keepsEnchant(){
		return hero() != null && hero().pointsInTalent(Talent.RBA_ARIS_T2_3) == 2;
	}

	public static boolean sealTransfersAnyGlyph(){
		return hero() != null && hero().hasTalent(Talent.RBA_ARIS_T2_3);
	}

	public static int sealShieldBonus(){
		return hero() != null ? hero().pointsInTalent(Talent.RBA_ARIS_T1_4) : 0;
	}

	public static float enchantFactor( Char attacker, float multi ){
		if (attacker instanceof Hero && ((Hero) attacker).belongings.attackingWeapon() instanceof RBAGun.Bullet) {
			multi *= ((RBAGun.Bullet) ((Hero) attacker).belongings.attackingWeapon()).whatEnchant().enchantFactor();
		}
		return multi;
	}

	/** Aris learns from every potion and scroll she identifies by using it. */
	public static void onConsumableIdentified(){
		Hero h = hero();
		if (h != null && h.hasTalent(Talent.RBA_ARIS_T1_3) && h.isAlive()) {
			int exp = 2+3*h.pointsInTalent(Talent.RBA_ARIS_T1_3);
			if (h.sprite != null) h.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(exp), FloatingText.EXPERIENCE);
			h.earnExp(exp, Talent.class);
		}
	}

	public static int ringBonus(){
		Hero h = hero();
		if (h != null && h.buff(RBASuperNovaCharge.class) != null && h.hasTalent(Talent.RBA_ARIS_EX2_3)) {
			float upgradePerCount;
			switch (h.pointsInTalent(Talent.RBA_ARIS_EX2_3)) {
				case 1: default: upgradePerCount = 100; break;
				case 2: upgradePerCount = 50; break;
				case 3: upgradePerCount = 30; break;
			}
			int upgrade = (int)(h.buff(RBASuperNovaCharge.class).count()/upgradePerCount);
			if (upgrade > 0) return Math.min(upgrade, 3);
		}
		return 0;
	}

	/** Mika fights barehanded with her strength, the Ring of Force's damage for her. */
	public static boolean mikaFists( Hero h ){
		return h != null && h.heroClass == HeroClass.RBA_MIKA;
	}

	public static int mikaMin( Hero h ){
		return Math.max(0, h.STR()-10);
	}

	public static int mikaMax( Hero h ){
		return 2*(h.STR()-10+h.lvl)+10;
	}

	// ---- gold and shops ------------------------------------------------------------------------------

	public static int goldGain( Hero h, int quantity ){
		return Math.round(quantity * RBAYuzuStatus.yuzuCreditMulti(h));
	}

	public static int sellValue( Hero h, int value ){
		return Math.round(value / RBAYuzuStatus.yuzuCreditMulti(h));
	}

	public static int shopPriceFactor(){
		return hero() != null ? hero().pointsInTalent(Talent.RBA_NONOMI_T2_2) : 0;
	}

	public static boolean unsellable( Item item ){
		return item instanceof RBAGun && ((RBAGun) item).checkKit() != null;
	}

	/** RBAGun students find a gunsmithing tool in every shop, as in the mod. */
	public static void addShopItems( ArrayList<Item> items ){
		if (RBAHero.usesGuns(hero())) items.add(new RBAGunSmithingTool());
	}

	// ---- the Tengu mask, a subclass's starting buff ------------------------------------------------------

	public static void onSubclassChosen( Hero h, HeroSubClass way ){
		if (way == HeroSubClass.RBA_SHOOT_ALL)       Buff.affect(h, RBAShootAllBuff.class);
		if (way == HeroSubClass.RBA_SPREAD_SHOT)     Buff.affect(h, RBASpreadShotBuff.class);
		if (way == HeroSubClass.RBA_RABBIT_SQUAD)    Buff.affect(h, RBARabbitSquadBuff.class);
		if (way == HeroSubClass.RBA_SWIFT_MOVEMENT)  Buff.affect(h, RBASwiftMovement.class);
		if (way == HeroSubClass.RBA_BANK_ROBBER)     Buff.affect(h, RBABankRobber.class);
		if (way == HeroSubClass.RBA_CONVERSATION)    Buff.affect(h, RBAConversation.class);
		if (way == HeroSubClass.RBA_TELESCOPE)       Buff.affect(h, RBASnipe.class);
		if (way == HeroSubClass.RBA_AVANT_GARDE_KUN) Buff.affect(h, RBAAvantGardeKunBuff.class);
		if (way == HeroSubClass.RBA_LITTLE_ANGRY)    Buff.affect(h, RBALittleAngry.class);
		if (way == HeroSubClass.RBA_DOUBLE_BARREL) {
			Item item = new RBAHG_T3();
			if (!item.identify().collect()) {
				Dungeon.level.drop(item, h.pos).sprite.drop();
			}
		}
	}

	// ---- levels ------------------------------------------------------------------------------------

	/** The mod's quest enemies, champions with a bounty, only in a student's run. */
	public static void rollForQuest( Mob m ){
		if (RBAHero.isStudent(hero())) RBAQuestEnemy.rollForQuest(m);
	}

	public static float respawnFactor(){
		return hero() != null && hero().subClass == HeroSubClass.RBA_SYMBOL_OF_PEACE ? 1.2f : 1f;
	}

	public static boolean seesThroughGrass( Char c ){
		return c instanceof Hero && ((Hero) c).subClass == HeroSubClass.RBA_CAMOUFLAGE;
	}

	public static float viewFactor( Hero h ){
		return 1f + 0.25f*h.pointsInTalent(Talent.RBA_MIYU_EX1_2);
	}

	public static int senseRange( Char c, int sense ){
		if (c.buff(RBAMariTrinity.TrinityBuff.class) != null){
			sense = Math.max( c.buff(RBAMariTrinity.TrinityBuff.class).mindVisionRange(), sense );
		}
		return sense;
	}

	public static int mindVisionRange( Hero h, int range ){
		if (h.hasTalent(Talent.RBA_HOSHINO_T2_3)) range = Math.max(range, 1+h.pointsInTalent(Talent.RBA_HOSHINO_T2_3));
		if (h.hasTalent(Talent.RBA_SHIROKO_T2_3) && Dungeon.level.map[h.pos] == Terrain.GRASS) range = Math.max(range, 1+2*h.pointsInTalent(Talent.RBA_SHIROKO_T2_3));
		if (h.hasTalent(Talent.RBA_MIYAKO_T2_3)) range = Math.max(range, 1+h.pointsInTalent(Talent.RBA_MIYAKO_T2_3));
		if (h.buff(RBASupportDrone.class) != null && h.hasTalent(Talent.RBA_MIYAKO_EX2_3)) {
			range += 1+Math.min(h.buff(RBASupportDrone.class).getDrone(), h.pointsInTalent(Talent.RBA_MIYAKO_EX2_3));
		}
		return range;
	}

	/** Cells the hero senses through Mari's Mind Bind and Miyu's scope. */
	public static void markMindVision( Char c, boolean[] heroMindFov ){
		for (RBAMindBind.MindBindBuff a : c.buffs(RBAMindBind.MindBindBuff.class)){
			Char ch = (Char) Actor.findById(a.charID);
			if (ch == null || !ch.isAlive() || Char.hasProp(ch, Char.Property.OBJECT)) continue;
			for (int i : PathFinder.NEIGHBOURS9) heroMindFov[ch.pos+i] = true;
		}
		for (RBASnipe.ScopedArea a : c.buffs(RBASnipe.ScopedArea.class)){
			if (Dungeon.depth != a.depth || Dungeon.branch != a.branch) continue;
			for (int i : PathFinder.NEIGHBOURS9) heroMindFov[a.pos+i] = true;
		}
	}

	/** Miyu's ration of hard tack, hidden on the floor like the Rogue's cached rations. */
	public static void dropHardTack( RegularLevel level, java.util.function.IntSupplier dropCell ){
		Hero h = hero();
		if (h == null || !h.hasTalent(Talent.RBA_MIYU_T1_1)) return;
		RBATalents.HardTackDropped dropped = Buff.affect(h, RBATalents.HardTackDropped.class);
		int targetFloor = (int)(2 + dropped.count());
		if (dropped.count() > 4) targetFloor++;
		if (Dungeon.depth >= targetFloor && dropped.count() < 2 + 2*h.pointsInTalent(Talent.RBA_MIYU_T1_1)){
			int cell;
			int tries = 100;
			boolean valid;
			do {
				cell = dropCell.getAsInt();
				valid = cell != -1
						&& level.map[cell] != Terrain.EMPTY_SP
						&& level.map[cell] != Terrain.WATER
						&& level.map[cell] != Terrain.PEDESTAL;
			} while (tries-- > 0 && !valid);
			if (valid) {
				if (level.map[cell] == Terrain.HIGH_GRASS || level.map[cell] == Terrain.FURROWED_GRASS) {
					level.map[cell] = Terrain.GRASS;
					level.losBlocking[cell] = false;
				}
				level.drop(new RBAHardTack(), cell).type = Heap.Type.CHEST;
				dropped.countUp(2);
			}
		}
	}

	// ---- buffs -------------------------------------------------------------------------------------

	public static void onInvisible( Char target ){
		if (target instanceof Hero && ((Hero) target).hasTalent(Talent.RBA_IZUNA_T1_4)){
			Buff.affect(target, RBATalents.ShadowHideTracker.class);
		}
	}

	public static void onDispel( Char ch ){
		RBANinjaCape.capeStealth capeBuff = ch.buff( RBANinjaCape.capeStealth.class );
		if (capeBuff != null) {
			capeBuff.dispel();
		}
	}

	public static boolean walksGrassLikeHuntress( Char ch ){
		return ch instanceof Hero && ((Hero) ch).heroClass == HeroClass.RBA_MIYU;
	}

	// ---- alchemy, the gunsmithing tool and the special guns, for students who use guns ---------------

	private static final com.shatteredpixel.shatteredpixeldungeon.items.Recipe TOOL_RECIPE =
			new com.shatteredpixel.shatteredpixeldungeon.items.RBAGunSmithingTool.Recipe();

	public static void addRecipes( ArrayList<Item> ingredients, ArrayList<com.shatteredpixel.shatteredpixeldungeon.items.Recipe> result ){
		if (!RBAHero.usesGuns(hero())) return;
		if (ingredients.size() == 1 && TOOL_RECIPE.testIngredients(ingredients)) result.add(0, TOOL_RECIPE);
		if (ingredients.size() == 2){
			for (com.shatteredpixel.shatteredpixeldungeon.items.Recipe recipe : com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.RBASpecialGun.gunRecipes){
				if (recipe.testIngredients(ingredients)) result.add(recipe);
			}
		}
	}

	// ---- saving ------------------------------------------------------------------------------------

	public static void reset(){
		RBAShims.reset();
		java.util.Arrays.fill(com.shatteredpixel.shatteredpixeldungeon.actors.hero.RBATalents.compassed, false);
	}
}
