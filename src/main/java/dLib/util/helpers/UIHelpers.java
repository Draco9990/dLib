package dLib.util.helpers;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.elements.UIElement;
import dLib.util.IntegerVector2;

import java.util.UUID;

public class UIHelpers {
    public static IntegerVector2 getMouseWorldPosition(){
        return new IntegerVector2((int) (InputHelper.mX / Settings.xScale), (int) (InputHelper.mY / Settings.yScale));
    }

    public static String generateRandomElementId(){
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        return randomUUIDString.replaceAll("-", "");
    }
}
