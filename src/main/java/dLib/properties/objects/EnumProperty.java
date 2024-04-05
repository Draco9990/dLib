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

    public final void previous(){
        value = EnumHelpers.previousEnum(value);
    }

    public final void next(){
        value = EnumHelpers.nextEnum(value);
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
        return addOnValueChangedListener(new BiConsumer<Enum<T>, Enum<T>>() {
            @Override
            public void accept(Enum<T> tEnum, Enum<T> tEnum2) {
                listener.run();
            }
        });
    }

    //endregion

    //endregion
}
