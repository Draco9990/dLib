package dLib.properties.objects;

import dLib.properties.ui.elements.EnumPropertyEditor;
import dLib.util.EnumHelpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiConsumer;

public class EnumProperty<T extends Enum<T>> extends Property<Enum<T>> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    //endregion

    //region Constructors

    public EnumProperty(Enum<T> value) {
        super(value);

        propertyEditorClass = EnumPropertyEditor.class;
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

    public Property<Enum<T>> addOnValueChangedListener(Runnable listener){
        return addOnValueChangedListener((tEnum, tEnum2) -> listener.run());
    }

    @Override
    public EnumProperty<T> setName(String newTitle) {
        return (EnumProperty<T>) super.setName(newTitle);
    }

    @Override
    public EnumProperty<T> setDescription(String description) {
        return (EnumProperty<T>) super.setDescription(description);
    }

    //endregion

    //endregion
}
