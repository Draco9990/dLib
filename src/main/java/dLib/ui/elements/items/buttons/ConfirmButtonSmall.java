package dLib.ui.elements.items.buttons;

import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;

public class ConfirmButtonSmall extends TextButton {
    public ConfirmButtonSmall(AbstractPosition xPos, AbstractPosition yPos) {
        super("Confirm", xPos, yPos, Dim.px(173), Dim.px(75));

        setTexture(Tex.stat(UICommonResources.confirmButtonSmall));
        label.setFontSize(18);
    }
}
