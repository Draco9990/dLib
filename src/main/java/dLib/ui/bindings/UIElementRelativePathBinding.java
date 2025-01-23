package dLib.ui.bindings;

import dLib.properties.objects.Property;
import dLib.properties.objects.StringProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.tools.uicreator.ui.properties.editors.UCRelativeUIElementBindingValueEditor;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public class UIElementRelativePathBinding extends AbstractUIElementBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    private StringProperty objectRelativePath = new StringProperty("");

    private transient UIElement ownerCache;
    private transient UIElement objectCache;

    protected UIElementRelativePathBinding() {
        super();
    }

    public UIElementRelativePathBinding(UIElement owner, UIElement object) {
        super();
        objectRelativePath.setValue(owner.getRelativePathToElement(object));
        objectCache = object;

        object.onHierarchyChangedEvent.subscribe(this, () -> {
            objectRelativePath.setValue(owner.getRelativePathToElement(object));
        });
    }

    @Override
    public UIElement getBoundObject(Object... params) {
        if(objectCache != null){
            return objectCache;
        }

        if(params.length == 0 || !(params[0] instanceof UIElement)){
            return null;
        }

        UIElement invoker = (UIElement) params[0];
        return invoker.getElementFromRelativePath(objectRelativePath.getValue());
    }

    @Override
    public boolean isBindingValid() {
        return !objectRelativePath.getValue().isEmpty();
    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new UCRelativeUIElementBindingValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new UCRelativeUIElementBindingValueEditor((Property<AbstractUIElementBinding>) property);
    }

    public StringProperty getObjectRelativePathRaw() {
        return objectRelativePath;
    }

    @Override
    public String getDisplayValue() {
        return objectRelativePath.getValue();
    }
}
