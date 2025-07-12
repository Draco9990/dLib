package dLib.modcompat.saythespire;

import sayTheSpire.Output;

public class SayTheSpireProxy {
    public static void outputCond(String message){
        if(message != null){
            Output.text(message, true);
        }
    }

    //region Context

    public static void overrideContext(){
        Output.uiManager.pushContext(new DLibSayTheSpireContext());
    }

    public static void resetContext(){
        if(Output.uiManager.getCurrentContext() instanceof DLibSayTheSpireContext){
            Output.uiManager.popContext();
        }
    }

    //endregion Context
}
