package dLib.util.settings.prefabs;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.settings.AbstractSettingUI;
import dLib.ui.elements.settings.ColorSettingUI;
import dLib.util.settings.Setting;

public class ColorSetting extends Setting<Color> {
    /** Constructors */
    public ColorSetting(Color value) {
        super(value);
    }

    /** UI */
    @Override
    public AbstractSettingUI makeUIFor(int xPos, int yPos, int width, int height) {
        return new ColorSettingUI(this, xPos, yPos, width, height);
    }
}
