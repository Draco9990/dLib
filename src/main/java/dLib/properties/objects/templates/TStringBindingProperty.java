package dLib.properties.objects.templates;

import dLib.util.bindings.string.AbstractStringBinding;
import dLib.util.bindings.string.StringStaticBinding;

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

    //endregion
}
