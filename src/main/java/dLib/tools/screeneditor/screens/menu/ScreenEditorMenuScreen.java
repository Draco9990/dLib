package dLib.tools.screeneditor.screens.menu;

import com.badlogic.gdx.graphics.Color;
import dLib.DLib;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.screens.AbstractScreen;
import dLib.ui.screens.ScreenManager;
import dLib.ui.themes.UITheme;

public class ScreenEditorMenuScreen extends AbstractScreen {
    /** Variables */

    /** Constructors */
    public ScreenEditorMenuScreen(){
        addChildNCS(new Renderable(UITheme.whitePixel, 10, 1080 - 218, 1489, 209).setRenderColor(Color.valueOf("#242424FF")));

        registerFileControls();
        registerToolbarControls();
    }

    public void registerFileControls(){
        TextButton closeButton = new TextButton("Close", 20, 1080-60, 200, 40);
        closeButton.getButton().addOnLeftClickConsumer(() -> {
            ScreenManager.closeScreen();
            ScreenEditorBaseScreen.instance = null;
        });
        addChildNCS(closeButton);

        TextButton saveButton = new TextButton("Save", 20, 1080-110, 200, 40);
        saveButton.getButton().addOnLeftClickConsumer(() -> {ScreenEditorBaseScreen.instance.getSaveManager().save();});
        addChildNCS(saveButton);
    }

    public void registerToolbarControls(){
        TextButton toolbarButton = new TextButton("Toolbar", 265, 1080-60, 200, 40);
        toolbarButton.getButton().addOnLeftClickConsumer(() -> {
            ScreenEditorBaseScreen.instance.hideAllToolbarItems();
            ScreenEditorBaseScreen.instance.getToolbarScreen().show();
        });
        addChildNCS(toolbarButton);

        TextButton propertiesButton = new TextButton("Properties", 265, 1080-110, 200, 40);
        propertiesButton.getButton().addOnLeftClickConsumer(() -> {
            ScreenEditorBaseScreen.instance.hideAllToolbarItems();
            ScreenEditorBaseScreen.instance.getPropertiesScreen().show();
        });
        addChildNCS(propertiesButton);

        TextButton elementsButton = new TextButton("Elements", 265, 1080-160, 200, 40);
        elementsButton.getButton().addOnLeftClickConsumer(() -> {
            ScreenEditorBaseScreen.instance.hideAllToolbarItems();
            ScreenEditorBaseScreen.instance.getElementListScreen().show();
        });
        addChildNCS(elementsButton);
    }

    /** ModID */
    @Override
    public String getModId() {
        return DLib.getModID();
    }
}
