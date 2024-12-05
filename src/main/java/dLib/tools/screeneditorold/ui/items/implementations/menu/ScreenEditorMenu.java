package dLib.tools.screeneditorold.ui.items.implementations.menu;

import com.badlogic.gdx.graphics.Color;
import dLib.tools.screeneditorold.screensold.ScreenEditorBaseScreen;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.elements.implementations.Toggle;
import dLib.ui.themes.UITheme;
import dLib.util.TextureManager;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class ScreenEditorMenu extends UIElement {
    //region Variables

    //endregion

    //region Constructors

    public ScreenEditorMenu(){
        super(Pos.px(10), Pos.px(1080-220), Dim.px(1490), Dim.px(210));
        addChildNCS(new Renderable(UITheme.whitePixel, Pos.px(0), Pos.px(0), getWidthRaw(), getHeightRaw()).setRenderColor(Color.valueOf("#242424FF")));

        initializeFileControls();
        initializePropertyControls();

        initializeToolbar();
    }

    private void initializeFileControls(){
        TextButton closeButton = new TextButton("Close", Pos.px(10), Pos.px(10), Dim.px(200), Dim.px(40));
        closeButton.getButton().addOnLeftClickEvent(this::close);
        addChildNCS(closeButton);

        TextButton saveButton = new TextButton("Save", Pos.px(10), Pos.px(getHeight() - 100), Dim.px(200), Dim.px(40));
        saveButton.getButton().addOnLeftClickEvent(() -> {getParent().getSaveManager().save();});
        addChildNCS(saveButton);
    }

    private void initializePropertyControls(){
        TextButton toolbarButton = new TextButton("Toolbar", Pos.px(255), Pos.px(getHeight() - 50), Dim.px(200), Dim.px(40));
        toolbarButton.getButton().addOnLeftClickEvent(() -> {
            getParent().hideAllToolbarItems();
            getParent().getToolbarScreen().showAndEnable();
        });
        addChildNCS(toolbarButton);

        TextButton propertiesButton = new TextButton("Properties", Pos.px(255), Pos.px(getHeight() - 100), Dim.px(200), Dim.px(40));
        propertiesButton.getButton().addOnLeftClickEvent(() -> {
            getParent().hideAllToolbarItems();
            getParent().getPropertiesScreen().showAndEnable();
        });
        addChildNCS(propertiesButton);

        TextButton elementsButton = new TextButton("Elements", Pos.px(255), Pos.px(getHeight() - 150), Dim.px(200), Dim.px(40));
        elementsButton.getButton().addOnLeftClickEvent(() -> {
            getParent().hideAllToolbarItems();
            getParent().getElementListScreen().showAndEnable();
        });
        addChildNCS(elementsButton);
    }

    private void initializeToolbar(){
        ScreenEditorMenu menu = this;

        HorizontalBox hBox = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.px(1488), Dim.px(40));
        hBox.setItemSpacing(5);

        addChildNCS(hBox);

        hBox.addItem(new Toggle(TextureManager.getTexture("dLibResources/images/ui/screeneditor/GridButton.png"), Pos.px(0), Pos.px(0), Dim.px(40), Dim.px(40)){
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
