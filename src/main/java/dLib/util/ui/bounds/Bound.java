package dLib.util.ui.bounds;

import dLib.ui.elements.UIElement;

public class Bound {
    public static PositionBounds pos(float left, float bottom, float right, float top) {
        return new PositionBounds(left, bottom, right, top);
    }

    public static ParentBounds parent(UIElement child) {
        return new ParentBounds(child);
    }

    public static ElementBounds element(UIElement element) {
        return new ElementBounds(element);
    }
}
