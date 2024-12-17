package dLib.properties.ui.elements;

import dLib.properties.objects.templates.TProperty;

public interface IEditableValue {
    AbstractValueEditor makeEditorFor();
    AbstractValueEditor makeEditorFor(TProperty property);
}
