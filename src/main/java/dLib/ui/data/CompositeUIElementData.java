package dLib.ui.data;

import dLib.ui.elements.CompositeUIElement;

import java.util.ArrayList;

public class CompositeUIElementData extends UIElementData{
    public ArrayList<UIElementData> background = new ArrayList<>();
    public UIElementData left;
    public UIElementData middle;
    public UIElementData right;
    public ArrayList<UIElementData> foreground = new ArrayList<>();

    public boolean isTemporary;

    @Override
    public CompositeUIElement makeLiveInstance(Object... params) {
        return new CompositeUIElement(this);
    }
}
