package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.elements.implementations.Interactable;
import dLib.util.bindings.texture.TexturePathBinding;
import dLib.util.bindings.texture.TextureThemeBinding;

import java.io.Serializable;

public class Image extends Interactable {
    //region Variables
    //endregion

    //region Constructors

    public Image(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);

        setClickthrough(true);
    }

    public Image(ImageData data){
        super(data);

        setClickthrough(true);
    }

    //endregion

    //region Methods
    //endregion

    public static class ImageData extends InteractableData implements Serializable {
        private static final long serialVersionUID = 1L;

        public ImageData(){
            textureBinding.setValue(new TexturePathBinding("dLibResources/images/ui/themes/WhitePixel.png"));

            isClickthrough = true;
        }

        @Override
        public Image makeUIElement() {
            return new Image(this);
        }
    }
}
