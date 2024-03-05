package dLib.tools.screeneditor.ui.items.preview.renderable;

import com.badlogic.gdx.graphics.Color;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.screens.preview.ScreenEditorPreviewScreen;
import dLib.tools.screeneditor.ui.items.preview.RenderableScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.data.UIElementData;
import dLib.ui.data.prefabs.ButtonData;
import dLib.ui.themes.UITheme;
import dLib.util.Reflection;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureEmptyBinding;
import dLib.util.bindings.texture.TextureThemeBinding;
import dLib.util.settings.Setting;
import dLib.util.settings.prefabs.MethodSetting;

import java.util.ArrayList;

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
}
