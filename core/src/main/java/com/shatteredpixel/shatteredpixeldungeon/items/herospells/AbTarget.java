package com.shatteredpixel.shatteredpixeldungeon.items.herospells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Battlecry;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PriorityTarget;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Speed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bandit;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BossDwarfKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BossNecromancer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BossRatKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BossTengu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM200;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DMW;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Gnoll;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goblin;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Guard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Snake;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tengu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Thief;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.towers.CampRat;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class AbTarget extends HeroSpellTargeted{
    {
        image = ItemSpriteSheet.HEROSPELL_FOCUS;

        cellCaster = new CellSelector.Listener() {
            @Override
            public void onSelect(Integer cell) {
                if (cell != null) {
                    if (Char.findChar(cell) != null) {
                        Char ch = Char.findChar(cell);
                        if (ch.alignment == Char.Alignment.ENEMY) {
                            Buff.affect(ch, PriorityTarget.class, 10);
                            if (ch instanceof Mob){
                                Mob chMob = (Mob)ch;
                                if (!(chMob.properties().contains(Char.Property.BOSS) || chMob.properties().contains(Char.Property.MINIBOSS))){
                                    if (chMob.targetingPreference == Mob.TargetingPreference.ONLY_AMULET) Buff.affect(chMob, Terror.class, 8);
                                    if (chMob.targetingPreference == Mob.TargetingPreference.NOT_HERO){
                                        chMob.targetingPreference = Mob.TargetingPreference.NONE;
                                        chMob.speak("&@?!", CharSprite.WARNING);
                                    }
                                    if (chMob instanceof Goblin || chMob instanceof Gnoll){
                                        Buff.affect(chMob, Terror.class, 2);
                                        chMob.speak("???", CharSprite.WARNING);
                                    }

                                    if (chMob instanceof Rat){
                                        Buff.affect(chMob, Terror.class, 3);
                                        chMob.speak("(0o0)", CharSprite.WARNING);
                                    }
                                    if (chMob instanceof Thief){
                                        chMob.speak("???", CharSprite.WARNING);
                                        Buff.affect(chMob, Vertigo.class, 3);
                                    }
                                    if (chMob instanceof CampRat){
                                        chMob.speak("?!", CharSprite.WARNING);
                                        Buff.affect(chMob, Terror.class, 6);
                                    }
                                    if (chMob instanceof BossTengu){
                                        chMob.speak("?!!!!", CharSprite.NEGATIVE);
                                        Buff.affect(chMob, Battlecry.class,5);
                                    }
                                    if (chMob instanceof BossRatKing){
                                        chMob.speak("?(0_o)?", CharSprite.WARNING);
                                        Buff.affect(chMob, Vertigo.class, 3);
                                    }
                                    if (chMob instanceof BossDwarfKing){
                                        int i = Random.Int(5);
                                        switch (i){
                                            case 0: default: chMob.speak("SILENCE, MORTAL", CharSprite.NEGATIVE);
                                            case 1: chMob.speak("YOU WILL DIE SOON, MORTAL", CharSprite.NEGATIVE);
                                            case 2: chMob.speak("YOU SCREAMS MEAN NOTHING", CharSprite.NEGATIVE);
                                            case 3: chMob.speak("I AM IMMORTAL!", CharSprite.NEGATIVE);
                                            case 4: chMob.speak("SILENCE!", CharSprite.NEGATIVE);
                                        }
                                    }
                                    if (chMob instanceof DMW || chMob instanceof DM200){
                                        chMob.speak("TARGET SCREAMS SUCCESSFULLY IGNORED", CharSprite.NEUTRAL);
                                    }
                                    if (chMob instanceof BossNecromancer){
                                        chMob.speak("WHATEVER", CharSprite.NEUTRAL);
                                    }
                                    if (chMob instanceof Guard){
                                        chMob.speak("HUH?", CharSprite.WARNING);
                                    }
                                }
                            }
                            Sample.INSTANCE.play(Assets.Sounds.CHALLENGE, 1f, 0.8f);
                            cooldown();
                        }
                    }
                }
            }

            @Override
            public String prompt () {
                return Messages.get(HeroSpellTargeted.class, "cell_choose");
            }
        };
    }



    @Override
    protected int castCooldown() {
        return 10;
    }
}
