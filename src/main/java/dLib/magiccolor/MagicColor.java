package dLib.magiccolor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;

public abstract class MagicColor extends Color {
    public MagicColor() {
        super();
    }

    @Override
    public float toFloatBits() {
        return getFinalColor().toFloatBits();
    }

    @Override
    public int toIntBits() {
        return getFinalColor().toIntBits();
    }

    public abstract Color getFinalColor();
    public abstract Color getFinalColorForPosition(int xPos, int yPos);

    public abstract MagicColor cpy();

    public void registerColor(){
        Colors.put(toString(), this);
    }

    @Override
    public String toString() {
        return "mc_" + getClass().getSimpleName();
    }
}
