/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Pixel Towers / Towers Pixel Dungeon
 * Copyright (C) 2024-2025 FixAkaTheFix (initials R. A. A.)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.SentientTower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.Tower;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.HashSet;

public class ScrollOfHolyNova extends ExoticScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_LULLABY;
		image = ItemSpriteSheet.EXOTIC_BANISHMENT;
	}
	
	@Override
	public void doRead() {
		if (false){
			new Flare( 3, 10 ).color( 0xFFFFAA, true ).show( curUser.sprite, 3f );

			Hero hero = Dungeon.hero;


			Belongings.Backpack backpack = hero.belongings.backpack;

			for (Item item : backpack) {
				if (item instanceof EquipableItem || item instanceof Wand) {
					ScrollOfRemoveCurse.uncurse(hero, item);
				}
			}

			HashSet<Mob> mobsFukConcurrentModificationException = new HashSet<>(Dungeon.level.mobs);

			for (Mob mob : mobsFukConcurrentModificationException) {
				if (hero.fieldOfView[mob.pos] && Dungeon.level.distance(mob.pos, hero.pos) < 30) {
					if (mob.properties().contains(Char.Property.UNDEAD) || mob.properties().contains(Char.Property.DEMONIC))
						mob.damage(mob.properties().contains(Char.Property.BOSS) ? mob.HT/60 + 1 :
								mob.properties().contains(Char.Property.MINIBOSS) ? mob.HT/15 + 1 : mob.HT/5 + 1, this);
					else if (mob.alignment == Char.Alignment.ALLY && !(mob instanceof Tower && ! (mob instanceof SentientTower)))
						Buff.affect(mob, Bless.class, 10);
				}
			}

			GLog.i( Messages.get(this, "procced_noteffective") );

			identify();
		} else{
			new Flare( 10, 85 ).color( 0xFFFFAA, true ).show( curUser.sprite, 3f );

			Hero hero = Dungeon.hero;

			Belongings.Backpack backpack = hero.belongings.backpack;

			for (Item item : backpack) {
				if (item instanceof EquipableItem || item instanceof Wand || item instanceof CorpseDust) {
					ScrollOfRemoveCurse.uncurse(hero, item);
				}
			}
			hero.belongings.uncurseEquipped();


			HashSet<Mob> mobsFukConcurrentModificationException = new HashSet<>(Dungeon.level.mobs);

			for (Mob mob : mobsFukConcurrentModificationException) {
				if (hero.fieldOfView[mob.pos]) {
					if (mob.properties().contains(Char.Property.UNDEAD) || mob.properties().contains(Char.Property.DEMONIC))
						mob.damage(mob.properties().contains(Char.Property.BOSS) ? mob.HT/30 :
								mob.properties().contains(Char.Property.MINIBOSS) ? mob.HT/8 : mob.HT/2, this);
					else if (mob.alignment == Char.Alignment.ALLY && !(mob instanceof Tower && ! (mob instanceof SentientTower)))
						Buff.affect(mob, Bless.class, 50);
				}
			}

			hero.buff( Hunger.class ).satisfy( Hunger.STARVING );
			GLog.p( Messages.get(this, "procced") );
		}
		Dungeon.hero.interrupt();
		Sample.INSTANCE.play( Assets.Sounds.READ );
		readAnimation();
	}

	@Override
	public int value() {
		int x = super.value();
		if (Dungeon.depth > 20) x*=3;
		return x;
	}
}
