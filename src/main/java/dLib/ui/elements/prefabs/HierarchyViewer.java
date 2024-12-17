package dLib.ui.elements.prefabs;

import dLib.ui.elements.UIElement;
import dLib.ui.resources.UICommonResources;

import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class HierarchyViewer extends VerticalBox {
    public HierarchyViewer() {
        super(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());

        disableItemWrapping();
    }

    public void loadForElement(UIElement element){
        clearItems();
        if(element == null) return;

        recursivelyAddChild(element, 0);
    }

    private void recursivelyAddChild(UIElement child, int level){
        HorizontalBox currentLevelBox = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(30));
        {
            currentLevelBox.addItem(new Spacer(Dim.px(20 * level), Dim.fill()));
            currentLevelBox.addItem(makeHierarchyViewerElementButton(child));
        }
        addItem(currentLevelBox);

        if(!child.getChildren().isEmpty()){
            for(UIElement grandChild : child.getChildren()){
                recursivelyAddChild(grandChild, level + 1);
            }
        }
    }

    protected HierarchyViewerChildElementButton makeHierarchyViewerElementButton(UIElement element){
        return new HierarchyViewerChildElementButton(element);
    }

    protected static class HierarchyViewerChildElementButton extends TextButton {
        protected UIElement element;

        public HierarchyViewerChildElementButton(UIElement element) {
            super(element.getId(), Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(30));
            getButton().setImage(Tex.stat(UICommonResources.itembox_itembg_horizontal));
            this.element = element;
        }
    }
}
