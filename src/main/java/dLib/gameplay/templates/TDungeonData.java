package dLib.gameplay.templates;

import com.badlogic.gdx.math.Vector2;
import dLib.gameplay.RoomData;

import java.io.Serializable;
import java.util.HashMap;

public abstract class TDungeonData<TRoomDataDef extends TRoomData> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    public HashMap<Vector2, TRoomDataDef> rooms = new HashMap<>();

    //endregion Variables

    //region Methods

    public abstract TRoomDataDef makeRoomData();

    public TRoomDataDef getRoom(int x, int y) {
        return rooms.computeIfAbsent(new Vector2(x, y), k -> makeRoomData());
    }

    public void cleanForSave(){
        rooms.values().forEach(TRoomData::cleanForSave);
    }

    //endregion Methods
}
