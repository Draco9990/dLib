package dLib.magiccolor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Texture;
import dLib.util.TextureManager;

public class ColorItem {
    public String displayName;
    public Color color;
    public Texture squareImage;

    public ColorItem(String displayName, Color color, String squareImage) {
        this(displayName, color, TextureManager.getTexture(squareImage));
    }

    public ColorItem(String displayName, Color color, Texture squareImage) {
        this.displayName = displayName;
        this.color = color;
        this.squareImage = squareImage;
    }

    public void registerColorStrings(){
        if(color instanceof MagicColor){
            ((MagicColor) color).registerColor();
        }
    }
}
