/*
 * Onyx's Pixel Dungeon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.desktop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Adds every crash to crashes/crashes.jsonl in the project folder, one line per crash, so a Claude session can
 * read the whole list when Onyx says "fix bugs" (see scripts/crashes.py and the fix-bugs skill).
 * Only writes when the project folder exists on this computer, so a copy of the game elsewhere writes nothing.
 */
public class CrashLog {

	static File folder() {
		String override = System.getProperty("onyx.crashlog");
		if (override != null) return new File(override);
		File repo = new File(System.getProperty("user.home"), "Documents" + File.separator + "onyx-pixel-dungeon");
		if (!new File(repo, "scripts").isDirectory()) return null;
		return new File(repo, "crashes");
	}

	public static void record( Throwable throwable, String version, String thread ) {
		try {
			//the screenshot tour's bots crash on purpose sometimes, their crashes are read from the tour log
			if (System.getProperty("onyx.gallery") != null) return;
			File folder = folder();
			if (folder == null) return;
			if (!folder.isDirectory() && !folder.mkdirs()) return;

			StringWriter sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));

			String line = "{\"time\":\"" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\""
					+ ",\"version\":\"" + escape(String.valueOf(version)) + "\""
					+ ",\"thread\":\"" + escape(String.valueOf(thread)) + "\""
					+ ",\"error\":\"" + escape(String.valueOf(throwable)) + "\""
					+ ",\"stack\":\"" + escape(sw.toString()) + "\"}\n";

			try (Writer w = new OutputStreamWriter(new FileOutputStream(new File(folder, "crashes.jsonl"), true), StandardCharsets.UTF_8)) {
				w.write(line);
			}
		} catch (Throwable ignored) {
			//never let the crash log hide the crash box
		}
	}

	private static String escape( String s ) {
		StringBuilder b = new StringBuilder();
		for (char c : s.toCharArray()) {
			switch (c) {
				case '"': b.append("\\\""); break;
				case '\\': b.append("\\\\"); break;
				case '\n': b.append("\\n"); break;
				case '\r': break;
				case '\t': b.append("\\t"); break;
				default:
					if (c < 0x20) b.append(String.format("\\u%04x", (int) c));
					else b.append(c);
			}
		}
		return b.toString();
	}
}
