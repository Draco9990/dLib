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

public class GameRunData implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    private static GameRunData current;

    public HashMap<Integer, DungeonCycleData> dungeonCycles = new HashMap<>();

    public ArrayList<ObtainKeyEffect.KeyColor> collectedKeys = new ArrayList<>();

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

    //endregion Static Getters

    public DungeonCycleData getDungeonCycle(int infinityDepth) {
        return dungeonCycles.computeIfAbsent(infinityDepth, k -> new DungeonCycleData());
    }

    public void load(){
        for (ObtainKeyEffect.KeyColor keyColor : collectedKeys){
            if(keyColor == ObtainKeyEffect.KeyColor.RED) Settings.hasRubyKey = true;
            if(keyColor == ObtainKeyEffect.KeyColor.GREEN) Settings.hasEmeraldKey = true;
            if(keyColor == ObtainKeyEffect.KeyColor.BLUE) Settings.hasSapphireKey = true;
        }
    }

    public void cleanForSave(){
        dungeonCycles.values().forEach(DungeonCycleData::cleanForSave);
    }

    //endregion Methods

    //region Patches

    @SpirePatch2(clz = CardCrawlGame.class, method = SpirePatch.CLASS)
    private static class GameRunDataField {
        public static SpireField<GameRunData> gameRunData = new SpireField<>(() -> null);
    }

    //endregion Patches
}
