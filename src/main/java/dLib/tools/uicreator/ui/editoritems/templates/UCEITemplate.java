package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.tools.uicreator.ui.components.UCEditorComponent;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIDraggableComponent;

public abstract class UCEITemplate {
    private String displayName;

    public UCEITemplate(String displayName) {
        this.displayName = displayName;
    }

    public UIElement makeEditorItem(){
        UIElement.UIElementData elementData = makeElementData();

        UIElement editorItem = elementData.makeUIElement();
        {
            UCEditorComponent editorComp = editorItem.addComponent(new UCEditorComponent(elementData));
            //DO stuff with comp

            UIDraggableComponent draggableComp = editorItem.addComponent(new UIDraggableComponent());
        }
        return editorItem;
    }

    protected abstract UIElement.UIElementData makeElementData();

    @Override
    public String toString() {
        return displayName;
    }
}
