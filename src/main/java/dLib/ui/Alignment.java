package dLib.ui;

import dLib.properties.objects.AlignmentProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.AlignmentValueEditor;
import dLib.properties.ui.elements.IEditableValue;

import java.io.Serializable;

public class Alignment implements IEditableValue, Serializable {
    private static final long serialVersionUID = 1L;

    public HorizontalAlignment horizontalAlignment;
    public VerticalAlignment verticalAlignment;

    public Alignment(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment){
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
    }

    public Alignment(Alignment copy){
        this.horizontalAlignment = copy.horizontalAlignment;
        this.verticalAlignment = copy.verticalAlignment;
    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new AlignmentValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new AlignmentValueEditor((AlignmentProperty) property);
    }

    public enum AlignmentType {
        HORIZONTAL,
        VERTICAL
    }

    public enum HorizontalAlignment {
        LEFT,
        CENTER,
        RIGHT
    }

    public enum VerticalAlignment {
        BOTTOM,
        CENTER,
        TOP
    }

    @Override
    public String toString() {
        return "[" + horizontalAlignment + ", " + verticalAlignment + "]";
    }
}
