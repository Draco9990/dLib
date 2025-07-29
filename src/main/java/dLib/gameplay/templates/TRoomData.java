package dLib.gameplay.templates;

import dLib.gameplay.RoomPhaseData;

import java.io.Serializable;
import java.util.HashMap;

public abstract class TRoomData<TRoomPhaseDataDef extends TRoomPhaseData> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    public HashMap<Integer, TRoomPhaseDataDef> phases = new HashMap<>();

    //endregion Variables

    //region Methods

    public abstract TRoomPhaseDataDef makeRoomPhaseData();

    public TRoomPhaseDataDef getRoomPhase(int phase) {
        return phases.computeIfAbsent(phase, k -> makeRoomPhaseData());
    }

    public void cleanForSave(){
        phases.values().forEach(TRoomPhaseData::cleanForSave);
    }

    //endregion Methods
}
