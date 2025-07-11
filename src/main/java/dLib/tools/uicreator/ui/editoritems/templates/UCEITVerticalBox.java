package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.itembox.VerticalBox;

public class UCEITVerticalBox extends UCEITemplate {
    public UCEITVerticalBox() {
        super("Vertical Box");
    }

    @Override
    protected UIElement.UIElementData generateElementData() {
        return new VerticalBox.VerticalBoxData();
    }
}
