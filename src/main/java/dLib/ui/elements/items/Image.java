package dLib.ui.elements.items;

import dLib.util.bindings.texture.AbstractTextureBinding;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;

public class Image extends Renderable {
    //region Variables
    //endregion

    //region Constructors

    public Image(AbstractTextureBinding imageBinding, AbstractPosition xPos, AbstractPosition yPos){
        this(imageBinding, xPos, yPos, Dim.fill(), Dim.fill());
    }
    public Image(AbstractTextureBinding imageBinding, AbstractDimension width, AbstractDimension height){
        this(imageBinding, Pos.px(0), Pos.px(0), width, height);
    }
    public Image(AbstractTextureBinding imageBinding, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(imageBinding, xPos, yPos, width, height);

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
            super();
            width.setValue(Dim.px(100));
            height.setValue(Dim.px(100));

            isPassthrough.setValue(true);
        }

        @Override
        public Image makeUIElement() {
            return new Image(this);
        }
    }
}
