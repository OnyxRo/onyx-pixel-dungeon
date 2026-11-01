import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.watabou.utils.Reflection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.TreeMap;

/**
 * What each monster does to a hero in a fight. For every monster and floor in a BalanceReport file, the monster is put
 * on a plain sewer floor of that depth, 3 steps from a hero who stands still, and the game's own turns run for a while. Every point of
 * health the hero loses is counted, whatever caused it, so zaps, bolts, gas, poison, burning and summons are in.
 * The hero is level 1 with no armor and lots of health, and nothing heals him.
 *
 *   java FightReport <balance.tsv> <out.tsv> [turns, default 50] [fights per monster, default 6]
 *
 * Lines are class depth fights hits avgHit maxHit perTurn. A monster that crashes is written with -1s.
 */
public class FightReport {

	static Field TIME, NOW;
	static final boolean DEBUG = Boolean.getBoolean("fight.debug");
	//the stand-in game screen, switched on only after a floor is built (building a floor with it would draw tiles)
	static com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene STUB;
	static Method ACT;

	public static void main(String[] args) throws Exception {
		String in = args[0], out = args[1];
		int turns = args.length > 2 ? Integer.parseInt(args[2]) : 50;
		int fights = args.length > 3 ? Integer.parseInt(args[3]) : 6;
		TestBoot.boot();
		TestBoot.stubWindow();
		//a game screen with empty layers, so monsters that show an alert icon or a status can run headless
		com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene scene = TestBoot.blank(com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.class);
		for (Field g : com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.class.getDeclaredFields()) {
			if (g.getType() == com.watabou.noosa.Group.class && !java.lang.reflect.Modifier.isStatic(g.getModifiers())) {
				g.setAccessible(true);
				g.set(scene, new com.watabou.noosa.Group());
			}
		}
		STUB = scene;
		TIME = Actor.class.getDeclaredField("time"); TIME.setAccessible(true);
		NOW = Actor.class.getDeclaredField("now"); NOW.setAccessible(true);
		ACT = Actor.class.getDeclaredMethod("act"); ACT.setAccessible(true);

		//each monster at each floor it was seen on
		TreeMap<Integer, LinkedHashSet<String>> todo = new TreeMap<>();
		try (BufferedReader r = new BufferedReader(new FileReader(in))) {
			String line;
			while ((line = r.readLine()) != null) {
				String[] p = line.split("\t");
				if (p[0].equals("MOB")) todo.computeIfAbsent(Integer.parseInt(p[1]), k -> new LinkedHashSet<>()).add(p[4]);
			}
		}

		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		Dungeon.seed = 777777L;
		Dungeon.init();
		int done = 0, broken = 0;
		try (PrintWriter w = new PrintWriter(out, "UTF-8")) {
			for (int depth : todo.keySet()) {
				Dungeon.depth = depth;
				Dungeon.branch = 0;
				for (String cls : todo.get(depth)) {
					//a fresh arena per monster, some leave things behind that spoil the next fight
					//a plain Shattered sewer floor as the arena, so every monster fights on the same kind of ground
					com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.scene = null;
					Level level = new com.shatteredpixel.shatteredpixeldungeon.levels.SewerLevel();
					level.create();
					//tall grass blocks sight, a monster that loses the hero just wanders off
					for (int i = 0; i < level.length(); i++) {
						int t = level.map[i];
						if (t == com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.HIGH_GRASS || t == com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.FURROWED_GRASS) {
							Level.set(i, com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.GRASS, level);
						}
					}
					Dungeon.level = level;
					com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.scene = STUB;
					long hits = 0, total = 0; int max = 0, ok = 0;
					//from 3 steps away first. A monster that never hurts the hero from there (one that does not move,
					//like a rot lasher) fights again from right beside him, and the line says so
					int start = 3;
					for (int pass = 0; pass < 2; pass++) {
						hits = 0; total = 0; max = 0; ok = 0;
						for (int f = 0; f < fights; f++) {
							try {
								int[] res = fight(level, cls, turns, start);
								if (res == null) continue;
								hits += res[0]; total += res[1]; max = Math.max(max, res[2]); ok++;
							} catch (Throwable t) {
								//a monster that cannot fight here, left out. The first reason is printed for the log
								if (f == 0 && pass == 0) System.out.println("NOFIGHT " + cls + " " + t);
							}
						}
						if (ok == 0 || hits > 0) break;
						start = 1;
					}
					if (ok == 0) { broken++; w.println(cls + "\t" + depth + "\t0\t-1\t-1\t-1\t-1\t" + start); continue; }
					w.println(cls + "\t" + depth + "\t" + ok + "\t" + hits + "\t" + String.format("%.2f", hits == 0 ? 0 : total / (double) hits)
							+ "\t" + max + "\t" + String.format("%.3f", total / (double) (ok * turns)) + "\t" + start);
					done++;
				}
			}
		}
		System.out.println("SUMMARY fights measured " + done + ", could not fight " + broken);
		System.exit(0);
	}

	/** hits, total damage, biggest single loss, or null when the monster could not be placed */
	static int[] fight(Level level, String clsName, int turns, int start) throws Throwable {
		Actor.clear();
		level.mobs.clear();
		level.blobs.clear();
		Hero hero = new Hero();
		Dungeon.hero = hero;
		hero.live();
		for (Buff b : hero.buffs().toArray(new Buff[0])) b.detach();
		hero.sprite = TestBoot.sprite();
		hero.sprite.ch = hero;
		new com.watabou.noosa.Group().add(hero.sprite);
		hero.HP = hero.HT = 100000;
		hero.pos = level.entrance();
		if (hero.pos < 0 || !level.passable[hero.pos]) {
			for (int i = 0; i < level.length(); i++) if (level.passable[i]) { hero.pos = i; break; }
		}
		Actor.add(hero);

		//a free cell 3 steps away by walking, or as close to that as the floor allows, so no wall is in between
		//and in plain sight of the hero
		boolean[] sight = new boolean[level.length()];
		com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster.castShadow(hero.pos % level.width(), hero.pos / level.width(), level.width(), sight, level.losBlocking, 8);
		com.watabou.utils.PathFinder.buildDistanceMap(hero.pos, level.passable);
		int cell = -1;
		for (int want = start; want >= 1 && cell < 0; want--) {
			for (int i = 0; i < level.length(); i++) {
				if (com.watabou.utils.PathFinder.distance[i] == want && sight[i] && level.distance(i, hero.pos) == want
						&& !level.solid[i] && Actor.findChar(i) == null) { cell = i; break; }
			}
		}
		if (cell < 0) return null;
		Mob mob = (Mob) Reflection.newInstance(Class.forName(clsName));
		if (mob == null) return null;
		mob.pos = cell;
		mob.state = mob.HUNTING;
		mob.sprite = TestBoot.sprite();
		mob.sprite.ch = mob;
		new com.watabou.noosa.Group().add(mob.sprite);
		//statues get their weapon from the room that makes them
		if (mob instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue) ((com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue) mob).createWeapon(false);
		//mimics are scaled to the floor when a level places them
		if (mob instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic) ((com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic) mob).setLevel(Dungeon.depth);
		level.mobs.add(mob);
		Actor.add(mob);
		Field enemy = Mob.class.getDeclaredField("enemy"); enemy.setAccessible(true);
		enemy.set(mob, hero);

		for (Actor a : Actor.all()) TIME.setFloat(a, 0);
		NOW.setFloat(null, 0);
		int hits = 0, total = 0, max = 0, steps = 0;
		while (steps++ < turns * 40) {
			Actor next = null;
			for (Actor a : Actor.all()) if (next == null || TIME.getFloat(a) < TIME.getFloat(next)) next = a;
			if (next == null) break;
			float t = TIME.getFloat(next);
			if (t >= turns) break;
			NOW.setFloat(null, t);
			if (next == hero) { TIME.setFloat(hero, t + 1); continue; }
			int before = hero.HP;
			try {
				ACT.invoke(next);
			} catch (java.lang.reflect.InvocationTargetException e) {
				throw e.getCause();
			}
			//an actor that did not spend time would loop forever, give it a turn
			if (TIME.getFloat(next) <= t && Actor.all().contains(next)) TIME.setFloat(next, t + 1);
			int lost = before - hero.HP;
			if (DEBUG && next == mob && steps < 12) System.out.println("DBG " + clsName + " d" + Dungeon.depth + " t" + t + " mob@" + mob.pos + " hero@" + hero.pos + " state " + mob.state.getClass().getSimpleName() + " alive " + mob.isAlive() + " lost " + lost + " dist " + level.distance(mob.pos, hero.pos));
			if (lost > 0) { hits++; total += lost; max = Math.max(max, lost); }
		}
		return new int[]{hits, total, max};
	}
}
