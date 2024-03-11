package dLib.ui.data.implementations;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.elements.implementations.Interactable;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.method.NoneMethodBinding;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureEmptyBinding;

import java.io.Serializable;

public class InteractableData extends Hoverable.HoverableData implements Serializable {
    private static final long serialVersionUID = 1L;

    public TextureBinding hoveredTexture = new TextureEmptyBinding(); //TODO REWORK THIS UGLY THNG
    public TextureBinding disabledTexture = new TextureEmptyBinding();

    //TODO HOVEREDCOLOR
    //TODO DISABLEDCOLOR

    public boolean isPassthrough = false;

    //TODO ON HOVER KEY
    //TODO ON TRIGGER KEY
    //TODO ON HOLD KEY

    public MethodBinding onLeftClick = new NoneMethodBinding();
    public MethodBinding onLeftClickHeld = new NoneMethodBinding();
    public MethodBinding onLeftClickRelease = new NoneMethodBinding();

    public MethodBinding onRightClick = new NoneMethodBinding();
    public MethodBinding onRightClickHeld = new NoneMethodBinding();
    public MethodBinding onRightClickRelease = new NoneMethodBinding();

    @Override
    public UIElement makeUIElement() {
        return new Interactable(this);
    }
}
