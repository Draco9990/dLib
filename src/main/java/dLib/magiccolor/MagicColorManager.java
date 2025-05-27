package dLib.magiccolor;

import java.util.ArrayList;
import java.util.HashMap;

public class MagicColorManager {
    public static HashMap<String, MagicColor> magicColors = new HashMap<>();

    public static void addMagicColor(MagicColor magicColor) {
        if (!magicColors.containsKey(magicColor.toString())) {
            magicColors.put(magicColor.toString(), magicColor);
        }
    }
}
