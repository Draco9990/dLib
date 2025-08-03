package dLib.gameplay;

import dLib.gameplay.templates.TRoomData;
import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.events.localevents.TriConsumerEvent;

import java.io.Serializable;
import java.util.HashMap;

public class RoomData extends TRoomData<RoomPhaseData> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    //endregion Variables

    //region Constructors

    public RoomData(int infinityDepth, String act, int x, int y) {
        super(infinityDepth, act, x, y);
    }

    //endregion Constructors

    //region Methods

    //region Static Getters

    public static RoomData getCurrent() {
        return getFor(SpireLocation.getCurrent());
    }

    public static RoomData getFor(SpireLocation location){
        return getFor(location.infinityCounter, location.act, location.x, location.y);
    }
    public static RoomData getFor(int infinityDepth, String actName, int x, int y){
        return DungeonData.getFor(infinityDepth, actName).getRoom(x, y);
    }

    //endregion Static Getters

    @Override
    public RoomPhaseData makeRoomPhaseData(int forPhase) {
        return new RoomPhaseData(infinityDepth, act, x, y, forPhase);
    }

    //endregion Methods
}
