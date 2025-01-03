package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.tools.uicreator.ui.elements.RootElement;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Image;

public class UCEITRootElement extends UCEITemplate {
    public UCEITRootElement() {
        super("Root Element");
    }

    @Override
    protected UIElement.UIElementData generateElementData() {
        return new RootElement.RootElementData();
    }
}
