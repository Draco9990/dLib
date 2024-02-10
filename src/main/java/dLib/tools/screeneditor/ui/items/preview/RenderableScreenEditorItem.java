package dLib.tools.screeneditor.ui.items.preview;

import dLib.ui.data.implementations.RenderableData;
import dLib.ui.elements.implementations.Renderable;
import dLib.util.bindings.image.TextureBinding;
import dLib.util.settings.Setting;
import dLib.util.settings.prefabs.TextureSetting;

import java.util.ArrayList;

public abstract class RenderableScreenEditorItem extends ScreenEditorItem {
    /** Variables */
    protected TextureSetting sTexture = (TextureSetting) new TextureSetting(){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
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
        RenderableData data = new RenderableData();
        data.ID = getId();
        data.x = x;
        data.y = y;
        data.width = width;
        data.height = height;
        return data;
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
