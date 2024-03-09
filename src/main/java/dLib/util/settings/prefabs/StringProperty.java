package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractSettingUI;
import dLib.ui.elements.settings.StringSettingUI;
import dLib.util.settings.Property;

import java.io.Serializable;

public class StringProperty extends Property<String> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    private InputConfirmationMode confirmationMode = InputConfirmationMode.ON_TEXT_CHANGED;

    //endregion

    //region Constructors

    public StringProperty(String defaultValue){
        super(defaultValue);
    }

    //endregion

    //region Confirmation Mode

    public StringProperty setConfirmationMode(InputConfirmationMode confirmationMode){
        this.confirmationMode = confirmationMode;

        return this;
    }

    public InputConfirmationMode getConfirmationMode(){
        return confirmationMode;
    }

    //region Methods

    @Override
    public StringProperty setName(String newTitle) {
        return (StringProperty) super.setName(newTitle);
    }

    @Override
    public AbstractSettingUI makeUIForEdit(int xPos, int yPos, int width, int height) {
        return new StringSettingUI(this, xPos, yPos, width, height);
    }

    //endregion

    public enum InputConfirmationMode{
        ON_TEXT_CHANGED,
        SELECTION_MANAGED
    }
}
