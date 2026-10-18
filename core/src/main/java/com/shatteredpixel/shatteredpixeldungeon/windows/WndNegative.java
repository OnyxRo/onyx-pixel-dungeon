/*
 * Onyx's Pixel Dungeon
 * The Cursed talents challenge from Transformation PD (dongyusangyu). On floors 2, 6, 11 and 21 the hero picks one
 * of three negative talents. Points spent on it weaken the curse, two points cancel it.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentsPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class WndNegative extends Window {

	private static final int WIDTH = 120;
	private static final int[] DEPTHS = {2, 6, 11, 21};
	private static final int PER_PICK = 3;

	public static WndNegative INSTANCE;

	//how many of the four picks this run has made
	public static class NegativeStages extends CounterBuff { { revivePersists = true; } }

	private final int tier;
	private boolean chosen = false;

	public static void showIfNeeded(){
		Hero hero = Dungeon.hero;
		if (!Dungeon.isChallenged(Challenges.CURSED_TALENTS) || hero == null || !hero.isAlive() || INSTANCE != null) return;
		NegativeStages st = hero.buff(NegativeStages.class);
		int done = st == null ? 0 : (int) st.count();
		if (done >= DEPTHS.length || Dungeon.depth < DEPTHS[done]) return;
		GameScene.show(new WndNegative(done));
	}

	//all negative talents in an order fixed by the dungeon seed
	public static ArrayList<Talent> pool(){
		ArrayList<Talent> all = new ArrayList<>();
		for (Talent t : Talent.values()){
			if (t.type() == Talent.TalentType.NEGATIVE) all.add(t);
		}
		Random.pushGenerator(Dungeon.seed + 7171);
		Random.shuffle(all);
		Random.popGenerator();
		return all;
	}

	public WndNegative(int stage){
		super();
		INSTANCE = this;
		tier = stage + 1;

		ArrayList<Talent> all = pool();
		LinkedHashMap<Talent, Integer> options = new LinkedHashMap<>();
		Hero hero = Dungeon.hero;
		for (int i = stage*PER_PICK; i < all.size() && options.size() < PER_PICK; i++){
			Talent t = all.get(i);
			boolean owned = false;
			for (LinkedHashMap<Talent, Integer> tr : hero.talents) owned |= tr.containsKey(t);
			if (!owned) options.put(t, 0);
		}

		IconTitle title = new IconTitle(Icons.get(Icons.TALENT), Messages.titleCase(Messages.get(this, "title")));
		title.color(TITLE_COLOR);
		title.setRect(0, 0, WIDTH, 0);
		add(title);

		RenderedTextBlock text = PixelScene.renderTextBlock(Messages.get(this, "select"), 6);
		text.maxWidth(WIDTH);
		text.setPos(0, title.bottom() + 2);
		add(text);

		TalentsPane.TalentTierPane pane = new TalentsPane.TalentTierPane(options, tier, TalentButton.Mode.NEGATIVE);
		add(pane);
		pane.title.text(" ");
		pane.setPos(0, text.bottom() + 2);
		pane.setSize(WIDTH, pane.height());
		resize(WIDTH, (int) pane.bottom());
	}

	public void select(Talent talent){
		if (chosen) return;
		chosen = true;
		addNegative(Dungeon.hero, talent, tier);
		hide();
	}

	public static void addNegative(Hero hero, Talent talent, int tier){
		LinkedHashMap<Talent, Integer> newTier = new LinkedHashMap<>(hero.talents.get(tier - 1));
		newTier.put(talent, 0);
		hero.talents.set(tier - 1, newTier);
		Buff.count(hero, NegativeStages.class, 1);
		Talent.onTalentUpgraded(hero, talent);
		hero.updateHT(false);
	}

	@Override
	public void onBackPressed(){
		//a curse must be chosen
	}

	@Override
	public void hide(){
		super.hide();
		INSTANCE = null;
	}
}
