package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.DeisticPD;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

//Deistic's Chains of Ares, the dwarf artifact. CHAIN hinders one enemy, PULL (dwarves, level 2 and up) drags every enemy in sight to you.
public class AresChains extends DeisticArtifact {

	private static final String AC_CHAIN = "CHAIN";
	private static final String AC_PULL = "PULL";

	{
		image = ItemSpriteSheet.ARES_CHAINS;
		defaultAction = AC_CHAIN;
	}

	@Override
	protected String race() {
		return DeisticPD.DWARF;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && !cursed && basic()) actions.add(AC_CHAIN);
		if (isEquipped(hero) && !cursed && full() && level() >= 2) actions.add(AC_PULL);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute(hero, action);
		if (action.equals(AC_CHAIN) && ready(hero)){
			GameScene.selectCell(chain);
		} else if (action.equals(AC_PULL) && ready(hero) && full()){
			pull(hero);
		}
	}

	private void pull( Hero hero ){
		boolean any = false;
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
			if (charge <= 0) break;
			if (mob.alignment != Char.Alignment.ENEMY || !hero.fieldOfView[mob.pos] || Dungeon.level.adjacent(hero.pos, mob.pos)) continue;
			if (Char.hasProp(mob, Char.Property.IMMOVABLE) || Char.hasProp(mob, Char.Property.BOSS)) continue;
			for (int n : PathFinder.NEIGHBOURS8){
				int cell = hero.pos + n;
				if (Actor.findChar(cell) == null && (Dungeon.level.passable[cell] || Dungeon.level.avoid[cell])){
					hero.sprite.parent.add(new Chains(hero.pos, mob.pos, Effects.Type.CHAIN, null));
					Actor.add(new Pushing(mob, mob.pos, cell));
					mob.pos = cell;
					Dungeon.level.occupyCell(mob);
					mob.damage(Random.NormalIntRange(mob.HT / 10, mob.HT / 4), this);
					if (mob.isAlive()) Buff.prolong(mob, Paralysis.class, 2f);
					charge--;
					any = true;
					break;
				}
			}
		}
		if (!any){
			GLog.i(Messages.get(this, "nobody"));
			return;
		}
		hero.sprite.showStatus(com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite.NEGATIVE, Messages.get(this, "yell"));
		used(5);
		Dungeon.observe();
		hero.spendAndNext(1f);
	}

	private final CellSelector.Listener chain = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer target ) {
			if (target == null) return;
			Hero hero = Dungeon.hero;
			int cell = new Ballistica(hero.pos, target, Ballistica.PROJECTILE).collisionPos;
			if (cell == hero.pos){
				GLog.i(Messages.get(AresChains.class, "self"));
				return;
			}
			final Char ch = Actor.findChar(cell);
			if (ch == null){
				GLog.i(Messages.get(AresChains.class, "nobody"));
				return;
			}
			hero.sprite.parent.add(new Chains(hero.pos, ch.pos, Effects.Type.CHAIN, null));
			Buff.prolong(ch, Cripple.class, 3 + level());
			Buff.affect(ch, Bleeding.class).set(3);
			ch.damage(Random.NormalIntRange(1, 10), AresChains.this);
			charge--;
			used(2);
			hero.sprite.zap(cell);
			hero.spendAndNext(1f);
		}

		@Override
		public String prompt() {
			return Messages.get(AresChains.class, "prompt");
		}
	};
}
