package dLib.test;

import dLib.ui.elements.prefabs.VerticalGridBox;
import dLib.ui.screens.AbstractScreen;

public class TestScreen extends AbstractScreen {

    public TestScreen(){
        addGenericBackground();

        VerticalGridBox<String> verticalGridBox = new VerticalGridBox<String>(0, 0, 1920, 1080);
        for(int i = 1; i < 1001; i++){
            verticalGridBox.addItem("Item" + i);
        }

        addChildCS(verticalGridBox);
    }

    @Override
    public String getModId() {
        return "Dlib";
    }
}
