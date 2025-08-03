package dLib.gameplay.templates;

import dLib.gameplay.RoomData;
import dLib.gameplay.SpireLocation;
import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.events.localevents.TriConsumerEvent;

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

    protected HashMap<String, Serializable> metadata = new HashMap<>();
    public BiConsumerEvent<String, Serializable> postMetadataChangedEvent = new BiConsumerEvent<>();                    public TriConsumerEvent<TRoomData, String, Serializable> postMetadataChangedGlobalEvent = new TriConsumerEvent<>();

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

    //region Metadata

    public void updateMetadata(String key, Serializable value){
        metadata.put(key, value);
        postMetadataChangedEvent.invoke(key, value);
        postMetadataChangedGlobalEvent.invoke(this, key, value);
    }

    public void removeMetadata(String key){
        if(metadata.containsKey(key)){
            metadata.remove(key);
            postMetadataChangedEvent.invoke(key, null);
            postMetadataChangedGlobalEvent.invoke(this, key, null);
        }
    }

    //endregion Metadata

    //endregion Methods
}
