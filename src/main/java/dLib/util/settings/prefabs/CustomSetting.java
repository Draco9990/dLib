package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractUISetting;
import dLib.ui.elements.settings.CustomUISetting;
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

    public CustomSetting(Class<T> valClass) {
        super(valClass);
    }

    /** Value */
    public abstract ArrayList<T> getAllOptions();

    /** UI */
    @Override
    public AbstractUISetting makeUIFor(int xPos, int yPos, int width, int height) {
        return new CustomUISetting<T>(this, xPos, yPos, width, height);
    }
}
