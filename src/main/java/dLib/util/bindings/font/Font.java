package dLib.util.bindings.font;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dLib.util.TextureManager;
import dLib.util.bindings.texture.TextureResourceBinding;
import dLib.util.bindings.texture.TextureStaticBinding;

public class Font {
    public static FontStaticBinding stat(BitmapFont font) {
        return new FontStaticBinding(font);
    }

    public static FontResourceBinding resource(Class<?> clazz, String fieldName) {
        return new FontResourceBinding(clazz, fieldName);
    }
}
