package dLib.tools.screeneditor.ui.items.preview;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.data.UIElementData;
import dLib.ui.data.implementations.RenderableData;
import dLib.ui.elements.implementations.Renderable;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.settings.Setting;
import dLib.util.settings.prefabs.TextureSetting;

import java.util.ArrayList;

public abstract class RenderableScreenEditorItem extends ScreenEditorItem {
    /** Variables */
    protected TextureSetting sTexture = (TextureSetting) new TextureSetting(){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            if(getElementData() != null){
                getElementData().textureBinding = getCurrentValue();
            }
            if(getCurrentValue().isValid()){
                setImage(getCurrentValue().getBoundTexture());
            }
        }
    }.setTitle("Image:");

    /** Constructors */
    public RenderableScreenEditorItem(TextureBinding image) {
        super(image.getBoundTexture());
        sTexture.setCurrentValue(image);
    }

    public RenderableScreenEditorItem(TextureBinding image, int xPos, int yPos) {
        super(image.getBoundTexture(), xPos, yPos);
        sTexture.setCurrentValue(image);
    }

    public RenderableScreenEditorItem(TextureBinding image, int xPos, int yPos, int width, int height) {
        super(image.getBoundTexture(), xPos, yPos, width, height);
        sTexture.setCurrentValue(image);
    }

    public RenderableScreenEditorItem(RenderableData data){
        super(data);
        sTexture.setCurrentValue(data.textureBinding);
    }

    /** Image */
    public Renderable setImage(TextureBinding binding){
        if(sTexture.getCurrentValue() != binding){
            sTexture.setCurrentValue(binding);
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
        renderableData.textureBinding = sTexture.getCurrentValue();
    }

    @Override
    public RenderableData getElementData() {
        return (RenderableData) super.getElementData();
    }

    /** Properties */
    @Override
    public ArrayList<Setting<?>> getPropertiesForItem() {
        ArrayList<Setting<?>> allProperties = super.getPropertiesForItem();
        allProperties.add(sTexture);
        return allProperties;
    }
}
