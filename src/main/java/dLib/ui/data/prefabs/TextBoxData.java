package dLib.ui.data.prefabs;

import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.renderable.TextBoxScreenEditorItem;
import dLib.ui.data.implementations.HoverableData;
import dLib.ui.elements.prefabs.TextBox;

import java.io.Serializable;

public class TextBoxData extends HoverableData  implements Serializable {
    private static final long serialVersionUID = 1L;

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

    @Override
    public ScreenEditorItem makeEditorInstance() {
        return new TextBoxScreenEditorItem(this);
    }
}
