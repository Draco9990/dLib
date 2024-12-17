package dLib.properties.ui.elements;

import com.badlogic.gdx.graphics.Color;
import dLib.util.ui.dimensions.AbstractDimension;

public class ValueEditorManager {
    public static AbstractValueEditor makeEditorFor(Object value, AbstractDimension width, AbstractDimension height){
        if(IEditableValue.class.isAssignableFrom(value.getClass())){
            return ((IEditableValue) value).makeEditorFor(width, height);
        }

        //return all manual editors
        if(value.getClass().isArray()){
            return new ArrayValueEditor(value, width, height);
        }
        else if(value instanceof Boolean){
            return new BooleanValueEditor(((Boolean) value), width, height);
        }
        else if(value instanceof Color){
            return new ColorValueEditor(((Color) value), width, height);
        }
        else if(value instanceof Enum){
            return new EnumValueEditor(((Enum) value), width, height);
        }
        else if(value instanceof Float){
            return new FloatValueEditor(((Float) value), width, height);
        }
        else if(value instanceof Integer){
            return new IntegerValueEditor(((Integer) value), width, height);
        }
        else if(value instanceof String){
            return new OnValueCommitedStringValueEditor(((String) value), width, height);
        }

        return null;
    }
}
