/*
 * Onyx's Pixel Dungeon
 * Based on Shattered Pixel Dungeon, Copyright (C) 2014-2025 Evan Debenham
 * Construct Hero from Pixel Dungeon Reforged, adapted
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ConstructSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Pixel Dungeon Reforged's Constructor ally. A construct that follows the hero, can be sent to a spot or an enemy,
 * heals slowly by itself and comes back 50 turns after it falls. Reforged's combat system is not here, so its
 * damage and armor sit on Shattered's own.
 */
public class ConstructHero extends DirectableAlly implements ActionIndicator.Action {

    {
        spriteClass = ConstructSprite.class;

        state = HUNTING;
        viewDistance = 8;
        EXP = 0;
        properties.add(Property.INORGANIC);

        immunities.add( CorrosiveGas.class );
        immunities.add( Burning.class );
        immunities.add( ScrollOfRetribution.class );
        immunities.add( ScrollOfPsionicBlast.class );
        immunities.add( AllyBuff.class );
    }

    private float healthRegen = 0.0f;

    public ConstructHero() {
        super();
        HT = HP = maxHP();
    }

    //three quarters of the hero's health
    private static int maxHP() {
        return Dungeon.hero == null ? 15 : Math.max(1, (int)(Dungeon.hero.HT * 0.75f));
    }

    public void setupActionIndicator() {
        ActionIndicator.setAction(this);
    }

    @Override
    protected boolean act() {
        //keeps its health in step with the hero's
        int target = maxHP();
        if (HT != target) {
            HP = Math.min(target, HP + Math.max(0, target - HT));
            HT = target;
        }

        flying = Dungeon.hero.pointsInTalent(Talent.CONSTRUCT_MOBILITY) == 3;
        viewDistance = 8 + Math.max(0, (Dungeon.hero.pointsInTalent(Talent.CONSTRUCT_VISION) - 1) * 2);
        boolean result = super.act();
        if (Dungeon.hero.hasTalent(Talent.CONSTRUCT_VISION)) {
            if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()) {
                fieldOfView = new boolean[Dungeon.level.length()];
            }
            Dungeon.level.updateFieldOfView(this, fieldOfView);
            GameScene.updateFog(pos, viewDistance + (int) Math.ceil(speed()));
        }
        return result;
    }

    @Override
    public void defendPos(int cell) {
        yell(Messages.get(this, "directed_position"));
        super.defendPos(cell);
    }

    @Override
    public void followHero() {
        yell(Messages.get(this, "directed_follow"));
        super.followHero();
    }

    @Override
    public void targetChar(Char ch) {
        yell(Messages.get(this, "directed_attack"));
        super.targetChar(ch);
    }

    @Override
    public int attackSkill( Char target ) {
        //same accuracy as the hero.
        return Dungeon.hero.lvl + 9;
    }

    private int level() {
        return Dungeon.hero == null ? 0 : Dungeon.hero.lvl/5;
    }

    @Override
    public int damageRoll() {
        int tier = 2 + Dungeon.hero.pointsInTalent(Talent.CONSTRUCT_LETHALITY);
        int min = tier + level();
        int max = 5*(tier+1) + level()*(tier+1);
        return Random.NormalIntRange(min, max);
    }

    @Override
    //Always spends exactly the specified amount of time, regardless of time-influencing factors
    protected void spendConstant( float time ){
        super.spendConstant(time);
        if (HP > 0 && HP < HT) {
            float amountToIncrease = 1.0f / 5.0f;
            if (Dungeon.hero.pointsInTalent(Talent.CONSTRUCT_LETHALITY) >= 2) {
                amountToIncrease = 1.0f / 2.0f;
            }

            healthRegen += amountToIncrease;

            int oldHP = HP;
            while (healthRegen > 1.0f) {
                HP = Math.min(HT, HP + 1);
                healthRegen -= 1.0f;
            }
            if (HP - oldHP > 0 && sprite != null) {
                sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(HP - oldHP), FloatingText.HEALING);
            }
        }
    }

    @Override
    public float speed() {
        float speed = super.speed();

        //moves 2 tiles at a time when returning to the hero
        if (state == WANDERING
                && defendingPos == -1
                && Dungeon.level.distance(pos, Dungeon.hero.pos) > 1){
            speed *= 2;
        }
        switch (Dungeon.hero.pointsInTalent(Talent.CONSTRUCT_MOBILITY)) {
            case 1:
                speed *= 1.5f;
                break;
            case 2:
            case 3:
                speed *= 2.0f;
                break;
        }

        return speed;
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return (int)(Dungeon.hero.defenseSkill * 0.5f);
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if (Dungeon.hero.hasTalent(Talent.CONSTRUCT_LETHALITY) && Random.Int( 2 ) == 0) {
            Buff.affect( enemy, Cripple.class );
        }
        return damage;
    }

    public static class Retaliation{} // Just used to avoid loop of retaliation damage

    @Override
    public void damage( int dmg, Object src ) {
        if (Dungeon.hero.pointsInTalent(Talent.CONSTRUCT_LETHALITY) >= 3 && enemy != null && enemy.isAlive()
                && Dungeon.level.adjacent(pos, enemy.pos) && dmg >= 10 && !(src instanceof Retaliation)) {
            enemy.damage((int) (dmg * 0.1f), new Retaliation());
        }
        super.damage(dmg, src);
    }

    @Override
    public int drRoll() {
        int drRoll = Dungeon.hero.drRoll();
        return (int)(drRoll * 0.8f);
    }

    @Override
    public void die(Object cause) {
        sayDefeated();
        Dungeon.hero.NotifyConstructDeath();
        ActionIndicator.clearAction(this);
        if (Dungeon.hero.pointsInTalent(Talent.CONSTRUCT_LETHALITY) >= 3) {
            CellEmitter.get(pos).burst(SmokeParticle.FACTORY, 8);

            ArrayList<Char> affected = new ArrayList<>();
            PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), 2 );
            for (int i = 0; i < PathFinder.distance.length; i++) {
                if (PathFinder.distance[i] < Integer.MAX_VALUE && Actor.findChar(i) != null) {
                    Char thisChar = Actor.findChar((i));
                    if (thisChar.alignment == Char.Alignment.ENEMY) {
                        affected.add(thisChar);
                    }
                }
            }
            for (Char ch : affected){
                ch.damage(damageRoll() * 3, this);

                if (ch.isAlive()) {
                    //trace a ballistica to our target (which will also extend past them
                    Ballistica trajectory = new Ballistica(pos, ch.pos, Ballistica.STOP_TARGET);
                    //trim it to just be the part that goes past them
                    trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size()-1), Ballistica.PROJECTILE);
                    //knock them back along that ballistica
                    WandOfBlastWave.throwChar(ch, trajectory, 2, false, true, this);
                }
            }
        }
        Dungeon.hero.interrupt();
        super.die(cause);
    }

    public void sayDefeated(){
        yell( Messages.get( this, "defeated_by_enemy"));
        Sample.INSTANCE.play( Assets.Sounds.GHOST );
    }

    @Override
    public String actionName() {
        return "";
    }

    @Override
    public int actionIcon() {
        return HeroIcon.CONSTRUCTOR;
    }

    @Override
    public Visual primaryVisual() {
        Image ico;
        ico = new HeroIcon(this);
        return ico;
    }

    @Override
    public int indicatorColor() {
        return 0xFF00BB;
    }

    @Override
    public void doAction() {
        GameScene.selectCell(constructDirector);

        ActionIndicator.setAction(this);
    }

    public CellSelector.Listener constructDirector = new CellSelector.Listener(){

        @Override
        public void onSelect(Integer cell) {
            if (cell == null) return;

            Sample.INSTANCE.play( Assets.Sounds.GHOST );

            directTocell(cell);

        }

        @Override
        public String prompt() {
            return  "\"" + Messages.get(ConstructHero.class, "direct_prompt") + "\"";
        }
    };

    private static final String HEALTH_REGEN_TAG = "health_regen";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(HEALTH_REGEN_TAG, healthRegen);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        healthRegen = bundle.getFloat(HEALTH_REGEN_TAG);
        setupActionIndicator();
    }
}
