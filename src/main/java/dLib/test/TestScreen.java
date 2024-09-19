package dLib.test;

import dLib.ui.Alignment;
import dLib.ui.animations.exit.UIAnimation_SlideOutDown;
import dLib.ui.elements.prefabs.*;
import dLib.ui.screens.AbstractScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestScreen extends AbstractScreen {

    public TestScreen(){
        addGenericBackground();

        TextBox box = new TextBox("HI HI HI HI", 100, 100, 300, 300).setWrap(true);
        box.setVerticalAlignment(Alignment.VerticalAlignment.CENTER);
        box.setMarginPercX(0).setMarginPercY(0);
        addChildNCS(box);

        addChildNCS(new Button(700, 1080-700, 50, 50).addOnLeftClickConsumer(new Runnable() {
            @Override
            public void run() {
                if(box.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
                    box.setVerticalAlignment(Alignment.VerticalAlignment.CENTER);
                }
                else if(box.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
                    box.setVerticalAlignment(Alignment.VerticalAlignment.BOTTOM);
                }
                else{
                    box.setVerticalAlignment(Alignment.VerticalAlignment.TOP);
                }
            }
        }));
    }

    @Override
    public String getModId() {
        return "Dlib";
    }
}
