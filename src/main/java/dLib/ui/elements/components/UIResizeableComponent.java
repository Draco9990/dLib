package dLib.ui.elements.components;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.themes.UITheme;
import dLib.ui.themes.UIThemeManager;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.util.UUID;

public class UIResizeableComponent extends UIElementComponent<UIElement> {
    private ResizeNode[] nodes = new ResizeNode[4];

    private UUID onHoveredEventId;
    private UUID onUnHoveredEventId;

    public UIResizeableComponent(){
        //Bottom left
        nodes[0] = new ResizeNode(Pos.px(-15), Pos.px(-15), Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.BOTTOM);

        //Bottom right
        nodes[1] = new ResizeNode(Pos.px(-15), Pos.px(-15), Alignment.HorizontalAlignment.RIGHT, Alignment.VerticalAlignment.BOTTOM);

        //Top left
        nodes[2] = new ResizeNode(Pos.px(-15), Pos.px(-15), Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.TOP);

        //Top right
        nodes[3] = new ResizeNode(Pos.px(-15), Pos.px(-15), Alignment.HorizontalAlignment.RIGHT, Alignment.VerticalAlignment.TOP);
    }

    @Override
    public void onRegisterComponent(UIElement owner) {
        super.onRegisterComponent(owner);

        onHoveredEventId = owner.addOnHoveredEvent(() -> {
            for (ResizeNode node : nodes) {
                if(node.pendingRemoval){
                    node.pendingRemoval = false;
                    continue;
                }

                owner.addChildNCS(node);
                node.showAndEnableInstantly();
            }
        });

        onUnHoveredEventId = owner.addOnUnHoveredEvent(() -> {
            for (ResizeNode node : nodes) {
                node.pendingRemoval = true;
            }
        });
    }

    @Override
    public void onUnregisterComponent(UIElement owner) {
        super.onUnregisterComponent(owner);

        owner.removeOnHoveredEvent(onHoveredEventId);
        owner.removeOnUnHoveredEvent(onUnHoveredEventId);
    }

    private static class ResizeNode extends Interactable {
        public boolean pendingRemoval = false;

        public ResizeNode(AbstractPosition xPos, AbstractPosition yPos, Alignment.HorizontalAlignment hAlign, Alignment.VerticalAlignment vAlign) {
            super(UITheme.whitePixel, xPos, yPos, Dim.px(30), Dim.px(30));

            setRenderColor(Color.RED);

            setHorizontalAlignment(hAlign);
            setVerticalAlignment(vAlign);

            addComponent(new UIDraggableComponent());
        }

        @Override
        protected void updateSelf() {
            super.updateSelf();

            if(pendingRemoval && !isHovered() && !isHeld()){
                getParent().removeChild(this);
                hideAndDisableInstantly();
                pendingRemoval = false;
            }
        }
    }
}
