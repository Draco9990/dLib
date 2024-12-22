package dLib.ui.elements.components;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Interactable;
import dLib.ui.resources.UICommonResources;

import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.util.UUID;

public class UIResizeableComponent extends UIElementComponent<UIElement> {
    private ResizeNode[] cornerResizeNodes = new ResizeNode[4];

    private UUID onHoveredEventId;
    private UUID onUnHoveredEventId;

    private UIElement owner;

    public UIResizeableComponent(UIElement inOwner){
        this.owner = inOwner;

        //Bottom left
        cornerResizeNodes[0] = new ResizeNode(Pos.px(-15), Pos.px(-15), Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.BOTTOM);
        UIDraggableComponent blDraggable = cornerResizeNodes[0].getComponent(UIDraggableComponent.class);
        blDraggable.onDraggedEvent.subscribeManaged(() -> {
            int worldCx = cornerResizeNodes[0].getWorldPositionCenteredX();
            int worldCy = cornerResizeNodes[0].getWorldPositionCenteredY();

            int elementWorldX = owner.getWorldPositionX();
            int elementWorldY = owner.getWorldPositionY();

            int offsetX = worldCx - elementWorldX;
            int offsetY = worldCy - elementWorldY;
            owner.offset(offsetX, offsetY);
            owner.resizeBy(-offsetX, -offsetY);
        });

        //Bottom right
        cornerResizeNodes[1] = new ResizeNode(Pos.px(-15), Pos.px(-15), Alignment.HorizontalAlignment.RIGHT, Alignment.VerticalAlignment.BOTTOM);
        UIDraggableComponent brDraggable = cornerResizeNodes[1].getComponent(UIDraggableComponent.class);
        brDraggable.onDraggedEvent.subscribeManaged(() -> {
            int worldCx = cornerResizeNodes[1].getWorldPositionCenteredX();
            int worldCy = cornerResizeNodes[1].getWorldPositionCenteredY();

            int elementWorldX = owner.getWorldPositionX() + owner.getWidth();
            int elementWorldY = owner.getWorldPositionY();

            int offsetX = worldCx - elementWorldX;
            int offsetY = worldCy - elementWorldY;
            owner.offset(0, offsetY);
            owner.resizeBy(offsetX, -offsetY);
        });

        //Top left
        cornerResizeNodes[2] = new ResizeNode(Pos.px(-15), Pos.px(-15), Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.TOP);
        UIDraggableComponent tlDraggable = cornerResizeNodes[2].getComponent(UIDraggableComponent.class);
        tlDraggable.onDraggedEvent.subscribeManaged(() -> {
            int worldCx = cornerResizeNodes[2].getWorldPositionCenteredX();
            int worldCy = cornerResizeNodes[2].getWorldPositionCenteredY();

            int elementWorldX = owner.getWorldPositionX();
            int elementWorldY = owner.getWorldPositionY() + owner.getHeight();

            int offsetX = worldCx - elementWorldX;
            int offsetY = worldCy - elementWorldY;
            owner.offset(offsetX, 0);
            owner.resizeBy(-offsetX, offsetY);
        });

        //Top right
        cornerResizeNodes[3] = new ResizeNode(Pos.px(-15), Pos.px(-15), Alignment.HorizontalAlignment.RIGHT, Alignment.VerticalAlignment.TOP);
        UIDraggableComponent trDraggable = cornerResizeNodes[3].getComponent(UIDraggableComponent.class);
        trDraggable.onDraggedEvent.subscribeManaged(() -> {
            int worldCx = cornerResizeNodes[3].getWorldPositionCenteredX();
            int worldCy = cornerResizeNodes[3].getWorldPositionCenteredY();

            int elementWorldX = owner.getWorldPositionX() + owner.getWidth();
            int elementWorldY = owner.getWorldPositionY() + owner.getHeight();

            int offsetX = worldCx - elementWorldX;
            int offsetY = worldCy - elementWorldY;
            owner.resizeBy(offsetX, offsetY);
        });
    }

    @Override
    public void onRegisterComponent(UIElement owner) {
        super.onRegisterComponent(owner);

        onHoveredEventId = owner.onHoveredEvent.subscribeManaged(() -> {
            for (ResizeNode node : cornerResizeNodes) {
                if(node.pendingRemoval){
                    node.pendingRemoval = false;
                    continue;
                }

                owner.addChildNCS(node);
                node.showAndEnableInstantly();
            }
        });

        onUnHoveredEventId = owner.onUnhoveredEvent.subscribeManaged(() -> {
            for (ResizeNode node : cornerResizeNodes) {
                node.pendingRemoval = true;
            }
        });
    }

    @Override
    public void onUnregisterComponent(UIElement owner) {
        super.onUnregisterComponent(owner);

        owner.onHoveredEvent.unsubscribeManaged(onHoveredEventId);
        owner.onUnhoveredEvent.unsubscribeManaged(onUnHoveredEventId);
    }

    private static class ResizeNode extends Interactable {
        public boolean pendingRemoval = false;

        public ResizeNode(AbstractPosition xPos, AbstractPosition yPos, Alignment.HorizontalAlignment hAlign, Alignment.VerticalAlignment vAlign) {
            super(Tex.stat(UICommonResources.white_pixel), xPos, yPos, Dim.px(30), Dim.px(30));

            setRenderColor(Color.RED);

            setHorizontalAlignment(hAlign);
            setVerticalAlignment(vAlign);

            setContainerBoundCalculationType(BoundCalculationType.CONTAINS_HALF);

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
