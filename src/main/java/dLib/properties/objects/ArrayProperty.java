package dLib.properties.objects;

import dLib.properties.objects.templates.TProperty;

import java.util.ArrayList;

public class ArrayProperty<ItemType> extends TProperty<ArrayList<ItemType>, ArrayProperty<ItemType>> {
    public ArrayProperty(ArrayList<ItemType> value) {
        super(value);
    }

    @Override
    public boolean setValueFromString(String value) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    //TODO placeholder
}
