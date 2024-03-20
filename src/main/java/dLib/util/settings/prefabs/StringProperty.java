package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractSettingUI;
import dLib.ui.elements.settings.StringSettingUI;
import dLib.util.settings.Property;

import java.io.Serializable;

public class StringProperty extends Property<String> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    //endregion

    //region Constructors

    public StringProperty(String defaultValue){
        super(defaultValue);
    }

    //endregion

    //region Confirmation Mode

    //region Methods

    @Override
    public StringProperty setName(String newTitle) {
        return (StringProperty) super.setName(newTitle);
    }

    public AbstractSettingUI makeEditUI(int xPos, int yPos, int width, int height) {
        return makeEditUI(xPos, yPos, width, height, EInputConfirmationMode.SELECTION_MANAGED);
    }

    public AbstractSettingUI makeEditUI(int xPos, int yPos, int width, int height, EInputConfirmationMode inputConfirmationMode) {
        return new StringSettingUI(this, xPos, yPos, width, height, inputConfirmationMode);
    }

    //endregion

    public enum EInputConfirmationMode {
        ON_TEXT_CHANGED,
        SELECTION_MANAGED
    }
}
