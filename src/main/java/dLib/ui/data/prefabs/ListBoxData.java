package dLib.ui.data.prefabs;

import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.composite.ListBoxScreenEditorItem;
import dLib.ui.data.ListCompositeUIElementData;
import dLib.ui.data.implementations.HoverableData;
import dLib.ui.elements.prefabs.ListBox;

import java.io.Serializable;

public class ListBoxData<ItemType> extends ListCompositeUIElementData  implements Serializable {
    private static final long serialVersionUID = 1L;

    public String titleBoxText = "";
    public int titleBoxHeight = 50;

    public int itemSpacing = 0;
    public boolean invertedItemOrder = false;

    public int scrollbarWidth = 50;

    //Add make element bindings, on clicked bingsins, and canclick

    @Override
    public ListBox<ItemType> makeLiveInstance(Object... params) {
        return new ListBox<ItemType>(this);
    }

    @Override
    public ScreenEditorItem makeEditorInstance() {
        return new ListBoxScreenEditorItem(this);
    }
}
