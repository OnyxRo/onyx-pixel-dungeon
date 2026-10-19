package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

//Transformation PD, the reward of its heroic bosses. The mod lets the hero swap in a talent of that boss.
//This game has no boss talent slots, so reading it raises max health by 10 and heals fully.
public class ScrollOfSublimation extends Item {

	{
		image = ItemSpriteSheet.SCROLL_HOLDER;
		stackable = false;
		unique = true;
		bones = false;
	}

	public static final String AC_READ = "READ";
	private static final String TYPE = "type";
	private String type = "";

	public ScrollOfSublimation type(String type) {
		this.type = type;
		return this;
	}

	public String type() { return type; }

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_READ);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		if (action.equals(AC_READ)) {
			Invisibility.dispel();
			hero.spend(1f);
			hero.busy();
			((HeroSprite) hero.sprite).read();
			Sample.INSTANCE.play(Assets.Sounds.READ);
			detach(hero.belongings.backpack);
			//transformation, a hero with the matching boss talent slot picks a talent of that boss
			if (com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationBossTalents.canSublimate(hero, type)){
				final Hero reader = hero;
				final com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent[] options = com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationBossTalents.talentsFor(type);
				String[] names = new String[options.length];
				for (int i = 0; i < options.length; i++) names[i] = com.shatteredpixel.shatteredpixeldungeon.messages.Messages.titleCase(options[i].title());
				com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.show(new com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions(
						new com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon(options[0]),
						com.shatteredpixel.shatteredpixeldungeon.messages.Messages.get(ScrollOfSublimation.class, "choose_title"),
						com.shatteredpixel.shatteredpixeldungeon.messages.Messages.get(ScrollOfSublimation.class, "choose_desc2"), names){
					@Override
					protected void onSelect(int index){
						com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationBossTalents.fillSlot(reader, type, options[index]);
						com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.onTalentUpgraded(reader, null);
					}
					@Override
					protected boolean hasInfo(int index){ return true; }
					@Override
					protected void onInfo(int index){
						com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.show(new com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoTalent(options[index], 0, null));
					}
					@Override
					public void onBackPressed(){}
				});
				return;
			}
			hero.HTBoost += 10;
			hero.updateHT(true);
			hero.HP = hero.HT;
			new Flare(6, 32).color(0xFFFF44, true).show(hero.sprite, 2f);
			GLog.p("Your health grows.");
		}
	}

	@Override
	public boolean isUpgradable() { return false; }

	@Override
	public boolean isIdentified() { return true; }

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(TYPE, type);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		type = bundle.getString(TYPE);
	}
}
