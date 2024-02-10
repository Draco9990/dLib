package dLib.util.bindings.image;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.themes.UIThemeManager;
import dLib.util.DLibLogger;
import dLib.util.Reflection;

import java.lang.reflect.Field;

public class TextureThemeBinding extends TextureBinding {
    /** Variables */
    private Field field;

    /** Constructors */
    public TextureThemeBinding(Field themeField){
        this.field = themeField;
    }

    public TextureThemeBinding(String fieldName, Class<?> fieldClass){
        this(Reflection.getFieldByName(fieldName, fieldClass));
    }

    /** Bindings */
    @Override
    public Texture getBoundTexture() {
        try{
            return (Texture) field.get(UIThemeManager.getDefaultTheme());
        }catch (Exception e){
            DLibLogger.log("Failed to get bound image due to " + e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public boolean isValid() {
        return field != null && getBoundTexture() != null;
    }

    /** Name */
    @Override
    public String getShortDisplayName() {
        return field.getName();
    }

    @Override
    public String getFullDisplayName() {
        return "theme/" + field.getName();
    }
}
