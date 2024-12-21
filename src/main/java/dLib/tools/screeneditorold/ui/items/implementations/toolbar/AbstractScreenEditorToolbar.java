package dLib.tools.screeneditorold.ui.items.implementations.toolbar;

import com.badlogic.gdx.graphics.Color;
import dLib.tools.screeneditorold.screensold.ScreenEditorBaseScreen;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Renderable;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class AbstractScreenEditorToolbar extends UIElement {
    //region Variables
    //endregion

    //region Constructors

    public AbstractScreenEditorToolbar(){
        super(Pos.px(1508), Pos.px(10), Dim.px(404), Dim.px(1060));
        addChildNCS(new Renderable(Tex.stat(UICommonResources.white_pixel), Pos.px(0), Pos.px(0), getWidthRaw(), getHeightRaw()).setRenderColor(Color.valueOf("#242424FF")));
    }

    //endregion

    //region Methods

    @Override
    public ScreenEditorBaseScreen getParent() {
        return (ScreenEditorBaseScreen) super.getParent();
    }

    //endregion
}
