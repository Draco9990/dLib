package dLib.ui.mousestates;

import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.mousestates.AbstractMouseState;
import dLib.ui.elements.UIElement;

public class DragAndDropMouseState<PayloadType> extends AbstractMouseState {
    private PayloadType payload;
    private String payloadZoneId;

    public DragAndDropMouseState(PayloadType payload, String payloadZoneId) {
        super("dragAndDrop");

        this.payload = payload;
        this.payloadZoneId = payloadZoneId;
    }

    @Override
    public void update() {
        super.update();

        if(!InputHelper.isMouseDown){
            //TODO check if hovering over a payload drop zone and if so, drop the payload
            exitMouseState();
        }
    }

    public PayloadType getPayload() {
        return payload;
    }

    public String getPayloadZoneId() {
        return payloadZoneId;
    }
}
