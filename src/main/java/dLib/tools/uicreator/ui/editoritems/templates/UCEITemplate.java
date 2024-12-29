package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.tools.uicreator.UCEditor;
import dLib.tools.uicreator.ui.components.UCEditorItemComponent;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.ElementGroupModifierComponent;
import dLib.ui.elements.components.UIDraggableComponent;
import dLib.ui.elements.components.UIResizeableComponent;
import dLib.ui.elements.components.UIZoomableComponent;
import dLib.ui.elements.items.ContextMenu;
import dLib.util.IntegerVector2;
import dLib.util.UIHelpers;
import dLib.util.ui.dimensions.PixelDimension;
import dLib.util.ui.position.PixelPosition;
import dLib.util.ui.position.Pos;

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

        //TODO rescale items
        if(editorItem.getLocalPositionXRaw() instanceof PixelPosition){
            editorItem.setLocalPositionX((int) (((PixelPosition) editorItem.getLocalPositionXRaw()).getValueRaw() * 0.8f));
        }
        if(editorItem.getLocalPositionYRaw() instanceof PixelPosition){
            editorItem.setLocalPositionY((int) (((PixelPosition) editorItem.getLocalPositionYRaw()).getValueRaw() * 0.8f));
        }

        if(editorItem.getWidthRaw() instanceof PixelDimension){
            editorItem.setWidth((int) (((PixelDimension) editorItem.getWidthRaw()).getValueRaw() * 0.8f));
        }
        if(editorItem.getHeightRaw() instanceof PixelDimension){
            editorItem.setHeight((int) (((PixelDimension) editorItem.getHeightRaw()).getValueRaw() * 0.8f));
        }

        UIDraggableComponent draggableComp = editorItem.addComponent(new UIDraggableComponent());
        UIResizeableComponent resizeableComp = editorItem.addComponent(new UIResizeableComponent(editorItem));
        ElementGroupModifierComponent groupComp = editorItem.addComponent(new ElementGroupModifierComponent(editorItem, "editorItem"));
        UCEditorItemComponent editorComp = editorItem.addComponent(new UCEditorItemComponent());

        editorItem.onLeftClickEvent.subscribeManaged(() -> {
            ((UCEditor)editorItem.getTopParent()).properties.hideAll();
            ((UCEditor)editorItem.getTopParent()).properties.propertyEditor.showAndEnableInstantly();
            ((UCEditor)editorItem.getTopParent()).properties.propertyEditor.setProperties(elementData);
        });

        editorItem.onRightClickEvent.subscribeManaged(() -> {
            IntegerVector2 mousePos = UIHelpers.getMouseWorldPosition();

            ContextMenu contextMenu = new ContextMenu(Pos.px(mousePos.x), Pos.px(mousePos.y));
            contextMenu.addOption(new ContextMenu.ContextMenuButtonOption("Duplicate"));
            contextMenu.addOption(new ContextMenu.ContextMenuButtonOption("Delete"));
            contextMenu.open();
        });

        return editorItem;
    }

    private void wrapElementData(UIElement.UIElementData data){
        data.isPassthrough.setValue(false);
    }

    protected abstract UIElement.UIElementData generateElementData();

    @Override
    public String toString() {
        return displayName;
    }
}
