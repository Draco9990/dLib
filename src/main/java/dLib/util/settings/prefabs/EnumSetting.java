package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractUISetting;
import dLib.ui.elements.settings.EnumUISetting;
import dLib.util.EnumHelpers;
import dLib.util.settings.Setting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class EnumSetting<T extends Enum<T>> extends Setting<Enum<T>> implements Serializable {
    static final long serialVersionUID = 1L;

    /** Variables */
    EControlType controlType;

    /** Constructors */
    public EnumSetting(Enum<T> value, EControlType controlType) {
        super(value);

        this.controlType = controlType;
    }

    /** Methods */
    public final void previous(){
        currentValue = EnumHelpers.previousEnum(currentValue);
    }

    public final void next(){
        currentValue = EnumHelpers.nextEnum(currentValue);
    }

    public final ArrayList<T> getAllPossibleValues(){
        return new ArrayList<>(Arrays.asList(currentValue.getDeclaringClass().getEnumConstants()));
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
    public AbstractUISetting makeUIFor(int xPos, int yPos, int width, int height) {
        return new EnumUISetting(this, xPos, yPos, width, height);
    }
}
