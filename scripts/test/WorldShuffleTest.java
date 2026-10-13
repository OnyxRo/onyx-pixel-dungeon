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

		File dir = java.nio.file.Files.createTempDirectory("onyxpd-test").toFile();
		File prefs = new File(dir, "settings.xml");
		com.badlogic.gdx.utils.GdxNativesLoader.load();
		Gdx.files = new Lwjgl3Files();
		Gdx.app = (Application) Proxy.newProxyInstance(Application.class.getClassLoader(),
				new Class[]{Application.class}, (proxy, m, a) -> {
			switch (m.getName()) {
				case "getPreferences":
					return new Lwjgl3Preferences(new Lwjgl3FileHandle(prefs, Files.FileType.Absolute));
				case "getType": return Application.ApplicationType.Desktop;
				case "getVersion": return 0;
				case "log": case "error": case "debug":
					System.out.println("[gdx " + a[0] + "] " + a[1]);
					return null;
			}
			Class<?> r = m.getReturnType();
			if (r == boolean.class) return false;
			if (r == int.class || r == long.class) return r == int.class ? (Object) 0 : (Object) 0L;
			return null;
		});
		com.shatteredpixel.shatteredpixeldungeon.SPDSettings.set(
				new Lwjgl3Preferences(new Lwjgl3FileHandle(prefs, Files.FileType.Absolute)));
		com.watabou.utils.FileUtils.setDefaultFileProperties(Files.FileType.Absolute, dir.getAbsolutePath() + "/");
		Game.version = "test";
		Game.versionCode = 9999;
		Messages.setup(Languages.ENGLISH);

		String[] names = {"Sewers", "Prison", "Caves", "City", "Halls"};
		Map<String, Integer> counts = new HashMap<>();
		int floors = 0, failures = 0;
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
					String w = l.getClass().getSimpleName().replace("Level", "");
					if (WorldShuffle.shuffles(d)) {
						String world = names[WorldShuffle.worldOf(l)];
						counts.merge(world, 1, Integer::sum);
						if (WorldShuffle.worldOf(l) == WorldShuffle.homeWorld(d)) {
							throw new IllegalStateException("shuffled floor kept its home world");
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
		System.out.println("floors generated: " + floors + ", failures: " + failures);
		System.out.println("shuffled-floor worlds: " + counts);
		System.exit(failures == 0 ? 0 : 1);
	}
}
