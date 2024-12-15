package dLib.ui.bindings;

import dLib.properties.objects.StringProperty;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public class RelativeUIElementBinding extends UIBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    private StringProperty objectRelativePath = new StringProperty("");

    public RelativeUIElementBinding() {
        super();
    }

    @Override
    public UIElement getBoundObject(Object... params) {
        if(params.length == 0 || !(params[0] instanceof UIElement)){
            return null;
        }

        return ((UIElement) params[0]).getChildFromPath(getObjectRelativePath());
    }

    public String getObjectRelativePath() {
        return objectRelativePath.getValue();
    }
    public StringProperty getObjectRelativePathRaw() {
        return objectRelativePath;
    }

    @Override
    public String getDisplayValue() {
        return "";
    }
}
