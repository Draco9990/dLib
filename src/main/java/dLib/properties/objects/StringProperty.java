package dLib.properties.objects;

import dLib.properties.ui.elements.OnValueCommitedStringPropertyEditor;
import dLib.properties.ui.elements.StringPropertyEditor;

import java.io.Serializable;

public class StringProperty extends Property<String> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    //endregion

    //region Constructors

    public StringProperty(String defaultValue){
        super(defaultValue);
        propertyEditorClass = OnValueCommitedStringPropertyEditor.class;
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
