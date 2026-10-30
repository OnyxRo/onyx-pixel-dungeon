package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.DeisticPD;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

//Deistic's Communications Relay, the human artifact. It calls in help for gold. A mercenary costs 500,
//a supply drop (humans only, level 5 and up, never on a boss floor) costs 2500.
public class CommRelay extends DeisticArtifact {

	private static final String AC_MERC = "MERC";
	private static final String AC_SUPPLY = "SUPPLY";

	public static final int MERC_COST = 500;
	public static final int SUPPLY_COST = 2500;

	{
		image = ItemSpriteSheet.COMM_RELAY;
		defaultAction = AC_MERC;
	}

	@Override
	protected String race() {
		return DeisticPD.HUMAN;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && !cursed && basic()) actions.add(AC_MERC);
		if (isEquipped(hero) && !cursed && full() && level() >= 5) actions.add(AC_SUPPLY);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute(hero, action);
		if (action.equals(AC_MERC) && ready(hero)){
			if (Dungeon.gold < MERC_COST){
				GLog.w(Messages.get(this, "no_gold", MERC_COST));
				return;
			}
			ArrayList<Integer> cells = new ArrayList<>();
			for (int n : PathFinder.NEIGHBOURS8){
				int c = hero.pos + n;
				if (Actor.findChar(c) == null && Dungeon.level.passable[c]) cells.add(c);
			}
			if (cells.isEmpty()){
				GLog.i(Messages.get(this, "no_room"));
				return;
			}
			MirrorImage merc = new MirrorImage();
			merc.duplicate(hero);
			GameScene.add(merc);
			ScrollOfTeleportation.appear(merc, Random.element(cells));
			Dungeon.gold -= MERC_COST;
			charge--;
			used(3);
			hero.spendAndNext(1f);
		} else if (action.equals(AC_SUPPLY) && ready(hero) && full()){
			if (Dungeon.bossLevel()){
				GLog.w(Messages.get(this, "boss_floor"));
			} else if (Dungeon.gold < SUPPLY_COST){
				GLog.w(Messages.get(this, "no_gold", SUPPLY_COST));
			} else {
				GameScene.selectCell(supply);
			}
		}
	}

	private final CellSelector.Listener supply = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer pos ) {
			if (pos == null || !Dungeon.level.passable[pos] || !Dungeon.hero.fieldOfView[pos]) return;
			Dungeon.level.drop(Generator.random(Generator.Category.FOOD), pos);
			Dungeon.level.drop(Generator.random(Random.oneOf(Generator.Category.RING, Generator.Category.WAND,
					Generator.Category.WEAPON, Generator.Category.ARMOR)), pos);
			Dungeon.level.drop(Generator.random(Generator.Category.POTION), pos);
			Heap heap = Dungeon.level.drop(Generator.random(Generator.Category.SCROLL), pos);
			if (heap.sprite != null) heap.sprite.drop();
			Dungeon.gold -= SUPPLY_COST;
			charge = 0;
			used(10);
			Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
			Dungeon.hero.spendAndNext(1f);
		}

		@Override
		public String prompt() {
			return Messages.get(CommRelay.class, "prompt");
		}
	};
}
