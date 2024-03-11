package dLib.ui.data.prefabs;

import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.renderable.ButtonScreenEditorItem;
import dLib.ui.data.implementations.InteractableData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.elements.prefabs.Button;

import java.io.Serializable;

public class ButtonData extends Interactable.InteractableData implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public UIElement makeUIElement() {
        return new Button(this);
    }
}
