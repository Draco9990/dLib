package dLib.gameplay.templates;

import dLib.gameplay.DungeonData;

import java.io.Serializable;
import java.util.HashMap;

public abstract class TDungeonCycleData<TDungeonDataDef extends DungeonData> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    public HashMap<String, TDungeonDataDef> dungeons = new HashMap<>();

    //endregion Variables

    //region Methods

    public abstract TDungeonDataDef makeDungeonData();

    public DungeonData getDungeon(String actName){
        return dungeons.computeIfAbsent(actName, k -> makeDungeonData());
    }

    public void cleanForSave(){
        dungeons.values().forEach(DungeonData::cleanForSave);
    }

    //endregion Methods
}
