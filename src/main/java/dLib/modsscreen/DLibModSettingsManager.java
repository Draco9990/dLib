package dLib.modsscreen;

import java.util.ArrayList;

public class DLibModSettingsManager {
    public static ArrayList<IDLibModSettingsContainer> modSettingsContainers = new ArrayList<>();

    public static void registerModSettingsContainer(IDLibModSettingsContainer container){
        modSettingsContainers.add(container);
    }
}
