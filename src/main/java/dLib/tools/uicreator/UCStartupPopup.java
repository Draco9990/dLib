package dLib.tools.uicreator;

import dLib.ui.animations.entry.UIAnimation_SlideInUp;
import dLib.ui.animations.exit.UIAnimation_SlideOutDown;
import dLib.ui.elements.items.DarkenLayer;
import dLib.ui.elements.items.Renderable;
import dLib.ui.elements.items.buttons.CancelButtonSmall;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.position.Pos;

public class UCStartupPopup extends DarkenLayer {
    CancelButtonSmall cancelButton;
    Renderable popup;

    public UCStartupPopup(){
        super();

        popup = new Renderable(Tex.stat(UICommonResources.background_medium));
        {
            cancelButton = new CancelButtonSmall(Pos.px(360), Pos.px(1080-875));
            popup.addChild(cancelButton);
        }
        popup.setEntryAnimation(new UIAnimation_SlideInUp(popup));
        popup.setExitAnimation(new UIAnimation_SlideOutDown(popup));
        addChild(popup);
    }
}
