package dLib.ui.data.implementations;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Interactable;

import java.io.Serializable;

public class InteractableData extends HoverableData implements Serializable {
    private static final long serialVersionUID = 1L;

    public String hoveredTexture;
    public String disabledTexture;

    public String hoveredColor;
    public String disabledColor;

    public boolean consumeTriggerEvent;

    @Override
    public Interactable makeLiveInstance(Object... params) {
        return new Interactable(this);
    }
}
