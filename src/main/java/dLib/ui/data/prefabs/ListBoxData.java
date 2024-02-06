package dLib.ui.data.prefabs;

import dLib.ui.data.ListCompositeUIElementData;
import dLib.ui.elements.prefabs.ListBox;

public class ListBoxData<ItemType> extends ListCompositeUIElementData {
    public ImageData itemBoxBackground;
    public ScrollboxData scrollboxData;

    @Override
    public ListBox<ItemType> makeLiveInstance(Object... params) {
        return new ListBox<ItemType>(this);
    }
}
