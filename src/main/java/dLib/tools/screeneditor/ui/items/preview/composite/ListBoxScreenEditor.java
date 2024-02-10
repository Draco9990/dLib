package dLib.tools.screeneditor.ui.items.preview.composite;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import dLib.tools.screeneditor.ui.items.preview.CompositeScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.data.prefabs.ListBoxData;
import dLib.ui.themes.UITheme;

public class ListBoxScreenEditor extends CompositeScreenEditorItem {
    /** Constructors */
    public ListBoxScreenEditor(){
        super(UITheme.whitePixel, 0, 0, 500, 500);
        setRenderColor(Color.LIGHT_GRAY);
    }

    public ListBoxScreenEditor(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    /** Data */
    @Override
    public ListBoxData makeElementData() {
        return null;//return new ListBoxData<>()
    }

    @Override
    public ListBoxData getElementData() {
        return (ListBoxData) super.getElementData();
    }

    /** Copy */
    @Override
    public ScreenEditorItem makeCopy() {
        return (ScreenEditorItem) new ListBoxScreenEditor(image, x, y, width, height).setRenderColor(getColorForRender());
    }
}
