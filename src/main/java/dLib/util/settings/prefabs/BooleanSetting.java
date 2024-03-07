package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractSettingUI;
import dLib.ui.elements.settings.ToggleSettingUI;
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
        setCurrentValue(!getCurrentValue());
    }

    /** Title */
    @Override
    public BooleanSetting setTitle(String newTitle) {
        return (BooleanSetting) super.setTitle(newTitle);
    }

    /** UI */
    @Override
    public AbstractSettingUI makeUIFor(int xPos, int yPos, int width, int height) {
        return new ToggleSettingUI(this, xPos, yPos, width, height);
    }
}
