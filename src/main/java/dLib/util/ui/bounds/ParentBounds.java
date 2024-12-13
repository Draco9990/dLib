package dLib.util.ui.bounds;

import dLib.ui.elements.UIElement;

public class ParentBounds extends ElementBounds{
    private UIElement child;

    public ParentBounds(UIElement child) {
        super(child.getParent());
        this.child = child;
    }

    @Override
    protected UIElement getBoundElement() {
        return child.getParent();
    }
}
