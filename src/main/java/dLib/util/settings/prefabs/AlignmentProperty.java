package dLib.util.settings.prefabs;

import dLib.ui.Alignment;
import dLib.ui.elements.settings.AbstractSettingUI;
import dLib.ui.elements.settings.AlignmentSettingUI;
import dLib.util.settings.Property;

public class AlignmentProperty extends Property<Alignment> {
    /** Constructors */
    public AlignmentProperty(Alignment value) {
        super(value);
    }

    /** UI */
    @Override
    public AbstractSettingUI makeUIForEdit(int xPos, int yPos, int width, int height) {
        return new AlignmentSettingUI(this, xPos, yPos, width, height);
    }
}
