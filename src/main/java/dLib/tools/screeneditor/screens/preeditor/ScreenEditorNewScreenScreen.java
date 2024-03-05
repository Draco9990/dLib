package dLib.tools.screeneditor.screens.preeditor;

import dLib.DLib;
import dLib.plugin.intellij.PluginManager;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.screens.AbstractScreen;

public class ScreenEditorNewScreenScreen extends AbstractScreen {
    /** Variables */
    private Inputfield inputfield;

    public ScreenEditorNewScreenScreen(){
        addGenericBackground();

        addElement(new TextBox("Enter screen name:", 539, 1080-572, 798, 105));

        inputfield = new Inputfield("", 538, 1080-713, 800, 121);
        addElement(inputfield);

        TextButton proceedButton = new TextButton("CREATE", 683, 1080-896, 532, 101);
        proceedButton.getButton().setOnLeftClickConsumer(new Runnable() {
            @Override
            public void run() {
                PluginManager.sendMessage("createNewScreen", inputfield.getTextBox().getText());
            }
        });
        addElement(proceedButton);
    }

    @Override
    public String getModId() {
        return DLib.getModID();
    }
}
