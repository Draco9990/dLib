package dLib.ui.elements.components;

import dLib.mousestates.AbstractMouseState;
import dLib.mousestates.MouseStateManager;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Image;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.ui.mousestates.DragAndDropMouseState;
import dLib.ui.mousestates.events.PostEnterMouseStateEvent;
import dLib.ui.mousestates.events.PreExitMouseStateEvent;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.Event;
import dLib.util.events.GlobalEvents;
import dLib.util.ui.dimensions.Dim;

import java.util.Objects;
import java.util.function.Consumer;

public class UIDropZoneComponent<DropObjectType> extends UIElementComponent<UIElement> {
    private UIElement owner;
    private String dropZoneId;

    private boolean hoveringWithPayload = false;
    private ImageTextBox payloadOverlay;

    private Image dropZoneOverlay;

    public Event<Consumer<DropObjectType>> onPayloadDroppedEvent = new Event<>();

    public UIDropZoneComponent(UIElement owner, String dropZoneId) {
        this.owner = owner;
        this.dropZoneId = dropZoneId;
    }

    @Override
    public void onRegisterComponent(UIElement owner) {
        super.onRegisterComponent(owner);

        owner.onHoveredEvent.subscribe(this, () -> {
            AbstractMouseState currentState = MouseStateManager.get().getCurrentState();
            if(!(currentState instanceof DragAndDropMouseState)){
                return;
            }

            DragAndDropMouseState dragAndDropState = (DragAndDropMouseState) currentState;
            if(dragAndDropState.getPayloadZoneId().equals(dropZoneId) && !Objects.equals(dragAndDropState.getSource(), owner)){
                hoveringWithPayload = true;

                payloadOverlay = new ImageTextBox("Drop here!", Dim.fill(), Dim.fill());
                payloadOverlay.setImage(Tex.stat(UICommonResources.dropZoneBg));
                payloadOverlay.setPassthrough(true);
                owner.addChild(payloadOverlay);
            }
        });

        owner.onUnhoveredEvent.subscribe(this, () -> {
            hoveringWithPayload = false;

            if(payloadOverlay != null){
                payloadOverlay.dispose();
                payloadOverlay = null;
            }
        });

        GlobalEvents.subscribe(this, PostEnterMouseStateEvent.class, postEnterMouseStateEvent -> {
            if(!(postEnterMouseStateEvent.mouseState instanceof DragAndDropMouseState)){
                return;
            }

            DragAndDropMouseState dragAndDropState = (DragAndDropMouseState) postEnterMouseStateEvent.mouseState;
            if(dragAndDropState.getPayloadZoneId().equals(dropZoneId) && !Objects.equals(dragAndDropState.getSource(), owner)){
                dropZoneOverlay = new Image(Tex.stat(UICommonResources.dropZoneOptionBg), Dim.fill(), Dim.fill());
                dropZoneOverlay.setPassthrough(true);
                owner.addChild(dropZoneOverlay);
            }
        });

        GlobalEvents.subscribe(this, PreExitMouseStateEvent.class, preExitMouseStateEvent -> {
            if(!(preExitMouseStateEvent.mouseState instanceof DragAndDropMouseState)){
                return;
            }

            DragAndDropMouseState dragAndDropState = (DragAndDropMouseState) preExitMouseStateEvent.mouseState;
            if(dragAndDropState.getPayloadZoneId().equals(dropZoneId) && !Objects.equals(dragAndDropState.getSource(), owner)){
                if(dropZoneOverlay != null){
                    dropZoneOverlay.dispose();
                    dropZoneOverlay = null;
                }

                if(owner.isHovered()){
                    onPayloadDroppedEvent.invoke(dropObjectTypeConsumer -> dropObjectTypeConsumer.accept((DropObjectType) dragAndDropState.getPayload()));
                }
            }
        });
    }

    @Override
    public void onUnregisterComponent(UIElement owner) {
        super.onUnregisterComponent(owner);

        owner.onHoveredEvent.unsubscribe(this);
        owner.onUnhoveredEvent.unsubscribe(this);

        GlobalEvents.unsubscribe(PostEnterMouseStateEvent.class, this);
        GlobalEvents.unsubscribe(PreExitMouseStateEvent.class, this);
    }
}
