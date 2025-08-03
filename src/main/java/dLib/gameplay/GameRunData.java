package dLib.gameplay;

import dLib.gameplay.templates.TGameRunData;
import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.events.localevents.TriConsumerEvent;

import java.io.Serializable;
import java.rmi.AccessException;
import java.util.HashMap;

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
    public DungeonCycleData makeDungeonCycleData(int forInfinityDepth) {
        return new DungeonCycleData(forInfinityDepth);
    }

    //endregion Static Getters

    //endregion Methods
}
