package dLib.util.settings.prefabs;

import dLib.util.bindings.texture.TextureBindingHelpers;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureEmptyBinding;
import dLib.util.settings.Property;

import java.util.ArrayList;

public class TextureBindingProperty extends CustomProperty<TextureBinding> {
    //region Variables
    //endregion

    //region Constructors

    public TextureBindingProperty(TextureBinding value) {
        super(value);
    }

    public TextureBindingProperty() {
        super(new TextureEmptyBinding());
    }

    //endregion

    //region Methods

    @Override
    public ArrayList<TextureBinding> getAllOptions() {
        return TextureBindingHelpers.getAllImageBindings();
    }

    @Override
    public TextureBindingProperty setName(String newTitle) {
        return (TextureBindingProperty) super.setName(newTitle);
    }

    //endregion
}
