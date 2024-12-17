package dLib.properties.ui.elements;

import dLib.util.ui.dimensions.AbstractDimension;

public interface IEditableValue {
    AbstractValueEditor makeEditorFor(AbstractDimension width, AbstractDimension height);
}
