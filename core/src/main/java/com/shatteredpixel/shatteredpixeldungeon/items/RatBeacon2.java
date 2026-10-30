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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Regrowth;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goblin;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SpectralSamurai;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SpectralSummoner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SpectralWizard;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AnnihilationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BargainTrap;
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
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.MultiTrap;
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
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class RatBeacon2 extends Item {
	public static final String AC_CHANNEL = "CHANNEL";
	{
		image = ItemSpriteSheet.RATBEACON2;
		unique = true;
		defaultAction = AC_CHANNEL;
	}

	@Override
	public ArrayList<String> actions(Hero hero ) {
		ArrayList<String> actions = new ArrayList<>();
		actions.add( AC_CHANNEL );
		return actions;
	}

	@Override
	public void execute(final Hero hero, String action ) {
		Sample.INSTANCE.play(Assets.Sounds.BEACON, 1.2f, 1.2f, 0.7f);
		//spawn spectral rat
		Mob test;
		int ratId = Random.Int(3);
		if(ratId==1){
			test = new SpectralSamurai();
		}else if(ratId==2){
			test = new SpectralWizard();
		}else{
			test = new SpectralSummoner();
		}
		test.state = test.WANDERING;
		do {
			test.pos = Random.Int(Dungeon.level.length());
		} while (
				Dungeon.level.solid[test.pos] ||
						Dungeon.level.pit[test.pos] ||
						Dungeon.level.distance(test.pos, Dungeon.hero.pos) < 8 ||
						Actor.findChar(test.pos) != null);
		GameScene.add( test );
		//spawn traps
		for (int i = 0; i < Dungeon.level.length(); i++){
			if (Dungeon.level.map[i] == Terrain.EMPTY && Random.Int( 15 ) == 0) {
				Class<? extends Trap> trapClass;
				trapClass = Random.oneOf(levelTraps[0]);
				Trap t = null;
				try {
					t = (Trap) trapClass.newInstance();
				} catch (IllegalAccessException e) {
				} catch (InstantiationException e) {
				}
				Dungeon.level.map[i] = Terrain.SECRET_TRAP;
				t.reveal();
				t.hide();
				Dungeon.level.setTrap(t, i);
				//ScrollOfMagicMapping.discover(i );
				if(t.visible){
					GameScene.discoverTile( i, Dungeon.level.map[i] );
					Dungeon.level.discover( i );
				}
			}
		}
		new Flare( 4, 70 ).color( 0xD69632, true ).show( hero.sprite, 4f );
		hero.spendAndNext(1f);
		GameScene.flash(0xFFFF00);
		GLog.w("The beacon energizes the dungeon!");
	}

	private static Class<?extends Trap>[][] levelTraps = new Class[][]{
			{AlarmTrap.class, BlazingTrap.class, BurningTrap.class, ChaosTrap.class,
					ChillingTrap.class, ConfusionTrap.class, CorrosionTrap.class, CryoTrap.class,
					CursingTrap.class, DisarmingTrap.class, DisintegrationTrap.class, DisrobingTrap.class,
					DistortionTrap.class, ExplosiveTrap.class, FlashingTrap.class, FlockTrap.class,
					FrostTrap.class, GrimTrap.class, GrippingTrap.class, GuardianTrap.class,
					MimicTrap.class, OozeTrap.class, PitfallTrap.class, PoisonDartTrap.class,
					RegrowthTrap.class, RockfallTrap.class, ShockingTrap.class, SmokescreenTrap.class,
					StormTrap.class, SummoningTrap.class, TeleportationTrap.class, ToxicTrap.class,
					AnnihilationTrap.class, WarpingTrap.class, WeakeningTrap.class, WornDartTrap.class,
					InfernoTrap.class, BlizzardTrap.class, FloodTrap.class, LotteryTrap.class,
					HealingTrap.class, SpookTrap.class, ChestnutTrap.class, SharpnelTrap.class, OvergrowthTrap.class, GoblinTrap.class, DreamTrap.class, CannonTrap.class, EnchantingTrap.class,
					SlicingTrap.class, FuryTrap.class, FrostCurseTrap.class, MarkingTrap.class,
					TransmutationTrap.class, RearmTrap.class, BargainTrap.class, MultiTrap.class},
	};

	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
}
