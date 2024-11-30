package dLib.util;

import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.vdurmont.semver4j.Semver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MinimalModInfo implements Serializable {
    static final long serialVersionUID = 1L;

    public String id;
    public Version version;

    public MinimalModInfo(ModInfo modInfo){
        this.id = modInfo.ID;
        this.version = new Version(modInfo.ModVersion);
    }

    public static List<MinimalModInfo> fromModInfos(List<ModInfo> modInfos){
        List<MinimalModInfo> toReturn = new ArrayList<>();
        for(ModInfo i : modInfos){
            toReturn.add(new MinimalModInfo(i));
        }

        return toReturn;
    }
}
