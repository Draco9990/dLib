package dLib.test;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.*;
import dLib.ui.screens.AbstractScreen_DEPRECATED;

import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class TestScreen extends UIElement {

    public TestScreen(){
        super(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());

        Scrollbox scrollbox = new Scrollbox(Pos.px(200), Pos.px(200), Dim.px(100), Dim.px(600));
        {
            VerticalBox test;
            scrollbox.addChildNCS(test = new VerticalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(500)));
            {
                for(int i = 0; i < 100; i++){
                    test.addItem(new TextButton("Test " + i, Dim.fill(), Dim.px(30)));
                }
            }
            VerticalBox test2;
            scrollbox.addChildNCS(test2 = new VerticalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(500)));
            {
                for(int i = 100; i < 200; i++){
                    test2.addItem(new TextButton("Test " + i, Dim.fill(), Dim.px(30)));
                }
            }
            VerticalBox test3;
            scrollbox.addChildNCS(test3 = new VerticalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(500)));
            {
                for(int i = 2; i < 300; i++){
                    test3.addItem(new TextButton("Test " + i, Dim.fill(), Dim.px(30)));
                }
            }
        }
        addChildNCS(scrollbox);
    }
}
