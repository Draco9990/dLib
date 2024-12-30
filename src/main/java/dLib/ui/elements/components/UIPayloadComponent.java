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

import java.util.function.Consumer;

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

                DragAndDropMouseState<PayloadType> dragAndDropState = new DragAndDropMouseState<>(payload, dropZoneId);
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
