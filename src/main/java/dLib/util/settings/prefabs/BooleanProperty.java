package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractSettingUI;
import dLib.ui.elements.settings.ToggleSettingUI;
import dLib.util.settings.Property;
import java.io.Serializable;

public class BooleanProperty extends Property<Boolean> implements Serializable {
    static final long serialVersionUID = 1L;

    /** Variables */

    /** Constructors */
    public BooleanProperty(boolean defaultValue){
        super(defaultValue);
    }

    /** Methods */
    public void toggle(){
        setValue_internal(!getValue());
    }

    /** Title */
    @Override
    public BooleanProperty setName(String newTitle) {
        return (BooleanProperty) super.setName(newTitle);
    }

    /** UI */
    @Override
    public AbstractSettingUI makeUIForEdit(int xPos, int yPos, int width, int height) {
        return new ToggleSettingUI(this, xPos, yPos, width, height);
    }
}
