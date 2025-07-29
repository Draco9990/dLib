package dLib.gameplay.templates;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;
import dLib.gameplay.DungeonCycleData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class TGameRunData<TDungeonCycleDef extends DungeonCycleData> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    public HashMap<Integer, TDungeonCycleDef> dungeonCycles = new HashMap<>();

    public ArrayList<ObtainKeyEffect.KeyColor> collectedKeys = new ArrayList<>();

    //endregion Variables

    //region Methods

    public abstract TDungeonCycleDef makeDungeonCycleData();

    public TDungeonCycleDef getDungeonCycle(int infinityDepth) {
        return dungeonCycles.computeIfAbsent(infinityDepth, k -> makeDungeonCycleData());
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
}
