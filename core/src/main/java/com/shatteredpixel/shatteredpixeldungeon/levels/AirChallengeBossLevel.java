package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Shinobi;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tengu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tengu2;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EmptyRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Group;

public class AirChallengeBossLevel extends Level {
    {
        color1 = 0x6a723d;
        color2 = 0x88924c;
    }

    private final int KEY_POS = 23 + width * 15;
    private final int ROOM_LEFT_POS = 9 + width * 19;
    private final int ROOM_RIGHT_POS = 39 + width * 19;

    public Tengu2 tengu;

    @Override
    public String waterTex() {
        return Assets.Sprites.WATER_PRISON;
    }

    @Override
    public String tilesTex() {
        return Assets.Sprites.TILES_PRISON;
    }

    @Override
    protected boolean build() {

        setSize(48, 48);

        map = Layouts.AIR_CHALLENGE_BOSS_START.clone();

        buildFlagMaps();
        cleanWalls();

        entrance = 23 + width * 6;
        exit = 0;

        return true;
    }

    @Override
    protected void createMobs() {
        Shinobi guard = (Shinobi) createMob();
        guard.pos = KEY_POS;
        mobs.add( guard );
        tengu = new Tengu2();
    }

    private boolean insideRoomLeft(int cell) {
        return (new EmptyRoom().set(4, 14, 12, 22)).inside(cellToPoint(cell));
    }

    private boolean insideRoomRight(int cell) {
        return (new EmptyRoom().set(33, 14, 43, 22)).inside(cellToPoint(cell));
    }

    @Override
    public void occupyCell( Char ch ) {
        super.occupyCell( ch );
        int cell = ch.pos;
        if (ch == Dungeon.hero) {
            //tengu = new Tengu2();
            //hero enters tengu's chamber
            if (insideRoomLeft(cell)) {
                tengu.pos = ROOM_LEFT_POS;
                GameScene.add(tengu);
                tengu.notice();
            } else if (insideRoomRight(cell)) {
                tengu.pos = ROOM_RIGHT_POS;
                GameScene.add(tengu);
                tengu.notice();
            }
        }
    }

    @Override
    protected void createItems() {
        drop(new IronKey(Dungeon.depth), KEY_POS);
    }

    @Override
    public Mob createMob() {
        return new Shinobi();
    }

    @Override
    public String tileName(int tile) {
        if (tile == Terrain.WATER) {
            return Messages.get(SewerLevel.class, "water_name");
        }
        return super.tileName(tile);
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.EMPTY_DECO:
                return Messages.get(SewerLevel.class, "empty_deco_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(SewerLevel.class, "bookshelf_desc");
            default:
                return super.tileDesc(tile);
        }
    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        SewerLevel.addSewerVisuals(this, visuals);
        return visuals;
    }
}
