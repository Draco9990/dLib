package dLib.ui.data.prefabs;

import dLib.ui.data.CompositeUIElementData;
import dLib.ui.elements.prefabs.TextButton;

public class TextButtonData extends CompositeUIElementData {
    public ButtonData buttonData;
    public TextBoxData textBoxData;

    @Override
    public TextButton makeLiveInstance(Object... params) {
        return new TextButton(this);
    }
}
