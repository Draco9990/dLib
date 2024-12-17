package dLib.properties.ui.elements;

import com.badlogic.gdx.graphics.Color;
import dLib.properties.objects.*;
import dLib.properties.objects.templates.TProperty;
import dLib.util.ui.dimensions.AbstractDimension;

public class ValueEditorManager {
    public static AbstractValueEditor makeEditorFor(Object someValue, AbstractDimension width, AbstractDimension height){
        Object value = someValue;
        if(someValue instanceof TProperty){
            value = ((TProperty) someValue).getValue();
        }

        if(IEditableValue.class.isAssignableFrom(value.getClass())){
            return ((IEditableValue) value).makeEditorFor(width, height);
        }

        //return all manual editors
        if(value instanceof Boolean){
            if(someValue instanceof BooleanProperty) return new BooleanValueEditor(((BooleanProperty) someValue), width, height);
            else return new BooleanValueEditor(((Boolean) value), width, height);
        }
        else if(value instanceof Color){
            if(someValue instanceof ColorProperty) return new ColorValueEditor(((ColorProperty) someValue), width, height);
            else return new ColorValueEditor(((Color) value), width, height);
        }
        else if(value instanceof Enum){
            if(someValue instanceof EnumProperty) return new EnumValueEditor(((EnumProperty) someValue), width, height);
            else return new EnumValueEditor(((Enum) value), width, height);
        }
        else if(value instanceof Float){
            if(someValue instanceof FloatProperty) return new FloatValueEditor(((FloatProperty) someValue), width, height);
            else return new FloatValueEditor(((Float) value), width, height);
        }
        else if(value instanceof Integer){
            if(someValue instanceof IntegerProperty) return new IntegerValueEditor(((IntegerProperty) someValue), width, height);
            else return new IntegerValueEditor(((Integer) value), width, height);
        }
        else if(value instanceof String){
            if(someValue instanceof StringProperty) return new OnValueCommitedStringValueEditor(((StringProperty) someValue), width, height);
            else return new OnValueCommitedStringValueEditor(((String) value), width, height);
        }

        return null;
    }
}
