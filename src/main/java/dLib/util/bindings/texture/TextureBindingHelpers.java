package dLib.util.bindings.texture;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.themes.UITheme;
import dLib.util.DLibLogger;
import dLib.util.Reflection;

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
                bindings.add(new TextureThemeBinding(f.getName()));
            }
        }catch (Exception e){
            DLibLogger.log("Failed to collect all theme bindings. Please contact Draco to fix this issue. " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return bindings;
    }

    public static ArrayList<TextureBinding> getCustomBindings(){
        ArrayList<TextureBinding> bindings = new ArrayList<>();
        bindings.add(new TextureEmptyBinding());
        return bindings;
    }
}
