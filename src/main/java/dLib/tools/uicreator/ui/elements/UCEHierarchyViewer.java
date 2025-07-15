package dLib.tools.uicreator.ui.elements;

import dLib.tools.uicreator.UCEditor;
import dLib.tools.uicreator.ui.components.UCEditorItemComponent;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.GeneratedElementComponent;
import dLib.ui.elements.items.hierarchyviewer.HierarchyViewer;
import dLib.ui.UIManager;

public class UCEHierarchyViewer extends HierarchyViewer {
    public UCEHierarchyViewer() {
        super();

        onReparentEvent.subscribe(this, (element, element2, element3) -> {
            UCEditor editor = UIManager.getOpenElementOfType(UCEditor.class);
            editor.itemTree.reparentItem(element, element2, element3);
        });
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

            onHoveredEvent.subscribe(this, () -> {
                UCEditorItemComponent component = element.getComponent(UCEditorItemComponent.class);
                component.setHoveredInHierarchy(true);
            });

            onUnhoveredEvent.subscribe(this, () -> {
                UCEditorItemComponent component = element.getComponent(UCEditorItemComponent.class);
                component.setHoveredInHierarchy(false);
            });

            onLeftClickReleaseEvent.subscribe(this, () -> {
                ((UCEditor)element.getTopParent()).properties.hideAll();
                ((UCEditor)element.getTopParent()).properties.propertyEditor.showAndEnableInstantly();
                ((UCEditor)element.getTopParent()).properties.propertyEditor.setProperties(element.getComponent(GeneratedElementComponent.class).sourceData);
            });
        }
    }
}
