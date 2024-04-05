package dLib.properties.objects;

import com.badlogic.gdx.graphics.Color;
import dLib.properties.ui.elements.ColorPropertyEditor;

public class ColorProperty extends Property<Color> {
    //region Variables
    //endregion

    //region Constructors

    public ColorProperty(Color value) {
        super(value);
        propertyEditorClass = ColorPropertyEditor.class;
    }

    //endregion

    //region Methods

    //endregion
}
