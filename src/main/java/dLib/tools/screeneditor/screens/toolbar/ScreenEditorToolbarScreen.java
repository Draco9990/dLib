package dLib.tools.screeneditor.screens.toolbar;

import com.badlogic.gdx.utils.OrderedMap;
import dLib.tools.screeneditor.ui.items.preview.*;
import dLib.tools.screeneditor.ui.items.preview.composite.InputfieldScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.composite.ListBoxScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.composite.TextButtonScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.renderable.BackgroundScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.renderable.ImageScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.renderable.TextBoxScreenEditorItem;
import dLib.ui.elements.prefabs.ListBox;

import java.util.ArrayList;
import java.util.Arrays;

public class ScreenEditorToolbarScreen extends AbstractScreenEditorToolbarScreen {
    public OrderedMap<String, Class<? extends ScreenEditorItem>> elementMap = new OrderedMap<>();

    public ScreenEditorToolbarScreen(){
        super();
        initializeElementMap();

        ListBox<String> toolElements = new ListBox<String>(1508, 10, 404, 1060){
            @Override
            public void onItemSelectionChanged(ArrayList<String> items) {
                super.onItemSelectionChanged(items);

                if(items.isEmpty()) return;
                onElementToAddChosen(elementMap.get(items.get(0)));
            }
        }.setItems(new ArrayList<>(Arrays.asList(elementMap.orderedKeys().toArray()))).setTitle("Tools:");
        toolElements.getBackground().setImage(null);
        addElement(toolElements);
    }

    /** Utility methods */
    public void initializeElementMap(){
        elementMap.put("Button", TextButtonScreenEditorItem.class); //TODO Should include a property for becoming a toggle instead & a text field property
        elementMap.put("Background", BackgroundScreenEditorItem.class);
        elementMap.put("Image", ImageScreenEditorItem.class);
        elementMap.put("Inputfield", InputfieldScreenEditorItem.class);
        elementMap.put("ListBox", ListBoxScreenEditorItem.class);
        elementMap.put("TextBox", TextBoxScreenEditorItem.class);
    }

    public void onElementToAddChosen(Class<? extends ScreenEditorItem> previewItem){}
}
