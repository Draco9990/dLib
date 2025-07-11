package dLib.tools.uidebugger.ui;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIDraggableComponent;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.util.ui.dimensions.Dim;

public class UIDebuggerScreen extends ImageTextBox {
    public UIDebuggerScreen(){
        super("", Dim.px(600), Dim.px(400));

        addComponent(new UIDraggableComponent());

        UIElement.postHoverGlobalEvent.subscribeManaged((element) -> {
            if(element.getTopParent() instanceof UIDebuggerScreen) return;

            String debugText = generateDebugTextForUIElement(element);
            textBox.setText(debugText);
            showAndEnable();
        });
    }

    //region Subclasses

    private String generateDebugTextForUIElement(UIElement element){
        String cachedPosition = " Cached(" + element.getLocalPositionX() + ", " + element.getLocalPositionY() + ")";
        String cachedDimensions = " Cached(" + element.getWidth() + ", " + element.getHeight() + ")";

        return "Element ID: " + element.getId() + "\n" +
                "Position: " + element.getLocalPositionXRaw().toString() + ", " + element.getLocalPositionYRaw().toString() + cachedPosition + "\n" +
                "Dimensions: " + element.getWidthRaw().toString() + ", " + element.getHeightRaw().toString() + cachedDimensions + "\n" +
                "Visible: " + element.isVisible() + "\n" +
                "Enabled: " + element.isEnabled() + "\n" +
                "Parent: " + (element.getParent() == null ? "null" : element.getParent().getId()) + "\n" +
                "Children: " + element.getChildren().size() + "\n" +
                "Alignment: " + element.getAlignment();
    }

    //endregion
}
