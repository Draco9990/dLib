package dLib.util.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import dLib.util.Reflection;

import java.util.HashMap;

public class FontHelpers {
    private static HashMap<BitmapFont, Float> trueScaleMap = new HashMap<>();

    public static BitmapFont nonASCIIFont;

    //region Methods

    public static void initialize(){
        nonASCIIFont = generateNonASCIIFont();
    }

    public static BitmapFont generateFont(FileHandle source){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(source);
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.characters = "";
        param.incremental = true;
        param.size = Math.round(50.0f * Settings.scale);
        param.gamma = 1.2F;
        param.minFilter = Texture.TextureFilter.Linear;
        param.magFilter = Texture.TextureFilter.Linear;
        generator.scaleForPixelHeight(param.size);

        BitmapFont toReturn = generator.generateFont(param);
        toReturn.setUseIntegerPositions(false);
        (toReturn.getData()).markupEnabled = true;
        if (LocalizedStrings.break_chars != null){
            (toReturn.getData()).breakChars = LocalizedStrings.break_chars.toCharArray();
        }
        (toReturn.getData()).fontFile = source;

        return toReturn;
    }
    public static BitmapFont generateNonASCIIFont(){
        FileHandle f = Reflection.getFieldValue("fontFile", FontHelper.class);
        Reflection.setFieldValue("fontFile", FontHelper.class, Gdx.files.internal("font/zht/NotoSansCJKtc-Regular.otf"));
        BitmapFont nonASCIIFont = FontHelper.prepFont(24.0F, true);
        Reflection.setFieldValue("fontFile", FontHelper.class, f);
        return nonASCIIFont;
    }

    public static Float getFontTrueScale(BitmapFont font){
        if(!trueScaleMap.containsKey(font)){
            calculateFontTrueScale(font);
        }

        return trueScaleMap.get(font);
    }

    private static void calculateFontTrueScale(BitmapFont font){
        float height = FontHelper.getHeight(font, "lL", 1.0f);
        float scale = 1f / height;
        trueScaleMap.put(font, scale);
    }

    //endregion
}
