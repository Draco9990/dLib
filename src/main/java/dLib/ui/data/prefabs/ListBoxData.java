package dLib.ui.data.prefabs;

import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.composite.ListBoxScreenEditorItem;
import dLib.ui.data.UIElementData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.ListBox;
import dLib.ui.util.ESelectionMode;

import java.io.Serializable;

public class ListBoxData extends UIElement.UIElementData implements Serializable {
    private static final long serialVersionUID = 1L;

    public String titleBoxText = "";
    public int titleBoxHeight = 50;

    public int itemSpacing = 0;
    public boolean invertedItemOrder = false;

    public int scrollbarWidth = 50;

    public ESelectionMode selectionMode = ESelectionMode.SINGLE;
    public int selectionLimit = 1;

    @Override
    public UIElement makeUIElement() {
        return new ListBox<>(this);
    }
}
