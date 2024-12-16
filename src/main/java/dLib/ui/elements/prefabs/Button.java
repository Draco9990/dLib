package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.resources.UICommonResources;
import dLib.ui.themes.UIThemeManager;
import dLib.util.bindings.texture.TextureThemeBinding;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;

public class Button extends Interactable {
    //region Variables
    //endregion

    //region Constructors

    public Button(AbstractPosition xPos, AbstractPosition yPos){
        this(xPos, yPos, Dim.fill(), Dim.fill());
    }
    public Button(AbstractDimension width, AbstractDimension height){
        this(Pos.px(0), Pos.px(0), width, height);
    }
    public Button(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(UICommonResources.button03_horizontal, xPos, yPos, width, height);
    }

    public Button(ButtonData data){
        super(data);
    }

    //endregion

    //region Methods

    @Override
    public Button setImage(Texture image) {
        return (Button) super.setImage(image);
    }

    //endregion

    public static class ButtonData extends Interactable.InteractableData implements Serializable {
        private static final long serialVersionUID = 1L;

        public ButtonData(){
            textureBinding.setValue(new TextureThemeBinding("button_large"));
        }

        @Override
        public Button makeUIElement() {
            return new Button(this);
        }
    }
}
