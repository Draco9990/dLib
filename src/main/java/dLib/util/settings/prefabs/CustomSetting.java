package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractSettingUI;
import dLib.ui.elements.settings.CustomSettingUI;
import dLib.util.settings.Setting;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class CustomSetting<T> extends Setting<T> implements Serializable {
    static final long serialVersionUID = 1L;

    /** Variables */

    /** Constructors */
    public CustomSetting(T defaultValue) {
        super(defaultValue);
    }

    /** Value */
    public abstract ArrayList<T> getAllOptions();

    /** UI */
    @Override
    public AbstractSettingUI makeUIFor(int xPos, int yPos, int width, int height) {
        return new CustomSettingUI<T>(this, xPos, yPos, width, height);
    }
}
