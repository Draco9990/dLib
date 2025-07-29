package dLib.gameplay;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;
import java.util.HashMap;

public class DungeonData extends TDungeonData<RoomData> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    //endregion Variables

    //region Methods

    //region Static Getters

    public static DungeonData getCurrent() {
        return getFor(SpireLocation.getCurrent());
    }

    public static DungeonData getFor(SpireLocation location){
        return getFor(location.infinityCounter, location.act);
    }
    public static DungeonData getFor(int infinityDepth, String actName){
        return DungeonCycleData.getFor(infinityDepth).getDungeon(actName);
    }

    //endregion Static Getters

    @Override
    public RoomData makeRoomData() {
        return new RoomData();
    }

    //endregion Methods
}
