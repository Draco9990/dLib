package dLib.tools.screeneditor.ui.items.preview.composite;

import com.badlogic.gdx.graphics.Texture;
import dLib.tools.screeneditor.ui.items.preview.CompositeScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.data.prefabs.InputfieldData;
import dLib.ui.themes.UIThemeManager;

public class InputfieldScreenEditorItem extends CompositeScreenEditorItem {
    /** Constructors */
    public InputfieldScreenEditorItem(){
        super(UIThemeManager.getDefaultTheme().inputfield, 0, 0, 500, 75);
    }

    public InputfieldScreenEditorItem(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    public InputfieldScreenEditorItem(InputfieldData data){
        super(data);
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
    public ScreenEditorItem makeCopy() {
        return new InputfieldScreenEditorItem(image, x, y, width, height);
    }
}
