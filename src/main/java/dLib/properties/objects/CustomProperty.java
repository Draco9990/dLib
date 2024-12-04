package dLib.properties.objects;

import dLib.properties.ui.elements.CustomPropertyEditor;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class CustomProperty<ItemClass> extends Property<ItemClass> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables
    //endregion

    //region Constructors

    public CustomProperty(ItemClass defaultValue) {
        super(defaultValue);
        propertyEditorClass = CustomPropertyEditor.class;
    }

    //endregion

    //region Methods

    public abstract ArrayList<ItemClass> getAllOptions();

    //endregion
}
