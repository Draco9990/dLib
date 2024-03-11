package dLib.ui.data.implementations;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.elements.implementations.Renderable;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.method.NoneMethodBinding;

import java.io.Serializable;

public class HoverableData extends Renderable.RenderableData implements Serializable {
    private static final long serialVersionUID = 1L;

    public MethodBinding onHovered = new NoneMethodBinding();
    public MethodBinding onHoverTick = new NoneMethodBinding();
    public MethodBinding onUnhovered = new NoneMethodBinding();

    @Override
    public UIElement makeUIElement() {
        return new Hoverable(this);
    }
}
