package dLib.tools.screeneditorold.ui.items.implementations.toolbar;

import com.badlogic.gdx.utils.OrderedMap;
import dLib.tools.screeneditorold.ui.items.editoritems.*;
import dLib.ui.elements.prefabs.VerticalListBox;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

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

        VerticalListBox<String> toolElements = (VerticalListBox<String>) new VerticalListBox<String>(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()){
            @Override
            public void onItemSelectionChanged(ArrayList<String> items) {
                super.onItemSelectionChanged(items);

                if(items.isEmpty()) return;
                self.getParent().getPreviewScreen().makeNewPreviewItem(elementMap.get(items.get(0)));
            }
        }.setItems(new ArrayList<>(Arrays.asList(elementMap.orderedKeys().toArray())));
        toolElements.setImage(null);
        addChildNCS(toolElements);
    }

    //endregion

    //region Methods

    public void initializeElementMap(){
        elementMap.put("Button", TextButtonScreenEditorItem.class);
        elementMap.put("Background", BackgroundScreenEditorItem.class);
        elementMap.put("Image", ImageScreenEditorItem.class);
        elementMap.put("Inputfield", InputfieldScreenEditorItem.class);
        elementMap.put("Horizontal List Box", HorizontalListBoxScreenEditorItem.class);
        elementMap.put("Vertical List Box", VerticalListBoxScreenEditorItem.class);
        elementMap.put("Vertical Grid Box", VerticalGridBoxScreenEditorItem.class);
        elementMap.put("TextBox", TextBoxScreenEditorItem.class);
    }

    //endregion
}
