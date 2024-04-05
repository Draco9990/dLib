package dLib.util.settings.prefabs;

import dLib.propertyeditors.ui.elements.AbstractPropertyEditor;
import dLib.propertyeditors.ui.elements.StringPropertyEditor;
import dLib.util.settings.Property;

import java.io.Serializable;

public class StringProperty extends Property<String> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    //endregion

    //region Constructors

    public StringProperty(String defaultValue){
        super(defaultValue);
        propertyEditorClass = StringPropertyEditor.class;
    }

    //endregion

    //region Confirmation Mode

    //region Methods

    @Override
    public StringProperty setName(String newTitle) {
        return (StringProperty) super.setName(newTitle);
    }

    //endregion
}
