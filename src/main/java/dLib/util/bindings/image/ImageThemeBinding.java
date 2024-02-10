package dLib.util.bindings.image;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.elements.UIElement;
import dLib.ui.themes.UIThemeManager;
import dLib.util.DLibLogger;
import dLib.util.Reflection;

import java.lang.reflect.Field;

public class ImageThemeBinding extends ImageBinding{
    /** Variables */
    private Field field;

    /** Constructors */
    public ImageThemeBinding(Field themeField){
        this.field = themeField;
    }

    /** Bindings */
    @Override
    public Texture getBoundImage() {
        try{
            return (Texture) field.get(UIThemeManager.getDefaultTheme());
        }catch (Exception e){
            DLibLogger.log("Failed to get bound image due to " + e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public boolean isBindingValid() {
        return field != null && getBoundImage() != null;
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
