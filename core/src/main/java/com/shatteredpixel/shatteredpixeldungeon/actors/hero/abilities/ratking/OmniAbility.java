/*
 * Onyx's Pixel Dungeon
 * The Rat King's Omni-Ability from DERanged PD (from Rat King PD 2 by zrp200), GPL-3.0. Rewritten for this game
 * (2026-10-22). It holds one armor ability of Shattered's or Re-ARranged's heroes, or the King's own, and rolls a
 * new one each time the held one is used. The tier 4 talent points move over to the new ability's talents.
 * Trinity is left out of the pool, its code asks the hero's armor ability to be Trinity itself.
 */
package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ratking;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.Trinity;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class OmniAbility extends ArmorAbility {

	private ArmorAbility armorAbility = null;

	public ArmorAbility activeAbility(){
		return armorAbility;
	}

	@Override
	public void use( ClassArmor armor, Hero hero ) {
		if (armorAbility == null){
			roll(hero);
			return;
		}
		final ArmorAbility held = armorAbility;
		if (held.targetingPrompt() == null){
			fire(held, armor, hero, hero.pos);
		} else {
			GameScene.selectCell(new CellSelector.Listener() {
				@Override
				public void onSelect(Integer cell) {
					fire(held, armor, hero, cell);
				}

				@Override
				public String prompt() {
					return held.targetingPrompt();
				}
			});
		}
	}

	private void fire( ArmorAbility held, ClassArmor armor, Hero hero, Integer cell ){
		float before = armor.charge;
		held.activateFor(armor, hero, cell);
		//a new ability once the held one has been used
		if (armor.charge < before && armorAbility == held) roll(hero);
	}

	@Override
	protected void activate( ClassArmor armor, Hero hero, Integer target ) {
		//use() does the work
	}

	private static ArrayList<ArmorAbility> pool(){
		ArrayList<ArmorAbility> pool = new ArrayList<>();
		for (HeroClass cls : HeroClass.values()){
			if (cls != HeroClass.RAT_KING && !Dungeon_shares(cls)) continue;
			for (ArmorAbility a : cls.armorAbilities()){
				if (a instanceof OmniAbility || a instanceof Trinity) continue;
				pool.add(a);
			}
		}
		return pool;
	}

	private static boolean Dungeon_shares( HeroClass cls ){
		return com.shatteredpixel.shatteredpixeldungeon.actors.hero.DerangedKing.sharesClass(cls);
	}

	public void roll( Hero hero ){
		ArrayList<ArmorAbility> pool = pool();
		//the Rat King's own abilities show up less
		if (Random.Int(8) == 0){
			for (Iterator<ArmorAbility> i = pool.iterator(); i.hasNext();){
				if (i.next() instanceof Ratmogrify) i.remove();
			}
		}
		if (armorAbility != null){
			for (Iterator<ArmorAbility> i = pool.iterator(); i.hasNext();){
				if (i.next().getClass() == armorAbility.getClass()) i.remove();
			}
		}
		armorAbility = Random.element(pool);
		GLog.p( Messages.get(this, "rolled", armorAbility.name()) );
		Item.updateQuickslot();
		if (hero.talents.size() >= 4){
			hero.talents.set(3, transferTalents(hero, armorAbility));
		}
	}

	//the points put in tier 4 move over to the new ability's talents in order
	private static LinkedHashMap<Talent, Integer> transferTalents( Hero hero, ArmorAbility ability ){
		LinkedHashMap<Talent, Integer> talents = new LinkedHashMap<>();
		Iterator<Integer> points = hero.talents.get(3).values().iterator();
		for (Talent t : ability.talents()){
			talents.put(t, points.hasNext() ? points.next() : 0);
		}
		return talents;
	}

	@Override
	public String targetingPrompt() {
		return armorAbility != null ? armorAbility.targetingPrompt() : null;
	}

	@Override
	public boolean useTargeting() {
		return armorAbility != null ? armorAbility.useTargeting() : super.useTargeting();
	}

	@Override
	public float chargeUse( Hero hero ) {
		return armorAbility != null ? armorAbility.chargeUse(hero) : 0;
	}

	@Override
	public String desc() {
		String desc = Messages.get(this, "desc");
		if (armorAbility != null){
			desc += "\n\n" + Messages.get(this, "ability_desc") + "\n" + armorAbility.desc();
		}
		return desc;
	}

	@Override
	public String shortDesc() {
		String desc = super.shortDesc();
		if (armorAbility != null){
			desc += "\n\n" + Messages.get(this, "ability_desc") + "\n" + armorAbility.shortDesc();
		}
		return desc;
	}

	@Override
	public int icon() {
		return armorAbility != null ? armorAbility.icon() : HeroIcon.RATMOGRIFY;
	}

	@Override
	public Talent[] talents() {
		return armorAbility != null ? armorAbility.talents() : new Talent[0];
	}

	private static final String ABILITY = "armorAbility";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		if (armorAbility != null) bundle.put(ABILITY, armorAbility);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(ABILITY)) armorAbility = (ArmorAbility) bundle.get(ABILITY);
	}
}
