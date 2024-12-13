package dLib.util.ui.bounds;

import dLib.ui.elements.UIElement;

public class Bound {
    public static StaticBounds constant(Integer left, Integer bottom, Integer right, Integer top) {
        return new StaticBounds(left, bottom, right, top);
    }

    public static ParentBounds parent(UIElement child) {
        return new ParentBounds(child);
    }

    public static ElementBounds element(UIElement element) {
        return new ElementBounds(element);
    }
}
