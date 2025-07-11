package dLib.util.bindings.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;

import java.util.function.Supplier;

//! Warning, not serializable
public class TextureLambdaBinding extends AbstractTextureBinding {

    private Supplier<NinePatch> ninePatchSupplier;

    public TextureLambdaBinding(Supplier<NinePatch> supplier){
        this.ninePatchSupplier = supplier;
    }

    @Override
    public NinePatch getBoundObject(Object... params) {
        return ninePatchSupplier.get();
    }

    @Override
    public String toString() {
        return "LAMBDA";
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
