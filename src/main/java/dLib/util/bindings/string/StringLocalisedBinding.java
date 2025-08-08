package dLib.util.bindings.string;

import dLib.properties.objects.StringProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.OnValueCommitedStringValueEditor;

// TODO create!
public class StringLocalisedBinding extends AbstractStringBinding {

    private StringProperty value;

    public StringLocalisedBinding(String value){
        this.value = new StringProperty(value);
    }

    @Override
    public String resolve(Object... params) {
        return value.getValue();
    }

    @Override
    public String toString() {
        return "STATIC";
    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new OnValueCommitedStringValueEditor(value);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        throw new UnsupportedOperationException("Not implemented - Value is edited through makeEditorFor() method");
    }
}
