import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.WorldShuffle;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Balance report. Builds every floor of N seeded runs the way a normal game does, and writes what is
 * standing or lying on each floor when it is made, so monsters and items can be compared by stage.
 *
 *   java BalanceReport <seeds> <out.tsv>
 *
 * Lines are MOB depth world mod class name hp attackSkill defense avgDmg maxDmg, and
 * ITEM depth world class name. Damage is 400 rolls of damageRoll at that depth, it leaves out
 * special attacks like zaps and explosions.
 */
public class BalanceReport {

	public static void main(String[] args) throws Exception {
		int seeds = args.length > 0 ? Integer.parseInt(args[0]) : 100;
		String out = args.length > 1 ? args[1] : "balance.tsv";
		TestBoot.boot();
		HeroClass[] classes = {HeroClass.WARRIOR, HeroClass.MAGE, HeroClass.ROGUE, HeroClass.HUNTRESS, HeroClass.DUELIST, HeroClass.CLERIC};
		Map<String, String> stats = new HashMap<>();
		int fails = 0;
		try (PrintWriter w = new PrintWriter(out, "UTF-8")) {
			for (int s = 0; s < seeds; s++) {
				GamesInProgress.selectedClass = classes[s % classes.length];
				Dungeon.seed = 5000011L * (s + 1);
				Dungeon.init();
				for (int d = 1; d <= 26; d++) {
					Dungeon.depth = d;
					Dungeon.branch = 0;
					Level l;
					try {
						l = Dungeon.newLevel();
					} catch (Throwable t) {
						fails++;
						continue;
					}
					String world = l.getClass().getSimpleName().replace("Level", "");
					String mod = WorldShuffle.modKey(l);
					if (mod == null) mod = "shattered";
					w.println("FLOOR\t" + d + "\t" + world + "\t" + mod);
					for (Mob m : l.mobs.toArray(new Mob[0])) {
						String key = m.getClass().getName() + "@" + d;
						String st = stats.get(key);
						if (st == null) {
							st = measure(m);
							stats.put(key, st);
						}
						w.println("MOB\t" + d + "\t" + world + "\t" + mod + "\t" + st);
					}
					for (Heap h : l.heaps.valueList()) {
						for (Item i : h.items) {
							String name;
							try { name = i.trueName(); } catch (Throwable t) { name = i.getClass().getSimpleName(); }
							w.println("ITEM\t" + d + "\t" + world + "\t" + i.getClass().getName() + "\t" + name + "\t" + h.type);
						}
					}
				}
				if (s % 20 == 19) System.out.println("seeds done " + (s + 1));
			}
		}
		System.out.println("SUMMARY balance report written, " + fails + " floors failed to build");
		System.exit(0);
	}

	//measured on the monster the floor made, so statues keep their weapon and mimics their depth
	private static String measure(Mob m) {
		Class<?> cls = m.getClass();
		String name = cls.getSimpleName();
		try {
			try { name = m.name(); } catch (Throwable ignored) {}
			long sum = 0; int max = 0, n = 400;
			for (int i = 0; i < n; i++) {
				int r = m.damageRoll();
				sum += r;
				if (r > max) max = r;
			}
			int acc = -1, def = -1;
			try { acc = m.attackSkill(Dungeon.hero); } catch (Throwable ignored) {}
			try { def = m.defenseSkill(Dungeon.hero); } catch (Throwable ignored) {}
			return cls.getName() + "\t" + name + "\t" + m.HT + "\t" + acc + "\t" + def + "\t" + String.format("%.2f", sum / (double) n) + "\t" + max;
		} catch (Throwable t) {
			return cls.getName() + "\t" + name + "\t-1\t-1\t-1\t-1\t-1";
		}
	}
}
