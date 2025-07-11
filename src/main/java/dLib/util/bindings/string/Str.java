package dLib.util.bindings.string;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dLib.util.TextureManager;
import dLib.util.bindings.string.interfaces.ITextProvider;
import dLib.util.bindings.texture.TextureNoneBinding;
import dLib.util.bindings.texture.TextureResourceBinding;
import dLib.util.bindings.texture.TextureStaticBinding;

import java.util.function.Supplier;

public class Str {
    public static StringStaticBinding stat(String text) {
        return new StringStaticBinding(text);
    }

    public static StringSourceBinding src(ITextProvider provider) {
        return new StringSourceBinding(provider);
    }

    public static StringLambdaBinding lambda(Supplier<String> provider) {
        return new StringLambdaBinding(provider);
    }
}
