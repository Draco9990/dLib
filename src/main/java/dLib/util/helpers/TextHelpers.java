package dLib.util.helpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextHelpers {
    public static boolean isValidMarkup(String markup){
        if(markup.isEmpty()){
            return true;
        }
        else if(markup.startsWith("#")){
            //Check if the rest of the string is a valid hex color code
            String hex = markup.substring(1);
            try{
                Color.valueOf(hex);
                return true;
            }catch (Exception ignored){}
        }
        else if(Colors.getColors().containsKey(markup)){
            return true;
        }

        return false;
    }

    public static String removeNullifiedMarkup(String text){
        // Pattern to find [TAG][]
        Pattern pattern = Pattern.compile("\\[([^\\]]+)\\]\\[\\]");
        Matcher matcher = pattern.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String tag = matcher.group(1);
            if (isValidMarkup(tag)) {
                matcher.appendReplacement(result, "");
            } else {
                matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group(0)));
            }
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
