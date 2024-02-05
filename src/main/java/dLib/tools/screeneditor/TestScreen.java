package dLib.tools.screeneditor;

import dLib.DLib;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.prefabs.ListBox;
import dLib.ui.screens.AbstractScreen;
import dLib.ui.themes.UIThemeManager;

public class TestScreen extends AbstractScreen {
    public TestScreen(){
        addGenericBackground();

        addInteractableElement(new ListBox<String>(100, 100, 1780, 880).addItem("AAA").addItem("BBB").addItem("CCC").addItem("DDD").addItem("EEE").addItem("FFF").addItem("GGG").addItem("HHH").addItem("III").addItem("JJJ").addItem("KKK").addItem("LLL").addItem("MMM").addItem("NNN").addItem("OOO").addItem("PPP").addItem("RRR").addItem("SSS").addItem("TTT").addItem("QQQ").addItem("VVV").addItem("WWW").addItem("XXX").addItem("YYY").addItem("ZZZ"));
    }

    @Override
    public String getModId() {
        return DLib.getModID();
    }
}
