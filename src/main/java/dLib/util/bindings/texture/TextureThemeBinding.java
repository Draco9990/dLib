package dLib.util.bindings.texture;

import com.badlogic.gdx.graphics.Texture;

import dLib.util.DLibLogger;
import dLib.util.Reflection;

import java.io.Serializable;

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
    public Texture getBoundObject(Object... params) {
        try{
            return Reflection.getFieldValue(themeFieldName, null);
        }catch (Exception e){
            DLibLogger.log("Failed to get bound image due to " + e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public String getDisplayValue() {
        return "theme/" + themeFieldName;
    }
}
