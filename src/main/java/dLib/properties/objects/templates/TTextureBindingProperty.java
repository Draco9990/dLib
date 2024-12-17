package dLib.properties.objects.templates;

import dLib.util.bindings.texture.TextureBindingHelpers;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureEmptyBinding;

import java.util.ArrayList;

public abstract class TTextureBindingProperty<PropertyType> extends TProperty<TextureBinding, PropertyType> {
    //region Variables
    //endregion

    //region Constructors

    public TTextureBindingProperty(TextureBinding value) {
        super(value);
    }

    public TTextureBindingProperty() {
        super(new TextureEmptyBinding());
    }

    //endregion

    //region Methods

    @Override
    public boolean setValueFromString(String value) {
        throw new UnsupportedOperationException("TextureBindingProperty does not support setting values from strings");
    }

    //endregion
}
