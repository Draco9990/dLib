package dLib.properties.objects.templates;

import dLib.util.bindings.texture.AbstractTextureBinding;

public abstract class TTextureBindingProperty<PropertyType> extends TProperty<AbstractTextureBinding, PropertyType> {
    //region Variables
    //endregion

    //region Constructors

    public TTextureBindingProperty(AbstractTextureBinding value) {
        super(value);
    }

    //endregion

    //region Methods

    @Override
    public boolean setValueFromString(String value) {
        throw new UnsupportedOperationException("TextureBindingProperty does not support setting values from strings");
    }

    //endregion
}
