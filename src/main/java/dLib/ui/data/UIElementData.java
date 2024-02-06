package dLib.ui.data;

import dLib.ui.elements.UIElement;

public abstract class UIElementData {
    public String name;

    public int x;
    public int y;

    public abstract UIElement makeLiveInstance(Object... params);
}
