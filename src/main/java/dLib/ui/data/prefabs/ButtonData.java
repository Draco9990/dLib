package dLib.ui.data.prefabs;

import dLib.ui.data.implementations.InteractableData;
import dLib.ui.elements.prefabs.Button;

import java.io.Serializable;

public class ButtonData extends InteractableData  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public Button makeLiveInstance(Object... params) {
        return new Button(this);
    }
}
