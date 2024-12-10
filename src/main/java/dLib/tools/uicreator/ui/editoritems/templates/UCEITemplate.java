package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.tools.uicreator.ui.components.UCEditorComponent;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.ElementGroupModifierComponent;
import dLib.ui.elements.components.UIDraggableComponent;

public abstract class UCEITemplate {
    private String displayName;

    public UCEITemplate(String displayName) {
        this.displayName = displayName;
    }

    public UIElement makeEditorItem(){
        UIElement.UIElementData elementData = makeElementData();
        wrapElementData(elementData);

        return makeEditorItem(elementData);
    }

    public UIElement makeEditorItem(UIElement.UIElementData elementData){
        UIElement editorItem = elementData.makeUIElement();
        {
            UIDraggableComponent draggableComp = editorItem.addComponent(new UIDraggableComponent());

            ElementGroupModifierComponent groupComp = editorItem.addComponent(new ElementGroupModifierComponent(editorItem, "editorItem"));

            UCEditorComponent editorComp = editorItem.addComponent(new UCEditorComponent(elementData));
            //DO stuff with comp
        }
        return editorItem;
    }

    private void wrapElementData(UIElement.UIElementData data){
        data.isPassthrough = false;
    }

    protected abstract UIElement.UIElementData makeElementData();

    @Override
    public String toString() {
        return displayName;
    }
}
