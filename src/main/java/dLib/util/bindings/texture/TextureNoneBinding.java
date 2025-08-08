package dLib.util.bindings.texture;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import dLib.properties.objects.TextureBindingProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.util.bindings.texture.editors.TextureNoneBindingValueEditor;

import java.io.Serializable;

// USE RESPONSIBLY!
public class TextureNoneBinding extends AbstractTextureBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    //For property editors
    private static final String PROPERTY_EDITOR_LONG_NAME = "None Texture";
    private static final String PROPERTY_EDITOR_SHORT_NAME = "none";

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new TextureNoneBindingValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new TextureNoneBindingValueEditor((TextureBindingProperty) property);
    }

    @Override
    public String toString() {
        return "NULL";
    }

    @Override
    public NinePatch resolve(Object... params) {
        return null;
    }
}
