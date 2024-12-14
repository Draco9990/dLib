package dLib.util.bindings.texture;

import com.badlogic.gdx.graphics.Texture;
import dLib.util.bindings.Binding;

import java.io.Serializable;

public abstract class TextureBinding extends Binding implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Binding */
    public abstract Texture getBoundTexture();
}
