package dLib.ui.elements.components;

import dLib.mousestates.MouseStateManager;
import dLib.ui.elements.UIElement;
import dLib.ui.mousestates.DragAndDropMouseState;

public class UIPayloadComponent<PayloadType> extends AbstractUIElementComponent<UIElement> {
    private UIElement owner;
    private PayloadType payload;
    private String dropZoneId;

    private boolean payloading = false;

    public UIPayloadComponent(UIElement owner, PayloadType payload, String dropZoneId) {
        this.owner = owner;
        this.payload = payload;
        this.dropZoneId = dropZoneId;
    }

    @Override
    public void onRegisterComponent(UIElement owner) {
        super.onRegisterComponent(owner);

        owner.postLeftClickHeldEvent.subscribe(this, (heldTimeSeconds) -> {
            if(heldTimeSeconds > 0.33){
                if(MouseStateManager.isInExternalState()){
                    return;
                }

                if(payloading){
                    return;
                }

                DragAndDropMouseState<PayloadType> dragAndDropState = new DragAndDropMouseState<>(payload, owner, dropZoneId);
                MouseStateManager.enterMouseState(dragAndDropState);
                payloading = true;
            }
        });

        owner.postLeftClickReleaseEvent.subscribe(this, () -> {
            if(payloading){
                payloading = false;
            }
        });
    }

    @Override
    public void onUnregisterComponent(UIElement owner) {
        super.onUnregisterComponent(owner);

        owner.postLeftClickHeldEvent.unsubscribe(this);
        owner.postLeftClickReleaseEvent.unsubscribe(this);
    }
}
