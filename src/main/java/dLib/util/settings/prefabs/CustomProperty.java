package dLib.util.settings.prefabs;

import dLib.propertyeditors.ui.elements.AbstractPropertyEditor;
import dLib.propertyeditors.ui.elements.CustomPropertyEditor;
import dLib.util.settings.Property;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class CustomProperty<T> extends Property<T> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables
    //endregion

    //region Constructors

    public CustomProperty(T defaultValue) {
        super(defaultValue);
    }

    //endregion

    //region Methods

    public abstract ArrayList<T> getAllOptions();

    public AbstractPropertyEditor makeEditUI(int xPos, int yPos, int width, int height) {
        return new CustomPropertyEditor<T>(this, xPos, yPos, width, height);
    }

    //endregion
}
