package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.MiasmaGas;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class DeferredDeath extends FlavourBuff {

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.DARKENED);
        else if (target.invisible == 0) target.sprite.remove(CharSprite.State.DARKENED);
    }

    @Override
    public int icon() {
        return BuffIndicator.CORRUPT;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }

    @Override
    public boolean act() {
        return super.act();
    }

    public void recover(float amount) {
        spend(-amount);
        if (cooldown() <= 0){
            detach();
        }
    }
    @Override
    public void detach() {
        super.detach();
        if (target.properties().contains(Char.Property.BOSS)) {
            target.damage(target.HP, new Grim());
        } else {
            target.die(this);
        }
        GameScene.add(Blob.seed(target.pos, 100, MiasmaGas.class));
        CellEmitter.get(target.pos).burst(ShadowParticle.UP, 20);
    }
}