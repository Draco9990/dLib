package dLib.properties.ui.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import dLib.properties.objects.*;
import dLib.properties.objects.templates.TDynamicProperty;
import dLib.properties.objects.templates.TProperty;

public class ValueEditorManager {
    public static AbstractValueEditor makeEditorFor(Object object){
        if(object instanceof DynamicProperty){
            return new DynamicValueEditor((DynamicProperty) object);
        }

        Object value = object;
        if(object instanceof TProperty){
            if(((TProperty<?, ?>) object).hasCustomEditorOverride()){
                return new PropertyOverridenValueEditor(((TProperty<?, ?>) object));
            }

            value = ((TProperty) object).getValue();
        }

        if(IEditableValue.class.isAssignableFrom(value.getClass())){
            if(object instanceof TProperty) return ((IEditableValue) value).makeEditorFor(((TProperty) object));
            else return ((IEditableValue) value).makeEditorFor();
        }

        //return all manual editors
        if(value instanceof Boolean){
            if(object instanceof BooleanProperty) return new BooleanValueEditor(((BooleanProperty) object));
            else return new BooleanValueEditor(((Boolean) value));
        }
        else if(value instanceof Color){
            return new ColorValueEditor(Color.valueOf((String) value));
        }
        else if(value instanceof Enum){
            if(object instanceof EnumProperty) return new EnumValueEditor(((EnumProperty) object));
            else return new EnumValueEditor(((Enum) value));
        }
        else if(value instanceof Float){
            if(object instanceof FloatProperty) return new FloatValueEditor(((FloatProperty) object));
            else return new FloatValueEditor(((Float) value));
        }
        else if(value instanceof Integer){
            if(object instanceof IntegerProperty) return new IntegerValueEditor(((IntegerProperty) object));
            else return new IntegerValueEditor(((Integer) value));
        }
        else if(value instanceof String){
            if(object instanceof StringProperty) return new OnValueCommitedStringValueEditor(((StringProperty) object));
            else if(object instanceof ColorProperty) return new ColorValueEditor(((ColorProperty) object));
            else return new OnValueCommitedStringValueEditor(((String) value));
        }

        return null;
    }
}
