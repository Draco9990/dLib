package dLib.tools.screeneditor.ui.items.implementations.toolbar;

import com.badlogic.gdx.graphics.Color;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.themes.UITheme;

public class AbstractScreenEditorToolbar extends UIElement {
    //region Variables
    //endregion

    //region Constructors

    public AbstractScreenEditorToolbar(){
        super(1508, 10, 404, 1060);
        addChildNCS(new Renderable(UITheme.whitePixel, 0, 0, getWidth(), getHeight()).setRenderColor(Color.valueOf("#242424FF")));
    }

    //endregion

    //region Methods

    @Override
    public ScreenEditorBaseScreen getParent() {
        return (ScreenEditorBaseScreen) super.getParent();
    }

    //endregion
}
