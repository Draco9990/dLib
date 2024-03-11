package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Hoverable;

import java.io.Serializable;

public class Image extends Hoverable {
    //region Variables
    //endregion

    //region Constructors

    public Image(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    public Image(ImageData data){
        super(data);
    }

    //endregion

    //region Methods
    //endregion

    public static class ImageData extends Hoverable.HoverableData implements Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public UIElement makeUIElement() {
            return new Image(this);
        }
    }
}
