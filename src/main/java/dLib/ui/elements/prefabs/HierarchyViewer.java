package dLib.ui.elements.prefabs;

import dLib.ui.elements.UIElement;
import dLib.ui.themes.UIThemeManager;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class HierarchyViewer extends VerticalBox {
    public HierarchyViewer() {
        super(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
    }

    public void loadForElement(UIElement element){
        clearItems();
        if(element == null) return;

        recursivelyAddChild(element, this);
    }

    private void recursivelyAddChild(UIElement child, VerticalListBox<UIElement> currentLevel){
        HierarchyViewerChildElementButton childButton = new HierarchyViewerChildElementButton(child);
        currentLevel.addItem(childButton);

        if(!child.getChildren().isEmpty()){
            HorizontalBox childLevel = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
            {
                childLevel.addItem(new Spacer(Dim.px(20), Dim.fill()));

                VerticalBox childList = new VerticalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
                {
                    for(UIElement grandChild : child.getChildren()){
                        recursivelyAddChild(grandChild, childList);
                    }
                }
                childLevel.addItem(childList);
            }
            currentLevel.addItem(childLevel);
        }
    }

    private static class HierarchyViewerChildElementButton extends TextButton {
        private UIElement element;

        public HierarchyViewerChildElementButton(UIElement element) {
            super(element.getId(), Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(30));
            getButton().setImage(UIThemeManager.getDefaultTheme().itemBoxVerticalItemBg);
            this.element = element;
        }
    }
}
