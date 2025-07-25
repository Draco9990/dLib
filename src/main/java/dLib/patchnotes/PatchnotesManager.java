package dLib.patchnotes;

import dLib.betterscreens.ui.elements.items.PanelListScreen;
import dLib.patchnotes.ui.PatchnotesScreen;
import dLib.util.events.serializableevents.SerializableRunnable;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

public class PatchnotesManager {
    private static HashMap<String, Pair<Supplier<PanelListScreen.Panel>, Patchnotes>> customPatchnotes = new HashMap<>();

    public static void registerCustomPatchnotes(String modId, Supplier<PanelListScreen.Panel> panel, Patchnotes patchnotes){
        if(customPatchnotes.containsKey(modId)){
            throw new IllegalArgumentException("Patchnotes for " + modId + " are already registered.");
        }

        customPatchnotes.put(modId, new Pair<>(panel, patchnotes));
    }

    public static ArrayList<PanelListScreen.Panel> generatePanels(){
        ArrayList<PanelListScreen.Panel> panels = new ArrayList<>();
        for(Pair<Supplier<PanelListScreen.Panel>, Patchnotes> entry : customPatchnotes.values()){
            PanelListScreen.Panel panel = entry.getKey().get();
            panel.postLeftClickEvent.subscribe(panel, (SerializableRunnable) () -> {
                PatchnotesScreen patchnotesScreen = new PatchnotesScreen(entry.getValue());
                patchnotesScreen.open();

                panel.getTopParent().close();
            });
            panels.add(panel);
        }
        return panels;
    }
}
