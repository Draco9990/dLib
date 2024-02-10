package dLib.util.bindings.method;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.themes.UITheme;
import dLib.util.DLibLogger;
import dLib.util.Reflection;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureThemeBinding;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MethodBindingHelpers {
    public static ArrayList<MethodBinding> getPremadeMethodBindings(){
        ArrayList<MethodBinding> bindings = new ArrayList<>();
        bindings.add(new CloseScreenMethodBinding());
        return bindings;
    }
}
