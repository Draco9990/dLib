package dLib.util.bindings.string;

import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.util.bindings.string.interfaces.ITextProvider;

public class StringSourceBinding extends AbstractStringBinding {

    private ITextProvider provider;

    public StringSourceBinding(ITextProvider provider){
        this.provider = provider;
    }

    @Override
    public String getBoundObject(Object... params) {
        return provider.getText();
    }

    @Override
    public String toString() {
        return "SRC";
    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        //return new StringStaticBindingValueEditor(this);
        //TODO
        throw new UnsupportedOperationException("This method is not implemented yet.");
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        // TODO
        throw new UnsupportedOperationException("This method is not implemented yet.");
    }
}
