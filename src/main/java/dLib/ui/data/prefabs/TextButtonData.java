package dLib.ui.data.prefabs;

import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.composite.TextButtonScreenEditorItem;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.TextButton;

import java.io.Serializable;

public class TextButtonData extends UIElement.UIElementData implements Serializable {
    private static final long serialVersionUID = 1L;

    public ButtonData buttonData;
    public TextBoxData textBoxData;

    @Override
    public UIElement makeUIElement() {
        return super.makeUIElement();
    }
}
