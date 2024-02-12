package dLib.ui.data.prefabs;

import dLib.ui.data.ListCompositeUIElementData;
import dLib.ui.data.implementations.HoverableData;
import dLib.ui.elements.prefabs.ListBox;

import java.io.Serializable;

public class ListBoxData<ItemType> extends ListCompositeUIElementData  implements Serializable {
    private static final long serialVersionUID = 1L;

    public HoverableData itemBoxBackground;
    public ScrollboxData scrollboxData;

    @Override
    public ListBox<ItemType> makeLiveInstance(Object... params) {
        return new ListBox<ItemType>(this);
    }
}
