package dLib.test;

import dLib.ui.animations.exit.UIAnimation_SlideOutDown;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.elements.prefabs.VerticalGridBox;
import dLib.ui.screens.AbstractScreen;

public class TestScreen extends AbstractScreen {

    public TestScreen(){
        addGenericBackground();

        TextButton button = new TextButton("Test Button", 0, 0, 1920, 1080);
        button.getButton().addOnLeftClickConsumer(new Runnable() {
            @Override
            public void run() {
                button.hide();
            }
        });

        button.setExitAnimation(new UIAnimation_SlideOutDown(button));

        addChildCS(button);
    }

    @Override
    public String getModId() {
        return "Dlib";
    }
}
