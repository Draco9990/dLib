package dLib.properties.objects;

import dLib.properties.objects.templates.TDynamicProperty;
import dLib.properties.objects.templates.TEnumProperty;

import java.io.Serializable;
import java.util.ArrayList;

public class DynamicProperty<ItemType> extends TDynamicProperty<ItemType> implements Serializable {
    static final long serialVersionUID = 1L;

    public DynamicProperty(ItemType value, ArrayList<ItemType> choices) {
        super(value, choices);
    }
}
