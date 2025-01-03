package dLib.ui.elements.items.buttons;

import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;

public class CancelButtonSmall extends TextButton {
    public CancelButtonSmall(AbstractPosition xPos, AbstractPosition yPos) {
        super("Cancel", xPos, yPos, Dim.px(161), Dim.px(74));

        setImage(Tex.stat(UICommonResources.cancelButtonSmall));
        label.setFontSize(16);
    }
}
