package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractUISetting;
import dLib.ui.elements.settings.StringUISetting;
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

    /** UI */
    @Override
    public AbstractUISetting makeUIFor(int xPos, int yPos, int width, int height) {
        return new StringUISetting(this, xPos, yPos, width, height);
    }
}
