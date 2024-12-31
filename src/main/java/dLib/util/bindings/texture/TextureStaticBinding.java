package dLib.util.bindings.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;

//! Warning, not serializable
public class TextureStaticBinding extends AbstractTextureBinding {

    private TextureRegion region;

    public TextureStaticBinding(TextureRegion region){
        this.region = region;
    }

    public TextureStaticBinding(Texture texture){
        this.region = new TextureRegion(texture);
    }

    @Override
    public TextureRegion getBoundObject(Object... params) {
        return region;
    }

    @Override
    public String getDisplayValue() {
        return "STATIC";
    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        throw new UnsupportedOperationException("Not implemented - Texture Static Binding should never be edited in-app");
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        throw new UnsupportedOperationException("Not implemented - Texture Static Binding should never be edited in-app");
    }
}
