package dLib.tools.screeneditor.screens.toolbar;

import com.badlogic.gdx.graphics.Color;
import dLib.DLib;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.screens.AbstractScreen;
import dLib.ui.themes.UITheme;

public class AbstractScreenEditorToolbarScreen extends AbstractScreen {
    /** Constructors */
    public AbstractScreenEditorToolbarScreen(){
        background = new Renderable(UITheme.whitePixel, 1508, 10, 404, 1060);
        background.setRenderColor(Color.valueOf("#242424FF"));
    }

    /** ID */
    @Override
    public String getModId() {
        return DLib.getModID();
    }
}
