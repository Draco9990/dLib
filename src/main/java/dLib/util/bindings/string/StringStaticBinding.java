package dLib.util.bindings.string;

import dLib.properties.objects.StringBindingProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.util.bindings.string.editors.StringStaticBindingValueEditor;

public class StringStaticBinding extends AbstractStringBinding {

    private String value;

    public StringStaticBinding(String value){
        this.value = value;
    }

    @Override
    public String resolve(Object... params) {
        return value;
    }

    @Override
    public String toString() {
        return "STATIC";
    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new StringStaticBindingValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new StringStaticBindingValueEditor((StringBindingProperty) property);
    }
}
