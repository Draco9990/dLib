package dLib.gameplay.templates;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class TGameRunData<TDungeonCycleDef extends TDungeonCycleData> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    public HashMap<Integer, TDungeonCycleDef> dungeonCycles = new HashMap<>();

    public ArrayList<ObtainKeyEffect.KeyColor> collectedKeys = new ArrayList<>();

    //endregion Variables

    //region Methods

    public abstract TDungeonCycleDef makeDungeonCycleData(int forInfinityDepth);

    public TDungeonCycleDef getDungeonCycle(int infinityDepth) {
        return dungeonCycles.computeIfAbsent(infinityDepth, k -> makeDungeonCycleData(infinityDepth));
    }

    public void load(){
        for (ObtainKeyEffect.KeyColor keyColor : collectedKeys){
            if(keyColor == ObtainKeyEffect.KeyColor.RED) Settings.hasRubyKey = true;
            if(keyColor == ObtainKeyEffect.KeyColor.GREEN) Settings.hasEmeraldKey = true;
            if(keyColor == ObtainKeyEffect.KeyColor.BLUE) Settings.hasSapphireKey = true;
        }
    }

    public void cleanForSave(){
        dungeonCycles.values().forEach(TDungeonCycleData::cleanForSave);
    }

    //endregion Methods
}
