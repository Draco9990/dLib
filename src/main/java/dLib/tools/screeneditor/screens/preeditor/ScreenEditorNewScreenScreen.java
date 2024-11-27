package dLib.tools.screeneditor.screens.preeditor;

import dLib.plugin.intellij.PluginManager;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Image;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.screens.AbstractScreen_DEPRECATED;
import dLib.ui.themes.UIThemeManager;
import dLib.util.IntegerVector2;

public class ScreenEditorNewScreenScreen extends UIElement {
    /** Variables */
    private Inputfield inputfield;

    public ScreenEditorNewScreenScreen(){
        super(0, 0, 1920, 1080);

        addChildNCS(new Image(UIThemeManager.getDefaultTheme().background, 0, 0, getWidth(), getHeight()));

        addChildNCS(new TextBox("Enter screen name:", 539, 1080-572, 798, 105));

        Inputfield.InputfieldData test = new Inputfield.InputfieldData();
        test.dimensions.setValue(new IntegerVector2(800, 121));
        inputfield = new Inputfield(test);
        //inputfield = new Inputfield("", 538, 1080-713, 800, 121);
        inputfield.setLocalPosition(538, 1080-713);
        //inputfield.setDimensions(800, 121);
        addChildCS(inputfield);

        TextButton proceedButton = new TextButton("CREATE", 683, 1080-896, 532, 101);
        proceedButton.getButton().addOnLeftClickConsumer(new Runnable() {
            @Override
            public void run() {
                PluginManager.sendMessage("createNewScreen", inputfield.getTextBox().getText());
            }
        });
        addChildCS(proceedButton);
    }
}
