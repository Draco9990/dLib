package dLib.tools.uicreator.ui.elements;

import dLib.tools.uicreator.ui.components.UCEditorItemComponent;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.ElementGroupModifierComponent;
import dLib.ui.elements.items.HierarchyViewer;

public class UCEHierarchyViewer extends HierarchyViewer {
    public UCEHierarchyViewer() {
        super();
    }

    @Override
    protected HierarchyViewerChildElementButton makeHierarchyViewerElementButton(UIElement element) {
        return new UCEHierarchyViewerChildElementButton(element);
    }

    protected static class UCEHierarchyViewerChildElementButton extends HierarchyViewerChildElementButton {
        public UCEHierarchyViewerChildElementButton(UIElement addingElement) {
            super(addingElement);

            if(!addingElement.hasComponent(UCEditorItemComponent.class)){
                return;
            }

            onHoveredEvent.subscribeManaged(() -> {
                UCEditorItemComponent component = element.getComponent(UCEditorItemComponent.class);
                component.setHoveredInHierarchy(true);
            });

            onUnhoveredEvent.subscribeManaged(() -> {
                UCEditorItemComponent component = element.getComponent(UCEditorItemComponent.class);
                component.setHoveredInHierarchy(false);
            });
        }
    }
}
