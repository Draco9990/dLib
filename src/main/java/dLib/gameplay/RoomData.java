package dLib.gameplay;

import dLib.gameplay.templates.TRoomData;

import java.io.Serializable;

public class RoomData extends TRoomData<RoomPhaseData> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    //endregion Variables

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
    public RoomPhaseData makeRoomPhaseData() {
        return new RoomPhaseData();
    }

    //endregion Methods
}
