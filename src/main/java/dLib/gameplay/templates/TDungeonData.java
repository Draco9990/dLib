package dLib.gameplay.templates;

import com.badlogic.gdx.math.Vector2;
import dLib.gameplay.SpireLocation;

import java.io.Serializable;
import java.util.HashMap;

public abstract class TDungeonData<TRoomDataDef extends TRoomData> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    public final int infinityDepth;
    public final String act;

    public HashMap<Vector2, TRoomDataDef> rooms = new HashMap<>();

    //endregion Variables

    //region Constructors

    public TDungeonData(int infinityDepth, String act){
        this.infinityDepth = infinityDepth;
        this.act = act;
    }

    //endregion Constructors

    //region Methods

    public abstract TRoomDataDef makeRoomData(int forX, int forY);

    public TRoomDataDef getRoom(int x, int y) {
        return rooms.computeIfAbsent(new Vector2(x, y), k -> makeRoomData(x, y));
    }

    public SpireLocation getPartialLocation(){
        return SpireLocation.getPartial(this);
    }

    public void cleanForSave(){
        rooms.values().forEach(TRoomData::cleanForSave);
    }

    //endregion Methods
}
