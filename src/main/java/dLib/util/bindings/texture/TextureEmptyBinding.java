package dLib.util.bindings.texture;

import com.badlogic.gdx.graphics.Texture;
import dLib.util.TextureManager;

import java.io.Serializable;

public class TextureEmptyBinding extends TextureBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public String getDisplayValue() {
        return "EMPTY";
    }

    /** Display */
    @Override
    public Texture getBoundObject(Object... params) {
        return TextureManager.getTexture("dLibResources/images/ui/BlankPixel.png");
    }
}
