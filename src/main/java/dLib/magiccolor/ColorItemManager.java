package dLib.magiccolor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ColorItemManager {
    public static LinkedHashMap<String, ColorItem> colorItems = new LinkedHashMap<>();

    public static void registerColorItem(ColorItem magicColor) {
        colorItems.put(magicColor.toString(), magicColor);

        if(magicColor.color instanceof MagicColor) ((MagicColor) magicColor.color).registerColor();
        else Colors.put(magicColor.color.toString(), magicColor.color);
    }

    @SpirePatch2(clz = Color.class, method = "valueOf")
    public static class CustomColorLoaderPatch{
        @SpirePrefixPatch
        public static SpireReturn<Color> Prefix(String hex) {
            String colorCode = hex;
            if(hex.startsWith("#")){
                colorCode = hex.substring(1);
            }
            if(Colors.get(colorCode) != null){
                return SpireReturn.Return(Colors.get(colorCode).cpy());
            }

            return SpireReturn.Continue();
        }
    }
}
