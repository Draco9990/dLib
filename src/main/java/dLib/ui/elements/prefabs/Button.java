package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.elements.implementations.Interactable;

public class Button extends Interactable {
    public Button(Texture image) {
        super(image);
    }

    public Button(Texture image, int xPos, int yPos) {
        super(image, xPos, yPos);
    }

    public Button(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }
}
