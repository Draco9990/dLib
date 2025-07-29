package dLib.gameplay;

import java.io.Serializable;
import java.util.HashMap;

public class RoomData implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    public HashMap<Integer, RoomPhaseData> phases = new HashMap<>();

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

    public RoomPhaseData getRoomPhase(int phase) {
        return phases.computeIfAbsent(phase, k -> new RoomPhaseData());
    }

    public void cleanForSave(){
        phases.values().forEach(RoomPhaseData::cleanForSave);
    }

    //endregion Methods
}
