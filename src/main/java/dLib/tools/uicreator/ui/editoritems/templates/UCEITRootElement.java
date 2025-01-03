package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.tools.uicreator.ui.components.UCEditorItemComponent;
import dLib.tools.uicreator.ui.elements.RootElement;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.ElementGroupModifierComponent;
import dLib.ui.elements.components.UIDraggableComponent;
import dLib.ui.elements.components.UIResizeableComponent;
import dLib.ui.elements.items.Image;

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
