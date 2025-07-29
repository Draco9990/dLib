package dLib.gameplay;

import dLib.gameplay.templates.TDungeonCycleData;

import java.io.Serializable;

public class DungeonCycleData extends TDungeonCycleData<DungeonData> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

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

    @Override
    public DungeonData makeDungeonData() {
        return new DungeonData();
    }

    //endregion Methods
}
