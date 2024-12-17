package dLib.properties.ui.elements;

import dLib.properties.objects.templates.TProperty;
import dLib.util.ui.dimensions.AbstractDimension;

public interface IEditableValue {
    AbstractValueEditor makeEditorFor(AbstractDimension width, AbstractDimension height);
    AbstractValueEditor makeEditorFor(TProperty property, AbstractDimension width, AbstractDimension height);
}
