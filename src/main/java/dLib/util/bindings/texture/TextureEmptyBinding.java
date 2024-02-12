package dLib.util.bindings.texture;

import com.badlogic.gdx.graphics.Texture;
import dLib.util.TextureManager;

import java.io.Serializable;

public class TextureEmptyBinding extends TextureBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Bindings */
    @Override
    public String getShortDisplayName() {
        return "EMPTY";
    }

    @Override
    public String getFullDisplayName() {
        return "EMPTY";
    }

    /** Display */
    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Texture getBoundTexture() {
        return TextureManager.getTexture("dLibResources/images/ui/BlankPixel.png");
    }
}
