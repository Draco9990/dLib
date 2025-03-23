package dLib.util.ui.text;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import dLib.util.helpers.TextHelpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextMetadata extends HashMap<Integer, CharMetadata> {
    public static TextMetadata generateFor(String text, GlyphLayout layout){
        TextMetadata metadata = new TextMetadata();
        int glyphRowIndex = 0;
        int glyphIndex = 0;

        List<Markup> currentMarkups = new ArrayList<>();

        int totalGlyphCount = 0;
        for (int index = 0; index < text.length(); index++) {
            char c = text.charAt(index);

            // Execution flow if we're in a markup
            if(c == '['){
                int markupEnd = text.indexOf(']', index);
                int otherMarkupStart = text.indexOf('[', index + 1);

                if(otherMarkupStart == -1 || markupEnd < otherMarkupStart){
                    String markupText = text.substring(index + 1, markupEnd);
                    if(TextHelpers.isValidMarkup(markupText)){
                        Markup markup = TextHelpers.getMarkupType(markupText);
                        markup.indexStart = index;
                        markup.indexEnd = markupEnd;

                        if(!(markup instanceof ColorEndMarkup)){
                            currentMarkups.add(markup);
                        }
                        else{
                            currentMarkups.remove(currentMarkups.size() - 1);
                        }

                        for(; index <= markupEnd; index++){
                            MarkupCharMetadata charMetadata = new MarkupCharMetadata();
                            charMetadata.realStringIndex = index;
                            charMetadata.entireMarkup = markup;

                            metadata.put(index, charMetadata);
                        }
                        index--;
                        continue;
                    }
                }
            }

            //Execution flow if we're not in a markup
            GlyphCharMetadata charMetadata = new GlyphCharMetadata();
            charMetadata.realStringIndex = index;
            charMetadata.glyphRowIndex = glyphRowIndex;
            charMetadata.glyphIndex = glyphIndex;
            charMetadata.totalGlyphIndex = totalGlyphCount;
            charMetadata.appliedMarkups = new ArrayList<>(currentMarkups);

            metadata.put(index, charMetadata);

            glyphIndex++;
            totalGlyphCount++;
            if(glyphIndex >= layout.runs.get(glyphRowIndex).glyphs.size){
                glyphRowIndex++;
                glyphIndex = 0;
            }
        }

        return metadata;
    }
}
