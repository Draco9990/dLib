package dLib.ui.data;

import dLib.ui.elements.ListCompositeUIElement;

import java.util.ArrayList;

public class ListCompositeUIElementData extends CompositeUIElementData{
    public ArrayList<CompositeUIElementData> dataList;

    @Override
    public ListCompositeUIElement makeLiveInstance(Object... params) {
        return new ListCompositeUIElement(this);
    }
}
