package dLib.tools.screeneditor.ui.items.preview.renderable;

import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.data.prefabs.ButtonData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Button;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureThemeBinding;

public class ButtonScreenEditorItem extends InteractableScreenEditorItem {
    /** Constructors */
    public ButtonScreenEditorItem(TextureBinding textureBinding, int xPos, int yPos, int width, int height) {
        super(textureBinding, xPos, yPos, width, height);
    }

    public ButtonScreenEditorItem(ButtonData data){
        super(data);
    }

    /** Data */
    @Override
    public ButtonData makeElementData() {
        return new ButtonData();
    }

    @Override
    public ButtonData getElementData() {
        return (ButtonData) super.getElementData();
    }

    /** Copy */
    public static ScreenEditorItem makeNewInstance(){
        return new ButtonScreenEditorItem(new TextureThemeBinding("button_small"), 0, 0, 75, 75);
    }

    @Override
    public Class<? extends UIElement> getLiveInstanceType() {
        return Button.class;
    }
}
