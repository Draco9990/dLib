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

        Scrollbox scrollbox = new Scrollbox(Pos.px(200), Pos.px(200), Dim.px(400), Dim.px(400));
        {
            scrollbox.addChildNCS(new Button(Pos.px(-20), Pos.px(-20), Dim.px(30), Dim.px(30)));
            scrollbox.addChildNCS(new Button(Pos.px(370), Pos.px(370), Dim.px(30), Dim.px(30)));
        }
        addChildNCS(scrollbox);
    }
}
