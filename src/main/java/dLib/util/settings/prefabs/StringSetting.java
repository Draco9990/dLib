package dLib.util.settings.prefabs;

import dLib.util.settings.Setting;

import java.io.Serializable;

public class StringSetting extends Setting<String> implements Serializable {
    static final long serialVersionUID = 1L;

    /** Variables */

    /** Constructors */
    public StringSetting(String defaultValue){
        super(defaultValue);
    }

    @Override
    public Setting<String> setCurrentValue(String currentValue) {
        sanitize(currentValue);
        return super.setCurrentValue(currentValue);
    }

    /** Methods */
    public void sanitize(String text){ }
}
