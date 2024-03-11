package dLib.ui.data.prefabs;

import dLib.ui.data.implementations.InteractableData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.elements.prefabs.Toggle;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureEmptyBinding;

import java.io.Serializable;

public class ToggleData extends Interactable.InteractableData implements Serializable {
    private static final long serialVersionUID = 1L;

    public boolean isToggled;

    public TextureBinding toggledTexture = new TextureEmptyBinding();
    public TextureBinding toggledHoveredTexture = new TextureEmptyBinding();
    public TextureBinding toggledDisabledTexture = new TextureEmptyBinding();

    @Override
    public UIElement makeUIElement() {
        return new Toggle(this);
    }
}
