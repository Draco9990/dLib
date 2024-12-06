package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import dLib.ui.elements.implementations.Interactable;
import dLib.util.IntegerVector2;
import dLib.util.bindings.texture.TexturePathBinding;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;

public class Image extends Interactable {
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

        setClickthrough(true);
    }

    public Image(ImageData data){
        super(data);

        setClickthrough(true);
    }

    //endregion

    //region Methods

    @Override
    protected Color getColorForRender() {
        setDisabledColor(getRenderColor());
        return super.getColorForRender();
    }


    //endregion

    public static class ImageData extends InteractableData implements Serializable {
        private static final long serialVersionUID = 1L;

        public ImageData(){
            textureBinding.setValue(new TexturePathBinding("dLibResources/images/ui/themes/WhitePixel.png"));
            dimensions.setValue(new IntegerVector2(100, 100));

            isPassthrough = true;
        }

        @Override
        public Image makeUIElement() {
            return new Image(this);
        }
    }
}
