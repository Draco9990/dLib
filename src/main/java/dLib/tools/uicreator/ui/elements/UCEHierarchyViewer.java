package dLib.tools.uicreator.ui.elements;

import dLib.tools.uicreator.ui.components.UCEditorComponent;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.ElementGroupModifierComponent;
import dLib.ui.elements.prefabs.HierarchyViewer;

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

            if(!addingElement.hasComponent(UCEditorComponent.class)){
                return;
            }

            getButton().addOnLeftClickEvent(() -> {
                ElementGroupModifierComponent component = element.getComponent(ElementGroupModifierComponent.class);
                component.select();
            });

            getButton().addOnHoveredEvent(() -> {
                UCEditorComponent component = element.getComponent(UCEditorComponent.class);
                component.setHoveredInHierarchy(true);
            });

            getButton().addOnUnHoveredEvent(() -> {
                UCEditorComponent component = element.getComponent(UCEditorComponent.class);
                component.setHoveredInHierarchy(false);
            });
        }
    }
}
