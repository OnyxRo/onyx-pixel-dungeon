/*
 * Onyx's Pixel Dungeon
 * The metamorphosis prism from Transformation PD (dongyusangyu). It swaps a boss talent the hero already has for
 * another talent of the same boss.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationBossTalents;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoTalent;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MetamorphosisPrism extends Spell {

	{
		image = ItemSpriteSheet.METAMORPH_PRISM;
		talentChance = 0;
	}

	@Override
	protected void onCast(final Hero hero) {
		final ArrayList<Talent> owned = new ArrayList<>();
		for (LinkedHashMap<Talent, Integer> tier : hero.talents){
			for (Talent t : tier.keySet()) if (Talent.isBossTalent(t)) owned.add(t);
		}
		//the subclass talents too, the prism trades them for the subclass's other talents
		for (Talent t : com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.subclassGroup(hero)){
			for (LinkedHashMap<Talent, Integer> tier : hero.talents) if (tier.containsKey(t) && !owned.contains(t)) owned.add(t);
		}
		if (owned.isEmpty()){
			GLog.w(Messages.get(this, "no_boss_talent"));
			return;
		}
		String[] names = new String[owned.size()];
		for (int i = 0; i < owned.size(); i++) names[i] = Messages.titleCase(owned.get(i).title());
		GameScene.show(new WndOptions(new TalentIcon(owned.get(0)), Messages.titleCase(name()), Messages.get(this, "choose_old"), names){
			@Override
			protected void onSelect(int index){
				chooseNew(hero, owned.get(index));
			}
		});
	}

	private void chooseNew(final Hero hero, final Talent old){
		final ArrayList<Talent> options = new ArrayList<>();
		ArrayList<Talent> group = new ArrayList<>();
		java.util.Collections.addAll(group, TransformationBossTalents.groupOf(old));
		if (group.isEmpty()) group.addAll(com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.subclassGroup(hero));
		for (Talent t : group){
			boolean have = false;
			for (LinkedHashMap<Talent, Integer> tier : hero.talents) have |= tier.containsKey(t);
			if (!have) options.add(t);
		}
		if (options.isEmpty()){
			GLog.w(Messages.get(this, "no_other"));
			return;
		}
		String[] names = new String[options.size()];
		for (int i = 0; i < options.size(); i++) names[i] = Messages.titleCase(options.get(i).title());
		GameScene.show(new WndOptions(new TalentIcon(old), Messages.titleCase(name()), Messages.get(this, "choose_new"), names){
			@Override
			protected void onSelect(int index){
				if (TransformationBossTalents.groupOf(old).length > 0){
					TransformationBossTalents.replaceBossTalent(hero, old, options.get(index));
				} else {
					com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.replaceTalent(hero, old, options.get(index));
				}
				detach(hero.belongings.backpack);
				updateQuickslot();
				hero.spendAndNext(1f);
				ScrollOfMetamorphosis.onMetamorph(old, options.get(index));
			}
			@Override
			protected boolean hasInfo(int index){ return true; }
			@Override
			protected void onInfo(int index){
				GameScene.show(new WndInfoTalent(options.get(index), 0, null));
			}
		});
	}

	@Override
	public int value() {
		return 40 * quantity;
	}

	@Override
	public int energyVal() {
		return 0;
	}

	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {
		{
			inputs = new Class[]{ScrollOfMetamorphosis.class};
			inQuantity = new int[]{3};
			cost = 3;
			output = MetamorphosisPrism.class;
			outQuantity = 1;
		}
	}
}
