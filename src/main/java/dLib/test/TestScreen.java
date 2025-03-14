package dLib.test;

import com.badlogic.gdx.graphics.Color;
import dLib.mousestates.MouseStateManager;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIDropZoneComponent;
import dLib.ui.elements.items.Image;
import dLib.ui.elements.items.PropertyEditor;
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

        PropertyEditor propertyEditor = new PropertyEditor(Pos.px(100), Pos.px(100), Dim.px(600), Dim.px(900));
        propertyEditor.setProperties(textBoxData.getEditableProperties());
        addChild(propertyEditor);
    }
}