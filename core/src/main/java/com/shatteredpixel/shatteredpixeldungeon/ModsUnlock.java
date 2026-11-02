package com.shatteredpixel.shatteredpixeldungeon;

import com.badlogic.gdx.Gdx;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.watabou.utils.DeviceCompat;

import java.util.HashSet;

/**
 * The copy shared on GitHub starts out as plain Shattered. Until the player beats Yog on floor 25,
 * floors keep their home worlds, only Shattered's six heroes, challenges and items are there, and the
 * mod buttons are hidden. Beating Yog opens everything at once.
 *
 * Onyx's own copy is built with -Donyx.dev=true (make-exe.ps1). It always has everything, plus the
 * testing tools, unless Friend view is turned on in its settings, which shows the shared copy with
 * its own unlock, kept apart from his.
 */
public class ModsUnlock {

	/** Set by the desktop launcher. The headless tests never run it, so they keep every mod. */
	public static boolean dev = true;

	private static final String FRIEND_VIEW   = "onyx_friend_view";
	private static final String UNLOCKED      = "onyx_mods_unlocked";
	private static final String UNLOCKED_FV   = "onyx_friend_mods_unlocked";
	private static final String INTRO_SEEN    = "onyx_intro_seen";
	private static final String INTRO_SEEN_FV = "onyx_friend_intro_seen";
	private static final String NEWS          = "onyx_unlock_news";

	/** Onyx's copy, a debug build, the screenshot tour or a headless test. */
	public static boolean devBuild(){
		return dev || DeviceCompat.isDebug();
	}

	public static boolean friendView(){
		return devBuild() && SPDSettings.getBoolean(FRIEND_VIEW, false);
	}

	public static void friendView( boolean on ){
		SPDSettings.put(FRIEND_VIEW, on);
	}

	/** The shared copy, or Onyx looking at it. No sandbox or other testing tools. */
	public static boolean publicMode(){
		return !devBuild() || friendView();
	}

	public static boolean unlocked(){
		return SPDSettings.getBoolean(friendView() ? UNLOCKED_FV : UNLOCKED, false);
	}

	/** True while the game should play as plain Shattered. */
	public static boolean locked(){
		return publicMode() && !unlocked();
	}

	/** Called when Yog falls. */
	public static void unlock(){
		if (!locked()) return;
		SPDSettings.put(friendView() ? UNLOCKED_FV : UNLOCKED, true);
		SPDSettings.put(NEWS, true);
	}

	/** Friend view only, so Onyx can see the locked start again. */
	public static void relock(){
		SPDSettings.put(UNLOCKED_FV, false);
		SPDSettings.put(INTRO_SEEN_FV, false);
	}

	/** The welcome window, shown once on the title screen. */
	public static boolean introDue(){
		return publicMode() && !SPDSettings.getBoolean(friendView() ? INTRO_SEEN_FV : INTRO_SEEN, false);
	}

	public static void introShown(){
		SPDSettings.put(friendView() ? INTRO_SEEN_FV : INTRO_SEEN, true);
	}

	/** The window saying the mods are open, shown once on the next title screen. */
	public static boolean newsDue(){
		return publicMode() && SPDSettings.getBoolean(NEWS, false);
	}

	public static void newsShown(){
		SPDSettings.put(NEWS, false);
	}

	public static boolean isShatteredHero( HeroClass cl ){
		switch (cl){
			case WARRIOR: case MAGE: case ROGUE: case HUNTRESS: case DUELIST: case CLERIC:
				return true;
			default:
				return false;
		}
	}

	/** Whether a badge was one of Shattered 4.0.2's, listed as badge.NAME in shattered-classes.txt. */
	public static boolean isOriginalBadge( Badges.Badge badge ){
		isOriginal(ModsUnlock.class); //loads the list
		return listMissing || original.contains("badge." + badge.name());
	}

	private static HashSet<String> original;
	private static boolean listMissing;

	/**
	 * Whether a class was part of Shattered 4.0.2. shattered-classes.txt lists every class of the
	 * first commit (0e810728), built by a few lines of Python with git show, see PORTING.md.
	 */
	public static boolean isOriginal( Class<?> c ){
		if (c == null) return false;
		if (original == null){
			HashSet<String> set = new HashSet<>();
			try {
				for (String line : Gdx.files.internal("shattered-classes.txt").readString("UTF-8").split("\n")){
					line = line.trim();
					if (!line.isEmpty()) set.add(line);
				}
			} catch (Exception e){
				ShatteredPixelDungeon.reportException(e);
				set = null;
			}
			original = set == null ? new HashSet<>() : set;
			listMissing = set == null;
		}
		//without the list nothing is filtered
		if (listMissing) return true;
		//anonymous classes count as the class they sit in, named nested classes are listed as Outer$Inner
		String[] parts = c.getName().split("\\$");
		String name = parts[0];
		if (!original.contains(name)) return false;
		for (int i = 1; i < parts.length; i++){
			if (parts[i].isEmpty() || Character.isDigit(parts[i].charAt(0))) break;
			if (!original.contains(parts[0] + "$" + parts[i])) return false;
		}
		return true;
	}
}
