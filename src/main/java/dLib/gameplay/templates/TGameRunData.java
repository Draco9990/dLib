package dLib.gameplay.templates;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;
import dLib.gameplay.GameRunData;
import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.events.localevents.TriConsumerEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class TGameRunData<TDungeonCycleDef extends TDungeonCycleData> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    public LinkedHashMap<Integer, TDungeonCycleDef> dungeonCycles = new LinkedHashMap<>();

    public ArrayList<ObtainKeyEffect.KeyColor> collectedKeys = new ArrayList<>();

    protected HashMap<String, Serializable> metadata = new HashMap<>();
    public BiConsumerEvent<String, Serializable> postMetadataChangedEvent = new BiConsumerEvent<>();                    public TriConsumerEvent<TGameRunData, String, Serializable> postMetadataChangedGlobalEvent = new TriConsumerEvent<>();

    //endregion Variables

    //region Methods

    public abstract TDungeonCycleDef makeDungeonCycleData(int forInfinityDepth);

    public TDungeonCycleDef getDungeonCycle(int infinityDepth) {
        return dungeonCycles.computeIfAbsent(infinityDepth, k -> makeDungeonCycleData(infinityDepth));
    }

    public void load(){
        for (ObtainKeyEffect.KeyColor keyColor : collectedKeys){
            if(keyColor == ObtainKeyEffect.KeyColor.RED) Settings.hasRubyKey = true;
            if(keyColor == ObtainKeyEffect.KeyColor.GREEN) Settings.hasEmeraldKey = true;
            if(keyColor == ObtainKeyEffect.KeyColor.BLUE) Settings.hasSapphireKey = true;
        }
    }

    public void cleanForSave(){
        dungeonCycles.values().forEach(TDungeonCycleData::cleanForSave);
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
