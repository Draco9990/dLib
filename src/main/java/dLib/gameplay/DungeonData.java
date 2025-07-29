package dLib.gameplay;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;
import java.util.HashMap;

public class DungeonData implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    public HashMap<Vector2, RoomData> rooms = new HashMap<>();

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

    public RoomData getRoom(int x, int y) {
        return rooms.computeIfAbsent(new Vector2(x, y), k -> new RoomData());
    }

    public void cleanForSave(){
        rooms.values().forEach(RoomData::cleanForSave);
    }

    //endregion Methods
}
