package dLib.util.helpers;

import dLib.DLib;

public class DebugHelpers {
    public static void printStacktrace(int amount) {
        StackTraceElement[] s = Thread.currentThread().getStackTrace();
        for (int i = 2; i < s.length; i++) {
            DLib.log(s[i].getClassName() + "." + s[i].getMethodName() + ":" + s[i].getLineNumber());
            amount--;
            if (amount < 0) break;
        }
    }

    public static String getStacktrace(int amount) {
        String res = "";
        StackTraceElement[] s = Thread.currentThread().getStackTrace();
        for (int i = 2; i < s.length; i++) {
            res += s[i].getClassName() + "." + s[i].getMethodName() + ":" + s[i].getLineNumber() + "\n";
            amount--;
            if (amount < 0) break;
        }

        return res;
    }
}
