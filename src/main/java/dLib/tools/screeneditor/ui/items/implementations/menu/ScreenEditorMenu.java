package dLib.tools.screeneditor.ui.items.implementations.menu;

import com.badlogic.gdx.graphics.Color;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.elements.implementations.Toggle;
import dLib.ui.screens.UIManager;
import dLib.ui.themes.UITheme;
import dLib.util.TextureManager;

public class ScreenEditorMenu extends UIElement {
    //region Variables

    //endregion

    //region Constructors

    public ScreenEditorMenu(){
        super(10, 1080-220, 1490, 210);
        addChildNCS(new Renderable(UITheme.whitePixel, 0, 0, getWidth(), getHeight()).setRenderColor(Color.valueOf("#242424FF")));

        initializeFileControls();
        initializePropertyControls();

        initializeToolbar();
    }

    private void initializeFileControls(){
        TextButton closeButton = new TextButton("Close", 10, getHeight() - 50, 200, 40);
        closeButton.getButton().addOnLeftClickConsumer(this::close);
        addChildNCS(closeButton);

        TextButton saveButton = new TextButton("Save", 10, getHeight() - 100, 200, 40);
        saveButton.getButton().addOnLeftClickConsumer(() -> {getParent().getSaveManager().save();});
        addChildNCS(saveButton);
    }

    private void initializePropertyControls(){
        TextButton toolbarButton = new TextButton("Toolbar", 255, getHeight() - 50, 200, 40);
        toolbarButton.getButton().addOnLeftClickConsumer(() -> {
            getParent().hideAllToolbarItems();
            getParent().getToolbarScreen().showAndEnable();
        });
        addChildNCS(toolbarButton);

        TextButton propertiesButton = new TextButton("Properties", 255, getHeight() - 100, 200, 40);
        propertiesButton.getButton().addOnLeftClickConsumer(() -> {
            getParent().hideAllToolbarItems();
            getParent().getPropertiesScreen().showAndEnable();
        });
        addChildNCS(propertiesButton);

        TextButton elementsButton = new TextButton("Elements", 255, getHeight() - 150, 200, 40);
        elementsButton.getButton().addOnLeftClickConsumer(() -> {
            getParent().hideAllToolbarItems();
            getParent().getElementListScreen().showAndEnable();
        });
        addChildNCS(elementsButton);
    }

    private void initializeToolbar(){
        ScreenEditorMenu menu = this;

        HorizontalBox hBox = new HorizontalBox(0, 0, 1488, 40);
        hBox.setItemSpacing(5);

        addChildNCS(hBox);

        hBox.addItem(new Toggle(TextureManager.getTexture("dLibResources/images/ui/screeneditor/GridButton.png"), 0, 0, 40, 40){
            @Override
            public void toggle() {
                super.toggle();

                menu.getParent().getEditorProperties().toggleGrid();
            }
        });
    }

    //endregion

    //region Methods

    @Override
    public ScreenEditorBaseScreen getParent() {
        return (ScreenEditorBaseScreen) super.getParent();
    }


    //endregion
}
