package dLib.ui.data;

import dLib.ui.elements.UIElement;
import dLib.util.IntegerVector2;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.method.NoneMethodBinding;

import java.io.Serializable;

public class UIElementData implements Serializable {
    private static final long serialVersionUID = 1L;

    public String id;

    public IntegerVector2 localPosition = new IntegerVector2(0, 0);
    public boolean dockedToParent = true;

    public IntegerVector2 lowerLocalBound = new IntegerVector2(null, null);
    public IntegerVector2 upperLocalBound = new IntegerVector2(null, null);
    public IntegerVector2 lowerWorldBound = new IntegerVector2(null, null);
    public IntegerVector2 upperWorldBound = new IntegerVector2(null, null);
    public boolean boundWithinParent = false;
    public boolean borderToBorderBound = false;

    public boolean isVisible = true;
    public boolean isEnabled = true;

    public MethodBinding onSelectionStateChangedBinding = new NoneMethodBinding();

    public int width;
    public int height;

    public UIElement makeUIElement(){
        return new UIElement(this);
    }
}