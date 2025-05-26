package dLib.magiccolor;

import java.util.ArrayList;

public class MagicColorManager {
    public static ArrayList<MagicColor> magicColors = new ArrayList<>();

    public static void addMagicColor(MagicColor magicColor) {
        if (!magicColors.contains(magicColor)) {
            magicColors.add(magicColor);
        }
    }
}
