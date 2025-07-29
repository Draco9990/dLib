package dLib.gameplay.templates;

import dLib.gameplay.SpireLocation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class TRoomData<TRoomPhaseDataDef extends TRoomPhaseData> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    public final int infinityDepth;
    public final String act;
    public final int x;
    public final int y;

    public LinkedHashMap<Integer, TRoomPhaseDataDef> phases = new LinkedHashMap<>();

    //endregion Variables

    //region Constructors

    public TRoomData(int infinityDepth, String act, int x, int y){
        this.infinityDepth = infinityDepth;
        this.act = act;
        this.x = x;
        this.y = y;
    }

    //endregion Constructors

    //region Methods

    public abstract TRoomPhaseDataDef makeRoomPhaseData(int forPhase);

    public TRoomPhaseDataDef getRoomPhase(int phase) {
        return phases.computeIfAbsent(phase, k -> makeRoomPhaseData(phase));
    }

    public SpireLocation getPartialLocation(){
        return SpireLocation.getPartial(this);
    }

    public void cleanForSave(){
        phases.values().forEach(TRoomPhaseData::cleanForSave);
    }

    //endregion Methods
}
