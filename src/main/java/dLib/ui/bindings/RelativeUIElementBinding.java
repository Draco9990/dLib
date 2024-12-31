package dLib.ui.bindings;

import dLib.properties.objects.StringProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.tools.uicreator.ui.properties.editors.UCRelativeUIElementBindingValueEditor;
import dLib.tools.uicreator.ui.properties.objects.UCUIElementBindingProperty;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public class RelativeUIElementBinding extends UIElementBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    private StringProperty objectRelativePath = new StringProperty("");

    public RelativeUIElementBinding() {
        super();
    }

    public RelativeUIElementBinding(UIElement object) {
        super();
        objectRelativePath.setValue(object.getRelativePath());
    }

    @Override
    public UIElement getBoundObject(Object... params) {
        if(params.length == 0 || !(params[0] instanceof UIElement)){
            return null;
        }

        return ((UIElement) params[0]).findChildFromPath(getObjectRelativePath());
    }

    @Override
    public boolean isBindingValid() {
        return !getObjectRelativePath().isEmpty();
    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new UCRelativeUIElementBindingValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new UCRelativeUIElementBindingValueEditor((UCUIElementBindingProperty) property);
    }

    public String getObjectRelativePath() {
        return objectRelativePath.getValue();
    }
    public StringProperty getObjectRelativePathRaw() {
        return objectRelativePath;
    }

    @Override
    public String getDisplayValue() {
        return objectRelativePath.getValue();
    }
}
