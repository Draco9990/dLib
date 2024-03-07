package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.data.prefabs.ImageData;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.elements.implementations.Renderable;

public class Image extends Hoverable {
    public Image(Texture image) {
        super(image);
    }

    public Image(Texture image, int xPos, int yPos) {
        super(image, xPos, yPos);
    }

    public Image(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    public Image(ImageData data){
        super(data);
    }
}
