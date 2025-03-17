package dLib.test;

import com.badlogic.gdx.graphics.Color;
import dLib.mousestates.MouseStateManager;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIDropZoneComponent;
import dLib.ui.elements.items.Image;
import dLib.ui.elements.items.PropertyEditor;
import dLib.ui.elements.items.input.Inputfield;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.itembox.HorizontalDataBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.itembox.VerticalDataBox;
import dLib.ui.elements.items.scroll.Scrollbox;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.mousestates.DragAndDropMouseState;
import dLib.ui.resources.UICommonResources;
import dLib.util.DLibLogger;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class TestScreen extends UIElement {

    public TestScreen(){
        super(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());

        TextBox.TextBoxData textBoxData = new TextBox.TextBoxData();

        Inputfield in1 = new Inputfield("THIS IS SOME TEXT WOAHHHHHHHHHHHHHHHHHHHHHHHHHHHH DAMN BOI", Pos.px(100), Pos.px(100), Dim.px(200), Dim.px(200));
        in1.textBox.setWrap(true);
        in1.textBox.setContentAlignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.TOP);
        addChild(in1);

        Inputfield in2= new Inputfield("THIS IS SOME TEXT WOAHHHHHHHHHHHHHHHHHHHHHHHHHHHH DAMN BOI", Pos.px(400), Pos.px(100), Dim.px(200), Dim.px(200));
        in2.textBox.setWrap(true);
        in2.textBox.setContentAlignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.TOP);
        addChild(in2);

        Inputfield in3= new Inputfield("THIS IS SOME TEXT WOAHHHHHHHHHHHHHHHHHHHHHHHHHHHH DAMN BOI", Pos.px(700), Pos.px(100), Dim.px(200), Dim.px(200));
        in3.textBox.setWrap(true);
        in3.textBox.setContentAlignment(Alignment.HorizontalAlignment.RIGHT, Alignment.VerticalAlignment.TOP);
        addChild(in3);

        in1 = new Inputfield("THIS IS SOME TEXT WOAHHHHHHHHHHHHHHHHHHHHHHHHHHHH DAMN BOI", Pos.px(100), Pos.px(400), Dim.px(200), Dim.px(200));
        in1.textBox.setWrap(true);
        in1.textBox.setContentAlignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.CENTER);
        addChild(in1);

        in2= new Inputfield("THIS IS SOME TEXT WOAHHHHHHHHHHHHHHHHHHHHHHHHHHHH DAMN BOI", Pos.px(400), Pos.px(400), Dim.px(200), Dim.px(200));
        in2.textBox.setWrap(true);
        in2.textBox.setContentAlignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.CENTER);
        addChild(in2);

        in3= new Inputfield("THIS IS SOME TEXT WOAHHHHHHHHHHHHHHHHHHHHHHHHHHHH DAMN BOI", Pos.px(700), Pos.px(400), Dim.px(200), Dim.px(200));
        in3.textBox.setWrap(true);
        in3.textBox.setContentAlignment(Alignment.HorizontalAlignment.RIGHT, Alignment.VerticalAlignment.CENTER);
        addChild(in3);

        in1 = new Inputfield("THIS IS SOME TEXT WOAHHHHHHHHHHHHHHHHHHHHHHHHHHHH DAMN BOI", Pos.px(100), Pos.px(700), Dim.px(200), Dim.px(200));
        in1.textBox.setWrap(true);
        in1.textBox.setContentAlignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.BOTTOM);
        addChild(in1);

        in2= new Inputfield("THIS IS SOME TEXT WOAHHHHHHHHHHHHHHHHHHHHHHHHHHHH DAMN BOI", Pos.px(400), Pos.px(700), Dim.px(200), Dim.px(200));
        in2.textBox.setWrap(true);
        in2.textBox.setContentAlignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.BOTTOM);
        addChild(in2);

        in3= new Inputfield("THIS IS SOME TEXT WOAHHHHHHHHHHHHHHHHHHHHHHHHHHHH DAMN BOI", Pos.px(700), Pos.px(700), Dim.px(200), Dim.px(200));
        in3.textBox.setWrap(true);
        in3.textBox.setContentAlignment(Alignment.HorizontalAlignment.RIGHT, Alignment.VerticalAlignment.BOTTOM);
        addChild(in3);
    }
}