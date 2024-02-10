package dLib.util.settings.prefabs;

import dLib.util.bindings.texture.TextureBindingHelpers;
import dLib.util.bindings.texture.TextureBinding;

import java.util.ArrayList;

public class TextureSetting extends CustomSetting<TextureBinding> {
    /** Constructors */
    public TextureSetting(TextureBinding value) {
        super(value);
    }

    public TextureSetting() {
        super(TextureBinding.class);
    }

    /** All options */
    @Override
    public ArrayList<TextureBinding> getAllOptions() {
        return TextureBindingHelpers.getAllImageBindings();
    }
}
