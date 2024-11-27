package dLib.test;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.*;
import dLib.ui.screens.AbstractScreen_DEPRECATED;
import dLib.ui.themes.UIThemeManager;

public class TestScreen extends UIElement {

    public TestScreen(){
        super(0, 0, 1920, 1080);

        addChildNCS(new Image(UIThemeManager.getDefaultTheme().background, 0, 0, getWidth(), getHeight()));

        addChildNCS(new HorizontalScrollBox(0, 0, 500, 500)
                .addChildNCS(new Button(0, 0, 100, 100))
                .addChildNCS(new Button(450, 0, 100, 100)));
    }
}
