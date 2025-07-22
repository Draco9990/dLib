package dLib.tools.uicreator;

import dLib.external.ExternalMessageSender;
import dLib.ui.animations.entry.UIAnimation_SlideInUp;
import dLib.ui.animations.exit.UIAnimation_SlideOutDown;
import dLib.ui.elements.items.DarkenLayer;
import dLib.ui.elements.items.templateinput.GenericInputWindow;
import dLib.ui.elements.items.Renderable;
import dLib.ui.elements.items.buttons.CancelButtonSmall;
import dLib.ui.elements.items.buttons.ConfirmButtonSmall;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.position.Pos;

public class UCStartupPopup extends DarkenLayer {
    CancelButtonSmall cancelButton;
    ConfirmButtonSmall confirmButton;
    Renderable popup;

    public UCStartupPopup(){
        super();

        popup = new Renderable(Tex.stat(UICommonResources.bg01));
        {
            cancelButton = new CancelButtonSmall(Pos.px(360), Pos.px(1080-820));
            cancelButton.postLeftClickEvent.subscribe(cancelButton, () -> getTopParent().close());
            popup.addChild(cancelButton);

            confirmButton = new ConfirmButtonSmall(Pos.px(1402), Pos.px(1080-820));
            confirmButton.label.setText("New");
            confirmButton.postLeftClickEvent.subscribe(confirmButton, () -> {
                GenericInputWindow inputWindow = new GenericInputWindow("Enter Name:", "Create");
                inputWindow.onConfirmEvent.subscribe(inputWindow, s -> {
                    ExternalMessageSender.send_createNewUIElement(s);

                    inputWindow.close();
                    UCStartupPopup.this.close();
                });
                inputWindow.open();
            });
            popup.addChild(confirmButton);
        }
        popup.setEntryAnimation(new UIAnimation_SlideInUp(popup));
        popup.setExitAnimation(new UIAnimation_SlideOutDown(popup));
        addChild(popup);
    }
}
