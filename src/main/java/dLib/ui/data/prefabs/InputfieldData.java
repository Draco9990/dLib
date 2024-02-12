package dLib.ui.data.prefabs;

import dLib.ui.data.CompositeUIElementData;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.prefabs.Inputfield;

import java.io.Serializable;
import java.util.List;

public class InputfieldData extends CompositeUIElementData  implements Serializable {
    private static final long serialVersionUID = 1L;

    public ButtonData buttonData;
    public TextBoxData textboxData;

    public List<Character> characterFilter;
    public int characterLimit;

    @Override
    public CompositeUIElement makeLiveInstance(Object... params) {
        return new Inputfield(this);
    }
}
