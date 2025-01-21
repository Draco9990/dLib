package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.itembox.GridBox;
import dLib.ui.elements.items.itembox.GridItemBox;

public class UCEITGridBox extends UCEITemplate {
    public UCEITGridBox() {
        super("Grid Box");
    }

    @Override
    protected UIElement.UIElementData generateElementData() {
        return new GridBox.GridBoxData();
    }
}
