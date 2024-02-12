package dLib.ui.data;

import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.elements.ListCompositeUIElement;

import java.io.Serializable;
import java.util.ArrayList;

public class ListCompositeUIElementData extends CompositeUIElementData implements Serializable {
    private static final long serialVersionUID = 1L;

    public ArrayList<CompositeUIElementData> dataList;

    @Override
    public ListCompositeUIElement makeLiveInstance(Object... params) {
        return new ListCompositeUIElement(this);
    }
}
