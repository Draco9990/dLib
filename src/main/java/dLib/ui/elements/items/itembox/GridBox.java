package dLib.ui.elements.items.itembox;

import dLib.ui.elements.UIElement;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

import java.io.Serializable;

public class GridBox extends GridItemBox<UIElement> {
    public GridBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        disableItemWrapping();
    }

    public GridBox(GridBoxData data){
        super(data);

        disableItemWrapping(); //TODO move to data after exposing
    }

    @Override
    public UIElement makeUIForItem(UIElement item) {
        return item;
    }

    public static class GridBoxData extends GridItemBoxData implements Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public UIElement makeUIElement_internal() {
            return new GridBox(this);
        }
    }
}
