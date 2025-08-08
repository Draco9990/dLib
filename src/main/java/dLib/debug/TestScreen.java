package dLib.debug;

import dLib.betterscreens.ui.elements.items.RelicSelectPopup;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.ContextMenu;
import dLib.ui.elements.items.VerticalCollapsableBox;
import dLib.ui.elements.items.input.Inputfield;
import dLib.ui.elements.items.itembox.VerticalDataBox;
import dLib.ui.elements.items.scroll.Scrollbox;
import dLib.ui.elements.items.text.TextButton;
import dLib.util.bindings.string.Str;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;

public class TestScreen extends UIElement {

    public TestScreen(){
        super(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());

        String text = "THIS is a [ REE ] test [RED]text[GREEN] that im doing[] why not [] woahhhh";

        Inputfield in1 = new Inputfield(text, Pos.px(100), Pos.px(100), Dim.px(200), Dim.px(200));
        in1.textBox.setWrap(true);
        in1.textBox.setContentAlignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.TOP);
        //addChild(in1);

        /*Inputfield in2= new Inputfield(text, Pos.px(400), Pos.px(100), Dim.px(200), Dim.px(200));
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

        Scrollbox scrollbox = new Scrollbox(Pos.px(100), Pos.px(100), Dim.px(500), Dim.px(500));
        {
            VerticalDataBox<String> vdb = new VerticalDataBox<String>(Dim.fill(), Dim.fill());
            vdb.setCanReorder(true);
            vdb.setCanDelete(true);
            vdb.setExternalToggling(true);
            vdb.addChild("Test1");
            /*vdb.addChild("Test12");
            vdb.addChild("Test13");
            vdb.addChild("Test14");
            vdb.addChild("Test15");
            vdb.addChild("Test16");
            vdb.addChild("Test17");
            vdb.addChild("Test18");
            vdb.addChild("Test19");
            vdb.addChild("Test10");
            vdb.addChild("Test111");
            vdb.addChild("Test122");
            vdb.addChild("Test133");*/
            //scrollbox.addChild(vdb);
        }
        //addChild(scrollbox);

        /*HorizontalBox test = new HorizontalBox(Pos.px(100), Pos.px(100), Dim.px(500), Dim.px(300));
        test.setTexture(UICommonResources.white_pixel);
        {
            Image test2 = new Image(Tex.stat(UICommonResources.white_pixel), Dim.px(100), Dim.px(100));
            test.addChild(test2);
        }
        addChild(test);*/

        /*Image img = new Image(Tex.stat(UICommonResources.white_pixel), Pos.perc(0.1), Pos.perc(0.1), Dim.px(200), Dim.px(200));
        img.onLeftClickEvent.subscribe(img, () -> img.setHorizontalAlignment((Alignment.HorizontalAlignment) EnumHelpers.nextEnum(img.getHorizontalAlignment())));
        img.onRightClickEvent.subscribe(img, () -> img.setVerticalAlignment((Alignment.VerticalAlignment) EnumHelpers.nextEnum(img.getVerticalAlignment())));
        addChild(img);*/

        /*RelicSelectPopup pop = new RelicSelectPopup();
        pop.putAllPotions();
        pop.open();*/

        /*CardSlot cardSlot = new CardSlot(Pos.px(100), Pos.px(100), 0.5f);
        addChild(cardSlot);*/

        /*ContextMenu test = new ContextMenu(Pos.px(0), Pos.px(0));
        test.optionsBox.addChild(new ContextMenu.ContextMenuButtonOption(Str.stat("test 1"), () -> {}));
        {
            ArrayList<ContextMenu.IContextMenuOption> subOptions = new ArrayList<>();
            subOptions.add(new ContextMenu.ContextMenuButtonOption(Str.stat("sub test 1"), () -> {}));
            subOptions.add(new ContextMenu.ContextMenuButtonOption(Str.stat("sub test 2"), () -> {}));
            ContextMenu.ContextMenuDropdownOption dropdown = new ContextMenu.ContextMenuDropdownOption(Str.stat("dropdown test"), subOptions);
            test.optionsBox.addChild(dropdown);
        }
        test.optionsBox.addChild(new ContextMenu.ContextMenuButtonOption(Str.stat("test 2"), () -> {}));
        test.optionsBox.addChild(new ContextMenu.ContextMenuButtonOption(Str.stat("test 3"), () -> {}));
        addChild(test);*/
    }
}