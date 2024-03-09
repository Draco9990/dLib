package dLib.ui.data.prefabs;

import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.composite.InputfieldScreenEditorItem;
import dLib.ui.data.CompositeUIElementData;
import dLib.ui.data.UIElementData;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.prefabs.Inputfield;

import java.io.Serializable;
import java.util.List;

public class InputfieldData extends UIElementData implements Serializable {
    private static final long serialVersionUID = 1L;

    public ButtonData buttonData;
    public TextBoxData textboxData;

    public List<Character> characterFilter;
    public int characterLimit;

    @Override
    public Inputfield makeLiveInstance(Object... params) {
        return new Inputfield(this);
    }

    @Override
    public ScreenEditorItem makeEditorInstance() {
        return new InputfieldScreenEditorItem(this);
    }
}
