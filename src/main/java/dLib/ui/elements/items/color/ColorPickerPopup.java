package dLib.ui.elements.items.color;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.ui.elements.items.Renderable;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class ColorPickerPopup extends Renderable {
    public ColorWheel colorWheel;

    public ColorPickerPopup(AbstractPosition xPos, AbstractPosition yPos) {
        super(Tex.stat(ImageMaster.OPTION_CONFIRM), xPos, yPos, Dim.px(438), Dim.px(340));

        setContextual(true);

        colorWheel = new ColorWheel(Pos.px(36), Pos.px(149), Dim.px(156), Dim.px(156));
        addChildNCS(colorWheel);
    }
}
