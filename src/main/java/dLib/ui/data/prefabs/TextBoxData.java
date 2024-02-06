package dLib.ui.data.prefabs;

import dLib.ui.data.implementations.HoverableData;
import dLib.ui.elements.prefabs.TextBox;

public class TextBoxData extends HoverableData {
    public String text;

    public boolean wrap;

    public String horizontalAlignment;
    public String verticalAlignment;

    public float marginPercX;
    public float marginPercY;

    @Override
    public TextBox makeLiveInstance(Object... params) {
        return new TextBox(this);
    }
}
