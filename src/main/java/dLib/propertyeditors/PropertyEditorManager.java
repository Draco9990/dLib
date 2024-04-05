package dLib.propertyeditors;

import dLib.propertyeditors.ui.elements.AbstractPropertyEditor;
import dLib.util.settings.Property;

import java.util.HashMap;
import java.util.Map;

public class PropertyEditorManager {
    //region Variables
    private static Map<Class<Property>, AbstractPropertyEditor> propertyEditors = new HashMap<>();
    //endregion

    //region Methods

    public void registerPropertyEditor(AbstractPropertyEditor propertyEditor, Class<Property> propertyClass){
        propertyEditors.put(propertyClass, propertyEditor);
    }

    public AbstractPropertyEditor findEditorForProperty(Class<Property> propertyClass){
        return propertyEditors.get(propertyClass);
    }

    //endregion
}
