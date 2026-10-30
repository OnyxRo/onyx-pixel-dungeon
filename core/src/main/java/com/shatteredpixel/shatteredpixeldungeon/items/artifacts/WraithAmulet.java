package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.DeisticPD;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

//Deistic's Wraithmetal Amulet, the wraith artifact. GHOST turns you invisible, ASSASSINATE (wraiths, level 2 and up)
//slips into the shadow of an enemy two steps away that has not noticed you and kills it outright.
public class WraithAmulet extends DeisticArtifact {

	private static final String AC_GHOST = "GHOST";
	private static final String AC_ASSASSINATE = "ASSASSINATE";

	{
		image = ItemSpriteSheet.WRAITH_AMULET;
		defaultAction = AC_GHOST;
	}

	@Override
	protected String race() {
		return DeisticPD.WRAITH;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && !cursed && basic()) actions.add(AC_GHOST);
		if (isEquipped(hero) && !cursed && full() && level() >= 2) actions.add(AC_ASSASSINATE);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute(hero, action);
		if (action.equals(AC_GHOST) && ready(hero)){
			Buff.affect(hero, Invisibility.class, Invisibility.DURATION);
			hero.sprite.emitter().burst(ShadowParticle.UP, 5);
			charge--;
			used(5);
			hero.spendAndNext(1f);
		} else if (action.equals(AC_ASSASSINATE) && ready(hero) && full()){
			GameScene.selectCell(porter);
		}
	}

	private final CellSelector.Listener porter = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer target ) {
			if (target == null) return;
			Hero hero = Dungeon.hero;
			Char ch = Actor.findChar(target);
			if (!(ch instanceof Mob) || ch.alignment != Char.Alignment.ENEMY){
				GLog.i(Messages.get(WraithAmulet.class, "nothing"));
				return;
			}
			if (Dungeon.level.distance(hero.pos, target) != 2){
				GLog.i(Messages.get(WraithAmulet.class, "far"));
				return;
			}
			Mob mob = (Mob) ch;
			if (mob.state == mob.HUNTING || Char.hasProp(mob, Char.Property.BOSS) || Char.hasProp(mob, Char.Property.MINIBOSS)){
				GLog.i(Messages.get(WraithAmulet.class, "noticed"));
				return;
			}
			int landing = -1;
			for (int n : com.watabou.utils.PathFinder.NEIGHBOURS8){
				int c = target + n;
				if (Dungeon.level.adjacent(c, target) && Actor.findChar(c) == null && Dungeon.level.passable[c]){
					landing = c;
					break;
				}
			}
			mob.damage(mob.HP, WraithAmulet.this);
			if (landing != -1){
				ScrollOfTeleportation.appear(hero, landing);
				Dungeon.level.occupyCell(hero);
			}
			hero.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
			charge--;
			used(10);
			Dungeon.observe();
			hero.spendAndNext(1f);
		}

		@Override
		public String prompt() {
			return Messages.get(WraithAmulet.class, "prompt");
		}
	};
}
