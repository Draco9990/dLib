package dLib.util.bindings.texture;

import com.badlogic.gdx.graphics.Texture;

import java.io.Serializable;

// USE RESPONSIBLY!
public class TextureNullBinding extends TextureBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public String getDisplayValue() {
        return "NULL";
    }

    /** Display */
    @Override
    public Texture getBoundObject(Object... params) {
        return null;
    }
}
