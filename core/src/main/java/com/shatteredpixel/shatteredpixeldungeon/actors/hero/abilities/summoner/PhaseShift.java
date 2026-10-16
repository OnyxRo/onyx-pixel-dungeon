package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.summoner;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

//from Ryuse's PD, the Summoner's regalia ability. It lets her materialize beside an ally she can see.
public class PhaseShift extends ArmorAbility {

	{
		baseChargeUse = 35f;
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	public int targetedPos(Char user, int dst) {
		return dst;
	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {
		if (target == null) return;

		Char ally = Actor.findChar(target);
		if (!Dungeon.level.heroFOV[target] || ally == null || ally.alignment != Char.Alignment.ALLY) {
			GLog.w(Messages.get(this, "fov"));
			return;
		}

		//a free cell beside the chosen ally
		int pos = -1;
		int start = Random.Int(8);
		for (int i = 0; i < 8; i++) {
			int cell = target + PathFinder.NEIGHBOURS8[(start + i) % 8];
			if (cell < 0 || cell >= Dungeon.level.length()) continue;
			if (Actor.findChar(cell) == null && Dungeon.level.passable[cell]
					&& !Dungeon.level.avoid[cell] && !Dungeon.level.pit[cell]) {
				pos = cell;
				break;
			}
		}
		if (pos == -1) {
			GLog.w(Messages.get(this, "no_space"));
			return;
		}

		armor.charge -= chargeUse(hero);
		armor.updateQuickslot();

		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (Dungeon.level.adjacent(mob.pos, hero.pos) && mob.alignment != Char.Alignment.ALLY) {
				mob.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 4);
			}
		}

		ScrollOfTeleportation.appear(hero, pos);
		Dungeon.level.occupyCell(hero);
		Dungeon.observe();
		GameScene.updateFog();

		hero.spendAndNext(Actor.TICK);
	}

	@Override
	public int icon() {
		return HeroIcon.WARP_BEACON;
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.TELEFRAG, Talent.REMOTE_BEACON, Talent.LONGRANGE_WARP, Talent.HEROIC_ENERGY};
	}
}
