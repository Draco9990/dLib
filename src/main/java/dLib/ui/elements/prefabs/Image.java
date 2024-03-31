package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Hoverable;
import dLib.util.bindings.texture.TexturePathBinding;
import dLib.util.bindings.texture.TextureThemeBinding;

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

        public ImageData(){
            textureBinding.setValue(new TexturePathBinding("dLibResources/images/ui/themes/WhitePixel.png"));
        }

        @Override
        public Image makeUIElement() {
            return new Image(this);
        }
    }
}
