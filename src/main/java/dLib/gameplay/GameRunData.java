package dLib.gameplay;

import dLib.gameplay.templates.TGameRunData;

import java.io.Serializable;
import java.rmi.AccessException;

public class GameRunData extends TGameRunData<DungeonCycleData> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    private static GameRunData current;

    //endregion Variables

    //region Methods

    //region Static Getters

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> GameRunData getCurrent() throws T {
        if(current == null){
            throw (T) new AccessException("No game run data is currently active. Ensure you're calling getCurrent() during a valid game run context.");
        }

        return current;
    }

    @Override
    public DungeonCycleData makeDungeonCycleData() {
        return new DungeonCycleData();
    }

    //endregion Static Getters

    //endregion Methods
}
