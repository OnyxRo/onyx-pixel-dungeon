package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.LicteriaLasher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.ObeliskBloodstone;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.ObeliskNecrotic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.ObeliskPermafrost;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ObeliskNecroticSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class AbObelisk extends HeroSpellTargeted{
    enum ObEnergy{
        BLOOD,
        COLD,
        DECAY
    }

    {
        image = ItemSpriteSheet.HEROSPELL_OBELISK;


        cellCaster = new CellSelector.Listener() {
            @Override
            public void onSelect(Integer cell) {
                if (cell != null) {
                    if (Char.findChar(cell) == null && Dungeon.level.passable[cell] && !Dungeon.level.pit[cell] && Dungeon.hero.fieldOfView[cell]) {

                        Mob obelisk = energy == ObEnergy.BLOOD ? new ObeliskBloodstone() : energy == ObEnergy.COLD ? new ObeliskPermafrost() : new ObeliskNecrotic();
                        obelisk.alignment = Char.Alignment.ALLY;
                        obelisk.pos = cell;
                        GameScene.add(obelisk);
                        Sample.INSTANCE.play(Assets.Sounds.SHATTER);
                        CellEmitter.floor(cell).burst(ShadowParticle.CURSE, 10);
                        CellEmitter.floor(cell).start(ShadowParticle.CURSE, 0.2f, 10);
                        cooldown();
                        if (energy == ObEnergy.BLOOD) energy = Random.oneOf(ObEnergy.COLD, ObEnergy.DECAY);
                        else if (energy == ObEnergy.COLD) energy = Random.oneOf(ObEnergy.BLOOD, ObEnergy.DECAY);
                        else if (energy == ObEnergy.DECAY) energy = Random.oneOf(ObEnergy.COLD, ObEnergy.BLOOD);
                        updateQuickslot();
                    }
                }
            }

            @Override
            public String prompt () {
                return Messages.get(HeroSpellTargeted.class, "cell_choose");
            }
        };
    }

    private ObEnergy energy = ObEnergy.BLOOD;

    @Override
    public ItemSprite.Glowing glowing() {
        ItemSprite.Glowing glowing;
        switch (energy) {
            default: {
                return null;
            }
            case BLOOD: {
                glowing = new ItemSprite.Glowing(0xAA0000);
            } break;
            case COLD: {
                glowing = new ItemSprite.Glowing(0xAAAAFF);
            } break;
            case DECAY:{
                glowing = new ItemSprite.Glowing(0x00FF00);
            } break;
        }
        return glowing;
    }

    @Override
    public String info() {
        String finalinfo = super.info();
        switch (energy) {
            case BLOOD: {
                finalinfo+= "\n\n" + Messages.get(AbObelisk.class, "energy_blood");
            } break;
            case COLD: {
                finalinfo+= "\n\n" + Messages.get(AbObelisk.class, "energy_cold");
            } break;
            case DECAY:{
                finalinfo+= "\n\n" + Messages.get(AbObelisk.class, "energy_decay");
            } break;
        }
        return finalinfo;
    }

    private static final String ENERGY = "energy";

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(ENERGY, energy);
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        energy = bundle.getEnum(ENERGY, ObEnergy.class);
        super.restoreFromBundle(bundle);
    }

    @Override
    protected int castCooldown() {
        return 150;
    }
}
