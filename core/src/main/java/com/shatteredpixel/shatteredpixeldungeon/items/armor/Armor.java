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

package com.shatteredpixel.shatteredpixeldungeon.items.armor;

import com.shatteredpixel.shatteredpixeldungeon.items.MetalPart;
import com.shatteredpixel.shatteredpixeldungeon.effects.Transmuting;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.engineer.ForceField;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Momentum;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.AuraOfProtection;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.BodyForm;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.HolyWard;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.LifeLinkSpell;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.PrismaticImage;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.AntiEntropy;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Bulk;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Displacement;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Metabolism;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Multiplicity;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Overgrowth;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Stench;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Affection;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Brimstone;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Camouflage;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Entanglement;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Flow;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Obfuscation;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Potential;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Repulsion;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Stone;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Swiftness;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Thorns;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfArcana;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ParchmentScrap;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ShardOfOblivion;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

public class Armor extends EquipableItem {

	protected static final String AC_DETACH       = "DETACH";
	
	public enum Augment {
		EVASION (2f , -1f),
		DEFENSE (-2f, 1f),
		NONE	(0f   ,  0f);
		
		private float evasionFactor;
		private float defenceFactor;
		
		Augment(float eva, float df){
			evasionFactor = eva;
			defenceFactor = df;
		}
		
		public int evasionFactor(int level){
			return Math.round((2 + level) * evasionFactor);
		}
		
		public int defenseFactor(int level){
			return Math.round((2 + level) * defenceFactor);
		}
	}
	
	public Augment augment = Augment.NONE;

	//Matt Edition special augment (Icy Core), kept next to Shattered's evasion and defense augment
	public com.shatteredpixel.shatteredpixeldungeon.items.armor.augments.ArmorAugment special = null;
	
	public Glyph glyph;
	public boolean glyphHardened = false;
	public boolean curseInfusionBonus = false;
	public boolean masteryPotionBonus = false;
	
	protected BrokenSeal seal;
	
	public int tier;
	
	private static final int USES_TO_ID = 10;
	private float usesLeftToID = USES_TO_ID;
	private float availableUsesToID = USES_TO_ID/2f;
	
	//Shittened PD's armors name a picture ID as well. Ours is drawn from the tier.
	public Armor( int tier, int ID ) {
		this( tier );
	}

	public Armor( int tier ) {
		this.tier = tier;
	}
	
	private static final String USES_LEFT_TO_ID = "uses_left_to_id";
	private static final String AVAILABLE_USES  = "available_uses";
	private static final String GLYPH			= "glyph";
	private static final String GLYPH_HARDENED	= "glyph_hardened";
	private static final String CURSE_INFUSION_BONUS = "curse_infusion_bonus";
	private static final String MASTERY_POTION_BONUS = "mastery_potion_bonus";
	private static final String SEAL            = "seal";
	private static final String AUGMENT			= "augment";
	private static final String MAGIC_LEVEL		= "magic_level";
	private static final String SPECIAL			= "special_augment";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		if (modify != null) bundle.put( "modify", modify ); //xun
		bundle.put( "mod_durability", modDurability );
		bundle.put( USES_LEFT_TO_ID, usesLeftToID );
		bundle.put( AVAILABLE_USES, availableUsesToID );
		bundle.put( GLYPH, glyph );
		bundle.put( GLYPH_HARDENED, glyphHardened );
		bundle.put( CURSE_INFUSION_BONUS, curseInfusionBonus );
		bundle.put( MASTERY_POTION_BONUS, masteryPotionBonus );
		bundle.put( SEAL, seal);
		bundle.put( AUGMENT, augment);
		bundle.put( SPECIAL, special);
		if (magicLevel != 0) bundle.put( MAGIC_LEVEL, magicLevel );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		if (bundle.contains( "modify" )) modify = bundle.getEnum( "modify", Modification.class ); //xun
		modDurability = bundle.getInt( "mod_durability" );
		usesLeftToID = bundle.getInt( USES_LEFT_TO_ID );
		availableUsesToID = bundle.getInt( AVAILABLE_USES );
		inscribe((Glyph) bundle.get(GLYPH));
		glyphHardened = bundle.getBoolean(GLYPH_HARDENED);
		curseInfusionBonus = bundle.getBoolean( CURSE_INFUSION_BONUS );
		masteryPotionBonus = bundle.getBoolean( MASTERY_POTION_BONUS );
		seal = (BrokenSeal)bundle.get(SEAL);
		magicLevel = bundle.getInt( MAGIC_LEVEL );
		
		augment = bundle.getEnum(AUGMENT, Augment.class);
		special = (com.shatteredpixel.shatteredpixeldungeon.items.armor.augments.ArmorAugment) bundle.get( SPECIAL );
	}

	public Armor applyAugment( com.shatteredpixel.shatteredpixeldungeon.items.armor.augments.ArmorAugment special ){
		if (this.special != null){
			GLog.i(Messages.get(Armor.class, "removed_augment", this.name(), this.special.name()));
		}
		this.special = special;
		updateQuickslot();
		if (special != null) {
			GLog.p(Messages.get(Armor.class, "got_augment", this.name(), special.name()));
		}
		return this;
	}

	@Override
	public void reset() {
		super.reset();
		usesLeftToID = USES_TO_ID;
		availableUsesToID = USES_TO_ID/2f;
		//armor can be kept in bones between runs, the seal cannot.
		seal = null;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (seal != null) actions.add(AC_DETACH);
		//rearranged, the Gunner melts armor down for liquid metal
		if (!isEquipped(hero) && hero.heroClass.is(HeroClass.GUNNER)) {
			actions.add(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon.AC_SCRAP);
		}
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon.AC_SCRAP)) {
			com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RearrangedWeapons.scrapArmor(this, hero); //rearranged
		}

		if (action.equals(AC_DETACH) && seal != null){
			BrokenSeal detaching = detachSeal();
			GLog.i( Messages.get(Armor.class, "detach_seal") );
			hero.sprite.operate(hero.pos);
			if (!detaching.collect()){
				Dungeon.level.drop(detaching, hero.pos);
			}
			updateQuickslot();
		}
	}

	@Override
	public boolean collect(Bag container) {
		if(super.collect(container)){
			if (Dungeon.hero != null && Dungeon.hero.isAlive() && isIdentified() && glyph != null){
				Catalog.setSeen(glyph.getClass());
				Statistics.itemTypesDiscovered.add(glyph.getClass());
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Item identify(boolean byHero) {
		if (glyph != null && byHero && Dungeon.hero != null && Dungeon.hero.isAlive()){
			Catalog.setSeen(glyph.getClass());
			Statistics.itemTypesDiscovered.add(glyph.getClass());
		}
		return super.identify(byHero);
	}

	public void setIDReady(){
		usesLeftToID = -1;
	}

	public boolean readyToIdentify(){
		return !isIdentified() && usesLeftToID <= 0;
	}

	@Override
	public boolean doEquip( Hero hero ) {
		//Transformation PD, the Slime Girl can only wear slime armor
		if (hero.heroClass == HeroClass.SLIMEGIRL && !((this instanceof SlimeArmor) || (this instanceof SlimeGirlArmor))){
			GLog.n( Messages.get(Armor.class, "slime") );
			return false;
		}

		// 15/25% chance
		if (!hero.heroClass.is(HeroClass.CLERIC) && hero.hasTalent(Talent.HOLY_INTUITION)
				&& cursed && !cursedKnown
				&& Random.Int(20) < 1 + 2*hero.pointsInTalent(Talent.HOLY_INTUITION)){
			cursedKnown = true;
			GLog.p(Messages.get(this, "curse_detected"));
			return false;
		}

		//Ryuse's PD, with an extra armor slot free the player picks where this armor goes
		if (hero.belongings.armor != null && hero.belongings.freeExtraArmor() >= 0 && !slotAsked){
			GameScene.show(new WndOptions(new ItemSprite(this),
					Messages.titleCase(name()),
					Messages.get(Armor.class, "slot_message"),
					Messages.get(Armor.class, "slot_main"),
					Messages.get(Armor.class, "slot_extra")){
				@Override
				protected void onSelect(int index) {
					slotAsked = true;
					wearExtra = index == 1;
					try {
						doEquip(Dungeon.hero);
					} finally {
						slotAsked = false;
						wearExtra = false;
					}
					updateQuickslot();
				}
			});
			return false;
		}

		if (wearExtra && hero.belongings.freeExtraArmor() >= 0){
			detach(hero.belongings.backpack);
			hero.belongings.extraArmors[hero.belongings.freeExtraArmor()] = this;
			cursedKnown = true;
			if (cursed) {
				equipCursed( hero );
				GLog.n( Messages.get(Armor.class, "equip_cursed") );
			}
			activate(hero);
			Talent.onItemEquipped(hero, this);
			hero.spend( timeToEquip( hero ) );
			hero.next();
			return true;
		}

		detach(hero.belongings.backpack);

		Armor oldArmor = hero.belongings.armor;
		if (hero.belongings.armor == null || hero.belongings.armor.doUnequip( hero, true, false )) {
			
			hero.belongings.armor = this;
			
			cursedKnown = true;
			if (cursed) {
				equipCursed( hero );
				GLog.n( Messages.get(Armor.class, "equip_cursed") );
			}
			
			((HeroSprite)hero.sprite).updateArmor();
			activate(hero);
			Talent.onItemEquipped(hero, this);
			hero.spend( timeToEquip( hero ) );

			if (Dungeon.hero.heroClass.is(HeroClass.WARRIOR) && checkSeal() == null){
				BrokenSeal seal = oldArmor != null ? oldArmor.checkSeal() : null;
				if (seal != null && (!cursed || (seal.getGlyph() != null && seal.getGlyph().curse()))){

					GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.SEAL),
							Messages.titleCase(seal.trueName()),
							Messages.get(Armor.class, "seal_transfer"),
							Messages.get(Armor.class, "seal_transfer_yes"),
							Messages.get(Armor.class, "seal_transfer_no")){
						@Override
						protected void onSelect(int index) {
							super.onSelect(index);
							if (index == 0){
								seal.affixToArmor(Armor.this, oldArmor);
								updateQuickslot();
							}
							super.hide();
						}

						@Override
						public void hide() {
							//do nothing, must press button
						}
					});
				} else {
					hero.next();
				}
			} else {
				hero.next();
			}
			return true;
			
		} else {
			
			collect( hero.belongings.backpack );
			return false;
			
		}
	}

	@Override
	public void activate(Char ch) {
		if (seal != null) Buff.affect(ch, BrokenSeal.WarriorShield.class).setArmor(this);
	}

	public void affixSeal(BrokenSeal seal){
		this.seal = seal;
		if (seal.level() > 0){
			//doesn't trigger upgrading logic such as affecting curses/glyphs
			int newLevel = trueLevel()+1;
			level(newLevel);
			Badges.validateItemLevelAquired(this);
		}
		if (seal.getGlyph() != null){
			inscribe(seal.getGlyph());
		}
		if (isEquipped(Dungeon.hero)){
			Buff.affect(Dungeon.hero, BrokenSeal.WarriorShield.class).setArmor(this);
		}
	}

	public BrokenSeal detachSeal(){
		if (seal != null){

			if (isEquipped(Dungeon.hero)) {
				BrokenSeal.WarriorShield sealBuff = Dungeon.hero.buff(BrokenSeal.WarriorShield.class);
				if (sealBuff != null) sealBuff.setArmor(null);
			}

			BrokenSeal detaching = seal;
			seal = null;

			if (detaching.level() > 0){
				degrade();
			}
			if (detaching.canTransferGlyph()){
				inscribe(null);
			} else {
				detaching.setGlyph(null);
			}
			return detaching;
		} else {
			return null;
		}
	}

	public BrokenSeal checkSeal(){
		return seal;
	}

	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		if (hero.heroClass == HeroClass.SLIMEGIRL && !((this instanceof SlimeArmor) || (this instanceof SlimeGirlArmor))){
			GLog.n( Messages.get(Armor.class, "slime") );
			return false;
		}
		if (super.doUnequip( hero, collect, single )) {

			if (hero.belongings.clearExtraSlot(this)){
				return true;
			}

			hero.belongings.armor = null;
			((HeroSprite)hero.sprite).updateArmor();

			BrokenSeal.WarriorShield sealBuff = hero.buff(BrokenSeal.WarriorShield.class);
			if (sealBuff != null) sealBuff.setArmor(null);

			return true;

		} else {

			return false;

		}
	}
	
	@Override
	public boolean isEquipped( Hero hero ) {
		return hero != null && (hero.belongings.armor() == this || hero.belongings.inExtraSlot(this));
	}

	//Ryuse's PD, set while the slot choice dialog is open
	private transient boolean slotAsked = false;
	private transient boolean wearExtra = false;

	//from Transformation PD, armor numbers used by hero replicas
	public int replicaDRMax() { return DRMax(); }

	public int replicaDRMin() { return DRMin(); }

	public float replicaEvasionFactor(Char owner, float evasion) {
		return evasion + augment.evasionFactor(buffedLvl());
	}

	public float replicaSpeedFactor(Char owner, float speed) {
		return speed;
	}

	//Matt Edition, a second level raised by the scroll of magic upgrade. It does not add to physical armor,
	//it only blocks magic damage. Raising the normal level makes the magic level count for less.
	public int magicLevel = 0;

	public final int magicDRMax(){
		return magicDRMax(magicLevel);
	}

	public int magicDRMax(int magicLvl){
		if (Dungeon.isChallenged(Challenges.NO_ARMOR)){
			return magicLvl > 0 ? 1 + tier + magicLvl : 0;
		}

		int physicalPenalty = (int)(level() * 0.18f);
		int effective = Math.max(0, magicLvl - physicalPenalty);
		if (effective == 0) return 0;

		//T1 and T2 are x1, T3 and T4 x2, T5 x3
		int tierMultiplier = (int)((tier + 0.5) / 1.5);
		int baseValue = 2 + (effective / 2) + (effective % 2);
		int maxMagicDR = tierMultiplier * baseValue;

		if (effective > maxMagicDR){
			return (effective - maxMagicDR + 1) / 2;
		}
		return maxMagicDR;
	}

	public final int magicDRMin(){
		return magicDRMin(magicLevel);
	}

	public int magicDRMin(int magicLvl){
		if (Dungeon.isChallenged(Challenges.NO_ARMOR)) return 0;

		int physicalPenalty = (int)(level() * 0.18f);
		int after = magicLvl - physicalPenalty;
		int effective = Math.max(0, (after / 2) + (after % 2));
		int max = magicDRMax(magicLvl);

		if (effective >= max) return effective - max;
		return effective;
	}

	public final int DRMax(){
		return DRMax(buffedLvl());
	}

	public int DRMax(int lvl){
		if (Dungeon.isChallenged(Challenges.NO_ARMOR) || com.shatteredpixel.shatteredpixeldungeon.GameModes.weakArmor()){
			return 1 + tier + lvl + augment.defenseFactor(lvl);
		}

		//rearranged, the afterimage glyph trades armor for evasion, the Knight's Armor Adaption turns spare strength into armor
		int max = com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RearrangedWeapons.armorUpgradeFactor(this, tier) * (2 + lvl) + augment.defenseFactor(lvl);
		max += com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RearrangedWeapons.armorAdaption(this);
		max += com.shatteredpixel.shatteredpixeldungeon.RBAHooks.armorMaxBonus(tier); //rebluearchive, Hoshino
		int extra = com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.armorMax(this); //transformation, Heavy Application and Thickened Armor
		if (lvl > max){
			return ((lvl - max)+1)/2 + extra;
		} else {
			return max + extra;
		}
	}

	//rearranged, the Knight's Smithing Spell and Craftman's Skills lend worn armor extra levels
	@Override
	public int buffedLvl() {
		int lvl = super.buffedLvl();
		Hero hero = Dungeon.hero;
		if (hero != null && isEquipped( hero )) {
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArmorEnhance armorEnhance = hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArmorEnhance.class);
			if (armorEnhance != null) {
				lvl = armorEnhance.armorLevel(lvl);
			}
			lvl += hero.pointsInTalent(com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.CRAFTMANS_SKILLS);
		}
		return lvl;
	}

	public final int DRMin(){
		return DRMin(buffedLvl());
	}

	public int DRMin(int lvl){
		if (Dungeon.isChallenged(Challenges.NO_ARMOR) || com.shatteredpixel.shatteredpixeldungeon.GameModes.weakArmor()){
			return 0;
		}

		int max = DRMax(lvl);
		if (lvl >= max){
			return (lvl - max);
		} else {
			return lvl;
		}
	}

	//This exists so we can test what a char's base evasion would be without armor affecting it
	//more ugly static vars yaaay~
	public static boolean testingNoArmDefSkill = false;
	
	public float evasionFactor( Char owner, float evasion ){
		if (testingNoArmDefSkill) return evasion;
		if (modify == Modification.DEFLECTION) evasion *= 1.5f; //xun
		if (owner == Dungeon.hero && com.shatteredpixel.shatteredpixeldungeon.actors.hero.XunTalents.toolArmorMod(Modification.DEFLECTION)) evasion *= 1.5f; //xun, Multi Modify
		evasion *= com.shatteredpixel.shatteredpixeldungeon.actors.hero.XunTalents.favoriteWork(owner, modify != null); //xun
		
		if (hasGlyph(Stone.class, owner) && !Stone.testingEvasion()){
			return 0;
		}
		
		if (owner instanceof Hero){
			int aEnc = STRReq() - ((Hero) owner).STR();
			if (aEnc > 0) evasion /= Math.pow(1.5, aEnc);
			
			Momentum momentum = owner.buff(Momentum.class);
			if (momentum != null){
				evasion += momentum.evasionBonus(((Hero) owner).lvl, Math.max(0, -aEnc));
			}
			evasion = com.shatteredpixel.shatteredpixeldungeon.actors.hero.RearrangedRework.unencumberedMovement((Hero) owner, aEnc, evasion); //rearranged
			//rearranged, the Slayer while awake, and the afterimage glyph
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awakening awakening = owner.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awakening.class);
			if (awakening != null) {
				evasion += awakening.evasionBonus(((Hero) owner).lvl, Math.max(0, -aEnc));
			}
			if (hasGlyph(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Afterimage.class, owner)){
				evasion *= Math.pow(1.2f, this.buffedLvl());
			}
		}
		
		return evasion + augment.evasionFactor(buffedLvl());
	}
	
	//Magic Ling PD, armor that changes how loud its wearer is
	public float stealthFactor( Char owner, float stealth ){
		return stealth;
	}

	public float speedFactor( Char owner, float speed ){
		if (modify == Modification.EXOSKELETON) speed *= 1.25f; //xun
		if (owner == Dungeon.hero && com.shatteredpixel.shatteredpixeldungeon.actors.hero.XunTalents.toolArmorMod(Modification.EXOSKELETON)) speed *= 1.25f; //xun, Multi Modify
		
		if (owner instanceof Hero) {
			int aEnc = STRReq() - ((Hero) owner).STR();
			if (aEnc > 0) speed /= Math.pow(1.2, aEnc);
		}
		
		return speed;
		
	}
	
	@Override
	public int level() {
		int level = super.level();
		//TODO warrior's seal upgrade should probably be considered here too
		// instead of being part of true level
		if (curseInfusionBonus) level += 1 + level/6;
		return level;
	}
	
	@Override
	public Item upgrade() {
		return upgrade( false );
	}
	
	public Item upgrade( boolean inscribe ) {

		if (glyph != null && com.shatteredpixel.shatteredpixeldungeon.RBAHooks.keepsEnchant()) inscribe = true; //rebluearchive, Aris
		if (inscribe){
			if (glyph == null){
				inscribe( Glyph.random() );
			}
		} else if (glyph != null) {
			//chance to lose harden buff is 10/20/40/80/100% when upgrading from +6/7/8/9/10
			if (glyphHardened) {
				if (level() >= 6 && Random.Float(10) < Math.pow(2, level()-6)){
					glyphHardened = false;
				}

			//chance to remove curse is a static 33%
			} else if (hasCurseGlyph()){
				if (glyph.removableByUpgrade() && Random.Int(3) == 0) inscribe(null);

			//otherwise chance to lose glyph is 10/20/40/80/100% when upgrading from +4/5/6/7/8
			} else {

				//the chance from +4/5, and then +6 can be set to 0% with metamorphed runic transference
				int lossChanceStart = 4;
				if (Dungeon.hero != null && !Dungeon.hero.heroClass.is(HeroClass.WARRIOR) && Dungeon.hero.heroClass != HeroClass.BA_ARIS && Dungeon.hero.hasTalent(Talent.RUNIC_TRANSFERENCE)){
					lossChanceStart += 1+Dungeon.hero.pointsInTalent(Talent.RUNIC_TRANSFERENCE);
				}

				if (level() >= lossChanceStart && Random.Float(10) < Math.pow(2, level()-4)) {
					inscribe(null);
				}
			}
		}
		
		if (!(hasCurseGlyph() && !glyph.removableByUpgrade())) {
			cursed = false;
		}

		if (seal != null && seal.level() == 0)
			seal.upgrade();

		return super.upgrade();
	}
	
	public int proc( Char attacker, Char defender, int damage ) {

		//xun, the Engineer's armor modifications wear down as the hero is hit
		if (modify == Modification.WEAKNESS_ENHANCE) damage = Math.max(0, damage - Math.round(DRMax() * 0.25f));
		if (defender == Dungeon.hero && com.shatteredpixel.shatteredpixeldungeon.actors.hero.XunTalents.toolArmorMod(Modification.WEAKNESS_ENHANCE)) damage = Math.max(0, damage - Math.round(DRMax() * 0.25f)); //xun, Multi Modify
		if (modify == Modification.EXPLOSIVE && attacker != null && attacker.isAlive()){
			//the charge goes off against the attacker only, so the wearer is not caught in it
			com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter.center(attacker.pos).burst(com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle.FACTORY, 10);
			com.watabou.noosa.audio.Sample.INSTANCE.play(com.shatteredpixel.shatteredpixeldungeon.Assets.Sounds.BLAST);
			attacker.damage(com.watabou.utils.Random.NormalIntRange(4 + Dungeon.scalingDepth(), 12 + 3 * Dungeon.scalingDepth()), this);
		}
		if (modify == Modification.EXPLOSIVE || modify == Modification.WEAKNESS_ENHANCE
				|| modify == Modification.EXOSKELETON || modify == Modification.DEFLECTION)
			decreaseModDurability();

		if (special != null && defender.buff(MagicImmune.class) == null) {
			damage = special.proc(this, attacker, defender, damage);
		}

		if (defender.buff(MagicImmune.class) == null) {
			Glyph trinityGlyph = null;
			//only when it's the hero or a char that uses the hero's armor
			if (Dungeon.hero.buff(BodyForm.BodyFormBuff.class) != null
					&& (defender == Dungeon.hero || defender instanceof PrismaticImage || defender instanceof ShadowClone.ShadowAlly)){
				trinityGlyph = Dungeon.hero.buff(BodyForm.BodyFormBuff.class).glyph();
				if (glyph != null && trinityGlyph != null && trinityGlyph.getClass() == glyph.getClass()){
					trinityGlyph = null;
				}
			}

			//Transformation PD, the Ring of the King carries a glyph of its own
			Glyph ringGlyph = null;
			com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfKing kingRing = null;
			if (defender instanceof Hero) {
				kingRing = ((Hero) defender).belongings.getItem(com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfKing.class);
				if (kingRing != null && kingRing.glyph != null && !kingRing.cursed && kingRing.isEquipped((Hero) defender)) {
					ringGlyph = kingRing.glyph;
				}
			}

			if (defender instanceof Hero && isEquipped((Hero) defender)
					&& defender.buff(HolyWard.HolyArmBuff.class) != null){
				if (ringGlyph != null){
					damage = ringGlyph.proc( kingRing.getArmor(), attacker, defender, damage );
				}
				if (glyph != null &&
						(((Hero) defender).subClass.is(HeroSubClass.PALADIN) || hasCurseGlyph())){
					damage = glyph.proc( this, attacker, defender, damage );
				}
				if (trinityGlyph != null){
					damage = trinityGlyph.proc( this, attacker, defender, damage );
				}
				int blocking = ((Hero) defender).subClass.is(HeroSubClass.PALADIN) ? 3 : 1;
				damage -= Math.round(blocking * Glyph.genericProcChanceMultiplier(defender));

			} else {
				if (glyph != null) {
					damage = glyph.proc(this, attacker, defender, damage);
				}
				if (trinityGlyph != null){
					damage = trinityGlyph.proc( this, attacker, defender, damage );
				}
				if (ringGlyph != null){
					damage = ringGlyph.proc( kingRing.getArmor(), attacker, defender, damage );
				}
				//so that this effect procs for allies using this armor via aura of protection
				if (defender.alignment == Dungeon.hero.alignment
						&& Dungeon.hero.buff(AuraOfProtection.AuraBuff.class) != null
						&& (Dungeon.level.distance(defender.pos, Dungeon.hero.pos) <= 2 || defender.buff(LifeLinkSpell.LifeLinkSpellBuff.class) != null)
						&& Dungeon.hero.buff(HolyWard.HolyArmBuff.class) != null) {
					int blocking = Dungeon.hero.subClass.is(HeroSubClass.PALADIN) ? 3 : 1;
					damage -= Math.round(blocking * Glyph.genericProcChanceMultiplier(defender));
				}
			}
			damage = Math.max(damage, 0);
		}
		
		if (!levelKnown && defender == Dungeon.hero) {
			float uses = Math.min( availableUsesToID, Talent.itemIDSpeedFactor(Dungeon.hero, this) );
			availableUsesToID -= uses;
			usesLeftToID -= uses;
			if (usesLeftToID <= 0) {
				if (ShardOfOblivion.passiveIDDisabled()){
					if (usesLeftToID > -1){
						GLog.p(Messages.get(ShardOfOblivion.class, "identify_ready"), name());
					}
					setIDReady();
				} else {
					identify();
					GLog.p(Messages.get(Armor.class, "identify"));
					Badges.validateItemLevelAquired(this);
				}
			}
		}
		
		return damage;
	}
	
	@Override
	public void onHeroGainExp(float levelPercent, Hero hero) {
		levelPercent *= Talent.itemIDSpeedFactor(hero, this);
		if (!levelKnown && isEquipped(hero) && availableUsesToID <= USES_TO_ID/2f) {
			//gains enough uses to ID over 0.5 levels
			availableUsesToID = Math.min(USES_TO_ID/2f, availableUsesToID + levelPercent * USES_TO_ID);
		}
	}
	
	@Override
	public String name() {
		if (isEquipped(Dungeon.hero) && !hasCurseGlyph() && Dungeon.hero.buff(HolyWard.HolyArmBuff.class) != null
			&& (!Dungeon.hero.subClass.is(HeroSubClass.PALADIN) || glyph == null)){
				return Messages.get(HolyWard.class, "glyph_name", super.name());
			} else {
				return glyph != null && (cursedKnown || !glyph.curse()) ? glyph.name( super.name() ) : super.name();

		}
	}
	
	@Override
	public String info() {
		String info = super.info();
		
		if (levelKnown) {

			info += "\n\n" + Messages.get(Armor.class, "curr_absorb", tier, DRMin(), DRMax(), STRReq());
			info += "\n" + Messages.get(Armor.class, "curr_magic_absorb", magicDRMin(), magicDRMax());
			if (magicLevel > 0) info += "\n" + Messages.get(Armor.class, "magic_level", magicLevel);
			
			if (Dungeon.hero != null && STRReq() > Dungeon.hero.STR()) {
				info += " " + Messages.get(Armor.class, "too_heavy");
			}
		} else {
			info += "\n\n" + Messages.get(Armor.class, "avg_absorb", tier, DRMin(0), DRMax(0), STRReq(0));
			info += "\n" + Messages.get(Armor.class, "avg_magic_absorb", magicDRMin(0), magicDRMax(0));

			if (Dungeon.hero != null && STRReq(0) > Dungeon.hero.STR()) {
				info += " " + Messages.get(Armor.class, "probably_too_heavy");
			}
		}

		switch (augment) {
			case EVASION:
				info += " " + Messages.get(Armor.class, "evasion");
				break;
			case DEFENSE:
				info += " " + Messages.get(Armor.class, "defense");
				break;
			case NONE:
		}

		if (isEquipped(Dungeon.hero) && !hasCurseGlyph() && Dungeon.hero.buff(HolyWard.HolyArmBuff.class) != null
				&& (!Dungeon.hero.subClass.is(HeroSubClass.PALADIN) || glyph == null)){
			info += "\n\n" + Messages.capitalize(Messages.get(Armor.class, "inscribed", Messages.get(HolyWard.class, "glyph_name", Messages.get(Glyph.class, "glyph"))));
			info += " " + Messages.get(HolyWard.class, "glyph_desc");
		} else if (glyph != null  && (cursedKnown || !glyph.curse())) {
			info += "\n\n" +  Messages.capitalize(Messages.get(Armor.class, "inscribed", glyph.name()));
			if (glyphHardened) info += " " + Messages.get(Armor.class, "glyph_hardened");
			info += " " + glyph.desc();
		} else if (glyphHardened){
			info += "\n\n" + Messages.get(Armor.class, "hardened_no_glyph");
		}
		
		if (cursed && isEquipped( Dungeon.hero )) {
			info += "\n\n" + Messages.get(Armor.class, "cursed_worn");
		} else if (cursedKnown && cursed) {
			info += "\n\n" + Messages.get(Armor.class, "cursed");
		} else if (!isIdentified() && cursedKnown){
			if (glyph != null && glyph.curse()) {
				info += "\n\n" + Messages.get(Armor.class, "weak_cursed");
			} else {
				info += "\n\n" + Messages.get(Armor.class, "not_cursed");
			}
		}

		if (seal != null) {
			info += "\n\n" + Messages.get(Armor.class, "seal_attached", seal.maxShield(tier, level()));
		}
		
		if (modify != null) info += "\n\n" + Messages.get(Armor.class, "has_modify", modify.title(), modDurability) + modify.desc(); //xun
		return info;
	}

	@Override
	public Emitter emitter() {
		if (seal == null) return super.emitter();
		Emitter emitter = new Emitter();
		emitter.pos(ItemSpriteSheet.film.width(image)/2f + 2f, ItemSpriteSheet.film.height(image)/3f);
		emitter.fillTarget = false;
		emitter.pour(Speck.factory( Speck.RED_LIGHT ), 0.6f);
		return emitter;
	}

	@Override
	public Item random() {
		//+0: 75% (3/4)
		//+1: 20% (4/20)
		//+2: 5%  (1/20)
		int n = 0;
		if (Random.Int(4) == 0) {
			n++;
			if (Random.Int(5) == 0) {
				n++;
			}
		}
		n = com.shatteredpixel.shatteredpixeldungeon.SinEffects.desireRoll(n); //Matt Edition, Sin of Desire
		level(n);

		//we use a separate RNG here so that variance due to things like parchment scrap
		//does not affect levelgen
		Random.pushGenerator(Random.Long());

			//30% chance to be cursed
			//15% chance to be inscribed
			float effectRoll = Random.Float();
			if (effectRoll < (Dungeon.isChallenged(com.shatteredpixel.shatteredpixeldungeon.Challenges.I_HATE_MYSELF) ? 0.7f : 0.3f) * ParchmentScrap.curseChanceMultiplier()) { //Matt Edition, I hate myself
				inscribe(Glyph.randomCurse());
				cursed = true;
			} else if (effectRoll >= 1f - (0.15f * ParchmentScrap.enchantChanceMultiplier())){
				inscribe();
			}

		Random.popGenerator();

		return this;
	}

	public int STRReq(){
		return STRReq(level());
	}

	public int STRReq(int lvl){
		int req = STRReq(tier, lvl);
		if (modify == Modification.EXOSKELETON) req--; //xun
		if (masteryPotionBonus){
			req -= 2;
		}
		return req;
	}

	protected static int STRReq(int tier, int lvl){
		lvl = Math.max(0, lvl);

		//strength req decreases at +1,+3,+6,+10,etc.
		return (8 + Math.round(tier * 2)) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
	}
	
	@Override
	public int value() {
		if (seal != null) return 0;

		int price = 20 * tier;
		if (hasGoodGlyph()) {
			price *= 1.5;
		}
		if (cursedKnown && (cursed || hasCurseGlyph())) {
			price /= 2;
		}
		if (levelKnown && level() > 0) {
			price *= (level() + 1);
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}

	public Armor inscribe( Glyph glyph ) {
		if (glyph == null || !glyph.curse()) curseInfusionBonus = false;
		this.glyph = glyph;
		updateQuickslot();
		//the hero needs runic transference to actually transfer, but we still attach the glyph here
		// in case they take that talent in the future
		if (seal != null){
			seal.setGlyph(glyph);
		}
		if (glyph != null && isIdentified() && Dungeon.hero != null
				&& Dungeon.hero.isAlive() && Dungeon.hero.belongings.contains(this)){
			Catalog.setSeen(glyph.getClass());
			Statistics.itemTypesDiscovered.add(glyph.getClass());
		}
		return this;
	}

	public Armor inscribe() {

		Class<? extends Glyph> oldGlyphClass = glyph != null ? glyph.getClass() : null;
		Glyph gl = Glyph.random( oldGlyphClass );
		gl = com.shatteredpixel.shatteredpixeldungeon.actors.hero.TransformationTalents.kebiGlyph(gl, oldGlyphClass); //transformation, Kebi

		return inscribe( gl );
	}

	public boolean hasGlyph(Class<?extends Glyph> type, Char owner) {
		if (owner.buff(MagicImmune.class) != null) {
			return false;
		} else if (glyph != null
				&& !glyph.curse()
				&& owner instanceof Hero
				&& isEquipped((Hero) owner)
				&& owner.buff(HolyWard.HolyArmBuff.class) != null
				&& !((Hero) owner).subClass.is(HeroSubClass.PALADIN)){
			return false;
		} else if (owner.buff(BodyForm.BodyFormBuff.class) != null
				&& owner.buff(BodyForm.BodyFormBuff.class).glyph() != null
				&& owner.buff(BodyForm.BodyFormBuff.class).glyph().getClass().equals(type)){
			return true;
		} else if (glyph != null && glyph.getClass() == type) {
			return true;
		} else if (owner instanceof Hero && ((Hero) owner).belongings != null
				&& ((Hero) owner).belongings.getItem(com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfKing.class) != null
				&& ((Hero) owner).belongings.getItem(com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfKing.class).isEquipped((Hero) owner)
				&& ((Hero) owner).belongings.getItem(com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfKing.class).glyph != null
				&& ((Hero) owner).belongings.getItem(com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfKing.class).glyph.getClass() == type
				&& !((Hero) owner).belongings.getItem(com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfKing.class).cursed) {
			return true;
		} else if (glyph != null) {
			return false;
		} else {
			return false;
		}
	}

	//these are not used to process specific glyph effects, so magic immune doesn't affect them
	public boolean hasGoodGlyph(){
		return glyph != null && !glyph.curse();
	}

	public boolean hasCurseGlyph(){
		return glyph != null && glyph.curse();
	}

	private static ItemSprite.Glowing HOLY = new ItemSprite.Glowing( 0xFFFF00 );

	@Override
	public ItemSprite.Glowing glowing() {
		if (isEquipped(Dungeon.hero) && !hasCurseGlyph() && Dungeon.hero.buff(HolyWard.HolyArmBuff.class) != null
				&& (!Dungeon.hero.subClass.is(HeroSubClass.PALADIN) || glyph == null)){
			return HOLY;
		} else {
			if (glyph == null && special != null) return special.glowing();
			return glyph != null && (cursedKnown || !glyph.curse()) ? glyph.glowing() : null;
		}
	}
	
	public static abstract class Glyph implements Bundlable {
		
		public static final Class<?>[] common = new Class<?>[]{
				Obfuscation.class, Swiftness.class, Viscosity.class, Potential.class,
				//Lovecraft PD
				com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Deflection.class,
				com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Stealth.class,
				//rearranged
				com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Mirrorimage.class };

		public static final Class<?>[] uncommon = new Class<?>[]{
				Brimstone.class, Stone.class, Entanglement.class,
				Repulsion.class, Camouflage.class, Flow.class,
				//Lovecraft PD
				com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Cloning.class,
				com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Explosion.class,
				com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Weight.class,
				com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Chaotic.class,
				//rearranged
				com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Afterimage.class,
				//xun, Xun's PD
				com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Freezingglyph.class };

		public static final Class<?>[] rare = new Class<?>[]{
				Affection.class, AntiMagic.class, Thorns.class,
				//Lovecraft PD
				com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.FreezeTime.class,
				com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Teleport.class,
				//rearranged
				com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Satisfying.class,
				//xun, Xun's PD
				com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Vengeance.class };

		public static final float[] typeChances = new float[]{
				50, //12.5% each
				40, //6.67% each
				10  //3.33% each
		};

		public static final Class<?>[] curses = new Class<?>[]{
				AntiEntropy.class, Corrosion.class, Displacement.class, Metabolism.class,
				Multiplicity.class, Stench.class, Overgrowth.class, Bulk.class,
				com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Binding.class,
				//Lovecraft PD
				com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Clumsy.class,
				//xun, Xun's PD
				com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Dizziness.class
		};
		
		public abstract int proc( Armor armor, Char attacker, Char defender, int damage );

		protected float procChanceMultiplier( Char defender ){
			return genericProcChanceMultiplier( defender );
		}

		//rearranged, for glyphs on the Knight's shield
		public float raProcChance( Char defender ){
			return procChanceMultiplier( defender );
		}

		public static float genericProcChanceMultiplier( Char defender ){
			float multi = RingOfArcana.enchantPowerMultiplier(defender);

			if (Dungeon.hero.alignment == defender.alignment
					&& Dungeon.hero.buff(AuraOfProtection.AuraBuff.class) != null
					&& (Dungeon.level.distance(defender.pos, Dungeon.hero.pos) <= 2 || defender.buff(LifeLinkSpell.LifeLinkSpellBuff.class) != null)){
				multi += 0.25f + 0.25f*Dungeon.hero.pointsInTalent(Talent.AURA_OF_PROTECTION);
			}

			return multi;
		}
		
		public String name() {
			if (!curse())
				return name( Messages.get(this, "glyph") );
			else
				return name( Messages.get(Item.class, "curse"));
		}
		
		public String name( String armorName ) {
			return Messages.get(this, "name", armorName);
		}

		public String desc() {
			return Messages.get(this, "desc");
		}

		public boolean curse() {
			return false;
		}

		//Matt Edition. A curse that returns false here survives upgrades
		public boolean removableByUpgrade() {
			return true;
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
		}
		
		public abstract ItemSprite.Glowing glowing();

		@SuppressWarnings("unchecked")
		public static Glyph random( Class<? extends Glyph> ... toIgnore ) {
			switch(Random.chances(typeChances)){
				case 0: default:
					return randomCommon( toIgnore );
				case 1:
					return randomUncommon( toIgnore );
				case 2:
					return randomRare( toIgnore );
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Glyph randomCommon( Class<? extends Glyph> ... toIgnore ){
			ArrayList<Class<?>> glyphs = new ArrayList<>(Arrays.asList(common));
			glyphs.removeAll(Arrays.asList(toIgnore));
			if (glyphs.isEmpty()) {
				return random();
			} else {
				return (Glyph) Reflection.newInstance(Random.element(glyphs));
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Glyph randomUncommon( Class<? extends Glyph> ... toIgnore ){
			ArrayList<Class<?>> glyphs = new ArrayList<>(Arrays.asList(uncommon));
			glyphs.removeAll(Arrays.asList(toIgnore));
			if (glyphs.isEmpty()) {
				return random();
			} else {
				return (Glyph) Reflection.newInstance(Random.element(glyphs));
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Glyph randomRare( Class<? extends Glyph> ... toIgnore ){
			ArrayList<Class<?>> glyphs = new ArrayList<>(Arrays.asList(rare));
			glyphs.removeAll(Arrays.asList(toIgnore));
			if (glyphs.isEmpty()) {
				return random();
			} else {
				return (Glyph) Reflection.newInstance(Random.element(glyphs));
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Glyph randomCurse( Class<? extends Glyph> ... toIgnore ){
			ArrayList<Class<?>> glyphs = new ArrayList<>(Arrays.asList(curses));
			glyphs.removeAll(Arrays.asList(toIgnore));
			if (glyphs.isEmpty()) {
				return random();
			} else {
				return (Glyph) Reflection.newInstance(Random.element(glyphs));
			}
		}
		
	}

	//xun, Xun's PD's modifications, fitted by the Engineer with metal parts
	public enum Modification {
		WEAKNESS_ENHANCE,
		EXPLOSIVE,
		CONDUCTIVE,
		DEFLECTION,
		EXOSKELETON;

		public int partCost(){
			switch (this){
				case WEAKNESS_ENHANCE:return 4;
				case EXPLOSIVE:       return 2;
				case CONDUCTIVE:      return 5;
				case DEFLECTION:      return 5;
				case EXOSKELETON:     return 10;
				default:              return 0;
			}
		}

		public int maxDurability(){
			switch (this){
				case EXPLOSIVE:       return 1;
				case CONDUCTIVE:      return 150;
				case DEFLECTION:return 15;
				case EXOSKELETON:     return 30;
				case WEAKNESS_ENHANCE:
				default:              return 25;
			}
		}

		public String title(){return Messages.get(Modification.class, toString());}
		public String desc() {return Messages.get(Modification.class, this + "_desc");}

		public boolean craftsman(){
			return this == DEFLECTION || this == EXOSKELETON;
		}
	}

	public void decreaseModDurability(){
		if (Dungeon.hero.buff(ForceField.Field.class) == null
				|| Random.Int(4) >= Dungeon.hero.pointsInTalent(Talent.REPAIR_ABILITY))
			modDurability = Math.max(modDurability - 1, 0);
		if (modDurability <= 0) modify(null);
	}

	public void modify(Modification mod){
		boolean activeRepair = modify == mod && Dungeon.hero.hasTalent(Talent.ACTIVE_REPAIR);
		modify = mod;
		if (mod == null){
			modDurability = 0;
			GLog.n(Messages.get(this, "modify_break"));
			float chance = Dungeon.hero.pointsInTalent(Talent.PART_RECYCLING) / 2f;
			while (Random.Float() < chance){
				MetalPart part = new MetalPart();
				if (!part.collect()) Dungeon.level.drop(part, Dungeon.hero.pos).sprite.drop();
				chance --;
			}
			if (Dungeon.hero.pointsInTalent(Talent.KINETIC_FRAGMENT) >= 2){
				Buff.affect(Dungeon.hero, Barrier.class).setShield(Dungeon.hero.lvl);
				Dungeon.hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(Dungeon.hero.lvl), FloatingText.SHIELDING);
			}
		} else {
			if (!activeRepair)
				modDurability = 0;

			float duraToInc = mod.maxDurability() * (1 + 0.15f * Dungeon.hero.pointsInTalent(Talent.DURABLE_MODIFIES));
			if (Dungeon.hero.pointsInTalent(Talent.ACTIVE_REPAIR) >= 2 && activeRepair) duraToInc *= 1.2f;
			modDurability += Math.round(duraToInc);

			Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
			Transmuting.show(curUser, this, this);
			curUser.sprite.operate(curUser.pos);

			if (Dungeon.hero.pointsInTalent(Talent.ACTIVE_REPAIR) >= 3 && activeRepair){
				MetalPart part = new MetalPart();
				if (!part.collect()) Dungeon.level.drop(part, Dungeon.hero.pos).sprite.drop();
			}
		}
	}

	public Modification modify = null;
	public int modDurability = 0;

}
