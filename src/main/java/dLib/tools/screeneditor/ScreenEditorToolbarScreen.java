package dLib.tools.screeneditor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.OrderedMap;
import dLib.DLib;
import dLib.tools.screeneditor.ui.items.preview.*;
import dLib.ui.HorizontalAlignment;
import dLib.ui.elements.prefabs.ListBox;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.screens.AbstractScreen;

import java.util.ArrayList;
import java.util.Arrays;

public class ScreenEditorToolbarScreen extends AbstractScreen {
    public OrderedMap<String, UIPreviewItem> elementMap = new OrderedMap<>();

    public ScreenEditorToolbarScreen(){
        initializeElementMap();

        addElementToForeground(new TextBox("Elements:", 1508, 1080-52, 404, 43, 0, 0).setHorizontalAlignment(HorizontalAlignment.LEFT).setRenderColor(Color.WHITE));

        addInteractableElement(new ListBox<String>(1508, 10, 404, 1013){
            @Override
            public void onItemSelected(String item) {
                super.onItemSelected(item);
                onElementToAddChosen(elementMap.get(item));
            }
        }.setItems(new ArrayList<>(Arrays.asList(elementMap.orderedKeys().toArray()))));
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

    @Override
    public String getModId() {
        return DLib.getModID();
    }
}
