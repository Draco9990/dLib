package dLib.gameplay;

import java.io.Serializable;

public class RoomPhaseData implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    //endregion Variables

    //region Methods

    //region Static Getters

    public static RoomPhaseData getCurrent() {
        return getFor(SpireLocation.getCurrent());
    }

    public static RoomPhaseData getFor(SpireLocation location){
        return getFor(location.infinityCounter, location.act, location.x, location.y, location.phase);
    }
    public static RoomPhaseData getFor(int infinityDepth, String actName, int x, int y, int phase){
        return RoomData.getFor(infinityDepth, actName, x, y).getRoomPhase(phase);
    }

    //endregion Static Getters

    public void cleanForSave(){

    }

    //endregion Methods
}
