package dLib.util;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.themes.UITheme;
import dLib.util.bindings.image.TextureBinding;
import dLib.util.bindings.image.TextureThemeBinding;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class TextureBindingHelpers {
    public static ArrayList<TextureBinding> getAllImageBindings(){
        ArrayList<TextureBinding> bindings = new ArrayList<>();
        bindings.addAll(getThemeBindings());
        bindings.addAll(getCustomBindings());
        return bindings;
    }

    public static ArrayList<TextureBinding> getThemeBindings(){
        ArrayList<TextureBinding> bindings = new ArrayList<>();
        try{
            for(Field f : Reflection.getFieldsByClass(Texture.class, UITheme.class)){
                bindings.add(new TextureThemeBinding(f));
            }
        }catch (Exception e){
            DLibLogger.log("Failed to collect all theme bindings. Please contact Draco to fix this issue. " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return bindings;
    }

    public static ArrayList<TextureBinding> getCustomBindings(){
        //TODO
        return new ArrayList<>();
    }
}
