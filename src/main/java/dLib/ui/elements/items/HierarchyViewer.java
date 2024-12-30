package dLib.ui.elements.items;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIDropZoneComponent;
import dLib.ui.elements.components.UIPayloadComponent;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;

import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

import java.util.UUID;

public class HierarchyViewer extends VerticalBox {
    private UIElement forElement;

    private boolean allowReordering = false;

    private UUID hierarchyViewerID = UUID.randomUUID();

    public HierarchyViewer() {
        super(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());

        disableItemWrapping();
    }

    public void loadForElement(UIElement element){
        clearItems();
        if(element == null) return;

        this.forElement = element;
        recursivelyAddChild(element, 0);
    }

    private void recursivelyAddChild(UIElement child, int level){
        HorizontalBox currentLevelBox = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(30));
        {
            currentLevelBox.addItem(new Spacer(Dim.px(20 * level), Dim.fill()));
            currentLevelBox.addItem(makeHierarchyViewerElementButton_internal(child));
        }
        addItem(currentLevelBox);

        if(!child.getChildren().isEmpty()){
            for(UIElement grandChild : child.getChildren()){
                recursivelyAddChild(grandChild, level + 1);
            }
        }
    }

    protected HierarchyViewerChildElementButton makeHierarchyViewerElementButton_internal(UIElement element){
        HierarchyViewerChildElementButton button = makeHierarchyViewerElementButton(element);

        if(allowsReordering()){
            button.addComponent(new UIPayloadComponent<UIElement>(button, element, "hierarchyViewer" + hierarchyViewerID));

            UIDropZoneComponent<UIElement> dropZoneComponent = button.addComponent(new UIDropZoneComponent<UIElement>(button, "hierarchyViewer" + hierarchyViewerID));
            dropZoneComponent.onPayloadDroppedEvent.subscribe(this, (payload) -> {
                payload.reparent(element);
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

    //endregion

    protected static class HierarchyViewerChildElementButton extends TextButton {
        protected UIElement element;

        public HierarchyViewerChildElementButton(UIElement element) {
            super(element.getId(), Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(30));
            setImage(Tex.stat(UICommonResources.itembox_itembg_horizontal));
            this.element = element;
        }
    }
}
