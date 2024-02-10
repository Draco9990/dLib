package dLib.util.bindings.image;

import com.badlogic.gdx.graphics.Texture;
import dLib.util.TextureManager;

public class ImagePathBinding extends ImageBinding{
    /** Variables */
    private String imagePath = "";

    /** Constructors */
    public ImagePathBinding(String imagePath){
        this.imagePath = imagePath;
    }

    /** Bindings */
    @Override
    public Texture getBoundImage() {
        return TextureManager.getTexture(imagePath);
    }

    @Override
    public boolean isBindingValid() {
        return getBoundImage() != null;
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
