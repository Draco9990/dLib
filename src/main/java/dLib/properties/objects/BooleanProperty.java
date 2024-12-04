package dLib.properties.objects;

import dLib.properties.ui.elements.BooleanPropertyEditor;

import java.io.Serializable;
import java.util.function.BiConsumer;

public class BooleanProperty extends Property<Boolean> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables
    //endregion

    //region Constructors

    public BooleanProperty(boolean defaultValue){
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

    @Override
    public BooleanProperty setName(String newTitle) {
        return (BooleanProperty) super.setName(newTitle);
    }

    @Override
    public BooleanProperty setDescription(String description) {
        return (BooleanProperty) super.setDescription(description);
    }

    @Override
    public BooleanProperty addOnValueChangedListener(BiConsumer<Boolean, Boolean> listener) {
        return (BooleanProperty) super.addOnValueChangedListener(listener);
    }

    @Override
    public BooleanProperty addOnValueChangedListener(Runnable listener) {
        return (BooleanProperty) super.addOnValueChangedListener(listener);
    }

    //endregion
}
