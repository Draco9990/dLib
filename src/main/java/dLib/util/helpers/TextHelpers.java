package dLib.util.helpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import dLib.util.IntegerVector2;
import dLib.util.ui.text.ColorEndMarkup;
import dLib.util.ui.text.ColorMarkup;
import dLib.util.ui.text.Markup;

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

    public static Markup getMarkupType(String markup){
        if(!isValidMarkup(markup)) return null;

        if(markup.isEmpty()) return new ColorEndMarkup();
        else return new ColorMarkup();
    }

    public static int getTotalGlyphCount(GlyphLayout layout){
        int count = 0;
        for (GlyphLayout.GlyphRun run : layout.runs) {
            count += run.glyphs.size;
        }
        return count;
    }

    public static int getGlyphOffsetForRowAndIndex(GlyphLayout layout, int runIndex, int glyphIndex){
        int totalCharsUpTo = 0;
        for (int i = 0; i < runIndex; i++){
            totalCharsUpTo += layout.runs.get(i).glyphs.size;
        }
        totalCharsUpTo += glyphIndex;

        return getTotalGlyphCount(layout) - totalCharsUpTo;
    }

    public static IntegerVector2 getIndexForGlyphOffset(GlyphLayout layout, int offset){
        for(int runIndex = layout.runs.size - 1; runIndex >= 0; runIndex--){
            GlyphLayout.GlyphRun run = layout.runs.get(runIndex);
            if(offset >= run.glyphs.size){
                offset -= run.glyphs.size;
            }
            else{
                return new IntegerVector2(runIndex, offset);
            }
        }

        return new IntegerVector2(0, 0);
    }

    public static String getAllMarkup(String text){
        // Match brackets with no nested brackets inside
        Pattern pattern = Pattern.compile("\\[(([^\\[\\]]*))\\]");
        Matcher matcher = pattern.matcher(text);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String innerContent = matcher.group(1);
            if (isValidMarkup(innerContent)) {
                result.append(matcher.group(0)); // keep full [content]
            }
        }

        return result.toString();
    }
}
