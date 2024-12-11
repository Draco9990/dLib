package dLib.tools.uicreator;

import dLib.tools.uicreator.ui.editoritems.templates.UCEITemplate;
import dLib.tools.uicreator.ui.elements.UCERootElement;
import dLib.ui.elements.UIElement;

import java.util.ArrayList;

public class UCEditorItemTree extends ArrayList<UCEditorItemTree.UCEditorItemTreeEntry> {
    public UCERootElement rootElement;

    public UCEditorItemTree(UCERootElement rootElement) {
        this.rootElement = rootElement;
    }

    public UCEditorItemTreeEntry findEntry(UIElement element){
        return stream().filter(e -> e.element == element).findFirst().orElse(null);
    }

    public void addItem(UIElement element, UIElement.UIElementData elementData, UCEITemplate template){
        UCEditorItemTreeEntry entry = new UCEditorItemTreeEntry(element, elementData, template);
        add(entry);

        rootElement.addChildNCS(element);
    }

    public void refreshItem(UIElement.UIElementData elementData){
        UCEditorItemTreeEntry entry = stream().filter(e -> e.elementData == elementData).findFirst().orElse(null);
        if(entry != null){
            UIElement elementToAdd = entry.template.makeEditorItem(elementData);
            entry.element.getParent().replaceChild(entry.element, elementToAdd);
            entry.element = elementToAdd;
        }
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
