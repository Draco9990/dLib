package dLib.properties.objects;

import com.badlogic.gdx.math.Vector2;
import dLib.properties.objects.templates.TFloatVector2Property;

import java.io.Serializable;

public class FloatVector2Property extends TFloatVector2Property<FloatVector2Property> implements Serializable {
    static final long serialVersionUID = 1L;

    public FloatVector2Property(Vector2 value) {
        super(value);
    }
}
