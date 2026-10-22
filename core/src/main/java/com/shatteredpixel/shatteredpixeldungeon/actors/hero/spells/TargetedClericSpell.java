/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;

public abstract class TargetedClericSpell extends ClericSpell {

	@Override
	public void onCast(HolyTome tome, Hero hero ){
		GameScene.selectCell(new CellSelector.Listener() {
			@Override
			public void onSelect(Integer cell) {
				onTargetSelected(tome, hero, cell);
			}

			@Override
			public String prompt() {
				return targetingPrompt();
			}
		});
	}

	@Override
	public int targetingFlags(){
		return Ballistica.MAGIC_BOLT;
	}

	protected String targetingPrompt(){
		return Messages.get(this, "prompt");
	}

	protected abstract void onTargetSelected(HolyTome tome, Hero hero, Integer target);

	//rearranged, the Enchanter's spells hang short debuffs on an enemy, one for each charge spent
	private static final java.util.HashMap<Class<? extends com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff>, Float> DEBUFFS = new java.util.HashMap<>();
	static {
		DEBUFFS.put(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple.class,   1f);
		DEBUFFS.put(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness.class, 1f);
		DEBUFFS.put(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror.class,    1f);
		DEBUFFS.put(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill.class,     1f);
		DEBUFFS.put(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots.class,     1f);
		DEBUFFS.put(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo.class,   1f);
		DEBUFFS.put(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok.class,      1f);
		DEBUFFS.put(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Daze.class,      1f);
		DEBUFFS.put(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost.class,     1f);
	}

	public static void onEnchanterSpellCast(com.shatteredpixel.shatteredpixeldungeon.actors.Char target, float spellCost) {
		int chargeUse = (int)spellCost;
		if (target.alignment == com.shatteredpixel.shatteredpixeldungeon.actors.Char.Alignment.ENEMY) {
			for (int i = 0; i < chargeUse; i++) {
				java.util.HashMap<Class<? extends com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff>, Float> debuffs = new java.util.HashMap<>(DEBUFFS);
				for (com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff existing : target.buffs()){
					if (existing instanceof com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff && debuffs.containsKey(existing.getClass())) {
						debuffs.put(((com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff) existing).getClass(), 0f);
					}
				}
				for (Class<? extends com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff> toAssign : debuffs.keySet()){
					if (debuffs.get(toAssign) > 0 && target.isImmune(toAssign)){
						debuffs.put(toAssign, 0f);
					}
				}
				Class<? extends com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff> debuffCls = com.watabou.utils.Random.chances(debuffs);
				if (debuffCls != null){
					com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.append(target, debuffCls, 5);
				} else {
					com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(target, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom.class);
				}
			}
		}
	}

}
