package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Image;

public class UCEITImage extends UCEITemplate {
    public UCEITImage() {
        super("Image");
    }

    @Override
    protected UIElement.UIElementData generateElementData() {
        return new Image.ImageData();
    }
}
