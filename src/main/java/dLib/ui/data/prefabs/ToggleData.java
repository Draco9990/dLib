package dLib.ui.data.prefabs;

import dLib.ui.data.implementations.InteractableData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Toggle;

public class ToggleData extends InteractableData {
    public boolean isToggled;

    public String toggledTexturePath;
    public String toggledHoveredTexturePath;
    public String toggledDisabledTexturePath;

    @Override
    public Toggle makeLiveInstance() {
        return new Toggle(this);
    }
}
