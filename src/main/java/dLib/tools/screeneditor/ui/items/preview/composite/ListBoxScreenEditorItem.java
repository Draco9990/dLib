package dLib.tools.screeneditor.ui.items.preview.composite;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import dLib.tools.screeneditor.ui.items.preview.CompositeScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.data.prefabs.ListBoxData;
import dLib.ui.themes.UITheme;

public class ListBoxScreenEditorItem extends CompositeScreenEditorItem {
    /** Constructors */
    public ListBoxScreenEditorItem(){
        super(0, 0, 500, 500);
        setRenderColor(Color.LIGHT_GRAY);
    }

    public ListBoxScreenEditorItem(int xPos, int yPos, int width, int height) {
        super(xPos, yPos, width, height);
    }

    public ListBoxScreenEditorItem(ListBoxData<?> data){
        super(data);
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
        return (ScreenEditorItem) new ListBoxScreenEditorItem(x, y, width, height).setRenderColor(getColorForRender());
    }
}
