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

    /** Methods */
    public void sterilize(String text){

    }
}
