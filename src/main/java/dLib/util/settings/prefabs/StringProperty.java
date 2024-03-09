package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractSettingUI;
import dLib.ui.elements.settings.StringSettingUI;
import dLib.util.settings.Property;

import java.io.Serializable;

public class StringProperty extends Property<String> implements Serializable {
    static final long serialVersionUID = 1L;

    /** Variables */
    private InputConfirmationMode confirmationMode = InputConfirmationMode.ON_TEXT_CHANGED;

    /** Constructors */
    public StringProperty(String defaultValue){
        super(defaultValue);
    }

    /** UI */
    @Override
    public AbstractSettingUI makeUIForEdit(int xPos, int yPos, int width, int height) {
        return new StringSettingUI(this, xPos, yPos, width, height);
    }

    /** Title */
    @Override
    public StringProperty setName(String newTitle) {
        return (StringProperty) super.setName(newTitle);
    }

    /** Input confirmation mode */
    public StringProperty setConfirmationMode(InputConfirmationMode confirmationMode){
        this.confirmationMode = confirmationMode;

        return this;
    }

    public InputConfirmationMode getConfirmationMode(){
        return confirmationMode;
    }

    public enum InputConfirmationMode{
        ON_TEXT_CHANGED,
        SELECTION_MANAGED
    }
}
