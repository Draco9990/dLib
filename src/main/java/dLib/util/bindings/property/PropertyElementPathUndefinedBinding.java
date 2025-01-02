package dLib.util.bindings.property;

import dLib.properties.objects.templates.TProperty;
import dLib.ui.bindings.UIElementUndefinedRelativePathBinding;

import java.io.Serializable;

public class PropertyElementPathUndefinedBinding extends PropertyElementPathBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    public PropertyElementPathUndefinedBinding(){
        super(new UIElementUndefinedRelativePathBinding(), "none");
    }

    @Override
    public TProperty getBoundObject(Object... params) {
        return null;
    }

    @Override
    public String getDisplayValue() {
        return "NONE";
    }
}
