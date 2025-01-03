package dLib.ui.mousestates;

import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.mousestates.AbstractMouseState;

public class DragAndDropMouseState<PayloadType> extends AbstractMouseState {
    private Object source;
    private PayloadType payload;
    private String payloadZoneId;

    public DragAndDropMouseState(PayloadType payload, Object source, String payloadZoneId) {
        super("dragAndDrop");

        this.source = source;
        this.payload = payload;
        this.payloadZoneId = payloadZoneId;
    }

    @Override
    public void update() {
        super.update();

        if(!InputHelper.isMouseDown){
            //* Actual drop logic is in UIDropZoneComponent
            exitMouseState();
        }
    }

    public Object getSource() {
        return source;
    }

    public PayloadType getPayload() {
        return payload;
    }

    public String getPayloadZoneId() {
        return payloadZoneId;
    }
}
