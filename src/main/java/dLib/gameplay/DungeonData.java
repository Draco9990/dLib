package dLib.gameplay;

import dLib.gameplay.templates.TDungeonData;
import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.events.localevents.TriConsumerEvent;

import java.io.Serializable;
import java.util.HashMap;

public class DungeonData extends TDungeonData<RoomData> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    //endregion Variables

    //region Constructors

    public DungeonData(int infinityDepth, String act) {
        super(infinityDepth, act);
    }

    //endregion Constructors

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
    public RoomData makeRoomData(int forX, int forY) {
        return new RoomData(infinityDepth, act, forX, forY);
    }

    //endregion Methods
}
