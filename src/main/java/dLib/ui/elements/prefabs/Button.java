package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.data.prefabs.ButtonData;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.themes.UIThemeManager;

public class Button extends Interactable {
    public Button(int xPos, int yPos, int width, int height) {
        super(UIThemeManager.getDefaultTheme().button_small, xPos, yPos, width, height);
    }

    public Button(ButtonData data){
        super(data);
    }

    /** Overrides for consistency */
    @Override
    public Button setImage(Texture image) {
        return (Button) super.setImage(image);
    }
}
