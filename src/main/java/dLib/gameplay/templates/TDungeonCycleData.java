package dLib.gameplay.templates;

import dLib.gameplay.SpireLocation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class TDungeonCycleData<TDungeonDataDef extends TDungeonData> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    public final int infinityDepth;

    public LinkedHashMap<String, TDungeonDataDef> dungeons = new LinkedHashMap<>();

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

    //endregion Methods
}
