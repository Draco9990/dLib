package dLib.tools.screeneditor.screens.toolbar;

import com.badlogic.gdx.graphics.Color;
import dLib.DLib;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.screens.AbstractScreen;
import dLib.ui.themes.UITheme;

public class AbstractScreenEditorToolbarScreen extends AbstractScreen {
    /** Constructors */
    public AbstractScreenEditorToolbarScreen(){
        super(); //1508, 10, 404, 1060 dimensions
        addChildNCS(new Renderable(UITheme.whitePixel, 0, 0, getWidth(), getHeight()).setRenderColor(Color.valueOf("#242424FF")));
    }

    /** ID */
    @Override
    public String getModId() {
        return DLib.getModID();
    }
}
