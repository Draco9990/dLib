package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.tools.uicreator.ui.editoritems.UCEditorItem;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Image;

public class UCEITImage extends UCEITemplate {
    public UCEITImage() {
        super("Image");
    }

    public UCEditorItem makeEditorItem() {
        return new UCEditorItem<>(makeElementData(), 150, 150);
    }

    @Override
    protected UIElement.UIElementData makeElementData() {
        return new Image.ImageData();
    }
}
