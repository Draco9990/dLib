package dLib.developermode;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.screens.mainMenu.MenuButton;
import dLib.DLib;
import dLib.external.ExternalEditorCommunicationManager;
import dLib.debug.TestScreen;
import dLib.tools.uicreator.UCEditor;
import dLib.tools.uicreator.UCStartupPopup;
import dLib.util.DLibConfigManager;
import dLib.util.bindings.string.Str;

import java.util.ArrayList;

public class DeveloperModeManager {
    public static void init(){
        DLib.registerCustomMainMenuButton(
                Enums.DEVELOPER,
                new ArrayList<MenuButton.ClickResult>() {{add(MenuButton.ClickResult.SETTINGS);}},
                Str.stat("Developer"),
                () -> {
                    if (!ExternalEditorCommunicationManager.isEnabled()) ExternalEditorCommunicationManager.enable();
                    if (!ExternalEditorCommunicationManager.isRunning()) ExternalEditorCommunicationManager.start();

                    if (false && ExternalEditorCommunicationManager.isRunning()) {
                        UCStartupPopup popup = new UCStartupPopup();
                        popup.open();
                    } else if (false) {
                        UCEditor editor = new UCEditor();
                        editor.open();
                    } else {
                        TestScreen testScreen = new TestScreen();
                        testScreen.open();
                    }
                },
                () -> DLibConfigManager.get().getBool(DLibConfigManager.DEV_MODE));
    }

    public static class Enums {
        @SpireEnum
        public static MenuButton.ClickResult DEVELOPER;
    }
}
