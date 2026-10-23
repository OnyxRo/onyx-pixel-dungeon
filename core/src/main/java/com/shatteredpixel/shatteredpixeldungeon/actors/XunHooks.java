/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Xun's Pixel Dungeon, by X-U-N-123, ported into Onyx's Pixel Dungeon
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

package com.shatteredpixel.shatteredpixeldungeon.actors;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.DMdrill;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Fetter;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.HiddenBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LightFlail;

//xun, hooks Xun's PD's items and monsters need in Shattered's shared classes (Char, Mob, Hero).
//Each call site is one line naming a method here. Talent hooks are in actors/hero/XunTalents.
public class XunHooks {

	//Char.speed, the DM drill's overload doubles speed
	public static float speed(Char ch, float speed){
		DMdrill.DMcombo combo = ch.buff(DMdrill.DMcombo.class);
		if (combo != null && combo.isOverloading()) speed *= 2f;
		return speed;
	}

	//Char.damage, the dark gold sword takes the damage off the target's max health too
	public static void afterDamage(Char ch, int dmg){
		com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Darkgoldsword.HTDecreaseTracker t
				= ch.buff(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Darkgoldsword.HTDecreaseTracker.class);
		if (t != null) {
			ch.HT = Math.max(1, ch.HT - dmg);
			t.detach();
		}
	}

	//the Zhouyi's ability swaps places with its target, as Xun's PD's Char.swapPos did
	public static void swapPos(Char a, Char b){
		if (!b.isAlive()) return;
		if (Char.hasProp(a, Char.Property.IMMOVABLE) || Char.hasProp(b, Char.Property.IMMOVABLE)) return;
		if (!com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.passable[a.pos] && !b.flying) return;
		if (a.properties().contains(Char.Property.LARGE) && !com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.openSpace[b.pos]
				|| b.properties().contains(Char.Property.LARGE) && !com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.openSpace[a.pos]) return;
		int oldPos = a.pos;
		int newPos = b.pos;
		a.pos = newPos;
		b.pos = oldPos;
		com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation.appear(a, newPos);
		com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation.appear(b, oldPos);
		com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.occupyCell(a);
		com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.occupyCell(b);
		if (a == com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero || b == com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero){
			com.shatteredpixel.shatteredpixeldungeon.Dungeon.observe();
			com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.updateFog();
		}
	}

	//the peaceful enchantment, a struck monster does not wake or turn on its attacker for that one hit
	public static boolean pacified(Char ch){
		return ch.buff(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Peaceful.PeacefulTracker.class) != null;
	}

	//Char.damage, the peace ends once the hit has landed
	public static void endPeace(Char ch){
		com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.detach(ch, com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Peaceful.PeacefulTracker.class);
	}

	//the elixir of overdraft hands out free turns, Char.spendConstant is protected so it goes through here
	public static void spendConstant(Char ch, float time){
		ch.spendConstant(time);
	}

	//the food pouch keeps raw meat clean and frozen meat lucky
	public static boolean hasFoodPocket(Char ch){
		return ch instanceof Hero && ((Hero) ch).belongings.getItem(com.shatteredpixel.shatteredpixeldungeon.items.bags.FoodPocket.class) != null;
	}

	//the Explorer's shovel and the Engineer's multitool cannot be transmuted (filled in by the hero port)
	public static boolean isHeroStarterTool(Object item){
		return false;
	}

	//the elixir of crash course trains accuracy and evasion like a level up
	public static void incSkills(Hero hero, int attack, int defense){
		hero.xunIncSkills(attack, defense);
	}

	//Char.resist, the elemental mask softens elemental harm for the hero wearing it
	public static float resist(Char ch, Class effect){
		if (!(ch instanceof Hero)) return 1f;
		Hero hero = (Hero) ch;
		com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ElementalMask mask = null;
		if (hero.belongings.artifact() instanceof com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ElementalMask)
			mask = (com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ElementalMask) hero.belongings.artifact();
		if (hero.belongings.misc() instanceof com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ElementalMask)
			mask = (com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ElementalMask) hero.belongings.misc();
		return mask == null ? 1f : mask.resist(effect);
	}

	//Char.isImmune, the frost glyph keeps its wearer from freezing
	public static void addImmunities(Char ch, java.util.HashSet<Class> immunes){
		if (ch.glyphLevel(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Freezingglyph.class) >= 0){
			immunes.add(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost.class);
			immunes.add(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill.class);
		}
	}

	//Char.stealth
	public static float stealth(Char ch){
		float stealth = 0;
		if (ch.buff(com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfConcealment.Conceal.class) != null) stealth += 4;
		return stealth;
	}

	//Level.feeling, the broken lock turns some plain floors into large or secret ones. Random is only called while the
	//hero carries the lock, so runs without it build the same floors as before
	public static com.shatteredpixel.shatteredpixeldungeon.levels.Level.Feeling wornLockFeeling(){
		float chance = com.shatteredpixel.shatteredpixeldungeon.items.trinkets.WornLock.overrideNormalLevelChance();
		if (chance > 0 && com.watabou.utils.Random.Float() < chance){
			return com.shatteredpixel.shatteredpixeldungeon.items.trinkets.WornLock.getNextFeeling();
		}
		return com.shatteredpixel.shatteredpixeldungeon.levels.Level.Feeling.NONE;
	}

	//Mob.createLoot and RegularLevel.createItems, the magical gem turns armor into wands and solidified metal
	//turns melee weapons into thrown weapons. Random is only called while the trinket is carried
	public static com.shatteredpixel.shatteredpixeldungeon.items.Item replaceDrop(com.shatteredpixel.shatteredpixeldungeon.items.Item item){
		if (item == null) return null;
		float metal = com.shatteredpixel.shatteredpixeldungeon.items.trinkets.SolidifiedMetal.missileReplaceChance();
		if (metal > 0 && item instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon
				&& com.watabou.utils.Random.Float() < metal){
			int tier = Math.max(1, Math.min(5, ((com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon) item).tier));
			com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon m = (com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon)
					com.shatteredpixel.shatteredpixeldungeon.items.Generator.random(com.shatteredpixel.shatteredpixeldungeon.items.Generator.misTiers[tier - 1]);
			if (m != null) {
				m.quantity(m.quantity() + Math.max(0, item.level()));
				m.cursed = item.cursed;
				m.enchant(((com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon) item).enchantment);
				return m;
			}
		}
		float gem = com.shatteredpixel.shatteredpixeldungeon.items.trinkets.MagicalGem.wandReplaceChance();
		if (gem > 0 && item instanceof com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor
				&& com.watabou.utils.Random.Float() < gem){
			com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand w = (com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand)
					com.shatteredpixel.shatteredpixeldungeon.items.Generator.random(com.shatteredpixel.shatteredpixeldungeon.items.Generator.Category.WAND);
			if (w != null) {
				w.level(item.level());
				w.cursed = item.cursed;
				if (((com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor) item).hasGoodGlyph()) w.cursedKnown = true;
				return w;
			}
		}
		return item;
	}

	//Mob.die, the cursed coin raises wraiths from fallen enemies and makes wraiths drop gold
	public static void onMobDied(com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob mob){
		if (mob instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith){
			float gold = com.shatteredpixel.shatteredpixeldungeon.items.trinkets.CursedCoin.dropGoldAmount();
			if (gold > 0 && com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero != null){
				int amount = (int)(com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero.lvl * gold);
				if (amount > 0) com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.drop(new com.shatteredpixel.shatteredpixeldungeon.items.Gold(amount), mob.pos).sprite.drop(mob.pos);
			}
		} else if (mob.alignment == Char.Alignment.ENEMY){
			float chance = com.shatteredpixel.shatteredpixeldungeon.items.trinkets.CursedCoin.summonWraithChance();
			if (chance > 0 && com.watabou.utils.Random.Float() < chance){
				com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith.spawnAt(mob.pos, com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith.class);
				if (com.shatteredpixel.shatteredpixeldungeon.Dungeon.level.heroFOV[mob.pos]) {
					com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter.get(mob.pos).burst(com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle.CURSE, 6);
					com.watabou.noosa.audio.Sample.INSTANCE.play(com.shatteredpixel.shatteredpixeldungeon.Assets.Sounds.CURSED);
				}
			}
		}
	}

	//ShopRoom.generateItems, the gold member card improves some of the stock
	public static void improveShopItems(java.util.ArrayList<com.shatteredpixel.shatteredpixeldungeon.items.Item> items){
		float chance = com.shatteredpixel.shatteredpixeldungeon.items.trinkets.MemberCard.betterItemChance();
		if (chance <= 0) return;
		for (com.shatteredpixel.shatteredpixeldungeon.items.Item i : items){
			if (com.watabou.utils.Random.Float() >= chance) continue;
			if (i instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon){
				i.upgrade();
				if (com.watabou.utils.Random.Int(3) == 0) ((com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon) i).enchant();
			} else if (i instanceof com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor){
				i.upgrade();
				if (com.watabou.utils.Random.Int(3) == 0) ((com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor) i).inscribe();
			} else if (i instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon
					|| i instanceof com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb
					|| i instanceof com.shatteredpixel.shatteredpixeldungeon.items.spells.Spell){
				i.quantity(i.quantity() + 1);
			}
		}
	}

	//Hero.rest, waiting locks a hidden blade again
	public static void onRest(Hero hero){
		com.shatteredpixel.shatteredpixeldungeon.actors.hero.XunTalents.onRest(hero);
		KindOfWeapon wep = hero.belongings.weapon();
		if (wep instanceof HiddenBlade) ((HiddenBlade) wep).lock(true);
		wep = hero.belongings.secondWep();
		if (wep instanceof HiddenBlade) ((HiddenBlade) wep).lock(true);
	}

	//flails of the mod, they spin like Shattered's flail and cannot surprise attack
	public static boolean isSpinFlail(Object wep){
		return wep instanceof LightFlail || wep instanceof Fetter;
	}

}
