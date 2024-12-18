package dLib.ui.elements.components;

import dLib.tools.uidebugger.ui.UIDebuggerScreen;
import dLib.ui.elements.UIElement;
import dLib.ui.screens.UIManager;

import java.util.UUID;

public class UIDebuggableComponent extends UIElementComponent{
    private boolean initialized = false;

    private UUID onHoverEventID;
    private UUID onUnHoverEventID;

    private UUID onLeftClickEventID;

    @Override
    public void onUpdate(UIElement owner) {
        super.onUpdate(owner);

        //defer initialization until the component has fully initialized itself

        if(owner.getTopParent() instanceof UIDebuggerScreen){
            return;
        }

        if(!initialized){
            initialized = true;

            onHoverEventID = owner.onHoveredEvent.subscribeManaged(() -> {
                UIDebuggerScreen debuggerScreen = getUIDebuggerScreen();
                if(debuggerScreen != null){
                    debuggerScreen.elementList.elementInfo.textBox.setText(generateDebugTextForUIElement(owner));
                }
            });

            onUnHoverEventID = owner.onUnhoveredEvent.subscribeManaged(() -> {
                UIDebuggerScreen debuggerScreen = getUIDebuggerScreen();
                if(debuggerScreen != null){
                    debuggerScreen.elementList.elementInfo.textBox.setText("");
                }
            });

            onLeftClickEventID = owner.onLeftClickEvent.subscribeManaged(() -> {
                System.out.println("Left clicked on " + owner);
            });
        }
    }

    @Override
    public void onUnregisterComponent(UIElement owner) {
        super.onUnregisterComponent(owner);

        if(onHoverEventID != null){
            owner.onHoveredEvent.unsubscribeManaged(onHoverEventID);
        }
        if(onUnHoverEventID != null){
            owner.onUnhoveredEvent.unsubscribeManaged(onUnHoverEventID);
        }

        if(onLeftClickEventID != null){
            owner.onLeftClickEvent.unsubscribeManaged(onLeftClickEventID);
        }
    }

    public UIDebuggerScreen getUIDebuggerScreen(){
        return UIManager.getOpenElementOfType(UIDebuggerScreen.class);
    }

    private String generateDebugTextForUIElement(UIElement element){
        String cachedPosition = " Cached(" + (element.getLocalPositionXCache() == null ? "null" : element.getLocalPositionXCache()) + ", " + (element.getLocalPositionYCache() == null ? "null" : element.getLocalPositionYCache()) + ")";
        String cachedDimensions = " Cached(" + (element.getWidthCache() == null ? "null" : element.getWidthCache()) + ", " + (element.getHeightCache() == null ? "null" : element.getHeightCache()) + ")";

        return "Element ID: " + element.getId() + "\n" +
                "Position: " + element.getLocalPositionXRaw().toString() + ", " + element.getLocalPositionYRaw().toString() + cachedPosition + "\n" +
                "Dimensions: " + element.getWidthRaw().toString() + ", " + element.getHeightRaw().toString() + cachedDimensions + "\n" +
                "Visible: " + element.isVisible() + "\n" +
                "Enabled: " + element.isEnabled() + "\n" +
                "Parent: " + (element.getParent() == null ? "null" : element.getParent().getId()) + "\n" +
                "Children: " + element.getChildren().size() + "\n" +
                "Alignment: " + element.getAlignment();
    }
}
