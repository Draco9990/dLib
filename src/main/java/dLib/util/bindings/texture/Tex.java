package dLib.util.bindings.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dLib.util.TextureManager;

public class Tex {
    public static TextureStaticBinding stat(TextureRegion region) {
        return new TextureStaticBinding(region);
    }
    public static TextureStaticBinding stat(Texture texture) {
        return new TextureStaticBinding(texture);
    }
    public static TextureStaticBinding stat(String texturePath){
        return new TextureStaticBinding(TextureManager.getTexture(texturePath));
    }
    public static TextureStaticBinding stat(NinePatch ninePatch){
        return new TextureStaticBinding(ninePatch);
    }

    public static TextureResourceBinding resource(Class<?> clazz, String fieldName) {
        return new TextureResourceBinding(clazz, fieldName);
    }

    public static TextureNoneBinding none(){
        return new TextureNoneBinding();
    }
}
