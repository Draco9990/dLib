package dLib.util.helpers;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.util.IntegerVector2;

public class UIHelpers {
    public static IntegerVector2 getMouseWorldPosition(){
        return new IntegerVector2((int) (InputHelper.mX / Settings.xScale), (int) (InputHelper.mY / Settings.yScale));
    }
}
