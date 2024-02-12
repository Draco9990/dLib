package dLib.developermode.ui.screens;

import dLib.DLib;
import dLib.ui.screens.AbstractScreen;

public class DeveloperModeLaunchScreen extends AbstractScreen {
    public DeveloperModeLaunchScreen(){
        addGenericBackground();
    }


    /** ID */
    @Override
    public String getModId() {
        return DLib.getModID();
    }
}
