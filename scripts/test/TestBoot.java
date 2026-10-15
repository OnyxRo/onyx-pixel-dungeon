import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3FileHandle;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Preferences;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.Game;

import java.io.File;
import java.lang.reflect.Proxy;

/**
 * Starts enough of the game to build levels and run actors without a window. Shared by the
 * headless tests. Run from the assets folder so string and asset lookups work.
 */
public class TestBoot {

	public static void boot() throws Exception {
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
	}

	private static sun.misc.Unsafe unsafe;

	/** An object of any class with no constructor run and every field zero. Enough to stand in for a window-bound object. */
	@SuppressWarnings("unchecked")
	public static <T> T blank( Class<T> cls ) {
		try {
			if (unsafe == null) {
				java.lang.reflect.Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
				f.setAccessible(true);
				unsafe = (sun.misc.Unsafe) f.get(null);
			}
			return (T) unsafe.allocateInstance(cls);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Stands in for the parts of the game that need a window, so actors can fight and act headless.
	 * Call once after boot. Give each actor a sprite with {@link #sprite()}.
	 */
	public static void stubWindow() throws Exception {
		java.lang.reflect.Field f = com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.class.getDeclaredField("cellSelector");
		f.setAccessible(true);
		f.set(null, blank(com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector.class));
		Game.instance = blank(Game.class);
		com.watabou.noosa.Camera.main = new com.watabou.noosa.Camera( 0, 0, 320, 240, 1f );
	}

	/** A sprite that is not visible and does nothing, so code that tells a sprite to do things does not crash. */
	public static com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite sprite() {
		com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite s = new com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite();
		s.visible = false;
		return s;
	}
}
