package dLib.test;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.*;
import dLib.ui.screens.AbstractScreen_DEPRECATED;
import dLib.ui.themes.UIThemeManager;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class TestScreen extends UIElement {

    public TestScreen(){
        super(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());

        addChildNCS(new Image(UIThemeManager.getDefaultTheme().background, Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()));
    }
}
