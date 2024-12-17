package dLib.properties.objects.templates;

import com.badlogic.gdx.graphics.Color;
import dLib.properties.ui.elements.ColorValueEditor;

import java.io.Serializable;

public abstract class TColorProperty<PropertyType> extends TProperty<String, PropertyType> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables
    //endregion

    //region Constructors

    public TColorProperty(Color value) {
        super(value.toString());
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
