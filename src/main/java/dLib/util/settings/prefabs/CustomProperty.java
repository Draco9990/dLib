package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractSettingUI;
import dLib.ui.elements.settings.CustomSettingUI;
import dLib.util.settings.Property;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class CustomProperty<T> extends Property<T> implements Serializable {
    static final long serialVersionUID = 1L;

    /** Variables */

    /** Constructors */
    public CustomProperty(T defaultValue) {
        super(defaultValue);
    }

    /** Value */
    public abstract ArrayList<T> getAllOptions();

    /** UI */
    @Override
    public AbstractSettingUI makeUIForEdit(int xPos, int yPos, int width, int height) {
        return new CustomSettingUI<T>(this, xPos, yPos, width, height);
    }
}
