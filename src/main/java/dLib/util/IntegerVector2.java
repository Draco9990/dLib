package dLib.util;

import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.IEditableValue;

import java.io.Serializable;
import java.util.Objects;

public class IntegerVector2 implements Serializable {
    static final long serialVersionUID = 1L;

    public Integer x;
    public Integer y;

    public IntegerVector2(Integer xIn, Integer yIn){
        this.x = xIn;
        this.y = yIn;
    }

    public IntegerVector2(IntegerVector2 copy){
        this.x = copy.x;
        this.y = copy.y;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof IntegerVector2)) return false;

        IntegerVector2 other = (IntegerVector2) obj;

        return Objects.equals(other.x, x) && Objects.equals(other.y, y);
    }

    public IntegerVector2 copy(){
        return new IntegerVector2(x, y);
    }
}
