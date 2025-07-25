package dLib.patchnotes;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.screens.mainMenu.MenuButton;
import com.megacrit.cardcrawl.screens.mainMenu.MenuPanelScreen;
import dLib.DLib;
import dLib.betterscreens.ui.elements.items.PanelListScreen;

public class PatchnotesPatches {
    public static void init(){
        DLib.registerMainMenuButtonActionOverride(
                MenuButton.ClickResult.PATCH_NOTES,
                () -> {
                    PanelListScreen newPanelScreen = new PanelListScreen();
                    newPanelScreen.panelBox.setChildren(PatchnotesManager.generatePanels());
                    newPanelScreen.open();
                },
                () -> true);
    }
}
