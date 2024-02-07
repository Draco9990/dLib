package dLib.tools.screeneditor.ui.items.preview.composite;

import com.badlogic.gdx.graphics.Texture;
import dLib.tools.screeneditor.ui.items.preview.CompositeUIPreviewItem;
import dLib.tools.screeneditor.ui.items.preview.UIPreviewItem;
import dLib.ui.data.CompositeUIElementData;
import dLib.ui.data.UIElementData;
import dLib.ui.data.prefabs.InputfieldData;
import dLib.ui.themes.UIThemeManager;

public class InputfieldUIPreview extends CompositeUIPreviewItem {
    /** Constructors */
    public InputfieldUIPreview(){
        super(UIThemeManager.getDefaultTheme().inputfield, 0, 0, 500, 75);
    }

    public InputfieldUIPreview(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    /** Data */
    @Override
    public InputfieldData makeElementData() {
        return new InputfieldData();
    }

    @Override
    public InputfieldData getElementData() {
        return (InputfieldData) super.getElementData();
    }

    /** Copy */
    @Override
    public UIPreviewItem makeCopy() {
        return new InputfieldUIPreview(image, x, y, width, height);
    }
}
