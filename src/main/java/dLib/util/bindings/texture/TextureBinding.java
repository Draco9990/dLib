package dLib.util.bindings.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dLib.properties.ui.elements.IEditableValue;
import dLib.util.bindings.ResourceBinding;

import java.io.Serializable;

public abstract class TextureBinding extends ResourceBinding<TextureRegion> implements Serializable, IEditableValue {
    private static final long serialVersionUID = 1L;

    @Override
    public abstract TextureRegion getBoundObject(Object... params);
}
