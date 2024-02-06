package dLib.ui.data.prefabs;

import dLib.ui.data.UIElementData;
import dLib.ui.data.implementations.InteractableData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Button;

public class ButtonData extends InteractableData {
    @Override
    public Button makeLiveInstance() {
        return new Button(this);
    }
}
