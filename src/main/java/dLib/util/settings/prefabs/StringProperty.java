package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractPropertyEditor;
import dLib.ui.elements.settings.StringPropertyEditor;
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

    public AbstractPropertyEditor makeEditUI(int xPos, int yPos, int width, int height) {
        return new StringPropertyEditor(this, xPos, yPos, width, height);
    }

    //endregion
}
