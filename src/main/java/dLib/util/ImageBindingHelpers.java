package dLib.util;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.themes.UITheme;
import dLib.ui.themes.UIThemeManager;
import dLib.util.bindings.image.ImageBinding;
import dLib.util.bindings.image.ImageThemeBinding;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageBindingHelpers {
    public static ArrayList<ImageBinding> getAllImageBindings(){
        ArrayList<ImageBinding> bindings = new ArrayList<>();
        bindings.addAll(getThemeBindings());
        bindings.addAll(getCustomBindings());
        return bindings;
    }

    public static ArrayList<ImageBinding> getThemeBindings(){
        ArrayList<ImageBinding> bindings = new ArrayList<>();
        try{
            for(Field f : Reflection.getFieldsByClass(Texture.class, UITheme.class)){
                bindings.add(new ImageThemeBinding(f));
            }
        }catch (Exception e){
            DLibLogger.log("Failed to collect all theme bindings. Please contact Draco to fix this issue. " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return bindings;
    }

    public static ArrayList<ImageBinding> getCustomBindings(){
        //TODO
        return new ArrayList<>();
    }
}
