package dLib.tools.screeneditorold.screensold.preeditor;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Image;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.themes.UIThemeManager;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class ScreenEditorNewScreenScreen extends UIElement {
    /** Variables */
    private Inputfield inputfield;

    public ScreenEditorNewScreenScreen(){
        super(Pos.px(0), Pos.px(0), Dim.px(1920), Dim.px(1080));

        addChildNCS(new Image(UIThemeManager.getDefaultTheme().background, Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()));

        addChildNCS(new TextBox("Enter screen name:", Pos.px(539), Pos.px(1080-572), Dim.px(798), Dim.px(105)));

        Inputfield.InputfieldData test = new Inputfield.InputfieldData();
        test.dimensions.setValue(Dim.px(800), Dim.px(121));
        inputfield = new Inputfield(test);
        //inputfield = new Inputfield("", 538, 1080-713, 800, 121);
        inputfield.setLocalPosition(538, 1080-713);
        //inputfield.setDimensions(800, 121);
        addChildCS(inputfield);

        TextButton proceedButton = new TextButton("CREATE", Pos.px(683), Pos.px(1080-896), Dim.px(532), Dim.px(101));
        addChildCS(proceedButton);
    }
}
