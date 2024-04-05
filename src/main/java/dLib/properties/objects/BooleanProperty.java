package dLib.properties.objects;

import dLib.properties.ui.elements.BooleanPropertyEditor;

import java.io.Serializable;

public class BooleanProperty extends Property<Boolean> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables
    //endregion

    //region Constructors

    public BooleanProperty(boolean defaultValue){
        super(defaultValue);

        propertyEditorClass = BooleanPropertyEditor.class;
    }

    //endregion

    //region Methods

    //region Value

    public void toggle(){
        setValue(!getValue());
    }

    //endregion

    @Override
    public BooleanProperty setName(String newTitle) {
        return (BooleanProperty) super.setName(newTitle);
    }

    //endregion
}
