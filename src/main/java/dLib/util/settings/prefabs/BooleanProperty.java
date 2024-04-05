package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractPropertyEditor;
import dLib.ui.elements.settings.TogglePropertyEditor;
import dLib.util.settings.Property;
import java.io.Serializable;

public class BooleanProperty extends Property<Boolean> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables
    //endregion

    //region Constructors

    public BooleanProperty(boolean defaultValue){
        super(defaultValue);
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

    public AbstractPropertyEditor makeEditUI(int xPos, int yPos, int width, int height) {
        return new TogglePropertyEditor(this, xPos, yPos, width, height);
    }

    //endregion
}
