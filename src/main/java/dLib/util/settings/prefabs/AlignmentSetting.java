package dLib.util.settings.prefabs;

import dLib.ui.Alignment;
import dLib.ui.elements.settings.AbstractSettingUI;
import dLib.ui.elements.settings.AlignmentSettingUI;
import dLib.util.settings.Setting;

public class AlignmentSetting extends Setting<Alignment> {
    /** Constructors */
    public AlignmentSetting(Alignment value) {
        super(value);
    }

    /** UI */
    @Override
    public AbstractSettingUI makeUIFor(int xPos, int yPos, int width, int height) {
        return new AlignmentSettingUI(this, xPos, yPos, width, height);
    }
}
