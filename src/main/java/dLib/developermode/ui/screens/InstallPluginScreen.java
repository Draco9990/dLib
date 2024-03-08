package dLib.developermode.ui.screens;

import dLib.DLib;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.screens.AbstractScreen;

public class InstallPluginScreen extends AbstractScreen {
    /** Constructors */
    public InstallPluginScreen(){
        addGenericBackground();

        addChildNCS(new TextBox("This feature requires IntelliJ Idea and the 'IntelliJ For StS' plugin to be installed in order to continue.", 240, 1080-624, 1400, 432).setWrap(true));
    }

    /** ID */
    @Override
    public String getModId() {
        return DLib.getModID();
    }
}
