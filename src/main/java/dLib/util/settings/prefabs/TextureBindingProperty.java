package dLib.util.settings.prefabs;

import dLib.util.bindings.texture.TextureBindingHelpers;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureEmptyBinding;

import java.util.ArrayList;

public class TextureBindingProperty extends CustomProperty<TextureBinding> {
    /** Constructors */
    public TextureBindingProperty(TextureBinding value) {
        super(value);
    }

    public TextureBindingProperty() {
        super(new TextureEmptyBinding());
    }

    /** All options */
    @Override
    public ArrayList<TextureBinding> getAllOptions() {
        return TextureBindingHelpers.getAllImageBindings();
    }
}
