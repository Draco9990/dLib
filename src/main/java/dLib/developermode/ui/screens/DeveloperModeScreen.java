package dLib.developermode.ui.screens;

import dLib.DLib;
import dLib.ui.screens.AbstractScreen;

public class DeveloperModeScreen extends AbstractScreen {
    public DeveloperModeScreen(){
        addGenericBackground();
    }

    /** ID */
    @Override
    public String getModId() {
        return DLib.getModID();
    }
}
