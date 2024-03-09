package dLib.tools.screeneditor.ui.items.preview.renderable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.ui.items.preview.RenderableScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.Alignment;
import dLib.ui.data.UIElementData;
import dLib.ui.data.prefabs.TextBoxData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.TextBox;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureEmptyBinding;
import dLib.util.settings.Property;
import dLib.util.settings.prefabs.AlignmentProperty;
import dLib.util.settings.prefabs.BooleanProperty;
import dLib.util.settings.prefabs.ColorProperty;
import dLib.util.settings.prefabs.StringProperty;

import java.util.ArrayList;

public class TextBoxScreenEditorItem extends RenderableScreenEditorItem {
    /** Variables */
    private TextBox textBox;

    /** Settings */
    private StringProperty sText = (StringProperty) new StringProperty(""){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            getElementData().text = getValue();
            textBox.setText(getValue());
        }
    }.setName("Text:");

    private ColorProperty sTextColor = (ColorProperty) new ColorProperty(Color.BLACK){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            getElementData().textColor = getValue().toString();
            textBox.setTextRenderColor(getValue());
        }
    }.setName("Text Color:");

    private AlignmentProperty sAlignment = (AlignmentProperty) new AlignmentProperty(new Alignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.CENTER)){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            getElementData().horizontalAlignment = getValue().horizontalAlignment.name();
            getElementData().verticalAlignment = getValue().verticalAlignment.name();
            textBox.setAlignment(getValue().horizontalAlignment, getValue().verticalAlignment);
        }
    }.setName("Alignment:");

    private BooleanProperty sWrap = (BooleanProperty) new BooleanProperty(false){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            getElementData().wrap = getValue();
            textBox.setWrap(getValue());
        }
    }.setName("Wrap:");

    /** Constructors */
    public TextBoxScreenEditorItem(TextureBinding textureBinding, int xPos, int yPos, int width, int height) {
        super(textureBinding, xPos, yPos, width, height);

        textBox = new TextBox("TEXT", xPos, yPos, width, height);
    }

    public TextBoxScreenEditorItem(TextBoxData data){
        super(data);

        textBox = new TextBox(data.text, data.x, data.y, data.width, data.height);

        sText.setValue(data.text);
        sTextColor.setValue(Color.valueOf(data.textColor));

        sAlignment.setValue(new Alignment(Alignment.HorizontalAlignment.valueOf(data.horizontalAlignment), Alignment.VerticalAlignment.valueOf(data.verticalAlignment)));
        sWrap.setValue(data.wrap);
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
    public void updateSelf() {
        super.updateSelf();

        textBox.update();
    }

    @Override
    public void renderSelf(SpriteBatch sb) {
        super.renderSelf(sb);
        textBox.renderSelf(sb);
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
        textBoxData.text = sText.getValue();
        textBoxData.horizontalAlignment = sAlignment.getValue().horizontalAlignment.name();
        textBoxData.verticalAlignment = sAlignment.getValue().verticalAlignment.name();
        textBoxData.wrap = sWrap.getValue();
        textBoxData.textColor = sTextColor.getValue().toString();
    }

    @Override
    public TextBoxData getElementData() {
        return (TextBoxData) super.getElementData();
    }

    /** Properties */
    @Override
    public ArrayList<Property<?>> getPropertiesForItem() {
        ArrayList<Property<?>> properties = super.getPropertiesForItem();
        properties.add(sText);
        properties.add(sAlignment);
        properties.add(sWrap);
        properties.add(sTextColor);
        return properties;
    }

    /** Settings */
    @Override
    public void initializeSettingsData() {
        super.initializeSettingsData();
        sText.setValue(textBox.getText());
        sTextColor.setValue(textBox.getTextRenderColor());
        sAlignment.setValue(new Alignment(textBox.getHorizontalAlignment(), textBox.getVerticalAlignment()));
        sWrap.setValue(textBox.getWrap());
    }

    /** Copy */
    public static ScreenEditorItem makeNewInstance(ScreenEditorBaseScreen screenEditor){
        return new TextBoxScreenEditorItem(new TextureEmptyBinding(), 0, 0, 300, 75);
    }

    @Override
    public Class<? extends UIElement> getLiveInstanceType() {
        return TextBox.class;
    }
}
