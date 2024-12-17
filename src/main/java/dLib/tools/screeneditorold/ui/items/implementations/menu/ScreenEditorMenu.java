package dLib.tools.screeneditorold.ui.items.implementations.menu;

import com.badlogic.gdx.graphics.Color;
import dLib.tools.screeneditorold.screensold.ScreenEditorBaseScreen;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.elements.implementations.Toggle;
import dLib.ui.resources.UICommonResources;

import dLib.util.TextureManager;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class ScreenEditorMenu extends UIElement {
    //region Variables

    //endregion

    //region Constructors

    public ScreenEditorMenu(){
        super(Pos.px(10), Pos.px(1080-220), Dim.px(1490), Dim.px(210));
        addChildNCS(new Renderable(Tex.stat(UICommonResources.white_pixel), Pos.px(0), Pos.px(0), getWidthRaw(), getHeightRaw()).setRenderColor(Color.valueOf("#242424FF")));

        initializeFileControls();
        initializePropertyControls();

        initializeToolbar();
    }

    private void initializeFileControls(){

    }

    private void initializePropertyControls(){
    }

    private void initializeToolbar(){
    }

    //endregion

    //region Methods

    @Override
    public ScreenEditorBaseScreen getParent() {
        return (ScreenEditorBaseScreen) super.getParent();
    }


    //endregion
}
