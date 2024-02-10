package dLib.util.settings.prefabs;

import dLib.util.ImageBindingHelpers;
import dLib.util.bindings.image.TextureBinding;

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
        return ImageBindingHelpers.getAllImageBindings();
    }
}
