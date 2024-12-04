package dLib.properties.objects.templates;

import dLib.properties.ui.elements.BooleanPropertyEditor;

import java.io.Serializable;

public abstract class TBooleanProperty<PropertyType> extends TProperty<Boolean, PropertyType> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables
    //endregion

    //region Constructors

    public TBooleanProperty(boolean defaultValue){
        super(defaultValue);

        propertyEditorClass = BooleanPropertyEditor.class;
    }

    //endregion

    //region Methods

    //region Value

    @Override
    public boolean setValueFromString(String value) {
        return setValue(Boolean.parseBoolean(value));
    }

    public void toggle(){
        setValue(!getValue());
    }

    //endregion

    //endregion
}
