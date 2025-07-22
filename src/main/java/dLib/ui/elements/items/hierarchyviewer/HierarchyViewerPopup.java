package dLib.ui.elements.items.hierarchyviewer;

import com.badlogic.gdx.math.Vector2;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIDraggableComponent;
import dLib.ui.elements.items.SimpleListPicker;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.scroll.Scrollbox;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.ui.UIManager;
import dLib.util.bindings.texture.Tex;
import dLib.util.helpers.UIHelpers;
import dLib.util.ui.dimensions.Dim;

import java.util.ArrayList;

public class HierarchyViewerPopup extends VerticalBox {
    public HierarchyViewerPopup(HierarchyViewer hierarchyViewer) {
        super(Dim.px(500), Dim.px(400));

        addComponent(new UIDraggableComponent());

        setTexture(Tex.stat(UICommonResources.button02_square));

        TextButton elementSelectionButton = new TextButton("Select Element", Dim.fill(), Dim.px(40));
        elementSelectionButton.postLeftClickEvent.subscribe(elementSelectionButton, () -> {
            Vector2 mousePos = UIHelpers.getMouseWorldPosition();

            ArrayList<UIElement> openElements = UIManager.getOpenElements();
            openElements.removeIf(element -> element.equals(HierarchyViewerPopup.this));

            SimpleListPicker<UIElement> picker = new SimpleListPicker<>(mousePos.x, mousePos.y, openElements);
            picker.onOptionSelectedEvent.subscribe(picker, selectedElement -> {
                hierarchyViewer.loadForElement(selectedElement);
            });
            picker.open();
        });
        addChild(elementSelectionButton);

        Scrollbox scrollbox = new Scrollbox(Dim.fill(), Dim.fill());
        scrollbox.setIsHorizontal(true);
        {
            scrollbox.addChild(hierarchyViewer);
        }
        addChild(scrollbox);
    }
}
