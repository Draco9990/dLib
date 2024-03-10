package dLib.ui.data.prefabs;

import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.composite.TextButtonScreenEditorItem;
import dLib.ui.data.UIElementData;
import dLib.ui.elements.prefabs.TextButton;

import java.io.Serializable;

public class TextButtonData extends UIElementData implements Serializable {
    private static final long serialVersionUID = 1L;

    public ButtonData buttonData;
    public TextBoxData textBoxData;

    @Override
    public TextButton makeLiveInstance(Object... params) {
        return new TextButton(this);
    }

    @Override
    public ScreenEditorItem makeEditorInstance() {
        return new TextButtonScreenEditorItem(this);
    }
}
