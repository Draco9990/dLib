package dLib.properties.objects.templates;

import dLib.properties.ui.elements.CustomPropertyEditor;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class TCustomProperty<ItemClass, PropertyType> extends TProperty<ItemClass, PropertyType> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables
    //endregion

    //region Constructors

    public TCustomProperty(ItemClass defaultValue) {
        super(defaultValue);
        propertyEditorClass = CustomPropertyEditor.class;
    }

    //endregion

    //region Methods

    public abstract ArrayList<ItemClass> getAllOptions();

    //endregion
}
