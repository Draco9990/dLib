package dLib.util.bindings.texture;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import dLib.util.bindings.ResourceBinding;

import java.io.Serializable;

public abstract class AbstractTextureBinding extends ResourceBinding<NinePatch> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public abstract NinePatch resolve(Object... params);
}
