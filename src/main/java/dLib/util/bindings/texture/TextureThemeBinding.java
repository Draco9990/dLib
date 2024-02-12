package dLib.util.bindings.texture;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.themes.UIThemeManager;
import dLib.util.DLibLogger;
import dLib.util.Reflection;

import java.io.Serializable;
import java.lang.reflect.Field;

public class TextureThemeBinding extends TextureBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Variables */
    private String themeFieldName = "";

    /** Constructors */
    public TextureThemeBinding(String fieldName){
        themeFieldName = fieldName;
    }

    /** Bindings */
    @Override
    public Texture getBoundTexture() {
        try{
            return Reflection.getFieldValue(themeFieldName, UIThemeManager.getDefaultTheme());
        }catch (Exception e){
            DLibLogger.log("Failed to get bound image due to " + e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public boolean isValid() {
        return themeFieldName != null && !themeFieldName.isEmpty() && getBoundTexture() != null;
    }

    /** Name */
    @Override
    public String getShortDisplayName() {
        return themeFieldName;
    }

    @Override
    public String getFullDisplayName() {
        return "theme/" + themeFieldName;
    }
}
