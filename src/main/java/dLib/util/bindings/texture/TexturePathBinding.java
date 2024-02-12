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
    public Texture getBoundTexture() {
        return TextureManager.getTexture(imagePath);
    }

    @Override
    public boolean isValid() {
        return getBoundTexture() != null;
    }

    /** Name */
    @Override
    public String getShortDisplayName() {
        String[] path = imagePath.split("\\\\");
        return path[path.length - 1];
    }

    @Override
    public String getFullDisplayName() {
        return imagePath;
    }
}
