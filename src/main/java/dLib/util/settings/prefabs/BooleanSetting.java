package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractUISetting;
import dLib.ui.elements.settings.ToggleUISetting;
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

    /** UI */
    @Override
    public AbstractUISetting makeUIFor(int xPos, int yPos, int width, int height) {
        return new ToggleUISetting(this, xPos, yPos, width, height);
    }
}
