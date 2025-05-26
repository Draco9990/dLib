package dLib.magiccolor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public abstract class MagicColor extends Color {
    public MagicColor() {
        super();
    }

    @Override
    public float toFloatBits() {
        return getFinalColor().toFloatBits();
    }

    public abstract Color getFinalColor();
    public abstract Texture getSquareImage();

    public abstract MagicColor cpy();
}
