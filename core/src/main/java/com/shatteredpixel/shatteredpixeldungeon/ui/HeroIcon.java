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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.ClericSpell;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

//icons for hero subclasses and abilities atm, maybe add classes?
public class HeroIcon extends Image {

	private static TextureFilm film;
	private static final int SIZE = 16;

	//transparent icon
	public static final int NONE    = 127;

	//subclasses
	public static final int BERSERKER   = 0;
	public static final int GLADIATOR   = 1;
	public static final int BATTLEMAGE  = 2;
	public static final int WARLOCK     = 3;
	public static final int ASSASSIN    = 4;
	public static final int FREERUNNER  = 5;
	public static final int SNIPER      = 6;
	public static final int WARDEN      = 7;
	public static final int CHAMPION    = 8;
	public static final int MONK        = 9;
	public static final int PRIEST      = 10;
	public static final int PALADIN     = 11;
	public static final int CONJURER = 12;
	public static final int DEVOTEE = 13;
	public static final int CONSTRUCTOR = 14;
	public static final int ARMORER = 15;

	//abilities
	public static final int HEROIC_LEAP     = 16;
	public static final int SHOCKWAVE       = 17;
	public static final int ENDURE          = 18;
	public static final int ELEMENTAL_BLAST = 19;
	public static final int WILD_MAGIC      = 20;
	public static final int WARP_BEACON     = 21;
	public static final int SMOKE_BOMB      = 22;
	public static final int DEATH_MARK      = 23;
	public static final int SHADOW_CLONE    = 24;
	public static final int SPECTRAL_BLADES = 25;
	public static final int NATURES_POWER   = 26;
	public static final int SPIRIT_HAWK     = 27;
	public static final int CHALLENGE       = 28;
	public static final int ELEMENTAL_STRIKE= 29;
	public static final int FEINT           = 30;
	public static final int ASCENDED_FORM   = 31;
	public static final int TRINITY         = 32;
	public static final int POWER_OF_MANY   = 33;
	public static final int RATMOGRIFY      = 34;
	public static final int WEAL_AND_WOE = 35;
	public static final int CALLING_BEYOND = 36;
	public static final int QUICKDRAW = 38;
	public static final int TRUESIGHT = 39;
	public static final int REFLECTION = 67;
	public static final int ETERNAL_BALANCE = 37;

	//cleric spells
	public static final int GUIDING_LIGHT   = 40;
	public static final int HOLY_WEAPON     = 41;
	public static final int HOLY_WARD       = 42;
	public static final int HOLY_INTUITION  = 43;
	public static final int SHIELD_OF_LIGHT = 44;
	public static final int RECALL_GLYPH    = 45;
	public static final int SUNRAY          = 46;
	public static final int DIVINE_SENSE    = 47;
	public static final int BLESS           = 48;
	public static final int CLEANSE         = 49;
	public static final int RADIANCE        = 50;
	public static final int HOLY_LANCE      = 51;
	public static final int HALLOWED_GROUND = 52;
	public static final int MNEMONIC_PRAYER = 53;
	public static final int SMITE           = 54;
	public static final int LAY_ON_HANDS    = 55;
	public static final int AURA_OF_PROTECTION = 56;
	public static final int WALL_OF_LIGHT   = 57;
	public static final int DIVINE_INTERVENTION = 58;
	public static final int JUDGEMENT       = 59;
	public static final int FLASH           = 60;
	public static final int BODY_FORM       = 61;
	public static final int MIND_FORM       = 62;
	public static final int SPIRIT_FORM     = 63;
	public static final int BEAMING_RAY     = 64;
	public static final int LIFE_LINK       = 65;
	public static final int STASIS          = 66;

	//all cleric spells have a separate icon with no background for the action indicator
	public static final int SPELL_ACTION_OFFSET      = 32;

	//action indicator visuals
	public static final int BERSERK         = 104;
	public static final int COMBO           = 105;
	public static final int PREPARATION     = 106;
	public static final int MOMENTUM        = 107;
	public static final int SNIPERS_MARK    = 108;
	public static final int WEAPON_SWAP     = 109;
	public static final int MONK_ABILITIES  = 110;
	public static final int CONJURE_MINIONS = 111;
	public static final int ARMORER_LIGHT = 112;

	//Transformation PD hero icons. scripts/port-icons.py copied the mod's cells 0 to 207 into ours at the mod's cell number plus 128.
	public static final int MODCELL = 128;
	public static final int FREEMAN = 12 + MODCELL;
	public static final int WATERSLIME = 13 + MODCELL;
	public static final int DARKSLIME = 14 + MODCELL;
	public static final int SPRING_SPELL = 35 + MODCELL;
	public static final int RAPID_WATERFALL = 36 + MODCELL;
	public static final int MAD_SLIME = 37 + MODCELL;
	public static final int TATTEKI = 67 + MODCELL;
	public static final int NINJAMASTER = 68 + MODCELL;
	public static final int DECOY = 69 + MODCELL;
	public static final int SPIDERJAR = 70 + MODCELL;
	public static final int ONESWORD = 71 + MODCELL;
	public static final int DARK_HOOK = 111 + MODCELL;
	public static final int NINJA_NORMAL = 112 + MODCELL;
	public static final int NINJA_WATER = 113 + MODCELL;
	public static final int NINJA_GRASS = 114 + MODCELL;
	public static final int SMOKEMASK = 115 + MODCELL;
	public static final int DRONEDIRECT = 116 + MODCELL;
	public static final int BALANCE = 117 + MODCELL;
	public static final int INVASION = 118 + MODCELL;
	public static final int PARRY = 119 + MODCELL;
	public static final int REASON = 120 + MODCELL;
	public static final int RITUAL = 121 + MODCELL;
	public static final int RAID_SESSION = 122 + MODCELL;
	public static final int NATURAL_CHILD = 123 + MODCELL;
	public static final int AT400 = 192 + MODCELL;
	public static final int AU400 = 193 + MODCELL;
	public static final int ROUTINE = 194 + MODCELL;
	public static final int DM400GAS = 195 + MODCELL;
	public static final int HONEYCOMB = 196 + MODCELL;
	public static final int SALESCONTRACT = 197 + MODCELL;
	//transformation, the spells of Transformation PD's spell talents
	public static final int ASCENSION_CURSE = 128 + MODCELL;
	public static final int DIVINE_PROTECTION = 130 + MODCELL;
	public static final int SHEPHERD_INTENTION = 132 + MODCELL;
	public static final int CONVERSION_HOLY = 134 + MODCELL;
	public static final int PURIFYING_EVIL = 136 + MODCELL;
	public static final int DIVINE_STORM = 138 + MODCELL;
	public static final int GENESIS = 140 + MODCELL;
	public static final int INDULGENCE = 142 + MODCELL;
	public static final int SILVER_LANGUAGE = 144 + MODCELL;
	public static final int RESURRECTION = 146 + MODCELL;
	public static final int TRAITOROUS_SPELL = 148 + MODCELL;
	public static final int SPRINT_SPELL = 150 + MODCELL;
	public static final int FOCUS_LIGHT = 152 + MODCELL;
	public static final int ZHUANYU_SPELL = 154 + MODCELL;
	public static final int THORN_WHIP = 156 + MODCELL;
	public static final int REVELATION = 158 + MODCELL;
	public static final int HOLY_GRENADE = 160 + MODCELL;
	public static final int BLADE_STAR = 162 + MODCELL;
	public static final int SACRED_BLADE = 164 + MODCELL;
	public static final int EQUIPMENT_BLESS = 166 + MODCELL;
	public static final int HOTLIGHT = 168 + MODCELL;
	public static final int CREDULOUS = 170 + MODCELL;
	public static final int RUNEMAGE = 198 + MODCELL;
	public static final int COMBATMASTER = 199 + MODCELL;
	public static final int MARCHFORWARD = 200 + MODCELL;
	public static final int TABOOMAGIC = 201 + MODCELL;
	public static final int KINGBLADE = 202 + MODCELL;
	public static final int ALCHEMIST = 203 + MODCELL;
	public static final int PIOUS = 204 + MODCELL;
	public static final int REASONDOSE = 205 + MODCELL;
	public static final int CONPOTION = 206 + MODCELL;
	public static final int HOLY_PRAYER = 207 + MODCELL;

	//rearranged, Re-ARranged PD's icons, drawn from its own sheet (interfaces/rearranged/hero_icons.png)
	public static final int RA = 10000;
	public static final int OUTLAW          = RA + 24;
	public static final int GUNSLINGER      = RA + 25;
	public static final int SPECIALIST      = RA + 26;
	public static final int SLASHER         = RA + 28;
	public static final int MASTER          = RA + 29;
	public static final int SLAYER          = RA + 30;
	public static final int ENGINEER        = RA + 32;
	public static final int EXPLORER        = RA + 33;
	public static final int RESEARCHER      = RA + 34;
	public static final int DEATHKNIGHT     = RA + 36;
	public static final int HORSEMAN        = RA + 37;
	public static final int CRUSADER        = RA + 38;
	public static final int SAVIOR          = RA + 40;
	public static final int THERAPIST       = RA + 41;
	public static final int MEDICALOFFICER  = RA + 42;
	public static final int BOWMASTER       = RA + 44;
	public static final int JUGGLER         = RA + 45;
	public static final int SHARPSHOOTER    = RA + 46;
	//rearranged, the third subclasses of Shattered's heroes, their buffs and the Cleric's new spells
	public static final int VETERAN         = RA + 2;
	public static final int WIZARD          = RA + 6;
	public static final int CHASER          = RA + 10;
	public static final int FIGHTER         = RA + 14;
	public static final int FENCER          = RA + 18;
	public static final int ENCHANTER       = RA + 22;
	public static final int TACKLE          = RA + 106;
	public static final int MAGIC_COMBO     = RA + 108;
	public static final int DIVINE_BLAST    = RA + 179;
	public static final int DIVINE_RAY      = RA + 180;
	public static final int HOLY_BOMB       = RA + 181;
	public static final int RA_RESURRECTION = RA + 182;
	public static final int HOLY_MANTLE     = RA + 183;
	public static final int POWER_OF_LIFE   = RA + 184;
	public static final int INDUCE_AGGRO    = RA + 185;
	public static final int SPELL_BURST     = RA + 186;
	public static final int TIME_AMP        = RA + 187;
	public static final int WEAKENING_HEX   = RA + 188;
	public static final int STUN            = RA + 189;
	public static final int THUNDER_IMBUE   = RA + 190;
	public static final int ARCANE_ARMOR    = RA + 191;
	public static final int ENCHANT         = RA + 192;
	public static final int RIOT            = RA + 72;
	public static final int REINFORCEDARMOR = RA + 73;
	public static final int FIRSTAIDKIT     = RA + 74;
	public static final int AWAKE           = RA + 76;
	public static final int SHADOW_BLADE    = RA + 77;
	public static final int KUNAI           = RA + 78;
	public static final int SPROUT          = RA + 80;
	public static final int TREASUREMAP     = RA + 81;
	public static final int ROOT            = RA + 82;
	public static final int HOLY_SHIELD     = RA + 84;
	public static final int STIMPACK        = RA + 85;
	public static final int UNSTABLE_ANKH   = RA + 86;
	public static final int HEAL_GENERATOR  = RA + 88;
	public static final int ANGEL_WING      = RA + 89;
	public static final int GAMMA_EMMIT     = RA + 90;
	public static final int DASH            = RA + 92;
	public static final int HUNT            = RA + 93;
	public static final int SNIPE           = RA + 94;
	public static final int SWORD_AURA      = RA + 128;
	public static final int AWAKEN_OFF      = RA + 129;
	public static final int AWAKEN_ON       = RA + 130;
	public static final int BUILD           = RA + 132;
	public static final int RIDE            = RA + 137;
	public static final int PRAY            = RA + 138;
	public static final int FIRST_AID       = RA + 140;
	public static final int COMMAND         = RA + 142;
	public static final int POWER_SHOT      = RA + 144;
	public static final int JUGGLING        = RA + 145;
	public static final int SHARPSHOOTING   = RA + 146;

	private static TextureFilm raFilm;

	//deranged, DERanged PD's icons, drawn from its own sheet (interfaces/deranged/hero_icons.png)
	public static final int DR = 20000;
	public static final int DR_KING         = DR + 97;
	public static final int DR_WRATH        = DR + 98;
	public static final int DR_BLOCKING     = DR + 99;
	private static TextureFilm drFilm;

	private void pick( int icon ){
		//rearranged, the mod's sheet keeps spell action icons 48 cells on, not 32
		if (icon >= DIVINE_BLAST + SPELL_ACTION_OFFSET && icon <= ENCHANT + SPELL_ACTION_OFFSET) icon += 16;
		//deranged, icons of 20000 and up come from DERanged PD's own sheet
		if (icon >= DR && icon < DR + 10000){
			texture( Assets.Interfaces.DR_HERO_ICONS );
			if (drFilm == null) drFilm = new TextureFilm(texture, SIZE, SIZE);
			frame(drFilm.get(icon - DR));
			return;
		}
		if (icon >= RA){
			texture( Assets.Interfaces.RA_HERO_ICONS );
			if (raFilm == null) raFilm = new TextureFilm(texture, SIZE, SIZE);
			frame(raFilm.get(icon - RA));
			return;
		}
		if (film == null){
			film = new TextureFilm(texture, SIZE, SIZE);
		}
		frame(film.get(icon));
	}

	public HeroIcon(HeroSubClass subCls){
		super( Assets.Interfaces.HERO_ICONS );
		if (film == null){
			film = new TextureFilm(texture, SIZE, SIZE);
		}
		pick(subCls.icon());
	}

	public HeroIcon(ArmorAbility abil){
		super( Assets.Interfaces.HERO_ICONS );
		if (film == null){
			film = new TextureFilm(texture, SIZE, SIZE);
		}
		pick(abil.icon());
	}

	public HeroIcon(ActionIndicator.Action action){
		super( Assets.Interfaces.HERO_ICONS );
		if (film == null){
			film = new TextureFilm(texture, SIZE, SIZE);
		}
		pick(action.actionIcon());
	}

	public HeroIcon(ActionIndicator1.Action action){
		super( Assets.Interfaces.HERO_ICONS );
		if (film == null){
			film = new TextureFilm(texture, SIZE, SIZE);
		}
		pick(action.actionIcon());
	}

	public HeroIcon(ClericSpell spell){
		super( Assets.Interfaces.HERO_ICONS );
		if (film == null){
			film = new TextureFilm(texture, SIZE, SIZE);
		}
		pick(spell.icon());
	}

}
