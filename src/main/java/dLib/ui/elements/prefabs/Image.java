package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.elements.implementations.Renderable;
import dLib.util.IntegerVector2;
import dLib.util.bindings.texture.TexturePathBinding;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;

public class Image extends Renderable {
    //region Variables
    //endregion

    //region Constructors

    public Image(Texture image, AbstractPosition xPos, AbstractPosition yPos){
        this(image, xPos, yPos, Dim.fill(), Dim.fill());
    }
    public Image(Texture image, AbstractDimension width, AbstractDimension height){
        this(image, Pos.px(0), Pos.px(0), width, height);
    }
    public Image(Texture image, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(image, xPos, yPos, width, height);

        setPassthrough(true);
    }

    public Image(ImageData data){
        super(data);
    }

    //endregion

    //region Methods

    //endregion

    public static class ImageData extends RenderableData implements Serializable {
        private static final long serialVersionUID = 1L;

        public ImageData(){
            textureBinding.setValue(new TexturePathBinding("dLibResources/images/ui/themes/WhitePixel.png"));
            dimensions.setValue(Dim.px(100), Dim.px(100));

            isPassthrough.setValue(true);
        }

        @Override
        public Image makeUIElement() {
            return new Image(this);
        }
    }
}
