package dLib.tools.screeneditor.ui.items.preview.renderable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.tools.screeneditor.ui.items.preview.RenderableScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.Alignment;
import dLib.ui.data.UIElementData;
import dLib.ui.data.prefabs.TextBoxData;
import dLib.ui.elements.prefabs.TextBox;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureEmptyBinding;
import dLib.util.bindings.texture.TextureThemeBinding;
import dLib.util.settings.Setting;
import dLib.util.settings.prefabs.AlignmentSetting;
import dLib.util.settings.prefabs.BooleanSetting;
import dLib.util.settings.prefabs.ColorSetting;
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

    private ColorSetting sTextColor = (ColorSetting) new ColorSetting(Color.BLACK){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            getElementData().textColor = getCurrentValue().toString();
            textBox.setTextRenderColor(getCurrentValue());
        }
    }.setTitle("Text Color:");

    private AlignmentSetting sAlignment = (AlignmentSetting) new AlignmentSetting(new Alignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.CENTER)){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            getElementData().horizontalAlignment = getCurrentValue().horizontalAlignment.name();
            getElementData().verticalAlignment = getCurrentValue().verticalAlignment.name();
            textBox.setAlignment(getCurrentValue().horizontalAlignment, getCurrentValue().verticalAlignment);
        }
    }.setTitle("Alignment:");

    private BooleanSetting sWrap = (BooleanSetting) new BooleanSetting(false){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            getElementData().wrap = getCurrentValue();
            textBox.setWrap(getCurrentValue());
        }
    }.setTitle("Wrap:");

    /** Constructors */
    public TextBoxScreenEditorItem(){
        this(new TextureEmptyBinding(), 0, 0, 300, 75);
    }

    public TextBoxScreenEditorItem(TextureBinding textureBinding, int xPos, int yPos, int width, int height) {
        super(textureBinding, xPos, yPos, width, height);

        textBox = new TextBox("TEXT", xPos, yPos, width, height);
    }

    public TextBoxScreenEditorItem(TextBoxData data){
        super(data);

        textBox = new TextBox(data.text, data.x, data.y, data.width, data.height);

        sText.setCurrentValue(data.text);
        sTextColor.setCurrentValue(Color.valueOf(data.textColor));

        sAlignment.setCurrentValue(new Alignment(Alignment.HorizontalAlignment.valueOf(data.horizontalAlignment), Alignment.VerticalAlignment.valueOf(data.verticalAlignment)));
        sWrap.setCurrentValue(data.wrap);
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
    public void initializeElementData(UIElementData data) {
        super.initializeElementData(data);
        TextBoxData textBoxData = (TextBoxData) data;
        textBoxData.text = sText.getCurrentValue();
        textBoxData.horizontalAlignment = sAlignment.getCurrentValue().horizontalAlignment.name();
        textBoxData.verticalAlignment = sAlignment.getCurrentValue().verticalAlignment.name();
        textBoxData.wrap = sWrap.getCurrentValue();
        textBoxData.textColor = sTextColor.getCurrentValue().toString();
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
        settings.add(sWrap);
        settings.add(sTextColor);
        return settings;
    }

    /** Settings */
    @Override
    public void initializeSettingsData() {
        super.initializeSettingsData();
        sText.setCurrentValue(textBox.getText());
        sTextColor.setCurrentValue(textBox.getTextRenderColor());
        sAlignment.setCurrentValue(new Alignment(textBox.getHorizontalAlignment(), textBox.getVerticalAlignment()));
        sWrap.setCurrentValue(textBox.getWrap());
    }

    /** Copy */
    @Override
    public ScreenEditorItem makeCopy() {
        return new TextBoxScreenEditorItem(sTexture.getCurrentValue(), x, y, width, height);
    }
}
