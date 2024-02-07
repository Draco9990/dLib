package dLib.tools.screeneditor.screens.toolbar;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.OrderedMap;
import dLib.DLib;
import dLib.tools.screeneditor.ui.items.preview.*;
import dLib.tools.screeneditor.ui.items.preview.composite.InputfieldUIPreview;
import dLib.tools.screeneditor.ui.items.preview.composite.ListBoxUIPreview;
import dLib.tools.screeneditor.ui.items.preview.renderable.ButtonUIPreviewItem;
import dLib.tools.screeneditor.ui.items.preview.renderable.ImageUIPreviewItem;
import dLib.tools.screeneditor.ui.items.preview.renderable.TextBoxUIPreview;
import dLib.ui.HorizontalAlignment;
import dLib.ui.elements.prefabs.ListBox;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.screens.AbstractScreen;

import java.util.ArrayList;
import java.util.Arrays;

public class ScreenEditorToolbarScreen extends AbstractScreenEditorToolbarScreen {
    public OrderedMap<String, UIPreviewItem> elementMap = new OrderedMap<>();

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
        addInteractableElement(toolElements);
    }

    /** Utility methods */
    public void initializeElementMap(){
        elementMap.put("Button", new ButtonUIPreviewItem()); // Should include a property for becoming a toggle instead & a text field property
        elementMap.put("Image", new ImageUIPreviewItem());
        elementMap.put("Inputfield", new InputfieldUIPreview());
        elementMap.put("ListBox", new ListBoxUIPreview());
        elementMap.put("TextBox", new TextBoxUIPreview());
    }

    public void onElementToAddChosen(UIPreviewItem previewItem){}
}
