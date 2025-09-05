package dLib.magiccolor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public abstract class MultiColor extends MagicColor{
    private Color primary;
    private Color secondary;

    public MultiColor(Color primary, Color secondary) {
        super();
        this.primary = primary;
        this.secondary = secondary;
    }

    @Override
    public Color getFinalColor() {
        if(primary instanceof MagicColor){
            return ((MagicColor) primary).getFinalColor();
        }

        return primary;
    }

    @Override
    public Color getFinalColorForPosition(int xPos, int yPos) {
        if(primary instanceof MagicColor){
            return ((MagicColor) primary).getFinalColorForPosition(xPos, yPos);
        }

        return primary;
    }

    public Color getPrimaryColor() {
        return primary;
    }
    public Color getSecondaryColor() {
        return secondary;
    }

    @Override
    public void registerColor() {
        super.registerColor();

        if(primary instanceof MagicColor){
            ((MagicColor) primary).registerColor();
        }
        if(secondary instanceof MagicColor){
            ((MagicColor) secondary).registerColor();
        }
    }
}
