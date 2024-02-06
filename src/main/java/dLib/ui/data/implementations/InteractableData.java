package dLib.ui.data.implementations;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Interactable;

public class InteractableData extends HoverableData{
    public String hoveredTexture;
    public String disabledTexture;

    public String hoveredColor;
    public String disabledColor;

    public boolean consumeTriggerEvent;

    @Override
    public UIElement makeLiveInstance(Object... params) {
        return new Interactable(this);
    }
}
