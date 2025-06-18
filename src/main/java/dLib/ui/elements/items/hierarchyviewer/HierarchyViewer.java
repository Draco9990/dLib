package dLib.ui.elements.items.hierarchyviewer;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIDropZoneComponent;
import dLib.ui.elements.components.UIPayloadComponent;
import dLib.ui.elements.components.UITransientElementComponent;
import dLib.ui.elements.items.Spacer;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.UUID;

public class HierarchyViewer extends VerticalBox {
    private UIElement forElement;

    private boolean allowReordering = false;
    private boolean canReorderRoot = false;

    private UUID hierarchyViewerID = UUID.randomUUID();

    public Event<TriConsumer<UIElement, UIElement, UIElement>> onReparentEvent = new Event<>();

    public HierarchyViewer() {
        super(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
    }

    public void loadForElement(UIElement element){
        clearChildren();
        if(element == null) return;

        this.forElement = element;
        recursivelyAddChild(element, 0);
    }

    private void recursivelyAddChild(UIElement child, int level){
        child.onHierarchyChangedEvent.subscribe(this, () -> {
            delayedActions.clear();
            delayedActions.add(() -> loadForElement(forElement));
        });

        HorizontalBox currentLevelBox = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(30));
        {
            currentLevelBox.addChild(new Spacer(Dim.px(20 * level), Dim.fill()));
            currentLevelBox.addChild(makeHierarchyViewerElementButton_internal(child));
        }
        if(shouldListChild(child)){
            addChild(currentLevelBox);
        }

        if(!child.getChildren().isEmpty()){
            for(UIElement grandChild : child.getChildren()){
                if(grandChild.hasComponent(UITransientElementComponent.class)){
                    continue;
                }

                recursivelyAddChild(grandChild, level + 1);
            }
        }
    }

    protected boolean shouldListChild(UIElement element) {
        return !element.hasComponent(UITransientElementComponent.class);
    }

    protected HierarchyViewerChildElementButton makeHierarchyViewerElementButton_internal(UIElement element){
        HierarchyViewerChildElementButton button = makeHierarchyViewerElementButton(element);

        if(allowsReordering()){
            if(canReorderRoot() || element != forElement){
                button.addComponent(new UIPayloadComponent<UIElement>(button, element, "hierarchyViewer" + hierarchyViewerID));
            }

            UIDropZoneComponent<UIElement> dropZoneComponent = button.addComponent(new UIDropZoneComponent<UIElement>(button, "hierarchyViewer" + hierarchyViewerID));
            dropZoneComponent.onPayloadDroppedEvent.subscribe(this, (payload) -> {
                UIElement currentParent = element.getParent();
                payload.reparent(element);

                onReparentEvent.invoke(uiElementUIElementUIElementTriConsumer -> uiElementUIElementUIElementTriConsumer.accept(payload, currentParent, element));
            });
        }

        return button;
    }

    protected HierarchyViewerChildElementButton makeHierarchyViewerElementButton(UIElement element){
        return new HierarchyViewerChildElementButton(element);
    }

    //region Reordering

    public void setAllowReordering(boolean allowReordering){
        this.allowReordering = allowReordering;
    }
    public boolean allowsReordering(){
        return allowReordering;
    }

    public void setCanReorderRoot(boolean canReorderRoot){
        this.canReorderRoot = canReorderRoot;
    }
    public boolean canReorderRoot(){
        return canReorderRoot;
    }

    //endregion

    protected static class HierarchyViewerChildElementButton extends TextButton {
        protected UIElement element;

        public HierarchyViewerChildElementButton(UIElement element) {
            super(element.getId(), Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(30));
            setTexture(Tex.stat(UICommonResources.button03_square));
            this.element = element;

            label.setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);
        }
    }
}
