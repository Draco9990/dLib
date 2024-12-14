package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.tools.uicreator.UCEditor;
import dLib.tools.uicreator.ui.components.UCEditorItemComponent;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.ElementGroupModifierComponent;
import dLib.ui.elements.components.UIDraggableComponent;
import dLib.ui.elements.components.UIResizeableComponent;
import dLib.util.ui.dimensions.StaticDimension;
import dLib.util.ui.position.StaticPosition;

public abstract class UCEITemplate {
    private String displayName;

    public UCEITemplate(String displayName) {
        this.displayName = displayName;
    }

    public UIElement.UIElementData makeElementData(){
        UIElement.UIElementData elementData = generateElementData();
        wrapElementData(elementData);

        return elementData;
    }

    public UIElement makeEditorItem(UIElement.UIElementData elementData){
        UIElement editorItem = elementData.makeUIElement();

        if(editorItem.getLocalPositionXRaw() instanceof StaticPosition){
            editorItem.setLocalPositionX((int) (((StaticPosition) editorItem.getLocalPositionXRaw()).getValueRaw() * 0.8f));
        }
        if(editorItem.getLocalPositionYRaw() instanceof StaticPosition){
            editorItem.setLocalPositionY((int) (((StaticPosition) editorItem.getLocalPositionYRaw()).getValueRaw() * 0.8f));
        }

        if(editorItem.getWidthRaw() instanceof StaticDimension){
            editorItem.setWidth((int) (((StaticDimension) editorItem.getWidthRaw()).getValueRaw() * 0.8f));
        }
        if(editorItem.getHeightRaw() instanceof StaticDimension){
            editorItem.setHeight((int) (((StaticDimension) editorItem.getHeightRaw()).getValueRaw() * 0.8f));
        }

        UIDraggableComponent draggableComp = editorItem.addComponent(new UIDraggableComponent());

        UIResizeableComponent resizeableComp = editorItem.addComponent(new UIResizeableComponent(editorItem));

        ElementGroupModifierComponent groupComp = editorItem.addComponent(new ElementGroupModifierComponent(editorItem, "editorItem"));

        UCEditorItemComponent editorComp = editorItem.addComponent(new UCEditorItemComponent());
        //DO stuff with comp

        editorItem.onLeftClickEvent.subscribeManaged(() -> {
            ((UCEditor)editorItem.getTopParent()).properties.hideAll();
            ((UCEditor)editorItem.getTopParent()).properties.propertyEditor.showAndEnableInstantly();
            ((UCEditor)editorItem.getTopParent()).properties.propertyEditor.setProperties(elementData);
        });
        return editorItem;
    }

    private void wrapElementData(UIElement.UIElementData data){
        data.isPassthrough = false;
    }

    protected abstract UIElement.UIElementData generateElementData();

    @Override
    public String toString() {
        return displayName;
    }
}
