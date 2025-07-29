package dLib.gameplay;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;
import java.util.HashMap;

public abstract class TDungeonData<TRoomDataDef extends RoomData> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    public HashMap<Vector2, TRoomDataDef> rooms = new HashMap<>();

    //endregion Variables

    //region Methods

    public abstract TRoomDataDef makeRoomData();

    public RoomData getRoom(int x, int y) {
        return rooms.computeIfAbsent(new Vector2(x, y), k -> makeRoomData());
    }

    public void cleanForSave(){
        rooms.values().forEach(RoomData::cleanForSave);
    }

    //endregion Methods
}
