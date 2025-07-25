package dLib.patchnotes;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.betterscreens.ui.elements.items.PanelListScreen;
import dLib.patchnotes.ui.PatchnotesScreen;
import dLib.util.bindings.string.Str;
import dLib.util.bindings.texture.Tex;
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

        panels.add(new PanelListScreen.Panel(
                Str.stat("Base Game"),
                Tex.stat(ImageMaster.P_STANDARD),
                Str.stat("Patchnotes for base Slay the Spire."),
                () -> CardCrawlGame.mainMenuScreen.patchNotesScreen.open())
        );

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
