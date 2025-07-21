package dLib.developermode;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.screens.mainMenu.MenuButton;
import dLib.DLib;
import dLib.external.ExternalEditorCommunicationManager;
import dLib.test.TestScreen;
import dLib.tools.uicreator.UCEditor;
import dLib.tools.uicreator.UCStartupPopup;
import dLib.util.DLibConfigManager;
import dLib.util.bindings.string.Str;

import java.util.function.Supplier;

public class DeveloperModeManager {
    public static void init(){
        DLib.registerCustomMainMenuButton(
                Enums.DEVELOPER,
                MenuButton.ClickResult.SETTINGS,
                Str.stat("DEVELOPER"),
                () -> {
                    if (!ExternalEditorCommunicationManager.isEnabled()) ExternalEditorCommunicationManager.enable();
                    if (!ExternalEditorCommunicationManager.isRunning()) ExternalEditorCommunicationManager.start();

                    if (false && ExternalEditorCommunicationManager.isRunning()) {
                        UCStartupPopup popup = new UCStartupPopup();
                        popup.open();
                    } else if (true) {
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
