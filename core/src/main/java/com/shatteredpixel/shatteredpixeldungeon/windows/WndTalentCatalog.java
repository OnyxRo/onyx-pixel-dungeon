/*
 * Onyx's Pixel Dungeon
 * The talent catalog of Transformation PD (dongyusangyu), a list of every talent a metamorphosis can offer,
 * by tier and kind. Tap a talent to read it.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ui.Component;

public class WndTalentCatalog extends Window {

	private static final int WIDTH = 120;

	//the whole catalog
	public WndTalentCatalog(){
		this(0, null);
	}

	//one tier and kind, or everything when tier is 0
	public WndTalentCatalog(int onlyTier, Talent.TalentType onlyKind){
		super();
		int maxHeight = (int)(Camera.main.height * 0.8f);
		resize(WIDTH, maxHeight);

		ScrollPane pane = new ScrollPane(new Component());
		add(pane);
		pane.setRect(0, 0, WIDTH, maxHeight);
		Component content = pane.content();

		RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
		title.hardlight(TITLE_COLOR);
		title.setPos((WIDTH - title.width())/2f, 2);
		content.add(title);
		float pos = title.bottom() + 4;

		for (int tier = 1; tier <= 3; tier++){
			if (onlyTier != 0 && tier != onlyTier) continue;
			for (Talent.TalentType kind : TransformationTalents.KINDS){
				if (onlyKind != null && kind != onlyKind) continue;
				int perRow = WIDTH / (TalentButton.WIDTH + 2);
				int n = 0;
				float x = 0;
				boolean header = false;
				for (Talent t : Talent.values()){
					if (!TransformationTalents.inPool(t) || t.tier() != tier || t.type() != kind) continue;
					if (!header){
						RenderedTextBlock h = PixelScene.renderTextBlock(Messages.get(this, "tier", tier) + ", "
								+ Messages.get(ScrollOfMetamorphosis.class, kind.name().toLowerCase(java.util.Locale.ENGLISH)), 6);
						h.setPos(0, pos);
						content.add(h);
						pos = h.bottom() + 2;
						header = true;
					}
					TalentButton b = new TalentButton(tier, t, 0, TalentButton.Mode.INFO);
					b.setPos(x + 1, pos);
					content.add(b);
					n++;
					x += TalentButton.WIDTH + 2;
					if (n % perRow == 0){
						x = 0;
						pos += TalentButton.HEIGHT + 2;
					}
				}
				if (header && n % perRow != 0) pos += TalentButton.HEIGHT + 2;
				if (header) pos += 2;
			}
		}
		content.setSize(WIDTH, pos);
		pane.setSize(WIDTH, Math.min(maxHeight, (int) pos));
		resize(WIDTH, (int) Math.min(maxHeight, pos));
		pane.scrollTo(0, 0);
	}
}
