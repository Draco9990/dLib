package dLib.properties.objects.templates;

import dLib.util.ui.position.AbstractPosition;

public class TPositionProperty<PropertyType> extends TProperty<AbstractPosition, PropertyType>{
    public TPositionProperty(AbstractPosition value) {
        super(value);
    }

    @Override
    public boolean setValueFromString(String value) {
        AbstractPosition newVal = getValue().cpy();
        newVal.setValueFromString(value);
        return setValue(newVal);
    }
}
