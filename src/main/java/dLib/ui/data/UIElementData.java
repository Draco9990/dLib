package dLib.ui.data;

import dLib.ui.elements.UIElement;
import dLib.util.settings.prefabs.StringSetting;

public abstract class UIElementData {
    public StringSetting name;

    public int x;
    public int y;

    public int width;
    public int height;

    public abstract UIElement makeLiveInstance(Object... params);
}
