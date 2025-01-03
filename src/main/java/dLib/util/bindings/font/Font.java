package dLib.util.bindings.font;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Font {
    public static FontStaticBinding stat(BitmapFont font) {
        return new FontStaticBinding(font);
    }

    public static FontResourceBinding resource(Class<?> clazz, String fieldName) {
        return new FontResourceBinding(clazz, fieldName);
    }
}
