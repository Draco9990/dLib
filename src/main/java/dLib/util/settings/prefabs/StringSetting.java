package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractSettingUI;
import dLib.ui.elements.settings.StringSettingUI;
import dLib.util.settings.Setting;

import java.io.Serializable;

public class StringSetting extends Setting<String> implements Serializable {
    static final long serialVersionUID = 1L;

    /** Variables */
    private InputConfirmationMode confirmationMode = InputConfirmationMode.ON_TEXT_CHANGED;

    /** Constructors */
    public StringSetting(String defaultValue){
        super(defaultValue);
    }

    /** UI */
    @Override
    public AbstractSettingUI makeUIFor(int xPos, int yPos, int width, int height) {
        return new StringSettingUI(this, xPos, yPos, width, height);
    }

    /** Title */
    @Override
    public StringSetting setTitle(String newTitle) {
        return (StringSetting) super.setTitle(newTitle);
    }

    /** Input confirmation mode */
    public StringSetting setConfirmationMode(InputConfirmationMode confirmationMode){
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
