package dLib.util.bindings.texture;

import com.badlogic.gdx.graphics.Texture;
import dLib.properties.objects.templates.TProperty;

import java.util.ArrayList;

public class TextureResourceBinding extends TextureBinding{
    //region Variables

    private ArrayList<TProperty<?, ?>> declaredParams = new ArrayList<>();

    //endregion Variables

    //region Methods

    public ArrayList<TProperty<?, ?>> getDeclaredParams() {
        return declaredParams;
    }

    //endregion Methods

    @Override
    public Texture getBoundObject(Object... params) {
        return null;
    }

    @Override
    public String getDisplayValue() {
        return "";
    }
}
