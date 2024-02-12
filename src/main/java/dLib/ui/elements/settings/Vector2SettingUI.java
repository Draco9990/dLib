package dLib.ui.elements.settings;

import dLib.util.settings.Setting;
import dLib.util.settings.prefabs.Vector2Setting;

public class Vector2SettingUI extends AbstractSettingUI{
    /** Variables */

    /** Constructors */
    public Vector2SettingUI(Vector2Setting setting, Integer xPos, Integer yPos, Integer width, Integer height) {
        super(setting, xPos, yPos, width, height);

        int startingX = (int) (xPos + width * (1-valuePercX));
    }
}
