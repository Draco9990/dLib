package dLib.ui.data.prefabs;

import dLib.ui.data.implementations.InteractableData;
import dLib.ui.elements.prefabs.Button;

public class ButtonData extends InteractableData {
    @Override
    public Button makeLiveInstance(Object... params) {
        return new Button(this);
    }
}
