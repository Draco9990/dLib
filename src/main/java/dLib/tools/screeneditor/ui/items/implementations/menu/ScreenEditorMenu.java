package dLib.tools.screeneditor.ui.items.implementations.menu;

import com.badlogic.gdx.graphics.Color;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.screens.ScreenManager;
import dLib.ui.themes.UITheme;

public class ScreenEditorMenu extends UIElement {
    //region Variables

    //endregion

    //region Constructors

    public ScreenEditorMenu(){
        super(10, 1080-220, 1490, 210);
        addChildNCS(new Renderable(UITheme.whitePixel, 0, 0, getWidth(), getHeight()).setRenderColor(Color.valueOf("#242424FF")));

        initializeFileControls();
        initializeToolbarControls();
    }

    public void initializeFileControls(){
        TextButton closeButton = new TextButton("Close", 10, height - 50, 200, 40);
        closeButton.getButton().addOnLeftClickConsumer(ScreenManager::closeScreen);
        addChildNCS(closeButton);

        TextButton saveButton = new TextButton("Save", 10, height - 100, 200, 40);
        saveButton.getButton().addOnLeftClickConsumer(() -> {getParent().getSaveManager().save();});
        addChildNCS(saveButton);
    }

    public void initializeToolbarControls(){
        TextButton toolbarButton = new TextButton("Toolbar", 255, height - 50, 200, 40);
        toolbarButton.getButton().addOnLeftClickConsumer(() -> {
            getParent().hideAllToolbarItems();
            getParent().getToolbarScreen().showAndEnable();
        });
        addChildNCS(toolbarButton);

        TextButton propertiesButton = new TextButton("Properties", 255, height - 100, 200, 40);
        propertiesButton.getButton().addOnLeftClickConsumer(() -> {
            getParent().hideAllToolbarItems();
            getParent().getPropertiesScreen().showAndEnable();
        });
        addChildNCS(propertiesButton);

        TextButton elementsButton = new TextButton("Elements", 255, height - 150, 200, 40);
        elementsButton.getButton().addOnLeftClickConsumer(() -> {
            getParent().hideAllToolbarItems();
            getParent().getElementListScreen().showAndEnable();
        });
        addChildNCS(elementsButton);
    }


    //endregion

    //region Methods

    @Override
    public ScreenEditorBaseScreen getParent() {
        return (ScreenEditorBaseScreen) super.getParent();
    }


    //endregion
}
