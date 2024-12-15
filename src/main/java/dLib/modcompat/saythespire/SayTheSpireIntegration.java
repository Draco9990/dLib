package dLib.modcompat.saythespire;

import sayTheSpire.Output;

public class SayTheSpireIntegration {
    public static void Output(String message){
        Output.text(message, true);
    }
}
