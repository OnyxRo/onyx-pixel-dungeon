package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Random;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class RingOfLuck extends Ring {
    public String statsInfo() {
        if (isIdentified()){
            return Messages.get(this, "stats", new DecimalFormat("#.##").format(100f * (0.125 * soloBonus())));
        } else {
            return Messages.get(this, "typical_stats", new DecimalFormat("#.##").format(30f));
        }
    }

    @Override
    protected RingBuff buff( ) {
        return new Luck();
    }

    public static float luckFactor( Char target ){
        float bonus = (float) Math.min(16, getBonus(target, RingOfLuck.Luck.class));
        return (float) (1 + 0.125 * bonus);
    }

    public class Luck extends RingBuff {
    }

    public static int rolls() {
        int baseRolls = (int) luckFactor(Dungeon.hero);
        int rolls = baseRolls;
        if (Random.Float() < (luckFactor(Dungeon.hero) - baseRolls)) {
            rolls++;
        }
        return rolls;
    }

    public static float distance(float number1, float number2) {
        return Math.abs(number1 - number2);
    }

    public static float closest(ArrayList<Float> numbers, float target) {
        float closest = -1;
        float closestDistance = Integer.MAX_VALUE;
        float closestIndex = 0;
        for (int i = 0; i < numbers.size(); i++) {
            if (distance(numbers.get(i), target) < closestDistance) {
                closestDistance = distance(numbers.get(i), target);
                closest = numbers.get(i);
                closestIndex = i;
            }
        }
        return closest;
    }

    public static int closest(ArrayList<Integer> numbers, int target) {
        ArrayList<Float> floats = new ArrayList<>();
        for (int i = 0; i < numbers.size(); i++) {
            floats.add((float)numbers.get(i));
        }
        return Math.round(closest(floats, target));
    }

    //Placeholders that roll multiple times and calculate best result. These all override watabou.utils.Random functions.
    public static float randomFloat(float target) {
        ArrayList<Float> numbers = new ArrayList<>();
        for (int i = 0; i < RingOfLuck.rolls(); i++) {
            numbers.add(Random.Float());
        }
        return closest(numbers, target);
    }

    //returns a uniformly distributed float in the range [0, max)
    public static float randomFloat(float max, float target ) {
        ArrayList<Float> numbers = new ArrayList<>();
        for (int i = 0; i < RingOfLuck.rolls(); i++) {
            numbers.add(Random.Float(max));
        }
        return closest(numbers, target);
    }

    //returns a uniformly distributed float in the range [min, max)
    public static float randomFloat( float min, float max, float target ) {
        ArrayList<Float> numbers = new ArrayList<>();
        for (int i = 0; i < RingOfLuck.rolls(); i++) {
            numbers.add(Random.Float(min, max));
        }
        return closest(numbers, target);
    }

    //returns a triangularly distributed float in the range [min, max)
    public static float randomNormalFloat( float min, float max, float target ) {
        ArrayList<Float> numbers = new ArrayList<>();
        for (int i = 0; i < RingOfLuck.rolls(); i++) {
            numbers.add(Random.NormalFloat(min, max));
        }
        return closest(numbers, target);
    }

    //returns a uniformly distributed int in the range [0, max)
    public static int randomInt( int max, int target ) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < RingOfLuck.rolls(); i++) {
            numbers.add(Random.Int(max));
        }
        return closest(numbers, target);
    }

    //returns a uniformly distributed int in the range [min, max)
    public static int randomInt( int min, int max, int target ) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < RingOfLuck.rolls(); i++) {
            numbers.add(Random.Int(min, max));
        }
        return closest(numbers, target);
    }

    //returns a uniformly distributed int in the range [min, max]
    public static int randomIntRange( int min, int max, int target ) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < RingOfLuck.rolls(); i++) {
            numbers.add(Random.IntRange(min, max));
        }
        return closest(numbers, target);
    }

    //returns a triangularly distributed int in the range [min, max]
    public static int randomNormalIntRange( int min, int max, int target ) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < RingOfLuck.rolls(); i++) {
            numbers.add(Random.NormalIntRange(min, max));
        }
        return closest(numbers, target);
    }

    public static int randomChances( float[] chances, int target ) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < RingOfLuck.rolls(); i++) {
            numbers.add(Random.chances(chances));
        }
        return closest(numbers, target);
    }

}
