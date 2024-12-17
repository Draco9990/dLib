package dLib.properties.objects.templates;

import basemod.Pair;
import dLib.properties.ui.elements.DimensionPropertyEditor;
import dLib.util.ui.dimensions.AbstractDimension;

public class TDimensionProperty<PropertyType> extends TProperty<Pair<AbstractDimension, AbstractDimension>, PropertyType>{
    public TDimensionProperty(AbstractDimension xPos, AbstractDimension yPos) {
        super(new Pair<>(xPos, yPos));

        propertyEditorClass = DimensionPropertyEditor.class;
    }

    public void setValue(AbstractDimension xPos, AbstractDimension yPos){
        setValue(new Pair<>(xPos, yPos));
    }

    public AbstractDimension getWidth(){
        return getValue().getKey();
    }

    public AbstractDimension getHeight(){
        return getValue().getValue();
    }

    public void setWidth(AbstractDimension xPos){
        setValue(xPos, getHeight());
    }

    public void setHeight(AbstractDimension yPos){
        setValue(getWidth(), yPos);
    }

    @Override
    public boolean setValueFromString(String value) {
        throw new UnsupportedOperationException("PositionProperty does not support setting value from string");
    }
}
