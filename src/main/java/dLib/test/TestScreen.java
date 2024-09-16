package dLib.test;

import dLib.ui.animations.exit.UIAnimation_SlideOutDown;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.elements.prefabs.VerticalGridBox;
import dLib.ui.elements.prefabs.VerticalListBox;
import dLib.ui.screens.AbstractScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestScreen extends AbstractScreen {

    public TestScreen(){
        addGenericBackground();

        ArrayList<String> test = new ArrayList<>();

        for(int i = 0; i < 100; i++){
            test.add("Test " + i);
        }

        addChildNCS(new VerticalListBox<String>(100, 100, 1920-200, 1080-200).setItems(test));
    }

    @Override
    public String getModId() {
        return "Dlib";
    }
}
