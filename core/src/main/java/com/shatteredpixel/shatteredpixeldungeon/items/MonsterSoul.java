package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

//Regrown Pixel Dungeon's Soul, renamed since this game already has a Soul class.
//Using or throwing it puts its monster back, asleep, and leaves a piece of meat.
public class MonsterSoul extends Item {

	public static final String AC_SUMMON = "SUMMON";

	{
		image = ItemSpriteSheet.MONSTER_SOUL;
		defaultAction = AC_SUMMON;
	}

	public Class<? extends Mob> monsterClass = Rat.class;

	public MonsterSoul(){
	}

	public MonsterSoul( Class<? extends Mob> monsterClass ){
		this.monsterClass = monsterClass;
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
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_SUMMON);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute(hero, action);
		if (action.equals(AC_SUMMON)) {
			Item left = shatter(hero.pos);
			if (left != this) {
				detach(hero.belongings.backpack);
				{ com.shatteredpixel.shatteredpixeldungeon.items.Heap dropped = Dungeon.level.drop(left, hero.pos); if (dropped.sprite != null) dropped.sprite.drop(); }
				hero.spendAndNext(1f);
			} else {
				GLog.w(Messages.get(this, "no_room"));
			}
		}
	}

	@Override
	protected void onThrow( int cell ) {
		if (Dungeon.level.pit[cell]) {
			super.onThrow(cell);
		} else {
			{ com.shatteredpixel.shatteredpixeldungeon.items.Heap dropped = Dungeon.level.drop(shatter(cell), cell); if (dropped.sprite != null) dropped.sprite.drop(); }
		}
	}

	private Item shatter( int pos ) {
		if (Dungeon.level.heroFOV[pos]) {
			Sample.INSTANCE.play(Assets.Sounds.SHATTER);
			Splash.at(pos, 0xffd500, 5);
		}
		int newPos = pos;
		if (Actor.findChar(pos) != null) {
			ArrayList<Integer> candidates = new ArrayList<>();
			for (int n : PathFinder.NEIGHBOURS4) {
				int c = pos + n;
				if (Dungeon.level.passable[c] && Actor.findChar(c) == null) candidates.add(c);
			}
			newPos = candidates.isEmpty() ? -1 : Random.element(candidates);
		}
		if (newPos == -1) return this;

		Mob mob = Reflection.newInstance(monsterClass);
		if (mob == null) mob = new Rat();
		mob.state = mob.SLEEPING;
		mob.pos = newPos;
		GameScene.add(mob);
		Sample.INSTANCE.play(Assets.Sounds.BEE);
		return new MysteryMeat();
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", Messages.titleCase(Messages.get(monsterClass, "name")));
	}

	private static final String MONSTER_CLASS = "monsterClass";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(MONSTER_CLASS, monsterClass);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		Class<?> c = bundle.getClass(MONSTER_CLASS);
		monsterClass = (c != null && Mob.class.isAssignableFrom(c)) ? c.asSubclass(Mob.class) : Rat.class;
	}

	@Override
	public int value() {
		return 10;
	}
}
