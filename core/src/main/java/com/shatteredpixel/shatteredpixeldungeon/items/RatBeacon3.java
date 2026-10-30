/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SpectralSamurai;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SpectralSummoner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SpectralWizard;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.InventorySpell;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AnnihilationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BlazingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BlizzardTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BurningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CannonTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChaosTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChestnutTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChillingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ConfusionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CorrosionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CryoTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CursingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisintegrationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisrobingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DistortionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DreamTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.EnchantingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlashingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlockTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FloodTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostCurseTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FuryTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GoblinTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrippingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GuardianTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.HealingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.InfernoTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.LotteryTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.MarkingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.MimicTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OozeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OvergrowthTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PitfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PoisonDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RearmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RegrowthTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SharpnelTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SlicingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SmokescreenTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SpookTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.StormTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ToxicTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TransmutationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WeakeningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class RatBeacon3 extends Item {
	public static final String AC_RICHES = "RICHES";
	public static final String AC_POWER = "POWER";
	{
		image = ItemSpriteSheet.RATBEACON3;
		unique = true;
		defaultAction = AC_POWER;
	}

	@Override
	public ArrayList<String> actions(Hero hero ) {
		ArrayList<String> actions = new ArrayList<>();
		actions.add( AC_RICHES );
		actions.add( AC_POWER );
		return actions;
	}

	@Override
	public void execute(final Hero hero, String action ) {
        super.execute( hero, action );

        if (action.equals(AC_RICHES)) {
            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                int ii = hero.pos+PathFinder.NEIGHBOURS8[i];
                if(Dungeon.level.passable[ii]){
					Dungeon.level.drop(Generator.random(), ii).sprite.drop(ii);
					CellEmitter.get( ii ).burst( Speck.factory( Speck.WOOL ), 4 );
				}
            }
			Sample.INSTANCE.play(Assets.Sounds.PUFF);
			hero.spendAndNext(1f);
        }else if (action.equals(AC_POWER)) {
			GameScene.selectItem( itemSelector );
        }
	}

	private WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {
		@Override
		public String textPrompt() {
			return "Upgrade an item";
		}

		@Override
		public boolean itemSelectable( Item item ) {
			return item.isUpgradable();
		}

		@Override
		public void onSelect( Item item ) {

			if (item != null) {

				onItemSelected( item );
				curUser.spend( 1f );
				curUser.busy();
				(curUser.sprite).operate( curUser.pos );

				Sample.INSTANCE.play( Assets.Sounds.READ );
				Invisibility.dispel();

			} else {
				curItem.collect( curUser.belongings.backpack );
			}
		}
	};

	private void onItemSelected( Item item ) {

		if (item instanceof Weapon && ((Weapon) item).enchantment != null && !((Weapon) item).hasCurseEnchant()) {
			((Weapon) item).upgrade(true);
		} else if (item instanceof Armor && ((Armor) item).glyph != null && !((Armor) item).hasCurseGlyph()) {
			((Armor) item).upgrade(true);
		} else {
			item.upgrade();
		}

		GLog.p( Messages.get(new MagicalInfusion(), "infuse", item.name()) );

		Badges.validateItemLevelAquired(item);

		curUser.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3);
		Statistics.upgradesUsed++;
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
