package dLib.util.bindings.texture;

import com.badlogic.gdx.graphics.Texture;
import dLib.util.TextureManager;

import java.io.Serializable;

// USE RESPONSIBLY!
public class TextureNullBinding extends TextureBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Bindings */
    @Override
    public String getShortDisplayName() {
        return "NULL";
    }

    @Override
    public String getFullDisplayName() {
        return "NULL";
    }

    /** Display */
    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Texture getBoundTexture() {
        return null;
    }
}
