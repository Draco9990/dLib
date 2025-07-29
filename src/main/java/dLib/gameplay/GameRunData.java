package dLib.gameplay;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;

import java.io.Serializable;
import java.rmi.AccessException;
import java.util.ArrayList;
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
    public DungeonCycleData makeDungeonCycleData() {
        return new DungeonCycleData();
    }

    //endregion Static Getters

    //endregion Methods
}
