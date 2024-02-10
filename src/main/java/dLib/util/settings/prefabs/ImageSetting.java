package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractUISetting;
import dLib.ui.elements.settings.CustomUISetting;
import dLib.util.ImageBindingHelpers;
import dLib.util.bindings.image.ImageBinding;
import dLib.util.settings.Setting;

import java.util.ArrayList;

public class ImageSetting extends CustomSetting<ImageBinding> {
    /** Constructors */
    public ImageSetting(ImageBinding value) {
        super(value);
    }

    /** All options */
    @Override
    public ArrayList<ImageBinding> getAllOptions() {
        return ImageBindingHelpers.getAllImageBindings();
    }
}
