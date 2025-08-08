package dLib.util.bindings.font;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import dLib.util.bindings.ResourceBinding;

import java.io.Serializable;

public abstract class AbstractFontBinding extends ResourceBinding<BitmapFont> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public abstract BitmapFont resolve(Object... params);
}
