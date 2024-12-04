package dLib.ui.elements.prefabs;

import dLib.ui.elements.UIElement;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.Pos;

public class Spacer extends UIElement {
    public Spacer(AbstractDimension width, AbstractDimension height) {
        super(Pos.px(0), Pos.px(0), width, height);
    }
}
