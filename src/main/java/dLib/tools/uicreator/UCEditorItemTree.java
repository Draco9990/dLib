package dLib.tools.uicreator;

import dLib.external.ExternalMessageSender;
import dLib.external.ExternalStatics;
import dLib.tools.uicreator.ui.components.data.UCEditorDataComponent;
import dLib.tools.uicreator.ui.editoritems.templates.UCEITRootElement;
import dLib.tools.uicreator.ui.elements.RootElement;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Renderable;
import dLib.ui.screens.UIManager;
import dLib.util.SerializationHelpers;

public class UCEditorItemTree {
    public RootElement.RootElementData rootElementData;

    public UCEditorItemTree(Renderable canvas) {
        super();

        UCEITRootElement template = new UCEITRootElement();
        RootElement.RootElementData rootElementData = new RootElement.RootElementData();
        RootElement newRoot = (RootElement) template.makeEditorItem(rootElementData);

        canvas.addChild(newRoot);
        this.rootElementData = rootElementData;
    }

    public UIElement.UIElementData findElementDataRecursively(UIElement.UIElementData currentParent, UIElement liveElement){
        if(currentParent.getComponent(UCEditorDataComponent.class).liveElement == liveElement){
            return currentParent;
        }

        for(UIElement.UIElementData child : currentParent.children){
            UIElement.UIElementData result = findElementDataRecursively(child, liveElement);
            if(result != null){
                return result;
            }
        }

        return null;
    }

    public void addItem(UIElement.UIElementData elementData){
        rootElementData.children.add(elementData);

        rootElementData.getComponent(UCEditorDataComponent.class).liveElement.addChild(elementData.getComponent(UCEditorDataComponent.class).liveElement);
        elementData.getComponent(UCEditorDataComponent.class).parentData = rootElementData;

        ExternalMessageSender.send_addVariableToClass(ExternalStatics.workingClass, elementData.getComponent(UCEditorDataComponent.class).liveElement.getClass(), elementData.id.getValue());

        elementData.rootOwnerId = rootElementData.id.getValue();
    }

    public void refreshItem(UIElement.UIElementData elementData){
        if(elementData instanceof RootElement.RootElementData){
            UCEditorDataComponent toRefreshComponent = elementData.getComponent(UCEditorDataComponent.class);

            UIElement oldElement = toRefreshComponent.liveElement;
            UIElement newElement = toRefreshComponent.template.makeEditorItem(elementData);
            oldElement.getParent().replaceChild(oldElement, newElement);
        }
        else{
            UIElement.UIElementData parentData = elementData.getComponent(UCEditorDataComponent.class).parentData;

            UCEditorDataComponent toRefreshComponent = elementData.getComponent(UCEditorDataComponent.class);
            UCEditorDataComponent parentComponent = parentData.getComponent(UCEditorDataComponent.class);

            UIElement oldElement = toRefreshComponent.liveElement;
            UIElement newElement = toRefreshComponent.template.makeEditorItem(elementData);
            parentComponent.liveElement.replaceChild(oldElement, newElement);

            updateDataComponentHierarchyForElementData(null, parentData);
        }
    }

    public void duplicateItem(UIElement element){
        UIElement.UIElementData data = findElementDataRecursively(rootElementData, element);

        UIElement.UIElementData copy = SerializationHelpers.deepCopySerializable(data);
        copy.getOrAddComponent(new UCEditorDataComponent()).template = data.getComponent(UCEditorDataComponent.class).template;
        updateDataComponentHierarchyForElementData(data.getOrAddComponent(new UCEditorDataComponent()).parentData, copy);

        UIElement copyElement = copy.getOrAddComponent(new UCEditorDataComponent()).template.makeEditorItem(copy);
        copyElement.reparent(element.getParent());

        UIElement.UIElementData parentData = findElementDataRecursively(rootElementData, element.getParent());
        parentData.children.add(copy);
    }

    public void deleteItem(UIElement element){
        UIElement.UIElementData parentData = findElementDataRecursively(rootElementData, element.getParent());
        UIElement.UIElementData elementData = findElementDataRecursively(rootElementData, element);

        parentData.children.remove(elementData);
        element.dispose();

        UCEditor editor = UIManager.getOpenElementOfType(UCEditor.class);
        editor.properties.hideAll();
        editor.properties.toolbarPropertiesScrollbox.showAndEnableInstantly();
        editor.properties.toolbox.showAndEnableInstantly();

        ExternalMessageSender.send_removeVariableFromClass(ExternalStatics.workingClass, element.getId());
    }

    public void reparentItem(UIElement toReparent, UIElement oldParent, UIElement newParent){
        UIElement.UIElementData toReparentEntry = findElementDataRecursively(rootElementData, toReparent);
        UIElement.UIElementData oldParentEntry = findElementDataRecursively(rootElementData, oldParent);
        UIElement.UIElementData newParentEntry = findElementDataRecursively(rootElementData, newParent);

        oldParentEntry.children.remove(toReparentEntry);
        newParentEntry.children.add(toReparentEntry);

        toReparentEntry.getComponent(UCEditorDataComponent.class).parentData = newParentEntry;
        toReparentEntry.getComponent(UCEditorDataComponent.class).liveElement.reparent(newParent);
    }

    private void updateDataComponentHierarchyForElementData(UIElement.UIElementData parent, UIElement.UIElementData data){
        UCEditorDataComponent dataComponent = data.getOrAddComponent(new UCEditorDataComponent());
        if(parent != null){
            dataComponent.parentData = parent;
        }

        for(UIElement.UIElementData child : data.children){
            updateDataComponentHierarchyForElementData(data, child);
        }
    }
}
