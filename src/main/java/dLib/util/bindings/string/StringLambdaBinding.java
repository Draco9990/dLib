package dLib.util.bindings.string;

import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;

import java.util.function.Supplier;

//! Warning, not serializable
public class StringLambdaBinding extends AbstractStringBinding {

    private Supplier<String> value;

    public StringLambdaBinding(Supplier<String> value){
        this.value = value;
    }

    @Override
    public String getBoundObject(Object... params) {
        return value.get();
    }

    @Override
    public String toString() {
        return "SUPPLIER";
    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        throw new UnsupportedOperationException("This binding does not support editing directly.");
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        throw new UnsupportedOperationException("This binding does not support editing directly.");
    }
}
