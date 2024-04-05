package dLib.util.settings.prefabs;

import dLib.ui.elements.propertyeditors.AbstractPropertyEditor;
import dLib.ui.elements.propertyeditors.EnumPropertyEditor;
import dLib.util.EnumHelpers;
import dLib.util.settings.Property;

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

    public AbstractPropertyEditor makeEditUI(int xPos, int yPos, int width, int height) {
        return new EnumPropertyEditor(this, xPos, yPos, width, height);
    }

    //endregion
}
