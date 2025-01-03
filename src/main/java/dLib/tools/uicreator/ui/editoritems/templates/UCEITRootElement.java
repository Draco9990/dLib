package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.tools.uicreator.ui.elements.RootElement;
import dLib.ui.elements.UIElement;

public class UCEITRootElement extends UCEITemplate {
    public UCEITRootElement() {
        super("Root Element");
    }

    @Override
    protected UIElement.UIElementData generateElementData() {
        return new RootElement.RootElementData();
    }

    @Override
    protected void registerComponents(UIElement editorItem) {
    }

    @Override
    protected void bindLeftClickEvent(UIElement editorItem, UIElement.UIElementData elementData) {
    }

    @Override
    protected void bindRightClickEvent(UIElement editorItem) {
    }
}
