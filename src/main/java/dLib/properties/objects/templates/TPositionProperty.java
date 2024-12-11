package dLib.properties.objects.templates;

import basemod.Pair;
import dLib.properties.ui.elements.PositionPropertyEditor;
import dLib.util.ui.position.AbstractPosition;

public class TPositionProperty<PropertyType> extends TProperty<Pair<AbstractPosition, AbstractPosition>, PropertyType>{
    public TPositionProperty(AbstractPosition xPos, AbstractPosition yPos) {
        super(new Pair<>(xPos, yPos));

        propertyEditorClass = PositionPropertyEditor.class;
    }

    public void setValue(AbstractPosition xPos, AbstractPosition yPos){
        setValue(new Pair<>(xPos, yPos));
    }

    public AbstractPosition getXPosition(){
        return getValue().getKey();
    }

    public AbstractPosition getYPosition(){
        return getValue().getValue();
    }

    public void setXPosition(AbstractPosition xPos){
        setValue(xPos, getYPosition());
    }

    public void setYPosition(AbstractPosition yPos){
        setValue(getXPosition(), yPos);
    }

    @Override
    public boolean setValueFromString(String value) {
        throw new UnsupportedOperationException("PositionProperty does not support setting value from string");
    }
}
