package dLib.ui.data;

import dLib.ui.elements.UIElement;

import java.io.Serializable;

public abstract class UIElementData implements Serializable {
    private static final long serialVersionUID = 1L;

    public String ID;

    public int x;
    public int y;

    public int width;
    public int height;

    public abstract UIElement makeLiveInstance(Object... params);
}
