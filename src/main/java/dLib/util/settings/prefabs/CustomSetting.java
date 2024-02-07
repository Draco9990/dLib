package dLib.util.settings.prefabs;

import dLib.util.settings.Setting;

import java.io.Serializable;

public abstract class CustomSetting<T> extends Setting<T> implements Serializable {
    static final long serialVersionUID = 1L;

    /** Variables */

    /** Constructors */
    public CustomSetting(T defaultValue) {
        super(defaultValue);
    }

    /** Methods */
    public abstract void requestChange();
}
