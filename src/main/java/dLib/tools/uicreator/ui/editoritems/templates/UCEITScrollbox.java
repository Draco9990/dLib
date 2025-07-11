package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.scroll.Scrollbox;

public class UCEITScrollbox extends UCEITemplate {
    public UCEITScrollbox() {
        super("Scrollbox");
    }

    @Override
    protected UIElement.UIElementData generateElementData() {
        return new Scrollbox.ScrollboxData();
    }
}
