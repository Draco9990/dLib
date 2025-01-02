package dLib.test;

import com.badlogic.gdx.graphics.Color;
import dLib.mousestates.MouseStateManager;
import dLib.ui.elements.UIElement;

import dLib.ui.elements.components.UIDropZoneComponent;
import dLib.ui.elements.items.Image;
import dLib.ui.mousestates.DragAndDropMouseState;
import dLib.ui.resources.UICommonResources;
import dLib.util.DLibLogger;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class TestScreen extends UIElement {

    public TestScreen(){
        super(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());

        Image payloadStart = new Image(Tex.stat(UICommonResources.white_pixel), Pos.px(50), Pos.px(50), Dim.px(150), Dim.px(150));
        payloadStart.onLeftClickEvent.subscribe(payloadStart, new Runnable() {
            @Override
            public void run() {
                DragAndDropMouseState<String> state = new DragAndDropMouseState<>("payload", payloadStart, "dropZone");
                MouseStateManager.get().enterMouseState(state);
            }
        });
        addChild(payloadStart);

        Image payloadEnd = new Image(Tex.stat(UICommonResources.white_pixel), Pos.px(350), Pos.px(350), Dim.px(150), Dim.px(150));
        payloadEnd.setRenderColor(Color.RED);
        UIDropZoneComponent<String> comp = payloadEnd.addComponent(new UIDropZoneComponent<String>(payloadEnd, "dropZone"));
        comp.onPayloadDroppedEvent.subscribe(this, s -> DLibLogger.log("Payload dropped: " + s));
        addChild(payloadEnd);
    }
}
