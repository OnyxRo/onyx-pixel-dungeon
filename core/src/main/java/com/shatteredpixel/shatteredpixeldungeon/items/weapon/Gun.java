package com.shatteredpixel.shatteredpixeldungeon.items.weapon;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EffectiveShotCooldown;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

/**
 * Pixel Dungeon Reforged's homemade railgun, the Artificer's weapon. One shot, then six turns to reload (or it
 * reloads itself after a while). The shot pierces everything in a line and the blast also hits the enemies
 * around each target and the end of the beam. It grows with the hero's level, not with upgrades.
 *
 * Reforged's Artificer talents act on it (Tinkerer's Meal, Effective Shot, Arcshielding, Volatile Chain, Autoload).
 * Its damage types are left out.
 */
public class Gun extends Weapon {

    public static final String AC_SHOOT		    = "SHOOT";
    public static final String AC_RELOAD        = "RELOAD";
    public static final float TIME_TO_RELOAD	= 6f;

    //turns until the gun loads itself
    public static final float AUTO_RELOAD_TIME  = 100f;

    private boolean loaded = true;

    {
        image = ItemSpriteSheet.GUN;

        defaultAction = AC_SHOOT;

        unique = true;
        bones = false;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.remove(AC_EQUIP);
        if (loaded) {
            actions.add(AC_SHOOT);
        }
        else {
            actions.add(AC_RELOAD);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_SHOOT) && !loaded) {
            action = AC_RELOAD;
        } else
        if (action.equals(AC_RELOAD) && loaded) {
            action = AC_SHOOT;
        }

        if (action.equals(AC_SHOOT)) {
            usesTargeting = true;
            curUser = hero;
            curItem = this;
            GameScene.selectCell( shooter );
        }
        if (action.equals(AC_RELOAD)) {
            setLoaded(true);
            hero.sprite.operate( hero.pos );
            hero.spendAndNext( TIME_TO_RELOAD );
        }
    }

    public void setLoaded(boolean value) {
        loaded = value;
        usesTargeting = value;
        updateQuickslot();

        if (Dungeon.hero != null) {
            AutoReloadTracker tracker = Dungeon.hero.buff(AutoReloadTracker.class);
            if (tracker != null) tracker.detach();
            if (!value) {
                Buff.affect(Dungeon.hero, AutoReloadTracker.class, AUTO_RELOAD_TIME - 20 * Dungeon.hero.pointsInTalent(Talent.WEAPON_MOD_AUTOLOAD));
            }
        }
    }

    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public String info() {
        String info = desc();

        info += "\n\n" + Messages.get( Gun.class, "stats",
                Math.round(augment.damageFactor(min())),
                Math.round(augment.damageFactor(max())),
                STRReq());

        if (STRReq() > Dungeon.hero.STR()) {
            info += " " + Messages.get(Weapon.class, "too_heavy");
        }

        switch (augment) {
            case SPEED:
                info += "\n\n" + Messages.get(Weapon.class, "faster");
                break;
            case DAMAGE:
                info += "\n\n" + Messages.get(Weapon.class, "stronger");
                break;
            case NONE:
        }

        if (enchantment != null && (cursedKnown || !enchantment.curse())){
            info += "\n\n" + Messages.capitalize(Messages.get(Weapon.class, "enchanted", enchantment.name()));
            if (enchantHardened) info += " " + Messages.get(Weapon.class, "enchant_hardened");
            info += " " + enchantment.desc();
        } else if (enchantHardened){
            info += "\n\n" + Messages.get(Weapon.class, "hardened_no_enchant");
        }

        if (cursed && isEquipped( Dungeon.hero )) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
        } else if (cursedKnown && cursed) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed");
        } else if (!isIdentified() && cursedKnown){
            info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
        }

        info += "\n\n" + Messages.get(MissileWeapon.class, "distance");

        return info;
    }

    @Override
    public int STRReq(int lvl) {
        return 10; // gun always needs 10 strength
    }

    @Override
    public int min(int lvl) {
        int hlvl = Dungeon.hero == null ? 1 : Dungeon.hero.lvl;
        int dmg = 4 + (4*hlvl/5)
                + (Dungeon.hero == null ? 0 : RingOfSharpshooting.levelDamageBonus(Dungeon.hero) / 2)
                + (curseInfusionBonus ? 1 + hlvl/30 : 0);
        return Math.max(0, dmg);
    }

    @Override
    public int max(int lvl) {
        int hlvl = Dungeon.hero == null ? 1 : Dungeon.hero.lvl;
        int dmg = 6 + (6*hlvl/5)
                + (Dungeon.hero == null ? 0 : RingOfSharpshooting.levelDamageBonus(Dungeon.hero))
                + (curseInfusionBonus ? 2 + hlvl/15 : 0);
        return Math.max(0, dmg);
    }

    @Override
    public int level() {
        int level = Dungeon.hero == null ? 0 : Dungeon.hero.lvl/5;
        if (curseInfusionBonus) level += 1 + level/6;
        return level;
    }

    @Override
    public int buffedLvl() {
        //level isn't affected by buffs/debuffs
        return level();
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public int damageRoll(Char owner) {
        boolean empowered = owner instanceof Hero && ((Hero) owner).hasTalent(Talent.EFFECTIVE_SHOT)
                && owner.buff(EffectiveShotCooldown.class) == null;
        int damage = empowered ? augment.damageFactor(max()) : augment.damageFactor(super.damageRoll(owner));
        if (owner instanceof Hero && owner.buff(Talent.ArtificerFoodDamageBonus.class) != null) {
            damage += 3;
            owner.buff(Talent.ArtificerFoodDamageBonus.class).detach();
        }
        return damage;
    }

    private static final String LOADED = "is_loaded";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LOADED, loaded);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        loaded = !bundle.contains(LOADED) || bundle.getBoolean(LOADED);
        usesTargeting = loaded;
    }

    //fires along a line from the hero. Everything in the line takes a full hit, enemies in the cells around
    //each of them and around the end of the beam take half. radius 1 is 3x3.
    public ArrayList<Mob> fire(final Hero user, final int cell, final boolean playSFX, final boolean spendTime, int explosionRadius) {
        ArrayList<Mob> mobs = new ArrayList<>();
        ArrayList<Char> alreadyDamagedMobs = new ArrayList<>();
        ArrayList<Integer> positionsToDamage = new ArrayList<>();
        final Ballistica beam = new Ballistica(user.pos, cell, Ballistica.STOP_SOLID);

        //Arcshielding, a hurt Artificer shields themselves as they fire
        if (user.hasTalent(Talent.ARCSHIELDING)) {
            float threshold = user.pointsInTalent(Talent.ARCSHIELDING) == 2 ? 0.4f : 0.25f;
            if (user.HP / (float) user.HT <= threshold) {
                Buff.affect(user, Barrier.class).setShield(user.pointsInTalent(Talent.ARCSHIELDING) == 2 ? 5 : 3);
            }
        }

        int maxDistance = Math.min(level() * 2 + 12, beam.dist);

        ArrayList<Char> chars = new ArrayList<>();
        for (int c : beam.subPath(1, maxDistance)) {
            Char ch;
            if ((ch = Actor.findChar(c)) != null) {
                if (ch instanceof Mob && ((Mob) ch).state == ((Mob) ch).PASSIVE
                        && !(Dungeon.level.mapped[c] || Dungeon.level.visited[c])) {
                    // avoid harming undiscovered passive chars
                } else {
                    if (!(ch instanceof Mob && ch.alignment == Char.Alignment.ALLY)) {
                        chars.add(ch);
                    }
                }
            }

            if (c == beam.collisionPos) {
                break;
            }
        }

        //the roll is made once, so the Effective Shot bonus and the food bonus apply to the whole shot
        final int shotDamage = damageRoll(user);
        lastShotDamage = shotDamage;

        for (Char ch : chars) {
            if (ch instanceof Mob) {
                mobs.add((Mob) ch);
            }
            ch.damage(shotDamage, this);
            if (ch.sprite != null) ch.sprite.flash();

            positionsToDamage.add(ch.pos);
            damageAdjacentEnemies(user, ch.pos, chars, alreadyDamagedMobs, positionsToDamage, explosionRadius);
        }

        positionsToDamage.add(beam.collisionPos);
        damageAdjacentEnemies(user, beam.collisionPos, chars, alreadyDamagedMobs, positionsToDamage, explosionRadius);

        if (spendTime) {
            user.sprite.operate(user.pos);
            user.spendAndNext(1.0f);
        }

        if (playSFX) {
            Sample.INSTANCE.play(Assets.Sounds.BLAST);
        }

        burnTerrain(positionsToDamage);

        //Effective Shot, the empowered shot comes round again after a few shots
        if (user.hasTalent(Talent.EFFECTIVE_SHOT)) {
            EffectiveShotCooldown cd = user.buff(EffectiveShotCooldown.class);
            if (cd == null) {
                Buff.affect(user, EffectiveShotCooldown.class).set(7 - user.pointsInTalent(Talent.EFFECTIVE_SHOT));
            } else {
                cd.left--;
                if (cd.left <= 0) cd.detach();
            }
        }

        //Volatile Chain, the last enemy hit takes extra damage from the next weapon hit
        if (user.hasTalent(Talent.VOLATILE_CHAIN) && !chars.isEmpty()) {
            Char last = chars.get(chars.size() - 1);
            if (last.isAlive()) Buff.prolong(user, Talent.VolatileChainTracker.class, 5f).object = last.id();
        }

        Invisibility.dispel();
        user.sprite.parent.add(new Beam.LightRay(user.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(beam.collisionPos)));

        setLoaded(false);

        Dungeon.observe();
        return mobs;
    }

    private int lastShotDamage = 0;

    private ArrayList<Integer> getExplosionCells(int centerPos, int radius) {
        ArrayList<Integer> cells = new ArrayList<>();
        int width = Dungeon.level.width();
        int cx = centerPos % width;

        for (int y = -radius; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                //do not wrap round the edge of the map
                if (cx + x < 0 || cx + x >= width) continue;
                int p = centerPos + x + y * width;
                if (p < 0 || p >= Dungeon.level.length()) continue;
                cells.add(p);
            }
        }

        return cells;
    }

    private void damageAdjacentEnemies(Hero user, int centerPos, ArrayList<Char> directlyHitChars,
                                       ArrayList<Char> alreadyDamagedMobs,
                                       ArrayList<Integer> positionsToDamage,
                                       int explosionRadius) {
        for (int position : getExplosionCells(centerPos, explosionRadius)) {
            Mob adjacent = Dungeon.level.findMob(position);

            if (adjacent != null && !directlyHitChars.contains(adjacent) && !alreadyDamagedMobs.contains(adjacent)) {
                alreadyDamagedMobs.add(adjacent);

                if (adjacent.state == adjacent.PASSIVE &&
                        !(Dungeon.level.mapped[position] || Dungeon.level.visited[position])) {
                    continue;
                }

                if (adjacent.alignment != Char.Alignment.ALLY) {
                    adjacent.damage(lastShotDamage / 2, this);
                }
            }

            positionsToDamage.add(position);
        }
    }

    private void burnTerrain(ArrayList<Integer> positionsToDamage) {
        for (Integer p : positionsToDamage) {
            if (p < 0 || p >= Dungeon.level.map.length) {
                continue;
            }

            int terrain = Dungeon.level.map[p];

            if (terrain == Terrain.EMPTY ||
                    terrain == Terrain.EMPTY_DECO ||
                    terrain == Terrain.OPEN_DOOR ||
                    terrain == Terrain.GRASS ||
                    terrain == Terrain.FURROWED_GRASS ||
                    Dungeon.level.flamable[p]) {

                Level.set(p, Terrain.EMBERS);
                GameScene.updateMap(p);
            }

            if (Dungeon.level.heroFOV[p]) {
                CellEmitter.get(p).burst(SmokeParticle.FACTORY, 8);
            }
        }
    }

    private CellSelector.Listener shooter = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                fire(curUser, target, true, true, 1);
            }
        }
        @Override
        public String prompt() {
            return Messages.get(Gun.class, "prompt");
        }
    };

    @Override
    public String status() {
        return loaded ? "1/1" : "0/1";
    }

    public static class AutoReloadTracker extends FlavourBuff {

        @Override
        public void detach() {
            super.detach();
        }

        @Override
        public boolean act() {
            Gun gun = Dungeon.hero.belongings.getItem(Gun.class);
            if (gun != null && !gun.isLoaded()) {
                gun.loaded = true;
                gun.usesTargeting = true;
                Item.updateQuickslot();
                GLog.p( Messages.get(Gun.class, "auto_reload") );
            }
            return super.act();
        }
    }
}
