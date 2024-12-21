package dLib.ui.elements.items.itembox;

import dLib.ui.elements.UIElement;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

public class GridBox extends GridItemBox<UIElement> {
    public GridBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        disableItemWrapping();
    }

    @Override
    public UIElement makeUIForItem(UIElement item) {
        return item;
    }
}
