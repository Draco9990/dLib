package dLib.util.settings.prefabs;

import dLib.util.settings.Setting;
import java.io.Serializable;

public class BooleanSetting extends Setting<Boolean> implements Serializable {
    static final long serialVersionUID = 1L;

    /** Variables */

    /** Constructors */
    public BooleanSetting(boolean defaultValue){
        super(defaultValue);
    }

    /** Methods */
    public void toggle(){
        currentValue = !currentValue;
    }
}
