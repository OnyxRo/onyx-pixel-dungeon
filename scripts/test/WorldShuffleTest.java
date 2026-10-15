import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3FileHandle;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Preferences;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.Game;

import java.io.File;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Headless check for "every floor a new world". It builds full dungeons (depth 1 to 26)
 * for many seeds without opening a window, and reports which world each floor got.
 * Fails if any floor throws during generation.
 *
 *   java -cp <classes>:<libs.jar>:<test classes> WorldShuffleTest [seeds]
 */
public class WorldShuffleTest {

	public static void main(String[] args) throws Exception {
		int seeds = args.length > 0 ? Integer.parseInt(args[0]) : 40;

		TestBoot.boot();

		Map<String, Integer> counts = new HashMap<>();
		java.util.Set<String> arrivals = new java.util.HashSet<>();
		int saved = 0;
		java.util.Map<Integer, java.util.Set<String>> byRegion = new java.util.TreeMap<>();
		int floors = 0, failures = 0;
		int bossFloors = 0, modBossFloors = 0;
		HeroClass[] classes = {HeroClass.WARRIOR, HeroClass.MAGE, HeroClass.ROGUE, HeroClass.HUNTRESS, HeroClass.DUELIST, HeroClass.CLERIC};

		for (int s = 0; s < seeds; s++) {
			long seed = 1000003L * (s + 1);
			GamesInProgress.selectedClass = classes[s % classes.length];
			Dungeon.seed = seed;
			Dungeon.init();
			StringBuilder line = new StringBuilder(String.format("seed %-10d", seed));
			for (int d = 1; d <= 26; d++) {
				Dungeon.depth = d;
				Dungeon.branch = 0;
				try {
					Level l = Dungeon.newLevel();
					floors++;
					if (Dungeon.bossLevel(d)) {
						bossFloors++;
						//a mod's boss floor only belongs after a region that went through one of that mod's worlds
						int bossWorld = WorldShuffle.bossWorldFor(d);
						boolean modBoss = bossWorld != -1;
						if (modBoss) {
							modBossFloors++;
							if (l.getClass() != WorldShuffle.modBossLevel(bossWorld, d).getClass()) {
								throw new IllegalStateException("boss floor " + d + " should be the mod's boss level");
							}
						}
					}
					String w = l.getClass().getSimpleName().replace("Level", "");
					if (WorldShuffle.shuffles(d)) {
						String world = l.getClass().getSimpleName().replace("Level", "");
						counts.merge(world, 1, Integer::sum);
						if (WorldShuffle.worldOf(l) == WorldShuffle.homeWorld(d)) {
							throw new IllegalStateException("shuffled floor kept its home world");
						}
						if (WorldShuffle.worldOf(l) < 0) {
							throw new IllegalStateException("shuffled floor is not a known world");
						}
						int region = WorldShuffle.regionOf(WorldShuffle.worldOf(l));
						if (region != WorldShuffle.homeWorld(d) && region != WorldShuffle.ANY_REGION) {
							throw new IllegalStateException(world + " is not made for floor " + d);
						}
						byRegion.computeIfAbsent(WorldShuffle.homeWorld(d), k -> new java.util.TreeSet<>()).add(world);
						if (WorldShuffle.isPortedWorld(l)) {
							String arrive = Messages.get(WorldShuffle.class, "arrive",
									Messages.get(l.getClass(), "name"), Messages.get(l.getClass(), "mod"));
							if (arrive.contains("!!!")) {
								throw new IllegalStateException("missing world strings for " + world + ": " + arrive);
							}
							if (arrivals.add(world)) {
								if (arrivals.size() <= 3) System.out.println("  log line: " + arrive);
								//save and reload the floor, the way the game does between sessions
								com.watabou.utils.Bundle b = new com.watabou.utils.Bundle();
								Dungeon.level = l;
								b.put("level", l);
								Dungeon.level = null;
								com.watabou.utils.Bundle reread = com.watabou.utils.Bundle.read(
										new java.io.ByteArrayInputStream(bundleBytes(b)));
								Level back = (Level) reread.get("level");
								if (back.getClass() != l.getClass() || !back.tilesTex().equals(l.tilesTex())
										|| back.map.length != l.map.length) {
									throw new IllegalStateException("save and reload changed " + world);
								}
								saved++;
							}
						}
						w = world.substring(0, 2).toUpperCase();
					} else {
						w = w.length() > 3 ? w.substring(0, 3).toLowerCase() : w.toLowerCase();
					}
					line.append(' ').append(w);
				} catch (Throwable t) {
					failures++;
					line.append(" !!");
					System.out.println("FAIL seed " + seed + " depth " + d + ": " + t);
					t.printStackTrace(System.out);
				}
			}
			if (s < 8) System.out.println(line);
		}
		//sandbox runs can force any world onto any regular floor, so build every combination
		int forced = 0;
		Dungeon.sandbox = true;
		for (int s = 0; s < Math.min(seeds, 3); s++) {
			long seed = 7000003L * (s + 1);
			GamesInProgress.selectedClass = classes[s % classes.length];
			Dungeon.seed = seed;
			Dungeon.init();
			for (int world = 0; world < WorldShuffle.worldCount(); world++) {
				Dungeon.sandboxWorld = world;
				if (WorldShuffle.menuName(world).isEmpty() || WorldShuffle.menuName(world).contains("!!!")) {
					failures++;
					System.out.println("FAIL: world " + world + " has no menu name");
				}
				for (int d = 1; d <= 26; d++) {
					Dungeon.depth = d;
					Dungeon.branch = 0;
					try {
						Level l = Dungeon.newLevel();
						if (WorldShuffle.canForce(d) && WorldShuffle.worldOf(l) != world) {
							throw new IllegalStateException("forced world was ignored");
						}
						if (!WorldShuffle.canForce(d) && !(Dungeon.bossLevel(d) || d == 26)) {
							throw new IllegalStateException("a regular floor could not be forced");
						}
						forced++;
					} catch (Throwable t) {
						failures++;
						System.out.println("FAIL sandbox seed " + seed + " world " + world + " depth " + d + ": " + t);
						t.printStackTrace(System.out);
					}
				}
			}
		}
		//sandbox runs can switch mods off. With only one mod left on, every shuffled floor must use it
		Dungeon.sandboxWorld = -1;
		int modChecks = 0;
		for (String only : WorldShuffle.mods()) {
			Dungeon.sandboxOffMods.clear();
			Dungeon.sandboxOffMods.addAll(WorldShuffle.mods());
			Dungeon.sandboxOffMods.remove(only);
			for (int d = 1; d <= 26; d++) {
				if (!WorldShuffle.shuffles(d)) continue;
				String got = WorldShuffle.modName(WorldShuffle.worldFor(d));
				modChecks++;
				if (!got.equals(only)) {
					failures++;
					System.out.println("FAIL: only " + only + " was on, but floor " + d + " rolled a world from " + got);
				}
			}
		}
		Dungeon.sandboxOffMods.clear();
		System.out.println("mod toggle checks: " + modChecks);

		Dungeon.sandbox = false;
		Dungeon.sandboxWorld = -1;
		System.out.println("sandbox floors built with a forced world: " + forced);

		System.out.println("ported worlds saved and reloaded: " + saved);
		System.out.println("floors generated: " + floors + ", failures: " + failures);
		System.out.println("boss floors: " + bossFloors + ", of which a mod's boss: " + modBossFloors);
		System.out.println("shuffled-floor worlds (" + counts.size() + " of " + (WorldShuffle.worldCount() - 5) + " ported): " + new java.util.TreeMap<>(counts));
		String[] regionNames = {"Sewers stage (2-3)", "Prison stage (6-8)", "Caves stage (11-13)", "City stage (16-18)", "Halls stage (21-24)"};
		for (java.util.Map.Entry<Integer, java.util.Set<String>> e : byRegion.entrySet()) {
			System.out.println("  " + regionNames[e.getKey()] + ": " + e.getValue());
		}
		//Shattered's own five only appear on their fixed floors now, so every ported world should show up
		if (seeds >= 100 && counts.size() < WorldShuffle.worldCount() - 5) {
			System.out.println("FAIL: some worlds never appeared");
			failures++;
		}
		//Single mod mode, every shuffled floor must come from the chosen mod
		int singleChecks = 0;
		for (String mod : WorldShuffle.mods()) {
			Dungeon.singleMod = mod;
			for (int d = 1; d <= 26; d++) {
				if (!WorldShuffle.shuffles(d)) continue;
				int w = WorldShuffle.worldFor(d);
				singleChecks++;
				if (!WorldShuffle.modName(w).equals(mod)) {
					System.out.println("FAIL: single mod " + mod + " gave " + WorldShuffle.modName(w) + " on floor " + d);
					failures++;
				}
			}
		}
		Dungeon.singleMod = "";
		System.out.println("single mod checks: " + singleChecks);
		//every ported monster needs a name string
		int namedMobs = 0;
		for (com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary cat : com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary.values()) {
			for (Class<?> cls : cat.entities()) {
				if (!com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary.isPorted(cls)) continue;
				namedMobs++;
				if (Messages.get(cls, "name").contains("!!!")) {
					System.out.println("FAIL: no name string for " + cls.getName());
					failures++;
				}
			}
		}
		System.out.println("ported monsters with name strings checked: " + namedMobs);
		System.exit(failures == 0 ? 0 : 1);
	}

	private static byte[] bundleBytes(com.watabou.utils.Bundle b) throws java.io.IOException {
		java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
		com.watabou.utils.Bundle.write(b, out);
		return out.toByteArray();
	}
}
