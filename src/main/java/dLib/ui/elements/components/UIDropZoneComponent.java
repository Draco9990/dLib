package dLib.ui.elements.components;

import dLib.mousestates.AbstractMouseState;
import dLib.mousestates.MouseStateManager;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Image;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.ui.mousestates.DragAndDropMouseState;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.ui.dimensions.Dim;

import java.util.Objects;

public class UIDropZoneComponent<DropObjectType> extends AbstractUIElementComponent<UIElement> {
    private UIElement owner;
    private String dropZoneId;

    private boolean hoveringWithPayload = false;
    private ImageTextBox payloadOverlay;

    private Image dropZoneOverlay;

    public ConsumerEvent<DropObjectType> onPayloadDroppedEvent = new ConsumerEvent<>();

    public UIDropZoneComponent(UIElement owner, String dropZoneId) {
        this.owner = owner;
        this.dropZoneId = dropZoneId;
    }

    @Override
    public void onRegisterComponent(UIElement owner) {
        super.onRegisterComponent(owner);

        owner.postHoveredEvent.subscribe(this, () -> {
            AbstractMouseState currentState = MouseStateManager.getCurrentState();
            if(!(currentState instanceof DragAndDropMouseState)){
                return;
            }

            DragAndDropMouseState dragAndDropState = (DragAndDropMouseState) currentState;
            if(dragAndDropState.getPayloadZoneId().equals(dropZoneId) && !Objects.equals(dragAndDropState.getSource(), owner)){
                hoveringWithPayload = true;

                payloadOverlay = new ImageTextBox("Drop here!", Dim.fill(), Dim.fill());
                payloadOverlay.setTexture(Tex.stat(UICommonResources.dropZoneBg));
                payloadOverlay.setPassthrough(true);
                owner.addChild(payloadOverlay);
            }
        });

        owner.postUnhoveredEvent.subscribe(this, () -> {
            hoveringWithPayload = false;

            if(payloadOverlay != null){
                payloadOverlay.dispose();
                payloadOverlay = null;
            }
        });

        AbstractMouseState.postStateEnterGlobalEvent.subscribe(this, mouseState -> {
            if(!(mouseState instanceof DragAndDropMouseState)){
                return;
            }

            DragAndDropMouseState dragAndDropState = (DragAndDropMouseState) mouseState;
            if(dragAndDropState.getPayloadZoneId().equals(dropZoneId) && !Objects.equals(dragAndDropState.getSource(), owner)){
                dropZoneOverlay = new Image(Tex.stat(UICommonResources.dropZoneOptionBg), Dim.fill(), Dim.fill());
                dropZoneOverlay.setPassthrough(true);
                owner.addChild(dropZoneOverlay);
            }
        });

        AbstractMouseState.preStateExitGlobalEvent.subscribe(this, mouseState -> {
            if(!(mouseState instanceof DragAndDropMouseState)){
                return;
            }

            DragAndDropMouseState dragAndDropState = (DragAndDropMouseState) mouseState;
            if(dragAndDropState.getPayloadZoneId().equals(dropZoneId) && !Objects.equals(dragAndDropState.getSource(), owner)){
                if(dropZoneOverlay != null){
                    dropZoneOverlay.dispose();
                    dropZoneOverlay = null;
                }

                if(owner.isHovered()){
                    onPayloadDroppedEvent.invoke((DropObjectType) dragAndDropState.getPayload());
                }
            }
        });
    }

    @Override
    public void onUnregisterComponent(UIElement owner) {
        super.onUnregisterComponent(owner);

        owner.postHoveredEvent.unsubscribe(this);
        owner.postUnhoveredEvent.unsubscribe(this);
    }
}
