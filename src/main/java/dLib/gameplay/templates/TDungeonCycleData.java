package dLib.gameplay.templates;

import dLib.gameplay.DungeonCycleData;
import dLib.gameplay.SpireLocation;
import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.events.localevents.TriConsumerEvent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class TDungeonCycleData<TDungeonDataDef extends TDungeonData> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    public final int infinityDepth;

    public LinkedHashMap<String, TDungeonDataDef> dungeons = new LinkedHashMap<>();

    protected HashMap<String, Serializable> metadata = new HashMap<>();
    public BiConsumerEvent<String, Serializable> postMetadataChangedEvent = new BiConsumerEvent<>();                    public TriConsumerEvent<TDungeonCycleData, String, Serializable> postMetadataChangedGlobalEvent = new TriConsumerEvent<>();

    //endregion Variables

    //region Constructors

    public TDungeonCycleData(int infinityDepth){
        this.infinityDepth = infinityDepth;
    }

    //endregion Constructors

    //region Methods

    public abstract TDungeonDataDef makeDungeonData(String forAct);

    public TDungeonDataDef getDungeon(String actName){
        return dungeons.computeIfAbsent(actName, k -> makeDungeonData(actName));
    }

    public SpireLocation getPartialLocation(){
        return SpireLocation.getPartial(this);
    }

    public void cleanForSave(){
        dungeons.values().forEach(TDungeonData::cleanForSave);
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
