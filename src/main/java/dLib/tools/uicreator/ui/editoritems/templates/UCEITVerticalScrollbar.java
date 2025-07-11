package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.scroll.VerticalScrollbar;

public class UCEITVerticalScrollbar extends UCEITemplate {
    public UCEITVerticalScrollbar() {
        super("Vertical Scrollbar");
    }

    @Override
    protected UIElement.UIElementData generateElementData() {
        return new VerticalScrollbar.VerticalScrollbarData();
    }
}
