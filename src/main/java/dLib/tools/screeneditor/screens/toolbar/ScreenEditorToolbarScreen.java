package dLib.tools.screeneditor.screens.toolbar;

import com.badlogic.gdx.utils.OrderedMap;
import dLib.tools.screeneditor.ui.items.preview.*;
import dLib.tools.screeneditor.ui.items.preview.composite.InputfieldScreenEditor;
import dLib.tools.screeneditor.ui.items.preview.composite.ListBoxScreenEditor;
import dLib.tools.screeneditor.ui.items.preview.renderable.ButtonScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.renderable.ImageScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.renderable.TextBoxScreenEditorItem;
import dLib.ui.elements.prefabs.ListBox;

import java.util.ArrayList;
import java.util.Arrays;

public class ScreenEditorToolbarScreen extends AbstractScreenEditorToolbarScreen {
    public OrderedMap<String, ScreenEditorItem> elementMap = new OrderedMap<>();

    public ScreenEditorToolbarScreen(){
        super();
        initializeElementMap();

        ListBox<String> toolElements = new ListBox<String>(1508, 10, 404, 1060){
            @Override
            public void onItemSelected(String item) {
                super.onItemSelected(item);
                onElementToAddChosen(elementMap.get(item));
            }
        }.setItems(new ArrayList<>(Arrays.asList(elementMap.orderedKeys().toArray()))).setTitle("Tools:");
        toolElements.getBackground().setImage(null);
        addElement(toolElements);
    }

    /** Utility methods */
    public void initializeElementMap(){
        elementMap.put("Button", new ButtonScreenEditorItem()); // Should include a property for becoming a toggle instead & a text field property
        elementMap.put("Image", new ImageScreenEditorItem());
        elementMap.put("Inputfield", new InputfieldScreenEditor());
        elementMap.put("ListBox", new ListBoxScreenEditor());
        elementMap.put("TextBox", new TextBoxScreenEditorItem());
    }

    public void onElementToAddChosen(ScreenEditorItem previewItem){}
}
