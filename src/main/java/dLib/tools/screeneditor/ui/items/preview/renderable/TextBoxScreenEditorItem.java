package dLib.tools.screeneditor.ui.items.preview.renderable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.tools.screeneditor.ui.items.preview.RenderableScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.Alignment;
import dLib.ui.data.prefabs.TextBoxData;
import dLib.ui.elements.prefabs.TextBox;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureThemeBinding;
import dLib.util.settings.Setting;
import dLib.util.settings.prefabs.AlignmentSetting;
import dLib.util.settings.prefabs.StringSetting;

import java.util.ArrayList;

public class TextBoxScreenEditorItem extends RenderableScreenEditorItem {
    /** Variables */
    private TextBox textBox;

    /** Settings */
    private StringSetting sText = (StringSetting) new StringSetting(""){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            getElementData().text = getCurrentValue();
            textBox.setText(getCurrentValue());
        }
    }.setTitle("Text:");

    private AlignmentSetting sAlignment = (AlignmentSetting) new AlignmentSetting(new Alignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.CENTER)){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            getElementData().horizontalAlignment = getCurrentValue().horizontalAlignment.name();
            getElementData().verticalAlignment = getCurrentValue().verticalAlignment.name();
            textBox.setAlignment(getCurrentValue().horizontalAlignment, getCurrentValue().verticalAlignment);
        }
    }.setTitle("Alignment:");

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

    /** Position & Dimensions */
    @Override
    public ScreenEditorItem setPosition(Integer newPosX, Integer newPosY) {
        super.setPosition(newPosX, newPosY);
        this.textBox.setPosition(x, y);
        return this;
    }

    @Override
    public ScreenEditorItem setDimensions(Integer newWidth, Integer newHeight) {
        super.setDimensions(newWidth, newHeight);
        this.textBox.setDimensions(width, height);
        return this;
    }

    /** Update & render */
    @Override
    public void update() {
        super.update();

        textBox.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);

        textBox.render(sb);
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

    /** Properties */
    @Override
    public ArrayList<Setting<?>> getPropertiesForItem() {
        ArrayList<Setting<?>> settings = super.getPropertiesForItem();
        settings.add(sText);
        settings.add(sAlignment);
        return settings;
    }

    /** Copy */
    @Override
    public ScreenEditorItem makeCopy() {
        return new TextBoxScreenEditorItem(sTexture.getCurrentValue(), x, y, width, height);
    }
}
