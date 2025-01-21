package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.itembox.VerticalBox;

public class UCEITHorizontalBox extends UCEITemplate {
    public UCEITHorizontalBox() {
        super("Horizontal Box");
    }

    @Override
    protected UIElement.UIElementData generateElementData() {
        return new HorizontalBox.HorizontalBoxData();
    }
}
