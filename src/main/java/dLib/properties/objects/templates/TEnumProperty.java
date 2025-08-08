package dLib.properties.objects.templates;

import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.EnumValueEditor;
import dLib.util.helpers.EnumHelpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class TEnumProperty<T extends Enum<T>, PropertyType> extends TProperty<Enum<T>, PropertyType> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    //endregion

    //region Constructors

    public TEnumProperty(Enum<T> value) {
        super(value);
    }

    //endregion

    //region Methods

    //region Values

    @Override
    public boolean setValueFromString(String value) {
        try {
            this.value = Enum.valueOf(this.value.getDeclaringClass(), value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String valueToString() {
        return value.toString();
    }

    public final void previous(){
        setValue(EnumHelpers.previousEnum(value));
    }

    public final void next(){
        setValue(EnumHelpers.nextEnum(value));
    }

    public final ArrayList<T> getAllPossibleValues(){
        return new ArrayList<>(Arrays.asList(value.getDeclaringClass().getEnumConstants()));
    }

    @Override
    public T getValue() {
        return (T) super.getValue();
    }

    @Override
    public String getValueForDisplay() {
        return super.getValueForDisplay().replace("_", " ");
    }

    //endregion

    //endregion
}
