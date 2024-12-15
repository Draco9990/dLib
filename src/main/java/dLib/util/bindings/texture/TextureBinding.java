package dLib.util.bindings.texture;

import com.badlogic.gdx.graphics.Texture;
import dLib.util.bindings.ResourceBinding;

import java.io.Serializable;

public abstract class TextureBinding extends ResourceBinding<Texture> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public abstract Texture getBoundObject(Object... params);
}
