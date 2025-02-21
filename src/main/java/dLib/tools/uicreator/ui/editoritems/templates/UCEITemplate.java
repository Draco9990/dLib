package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.external.ExternalMessageSender;
import dLib.external.ExternalStatics;
import dLib.properties.objects.templates.TProperty;
import dLib.tools.uicreator.UCEditor;
import dLib.tools.uicreator.ui.components.UCEditorItemComponent;
import dLib.tools.uicreator.ui.components.data.UCEditorDataComponent;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.ElementGroupModifierComponent;
import dLib.ui.elements.components.GeneratedElementComponent;
import dLib.ui.elements.components.UIDraggableComponent;
import dLib.ui.elements.components.UIResizeableComponent;
import dLib.ui.elements.items.ContextMenu;
import dLib.ui.screens.UIManager;
import dLib.util.IntegerVector2;
import dLib.util.helpers.UIHelpers;
import dLib.util.ui.dimensions.PixelDimension;
import dLib.util.ui.position.PixelPosition;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;

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

        wrapEditorItem(elementData, editorItem);
        for(UIElement child : editorItem.getAllChildren()){
            if(child.hasComponent(GeneratedElementComponent.class) && child.getComponent(GeneratedElementComponent.class).sourceData != null){
                UCEITemplateManager.getBestTemplateFor(child.getComponent(GeneratedElementComponent.class).sourceData).wrapEditorItem(child.getComponent(GeneratedElementComponent.class).sourceData, child);
            }
        }

        return editorItem;
    }

    public void wrapEditorItem(UIElement.UIElementData elementData, UIElement editorItem){
        rescaleDimensions(editorItem);
        registerComponents(editorItem);

        bindLeftClickEvent(editorItem, elementData);
        bindRightClickEvent(editorItem);

        UCEditorDataComponent dataComponent = elementData.getOrAddComponent(new UCEditorDataComponent());
        dataComponent.liveElement = editorItem;
        dataComponent.template = this;
    }

    protected void rescaleDimensions(UIElement editorItem){
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
    }

    protected void registerComponents(UIElement editorItem){
        UIDraggableComponent draggableComp = editorItem.addComponent(new UIDraggableComponent());
        UIResizeableComponent resizeableComp = editorItem.addComponent(new UIResizeableComponent(editorItem));
        ElementGroupModifierComponent groupComp = editorItem.addComponent(new ElementGroupModifierComponent(editorItem, "editorItem"));
        UCEditorItemComponent editorComp = editorItem.addComponent(new UCEditorItemComponent());
    }

    protected void bindLeftClickEvent(UIElement editorItem, UIElement.UIElementData elementData){
        editorItem.onLeftClickEvent.subscribeManaged(() -> {
            ((UCEditor)editorItem.getTopParent()).properties.hideAll();
            ((UCEditor)editorItem.getTopParent()).properties.propertyEditor.showAndEnableInstantly();
            ((UCEditor)editorItem.getTopParent()).properties.propertyEditor.setProperties(elementData);
        });
    }

    protected void bindRightClickEvent(UIElement editorItem){
        editorItem.onRightClickEvent.subscribeManaged(() -> {
            IntegerVector2 mousePos = UIHelpers.getMouseWorldPosition();

            ContextMenu contextMenu = new ContextMenu(Pos.px(mousePos.x), Pos.px(mousePos.y));

            ContextMenu.ContextMenuButtonOption duplicateOption = new ContextMenu.ContextMenuButtonOption("Duplicate", () -> {
                UCEditor editor = UIManager.getOpenElementOfType(UCEditor.class);
                editor.itemTree.duplicateItem(editorItem);
            });
            contextMenu.addChild(duplicateOption);

            ContextMenu.ContextMenuButtonOption deleteOption = new ContextMenu.ContextMenuButtonOption("Delete", () -> {
                UCEditor editor = UIManager.getOpenElementOfType(UCEditor.class);
                editor.itemTree.deleteItem(editorItem);
            });
            contextMenu.addChild(deleteOption);

            contextMenu.open();
        });
    }

    private void wrapElementData(UIElement.UIElementData data){
        data.isPassthrough.setValue(false);

        data.id.onValueChangedEvent.subscribeManaged((oldValue, newValue) -> {
            ExternalMessageSender.send_renameVariableInClass(ExternalStatics.workingClass, oldValue, newValue);
        });

        for(TProperty<?, ?> property : data.getEditableProperties()){
            property.onValueChangedEvent.subscribeManaged((oldValue, newValue) -> {
                UCEditor editor = UIManager.getOpenElementOfType(UCEditor.class);
                ExternalMessageSender.send_saveUIElement(ExternalStatics.workingClass, editor.itemTree.rootElementData);
            });
        }
    }

    protected abstract UIElement.UIElementData generateElementData();

    @Override
    public String toString() {
        return displayName;
    }
}
