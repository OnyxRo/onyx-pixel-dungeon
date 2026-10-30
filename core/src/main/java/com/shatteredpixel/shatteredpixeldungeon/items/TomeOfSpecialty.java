package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.DeisticPD;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

//Deistic Pixel Dungeon's Tome of Specialty, dropped by Tengu in a run with a Deistic race.
//Reading it picks one of the race's two specialties, which then lasts the whole run.
public class TomeOfSpecialty extends Item {

	private static final String AC_READ = "READ";

	{
		image = ItemSpriteSheet.TOME_OF_SPECIALTY;
		unique = true;
		defaultAction = AC_READ;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_READ);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute(hero, action);
		if (!action.equals(AC_READ)) return;

		String race = DeisticPD.race();
		if (race == null){
			GLog.w(Messages.get(this, "no_race"));
			return;
		}
		if (DeisticPD.specialty() != null){
			GLog.w(Messages.get(this, "already"));
			return;
		}
		final DeisticPD.Way[] ways = DeisticPD.waysFor(race);
		GameScene.show(new WndOptions(new com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite(this), Messages.titleCase(name()),
				Messages.get(this, "choose") + "\n\n" + ways[0].title() + ". " + ways[0].desc() + "\n\n" + ways[1].title() + ". " + ways[1].desc(),
				Messages.titleCase(ways[0].title()), Messages.titleCase(ways[1].title())){
			@Override
			protected void onSelect( int index ) {
				choose(hero, ways[index]);
			}
		});
	}

	private void choose( Hero hero, DeisticPD.Way way ){
		detach(hero.belongings.backpack);
		Buff.affect(hero, DeisticPD.Specialty.class).way = way;
		hero.sprite.emitter().burst(Speck.factory(Speck.STAR), 12);
		Sample.INSTANCE.play(Assets.Sounds.MASTERY);
		GLog.p(Messages.get(this, "chosen", way.title()));
		hero.spendAndNext(1f);
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}
}
