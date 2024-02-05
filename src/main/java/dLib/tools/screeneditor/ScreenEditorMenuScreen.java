package dLib.tools.screeneditor;

import com.badlogic.gdx.graphics.Color;
import dLib.DLib;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.screens.AbstractScreen;
import dLib.ui.themes.UITheme;
import dLib.ui.themes.UIThemeManager;

public class ScreenEditorMenuScreen extends AbstractScreen {
    /** Variables */

    /** Constructors */
    public ScreenEditorMenuScreen(){
        background = new Renderable(UITheme.whitePixel, 10, 1080 - 218, 1489, 209).setRenderColor(Color.valueOf("#242424FF"));

        registerFileControls();
        registerToolbarControls();
    }

    public void registerFileControls(){
        addInteractableElement(new TextButton("Close", 20, 1080-92, 236, 64));
    }

    public void registerToolbarControls(){
        addInteractableElement(new TextButton("Toolbar", 294, 1080-92, 236, 64));
        addInteractableElement(new TextButton("Properties", 294, 1080-167, 236, 64));
    }

    /** ModID */
    @Override
    public String getModId() {
        return DLib.getModID();
    }
}
