package dLib.magiccolor;

import com.badlogic.gdx.graphics.Colors;

import java.util.HashMap;

public class MagicColorManager {
    public static HashMap<String, MagicColor> magicColors = new HashMap<>();

    public static void registerMagicColor(MagicColor magicColor) {
        magicColors.put(magicColor.toString(), magicColor);
        Colors.put(magicColor.toString(), magicColor);
    }
}
