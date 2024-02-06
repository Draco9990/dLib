package dLib.ui.data.prefabs;

import dLib.ui.data.CompositeUIElementData;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.elements.prefabs.TextButton;

public class TextButtonData extends CompositeUIElementData {
    public ButtonData buttonData;
    public TextBoxData textBoxData;

    @Override
    public TextButton makeLiveInstance() {
        return new TextButton(this);
    }
}
