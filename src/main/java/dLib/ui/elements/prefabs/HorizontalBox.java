package dLib.ui.elements.prefabs;

import dLib.ui.elements.UIElement;
import dLib.ui.util.ESelectionMode;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class HorizontalBox extends HorizontalListBox<UIElement> {
    //region Variables

    //endregion

    //region Constructors

    public HorizontalBox(AbstractPosition xPos, AbstractPosition yPos) {
        this(xPos, yPos, Dim.fill(), Dim.fill());
    }
    public HorizontalBox(AbstractDimension width, AbstractDimension height) {
        this(Pos.perc(0), Pos.perc(0), width, height);
    }
    public HorizontalBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        disableItemWrapping();

        setSelectionMode(ESelectionMode.NONE);
        setImage(null);
    }

    public HorizontalBox(HorizontalListBoxData data) {
        super(data);
    }

    //endregion

    //region Methods

    @Override
    public UIElement makeUIForItem(UIElement item) {
        return item;
    }

    //endregion
}
