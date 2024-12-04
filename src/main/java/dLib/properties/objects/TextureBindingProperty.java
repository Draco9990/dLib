package dLib.properties.objects;

import dLib.properties.objects.templates.TTextureBindingProperty;
import dLib.util.bindings.texture.TextureBinding;

import java.io.Serializable;

public class TextureBindingProperty extends TTextureBindingProperty<TextureBindingProperty> implements Serializable {
    static final long serialVersionUID = 1L;

    public TextureBindingProperty(TextureBinding currentValue) {
        super(currentValue);
    }
}
