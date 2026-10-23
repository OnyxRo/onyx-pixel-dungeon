/*
 * Onyx's Pixel Dungeon
 *
 * Members Re Blue Archive PD (https://github.com/Hoto-Mocha/Re-Blue-Archive-Pixel-Dungeon) added to Shattered's own
 * classes, kept here so those classes stay as they were. Ported 2026-10-22.
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

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Sheep;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.fox.RBAFox;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.fox.RBAKurumi;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.fox.RBANiko;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.fox.RBAOtogi;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.fox.RBAYukino;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.ImpShopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.CityBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.DeadEndLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.HallsBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.LastLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class RBAShims {

	/** Ballistica.WALL_PENETRATION in the mod, a bolt that only stops at its target or a character. */
	public static final int WALL_PENETRATION = Ballistica.STOP_TARGET | Ballistica.STOP_CHARS;

	// ---- Dungeon.mobsToQuest, kills left until the next quest enemy, saved with the run -------------

	public static float mobsToQuest = 1;
	private static final String MOBS_TO_QUEST = "rba_mobs_to_quest";

	public static void reset(){
		mobsToQuest = 1;
	}

	public static void store( Bundle bundle ){
		bundle.put( MOBS_TO_QUEST, mobsToQuest );
	}

	public static void restore( Bundle bundle ){
		mobsToQuest = bundle.contains(MOBS_TO_QUEST) ? bundle.getFloat( MOBS_TO_QUEST ) : 1;
	}

	// ---- PotionOfCleansing.cleanseDebuff, StoneOfClairvoyance.mapping, Viscosity.applyViscosity ------

	/** Removes negative buffs without the hunger and long buff side of a full cleanse. */
	public static void cleanseDebuff( Char ch, float duration ){
		for (com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff b : ch.buffs()){
			if (b.type == com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.buffType.NEGATIVE
					&& !(b instanceof com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff)
					&& !(b instanceof com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory)){
				b.detach();
			}
		}
		if (duration > 0) {
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.prolong(ch,
					com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing.Cleanse.class, duration);
		}
	}

	/** Maps the area around a cell like a Stone of Clairvoyance of the given reach. */
	public static void clairvoyance( int cell, int distance ){
		Level level = Dungeon.level;
		distance = Math.max(0, Math.min(distance, com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster.MAX_DISTANCE));
		com.watabou.utils.Point c = level.cellToPoint(cell);
		int[] rounding = com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster.rounding[distance];
		int left, right, curr;
		for (int y = Math.max(0, c.y - distance); y <= Math.min(level.height() - 1, c.y + distance); y++) {
			if (rounding[Math.abs(c.y - y)] < Math.abs(c.y - y)) {
				left = c.x - rounding[Math.abs(c.y - y)];
			} else {
				left = distance;
				while (rounding[left] < rounding[Math.abs(c.y - y)]) {
					left--;
				}
				left = c.x - left;
			}
			right = Math.min(level.width() - 1, c.x + c.x - left);
			left = Math.max(0, left);
			for (curr = left + y * level.width(); curr <= right + y * level.width(); curr++) {
				GameScene.checkedCell(curr, cell);
				level.mapped[curr] = true;
				if (level.secret[curr]) {
					level.discover(curr);
					if (level.heroFOV[curr]) {
						GameScene.discoverTile(curr, level.map[curr]);
						com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping.discover(curr);
					}
				}
			}
		}
		GameScene.updateFog();
	}

	/** Adds to the damage a Viscosity glyph spreads over time, as if the glyph had more levels. */
	public static void applyViscosity( Char defender, int level ){
		com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(defender,
				com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity.ViscosityTracker.class).level += level;
	}

	// ---- Actor.enemiesInHeroFOV ---------------------------------------------------------------------

	public static HashSet<Mob> enemiesInHeroFOV( Level level ){
		HashSet<Mob> result = new HashSet<>();
		for (Char ch : Actor.chars()) {
			if (level.heroFOV[ch.pos] && ch instanceof Mob && ch.alignment == Char.Alignment.ENEMY) result.add((Mob) ch);
		}
		return result;
	}

	// ---- Level.canBreakWall and breakWall, for Yuzu's Avant-Garde Kun, the Scrunchie and the sandbox console --

	public static boolean canBreakWall( int cell ){
		Level level = Dungeon.level;
		boolean canBreakWall = level.solid[cell]
				&& level.map[cell] != Terrain.ALCHEMY
				&& cell % level.width() != 0
				&& cell % level.width() != level.width()-1
				&& cell > level.width()
				&& cell < level.map.length-level.width();
		boolean canBreakLevel = Dungeon.branch == 0
				&& !(level instanceof DeadEndLevel)
				&& !(level instanceof LastLevel)
				&& !(level instanceof SewerBossLevel)
				&& !(level instanceof PrisonBossLevel)
				&& !(level instanceof CavesBossLevel)
				&& !(level instanceof CityBossLevel)
				&& !(level instanceof HallsBossLevel)
				&& !Dungeon.bossLevel();
		return canBreakWall && canBreakLevel;
	}

	public static boolean breakWall( int cell ){
		if (!canBreakWall(cell)) return false;
		Level level = Dungeon.level;
		level.set(cell, Terrain.EMPTY);
		for (int i : PathFinder.NEIGHBOURS9) {
			level.discoverable[cell+i] = true;
		}
		level.losBlocking[cell] = false;
		GameScene.updateMap(cell);
		Dungeon.observe();
		return true;
	}

	// ---- Shopkeeper.run, Shiroko's bank robbery. The shopkeeper runs, the shop's goods are left behind
	// ---- and, with the warning, the Fox squad comes after the hero -----------------------------------

	public static void shopkeeperRun( Shopkeeper sk, boolean warning ){
		//the goods stay, the normal flight would take them along
		for (Heap heap: Dungeon.level.heaps.valueList()) {
			if (heap.type == Heap.Type.FOR_SALE) {
				heap.type = Heap.Type.HEAP;
			}
		}
		sk.destroy();

		Notes.remove( sk.landmark() );
		boolean imp = sk instanceof ImpShopkeeper;
		sk.yell(Messages.get(RBAShims.class, imp ? "yell_akira" : "yell"));
		GLog.newLine();
		if (warning) {
			GLog.w(Messages.get(RBAShims.class, imp ? "flee_warning_akira" : "flee_warning"));
			Sample.INSTANCE.play(Assets.Sounds.ALERT);
			CellEmitter.center( sk.pos ).start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );

			//clears the room of monsters other than allies and NPCs
			Ballistica aim;
			int w = Dungeon.level.width(), h = Dungeon.level.height();
			int x = sk.pos % w;
			int y = sk.pos / w;
			if (Math.max(x, w-x) >= Math.max(y, h-y)){
				aim = new Ballistica(sk.pos, x > w/2 ? sk.pos - 1 : sk.pos + 1, Ballistica.WONT_STOP);
			} else {
				aim = new Ballistica(sk.pos, y > h/2 ? sk.pos - w : sk.pos + w, Ballistica.WONT_STOP);
			}
			ConeAOE aoe = new ConeAOE(aim, 8, 360, Ballistica.STOP_SOLID);
			for (int cell : aoe.cells) {
				Char ch = Actor.findChar(cell);
				if (!(ch instanceof Mob) || (ch instanceof NPC && !(ch instanceof Sheep)) || ch.alignment == Char.Alignment.ALLY) continue;
				ch.destroy();
				if (ch.sprite != null) {
					ch.sprite.killAndErase();
					CellEmitter.get(cell).burst(Speck.factory(Speck.WOOL), 4);
				}
			}

			//the Fox squad, four of them around where the shopkeeper stood
			ArrayList<Integer> candidates = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS8) {
				if (!Dungeon.level.solid[sk.pos+i]) candidates.add(sk.pos+i);
			}
			Random.shuffle(candidates);
			ArrayList<RBAFox> foxes = new ArrayList<>();
			RBAYukino yukino = null; RBANiko niko = null; RBAKurumi kurumi = null; RBAOtogi otogi = null;
			int talkIndex = Random.Int(4);
			for (int i = 0; i < 4 && i < candidates.size(); i++) {
				int spawnPos = candidates.get(i);
				RBAFox toSpawn;
				if (i == 0)      { toSpawn = RBAFox.spawnAt(spawnPos, new RBAYukino()); yukino = (RBAYukino) toSpawn; }
				else if (i == 1) { toSpawn = RBAFox.spawnAt(spawnPos, new RBANiko());   niko = (RBANiko) toSpawn; }
				else if (i == 2) { toSpawn = RBAFox.spawnAt(spawnPos, new RBAKurumi()); kurumi = (RBAKurumi) toSpawn; }
				else             { toSpawn = RBAFox.spawnAt(spawnPos, new RBAOtogi());  otogi = (RBAOtogi) toSpawn; }
				foxes.add(toSpawn);
				if (i == talkIndex) toSpawn.yell(Messages.get(toSpawn.getClass(), "show"));

				Char c = Actor.findChar(spawnPos);
				if (c != null) {
					Char toPush = Char.hasProp(c, Char.Property.IMMOVABLE) ? toSpawn : c;
					ArrayList<Integer> pushCandidates = new ArrayList<>();
					for (int n : PathFinder.NEIGHBOURS8) {
						int cell = spawnPos + n;
						if (!Dungeon.level.solid[cell] && Actor.findChar( cell ) == null
								&& (!Char.hasProp(toPush, Char.Property.LARGE) || Dungeon.level.openSpace[cell])) {
							pushCandidates.add( cell );
						}
					}
					Random.shuffle(pushCandidates);
					if (!pushCandidates.isEmpty()){
						Actor.add( new Pushing( toPush, toPush.pos, pushCandidates.get(0) ));
						toPush.pos = pushCandidates.get(0);
						Dungeon.level.occupyCell(toPush);
					}
				}
				CellEmitter.get(spawnPos).burst(Speck.factory(Speck.WOOL), 4);
				GameScene.add(toSpawn, 0f);
			}
			for (RBAFox fox : foxes) {
				fox.setAlly( yukino, niko, kurumi, otogi );
			}
		} else {
			GLog.i(Messages.get(RBAShims.class, imp ? "flee_akira" : "flee"));
		}

		if (sk.sprite != null) {
			sk.sprite.killAndErase();
			CellEmitter.get(sk.pos).burst(ElmoParticle.FACTORY, 6);
		}
	}
}
