package com.shatteredpixel.shatteredpixeldungeon.items.weapon;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.DisposableMissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

import java.util.ArrayList;

//Elemental PD's Alchemist Flask, the Alchemist's version of the Spirit Bow. It throws an endless supply of flasks,
//each with a random harmful effect. It grows with the hero's level and can be augmented and enchanted.
//The mod's sniper special and its Great Fire Demon taunt are left out, the Alchemist has no sniper.
public class AlchemistFlask extends Weapon {

	public static final String AC_USE = "USE";

	{
		image = ItemSpriteSheet.ELIXIR_TOXIC;
		defaultAction = AC_USE;
		usesTargeting = true;
		unique = true;
		bones = false;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		actions.remove(AC_EQUIP);
		actions.add(AC_USE);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute(hero, action);
		if (action.equals(AC_USE)) {
			curUser = hero;
			curItem = this;
			GameScene.selectCell(thrower);
		}
	}

	@Override
	public String info() {
		String info = super.info();
		info += "\n\n" + Messages.get(AlchemistFlask.class, "stats",
				Math.round(augment.damageFactor(min())), Math.round(augment.damageFactor(max())), STRReq());
		if (STRReq() > Dungeon.hero.STR()) {
			info += " " + Messages.get(Weapon.class, "too_heavy");
		} else if (Dungeon.hero.STR() > STRReq()) {
			info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
		}
		if (augment == Augment.SPEED) info += "\n\n" + Messages.get(Weapon.class, "faster");
		else if (augment == Augment.DAMAGE) info += "\n\n" + Messages.get(Weapon.class, "stronger");
		if (enchantment != null && (cursedKnown || !enchantment.curse())) {
			info += "\n\n" + Messages.capitalize(Messages.get(Weapon.class, "enchanted", enchantment.name()));
			info += " " + enchantment.desc();
		}
		if (cursedKnown && cursed) info += "\n\n" + Messages.get(Weapon.class, "cursed");
		info += "\n\n" + Messages.get(MissileWeapon.class, "distance");
		return info;
	}

	@Override
	public int STRReq( int lvl ) {
		return STRReq(1, lvl);
	}

	@Override
	public int min( int lvl ) {
		return 1 + (Dungeon.hero == null ? 0 : Dungeon.hero.lvl / 5) + (curseInfusionBonus ? 1 : 0);
	}

	@Override
	public int max( int lvl ) {
		return 3 + (Dungeon.hero == null ? 0 : (int) (Dungeon.hero.lvl / 3f)) + (curseInfusionBonus ? 2 : 0);
	}

	@Override
	public int damageRoll( Char owner ) {
		int damage = augment.damageFactor(super.damageRoll(owner));
		if (owner instanceof Hero) {
			int exStr = ((Hero) owner).STR() - STRReq();
			if (exStr > 0) damage += Random.IntRange(0, exStr);
		}
		return damage;
	}

	@Override
	public int targetingPos( Hero user, int dst ) {
		return new Flask().targetingPos(user, dst);
	}

	@Override
	public int level() {
		return (Dungeon.hero == null ? 0 : Dungeon.hero.lvl / 5) + (curseInfusionBonus ? 1 : 0);
	}

	@Override
	public int buffedLvl() {
		return level();
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	public class Flask extends DisposableMissileWeapon {

		//rolled once per flask, the mod's odds out of 100
		private final int effect = Random.Int(1, 101);

		{
			if (effect <= 5) image = ItemSpriteSheet.POTION_GOLDEN;
			else if (effect <= 11) image = ItemSpriteSheet.POTION_CHARCOAL;
			else if (effect <= 19) image = ItemSpriteSheet.POTION_MAGENTA;
			else if (effect <= 29) image = ItemSpriteSheet.POTION_BISTRE;
			else if (effect <= 44) image = ItemSpriteSheet.POTION_CRIMSON;
			else if (effect <= 59) image = ItemSpriteSheet.POTION_JADE;
			else if (effect <= 74) image = ItemSpriteSheet.POTION_AZURE;
			else if (effect <= 94) image = ItemSpriteSheet.POTION_SILVER;
			else image = ItemSpriteSheet.POTION_AMBER;
			setID = 0;
		}

		@Override
		public int min() {
			return AlchemistFlask.this.min(AlchemistFlask.this.buffedLvl());
		}

		@Override
		public int max() {
			return AlchemistFlask.this.max(AlchemistFlask.this.buffedLvl());
		}

		@Override
		public ArrayList<String> actions( Hero hero ) {
			return new ArrayList<>();
		}

		@Override
		public String defaultAction() {
			return null;
		}

		@Override
		public int defaultQuantity() {
			return 1;
		}

		@Override
		public int damageRoll( Char owner ) {
			return AlchemistFlask.this.damageRoll(owner);
		}

		@Override
		public boolean hasEnchant( Class<? extends Enchantment> type, Char owner ) {
			return AlchemistFlask.this.hasEnchant(type, owner);
		}

		@Override
		public int proc( Char attacker, Char defender, int damage ) {
			return AlchemistFlask.this.proc(attacker, defender, damage);
		}

		@Override
		public float delayFactor( Char user ) {
			return AlchemistFlask.this.delayFactor(user);
		}

		@Override
		public int STRReq( int lvl ) {
			return AlchemistFlask.this.STRReq();
		}

		@Override
		protected void onThrow( int cell ) {
			Char enemy = Actor.findChar(cell);
			if (enemy == null || enemy == curUser) {
				parent = null;
				Splash.at(cell, 0xCC99FFFF, 1);
				return;
			}
			if (!curUser.shoot(enemy, this)) {
				Splash.at(cell, 0xCC99FFFF, 1);
				return;
			}
			if (effect <= 5) Buff.affect(enemy, Paralysis.class, 2f);
			else if (effect <= 11) Buff.affect(enemy, Roots.class, Roots.DURATION);
			else if (effect <= 19) Buff.affect(enemy, Poison.class).set(3 + Dungeon.scalingDepth() / 2);
			else if (effect <= 29) Buff.affect(enemy, Cripple.class, Cripple.DURATION);
			else if (effect <= 44) {
				GameScene.add(Blob.seed(cell, 2, Fire.class));
				CellEmitter.get(cell).burst(FlameParticle.FACTORY, 2);
			} else if (effect <= 59) GameScene.add(Blob.seed(cell, 20 + 5 * Dungeon.scalingDepth(), ToxicGas.class));
			else if (effect <= 74) GameScene.add(Blob.seed(cell, 10, Freezing.class));
			else if (effect >= 95) Buff.affect(enemy, Corrosion.class).set(2f, 1 + Dungeon.scalingDepth() / 3);
		}

		@Override
		public void throwSound() {
		}
	}

	private final CellSelector.Listener thrower = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer target ) {
			if (target != null) new Flask().cast(curUser, target);
		}

		@Override
		public String prompt() {
			return Messages.get(AlchemistFlask.class, "prompt");
		}
	};
}
