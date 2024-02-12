package dLib.ui.data.prefabs;

import dLib.ui.data.ListCompositeUIElementData;
import dLib.ui.data.implementations.HoverableData;
import dLib.ui.elements.prefabs.ListBox;

public class ListBoxData<ItemType> extends ListCompositeUIElementData {
    public HoverableData itemBoxBackground;
    public ScrollboxData scrollboxData;

    @Override
    public ListBox<ItemType> makeLiveInstance(Object... params) {
        return new ListBox<ItemType>(this);
    }
}
