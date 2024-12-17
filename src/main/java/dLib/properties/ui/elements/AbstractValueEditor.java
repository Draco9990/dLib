package dLib.properties.ui.elements;

import dLib.properties.objects.Property;
import dLib.properties.objects.templates.TProperty;
import dLib.ui.elements.UIElement;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class AbstractValueEditor<ValueType> extends UIElement {
    //region Variables

    public Event<Consumer<ValueType>> onValueChangedEvent = new Event<>();

    public Event<Consumer<Object>> setValueEvent = new Event<>();

    protected TProperty<ValueType, ? extends TProperty> boundProperty;

    //endregion

    //region Constructors

    public AbstractValueEditor(AbstractDimension width, AbstractDimension height) {
        super(width, height);
    }

    public AbstractValueEditor(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);
    }


    //endregion
}
