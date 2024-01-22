package dLib.util;

import com.google.gson.Gson;
import com.megacrit.cardcrawl.helpers.controller.CInputAction;
import com.megacrit.cardcrawl.helpers.input.InputAction;
import dLib.DLib;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Locale;

public class Help {
    public static class Json{
        public static String toJson(Object object){
            return new Gson().toJson(object);
        }

        public static Object fromJson(String object, Class<?> objectClass){
            return new Gson().fromJson(object, objectClass);
        }
    }

    public static class Input{
        public static boolean isPressed(CInputAction c, InputAction i){
            return isPressed(c) || isPressed(i);
        }
        public static boolean isPressed(CInputAction i){
            return i != null && i.justPressed;
        }
        public static boolean isPressed(InputAction i){
            return i != null && i.isJustPressed();
        }
    }

    public static class StringOps{
        public static java.lang.String capitalize(java.lang.String str){
            java.lang.String s1 = str.substring(0, 1).toUpperCase(Locale.ROOT);
            java.lang.String s2 = str.substring(1).toLowerCase(Locale.ROOT);
            java.lang.String res = s1 + s2;
            return res;
        }
    }

    public static class UI{

    }

    public static class Util{
        public static void copyToClipboard(String s) {
            StringSelection selection = new StringSelection(s);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        }
    }

    public static class Dev{
        public static void printStacktrace(int amount) {
            StackTraceElement[] s = Thread.currentThread().getStackTrace();
            for (int i = 2; i < s.length; i++) {
                DLib.log(s[i].getClassName() + "." + s[i].getMethodName() + ":" + s[i].getLineNumber());
                amount--;
                if (amount < 0) break;
            }
        }
    }
}
