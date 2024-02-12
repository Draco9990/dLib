package dLib.tools.screeneditor.ui.items.preview.renderable;

import dLib.tools.screeneditor.ui.items.preview.RenderableScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.data.prefabs.TextBoxData;
import dLib.ui.elements.prefabs.TextBox;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureThemeBinding;
import dLib.util.settings.prefabs.StringSetting;

public class TextBoxScreenEditorItem extends RenderableScreenEditorItem {
    /** Variables */
    private TextBox textBox;

    /** Settings */
    private StringSetting sText = (StringSetting) new StringSetting(""){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            getElementData().text = getCurrentValue();
        }
    }.setTitle("Text:");

    /** Constructors */
    public TextBoxScreenEditorItem(){
        this(new TextureThemeBinding("button_large_outline_empty"), 0, 0, 300, 75);
    }

    public TextBoxScreenEditorItem(TextureBinding textureBinding, int xPos, int yPos, int width, int height) {
        super(textureBinding, xPos, yPos, width, height);

        textBox = new TextBox("", xPos, yPos, width, height);
    }

    /** Text */
    public void setText(String text){
        this.textBox.setText(text);
        getElementData().text = text;
    }

    /** Data */
    @Override
    public TextBoxData makeElementData() {
        return new TextBoxData();
    }

    @Override
    public TextBoxData getElementData() {
        return (TextBoxData) super.getElementData();
    }

    /** Copy */
    @Override
    public ScreenEditorItem makeCopy() {
        return new TextBoxScreenEditorItem(sTexture.getCurrentValue(), x, y, width, height);
    }
}
