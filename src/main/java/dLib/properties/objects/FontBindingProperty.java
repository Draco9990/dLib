package dLib.properties.objects;

import dLib.properties.objects.templates.TProperty;
import dLib.util.bindings.font.AbstractFontBinding;

public class FontBindingProperty extends TProperty<AbstractFontBinding, FontBindingProperty> {
    //region Variables
    //endregion

    //region Constructors

    public FontBindingProperty(AbstractFontBinding value) {
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
