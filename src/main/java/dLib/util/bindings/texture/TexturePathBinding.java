package dLib.util.bindings.texture;

import com.badlogic.gdx.graphics.Texture;
import dLib.util.TextureManager;

import java.io.Serializable;

public class TexturePathBinding extends TextureBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Variables */
    private String imagePath = "";

    /** Constructors */
    public TexturePathBinding(String imagePath){
        this.imagePath = imagePath;
    }

    /** Bindings */
    @Override
    public Texture getBoundObject(Object... params) {
        return TextureManager.getTexture(imagePath);
    }

    @Override
    public String getDisplayValue() {
        return imagePath;
    }
}
