package dLib.test;

import com.megacrit.cardcrawl.helpers.RelicLibrary;
import dLib.betterscreens.ui.elements.items.PotionSelectPopup;
import dLib.betterscreens.ui.elements.items.RelicSelectPopup;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.VerticalCollapsableBox;
import dLib.ui.elements.items.itembox.VerticalDataBox;
import dLib.ui.elements.items.scroll.Scrollbox;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;

public class TestScreen extends UIElement {

    public TestScreen(){
        super(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());

        /*String text = "THIS is a [ REE ] test [RED]text[GREEN] that im doing[] why not [] woahhhh";

        Inputfield in1 = new Inputfield(text, Pos.px(100), Pos.px(100), Dim.px(200), Dim.px(200));
        in1.textBox.setWrap(true);
        in1.textBox.setContentAlignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.TOP);
        addChild(in1);

        Inputfield in2= new Inputfield(text, Pos.px(400), Pos.px(100), Dim.px(200), Dim.px(200));
        in2.textBox.setWrap(true);
        in2.textBox.setContentAlignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.TOP);
        addChild(in2);

        Inputfield in3= new Inputfield(text, Pos.px(700), Pos.px(100), Dim.px(200), Dim.px(200));
        in3.textBox.setWrap(true);
        in3.textBox.setContentAlignment(Alignment.HorizontalAlignment.RIGHT, Alignment.VerticalAlignment.TOP);
        addChild(in3);

        in1 = new Inputfield(text, Pos.px(100), Pos.px(400), Dim.px(200), Dim.px(200));
        in1.textBox.setWrap(true);
        in1.textBox.setContentAlignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.CENTER);
        addChild(in1);

        in2= new Inputfield(text, Pos.px(400), Pos.px(400), Dim.px(200), Dim.px(200));
        in2.textBox.setWrap(true);
        in2.textBox.setContentAlignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.CENTER);
        addChild(in2);

        in3= new Inputfield(text, Pos.px(700), Pos.px(400), Dim.px(200), Dim.px(200));
        in3.textBox.setWrap(true);
        in3.textBox.setContentAlignment(Alignment.HorizontalAlignment.RIGHT, Alignment.VerticalAlignment.CENTER);
        addChild(in3);

        in1 = new Inputfield(text, Pos.px(100), Pos.px(700), Dim.px(200), Dim.px(200));
        in1.textBox.setWrap(true);
        in1.textBox.setContentAlignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.BOTTOM);
        addChild(in1);

        in2= new Inputfield(text, Pos.px(400), Pos.px(700), Dim.px(200), Dim.px(200));
        in2.textBox.setWrap(true);
        in2.textBox.setContentAlignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.BOTTOM);
        addChild(in2);

        in3= new Inputfield(text, Pos.px(700), Pos.px(700), Dim.px(200), Dim.px(200));
        in3.textBox.setWrap(true);
        in3.textBox.setContentAlignment(Alignment.HorizontalAlignment.RIGHT, Alignment.VerticalAlignment.BOTTOM);
        addChild(in3);*/

        /*Scrollbox scrollbox = new Scrollbox(Pos.px(100), Pos.px(100), Dim.px(500), Dim.px(500));
        {
            VerticalDataBox<String> vdb = new VerticalDataBox<String>(Dim.fill(), Dim.fill());
            vdb.setCanReorder(true);
            vdb.setCanDelete(true);
            vdb.setExternalToggling(true);
            vdb.addChild("Test1");
            vdb.addChild("Test2");
            vdb.addChild("Test3");
            vdb.addChild("Test4");
            vdb.addChild("Test5");
            vdb.addChild("Test6");
            vdb.addChild("Test7");
            vdb.addChild("Test21");
            vdb.addChild("Test22");
            vdb.addChild("Test23");
            vdb.addChild("Test24");
            vdb.addChild("Test25");
            vdb.addChild("Test26");
            vdb.addChild("Test27");
            vdb.addChild("Test31");
            vdb.addChild("Test32");
            vdb.addChild("Test33");
            vdb.addChild("Test34");
            vdb.addChild("Test35");
            vdb.addChild("Test36");
            vdb.addChild("Test37");
            scrollbox.addChild(vdb);
        }
        addChild(scrollbox);*/

        PotionSelectPopup pop = new PotionSelectPopup();
        pop.open();
    }
}