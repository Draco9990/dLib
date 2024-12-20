package dLib.properties.ui.elements;

import dLib.properties.objects.templates.TProperty;
import dLib.ui.elements.UIElement;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;

public abstract class AbstractValueEditor<ValueType, PropertyType extends TProperty> extends UIElement {
    //region Variables

    public PropertyType boundProperty;

    //endregion

    //region Constructors

    public AbstractValueEditor(PropertyType property) {
        super(Dim.fill(), Dim.auto());
        this.boundProperty = property;
    }

    public AbstractValueEditor(PropertyType property, AbstractPosition xPos, AbstractPosition yPos) {
        super(xPos, yPos, Dim.fill(), Dim.auto());
        this.boundProperty = property;
    }


    //endregion

    //region Methods

    protected boolean isEditorValidForPropertyChange(){
        return boundProperty.getPreviousValue().getClass() == boundProperty.getValue().getClass();
    }

    //endregion
}
