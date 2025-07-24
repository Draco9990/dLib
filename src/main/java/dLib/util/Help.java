package dLib.util;

import com.megacrit.cardcrawl.helpers.controller.CInputAction;
import com.megacrit.cardcrawl.helpers.input.InputAction;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Locale;

public class Help {
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

    public static class Util{
        public static void copyToClipboard(String s) {
            StringSelection selection = new StringSelection(s);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        }
    }
}
