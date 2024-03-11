package dLib.tools.screeneditor.ui.items.implementations.toolbar;

import com.badlogic.gdx.utils.OrderedMap;
import dLib.tools.screeneditor.ui.items.editoritems.*;
import dLib.tools.screeneditor.ui.items.editoritems.composite.InputfieldScreenEditorItem;
import dLib.tools.screeneditor.ui.items.editoritems.composite.ListBoxScreenEditorItem;
import dLib.tools.screeneditor.ui.items.editoritems.composite.TextButtonScreenEditorItem;
import dLib.tools.screeneditor.ui.items.editoritems.renderable.BackgroundScreenEditorItem;
import dLib.tools.screeneditor.ui.items.editoritems.renderable.ImageScreenEditorItem;
import dLib.tools.screeneditor.ui.items.editoritems.renderable.TextBoxScreenEditorItem;
import dLib.ui.elements.prefabs.ListBox;

import java.util.ArrayList;
import java.util.Arrays;

public class ScreenEditorToolbox extends AbstractScreenEditorToolbar {
    //region Variables

    public OrderedMap<String, Class<? extends ScreenEditorItem>> elementMap = new OrderedMap<>();

    //endregion

    //region Constructors

    public ScreenEditorToolbox(){
        super();
        initializeElementMap();

        ScreenEditorToolbox self =this;

        ListBox<String> toolElements = new ListBox<String>(0, 0, getWidth(), getHeight()){
            @Override
            public void onItemSelectionChanged(ArrayList<String> items) {
                super.onItemSelectionChanged(items);

                if(items.isEmpty()) return;
                self.getParent().getPreviewScreen().makeNewPreviewItem(elementMap.get(items.get(0)));
            }
        }.setItems(new ArrayList<>(Arrays.asList(elementMap.orderedKeys().toArray()))).setTitle("Tools:");
        toolElements.getBackground().setImage(null);
        addChildNCS(toolElements);
    }

    //endregion

    //region Methods

    public void initializeElementMap(){
        elementMap.put("Button", TextButtonScreenEditorItem.class);
        elementMap.put("Background", BackgroundScreenEditorItem.class);
        elementMap.put("Image", ImageScreenEditorItem.class);
        elementMap.put("Inputfield", InputfieldScreenEditorItem.class);
        elementMap.put("ListBox", ListBoxScreenEditorItem.class);
        elementMap.put("TextBox", TextBoxScreenEditorItem.class);
    }

    //endregion
}
