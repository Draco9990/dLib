package dLib.modversions;

import dLib.files.JsonDataFile;
import dLib.files.JsonStorageFileRules;
import dLib.util.Version;

import java.util.HashMap;

public class ModVersionTracker extends JsonDataFile {
    private HashMap<String, Version> lastRanModVersions = new HashMap<>();

    public void setModVersion(String modId, Version version) {
        lastRanModVersions.put(modId, version);
    }
    public Version getModVersion(String modId) {
        if (!lastRanModVersions.containsKey(modId)) {
            return null;
        }
        return lastRanModVersions.get(modId);
    }

    public static JsonStorageFileRules<ModVersionTracker> makeRules(){
        JsonStorageFileRules<ModVersionTracker> rules = new JsonStorageFileRules<>();

        rules.saveSteamCloud = false;
        rules.saveLocal = true;

        rules.localRelativeDirPath = "dLib";
        rules.fileName = "versionInfo";
        rules.perSave = false;

        rules.makeNew = ModVersionTracker::new;

        return rules;
    }
}
