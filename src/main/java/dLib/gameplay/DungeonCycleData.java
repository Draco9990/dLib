package dLib.gameplay;

import java.io.Serializable;
import java.util.HashMap;

public class DungeonCycleData implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    public HashMap<String, DungeonData> dungeons = new HashMap<>();

    //endregion Variables

    //region Methods

    //region Static Getters

    public static DungeonCycleData getCurrent() {
        return getFor(SpireLocation.getCurrent());
    }

    public static DungeonCycleData getFor(SpireLocation location){
        return getFor(location.infinityCounter);
    }
    public static DungeonCycleData getFor(int infinityDepth){
        return GameRunData.getCurrent().getDungeonCycle(infinityDepth);
    }

    //endregion Static Getters

    public DungeonData getDungeon(String actName){
        return dungeons.computeIfAbsent(actName, k -> new DungeonData());
    }

    public void cleanForSave(){
        dungeons.values().forEach(DungeonData::cleanForSave);
    }

    //endregion Methods
}
