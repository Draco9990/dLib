package dLib.magiccolor;

import com.badlogic.gdx.graphics.Color;
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
    public abstract Color getFinalDeterminedColor_RenderPosition(int xPos, int yPos);

    public abstract Texture getSquareImage();

    public abstract MagicColor cpy();

    @Override
    public String toString() {
        return "mc_" + getClass().getSimpleName();
    }

    @SpirePatch2(clz = Color.class, method = "valueOf")
    public static class MagicColorLoaderPatch{
        @SpirePrefixPatch
        public static SpireReturn<Color> Prefix(String hex) {
            if(MagicColorManager.magicColors.containsKey(hex)) {
                return SpireReturn.Return(MagicColorManager.magicColors.get(hex).cpy());
            }

            return SpireReturn.Continue();
        }
    }
}
