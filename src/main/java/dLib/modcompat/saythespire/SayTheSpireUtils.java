package dLib.modcompat.saythespire;

import dLib.modcompat.ModManager;
import sayTheSpire.Output;

public class SayTheSpireUtils {
    public static void outputCond(String message){
        if(ModManager.SayTheSpire.isActive() && message != null){
            Output.text(message, true);
        }
    }
}
