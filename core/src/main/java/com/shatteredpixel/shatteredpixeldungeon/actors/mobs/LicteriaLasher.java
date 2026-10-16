package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LicteriaLasherSprite;
import com.watabou.utils.Random;

public class LicteriaLasher extends Mob {

    {
        spriteClass = LicteriaLasherSprite.class;

        HP = HT = 6;
        defenseSkill = 0;

        EXP = 0;


        state = WANDERING = new LicteriaLasher.Waiting();
        viewDistance = 1;

        immunities.add(ShieldBuff.class);
        immunities.add(Healing.class);

        properties.add(Property.IMMOVABLE);
        properties.add(Property.INORGANIC);
    }

    @Override
    protected boolean act() {
        if (HP < HT && (enemy == null || !Dungeon.level.adjacent(pos, enemy.pos))) {
            speak(Integer.toString(Math.min(1, HT - HP)), CharSprite.POSITIVE);
            HP = Math.min(HT, HP + 1);
        }
        return super.act();
    }

    @Override
    public boolean interact(Char c) {
        if (c instanceof Hero) return true;
        return super.interact(c);
    }

    @Override
    public float attackDelay() {
        return super.attackDelay() * 2;
    }

    @Override
    public void damage(int dmg, Object src) {
        if (src instanceof Burning) {
            destroy();
            sprite.die();
        } else {
            super.damage(1, src);
        }
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);
        Ballistica b = new Ballistica(pos, enemy.pos, Ballistica.STOP_SOLID);

        if (b.dist > 1)
            WandOfBlastWave.throwChar(enemy, new Ballistica(pos, enemy.pos, Ballistica.STOP_SOLID), 2, false, false, this.getClass());

        return super.attackProc(enemy, damage);
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    protected boolean getCloser(int target) {
        return false;
    }

    @Override
    protected boolean getFurther(int target) {
        return false;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(1, Dungeon.hero.lvl+2);
    }

    @Override
    public int attackSkill(Char target) {
        return 25;
    }

    {
        immunities.add(ToxicGas.class);
    }

    private class Waiting extends Mob.Wandering {

        @Override
        protected boolean noticeEnemy() {
            spend(TICK);
            return super.noticeEnemy();
        }
    }

}