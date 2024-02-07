package dLib.util.settings.prefabs;

import dLib.util.EnumHelpers;
import dLib.util.settings.Setting;

import java.io.Serializable;

public class EnumSetting<T extends Enum<T>> extends Setting<Enum<T>> implements Serializable {
    static final long serialVersionUID = 1L;

    /** Variables */

    /** Constructors */
    public EnumSetting(Enum<T> value) {
        super(value);
    }

    /** Methods */
    public final void previous(){
        currentValue = EnumHelpers.previousEnum(currentValue);
    }

    public final void next(){
        currentValue = EnumHelpers.nextEnum(currentValue);
    }

    @Override
    public String getValueForDisplay() {
        return super.getValueForDisplay().replace("_", " ");
    }
}
