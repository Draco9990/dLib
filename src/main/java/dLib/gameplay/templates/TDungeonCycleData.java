package dLib.gameplay.templates;

import dLib.gameplay.DungeonData;

import java.io.Serializable;
import java.util.HashMap;

public abstract class TDungeonCycleData<TDungeonDataDef extends TDungeonData> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    public HashMap<String, TDungeonDataDef> dungeons = new HashMap<>();

    //endregion Variables

    //region Methods

    public abstract TDungeonDataDef makeDungeonData();

    public TDungeonDataDef getDungeon(String actName){
        return dungeons.computeIfAbsent(actName, k -> makeDungeonData());
    }

    public void cleanForSave(){
        dungeons.values().forEach(TDungeonData::cleanForSave);
    }

    //endregion Methods
}
