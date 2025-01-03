package dLib.properties.objects;

import com.badlogic.gdx.graphics.Color;
import dLib.properties.objects.templates.TColorProperty;

import java.io.Serializable;

public class ColorProperty extends TColorProperty<ColorProperty> implements Serializable {
    static final long serialVersionUID = 1L;

    public ColorProperty(Color value) {
        super(value);
    }

    public void setValue(Color value) {
        setValue(value.toString());
    }
}
