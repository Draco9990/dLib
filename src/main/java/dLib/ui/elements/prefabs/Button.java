package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.themes.UIThemeManager;
import dLib.util.bindings.texture.TextureThemeBinding;

import java.io.Serializable;

public class Button extends Interactable {
    //region Variables
    //endregion

    //region Constructors

    public Button(int xPos, int yPos, int width, int height) {
        super(UIThemeManager.getDefaultTheme().button_small, xPos, yPos, width, height);
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
            textureBinding = new TextureThemeBinding("button_large");
        }

        @Override
        public Button makeUIElement() {
            return new Button(this);
        }
    }
}
