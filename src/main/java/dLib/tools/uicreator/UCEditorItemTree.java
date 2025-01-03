package dLib.tools.uicreator;

import dLib.tools.uicreator.ui.editoritems.templates.UCEITRootElement;
import dLib.tools.uicreator.ui.editoritems.templates.UCEITemplate;
import dLib.tools.uicreator.ui.elements.RootElement;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Renderable;
import dLib.ui.screens.UIManager;
import dLib.util.SerializationHelpers;

import java.util.ArrayList;

public class UCEditorItemTree extends ArrayList<UCEditorItemTree.UCEditorItemTreeEntry> {
    public RootElement rootElement;
    public RootElement.RootElementData rootElementData;

    public UCEditorItemTree(Renderable canvas) {
        super();

        UCEITRootElement template = new UCEITRootElement();
        RootElement.RootElementData rootElementData = new RootElement.RootElementData();
        RootElement newRoot = (RootElement) template.makeEditorItem(rootElementData);

        canvas.addChild(newRoot);
        rootElement = newRoot;
        this.rootElementData = rootElementData;

        add(new UCEditorItemTreeEntry(newRoot, rootElementData, template));
    }

    public UCEditorItemTreeEntry findEntry(UIElement element){
        return stream().filter(e -> e.element == element).findFirst().orElse(null);
    }

    public void addItem(UIElement element, UIElement.UIElementData elementData, UCEITemplate template){
        UCEditorItemTreeEntry entry = new UCEditorItemTreeEntry(element, elementData, template);
        add(entry);

        rootElement.addChild(element);
    }

    public void refreshItem(UIElement.UIElementData elementData){
        UCEditorItemTreeEntry entry = stream().filter(e -> e.elementData == elementData).findFirst().orElse(null);
        if(entry != null){
            UIElement elementToAdd = entry.template.makeEditorItem(elementData);
            UIElement previousElement = entry.element;

            entry.element = elementToAdd;

            for(UIElement child : previousElement.getChildren()){
                child.reparent(elementToAdd);
            }
            previousElement.getParent().replaceChild(previousElement, elementToAdd);

            if(elementToAdd instanceof RootElement){
                this.rootElement = (RootElement) elementToAdd;
            }
        }
    }

    public void duplicateItem(UIElement element){
        UCEditorItemTreeEntry entry = findEntry(element);
        if(entry != null){
            UIElement.UIElementData elementData = SerializationHelpers.deepCopySerializable(entry.elementData);
            elementData.id.setValue("Copy of " + elementData.id.getValue());
            UIElement elementToAdd = entry.template.makeEditorItem(elementData);

            addItem(elementToAdd, elementData, entry.template);

            elementToAdd.reparent(element.getParent());
        }
    }

    public void deleteItem(UIElement element){
        UCEditorItemTreeEntry entry = findEntry(element);
        if(entry != null){
            remove(entry);
            element.dispose();
        }

        UCEditor editor = UIManager.getOpenElementOfType(UCEditor.class);
        editor.properties.hideAll();
        editor.properties.toolbarPropertiesScrollbox.showAndEnableInstantly();
        editor.properties.toolbox.showAndEnableInstantly();
    }

    public static class UCEditorItemTreeEntry{
        public UIElement element;
        public UIElement.UIElementData elementData;
        public UCEITemplate template;

        public UCEditorItemTreeEntry(UIElement element, UIElement.UIElementData elementData, UCEITemplate template) {
            this.element = element;
            this.elementData = elementData;
            this.template = template;
        }
    }
}
