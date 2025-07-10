package dLib.properties.objects.templates;

import dLib.util.bindings.string.AbstractStringBinding;
import dLib.util.bindings.string.StringStaticBinding;
import dLib.util.bindings.texture.AbstractTextureBinding;

public abstract class TStringBindingProperty<PropertyType> extends TProperty<AbstractStringBinding, PropertyType> {
    //region Variables
    //endregion

    //region Constructors

    public TStringBindingProperty(AbstractStringBinding value) {
        super(value);
    }

    public TStringBindingProperty() {
        this(new StringStaticBinding(""));
    }

    //endregion

    //region Methods

    @Override
    public boolean setValueFromString(String value) {
        throw new UnsupportedOperationException("StringBindingProperty does not support setting values from strings");
    }

    //endregion
}
