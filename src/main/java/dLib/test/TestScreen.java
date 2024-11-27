package dLib.test;

import dLib.ui.elements.prefabs.*;
import dLib.ui.screens.AbstractScreen;

public class TestScreen extends AbstractScreen {

    public TestScreen(){
        addGenericBackground();

        addChildNCS(new HorizontalScrollBox(0, 0, 500, 500)
                .addChildNCS(new Button(0, 0, 100, 100))
                .addChildNCS(new Button(450, 0, 100, 100)));
    }
}
