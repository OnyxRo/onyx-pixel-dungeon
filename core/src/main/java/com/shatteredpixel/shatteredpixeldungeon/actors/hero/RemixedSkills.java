/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Re-Remixed Dungeon, by QuasiStellar, ported into Onyx's Pixel Dungeon
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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DeathlingMob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.GnollSheep;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

//reremixed, Re-Remixed Dungeon's skills for the Necromancer and the Gnoll, paid for with mana.
//The mana is the hero's MP pool that Cursed PD's power items already use, so there is one blue bar.
//The Necromancer only gains mana by killing with a skull embedded weapon. The Gnoll's mana is his
//satiety, so his skills make him hungrier and eating fills them back up. The Elf has none, as in the mod.
public class RemixedSkills {

	public enum Skill {
		DEATHLING(5), PLUNDER(10), REVITALIZATION(10),
		MADNESS(5), DECOY(10), DISGUISE(15);

		public final int cost;

		Skill( int cost ){
			this.cost = cost;
		}

		public String title(){
			return Messages.get(RemixedSkills.class, name().toLowerCase() + "_name");
		}

		public String desc(){
			return Messages.get(RemixedSkills.class, name().toLowerCase() + "_desc", cost);
		}
	}

	public static final int NECRO_MANA = 20;
	public static final int GNOLL_MANA = 25;
	private static final float GNOLL_HUNGER_PER_MANA = 18f;

	public static boolean usesSkills( Hero hero ){
		return hero != null && (hero.heroClass == HeroClass.NECROMANCER || hero.heroClass == HeroClass.GNOLL);
	}

	public static Skill[] skills( Hero hero ){
		if (hero == null) return new Skill[0];
		if (hero.heroClass == HeroClass.NECROMANCER) return new Skill[]{Skill.DEATHLING, Skill.PLUNDER, Skill.REVITALIZATION};
		if (hero.heroClass == HeroClass.GNOLL) return new Skill[]{Skill.MADNESS, Skill.DECOY, Skill.DISGUISE};
		return new Skill[0];
	}

	//starting mana and the skill button, called from the class's starting kit
	public static void initHero( Hero hero ){
		if (hero.heroClass == HeroClass.NECROMANCER){
			hero.MAX_MP = NECRO_MANA;
			hero.MP = 0;
		} else if (hero.heroClass == HeroClass.GNOLL){
			hero.MAX_MP = GNOLL_MANA;
			hero.MP = GNOLL_MANA;
		}
		if (usesSkills(hero)) Buff.affect(hero, SkillAction.class);
	}

	//the Gnoll's mana is read from his hunger
	public static void syncMana( Hero hero ){
		if (hero == null || hero.heroClass != HeroClass.GNOLL) return;
		Hunger h = hero.buff(Hunger.class);
		if (h == null) return;
		int mp = Math.round((Hunger.STARVING - h.hunger()) / GNOLL_HUNGER_PER_MANA);
		hero.MP = Math.max(0, Math.min(hero.MAX_MP, mp));
	}

	public static void earnMana( Hero hero, int amount ){
		hero.MP = Math.max(0, Math.min(hero.MP + amount, hero.MAX_MP));
		if (hero.heroClass == HeroClass.GNOLL){
			Hunger h = hero.buff(Hunger.class);
			if (h != null) h.affectHunger(amount * GNOLL_HUNGER_PER_MANA);
			syncMana(hero);
		}
	}

	//MpRegen asks this first. Re-Remixed's classes do not refill over time, the Gnoll follows his hunger instead
	public static boolean manaTick( Hero hero ){
		if (!usesSkills(hero)) return false;
		syncMana(hero);
		return true;
	}

	public static boolean visible( Hero hero, Skill s ){
		switch (s){
			case REVITALIZATION:
				for (Mob m : Dungeon.level.mobs) if (m instanceof DeathlingMob) return true;
				return false;
			case MADNESS:
				for (Mob m : Dungeon.level.mobs){
					if (m.alignment == Char.Alignment.ENEMY && Dungeon.level.adjacent(hero.pos, m.pos)) return true;
				}
				return false;
			default:
				return true;
		}
	}

	public static boolean canUse( Hero hero, Skill s ){
		syncMana(hero);
		return hero.MP >= s.cost && visible(hero, s);
	}

	//runs one skill, returns false when it could not be used
	public static boolean use( Hero hero, Skill s ){
		if (!canUse(hero, s)) return false;
		switch (s){
			case DEATHLING: return deathling(hero);
			case PLUNDER:
				Buff.affect(hero, Necromancy.class, 20f);
				finish(hero, s);
				return true;
			case REVITALIZATION:
				for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])){
					if (m instanceof DeathlingMob) Buff.affect(m, Healing.class).setHeal((int)(0.5f*m.HT + 30), 0.25f, 0);
				}
				finish(hero, s);
				return true;
			case MADNESS:
				Invisibility.dispel();
				for (int n : PathFinder.NEIGHBOURS8){
					Char ch = Actor.findChar(hero.pos + n);
					if (ch != null && ch.alignment == Char.Alignment.ENEMY) hero.attack(ch);
				}
				if (hero.sprite != null) Sample.INSTANCE.play(Assets.Sounds.HIT);
				finish(hero, s);
				return true;
			case DECOY:
				GameScene.selectCell(new DecoyTarget());
				return true;
			case DISGUISE: return disguise(hero);
		}
		return false;
	}

	private static void finish( Hero hero, Skill s ){
		earnMana(hero, -s.cost);
		hero.spend(1f);
		hero.busy();
		if (hero.sprite != null){
			hero.sprite.operate(hero.pos);
			hero.next();
		}
	}

	private static boolean deathling( Hero hero ){
		int count = 0;
		for (Mob m : Dungeon.level.mobs) if (m instanceof DeathlingMob) count++;
		if (count >= 3){
			GLog.w(Messages.get(RemixedSkills.class, "three"));
			return false;
		}
		ArrayList<Integer> cells = new ArrayList<>();
		for (int n : PathFinder.NEIGHBOURS8){
			int p = hero.pos + n;
			if (Actor.findChar(p) == null && Dungeon.level.passable[p]) cells.add(p);
		}
		if (cells.isEmpty()){
			GLog.w(Messages.get(RemixedSkills.class, "no_space"));
			return false;
		}
		DeathlingMob.spawnAt(Random.element(cells));
		finish(hero, Skill.DEATHLING);
		return true;
	}

	private static boolean disguise( Hero hero ){
		Invisibility.dispel();
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
			if (mob.alignment == Char.Alignment.ENEMY && Dungeon.level.heroFOV[mob.pos]){
				Buff.affect(mob, Charm.class, 20f).object = hero.id();
				if (mob.sprite != null) mob.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 5);
			}
		}
		//the Gnoll throws off his armor to pass as a harmless beast
		Armor armor = hero.belongings.armor;
		if (armor != null && !armor.cursed && armor.doUnequip(hero, false, false)){
			Dungeon.level.drop(armor, hero.pos);
			if (hero.sprite instanceof HeroSprite) ((HeroSprite) hero.sprite).updateArmor();
		}
		if (hero.sprite != null){
			hero.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 5);
			Sample.INSTANCE.play(Assets.Sounds.CHARMS);
		}
		finish(hero, Skill.DISGUISE);
		return true;
	}

	private static class DecoyTarget extends CellSelector.Listener {
		@Override
		public void onSelect( Integer cell ){
			Hero hero = Dungeon.hero;
			if (cell == null || hero == null) return;
			if (!Dungeon.level.heroFOV[cell] || Dungeon.level.solid[cell] || Dungeon.level.pit[cell] || Actor.findChar(cell) != null){
				GLog.w(Messages.get(RemixedSkills.class, "bad_target"));
				return;
			}
			if (!canUse(hero, Skill.DECOY)) return;
			GnollSheep.spawn(cell);
			finish(hero, Skill.DECOY);
		}

		@Override
		public String prompt(){
			return Messages.get(RemixedSkills.class, "decoy_prompt");
		}
	}

	//the skill button, one action button that opens the list of skills with their costs
	public static class SkillAction extends Buff implements ActionIndicator.Action {

		{
			revivePersists = true;
			type = buffType.NEUTRAL;
		}

		@Override
		public boolean attachTo( Char target ){
			if (super.attachTo(target)){
				ActionIndicator.setAction(this);
				return true;
			}
			return false;
		}

		@Override
		public boolean act(){
			//other buttons can take the slot for a while, take it back when it is free
			if (ActionIndicator.action == null && target instanceof Hero && usesSkills((Hero) target)) ActionIndicator.setAction(this);
			if (target instanceof Hero) syncMana((Hero) target);
			spend(TICK);
			return true;
		}

		@Override
		public void restoreFromBundle( Bundle bundle ){
			super.restoreFromBundle(bundle);
			ActionIndicator.setAction(this);
		}

		@Override
		public String actionName(){
			return Messages.get(RemixedSkills.class, "skills");
		}

		@Override
		public int actionIcon(){
			return Dungeon.hero != null && Dungeon.hero.heroClass == HeroClass.GNOLL ? HeroIcon.SHOCKWAVE : HeroIcon.DEATH_MARK;
		}

		@Override
		public int indicatorColor(){
			return 0x4A7BFF;
		}

		@Override
		public void doAction(){
			final Hero hero = Dungeon.hero;
			if (hero == null) return;
			final Skill[] list = skills(hero);
			syncMana(hero);
			String[] options = new String[list.length];
			for (int i = 0; i < list.length; i++){
				options[i] = Messages.get(RemixedSkills.class, "option", list[i].title(), list[i].cost);
			}
			StringBuilder msg = new StringBuilder(Messages.get(RemixedSkills.class, "mana", hero.MP, hero.MAX_MP));
			for (Skill s : list) msg.append("\n\n_").append(s.title()).append("_ ").append(s.desc());
			GameScene.show(new WndOptions(Messages.get(RemixedSkills.class, "skills"), msg.toString(), options){
				@Override
				protected boolean enabled( int index ){
					return canUse(hero, list[index]);
				}

				@Override
				protected void onSelect( int index ){
					use(hero, list[index]);
				}
			});
		}
	}

	//Plunder. While it lasts, monsters the Necromancer's side kills rise again on his side
	public static class Necromancy extends FlavourBuff {
		{
			type = buffType.POSITIVE;
			announced = true;
		}

		@Override
		public int icon(){
			return BuffIndicator.SACRIFICE;
		}

		@Override
		public float iconFadePercent(){
			return Math.max(0, (20f - visualcooldown()) / 20f);
		}
	}

	//the Gnoll's rage after a kill, his blows land 30 percent harder
	public static class Rage extends FlavourBuff {
		{
			type = buffType.POSITIVE;
			announced = true;
		}

		@Override
		public int icon(){
			return BuffIndicator.RAGE;
		}

		@Override
		public float iconFadePercent(){
			return Math.max(0, (5f - visualcooldown()) / 5f);
		}
	}

	//Mob.die. Skull kills give the Necromancer mana, kills enrage the Gnoll, and under Plunder the dead rise as allies.
	//Returns true when the monster rose again instead of dying.
	public static boolean onMobDie( Mob mob, Object cause ){
		Hero hero = Dungeon.hero;
		if (hero == null || mob.alignment != Char.Alignment.ENEMY) return false;
		if (cause == hero){
			if (hero.heroClass == HeroClass.NECROMANCER && hero.belongings.weapon() instanceof MeleeWeapon
					&& ((MeleeWeapon) hero.belongings.weapon()).skull != null){
				earnMana(hero, 1);
			}
			if (hero.heroClass == HeroClass.GNOLL) Buff.prolong(hero, Rage.class, 5f);
		}
		if (hero.buff(Necromancy.class) != null && !mob.properties().contains(Char.Property.BOSS)
				&& !mob.properties().contains(Char.Property.MINIBOSS) && !mob.isImmune(Corruption.class)){
			mob.HP = mob.HT;
			for (Buff b : mob.buffs()){
				if (b.type == Buff.buffType.NEGATIVE) b.detach();
			}
			if (hero.sprite != null) AllyBuff.affectAndLoot(mob, hero, Corruption.class);
			else Buff.affect(mob, Corruption.class);
			return mob.buff(Corruption.class) != null;
		}
		return false;
	}

	public static int attackProc( Hero hero, Char enemy, int damage ){
		return hero.buff(Rage.class) != null ? Math.round(damage * 1.3f) : damage;
	}

	//the Necromancer rises from the dead once his mana is full, and it empties
	public static boolean necroRevive( Hero hero ){
		if (hero.heroClass != HeroClass.NECROMANCER || hero.MAX_MP <= 0 || hero.MP < hero.MAX_MP) return false;
		hero.HP = hero.HT;
		hero.MP = 0;
		PotionOfHealing.cure(hero);
		Buff.detach(hero, Paralysis.class);
		hero.spend(-hero.cooldown());
		if (hero.sprite != null){
			new Flare(8, 32).color(0xFFFFFF, true).show(hero.sprite, 2f);
			CellEmitter.get(hero.pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
			Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
		}
		GLog.w(Messages.get(RemixedSkills.class, "necro_revive"));
		return true;
	}
}
