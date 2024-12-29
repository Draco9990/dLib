package dLib.util;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public class UIHelpers {
    public static IntegerVector2 getMouseWorldPosition(){
        return new IntegerVector2((int) (InputHelper.mX / Settings.xScale), (int) (InputHelper.mY / Settings.yScale));
    }
}
