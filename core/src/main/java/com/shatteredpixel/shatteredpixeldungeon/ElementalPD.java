package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.DewVial;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.EmptyPotionFlask;

//Elemental PD (Meel703). Its run wide rules are game modes, each off unless picked in the Game modes window.
public class ElementalPD {

	public static final String MOD = "Elemental Pixel Dungeon";

	//drinking a potion leaves an empty flask, and brewing a potion from seeds needs one
	public static final String FLASKS = "elemental_flasks";
	//every hero gets a small extra from each meal, as in the mod
	public static final String MEALS  = "elemental_meals";
	//every hero starts with a dew vial
	public static final String DEW    = "elemental_dew";

	public static void register(){
		GameModes.register(FLASKS, MOD, 1, null);
		GameModes.register(MEALS, MOD, 1, null);
		GameModes.register(DEW, MOD, 1, null);
	}

	public static void onStart( Hero hero ){
		if (GameModes.on(DEW) && hero.belongings.getItem(DewVial.class) == null){
			new DewVial().collect();
		}
		if (GameModes.on(FLASKS)){
			new EmptyPotionFlask().quantity(2).collect();
		}
	}

	public static void onPotionDrunk( Hero hero ){
		if (!GameModes.on(FLASKS) || hero == null) return;
		Item flask = new EmptyPotionFlask();
		if (!flask.collect() && Dungeon.level != null) Dungeon.level.drop(flask, hero.pos);
	}

	public static boolean canBrew(){
		if (!GameModes.on(FLASKS)) return true;
		return Dungeon.hero != null && Dungeon.hero.belongings.getItem(EmptyPotionFlask.class) != null;
	}

	public static void useFlask(){
		if (!GameModes.on(FLASKS) || Dungeon.hero == null) return;
		Item flask = Dungeon.hero.belongings.getItem(EmptyPotionFlask.class);
		if (flask != null) flask.detach(Dungeon.hero.belongings.backpack);
	}

	//the mod's Food.satisfy gave each class a little something with every meal
	public static void onMeal( Hero hero ){
		if (!GameModes.on(MEALS)) return;
		HeroClass c = hero.heroClass;
		if (c == HeroClass.WARRIOR){
			if (hero.HP < hero.HT) hero.HP = Math.min(hero.HP + hero.HT / 5, hero.HT);
		} else if (c == HeroClass.MAGE){
			Buff.affect(hero, Recharging.class, Recharging.DURATION - 25f);
		} else if (c == HeroClass.ROGUE){
			Buff.affect(hero, Invisibility.class, Invisibility.DURATION - 17f);
		} else if (c == HeroClass.HUNTRESS){
			Buff.affect(hero, Haste.class, Haste.DURATION - 16f);
		} else if (c == HeroClass.ALCHEMIST){
			Buff.affect(hero, BlobImmunity.class, BlobImmunity.DURATION - 13f);
		}
	}
}
