package dLib.ui.data.prefabs;

import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.composite.InputfieldScreenEditorItem;
import dLib.ui.data.UIElementData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.elements.prefabs.TextBox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InputfieldData extends UIElement.UIElementData implements Serializable {
    private static final long serialVersionUID = 1L;

    public Button.ButtonData buttonData = null;
    public TextBox.TextBoxData textboxData = null;

    public List<Character> characterFilter = new ArrayList<>();
    public int characterLimit = -1;

    Inputfield.EInputfieldPreset inputfieldPreset = Inputfield.EInputfieldPreset.GENERIC;

    @Override
    public UIElement makeUIElement() {
        return new Inputfield(this);
    }
}
