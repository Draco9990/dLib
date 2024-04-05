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
        propertyEditorClass = CustomPropertyEditor.class;
    }

    //endregion

    //region Methods

    public abstract ArrayList<T> getAllOptions();

    //endregion
}
