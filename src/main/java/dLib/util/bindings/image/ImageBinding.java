package dLib.util.bindings.image;

import com.badlogic.gdx.graphics.Texture;

public abstract class ImageBinding {

    /** Binding */
    public abstract boolean isBindingValid();
    public abstract Texture getBoundImage();

    /** Display */
    public abstract String getShortDisplayName();
    public abstract String getFullDisplayName();
}
