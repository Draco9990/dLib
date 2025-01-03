package dLib.ui.elements.components;

import dLib.mousestates.MouseStateManager;
import dLib.ui.elements.UIElement;
import dLib.ui.mousestates.DragAndDropMouseState;

public class UIPayloadComponent<PayloadType> extends UIElementComponent<UIElement> {
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

        owner.onLeftClickHeldEvent.subscribe(this, (heldTimeSeconds) -> {
            if(heldTimeSeconds > 0.33){
                if(MouseStateManager.get().isInExternalState()){
                    return;
                }

                if(payloading){
                    return;
                }

                DragAndDropMouseState<PayloadType> dragAndDropState = new DragAndDropMouseState<>(payload, owner, dropZoneId);
                MouseStateManager.get().enterMouseState(dragAndDropState);
                payloading = true;
            }
        });

        owner.onLeftClickReleaseEvent.subscribe(this, () -> {
            if(payloading){
                payloading = false;
            }
        });
    }

    @Override
    public void onUnregisterComponent(UIElement owner) {
        super.onUnregisterComponent(owner);

        owner.onLeftClickHeldEvent.unsubscribe(this);
        owner.onLeftClickReleaseEvent.unsubscribe(this);
    }
}
