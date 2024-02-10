package dLib.util.bindings.image;

import com.badlogic.gdx.graphics.Texture;

public abstract class TextureBinding {

    /** Binding */
    public abstract boolean isValid();
    public abstract Texture getBoundTexture();

    /** To String */
    @Override
    public String toString() {
        return getShortDisplayName();
    }

    /** Display */
    public abstract String getShortDisplayName();
    public abstract String getFullDisplayName();
}
