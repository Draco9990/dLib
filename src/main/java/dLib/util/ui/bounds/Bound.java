package dLib.util.ui.bounds;

import dLib.ui.elements.UIElement;

public class Bound {
    public static PositionBounds pos(Integer left, Integer bottom, Integer right, Integer top) {
        return new PositionBounds(left, bottom, right, top);
    }

    public static ParentBounds parent(UIElement child) {
        return new ParentBounds(child);
    }

    public static ElementBounds element(UIElement element) {
        return new ElementBounds(element);
    }
}
