package dLib.tools.screeneditor.ui.items.preview.composite;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import dLib.tools.screeneditor.ui.items.preview.CompositeUIPreviewItem;
import dLib.tools.screeneditor.ui.items.preview.UIPreviewItem;
import dLib.ui.data.CompositeUIElementData;
import dLib.ui.data.UIElementData;
import dLib.ui.data.prefabs.ListBoxData;
import dLib.ui.elements.prefabs.ListBox;
import dLib.ui.themes.UITheme;

public class ListBoxUIPreview extends CompositeUIPreviewItem {
    /** Constructors */
    public ListBoxUIPreview(){
        super(UITheme.whitePixel, 0, 0, 500, 500);
        setRenderColor(Color.LIGHT_GRAY);
    }

    public ListBoxUIPreview(Texture image, int xPos, int yPos, int width, int height) {
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
    public UIPreviewItem makeCopy() {
        return (UIPreviewItem) new ListBoxUIPreview(image, x, y, width, height).setRenderColor(getColorForRender());
    }
}
