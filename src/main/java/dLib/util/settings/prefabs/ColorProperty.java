package dLib.util.settings.prefabs;

import com.badlogic.gdx.graphics.Color;
import dLib.propertyeditors.ui.elements.AbstractPropertyEditor;
import dLib.propertyeditors.ui.elements.ColorPropertyEditor;
import dLib.util.settings.Property;

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
