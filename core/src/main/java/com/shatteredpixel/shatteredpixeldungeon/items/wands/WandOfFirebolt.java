package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class WandOfFirebolt extends DamageWand {

    //Blazing keeps its own color private
    private static final ItemSprite.Glowing ORANGE = new ItemSprite.Glowing( 0xFF4400 );

    {
        image = ItemSpriteSheet.WAND_FIREBOLT;
    }

    @Override
    public int min(int lvl) {
        return lvl;
    }

    @Override
    public int max(int lvl) {
        return 9+6*lvl;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return ORANGE;
    }

    @Override
    public void onZap(Ballistica attack) {
        Char ch = Actor.findChar(attack.collisionPos);
        boolean found = ch != null;
        for(int cell : attack.subPath(1,found ? attack.dist-1 : attack.dist)) {
            if(Dungeon.level.flamable[cell] || cell == attack.collisionPos) GameScene.add(Fire.seed(cell,1,Fire.class));
            Heap heap = Dungeon.level.heaps.get(cell);
            if(heap != null) heap.burn();
        }
        if(found) {
            int dmg = damageRoll();
            wandProc(ch, 1);
            ch.damage(dmg, this);
            if(ch.isAlive()) Buff.affect(ch, Burning.class).reignite(ch);
            ch.sprite.emitter().burst(FlameParticle.FACTORY, 5);
        }
    }

    @Override
    public String upgradeStat2(int level) {
        return Integer.toString(6 + level);
    }

    @Override
    public void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.FIRE,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    public final Weapon.Enchantment onHit = new Weapon.Enchantment() {
        @Override
        protected float procChanceMultiplier(Char attacker) {
            return Wand.procChanceMultiplier(attacker);
        }

        @Override
        public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
            // pre-rework blazing ;)
            // lvl 0 - 33%
            // lvl 1 - 50%
            // lvl 2 - 60%
            int level = Math.max(0, weapon.buffedLvl());
            float procChance = (level+1f)/(level+3f) * procChanceMultiplier(attacker);
            if (Random.Float() < procChance) {
                float effectBoost = procChance - 1;
                float igniteChance = .5f;
                if(effectBoost > 0) {
                    // put half of the bonus into ignite chance.
                    igniteChance += effectBoost /= 2;
                    if(igniteChance > 1) {
                        effectBoost += igniteChance - 1;
                        igniteChance = 1;
                    }
                }
                if (Random.Float() < igniteChance) {
                    Buff.affect(defender, Burning.class).reignite(defender);
                }
                if (!defender.isImmune(WandOfFirebolt.this.getClass())) {
                    int bonusDamage = Random.Int(1, level + 2);
                    if(effectBoost > 0) bonusDamage = Math.round(bonusDamage * effectBoost);
                    defender.damage(bonusDamage, WandOfFirebolt.this);
                }

                defender.sprite.emitter().burst(FlameParticle.FACTORY, level + 1);
            }
            return damage;
        }

        @Override
        public ItemSprite.Glowing glowing() { return WandOfFirebolt.this.glowing(); }
    };
    @Override public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        onHit.proc(staff, attacker, defender, damage);
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( ORANGE.color );
        particle.am = 0.5f;
        particle.setLifespan(0.6f);
        particle.acc.set(0, -40);
        particle.setSize( 0f, 3f);
        particle.shuffleXY( 1.5f );
    }

    @Override
    public int value() {
        return super.value()*4/3;
    }
}
