package dLib.properties.objects;

import dLib.properties.objects.templates.TIntegerVector2Property;
import dLib.util.IntegerVector2;

import java.io.Serializable;

public class IntegerVector2Property extends TIntegerVector2Property<IntegerVector2Property> implements Serializable {
    static final long serialVersionUID = 1L;

    public IntegerVector2Property(IntegerVector2 value) {
        super(value);
    }
}
