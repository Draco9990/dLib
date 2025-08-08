package dLib.util.bindings.texture;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;

//! Warning, not serializable
public class TextureStaticBinding extends AbstractTextureBinding {

    private NinePatch texture;

    public TextureStaticBinding(TextureRegion region){
        this.texture = new NinePatch(region);
    }
    public TextureStaticBinding(Texture texture){
        this.texture = new NinePatch(texture);
    }
    public TextureStaticBinding(NinePatch ninePatch){
        this.texture = ninePatch;
    }

    @Override
    public NinePatch resolve(Object... params) {
        return texture;
    }

    @Override
    public String toString() {
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
