package dLib.tools.screeneditor.ui.items.preview;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.data.UIElementData;
import dLib.ui.data.implementations.RenderableData;
import dLib.ui.elements.implementations.Renderable;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.settings.Property;
import dLib.util.settings.prefabs.ColorProperty;
import dLib.util.settings.prefabs.TextureBindingProperty;

import java.util.ArrayList;

public abstract class RenderableScreenEditorItem extends ScreenEditorItem {
    /** Variables */
    protected TextureBindingProperty sTexture = (TextureBindingProperty) new TextureBindingProperty(){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            if(getElementData() != null){
                getElementData().textureBinding = getValue();
            }
            if(getValue().isValid()){
                setImage(getValue().getBoundTexture());
            }
        }
    }.setName("Image:");

    protected ColorProperty sRenderColor = (ColorProperty) new ColorProperty(Color.WHITE){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            getElementData().renderColor = getValue().toString();
            setRenderColor(getValue());
        }
    }.setName("Render color:");

    /** Constructors */
    public RenderableScreenEditorItem(TextureBinding image, int xPos, int yPos, int width, int height) {
        super(image.getBoundTexture(), xPos, yPos, width, height);
        initialize(image);
    }

    public RenderableScreenEditorItem(RenderableData data){
        super(data);
        sTexture.setValue(data.textureBinding);
        sRenderColor.setValue(Color.valueOf(data.renderColor));
    }

    private void initialize(TextureBinding binding){
        sTexture.setValue(binding);
        sRenderColor.setValue(getColorForRender());
    }

    /** Image */
    public Renderable setImage(TextureBinding binding){
        if(sTexture.getValue() != binding){
            sTexture.setValue(binding);
        }

        if(!binding.isValid()){
            return this;
        }

        this.image = binding.getBoundTexture();
        return this;
    }

    /** Data */
    @Override
    public RenderableData makeElementData() {
        return new RenderableData();
    }

    @Override
    public void initializeElementData(UIElementData data) {
        super.initializeElementData(data);
        RenderableData renderableData = (RenderableData) data;
        renderableData.textureBinding = sTexture.getValue();
        renderableData.renderColor = sRenderColor.getValue().toString();
    }

    @Override
    public RenderableData getElementData() {
        return (RenderableData) super.getElementData();
    }

    /** Properties */
    @Override
    public ArrayList<Property<?>> getPropertiesForItem() {
        ArrayList<Property<?>> allProperties = super.getPropertiesForItem();
        allProperties.add(sTexture);
        allProperties.add(sRenderColor);
        return allProperties;
    }
}
