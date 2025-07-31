package dLib.modversions;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import dLib.files.JsonDataFileManager;
import dLib.util.Version;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ModVersionManager {
    private static HashMap<String, ArrayList<Runnable>> onInitialRunActions = new HashMap<>();
    private static HashMap<String, HashMap<Version, ArrayList<Runnable>>> onVersionUpdateActions = new HashMap<>();

    public static void init(){
        JsonDataFileManager.registerFileRules(ModVersionTracker.class, ModVersionTracker.makeRules());
    }

    public static void postInit(){
        ModVersionTracker tracker = JsonDataFileManager.load(ModVersionTracker.class);
        for (ModInfo mod : Loader.MODINFOS){
            if(!onInitialRunActions.containsKey(mod.ID) && !onVersionUpdateActions.containsKey(mod.ID)){
                continue;
            }

            Version existingVersion = tracker.getModVersion(mod.ID);
            Version newVersion = new Version(mod.ModVersion);
            if(existingVersion != null){
                modVersionComparison(mod.ID, existingVersion, newVersion);
            }
            else{
                if(onInitialRunActions.containsKey(mod.ID)){
                    for (Runnable action : onInitialRunActions.get(mod.ID)) {
                        action.run();
                    }
                }
            }

            tracker.setModVersion(mod.ID, newVersion);
        }
        tracker.save();
    }

    private static void modVersionComparison(String mod, Version oldVersion, Version newVersion){
        if(oldVersion.isSameAs(newVersion) || oldVersion.isNewerThan(newVersion)) {
            return; // No action needed, versions are equal or new version is older
        }

        if(!onVersionUpdateActions.containsKey(mod)){
            return;
        }

        List<Version> sorted = onVersionUpdateActions.get(mod)
                .keySet()
                .stream()
                .sorted()
                .collect(Collectors.toList());
        for (Version version : sorted) {
            if (version.isNewerThan(oldVersion) && (version.isOlderThan(newVersion) || version.isSameAs(newVersion))) {
                for (Runnable action : onVersionUpdateActions.get(mod).get(version)) {
                    action.run();
                }
            }
        }
    }

    public static void registerOnInitialRun(String modId, Runnable action){
        if(!onInitialRunActions.containsKey(modId)){
            onInitialRunActions.put(modId, new ArrayList<>());
        }

        onInitialRunActions.get(modId).add(action);
    }

    public static void registerOnVersionUpdate(String modId, Version version, Runnable action){
        if(!onVersionUpdateActions.containsKey(modId)){
            onVersionUpdateActions.put(modId, new HashMap<>());
        }

        if(!onVersionUpdateActions.get(modId).containsKey(version)){
            onVersionUpdateActions.get(modId).put(version, new ArrayList<>());
        }

        onVersionUpdateActions.get(modId).get(version).add(action);
    }
}
