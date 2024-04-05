package dLib.util.settings.prefabs;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.elements.settings.AbstractPropertyEditor;
import dLib.ui.elements.settings.ColorPropertyEditor;
import dLib.util.settings.Property;

public class ColorProperty extends Property<Color> {
    //region Variables
    //endregion

    //region Constructors

    public ColorProperty(Color value) {
        super(value);
    }

    //endregion

    //region Methods

    public AbstractPropertyEditor makeEditUI(int xPos, int yPos, int width, int height) {
        return new ColorPropertyEditor(this, xPos, yPos, width, height);
    }

    //endregion
}
