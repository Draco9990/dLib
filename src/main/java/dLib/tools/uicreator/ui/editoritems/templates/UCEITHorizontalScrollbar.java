package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.scroll.HorizontalScrollbar;
import dLib.ui.elements.items.scroll.VerticalScrollbar;

public class UCEITHorizontalScrollbar extends UCEITemplate {
    public UCEITHorizontalScrollbar() {
        super("Horizontal Scrollbar");
    }

    @Override
    protected UIElement.UIElementData generateElementData() {
        return new HorizontalScrollbar.HorizontalScrollbarData();
    }
}
