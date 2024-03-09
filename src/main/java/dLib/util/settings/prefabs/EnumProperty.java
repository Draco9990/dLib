package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractSettingUI;
import dLib.ui.elements.settings.EnumSettingUI;
import dLib.util.EnumHelpers;
import dLib.util.settings.Property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class EnumProperty<T extends Enum<T>> extends Property<Enum<T>> implements Serializable {
    static final long serialVersionUID = 1L;

    /** Variables */
    EControlType controlType;

    /** Constructors */
    public EnumProperty(Enum<T> value, EControlType controlType) {
        super(value);

        this.controlType = controlType;
    }

    /** Methods */
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

    /** Control type*/
    public EControlType getControlType(){
        return controlType;
    }

    public enum EControlType{
        ARROWS,
        CLICK
    }

    /** UI */
    @Override
    public AbstractSettingUI makeUIForEdit(int xPos, int yPos, int width, int height) {
        return new EnumSettingUI(this, xPos, yPos, width, height);
    }
}
