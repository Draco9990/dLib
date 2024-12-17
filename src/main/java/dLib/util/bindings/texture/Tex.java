package dLib.util.bindings.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tex {
    public static TextureStaticBinding stat(TextureRegion region) {
        return new TextureStaticBinding(region);
    }
    public static TextureStaticBinding stat(Texture texture) {
        return new TextureStaticBinding(texture);
    }

    public static TextureResourceBinding resource(Class<?> clazz, String fieldName) {
        return new TextureResourceBinding(clazz, fieldName);
    }
}
