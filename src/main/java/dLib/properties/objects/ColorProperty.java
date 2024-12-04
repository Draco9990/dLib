package dLib.properties.objects;

import com.badlogic.gdx.graphics.Color;
import dLib.properties.ui.elements.ColorPropertyEditor;

public class ColorProperty extends Property<String> {
    //region Variables
    //endregion

    //region Constructors

    public ColorProperty(Color value) {
        super(value.toString());
        propertyEditorClass = ColorPropertyEditor.class;
    }

    //endregion

    //region Methods

    @Override
    public boolean setValueFromString(String value) {
        return setValue(value);
    }

    public void setColorValue(Color value){
        setValue(value.toString());
    }

    public Color getColorValue(){
        return Color.valueOf(getValue());
    }

    //endregion
}
