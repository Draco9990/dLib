package dLib.properties.objects;

import dLib.properties.objects.templates.TArrayProperty;

import java.io.Serializable;
import java.util.ArrayList;

public class ArrayProperty<ArrayItemClass> extends TArrayProperty<ArrayItemClass, ArrayProperty<ArrayItemClass>> implements Serializable {
    static final long serialVersionUID = 1L;

    public ArrayProperty(ArrayList<ArrayItemClass> defaultValue) {
        super(defaultValue);
    }
}
