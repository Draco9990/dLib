package dLib.util.settings.prefabs;

import dLib.util.bindings.texture.TextureBindingHelpers;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureEmptyBinding;

import java.util.ArrayList;

public class TextureProperty extends CustomProperty<TextureBinding> {
    /** Constructors */
    public TextureProperty(TextureBinding value) {
        super(value);
    }

    public TextureProperty() {
        super(new TextureEmptyBinding());
    }

    /** All options */
    @Override
    public ArrayList<TextureBinding> getAllOptions() {
        return TextureBindingHelpers.getAllImageBindings();
    }
}
