package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.fox;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.SR.RBASR;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RBAOtogiSprite;
import com.watabou.utils.Random;

public class RBAOtogi extends RBAFox {

	{
		spriteClass = RBAOtogiSprite.class; //오토기 스프라이트 사용
		loot = new RBASR();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 2 + level, 3 + 3*level); //높은 공격력
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, level/3); //낮은 방어력
	}

	@Override
	public int attackSkill( Char target ) {
		return 2 + level*2; //높은 정확성
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		return !Dungeon.level.adjacent( pos, enemy.pos )
				&& (super.canAttack(enemy) || new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos ); //원거리에서만 공격 가능. 투사체가 닿을 수 있는 곳에만 공격 가능
	}

	@Override
	protected boolean getCloser( int target ) { //가까이 가면 거리를 벌림
		if (state == HUNTING) {
			return enemySeen && getFurther( target );
		} else {
			return super.getCloser( target );
		}
	}

	public void adjustStats( int level ) {
		HP = HT = (1 + level) * 3; //낮은 체력
		defenseSkill = 2 + level / 2;
	}

	@Override
	public RBAFox findPartner() {
		if (yukinoID != -1) return (RBAFox) Actor.findById(yukinoID);
		if (nikoID != -1) return (RBAFox) Actor.findById(nikoID);
		if (kurumiID != -1) return (RBAFox) Actor.findById(kurumiID);
		return null;
	}
	
}
