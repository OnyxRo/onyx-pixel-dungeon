/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * Shittened Pixel Dungeon (Haukii), item skins
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Skin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.SkinEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.SkinSpeck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.WoollyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Berry;
import com.shatteredpixel.shatteredpixeldungeon.items.food.CoffeeBean;
import com.shatteredpixel.shatteredpixeldungeon.items.food.GoldenMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Milk;
import com.shatteredpixel.shatteredpixeldungeon.items.misc.Painting;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.UnstableBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Glaive;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Katana;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Longsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RunicBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Scimitar;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WarHammer;
import com.shatteredpixel.shatteredpixeldungeon.journal.SkinCatalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SkinSprites;
import com.shatteredpixel.shatteredpixeldungeon.ui.InventoryPane;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.Holiday;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

//Shittened PD's item skins, the part that acts. The skin list itself is Skin.java.
//
//In the mod each of the 15 items carried its own PAINT action, picker, name and sparkle code. Here they all
//live in this one class and Item calls into it, so none of those items needed changing. A painted item keeps
//its skin in Item.skin. Never painted items have no skin saved at all, so old saves load as they were.
public class SkinPaint {

	public static final String AC_PAINT = "PAINT";
	public static final String AC_EAT   = "EAT";

	private static final String SKIN = "skin";

	//the mod's equip sounds, played when a skin goes on
	private static final String SND_SWORD = "sounds/shittened/equip_sword.mp3";
	private static final String SND_AXE   = "sounds/shittened/equip_axe.mp3";
	private static final String SND_CLOTH = "sounds/shittened/equip_cloth.mp3";
	private static final String SND_WOOD  = "sounds/shittened/equip_wood.mp3";

	public static boolean paintable(Item item) {
		return item != null && Skin.hasSkins(item.getClass());
	}

	//skins that only belong to the item they were made for, so arrows and other copies never pick them up
	private static Skin own(Item item) {
		if (item == null || item.skin == null || item.skin.skinFor() != item.getClass()) return null;
		return item.skin;
	}

	// ---------- hooks called from Item ----------

	public static void addActions(Item item, ArrayList<String> actions) {
		if (!paintable(item)) return;
		//weapons and the spellbook have to be identified first, as in the mod
		boolean needsId = (item instanceof MeleeWeapon && !(item instanceof MagesStaff)) || item instanceof UnstableSpellbook;
		if (!needsId || item.isIdentified()) actions.add(AC_PAINT);
		if (own(item) == Skin.CAKEHAMMER) actions.add(AC_EAT);
	}

	public static void execute(Item item, Hero hero, String action) {
		if (!paintable(item)) return;
		if (action.equals(AC_PAINT)) {
			Item.curItem = item;
			GameScene.selectItem(selector);
		} else if (action.equals(AC_EAT) && own(item) == Skin.CAKEHAMMER) {
			eatCake(item, hero);
		}
	}

	public static String legendName(Item item) {
		Skin s = own(item);
		return s == null ? null : s.legendName();
	}

	//the line added to the item window
	public static String info(Item item) {
		Skin s = own(item);
		if (s == null || s.rarityTier() == 0) return "";
		//the mod's desc_skin lines start with their own blank line
		String text = Messages.get(item, "desc_skin", s.rarityName() + " _" + s.skinName() + "_");
		if (!s.desc().isEmpty()) text += "\n\n\"" + s.desc() + "\"";
		return text;
	}

	public static void store(Item item, Bundle bundle) {
		if (item.skin != null) bundle.put(SKIN, item.skin.name());
	}

	public static void restore(Item item, Bundle bundle) {
		if (!bundle.contains(SKIN)) return;
		try {
			item.skin = Skin.valueOf(bundle.getString(SKIN));
			if (item.skin.skinFor() == item.getClass()) item.image = item.skin.sprite();
		} catch (IllegalArgumentException e) {
			item.skin = null; //a skin this version does not have, keep the usual look
		}
	}

	//puts a skin on without counting it, for starting kits and the journal
	public static void change(Item item, Skin skin) {
		item.skin = skin;
		item.image = skin.sprite();
	}

	//puts a skin on and records it in the journal
	public static void apply(Item item, Skin skin) {
		change(item, skin);
		item.updateQuickslot();
		if (Dungeon.hero != null && item instanceof Armor && item == Dungeon.hero.belongings.armor()
				&& Dungeon.hero.sprite instanceof HeroSprite) {
			((HeroSprite) Dungeon.hero.sprite).updateArmor();
		}
		SkinCatalog.setSeen(skin);
		SkinCatalog.countApply(skin);
	}

	// ---------- the picker ----------

	private static final WndBag.ItemSelector selector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get(SkinPaint.class, "prompt");
		}

		@Override
		public Class<? extends Bag> preferredBag() {
			if (InventoryPane.lastBag != null) return InventoryPane.lastBag.getClass();
			return Belongings.Backpack.class;
		}

		@Override
		public boolean itemSelectable(Item item) {
			return Item.curItem != null && item != null && item.isIdentified()
					&& Skin.containsIngredient(item, Item.curItem.getClass());
		}

		@Override
		public void onSelect(Item ingredient) {
			Item target = Item.curItem;
			if (ingredient == null || target == null || !itemSelectable(ingredient)) return;
			Skin skin = pick(target, ingredient);
			if (skin == null) return;
			GLog.p(Messages.get(target, "applyskin"));
			if (Dungeon.hero.sprite != null) Dungeon.hero.sprite.operate(Dungeon.hero.pos);
			play(soundFor(target), 1f);
			apply(target, skin);
		}
	};

	//works out the skin an ingredient gives, with the mod's special steps. Null when it does not apply.
	public static Skin pick(Item target, Item ingredient) {
		Class<?> c = target.getClass();
		Skin skin = Skin.fromIngredient(ingredient, c);
		Skin now = Skin.current(target);
		if (skin == null) return null;

		if (c == BrokenSeal.class) {
			if (ingredient instanceof Waterskin) {
				if (((Waterskin) ingredient).volume() > 5) {
					setVolume((Waterskin) ingredient, ((Waterskin) ingredient).volume() - 5);
				} else {
					GLog.w(Messages.get(BrokenSeal.class, "nowater"));
					return null;
				}
			} else if (ingredient instanceof Torch) {
				if (now == Skin.WOODSEAL) {
					skin = Skin.FIERYSEAL;
					play(Assets.Sounds.BURNING, 1f);
				} else if (now == Skin.BOMBSEAL) {
					skin = Skin.BLOWNUPSEAL;
					play(Assets.Sounds.BLAST, 3f);
				} else {
					GLog.w(Messages.get(BrokenSeal.class, "notburning"));
					return null;
				}
			} else if (ingredient instanceof GoldenMeat && now != Skin.GOLDSEAL) {
				GLog.w(Messages.get(BrokenSeal.class, "notgolden"));
				return null;
			} else if (ingredient instanceof WoollyBomb) {
				play(Assets.Sounds.SHEEP, 1f);
			}

		} else if (c == CloakOfShadows.class) {
			if (ingredient instanceof Torch) {
				if (now == Skin.WOODENCLOAK) {
					skin = Skin.FIERYCLOAK;
					play(Assets.Sounds.BURNING, 1f);
				} else {
					GLog.w(Messages.get(BrokenSeal.class, "notburning"));
					return null;
				}
			} else if (ingredient instanceof GoldenMeat && now != Skin.GOLDENCLOAK) {
				GLog.w(Messages.get(BrokenSeal.class, "notgolden"));
				return null;
			}

		} else if (c == SpiritBow.class) {
			if (ingredient instanceof Waterskin) {
				skin = now == Skin.FLAMINGBOW ? Skin.ASHBOW : Skin.WATERBOW;
			}

		} else if (c == RunicBlade.class) {
			if (ingredient instanceof Waterskin && !((Waterskin) ingredient).isFull()) {
				GLog.w(Messages.get(RunicBlade.class, "nowater"));
				return null;
			} else if (ingredient instanceof SandalsOfNature && ((Artifact) ingredient).visiblyUpgraded() < 10) {
				GLog.w(Messages.get(RunicBlade.class, "sandals", ingredient.name()));
				return null;
			}

		} else if (c == Longsword.class) {
			if (ingredient instanceof Painting) {
				if (((Painting) ingredient).id == 2) {
					skin = Skin.STARRYLSWORD;
				} else if (((Painting) ingredient).id == 3) {
					skin = Skin.WAVELSWORD;
				} else {
					GLog.w(Messages.get(Longsword.class, "notpretty"));
					return null;
				}
			}

		} else if (c == Glaive.class) {
			//the mod checked the new skin here instead of the current one, so these never worked
			if (ingredient instanceof UnstableBrew && now == Skin.RAINBOWGLAIVE) {
				skin = Skin.FULLRAINBOWGLAIVE;
			} else if (ingredient instanceof UnstableBrew) {
				skin = Skin.RAINBOWGLAIVE;
			} else if (ingredient instanceof Torch) {
				if (now == Skin.NUTGLAIVE || now == Skin.NATUREGLAIVE) {
					skin = Skin.FIERYGLAIVE;
					play(Assets.Sounds.BURNING, 1f);
				} else if (now == Skin.BOMBGLAIVE) {
					skin = Skin.BLOWNUPGLAIVE;
					play(Assets.Sounds.BLAST, 1f);
				} else {
					GLog.w(Messages.get(BrokenSeal.class, "notburning"));
					return null;
				}
			}

		} else if (c == WarHammer.class) {
			//the cake is built in order. Cake base, coffee bean, milk, berry.
			if (ingredient instanceof CoffeeBean) {
				if (now != Skin.CAKEBASEHAMMER) { GLog.w(Messages.get(WarHammer.class, "cake")); return null; }
			} else if (ingredient instanceof Milk) {
				if (now != Skin.CHOCOHAMMER) { GLog.w(Messages.get(WarHammer.class, "cake")); return null; }
			} else if (ingredient instanceof Berry) {
				if (now == Skin.CREAMHAMMER) {
					skin = Skin.CAKEHAMMER;
					ingredient.detach(Dungeon.hero.belongings.backpack);
					play(Assets.Sounds.BURNING, 1f);
				} else {
					skin = Skin.BERRYHAMMER;
				}
			} else if (ingredient instanceof GooBlob) {
				if (now == Skin.GOLDHAMMER) {
					play(Assets.Sounds.BURNING, 1f);
				} else {
					GLog.w(Messages.get(SkinPaint.class, "goo_gold"));
					return null;
				}
			}
		}
		return skin;
	}

	private static void setVolume(Waterskin skin, int volume) {
		Bundle b = new Bundle();
		skin.storeInBundle(b);
		b.put("volume", volume);
		skin.restoreFromBundle(b);
		skin.updateQuickslot();
	}

	private static String soundFor(Item target) {
		Class<?> c = target.getClass();
		if (c == SpiritBow.class || c == MagesStaff.class || c == PlateArmor.class) return SND_WOOD;
		if (c == RunicBlade.class || c == Longsword.class || c == Katana.class) return SND_SWORD;
		if (c == BrokenSeal.class || c == CloakOfShadows.class || c == LeatherArmor.class
				|| c == ClothArmor.class || c == UnstableSpellbook.class) return SND_CLOTH;
		return SND_AXE;
	}

	private static void play(String sound, float volume) {
		Sample.INSTANCE.load(sound);
		Sample.INSTANCE.play(sound, volume);
	}

	private static void eatCake(Item hammer, Hero hero) {
		apply(hammer, Skin.BITTENHAMMER);
		//the mod's copy of Food.execute
		Buff.affect(hero, Hunger.class).satisfy(Dungeon.isChallenged(Challenges.NO_FOOD) ? 100f : 300f);
		hero.sprite.operate(hero.pos);
		hero.busy();
		SpellSprite.show(hero, SpellSprite.FOOD);
		Sample.INSTANCE.play(Assets.Sounds.EAT);
		GLog.i(Messages.get(WarHammer.class, "eat_msg"));
		hero.spendAndNext(1f);
	}

	// ---------- events from the mod ----------

	public static void holidaySkin(Item item) {
		if (item instanceof CloakOfShadows && Holiday.getCurrentHoliday() == Holiday.HALLOWEEN) {
			change(item, Skin.DRACULACLOAK);
		} else if (item instanceof SpiritBow && Holiday.getCurrentHoliday() == Holiday.EASTER) {
			change(item, Skin.EGGBOW);
		}
	}

	//the warrior's sheep seal bleats now and then
	public static void onHeroAct(Hero hero) {
		if (hero.heroClass == HeroClass.WARRIOR && Random.Int(50) == 0
				&& hero.belongings.armor() != null
				&& hero.belongings.armor().checkSeal() != null
				&& own(hero.belongings.armor().checkSeal()) == Skin.SHEEPSEAL) {
			Sample.INSTANCE.play(Assets.Sounds.SHEEP, 0.6f, Random.Float(0.7f, 1.3f));
		}
	}

	//the hero may step on a lego, and a plain staff, runic blade, longsword, scimitar or katana in hand gets bent
	public static void onHeroMove(Hero hero) {
		if (Random.Int(3500) != 6 || Dungeon.depth <= 0 || !hero.isAlive()) return;
		GameScene.legoBanner();
		hero.damage(hero.HP / 6 + Dungeon.depth, hero);
		Item w = hero.belongings.weapon();
		if (w == null || own(w) != null && own(w).rarityTier() != 0) return;
		if (w.getClass() == MagesStaff.class) {
			apply(w, Skin.BENTWAND);
			GLog.w(Messages.get(SkinPaint.class, "lego_staff"));
		} else if (w.getClass() == RunicBlade.class) {
			apply(w, Skin.BROKENRUNIC);
			GLog.w(Messages.get(SkinPaint.class, "lego_runic"));
		} else if (w.getClass() == Longsword.class) {
			apply(w, Skin.BENTLSWORD);
			GLog.w(Messages.get(SkinPaint.class, "lego_sword"));
		} else if (w.getClass() == Scimitar.class) {
			apply(w, Skin.BENTSCIMITAR);
			GLog.w(Messages.get(SkinPaint.class, "lego_scimitar"));
		} else if (w.getClass() == Katana.class) {
			apply(w, Skin.MESSEDKATANA);
			GLog.w(Messages.get(SkinPaint.class, "lego_katana"));
		}
	}

	// ---------- the spirit bow ----------

	public static void knockArrow(SpiritBow bow, Item arrow) {
		Skin s = own(bow);
		if (s != null) arrow.image = s.arrowImage();
	}

	//the splash a spirit arrow leaves, coloured by the bow's skin
	public static void arrowSplash(SpiritBow bow, int cell, boolean missed) {
		Skin s = own(bow);
		int color = 0xCC99FFFF;
		int amount = 1;
		if (s != null) {
			switch (s) {
				case ORANGEBOW:    color = 0xCCffc34d; break;
				case YELLOWBOW:    color = 0xCCdfff40; break;
				case GREENBOW:     color = 0xCC99ff34; break;
				case BLUEBOW:      color = 0xCCb2f2ff; break;
				case TURQUOISEBOW: color = 0xCC5a86b3; break;
				case PURPLEBOW:    color = 0xCCee99ff; break;
				case PINKBOW:      color = 0xCCffa6e1; break;
				case BROWNBOW:     color = 0xCC998f5c; break;
				case BLACKBOW:     color = 0xCC595959; break;
				case GRAYBOW:      color = 0xCCb5bfbf; break;
				case WHITEBOW:     color = 0xCCffffff; break;
				case WOODENBOW:    color = 0xCC866647; break;
				case WALNUTBOW:    color = 0xCC73512f; break;
				case GOOBOW:       color = 0xCC262626; break;
				case CATALYSTBOW:  color = 0xCCc48400; break;
				case MEATBOW:      color = 0xCCff9d33; break;
				case RAINBOWBOW:   color = Random.oneOf(0xCCd6680a, 0xCCffe859, 0xCC00ff3a, 0xCC01e9ff, 0xCC7500ff, 0xCCff01b0); break;
				case LOVEBOW:      color = 0xCCd95374; amount = 2; break;
				case EGGBOW:       color = Random.oneOf(0xCCfbe9a3, 0xCCad5d20); break;
				case WATERBOW:     color = 0xCC79bcc9; break;
				case GOLDENBOW:
					if (Random.Float() > 0.2f) { color = 0xCCfdf55f; amount = 3; } else { color = 0xCCfffde0; }
					break;
				case NATUREBOW:    color = Random.oneOf(0xCC8e8f4f, 0xCC6b6b3c, 0xCC829040); amount = 3; break;
				case METALBOW:     color = 0xCCaaaaa3; break;
				case ROYALBOW:
					if (Random.Float() > 0.2f) { color = 0xCCffe940; amount = 2; } else { color = 0xCCdd0024; }
					break;
				case CROSSBOWBOW:  color = 0xCC9e9e9e; break;
				case EARTHBOW:     color = Random.Float() > 0.2f ? 0xCC805500 : 0xCCfff568; break;
				case TENGUBOW:     color = 0xCCb46b30; break;
				case WILLOWBOW:    color = 0xCC538e49; break;
				case RINGBOW:      color = 0xCCfa9c13; break;
				case FLAMINGBOW:
					play(Assets.Sounds.BURNING, 1f);
					amount = 3;
					color = Random.oneOf(0xCCff5500, 0xCCffe566, 0xCCffffff, 0xCCff9d33);
					break;
				//the mod gave this to the plain bow by mistake, the ash bow is the one meant
				case ASHBOW:
					amount = 6;
					color = Random.oneOf(0xCC050505, 0xCC262626, 0xCC101010);
					break;
				default: break;
			}
		}
		if (missed && (s == Skin.FLAMINGBOW || s == Skin.ASHBOW)) {
			for (int i = 0; i < 15; i++) {
				Splash.at(cell, s == Skin.FLAMINGBOW
						? Random.oneOf(0xCCff5500, 0xCCffe566, 0xCCffffff, 0xCCff9d33)
						: Random.oneOf(0xCC050505, 0xCC262626, 0xCC101010), 1);
			}
		}
		Splash.at(cell, color, amount);
	}

	// ---------- sparkles ----------

	//the item's own sparkle, for the seal and the glaive
	public static Emitter emitter(Item item) {
		Skin s = own(item);
		if (s == null) return null;
		SkinEmitter e = new SkinEmitter();
		e.pos(SkinSprites.width(item.image) / 2f, SkinSprites.height(item.image) / 2f - 3f);
		e.fillTarget = false;
		switch (s) {
			case DMSEAL:
				e.pour(SkinSpeck.factory(SkinSpeck.DMSEAL), 0.2f);
				return e;
			case KINGSEAL:
				e.pour(SkinSpeck.factory(SkinSpeck.KINGSEAL), 1f);
				e.randomSize = true; e.randomHeight = 3f; e.randomWidth = 3f; e.centerOffsetY = -5f;
				return e;
			case FIERYSEAL:
				e.pour(SkinSpeck.factory(SkinSpeck.FIERYSEAL), 0.15f);
				return e;
			case PUREGOLDSEAL:
				e.pos(SkinSprites.width(item.image) / 2f + Random.Float(4f), SkinSprites.height(item.image) / 3f + Random.Float(-2f, 2f));
				e.pour(SkinSpeck.factory(SkinSpeck.PUREGOLDSEAL), 0.3f);
				return e;
			case AMULETSEAL:
				e.randomSize = true; e.randomHeight = 3f; e.randomWidth = 3f; e.centerOffsetY = -4f;
				e.pour(SkinSpeck.factory(SkinSpeck.AMULETSEAL), 0.6f);
				return e;
			case BLOWNUPSEAL:
				e.pos(SkinSprites.width(item.image) / 2f, SkinSprites.height(item.image) / 2f + 4f);
				e.randomSize = true; e.randomHeight = 3f; e.randomWidth = 3f;
				e.pour(SkinSpeck.factory(SkinSpeck.BLOWNUPSEAL), 0.3f);
				return e;
			case TRUEGOLDGLAIVE:
				e.randomSize = true; e.randomHeight = 6f; e.randomWidth = 6f;
				e.pour(SkinSpeck.factory(SkinSpeck.PUREGOLDSEAL), 0.3f);
				return e;
			case BEEGLAIVE:
				e.randomSize = true; e.randomHeight = 6f; e.randomWidth = 6f;
				e.pour(SkinSpeck.factory(SkinSpeck.BEES), 0.4f);
				return e;
			case SNAKEGLAIVE:
				e.randomSize = true; e.randomHeight = 6f; e.randomWidth = 6f;
				e.pour(SkinSpeck.factory(SkinSpeck.SNAKESEAL), 0.5f);
				return e;
			case SHROUDEDGLAIVE:
				e.randomSize = true; e.randomHeight = 5f; e.randomWidth = 5f; e.centerOffsetY = -2f;
				e.pour(SkinSpeck.factory(SkinSpeck.SMOKE), 0.3f);
				return e;
			case BLOWNUPGLAIVE:
				e.pos(SkinSprites.width(item.image) / 2f, SkinSprites.height(item.image) / 2f + 4f);
				e.randomSize = true; e.randomHeight = 2f; e.randomWidth = 2f; e.centerOffsetY = 1f;
				e.pour(SkinSpeck.factory(SkinSpeck.BLOWNUPSEAL), 0.3f);
				return e;
			case FIERYGLAIVE:
				e.randomSize = true; e.randomHeight = 5f; e.randomWidth = 5f; e.centerOffsetY = -2f;
				e.pour(SkinSpeck.factory(SkinSpeck.FIERYSEAL), 0.05f);
				return e;
			default:
				return null;
		}
	}

	//the glow over armor wearing a painted seal. Null for the plain red seal, which Armor draws itself.
	public static Emitter sealEmitter(Item armor, BrokenSeal seal) {
		Skin s = own(seal);
		if (s == null || s.rarityTier() == 0) return null;
		SkinEmitter e = new SkinEmitter();
		e.pos(SkinSprites.width(armor.image) / 2f + 2f, SkinSprites.height(armor.image) / 3f);
		e.fillTarget = false;
		float rate = 0.6f;
		int type;
		switch (s) {
			case YELLOWSEAL: type = SkinSpeck.YELLOWSEAL; break;
			case GREENSEAL: type = SkinSpeck.GREENSEAL; break;
			case BLUESEAL: type = SkinSpeck.BLUESEAL; break;
			case PURPLESEAL: type = SkinSpeck.PURPLESEAL; break;
			case BLACKSEAL: type = SkinSpeck.BLACKSEAL; break;
			case WHITESEAL: type = SkinSpeck.WHITESEAL; break;
			case GRAYSEAL: type = SkinSpeck.GRAYSEAL; break;
			case TURQUOISESEAL: type = SkinSpeck.TURQUOISESEAL; break;
			case BROWNSEAL: type = SkinSpeck.BROWNSEAL; break;
			case PINKSEAL: type = SkinSpeck.PINKSEAL; break;
			case ORANGESEAL: type = SkinSpeck.ORANGESEAL; break;
			case WOODSEAL: type = SkinSpeck.WOODSEAL; break;
			case MEATSEAL: type = SkinSpeck.MEATSEAL; break;
			case TRANSPARENTSEAL: type = SkinSpeck.TRANSPARENTSEAL; break;
			case HEALINGSEAL: type = SkinSpeck.HEALINGSEAL; break;
			case CUTSEAL: type = SkinSpeck.CUTSEAL; break;
			case THIEFSEAL: type = SkinSpeck.THIEFSEAL; break;
			case COPPERSEAL: type = SkinSpeck.COPPERSEAL; break;
			case CUBESEAL: type = SkinSpeck.CUBESEAL; break;
			case RINGSEAL: type = SkinSpeck.RINGSEAL; break;
			case RAINBOWSEAL: type = SkinSpeck.RAINBOWSEAL; break;
			case GOLDSEAL: type = SkinSpeck.GOLDSEAL; rate = 0.3f; break;
			case CORPSESEAL: type = SkinSpeck.CORPSESEAL; rate = Random.Float(0.8f, 4f); break;
			case HEARTSEAL: type = SkinSpeck.HEARTSEAL; break;
			case BURNTSEAL: type = SkinSpeck.BURNTSEAL; break;
			case GOOSEAL: type = SkinSpeck.GOOSEAL; rate = 2f; break;
			case WATERSEAL: type = SkinSpeck.WATERSEAL; rate = 0.1f; break;
			case WARRIORSEAL: type = SkinSpeck.WARRIORSEAL; break;
			case COWBOYSEAL: type = SkinSpeck.COWBOYSEAL; break;
			case HONEYSEAL:
				e.randomSize = true; e.randomWidth = 1f; e.randomHeight = 1f;
				type = SkinSpeck.HONEYSEAL; rate = 0.4f; break;
			case ICESEAL: type = SkinSpeck.ICESEAL; rate = 0.9f; break;
			case EYESEAL: type = SkinSpeck.EYESEAL; rate = 1.3f; break;
			case SNAKESEAL: type = SkinSpeck.SNAKESEAL; rate = 0.4f; break;
			case CANDLESEAL: type = SkinSpeck.CANDLESEAL; rate = 0.1f; break;
			case BOMBSEAL: type = SkinSpeck.BOMBSEAL; rate = 0.3f; break;
			case SHEEPSEAL: type = SkinSpeck.SHEEPSEAL; break;
			case DMSEAL: type = SkinSpeck.DMSEAL; rate = 0.02f; break;
			case KINGSEAL: type = SkinSpeck.KINGSEAL; rate = 0.15f; break;
			case FIERYSEAL: type = SkinSpeck.FIERYSEAL; rate = 0.01f; break;
			case PUREGOLDSEAL:
				e.fillTarget = true;
				e.pos(SkinSprites.width(armor.image) / 2f + Random.Float(4f), SkinSprites.height(armor.image) / 3f + Random.Float(-2f, 2f));
				type = SkinSpeck.PUREGOLDSEAL; rate = 0.1f; break;
			case AMULETSEAL:
				e.randomSize = true; e.randomHeight = 2f; e.randomWidth = 2f;
				type = SkinSpeck.AMULETSEAL; rate = 0.2f; break;
			case BLOWNUPSEAL: type = SkinSpeck.BLOWNUPSEAL; rate = 0.04f; break;
			default: return null;
		}
		e.pour(SkinSpeck.factory(type), rate);
		return e;
	}
}
