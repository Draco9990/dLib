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

            onHoverEventID = owner.addOnHoveredEvent(() -> {
                UIDebuggerScreen debuggerScreen = getUIDebuggerScreen();
                if(debuggerScreen != null){
                    debuggerScreen.elementList.elementInfo.setText(generateDebugTextForUIElement(owner));
                }
            });

            onUnHoverEventID = owner.addOnUnHoveredEvent(() -> {
                UIDebuggerScreen debuggerScreen = getUIDebuggerScreen();
                if(debuggerScreen != null){
                    debuggerScreen.elementList.elementInfo.setText("");
                }
            });

            onLeftClickEventID = owner.addOnLeftClickEvent(() -> {
                System.out.println("Left clicked on " + owner);
            });
        }
    }

    @Override
    public void onUnregisterComponent(UIElement owner) {
        super.onUnregisterComponent(owner);

        if(onHoverEventID != null){
            owner.removeOnHoveredEvent(onHoverEventID);
        }
        if(onUnHoverEventID != null){
            owner.removeOnUnHoveredEvent(onUnHoverEventID);
        }

        if(onLeftClickEventID != null){
            owner.removeOnLeftClickEvent(onLeftClickEventID);
        }
    }

    public UIDebuggerScreen getUIDebuggerScreen(){
        return UIManager.getOpenScreenOfType(UIDebuggerScreen.class);
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
