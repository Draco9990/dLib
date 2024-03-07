package dLib.ui.data.implementations;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Interactable;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.method.NoneMethodBinding;

import java.io.Serializable;

public class InteractableData extends HoverableData implements Serializable {
    private static final long serialVersionUID = 1L;

    public MethodBinding onLeftClick = new NoneMethodBinding();
    public MethodBinding onLeftClickHeld = new NoneMethodBinding();
    public MethodBinding onLeftClickRelease = new NoneMethodBinding();

    public MethodBinding onRightClick = new NoneMethodBinding();
    public MethodBinding onRightClickHeld = new NoneMethodBinding();
    public MethodBinding onRightClickRelease = new NoneMethodBinding();

    @Override
    public Interactable makeLiveInstance(Object... params) {
        return new Interactable(this);
    }
}
