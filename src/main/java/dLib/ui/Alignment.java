package dLib.ui;

import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.AlignmentValueEditor;
import dLib.properties.ui.elements.IEditableValue;
import dLib.util.ui.dimensions.AbstractDimension;

public class Alignment implements IEditableValue {
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
    public AbstractValueEditor makeEditorFor(AbstractDimension width, AbstractDimension height) {
        return new AlignmentValueEditor(this, width, height);
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
