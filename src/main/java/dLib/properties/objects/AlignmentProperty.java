package dLib.properties.objects;

import dLib.properties.objects.templates.TAlignmentProperty;
import dLib.ui.Alignment;

import java.io.Serializable;

public class AlignmentProperty extends TAlignmentProperty<AlignmentProperty> implements Serializable {
    static final long serialVersionUID = 1L;

    public AlignmentProperty(Alignment value) {
        super(value);
    }

    public AlignmentProperty(Alignment.HorizontalAlignment horizontalAlignment, Alignment.VerticalAlignment verticalAlignment) {
        this(new Alignment(horizontalAlignment, verticalAlignment));
    }
}
