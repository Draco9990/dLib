package dLib.util.bindings.image;

import com.badlogic.gdx.graphics.Texture;
import dLib.util.TextureManager;

public class TexturePathBinding extends TextureBinding {
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
