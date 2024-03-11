package dLib.ui.data.prefabs;

import com.badlogic.gdx.graphics.Color;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.renderable.TextBoxScreenEditorItem;
import dLib.ui.Alignment;
import dLib.ui.data.implementations.HoverableData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.elements.prefabs.TextBox;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.method.NoneMethodBinding;

import java.io.Serializable;

public class TextBoxData extends Hoverable.HoverableData implements Serializable {
    private static final long serialVersionUID = 1L;

    public String text = "";

    public String textRenderColor = Color.WHITE.toString();
    //TODO FONT
    public boolean wrap;

    public String horizontalAlignment = Alignment.HorizontalAlignment.CENTER.name();
    public String verticalAlignment = Alignment.VerticalAlignment.CENTER.name();

    public float marginPercX = 0.07f;
    public float marginPercY = 0.33f;

    public MethodBinding onTextChanged = new NoneMethodBinding();

    public int paddingRight = 0;
    public int paddingTop = 0;
    public int paddingLeft = 0;
    public int paddingBottom = 0;

    @Override
    public UIElement makeUIElement() {
        return new TextBox(this);
    }
}
