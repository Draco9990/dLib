package dLib.properties.ui.elements;

import dLib.properties.objects.Property;
import dLib.properties.objects.templates.TProperty;
import dLib.ui.elements.UIElement;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class AbstractValueEditor<ValueType, PropertyType extends TProperty> extends UIElement {
    //region Variables

    protected PropertyType boundProperty;

    //endregion

    //region Constructors

    public AbstractValueEditor(PropertyType property, AbstractDimension width, AbstractDimension height) {
        super(width, height);
        this.boundProperty = property;
    }

    public AbstractValueEditor(PropertyType property, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);
        this.boundProperty = property;
    }


    //endregion

    //region Methods

    protected boolean isEditorValidForPropertyChange(){
        return boundProperty.getPreviousValue().getClass() == boundProperty.getValue().getClass();
    }

    //endregion
}
